package proto.serveur;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Programme de diffusion
 */
public class Programme {

// ATTRIBUTS DU PROGRAMME
//*********************************************************

	/**
	 * Identifiant du programme
	 */
	private String id;

	/**
	 * Titre du programme
	 */
	private String titre;

	/**
	 * Thématique du programme
	 */
	private String thematique;

	/**
	 * Etat du programme
	 */
	private boolean etat = false;

	/**
	 * Liste des documents associés au programme
	 */
	private Hashtable documents = new Hashtable();
	

	// Initialisation du fichier archive
	String fichier_archive = "_Archive.txt";

	// Initialisation de l'état du programme non archivé
	String etat_archive = "false";

// CONSTRUCTEUR
//*********************************************************
	
	/**
	 * Constructeur
	 * @param String id
	 * @param titre
	 * @param thematique
	 */
	public Programme (String id, String titre, String thematique) {
		this.id = id;
		this.titre = titre;
		this.thematique = thematique;
	}

// METHODES STATIQUES
//*********************************************************

	/**
	 * Création du programme en base
	 * @param String titre
	 * @param String thematique
	 * @return Programme
	 * @throws SQLException 
	 */
	public static Programme create(String titre, String thematique) throws SQLException {
		
		String requete = "INSERT INTO Programme('" + titre + "', '" + thematique + "');";
		new Jdbc(requete);
		
		return Programme.getByTitre(titre);
	}
	
	/**
	 * Insancie un objet programme après avoir récupéré ces infos depuis la base à partir de son titre
	 * @param String titre
	 * @return Programme
	 * @throws SQLException 
	 */
	public static Programme getByTitre(String titre) throws SQLException {
		
		String requete = "SELECT * FROM Programme WHERE titre = '" + titre + "';";
		new Jdbc(requete);
		
		if (titre.equalsIgnoreCase("classic")) {
			//On retourne un objet programme configuré
			return new Programme("1", "classic", "Musique classique");
		}
		
		//Sinon, on retourne un objet vide
		return null;
	}
	
	/**
	 * Insancie un objet programme après avoir récupéré ces infos depuis la base à partir de son id
	 * @param String id
	 * @return Programme
	 * @throws SQLException 
	 */
	public static Programme getById(String id) throws SQLException {
		
		String requete = "SELECT * FROM Programme WHERE id = '" + id + "';";
		new Jdbc(requete);

		if (id.equalsIgnoreCase("1")) {
			//On retourne un objet programme configuré
			return new Programme("1", "classic", "Musique classique");
		}
		
		//Sinon, on retourne un objet vide
		return null;
	}
	
	/**
	 * Retourne un vecteur d'objets canaux instanciés à partir de toutes les infos de la base 
	 * @return Hashtable
	 * @throws SQLException 
	 */
	public static Hashtable getAll() throws SQLException {
		
		//On crée un vecteur pour contenir les objets programmes instanciés
		Hashtable programmes = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id FROM Programme;";
		new Jdbc(requete);
		
		//Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		programmes.put("1", Programme.getById("1"));
		
		//On retourne le vecteur contenant les objets programmes instanciés
		return programmes;
	}
	
	/**
	 * Détruit les infos d'un programme contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) throws SQLException {
		
		//On supprime le programme de la base, en partant d'un id
		String requete = "DELETE * FROM Programme WHERE id = '" + id + "';";
		new Jdbc(requete);
		
		//On retourne le resultat de l'opération (succès/échec)
		return true;
	}
	
	
// METHODES DYNAMIQUES
//*********************************************************

	/**
	 * Détruit le programme et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() throws SQLException {
		
		//On supprime les infos de la base
		return Programme.deleteById(this.id);
	}
	
	/**
	 * Retourne l'identifiant du programme
	 * @return String
	 */
	public String getId() {
		return id;
	}

	/**
	 * Retourne le titre du programme
	 * @return String
	 */
	public String getTitre() {
		return titre;
	}

	/**
	 * Retourne la thématique du programme
	 * @return String
	 */
	public String getThematique() {
		return thematique;
	}
	
	/**
	 * Retourne les documents associés au programme
	 * @return Vector of Document
	 */
	public Hashtable getDocuments() {
		return documents;
	}

	/**
	 * Retourne l'état du programme
	 * @return boolean
	 */
	public boolean getEtat() {
		return etat;
	}

	/**
	 * Ajoute un document au programme
	 * @param Document doc
	 */
	public void ajouterDocument(Document doc) {
		
		//On ajoute le document au programme
		//************
		// => JDBC <=
		//************
		
		//On met à jour le vecteurs d'association
		documents.put(doc.getId(), doc);
		
		//On signale au document cet ajout
		doc.ajouterProgramme(this);
	}

	/**
	 * Retire un document du programme
	 * @param Document doc
	 * @return Document
	 */
	public Document retirerDocument(Document doc) {
		
		// On retire le document du programme
		//************
		// => JDBC <=
		//************
		
		//On met à jour le vecteur d'association
		documents.remove(doc.getId());
		
		//On signale au document ce retrait
		doc.enleverProgramme(this.id);
		
		return doc;
	}

	/**
	 * Retourne la liste des documents associés au programme
	 * @return Hashtable
	 */
	public Hashtable listerDocuments() {
		return documents;
	}
	
	/**
	 * Ajoute un programme (ensemble de documents) au programme
	 * @param prog
	 */
	public void ajouterProgramme(Programme prog) {
		/*prog.listerDocuments();
		// System.out.println(Prog.v);
		int i;
		String donnee;
		for (i = 0; i < prog.documents.size(); i++) {
			donnee = (String) prog.getDocuments().elementAt(i);
			documents.addElement(donnee);
			System.out.println("Le document : " + donnee + " du programme : "
					+ prog.getId() + " a été inséré dans le programme : "
					+ this.id);
		}*/
	}

	// Sauvegarde du vecteur de document si cela n'a pas déjà été fait
	public void archiver() {
		/*int i;
		String donnee;
		if (etat_archive == "false") {
			etat_archive = "true";
			// Création du nom complet du fichier Archive de tous les documents
			// composant le programme
			fichier_archive = this.id + fichier_archive;
			// Sauvegarde de la liste des documents du programme dans le fichier
			// Archive
			for (i = 0; i < this.documents.size(); i++) {
				donnee = (String) this.documents.elementAt(i);
				new EcrireFichier(fichier_archive, donnee);
			}
			System.out.println("Le programme est archivé dans le fichier : "
					+ fichier_archive);
		} else {
			System.out.println("Erreur : le programme est deja archive");
		}*/
	}

	/**
	 *INUTILE ?????
	 *public void VerouillerDocuments(String Id_Doc) {
	 *System.out.println("Document verrouillé : " + Id_Doc);
	 *}
	 *public void DeverouillerDocuments(String Id_Doc) {
	 *System.out.println("Document déverrouillé : " + Id_Doc);
	 *}
	 */

	// Modification des informations d'un programme
	public void InsertionInfos(String Id_Prog, String Titre, String Thematique) {
		// your code here
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// A quoi ça sert ???
	///// ==> public Diffusion dIFFUSION; ==> A DECOMMENTER !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	public java.util.Collection dOCUMENT = new java.util.TreeSet();

	public Programme PROGRAMME;

	public boolean enDiffusion() {
		return false;
	}

	public void DiffuserProgramme(String Id_Prog) {
		// your code here
	}

	public void Ajout(String Id) {
		// your code here
	}

	public void RetraitProgramme(String Id_Prog) {
		// your code here
	}

	public void Planifier(java.util.Date Jour, int Heure, String IdeCanal) {
		// your code here
	}

	public boolean SetDiffusion() {
		// your code here
		return false;
	}

	public void ArreterDiffusionProgramme(String Id_Prog) {
		// your code here
	}

	public void RelancerDiffusionProgramme(String Id_Prog) {
		// your code here
	}

	public boolean retirerDocument(String Id) {
		// your code here
		return false;
	}

	public boolean retraitDoc(String Id) {
		// your code here
		return false;
	}
	
	public void setTitre(String id, String titre) throws SQLException {
		String requete = "UPDATE Programme SET titre = '" + titre + "' WHERE id = '" + id + "';";
		new Jdbc(requete);
	}
	
	public void setUtilMax(String id, String thematique) throws SQLException {
		String requete = "UPDATE Programme SET thematique = '" + thematique + "' WHERE id = '" + id + "';";
		new Jdbc(requete);
	}
	
	
}