package proto.serveur;

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
	private int compteur_verrou = 0;

	// A quoi ça sert ???
	private java.util.Collection programmes = new java.util.TreeSet();

	private Contrat contrat;
	
// CONSTRUCTEUR
//********************************************
	
	public Document() {
		
	}
	
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

// METHODES DYNAMIQUES
//********************************************

	public void Creer(String Id_Doc, String Titre, int Duree, String Jour,
			String Mois, String Annee, String Source, String Langue, String Genre,
			String Fichier) {
		
		this.id = Id_Doc;
		this.titre = Titre;
		this.jour = Jour;
		this.mois = Mois;
		this.annee = Annee;
		this.source = Source;
		this.langue = Langue;
		this.genre = Genre;
		this.fichier = Fichier;
		this.duree = Duree;
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
	 * Pose un verrou sur le document
	 */
	public void poserVerrou() {
		compteur_verrou = compteur_verrou + 1;
		System.out.println("Le compteur de verrou = " + compteur_verrou);
	}

	/**
	 * Enlève un verrou sur le document
	 */
	public void deverouillerDocument() {
		compteur_verrou = compteur_verrou - 1;
		System.out.println("Le compteur de verrou = " + compteur_verrou);
	}
	
	/**
	 * Affiche le nombre de verrous sur le document
	 * Affiche les programmes vérrouillant le document
	 */
	public void compterVerrouProgramme() {
		System.out.println("Le document : " + id + " est verrouillé : " + compteur_verrou + " fois");
	}
	
	/**
	 * Affiche la liste des programmes vérrouillant le document
	 */
	public void getProgrammesArchives() {
		int i;
		if (verrous.size() != 0) {
			for (i = 0; i < verrous.size(); i++) {
				String prog = (String) verrous.elementAt(i);
				System.out.println("Le document : " + id + " est verrouillé par le programme : " + prog);
			}
		} else {
			System.out.println("Le document : " + id + " est verrouillé par aucun programme");
		}
	}

	/**
	 * Ajoute l'id du programme qui insère le document dans sa liste
	 * Appel la fonction de pose d'un verrou
	 * @param String
	 */
	public void ajouterProgramme(String idProg) {
		verrous.addElement(idProg);
		System.out.println("Document verrouillé : " + this.id);
		poserVerrou();
	}

	/**
	 * Supprime l'id du programme qui supprime le document dans sa liste
	 * Appel la fonction de déverrouillage
	 * @param String
	 */
	public void enleverProgramme(String idProg) {
		verrous.remove(idProg);
		System.out.println("Document déverrouillé : " + this.id);
		deverouillerDocument();
	}
	
	/**
	 * Retourne la date de création du document
	 * @return Date
	 */
	public java.util.Date GetDate_Creation() {
		String Date = jour + "-"+ mois + "-" + annee;
		TransformeDate TD = new TransformeDate(Date);
		System.out.println(TD.d);
		return TD.d;
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

	public void supprimer() {
		// your code here
	}

	public boolean suppression() {
		// your code here
		return false;
	}

	public boolean supprimerInfos() {
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