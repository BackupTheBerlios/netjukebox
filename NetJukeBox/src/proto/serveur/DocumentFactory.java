package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;


public class DocumentFactory {
	
//ATTRIBUTS
//-----------------------------------
	
	/**
	 * Instances organis�es par ID
	 */
	private static Hashtable instancesById = new Hashtable();
	
	/**
	 * Instances organis�es par Titre
	 */
	private static Hashtable instancesByTitre = new Hashtable();
	
	/**
	 * Cr�e le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(DocumentFactory.class);
	
//METHODES STATIQUES
//-----------------------------------

	/**
	 * V�rifie s'il y a une instance pour l'ID demand�
	 * @param String key
	 * @return boolean
	 */
	public static boolean containsId(String key) {
		return instancesById.containsKey(key);
	}
	
	/**
	 * V�rifie s'il y a une instance pour le titre demand�
	 * @param String key
	 * @return boolean
	 */
	public static boolean containsTitre(String key) {
		return instancesByTitre.containsKey(key);
	}
	
	/**
	 * Renvoie la liste des instances
	 * @return Hashtable
	 */
	public static Hashtable getInstances() {
		return instancesById;
	}
	
	
	/**
	 * Cr�ation du programme en base
	 * @param String titre
	 * @param int duree
	 * @param String jour
	 * @param String mois
	 * @param String annee
	 * @param String source
	 * @param String langue
	 * @param String genre
	 * @param String fichier
	 * @return Document
	 * @throws SQLException 
	 */
	public static Document create(String titre, int duree, int jour,
			int mois, int annee, String source, String langue, String genre,
			String fichier, String artiste, String interprete, String compositeur) /*throws SQLException*/ {
		
		logger.debug("D�marrage : Document.create");				
		
		//On assemble la date
		//String date = jour+"-"+mois+"-"+annee;
		GregorianCalendar date = new GregorianCalendar(annee, mois, jour);
		
		//On cr�e le document dans la base
		String requete = "INSERT INTO document (titre, duree, date, source, langue, genre, fichier, artiste, interprete, compositeur) VALUES ('" +
			titre.replace("'", "''") + "', '" + duree + "', '" + date.getTimeInMillis() + "', '" + source.replace("'", "''") + "', '" +langue.replace("'", "''") + "', '" +
			genre.replace("'", "''") + "', '" + fichier.replace("'", "''") + "', '"+ artiste.replace("'", "''") + "', '"+ interprete.replace("'", "''") + "', '" + compositeur.replace("'", "''") + "');"; 
		
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien d�roul�e
		if (nbRows>0) {
			//On retourne ensuite un objet pour ce document
			logger.debug("Arr�t: Document.create");
			return getByTitre(titre);
		}
		//Sinon on retourne un objet vide
		logger.debug("Arr�t: Document.create");
		return null;
	}
	
	/**
	 * Instancie un objet document apr�s avoir r�cup�r� ces infos depuis la base � partir de son titre
	 * @param String titre
	 * @return Document
	 * @throws SQLException 
	 */
	public static Document getByTitre(String titre) /*throws SQLException*/ {
		logger.debug("D�marrage: Doument.getByTitre");
		logger.info("Document.getByTitre("+titre+")");
		
		//Si le document est d�j� instanci�
		if (instancesById.containsKey(titre)) {
			System.out.println("Instance trouv�e pour Document "+titre);
			logger.debug("Arr�t: Document.getByTitre");
			return (Document)instancesById.get(titre);
		}
		
		//Sinon, on cr�e l'instance
		else {
		
			String requete = "SELECT * FROM document WHERE titre = '" + titre.replace("'", "''") + "';";
	
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
			
			//S'il y a un resultat
			if (resultats!=null && resultats.size()>0) {
				
				//On prend le 1er �l�ment
				Dictionary dico = (Dictionary) resultats.firstElement();
				
				//On mappe les champs
				String id = String.valueOf((Integer)dico.get("id"));
				int duree = (int)(Integer)dico.get("duree");
				String source = ((String)dico.get("source")).replace("''", "'");
				String langue = ((String)dico.get("langue")).replace("''", "'");
				String genre = ((String)dico.get("genre")).replace("''", "'");
				String fichier = ((String)dico.get("fichier")).replace("''", "'");
				
				//On assemble la date
				GregorianCalendar date = new GregorianCalendar();
				date.setTimeInMillis(Long.valueOf((String)dico.get("date")));
				int jour = date.get(GregorianCalendar.DATE);
				int mois = date.get(GregorianCalendar.MONTH);
				int annee = date.get(GregorianCalendar.YEAR);
				
				String artiste = ((String)dico.get("artiste")).replace("''", "'");
				String interprete = ((String)dico.get("interprete")).replace("''", "'");
				String compositeur = ((String)dico.get("compositeur")).replace("''", "'");
				
				System.out.println("-------- Document -----------");
				System.out.println("Nb de champs: "+dico.size());
				System.out.println("ID: "+id);
				System.out.println("Titre: "+titre);
				System.out.println("Duree: "+duree);
				System.out.println("Source: "+source);
				System.out.println("Langue: "+langue);
				System.out.println("Genre: "+genre);
				System.out.println("Fichier: "+fichier);
				System.out.println("Date: "+jour+"/"+mois+"/"+annee);
				System.out.println("Artiste: "+artiste);
				System.out.println("Interpr�te: "+interprete);
				System.out.println("Compositeur: "+compositeur);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
				Document doc = new Document(id, titre, duree, date, source, langue, genre, fichier, artiste, interprete, compositeur);
				instancesById.put(doc.getId(), doc);
				instancesByTitre.put(doc.getTitre(), doc);
				doc.setProgrammesAssocies();
				doc.setContratsAssocies();
				logger.debug("Arr�t: Document.getByTitre");
				return doc;
			}

			//Sinon, on retourne un objet vide
			logger.debug("Arr�t: Document.getByTitre");
			return null;
		}
	}
	
	/**
	 * Instancie un objet document apr�s avoir r�cup�r� ces infos depuis la base � partir de son id
	 * @param String id
	 * @return Document
	 * @throws SQLException 
	 */
	public static Document getById(String id) /*throws SQLException*/ {
		logger.debug("D�marrage: Document.getById");
		logger.info("Document.getById("+id+")");
		
//		Si le document est d�j� instanci�
		if (instancesById.containsKey(id)) {
			System.out.println("Instance trouv�e pour Document "+id);
			logger.debug("Arr�t: Document.getById");
			return (Document)instancesById.get(id);
		}
		
		//Sinon, on cr�e l'instance
		else {
		
			String requete = "SELECT * FROM document WHERE id = '" + id + "';";
	
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
			
			//S'il y a un resultat
			if (resultats!=null && resultats.size()>0) {
				
				//On prend le 1er �l�ment
				Dictionary dico = (Dictionary) resultats.firstElement();
				
				//On mappe les champs
				String titre = ((String)dico.get("titre")).replace("''", "'");
				int duree = (int)(Integer)dico.get("duree");
				String source = ((String)dico.get("source")).replace("''", "'");
				String langue = ((String)dico.get("langue")).replace("''", "'");
				String genre = ((String)dico.get("genre")).replace("''", "'");
				String fichier = ((String)dico.get("fichier")).replace("''", "'");
				
				String artiste = ((String)dico.get("artiste")).replace("''", "'");
				String interprete = ((String)dico.get("interprete")).replace("''", "'");
				String compositeur = ((String)dico.get("compositeur")).replace("''", "'");
				
				//On assemble la date
				GregorianCalendar date = new GregorianCalendar();
				date.setTimeInMillis(Long.valueOf((String)dico.get("date")));
				int jour = date.get(GregorianCalendar.DATE);
				int mois = date.get(GregorianCalendar.MONTH);
				int annee = date.get(GregorianCalendar.YEAR);
				
				
				System.out.println("-------- Document -----------");
				System.out.println("Nb de champs: "+dico.size());
				System.out.println("ID: "+id);
				System.out.println("Titre: "+titre);
				System.out.println("Duree: "+duree);
				System.out.println("Source: "+source);
				System.out.println("Langue: "+langue);
				System.out.println("Genre: "+genre);
				System.out.println("Fichier: "+fichier);
				System.out.println("Date: "+jour+"/"+mois+"/"+annee);
				System.out.println("Artiste: "+artiste);
				System.out.println("Interpr�te: "+interprete);
				System.out.println("Compositeur: "+compositeur);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
	
				
				Document doc = new Document(id, titre, duree, date, source, langue, genre, fichier, artiste, interprete, compositeur);
				instancesById.put(doc.getId(), doc);
				instancesByTitre.put(doc.getTitre(), doc);
				doc.setProgrammesAssocies();
				doc.setContratsAssocies();
				logger.debug("Arr�t: Document.getById");
				return doc;
			}
			
			//Sinon, on retourne un objet vide
			logger.debug("Arr�t: Document.getById");
			return null;
		}
	}
	
	/**
	 * Retourne un vecteur d'objets documents instanci�s � partir de toutes les infos de la base 

	 * @return Hashtable
	 * @throws SQLException 
	 */

	public static Hashtable getAll() /*throws SQLException*/ {
		logger.debug("D�marrage: getAll");
		
		//On cr�e un vecteur pour contenir les objets documents instanci�s
		Hashtable documents = new Hashtable();
		
		//On va chercher dans la liste des id de tous les documents
		String requete = "SELECT id FROM document;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		logger.info("Document.getAll() : "+resultats.size()+" document(s) trouv�(s)");
		
		//Pour chaque document, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id"));
			documents.put(id, getById(id));
		}

		//On retourne le vecteur contenant les objets documents instanci�s
		logger.debug("Arr�t: getAll");
		return documents;
	}
	
	/**
	 * D�truit les infos d'un document contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		logger.debug("D�marrage: deleteById");
		
		//On supprime le document de la base, en partant d'un id
		String requete = "DELETE FROM document WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);

		//Si la ligne est bien supprim�e de la base
		if (nbRows>0) {
			
			//On retire l'instance
			Document d = getById(id);
			instancesByTitre.remove(d.getTitre());
			instancesById.remove(id);
			logger.debug("Arr�t: deleteById");
			return true;
		}
		
		//Sinon, suppression invalide
		logger.debug("Arr�t: deleteById");
		return false;
	}
	
	/**
	 * R�indexe la collection d'instances bas�e sur les titres
	 * @param oldTitre
	 * @param newTitre
	 */
	public static void updateByTitre(String oldTitre, String newTitre) {
		logger.debug("D�marrage: updateByTitre");
		Document d = (Document)instancesByTitre.get(oldTitre);
		instancesByTitre.remove(oldTitre);
		instancesByTitre.put(newTitre, d);
		logger.debug("Arr�t: updateByTitre");
	}
}