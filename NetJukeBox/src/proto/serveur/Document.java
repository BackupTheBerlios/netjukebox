package proto.serveur;

import java.sql.SQLException;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Document mis à disposition dans le NetJukeBox
 */
public class Document {

// ATTRIBUTS DU DOCUMENT
//********************************************

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
	 * Durée du document
	 */
	private int duree;

	/**
	 * Jour de parution du document
	 */
	private String jour;

	/**
	 * Mois de parution du document
	 */
	private String mois;

	/**
	 * Année de parution du document
	 */
	private String annee;

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
	 * Liste des programmes associés au document
	 */
	private Hashtable programmes = new Hashtable();

	/**
	 * Contrat relatif au document
	 */
	private Contrat contrat;
	
	
// CONSTRUCTEUR
//********************************************
	
	/**
	 * Constructeur
	 * @param id
	 * @param titre
	 * @param duree
	 * @param jour
	 * @param mois
	 * @param annee
	 * @param source
	 * @param langue
	 * @param genre
	 * @param fichier
	 */
	public Document(String id, String titre, int duree, String jour,
			String mois, String annee, String source, String langue, String genre,
			String fichier) {

		this.id = id;
		this.titre = titre;
		this.jour = jour;
		this.mois = mois;
		this.annee = annee;
		this.source = source;
		this.langue = langue;
		this.genre = genre;
		this.fichier = fichier;
		this.duree = duree;
	}

// METHODES STATIQUES
//********************************************
	
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
	public static Document create(String titre, int duree, String jour,
			String mois, String annee, String source, String langue, String genre,
			String fichier) /*throws SQLException*/ {
		
		//On crée le document dans la base
		String requete = "INSERT INTO Document('" + titre + "', '" + duree + "', '" + jour + 
		"', '" + mois + "', '" + annee + "', '" + source + "', '" + langue + "', '" + genre +
		"', '" + fichier + "');"; 
		
		Jdbc base = Jdbc.getInstance();
		base.executeUpdate(requete);
		
		//On retourne ensuite un objet pour ce document
		return Document.getByTitre(titre);
	}
	
	/**
	 * Instancie un objet document après avoir récupéré ces infos depuis la base à partir de son titre
	 * @param String titre
	 * @return Document
	 * @throws SQLException 
	 */
	public static Document getByTitre(String titre) /*throws SQLException*/ {
		
		String requete = "SELECT * FROM Document WHERE titre = '" + titre + "';";

		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			System.out.println(dico.get("nom"));
		}
		
		// TRAITER LE VECTEUR ET CREER UN OBJET POUR CHAQUE LIGNE

		//----------- TEST SANS JDBC---------------------
		if (titre.equalsIgnoreCase("Rhapsody in Blue")) {
			//On retourne un objet document configuré
			return new Document("1", "Rhapsody in Blue", 1100, "01", "01", "2006", "Source", "FR", "classic", "file://home/philippe/njb/1.mp3");
		}
		//-----------------------------------------------
		
		//Sinon, on retourne un objet vide
		return null;
	}
	
	/**
	 * Instancie un objet document après avoir récupéré ces infos depuis la base à partir de son id
	 * @param String id
	 * @return Document
	 * @throws SQLException 
	 */
	public static Document getById(String id) /*throws SQLException*/ {
		
		String requete = "SELECT * FROM Document WHERE id_doc = '" + id + "';";

		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			System.out.println(dico.get("nom"));
		}

		// TRAITER LE VECTEUR ET CREER UN OBJET POUR CHAQUE LIGNE

		//----------- TEST SANS JDBC---------------------
		if (id.equalsIgnoreCase("1")) {
			//On retourne un objet document configuré
			return new Document("1", "Rhapsody in Blue", 1100, "01", "01", "2006", "Source", "FR", "classic", "file://home/philippe/njb/1.mp3");
		}
		//-----------------------------------------------
		
		//Sinon, on retourne un objet vide
		return null;
	}
	
	/**
	 * Retourne un vecteur d'objets documents instanciés à partir de toutes les infos de la base 

	 * @return Hashtable
	 * @throws SQLException 
	 */

	public static Hashtable getAll() /*throws SQLException*/ {		
		//On crée un vecteur pour contenir les objets documents instanciés
		Hashtable documents = new Hashtable();
		
		//On va chercher dans la liste des id de tous les documents

		String requete = "SELECT id FROM Document;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			System.out.println(dico.get("nom"));
		}
		
		// TRAITER LE VECTEUR ET CREER UN OBJET POUR CHAQUE LIGNE

		//----------- TEST SANS JDBC---------------------
		
		//Pour chaque document, on instancie un objet que l'on stocke dans le vecteur
		documents.put("1", Document.getById("1"));
		//-----------------------------------------------
		
		//On retourne le vecteur contenant les objets documents instanciés
		return documents;
	}
	
	/**
	 * Détruit les infos d'un document contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) throws SQLException {
		
		//On supprime le document de la base, en partant d'un id
		String requete = "DELETE * FROM Document WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		base.executeUpdate(requete);
		
		//On retourne le resultat de l'opération (succès/échec)
		return true;
	}

// METHODES DYNAMIQUES
//********************************************

	/**
	 * Détruit le document et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() throws SQLException {
		
		//On supprime les infos de la base
		return Document.deleteById(this.id);
	}


	/**
	 * Retourne l'identifiant du document
	 * @return String
	 */
	public String getId() {
		return id;
	}

	/**
	 * Retourne le titre du document
	 * @return String
	 */
	public String getTitre() {
		return titre;
	}

	/**
	 * Retourne la durée du document
	 * @return int
	 */
	public int getDuree() {
		return duree;
	}

	/**
	 * Retourne la source du document
	 * @return String
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Retourne la langue du documennt
	 * @return String
	 */
	public String getLangue() {
		return langue;
	}

	/**
	 * Retourne le genre du document
	 * @return String
	 */
	public String getGenre() {
		return genre;
	}

	/**
	 * Retourne le chemin où stocké le document
	 * @return String
	 */
	public String getFichier() {
		return fichier;
	}

	/**
	 * Retourne l'état du document
	 * @return String
	 */
	public String getEtat() {
		return etat;
	}
	
	/**
	 * Affiche le nombre de verrous sur le document
	 * Affiche les programmes vérrouillant le document
	 */
	public void compterVerrouProgramme() {
		System.out.println("Le document : " + id + " est verrouillé : " + verrous.size() + " fois");
	}
	
	/**
	 * Affiche la liste des programmes vérrouillant le document
	 */
	public void getProgrammesArchives() {
		int i;
		if (verrous.size() != 0) {
			for (i = 0; i < verrous.size(); i++) {
				String idProg = (String) verrous.elementAt(i);
				System.out.println("Le document : " + id + " est verrouillé par le programme : " + idProg);
			}
		} else {
			System.out.println("Le document : " + id + " est verrouillé par aucun programme");
		}
	}

	/**
	 * Ajoute le programme qui insère le document dans sa liste
	 * Appel la fonction de pose d'un verrou
	 * @param Programme prog
	 */
	public void ajouterProgramme(Programme prog) {
		programmes.put(prog.getId(), prog);
		verrous.addElement(prog.getId());
		System.out.println("Document verrouillé : " + this.id);
		System.out.println("Le compteur de verrou = " + verrous.size());
	}

	/**
	 * Supprime de sa liste le programme qui supprime le document
	 * Appel la fonction de déverrouillage
	 * @param String
	 */
	public void enleverProgramme(String idProg) {
		programmes.remove(idProg);
		verrous.remove(idProg);
		System.out.println("Document déverrouillé : " + this.id);
		System.out.println("Le compteur de verrou = " + verrous.size());
	}
	
	/**
	 * Retourne la date de création du document
	 * @return Date
	 */
	public Date GetDate_Creation() {
		String Date = jour + "-"+ mois + "-" + annee;
		TransformeDate TD = new TransformeDate(Date);
		System.out.println(TD.d);
		return TD.d;
	}
	
	public void setFichier(String id, String fichier) throws SQLException {
		String requete = "UPDATE Document SET fichier = '" + fichier + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		base.executeUpdate(requete);
	}
	
	public void setGenre(String id, String genre) throws SQLException {
		String requete = "UPDATE Document SET genre = '" + genre + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		base.executeUpdate(requete);
	}
	
	public void setLangue(String id, String langue) throws SQLException {
		String requete = "UPDATE Document SET langue = '" + langue + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		base.executeUpdate(requete);
	}
	
	public void setSource(String id, String source) throws SQLException {
		String requete = "UPDATE Document SET source = '" + source + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		base.executeUpdate(requete);
	}
	
	public void setAnnee(String id, String annee) throws SQLException {
		String requete = "UPDATE Document SET annee = '" + annee + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		base.executeUpdate(requete);
	}
	
	public void setMois(String id, String mois) throws SQLException {
		String requete = "UPDATE Document SET mois = '" + mois + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		base.executeUpdate(requete);
	}
	
	public void setJour(String id, String jour) throws SQLException {
		String requete = "UPDATE Document SET jour = '" + jour + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		base.executeUpdate(requete);
	}
	
	public void setTitre(String id, String titre) throws SQLException {
		String requete = "UPDATE Document SET titre = '" + titre + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		base.executeUpdate(requete);
	}
	
	public void setDuree(String id, int duree) throws SQLException {
		String requete = "UPDATE Document SET duree = '" + duree + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		base.executeUpdate(requete);
	}
	
	/**
	 * Attribue un nom au fichier
	 * @param fichier
	 */
	public void nommerFichier(String fichier) {
		// your code here
	}
	
	/**
	 * Insère les informations du document
	 * @param id
	 * @param titre
	 * @param dateCreation
	 * @param source
	 * @param langue
	 * @param genre
	 */
	public void insertionInfos(String id, String Ttitre,
			java.util.Date dateCreation, String source, String langue,
			String genre) {
		
		// your code here
	}

	public boolean enLecture(String Etat) {
		// your code here
		return false;
	}

	public boolean estProgramme(String Etat) {
		// your code here
		return false;
	}


	public boolean modifier(String Id_Doc, String Titre,
			java.util.Date Date_Creation, String Source, String Langue,
			String Genre) {
		
		// your code here
		return false;
	}

	public boolean majInfos(String Id_Doc, String Titre,
			java.util.Date Date_Creation, String Source, String Langue,
			String Genre) {
		
		// your code here
		return false;
	}

	public void selectionner() {
		// your code here
	}
}