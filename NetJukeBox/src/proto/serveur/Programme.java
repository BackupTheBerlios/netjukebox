package proto.serveur;

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
	private Vector documents = new Vector();
	

	// Initialisation du fichier archive
	String fichier_archive = "_Archive.txt";

	// Initialisation de l'état du programme non archivé
	String etat_archive = "false";

// CONSTRUCTEUR
//*********************************************************
	
	/**
	 * Constructeur
	 */
	public Programme() {
	
	}
	
	/**
	 * Constructeur
	 * @param String id
	 * @param titre
	 * @param thematique
	 */
	public Programme (String id, String titre, String thematique) {
		super();
		this.id = id;
		this.titre = titre;
		this.thematique = thematique;
	}

// METHODES STATIQUES
//*********************************************************

// METHODES DYNAMIQUES
//*********************************************************

	// Initialisation du proramme à vide
	public void Creer(String Id_Prog, String Titre, String Thematique) {
		this.id = Id_Prog;
		this.titre = Titre;
		this.thematique = Thematique;
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
	public Vector getDocuments() {
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
	public void ajouterDocument(Document Doc) {
		documents.addElement(Doc.getId());
		Doc.poserVerrou(id);
	}

	/**
	 * Retire un document du programme
	 * @param Doc
	 */
	public void retraitDocument(Document Doc) {
		documents.remove(Doc.getId());
		Doc.deverouillerDocument(this.id);
	}

	/**
	 * Retourne la liste des documents associés au programme
	 * @return Vector of Document
	 */
	public Vector listerDocuments() {
		return documents;
	}
	
	/**
	 * Ajoute un programme (ensemble de documents) au programme
	 * @param prog
	 */
	public void AjouterProgramme(Programme prog) {
		prog.listerDocuments();
		// System.out.println(Prog.v);
		int i;
		String donnee;
		for (i = 0; i < prog.documents.size(); i++) {
			donnee = (String) prog.getDocuments().elementAt(i);
			documents.addElement(donnee);
			System.out.println("Le document : " + donnee + " du programme : "
					+ prog.getId() + " a été inséré dans le programme : "
					+ this.id);
		}
	}

	// Sauvegarde du vecteur de document si cela n'a pas déjà été fait
	public void archiver() {
		int i;
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
		}
	}

	// //////////////////// INUTILE ????? ///////////////////////////////////
	public void VerouillerDocuments(String Id_Doc) {
		System.out.println("Document verrouillé : " + Id_Doc);
	}

	public void DeverouillerDocuments(String Id_Doc) {
		System.out.println("Document déverrouillé : " + Id_Doc);
	}

	// ////////////////////INUTILE ????? ///////////////////////////////////

	// Modification des informations d'un programme
	public void InsertionInfos(String Id_Prog, String Titre, String Thematique) {
		// your code here
	}

	// A quoi ça sert ???
	public Diffusion dIFFUSION;

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

	public void SetTitre(String Titre) {
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
}