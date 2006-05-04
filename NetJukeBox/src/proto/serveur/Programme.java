package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Enumeration;
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
	
	/**
	 * Durée du programme
	 */
	private long duree;

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
	public Programme (String id, String titre, String thematique, long duree) {
		this.id = id;
		this.titre = titre;
		this.thematique = thematique;
		this.duree = duree;
		this.documents = getDocumentsProgrammes();
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
	public static Programme create(String titre, String thematique) /*throws SQLException*/ {
		
		System.out.println("Programme.create()");
		
		String requete = "INSERT INTO programme (titre, thematique) VALUES ('" + titre + "', '" + thematique + "');";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien déroulée
		if (nbRows>0) {
			//On retourne ensuite un objet pour ce programme
			return Programme.getByTitre(titre);
		}
		
		//Sinon, retourne un objet vide
		return null;
	}
	
	/**
	 * Insancie un objet programme après avoir récupéré ces infos depuis la base à partir de son titre
	 * @param String titre
	 * @return Programme
	 * @throws SQLException 
	 */
	public static Programme getByTitre(String titre) /*throws SQLException*/ {
		
		System.out.println("Programme.getByTitre("+titre+")");
		
		String requete = "SELECT * FROM programme WHERE titre = '" + titre + "';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		// S'il y a un resultat
		if (resultats != null && resultats.size()>0) {
			
			//On prend le 1er élément
			Dictionary dico = (Dictionary) resultats.firstElement();
			
			//On mappe les champs
			String id = String.valueOf((Integer)dico.get("id"));
			String titreProg = (String)dico.get("titre");
			String thematique = (String)dico.get("thematique");
			long duree = (long)(Long)dico.get("duree");
			
			System.out.println("-------- Programme -----------");
			System.out.println("Nb de champs: "+dico.size());
			System.out.println("ID: "+id);
			System.out.println("Titre: "+titreProg);
			System.out.println("Thématique: "+thematique);
			System.out.println("Durée: "+duree);
			System.out.println("-----------------------------");
			
			//On retourne l'objet
			return new Programme(id, titreProg, thematique, duree);
		}
		
		/*
		//---------------- TEST SANS JDBC --------------------
		if (titre.equalsIgnoreCase("classic")) {
			//On retourne un objet programme configuré
			return new Programme("1", "classic", "Musique classique");
		}
		//----------------------------------------------------
		*/
		
		//Sinon, on retourne un objet vide
		return null;
	}
	
	/**
	 * Insancie un objet programme après avoir récupéré ces infos depuis la base à partir de son id
	 * @param String id
	 * @return Programme
	 * @throws SQLException 
	 */
	public static Programme getById(String id) /*throws SQLException*/ {
		
		System.out.println("Programme.getById("+id+")");
		
		String requete = "SELECT * FROM programme WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);

		// S'il y a un resultat
		if (resultats != null && resultats.size()>0) {
			
			//On prend le 1er élément
			Dictionary dico = (Dictionary) resultats.firstElement();
			
			//On mappe les champs
			String idProg = String.valueOf((Integer)dico.get("id"));
			String titre = (String)dico.get("titre");
			String thematique = (String)dico.get("thematique");
			long duree = (long)(Long)dico.get("duree");
			
			System.out.println("-------- Programme -----------");
			System.out.println("Nb de champs: "+dico.size());
			System.out.println("ID: "+idProg);
			System.out.println("Titre: "+titre);
			System.out.println("Thématique: "+thematique);
			System.out.println("Durée: "+duree);
			System.out.println("-----------------------------");
			
			//On retourne l'objet
			return new Programme(idProg, titre, thematique, duree);
		}
		
		/*
		//---------------- TEST SANS JDBC --------------------
		if (id.equalsIgnoreCase("1")) {
			//On retourne un objet programme configuré
			return new Programme("1", "classic", "Musique classique");
		}
		//----------------------------------------------------
		*/
		
		//Sinon, on retourne un objet vide
		return null;
	}
	
	/**
	 * Retourne un vecteur d'objets canaux instanciés à partir de toutes les infos de la base 
	 * @return Hashtable
	 * @throws SQLException 
	 */
	public static Hashtable getAll() /*throws SQLException*/ {
		
		System.out.println("Programme.getAll()");
		
		//On crée un vecteur pour contenir les objets programmes instanciés
		Hashtable programmes = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id FROM programme;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		System.out.println("Programme.getAll() : "+resultats.size()+" programme(s) trouvé(s)");
		
		// Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id"));
			programmes.put(id, Programme.getById(id));
		}
		
		/*
		//---------------- TEST SANS JDBC --------------------
		programmes.put("1", Programme.getById("1"));
		//----------------------------------------------------
		*/
		
		//On retourne le vecteur contenant les objets programmes instanciés
		return programmes;
	}
	
	/**
	 * Détruit les infos d'un programme contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		
		//On supprime le programme de la base, en partant d'un id
		String requete = "DELETE FROM programme WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//On retourne le resultat de l'opération (succès/échec)
		return nbRows>0;
	}
	
	
// METHODES DYNAMIQUES
//*********************************************************
	
	/**
	 * Modifie les attributs
	 * @param String titre
	 * @param String thematique
	 * @return boolean
	 */
	public boolean modifier(String titre, String thematique) {
		
		String requete = "UPDATE programme SET titre = '" + titre + "', thematique = '" + thematique + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.titre = titre;
			this.thematique = thematique;
		}
		return nbRows>0;
	}
	
	/**
	 * Détruit le programme et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		
		//On supprime les associations programme/document
		Document d;
		for (int i=0; i<documents.size(); i++){
			d = (Document)documents.get(i);
			d.retirerProgramme(id);
		}
		
		//On supprime les infos de la base
		return Programme.deleteById(this.id);
	}
	
	/**
	 * Ajoute un document au programme
	 * @param Document doc
	 */
	public void ajouterDocument(Document doc) {
		
		//On ajoute le document au programme
		String requete = "INSERT INTO composer (id_prog, id_doc, calage) VALUES ('" + id + "', '" + doc.getId() + "', '" +(duree+doc.getDuree())+"');";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien déroulée
		if (nbRows>0) {
			
			//On met à jour le vecteurs d'association
			documents.put(doc.getId(), doc);
			
			//On met à jour la durée du programme
			this.setDuree(duree+doc.getDuree());
			
			//On signale au document cet ajout
			doc.ajouterProgramme(this);
		}
	}

	/**
	 * Retire un document du programme
	 * @param String id
	 * @return boolean
	 */
	public boolean retirerDocument(String id) {
		
		// On retire le document du programme
		String requete = "DELETE FROM composer WHERE id_prog='"+this.id+"' AND id_doc='"+id+"');";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la suppression s'est bien déroulée
		if (nbRows>0) {
			
			//On met à jour le vecteur d'association
			documents.remove(id);
			
			//On signale au document ce retrait
			//doc.enleverProgramme(this.id);
			return true;
		}
		
		//Sinon
		return false;
	}
	
	/**
	 * Ajoute un programme (ensemble de documents) au programme
	 * @param Programme prog
	 */
	public void ajouterProgramme(Programme prog) {
		
		//On récupère la liste des docs du programme à ajouter
		Enumeration listeDocs = prog.getDocuments().elements();
		
		//Pour chaque doc, on l'ajoute au programme courant
		while (listeDocs.hasMoreElements()) {
			ajouterDocument((Document)listeDocs.nextElement());
		}
	}
	
//#### GETTERS ####
//#################
	
	/**
	 * Retourne la liste des documents associés au programme
	 * @return Hashtable
	 */
	public Hashtable listerDocuments() {
		return documents;
	}
	
	/**
	 * Retourne l'ensemble des attributs sous la forme d'un dictionnaire
	 * @return Dictionary
	 */
	public Dictionary getAttributesDictionary() {
		
		Dictionary dico = new Hashtable();
		
		dico.put("id", id);
		dico.put("titre", titre);
		dico.put("duree", Long.toString(duree));
		dico.put("thematique", thematique);
		dico.put("nbDocs", documents.size());
		
		/*Hashtable dicoDocs = new Hashtable();
		Enumeration horaires = documents.keys();
		while (horaires.hasMoreElements()) {
			long horaire = (Long)horaires.nextElement();
			dicoDocs.put(horaire, ((Document)documents.get(horaire)).getId());
		}
		
		dico.put("documents", dicoDocs);*/
		
		return dico;
	}
	
	/**
	 * Retourne la liste des documents programmés
	 * @return Hastable
	 */
	private Hashtable getDocumentsProgrammes() {
		System.out.println("Programme.getDocumentsProgrammes()");
		
		//On crée un vecteur pour contenir les objets documents instanciés
		Hashtable docs = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id_doc, calage FROM composer WHERE id_prog = '"+id+"';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		System.out.println("Programme.getDocumentsProgrammes() : "+resultats.size()+" docment(s) trouvé(s)");
		
		// Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id_doc"));
			docs.put(id, Document.getById(id));
		}
		
		return docs;
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
	 * Retourne la durée du programme
	 * @return long
	 */
	public long getDuree() {
		return duree;
	}
	
//#### SETTERS ####
//#################
	
	/**
	 * Met à jour l'attribut duree
	 * @param String titre
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setDuree(long duree) /*throws SQLException*/ {
		String requete = "UPDATE programme SET duree = '" + duree + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.duree = duree;
		}
		return nbRows>0;
	}

	/**
	 * Met à jour l'attribut titre
	 * @param String titre
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setTitre(String titre) /*throws SQLException*/ {
		String requete = "UPDATE programme SET titre = '" + titre + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.titre = titre;
		}
		return nbRows>0;
	}
	
	/**
	 * Met à jour l'attribut thematique
	 * @param String thematique
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setThematique(String thematique) /*throws SQLException*/ {
		String requete = "UPDATE programme SET thematique = '" + thematique + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.thematique = thematique;
		}
		return nbRows>0;
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
	
	// A quoi ça sert ???
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

	public boolean retraitDoc(String Id) {
		// your code here
		return false;
	}

}