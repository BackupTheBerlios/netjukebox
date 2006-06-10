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
	 * Instances organisées par ID
	 */
	private static Hashtable instancesById = new Hashtable();
	
	/**
	 * Instances organisées par Titre
	 */
	private static Hashtable instancesByTitre = new Hashtable();
	
	/**
	 * Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(DocumentFactory.class);
	
//METHODES STATIQUES
//-----------------------------------

	/**
	 * Vérifie s'il y a une instance pour l'ID demandé
	 * @param String key
	 * @return boolean
	 */
	public static boolean containsId(String key) {
		return instancesById.containsKey(key);
	}
	
	/**
	 * Vérifie s'il y a une instance pour le titre demandé
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
	 * Création du programme en base
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
		
		logger.debug("Démarrage : Document.create");				
		
		//On assemble la date
		//String date = jour+"-"+mois+"-"+annee;
		GregorianCalendar date = new GregorianCalendar(annee, mois, jour);
		
		//On crée le document dans la base
		String requete = "INSERT INTO document (titre, duree, date, source, langue, genre, fichier, artiste, interprete, compositeur) VALUES ('" +
			titre.replace("'", "''") + "', '" + duree + "', '" + date.getTimeInMillis() + "', '" + source.replace("'", "''") + "', '" +langue.replace("'", "''") + "', '" +
			genre.replace("'", "''") + "', '" + fichier.replace("'", "''") + "', '"+ artiste.replace("'", "''") + "', '"+ interprete.replace("'", "''") + "', '" + compositeur.replace("'", "''") + "');"; 
		
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien déroulée
		if (nbRows>0) {
			//On retourne ensuite un objet pour ce document
			logger.debug("Arrêt: Document.create");
			return getByTitre(titre);
		}
		//Sinon on retourne un objet vide
		logger.debug("Arrêt: Document.create");
		return null;
	}
	
	/**
	 * Instancie un objet document après avoir récupéré ces infos depuis la base à partir de son titre
	 * @param String titre
	 * @return Document
	 * @throws SQLException 
	 */
	public static Document getByTitre(String titre) /*throws SQLException*/ {
		logger.debug("Démarrage: Doument.getByTitre");
		logger.info("Document.getByTitre("+titre+")");
		
		//Si le document est déjà instancié
		if (instancesById.containsKey(titre)) {
			System.out.println("Instance trouvée pour Document "+titre);
			logger.debug("Arrêt: Document.getByTitre");
			return (Document)instancesById.get(titre);
		}
		
		//Sinon, on crée l'instance
		else {
		
			String requete = "SELECT * FROM document WHERE titre = '" + titre.replace("'", "''") + "';";
	
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
			
			//S'il y a un resultat
			if (resultats!=null && resultats.size()>0) {
				
				//On prend le 1er élément
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
				System.out.println("Interprète: "+interprete);
				System.out.println("Compositeur: "+compositeur);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
				Document doc = new Document(id, titre, duree, date, source, langue, genre, fichier, artiste, interprete, compositeur);
				instancesById.put(doc.getId(), doc);
				instancesByTitre.put(doc.getTitre(), doc);
				doc.setProgrammesAssocies();
				doc.setContratsAssocies();
				logger.debug("Arrêt: Document.getByTitre");
				return doc;
			}

			//Sinon, on retourne un objet vide
			logger.debug("Arrêt: Document.getByTitre");
			return null;
		}
	}
	
	/**
	 * Instancie un objet document après avoir récupéré ces infos depuis la base à partir de son id
	 * @param String id
	 * @return Document
	 * @throws SQLException 
	 */
	public static Document getById(String id) /*throws SQLException*/ {
		logger.debug("Démarrage: Document.getById");
		logger.info("Document.getById("+id+")");
		
//		Si le document est déjà instancié
		if (instancesById.containsKey(id)) {
			System.out.println("Instance trouvée pour Document "+id);
			logger.debug("Arrêt: Document.getById");
			return (Document)instancesById.get(id);
		}
		
		//Sinon, on crée l'instance
		else {
		
			String requete = "SELECT * FROM document WHERE id = '" + id + "';";
	
			Jdbc base = Jdbc.getInstance();
			Vector resultats = base.executeQuery(requete);
			
			//S'il y a un resultat
			if (resultats!=null && resultats.size()>0) {
				
				//On prend le 1er élément
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
				System.out.println("Interprète: "+interprete);
				System.out.println("Compositeur: "+compositeur);
				System.out.println("-----------------------------");
				
				//On retourne l'objet
	
				
				Document doc = new Document(id, titre, duree, date, source, langue, genre, fichier, artiste, interprete, compositeur);
				instancesById.put(doc.getId(), doc);
				instancesByTitre.put(doc.getTitre(), doc);
				doc.setProgrammesAssocies();
				doc.setContratsAssocies();
				logger.debug("Arrêt: Document.getById");
				return doc;
			}
			
			//Sinon, on retourne un objet vide
			logger.debug("Arrêt: Document.getById");
			return null;
		}
	}
	
	/**
	 * Retourne un vecteur d'objets documents instanciés à partir de toutes les infos de la base 

	 * @return Hashtable
	 * @throws SQLException 
	 */

	public static Hashtable getAll() /*throws SQLException*/ {
		logger.debug("Démarrage: getAll");
		
		//On crée un vecteur pour contenir les objets documents instanciés
		Hashtable documents = new Hashtable();
		
		//On va chercher dans la liste des id de tous les documents
		String requete = "SELECT id FROM document;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		logger.info("Document.getAll() : "+resultats.size()+" document(s) trouvé(s)");
		
		//Pour chaque document, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id"));
			documents.put(id, getById(id));
		}

		//On retourne le vecteur contenant les objets documents instanciés
		logger.debug("Arrêt: getAll");
		return documents;
	}
	
	/**
	 * Détruit les infos d'un document contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		logger.debug("Démarrage: deleteById");
		
		//On supprime le document de la base, en partant d'un id
		String requete = "DELETE FROM document WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);

		//Si la ligne est bien supprimée de la base
		if (nbRows>0) {
			
			//On retire l'instance
			Document d = getById(id);
			instancesByTitre.remove(d.getTitre());
			instancesById.remove(id);
			logger.debug("Arrêt: deleteById");
			return true;
		}
		
		//Sinon, suppression invalide
		logger.debug("Arrêt: deleteById");
		return false;
	}
	
	/**
	 * Réindexe la collection d'instances basée sur les titres
	 * @param oldTitre
	 * @param newTitre
	 */
	public static void updateByTitre(String oldTitre, String newTitre) {
		logger.debug("Démarrage: updateByTitre");
		Document d = (Document)instancesByTitre.get(oldTitre);
		instancesByTitre.remove(oldTitre);
		instancesByTitre.put(newTitre, d);
		logger.debug("Arrêt: updateByTitre");
	}
}