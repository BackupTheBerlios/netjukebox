package proto.serveur;

import java.sql.SQLException;
import java.util.Date;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Document mis � disposition dans le NetJukeBox
 */
public class Document {

// ATTRIBUTS DU DOCUMENT
//********************************************

	/**
	 * Cr�e le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(Document.class);
	
	/**
	 * Identifiant du document
	 */
	private String id;

	/**
	 * Titre du document
	 */
	private String titre;

	/**
	 * Source du document
	 */
	private String source;

	/**
	 * Dur�e du document
	 */
	private int duree;

	/**
	 * Date de parution du document
	 */
	private GregorianCalendar dateParution;

	/**
	 * Langue du document
	 */
	private String langue;

	/**
	 * Genre du document
	 */
	private String genre;

	/**
	 * Nom du fichier correspondant au document
	 */
	private String fichier;

	/**
	 * Etat du document
	 */
	private String etat;

	/**
	 * Liste de verrous sur le document
	 */
	private Vector verrous = new Vector();
	

	/**
	 * Liste des programmes associ�s au document
	 */
	private Hashtable programmes = new Hashtable();

	/**
	 * Contrat relatif au document
	 */
	private Contrat contrat;
	
	/**
	 * Artiste
	 */
	private String artiste;

	/**
	 * Interpr�te
	 */
	private String interprete;

	/**
	 * Compositeur
	 */
	private String compositeur;
	
	
// CONSTRUCTEUR
//********************************************
	
	/**
	 * Constructeur
	 * @param id
	 * @param titre
	 * @param duree
	 * @param GregorianCalendar dateParution
	 * @param source
	 * @param langue
	 * @param genre
	 * @param fichier
	 * 
	 * @param String artiste
	 * @param String interprete
	 * @param String compositeur
	 */
	public Document(String id, String titre, int duree, GregorianCalendar dateParution, String source,
			String langue, String genre, String fichier, String artiste, String interprete, String compositeur) {

		this.id = id;
		this.titre = titre;
		this.dateParution = dateParution;
		this.source = source;
		this.langue = langue;
		this.genre = genre;
		this.fichier = fichier;
		this.duree = duree;
		
		this.artiste = artiste;
		this.interprete = interprete;
		this.compositeur = compositeur;
	}

// METHODES STATIQUES
//********************************************
	
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
		
	
		PropertyConfigurator.configure("C:/Documents and Settings/Marie Rubini/Mes documents/workspace/NetJukeBox/proto/serveur/log4j.prop");
		logger.debug("D�marrage : Document.create");				
		
		//On assemble la date
		//String date = jour+"-"+mois+"-"+annee;
		GregorianCalendar date = new GregorianCalendar(annee, mois-1, jour);
		
		//On cr�e le document dans la base
		String requete = "INSERT INTO document (titre, duree, date, source, langue, genre, fichier, artiste, interprete, compositeur) VALUES ('" +
			titre + "', '" + duree + "', '" + date.getTimeInMillis() + "', '" + source + "', '" +langue + "', '" +
			genre + "', '" + fichier + "', '"+ artiste + "', '"+ interprete + "', '" + compositeur + "');"; 
		
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien d�roul�e
		if (nbRows>0) {
			//On retourne ensuite un objet pour ce document
			logger.debug("Arr�t: Document.create");
			return Document.getByTitre(titre);
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
		
		String requete = "SELECT * FROM document WHERE titre = '" + titre + "';";

		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		//S'il y a un resultat
		if (resultats!=null && resultats.size()>0) {
			
			//On prend le 1er �l�ment
			Dictionary dico = (Dictionary) resultats.firstElement();
			
			//On mappe les champs
			String id = String.valueOf((Integer)dico.get("id"));
			String titreDoc = (String)dico.get("titre");
			int duree = (int)(Integer)dico.get("duree");
			String source = (String)dico.get("source");
			String langue = (String)dico.get("langue");
			String genre = (String)dico.get("genre");
			String fichier = (String)dico.get("fichier");
			
			//On assemble la date
			GregorianCalendar date = new GregorianCalendar();
			date.setTimeInMillis(Long.valueOf((String)dico.get("date")));
			int jour = date.get(GregorianCalendar.DATE);
			int mois = date.get(GregorianCalendar.MONTH);
			int annee = date.get(GregorianCalendar.YEAR);
			
			String artiste = (String)dico.get("artiste");
			String interprete = (String)dico.get("interprete");
			String compositeur = (String)dico.get("compositeur");
			
			System.out.println("-------- Document -----------");
			System.out.println("Nb de champs: "+dico.size());
			System.out.println("ID: "+id);
			System.out.println("Titre: "+titreDoc);
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

			logger.debug("Arr�t: Document.getByTitre");	
			return new Document(id, titreDoc, duree, date, source, langue, genre, fichier, artiste, interprete, compositeur);
		}

	
		/*
		//----------- TEST SANS JDBC---------------------
		if (titre.equalsIgnoreCase("Rhapsody in Blue")) {
			//On retourne un objet document configur�
			return new Document("1", "Rhapsody in Blue", 1100, "01", "01", "2006", "Source", "FR", "classic", "file://home/philippe/njb/1.mp3");
		}
		//-----------------------------------------------
		*/
		
		//Sinon, on retourne un objet vide
		logger.debug("Arr�t: Document.getByTitre");
		return null;
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
		
		String requete = "SELECT * FROM document WHERE id = '" + id + "';";

		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		//S'il y a un resultat
		if (resultats!=null && resultats.size()>0) {
			
			//On prend le 1er �l�ment
			Dictionary dico = (Dictionary) resultats.firstElement();
			
			//On mappe les champs
			String idDoc = String.valueOf((Integer)dico.get("id"));
			String titre = (String)dico.get("titre");
			int duree = (int)(Integer)dico.get("duree");
			String source = (String)dico.get("source");
			String langue = (String)dico.get("langue");
			String genre = (String)dico.get("genre");
			String fichier = (String)dico.get("fichier");
			
			String artiste = (String)dico.get("artiste");
			String interprete = (String)dico.get("interprete");
			String compositeur = (String)dico.get("compositeur");
			
			//On assemble la date
			GregorianCalendar date = new GregorianCalendar();
			date.setTimeInMillis(Long.valueOf((String)dico.get("date")));
			int jour = date.get(GregorianCalendar.DATE);
			int mois = date.get(GregorianCalendar.MONTH);
			int annee = date.get(GregorianCalendar.YEAR);
			
			
			System.out.println("-------- Document -----------");
			System.out.println("Nb de champs: "+dico.size());
			System.out.println("ID: "+idDoc);
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

			logger.debug("Arr�t: Document.getById");
			return new Document(idDoc, titre, duree, date, source, langue, genre, fichier, artiste, interprete, compositeur);

		}

		/*
		//----------- TEST SANS JDBC---------------------
		if (id.equalsIgnoreCase("1")) {
			//On retourne un objet document configur�
			return new Document("1", "Rhapsody in Blue", 1100, "01", "01", "2006", "Source", "FR", "classic", "file://home/philippe/njb/1.mp3");
		}
		//-----------------------------------------------
		*/
		
		//Sinon, on retourne un objet vide
		logger.debug("Arr�t: Document.getById");
		return null;
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
			documents.put(id, Document.getById(id));
		}

		/*
		//----------- TEST SANS JDBC---------------------
		documents.put("1", Document.getById("1"));
		//-----------------------------------------------
		*/
		
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
		
		//On retourne le resultat de l'op�ration (succ�s/�chec)
		logger.debug("Arr�t: deleteById");
		return nbRows > 0;
	}

// METHODES DYNAMIQUES
//*********************************************
	
	/**
	 * Attribue un nom au fichier
	 * @param fichier
	 */
	public void nommerFichier(String fichier) {
		// your code here
	}

	public void selectionner() {
		// your code here
	}
	
	/**
	 * Ajoute le programme qui ins�re le document dans sa liste
	 * Appel la fonction de pose d'un verrou
	 * @param Programme prog
	 */
	public void ajouterProgramme(Programme prog) {
		logger.debug("D�marrage: ajouterProgramme");
		
		//S'il n'y a pas de verrous pour le programme
		if (!verrous.contains(prog.getId())) {
			
			//On ajoute le prog � la liste des programmes associ�s
			programmes.put(prog.getId(), prog);
		}
		
		//On pose un verrou pour le programme
		verrous.addElement(prog.getId());
		
		logger.info("Document verrouill� : " + this.id);
		logger.info("Le compteur de verrou = " + verrous.size());
		logger.debug("Arr�t: ajouterProgramme");
	}

	/**
	 * Supprime de sa liste le programme qui supprime le document
	 * Appel la fonction de d�verrouillage
	 * @param String
	 */
	public void retirerProgramme(String idProg) {
		logger.debug("D�marrage: retirerProgramme");
				
		//On enl�ve un verrou pour le programme
		verrous.remove(idProg);
		
		//S'il n'y a plus de verrous pour le programme
		if (!verrous.contains(idProg)) {
			
			//Le document n'est plus dans ce programme.
			//On le retire de la liste des progs associ�s
			programmes.remove(idProg);
		}
		
		logger.info("Document d�verrouill� : " + this.id);
		logger.info("Le compteur de verrou = " + verrous.size());
		logger.debug("Arr�t: retirerProgramme");
	}
	
	/**
	 * Modifie les attributs
	 * @param String titre
	 * @param int duree
	 * @param String jour
	 * @param String mois
	 * @param String annee
	 * @param String source
	 * @param String langue
	 * @param String genre
	 * @param String fichier
	 * @return boolean
	 */
	public boolean modifier(String titre, int duree, int jour,
			int mois, int annee, String source, String langue, String genre,
			String fichier, String artiste, String interprete, String compositeur) {
		
		logger.debug("D�marrage: modifier");

		GregorianCalendar date = new GregorianCalendar(annee, mois, jour);
		
		String requete = "UPDATE document SET titre = '" + titre + "', duree = '" + duree +
			"', date = '"+ date.getTimeInMillis() + "', source = '" + source + "', langue = '" + langue +
			"', genre = '" + langue + "', fichier = '" + fichier + "', artiste = '"+ artiste +
			"', interprete = '" + interprete + "', compositeur = '"+ compositeur + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.titre = titre;
			this.dateParution = date;
			this.source = source;
			this.langue = langue;
			this.genre = genre;
			this.fichier = fichier;
			this.duree = duree;
			
			this.artiste = artiste;
			this.compositeur = compositeur;
			this.interprete = interprete;
		}
		logger.debug("Arr�t: modifier");
		return nbRows>0;
	}
	
	/**
	 * D�truit le document et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		logger.debug("D�marrage: supprimer");
				
		//On supprime les associations document/programme
		Programme p;
		for (int i=0; i<programmes.size(); i++){
			p = (Programme)programmes.get(i);
			p.retirerDocument(id);
		}
		
		//On supprime les infos de la base
		logger.debug("Arr�t: supprimer");
		return Document.deleteById(id);
	}

//#### GETTERS ####
//#################
	
	/**
	 * Retourne l'ensemble des attributs sous la forme d'un dictionnaire
	 * @return Dictionary
	 */
	public Dictionary getAttributesDictionary() {
		logger.debug("D�marrage: getAttributesDictionary");
	
		int jour = dateParution.get(GregorianCalendar.DATE);
		int mois = dateParution.get(GregorianCalendar.MONTH);
		int annee = dateParution.get(GregorianCalendar.YEAR);
		
		Dictionary dico = new Hashtable();
		
		dico.put("id", id);
		dico.put("titre", titre);
		dico.put("duree", duree);
		dico.put("genre", genre);
		dico.put("source", source);
		dico.put("langue", langue);
		dico.put("date", (jour+"/"+mois+"/"+annee));
		dico.put("fichier", fichier);
		
		dico.put("artiste", artiste);
		dico.put("interprete", interprete);
		dico.put("compositeur", compositeur);

		logger.debug("Arr�t: getAttributesDictionary");
		return dico;
	}

	/**
	 * Retourne l'identifiant du document
	 * @return String
	 */
	public String getId() {
		logger.debug("D�marrage: getId");
		logger.debug("Arr�t: getId");
		return id;
	}

	/**
	 * Retourne le titre du document
	 * @return String
	 */
	public String getTitre() {
		logger.debug("D�marrage: getTitre");
		logger.debug("Arr�t: getTitre");
		return titre;
	}

	/**
	 * Retourne la dur�e du document
	 * @return int
	 */
	public int getDuree() {
		logger.debug("D�marrage: getDuree");
		logger.debug("Arr�t: getDuree");
		return duree;
	}

	/**
	 * Retourne la source du document
	 * @return String
	 */
	public String getSource() {
		logger.debug("D�marrage: getSource");
		logger.debug("Arr�t: getSource");
		return source;
	}

	/**
	 * Retourne la langue du documennt
	 * @return String
	 */
	public String getLangue() {
		logger.debug("D�marrage: getLangue");
		logger.debug("Arr�t: getLangue");
		return langue;
	}

	/**
	 * Retourne le genre du document
	 * @return String
	 */
	public String getGenre() {
		logger.debug("D�marrage: getGenre");
		logger.debug("Arr�t: getGenre");
		return genre;
	}

	/**
	 * Retourne le chemin o� stock� le document
	 * @return String
	 */
	public String getFichier() {
		logger.debug("D�marrage: getFichier");
		logger.debug("Arr�t: getFichier");
		return fichier;
	}

	/**
	 * Retourne l'�tat du document
	 * @return String
	 */
	public String getEtat() {
		logger.debug("D�marrage: getEtat");
		logger.debug("Arr�t: getEtat");
		return etat;
	}
	
	/**
	 * Affiche le nombre de verrous sur le document
	 * Affiche les programmes v�rrouillant le document
	 */
	public void compterVerrouProgramme() {
		logger.debug("D�marrage: compterVerrouProgramme");
		logger.info("Le document : " + id + " est verrouill� : " + verrous.size() + " fois");
		logger.debug("Arr�t: compterVerrouProgramme");
	}
	
	/**
	 * Affiche la liste des programmes v�rrouillant le document
	 */
	public void getProgrammesArchives() {
	logger.debug("D�marrage: getProgrammesArchives");

		int i;
		if (verrous.size() != 0) {
			for (i = 0; i < verrous.size(); i++) {
				String idProg = (String) verrous.elementAt(i);
				logger.info("Le document : " + id + " est verrouill� par le programme : " + idProg);
			}
		} else {
			logger.info("Le document : " + id + " est verrouill� par aucun programme");
		}
		logger.debug("Arr�t: getProgrammesArchives");
	}
	
	/**
	 * Retourne la date de cr�ation du document
	 * @return Date
	 */
	public Date getDateCreation() {

		logger.debug("D�marrage: getDateCreation");
		
		int jour = dateParution.get(GregorianCalendar.DATE);
		int mois = dateParution.get(GregorianCalendar.MONTH);
		int annee = dateParution.get(GregorianCalendar.YEAR);

		String date = jour + "/"+ mois + "/" + annee;
		TransformeDate TD = new TransformeDate(date);
		logger.info(TD.d);
		logger.debug("Arr�t: getDateCreation");
		return TD.d;
	}
	
	/**
	 * V�rifie l'�tat EN LECTURE
	 * @return boolean
	 */
	public boolean enLecture() {
		logger.debug("D�marrage: enLecture");
		logger.debug("Arr�t: enLecture");
		return etat.equalsIgnoreCase("LECTURE");
	}

	/**
	 * V�rifie l'�tat PROFRAMME
	 * @return boolean
	 */
	public boolean estProgramme() {
		logger.debug("D�marrage: estProgramme");
		logger.debug("Arr�t: estProgramme");
		return etat.equalsIgnoreCase("PROGRAMME");
	}
	
	/**
	 * @return Renvoie artiste.
	 */
	public String getArtiste() {
		logger.debug("D�marrage: getArtiste");
		logger.debug("Arr�t: getArtiste");
		return artiste;
	}

	/**
	 * @return Renvoie compositeur.
	 */
	public String getCompositeur() {
		logger.debug("D�marrage: getCompositeur");
		logger.debug("Arr�t: getCompositeur");
		return compositeur;
	}

	/**
	 * @return Renvoie interprete.
	 */
	public String getInterprete() {
		logger.debug("D�marrage: getInterprete");
		logger.debug("Arr�t: getInterprete");
		return interprete;
	}
		
//#### SETTERS ####
//#################
	/**
	 * @param compositeur compositeur � d�finir.
	 */
	public boolean setCompositeur(String compositeur) {
		logger.debug("D�marrage: setCompositeur");
		
		String requete = "UPDATE document SET compositeur = '" + compositeur + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.compositeur = compositeur;
		}
		logger.debug("Arr�t: setCompositeur");
		return nbRows>0;
	}
	/**
	 * @param artiste artiste � d�finir.
	 */
	public boolean setArtiste(String artiste) {
		logger.debug("D�marrage: setArtiste");
		
		String requete = "UPDATE document SET artiste = '" + artiste + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.artiste = artiste;
		}
		logger.debug("Arr�t: setArtiste");
		return nbRows>0;
	}
	/**
	 * @param interprete interprete � d�finir.
	 */
	public boolean setInterprete(String interprete) {
		logger.debug("D�marrage: setInterprete");

		String requete = "UPDATE document SET interprete = '" + interprete + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.interprete = interprete;
		}
		logger.debug("Arr�t: setInterprete");
		return nbRows>0;
	}
	
	/**
	 * Met � jour l'attribut fichier
	 * @param String fichier
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setFichier(String fichier) /*throws SQLException*/ {
		logger.debug("D�marrage: setFichier");
	
		String requete = "UPDATE document SET fichier = '" + fichier + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.fichier = fichier;
		}
		logger.debug("Arr�t: setFichier");
		return nbRows>0;
	}
	
	/**
	 * Met � jour l'attribut genre
	 * @param genre
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setGenre(String genre) /*throws SQLException*/ {
		logger.debug("D�marrage: setGenre");

		String requete = "UPDATE Document SET genre = '" + genre + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.genre = genre;
		}
		logger.debug("Arr�t: setGenre");
		return nbRows>0;
	}
	
	/**
	 * Met � jour l'attribut langue
	 * @param String langue
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setLangue(String langue) /*throws SQLException*/ {
		logger.debug("D�marrage: setLangue");
		
		String requete = "UPDATE document SET langue = '" + langue + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.langue = langue;
		}
		logger.debug("Arr�t: setLangue");
		return nbRows>0;
	}
	
	/**
	 * Met � jour l'attribut source
	 * @param String source
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setSource(String source) /*throws SQLException*/ {
		logger.debug("D�marrage: setSource");
		
		String requete = "UPDATE document SET source = '" + source + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.source = source;
		}
		logger.debug("Arr�t: setSource");
		return nbRows>0;
	}
	
	/**
	 * Met � jour l'attribut date
	 * @param int jour
	 * @param int mois
	 * @param int annee
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setDate(int jour, int mois, int annee) /*throws SQLException*/ {
		logger.debug("D�marrage: setDate");
		
		GregorianCalendar date = new GregorianCalendar(annee, mois, jour);
		
		String requete = "UPDATE document SET date = '" + date.getTimeInMillis() + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.dateParution = date;
		}
		logger.debug("Arret: setDate");
		return nbRows>0;
	}
	
	/**
	 * Met � jour l'attribut titre
	 * @param String titre
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setTitre(String titre) /*throws SQLException*/ {
		logger.debug("D�marrage: setTitre");
		
		String requete = "UPDATE document SET titre = '" + titre + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.titre = titre;
		}
		logger.debug("Arr�t: setTitre");
		return nbRows>0;
	}
	
	/**
	 * Met � jour l'attribut dur�e
	 * @param String duree
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setDuree(int duree) /*throws SQLException*/ {
		logger.debug("D�marrage: setDuree");
		
		String requete = "UPDATE document SET duree = '" + duree + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.duree = duree;
		}
		logger.debug("Arr�t: setDuree");
		return nbRows>0;
	}
}