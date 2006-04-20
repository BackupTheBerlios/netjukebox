package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
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
	 * Th�matique du programme
	 */
	private String thematique;

	/**
	 * Etat du programme
	 */
	private boolean etat = false;

	/**
	 * Liste des documents associ�s au programme
	 */
	private Hashtable documents = new Hashtable();
	

	// Initialisation du fichier archive
	String fichier_archive = "_Archive.txt";

	// Initialisation de l'�tat du programme non archiv�
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
	 * Cr�ation du programme en base
	 * @param String titre
	 * @param String thematique
	 * @return Programme
	 * @throws SQLException 
	 */
	public static Programme create(String titre, String thematique) throws SQLException {
		
		String requete = "INSERT INTO programme ('" + titre + "', '" + thematique + "');";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien d�roul�e
		if (nbRows>0) {
			//On retourne ensuite un objet pour ce programme
			return Programme.getByTitre(titre);
		}
		
		//Sinon, retourne un objet vide
		return null;
	}
	
	/**
	 * Insancie un objet programme apr�s avoir r�cup�r� ces infos depuis la base � partir de son titre
	 * @param String titre
	 * @return Programme
	 * @throws SQLException 
	 */
	public static Programme getByTitre(String titre) throws SQLException {
		
		String requete = "SELECT * FROM programme WHERE titre = '" + titre + "';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		// S'il y a un resultat
		if (resultats != null) {
			
			//On prend le 1er �l�ment
			Dictionary dico = (Dictionary) resultats.firstElement();
			
			//On mappe les champs
			String id = (String)dico.get("id");
			String titreProg = (String)dico.get("titre");
			String thematique = (String)dico.get("thematique");
			
			//On retourne l'objet
			return new Programme(id, titreProg, thematique);
		}
		
		/*
		//---------------- TEST SANS JDBC --------------------
		if (titre.equalsIgnoreCase("classic")) {
			//On retourne un objet programme configur�
			return new Programme("1", "classic", "Musique classique");
		}
		//----------------------------------------------------
		*/
		
		//Sinon, on retourne un objet vide
		return null;
	}
	
	/**
	 * Insancie un objet programme apr�s avoir r�cup�r� ces infos depuis la base � partir de son id
	 * @param String id
	 * @return Programme
	 * @throws SQLException 
	 */
	public static Programme getById(String id) throws SQLException {
		
		String requete = "SELECT * FROM programme WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);

		// S'il y a un resultat
		if (resultats != null) {
			
			//On prend le 1er �l�ment
			Dictionary dico = (Dictionary) resultats.firstElement();
			
			//On mappe les champs
			String idProg = (String)dico.get("id");
			String titre = (String)dico.get("titre");
			String thematique = (String)dico.get("thematique");
			
			//On retourne l'objet
			return new Programme(idProg, titre, thematique);
		}
		
		/*
		//---------------- TEST SANS JDBC --------------------
		if (id.equalsIgnoreCase("1")) {
			//On retourne un objet programme configur�
			return new Programme("1", "classic", "Musique classique");
		}
		//----------------------------------------------------
		*/
		
		//Sinon, on retourne un objet vide
		return null;
	}
	
	/**
	 * Retourne un vecteur d'objets canaux instanci�s � partir de toutes les infos de la base 
	 * @return Hashtable
	 * @throws SQLException 
	 */
	public static Hashtable getAll() throws SQLException {
		
		//On cr�e un vecteur pour contenir les objets programmes instanci�s
		Hashtable programmes = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id FROM programme;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		// Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = (String)dico.get("id");
			programmes.put(id, Programme.getById(id));
		}
		
		/*
		//---------------- TEST SANS JDBC --------------------
		programmes.put("1", Programme.getById("1"));
		//----------------------------------------------------
		*/
		
		//On retourne le vecteur contenant les objets programmes instanci�s
		return programmes;
	}
	
	/**
	 * D�truit les infos d'un programme contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) throws SQLException {
		
		//On supprime le programme de la base, en partant d'un id
		String requete = "DELETE * FROM programme WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//On retourne le resultat de l'op�ration (succ�s/�chec)
		return nbRows>0;
	}
	
	
// METHODES DYNAMIQUES
//*********************************************************

	/**
	 * D�truit le programme et ses infos en base
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
	 * Retourne la th�matique du programme
	 * @return String
	 */
	public String getThematique() {
		return thematique;
	}
	
	/**
	 * Retourne les documents associ�s au programme
	 * @return Vector of Document
	 */
	public Hashtable getDocuments() {
		return documents;
	}

	/**
	 * Retourne l'�tat du programme
	 * @return boolean
	 */
	public boolean getEtat() {
		return etat;
	}

	/**
	 * Met � jour l'attribut titre
	 * @param String titre
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setTitre(String titre) throws SQLException {
		String requete = "UPDATE programme SET titre = '" + titre + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.titre = titre;
		}
		return nbRows>0;
	}
	
	/**
	 * Met � jour l'attribut thematique
	 * @param String thematique
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setThematique(String thematique) throws SQLException {
		String requete = "UPDATE programme SET thematique = '" + thematique + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.thematique = thematique;
		}
		return nbRows>0;
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
		
		//On met � jour le vecteurs d'association
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
		
		//On met � jour le vecteur d'association
		documents.remove(doc.getId());
		
		//On signale au document ce retrait
		doc.enleverProgramme(this.id);
		
		return doc;
	}

	/**
	 * Retourne la liste des documents associ�s au programme
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
					+ prog.getId() + " a �t� ins�r� dans le programme : "
					+ this.id);
		}*/
	}

	// Sauvegarde du vecteur de document si cela n'a pas d�j� �t� fait
	public void archiver() {
		/*int i;
		String donnee;
		if (etat_archive == "false") {
			etat_archive = "true";
			// Cr�ation du nom complet du fichier Archive de tous les documents
			// composant le programme
			fichier_archive = this.id + fichier_archive;
			// Sauvegarde de la liste des documents du programme dans le fichier
			// Archive
			for (i = 0; i < this.documents.size(); i++) {
				donnee = (String) this.documents.elementAt(i);
				new EcrireFichier(fichier_archive, donnee);
			}
			System.out.println("Le programme est archiv� dans le fichier : "
					+ fichier_archive);
		} else {
			System.out.println("Erreur : le programme est deja archive");
		}*/
	}

	/**
	 *INUTILE ?????
	 *public void VerouillerDocuments(String Id_Doc) {
	 *System.out.println("Document verrouill� : " + Id_Doc);
	 *}
	 *public void DeverouillerDocuments(String Id_Doc) {
	 *System.out.println("Document d�verrouill� : " + Id_Doc);
	 *}
	 */
	
	// A quoi �a sert ???
	///// ==> public Diffusion dIFFUSION; ==> A DECOMMENTER !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

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

}