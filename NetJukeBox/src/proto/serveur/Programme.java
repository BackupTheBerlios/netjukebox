package proto.serveur;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Programme de diffusion
 */
public class Programme {
	/**
	 * Cr�e le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(Programme.class);

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
	
	/**
	 * Dur�e du programme
	 */
	private long duree;

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
	 * Cr�ation du programme en base
	 * @param String titre
	 * @param String thematique
	 * @return Programme
	 * @throws SQLException 
	 */
	public static Programme create(String titre, String thematique) /*throws SQLException*/ {
		//PropertyConfigurator.configure("src/proto/serveur/log4j.prop");
		//PropertyConfigurator.configure("C:/Documents and Settings/Marie Rubini/Mes documents/workspace/NetJukeBox/proto/serveur/log4j.prop");
		
		logger.debug("D�marrage: Programme.create");
		
				
		String requete = "INSERT INTO programme (titre, thematique, duree) VALUES ('" + titre + "', '" + thematique + "', '0');";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien d�roul�e
		if (nbRows>0) {
			//On retourne ensuite un objet pour ce programme
			logger.debug("Arr�t: Programme.create");
			return Programme.getByTitre(titre);
		}
		
		//Sinon, retourne un objet vide
		logger.debug("Arr�t: Programme.create");
		return null;
	}
	
	/**
	 * Insancie un objet programme apr�s avoir r�cup�r� ces infos depuis la base � partir de son titre
	 * @param String titre
	 * @return Programme
	 * @throws SQLException 
	 */
	public static Programme getByTitre(String titre) /*throws SQLException*/ {
		logger.debug("D�marrage: Programme.getByTitre");
		logger.info ("Programme.getByTitre("+titre+")");
		
		String requete = "SELECT * FROM programme WHERE titre = '" + titre + "';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		// S'il y a un resultat
		if (resultats != null && resultats.size()>0) {
			
			//On prend le 1er �l�ment
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
			System.out.println("Th�matique: "+thematique);
			System.out.println("Dur�e: "+duree);
			System.out.println("-----------------------------");
			
			//On retourne l'objet
			logger.debug("Arr�t: Programme.getByTitre");
			return new Programme(id, titreProg, thematique, duree);
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
		logger.debug("Arr�t: Programme.getByTitre");
		return null;
	}
	
	/**
	 * Insancie un objet programme apr�s avoir r�cup�r� ces infos depuis la base � partir de son id
	 * @param String id
	 * @return Programme
	 * @throws SQLException 
	 */
	public static Programme getById(String id) /*throws SQLException*/ {
		logger.debug("D�marrage: Programme.getById");
		logger.info("Programme.getById("+id+")");
		
		String requete = "SELECT * FROM programme WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);

		// S'il y a un resultat
		if (resultats != null && resultats.size()>0) {
			
			//On prend le 1er �l�ment
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
			System.out.println("Th�matique: "+thematique);
			System.out.println("Dur�e: "+duree);
			System.out.println("-----------------------------");
			
			//On retourne l'objet
			logger.debug("Arr�t: Programme.getById");
			return new Programme(idProg, titre, thematique, duree);
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
		logger.debug("Arr�t: Programme.getById");
		return null;
	}
	
	/**
	 * Retourne un vecteur d'objets canaux instanci�s � partir de toutes les infos de la base 
	 * @return Hashtable
	 * @throws SQLException 
	 */
	public static Hashtable getAll() /*throws SQLException*/ {
		logger.debug("D�marrage: getAll");
				
		//On cr�e un vecteur pour contenir les objets programmes instanci�s
		Hashtable programmes = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id FROM programme;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		logger.info("Programme.getAll() : "+resultats.size()+" programme(s) trouv�(s)");
		
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
		
		//On retourne le vecteur contenant les objets programmes instanci�s
		logger.debug("Arr�t: getAll");
		return programmes;
	}
	
	/**
	 * D�truit les infos d'un programme contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		logger.debug("D�marrage: deleteById");
		//On supprime le programme de la base, en partant d'un id
		String requete = "DELETE FROM programme WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//On retourne le resultat de l'op�ration (succ�s/�chec)
		logger.debug("Arr�t: deleteById");
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
		logger.debug("D�marrage: modifier");
		String requete = "UPDATE programme SET titre = '" + titre + "', thematique = '" + thematique + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.titre = titre;
			this.thematique = thematique;
			logger.debug("Arr�t: modifier");
		}
		logger.debug("Arr�t: modifier");
		return nbRows>0;
	}
	
	/**
	 * D�truit le programme et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		logger.debug("D�marrage: supprimer");
		//On supprime les associations programme/document
		Document d;
		for (int i=0; i<documents.size(); i++){
			d = (Document)documents.get(i);
			d.retirerProgramme(id);
		}
		
		//On supprime les infos de la base
		logger.debug("Arr�t: supprimer");
		return Programme.deleteById(this.id);
	}
	
	/**
	 * Ajoute un document au programme
	 * @param Document doc
	 */
	public void ajouterDocument(Document doc) {
		logger.debug("D�marrage: ajouterDocument");
		//On ajoute le document au programme
		String requete = "INSERT INTO composer (id_prog, id_doc, calage) VALUES ('" + id + "', '" + doc.getId() + "', '" +duree+"');";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien d�roul�e
		if (nbRows>0) {
			
			//On met � jour le vecteurs d'association
			documents.put(duree, doc);
			
			//On met � jour la dur�e du programme
			this.setDuree(duree+doc.getDuree());
			
			//On signale au document cet ajout
			doc.ajouterProgramme(this);
			logger.debug("Arr�t: ajouterDocument");
		}
	}

	/**
	 * Retire un document du programme
	 * @param String id
	 * @return boolean
	 */
	public boolean retirerDocument(String calage) {
		logger.debug("D�marrage: retirerDocument");
		// On retire le document du programme
		String requete = "DELETE FROM composer WHERE id_prog='"+this.id+"' AND calage='"+calage+"';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la suppression s'est bien d�roul�e
		if (nbRows>0) {
			
			//On r�cup�re le doc
			Document doc = (Document)documents.get(Long.parseLong(calage));
			
			//On met � jour la dur�e du programme
			this.setDuree(duree-doc.getDuree());
			
			//On signale au document ce retrait
			doc.retirerProgramme(this.id);
			
			//On met � jour le vecteur d'association
			documents.remove(Long.parseLong(calage));
			
			logger.debug("Arr�t: retirerDocument");			
			return true;
		}
		
		//Sinon
		logger.debug("Arr�t: retirerDocument");
		return false;
	}
	
	/**
	 * Ajoute un programme (ensemble de documents) au programme
	 * @param Programme prog
	 */
	public void ajouterProgramme(Programme prog) {
		logger.debug("D�marrage: ajouterProgramme");
		//On r�cup�re la liste des docs du programme � ajouter
		Enumeration listeDocs = prog.getDocuments().elements();
		
		//Pour chaque doc, on l'ajoute au programme courant
		while (listeDocs.hasMoreElements()) {
			ajouterDocument((Document)listeDocs.nextElement());
		}
		logger.debug("Arr�t: ajouterProgramme");
	}
	
//#### GETTERS ####
//#################
	
	/**
	 * Retourne la liste des documents associ�s au programme
	 * @return Hashtable
	 */
	public /*pure*/ Hashtable listerDocuments() {
		return documents;
	}
	
	/**
	 * Retourne l'ensemble des attributs sous la forme d'un dictionnaire
	 * @return Dictionary
	 */
	public /*pure*/ Dictionary getAttributesDictionary() {
		
		Dictionary dico = new Hashtable();
		
		dico.put("id", id);
		dico.put("titre", titre);
		dico.put("duree", Long.toString(duree));
		dico.put("thematique", thematique);
		//dico.put("nbDocs", documents.size());
		
		//Liste des docs
		Vector vDocs = new Vector();
		Dictionary dicoDoc;
		Document doc;
		
		Enumeration horaires = documents.keys();
		while (horaires.hasMoreElements()) {
			long horaire = (Long)horaires.nextElement();
			doc = (Document)documents.get(horaire);
			dicoDoc = new Hashtable();
			dicoDoc.put("calage", Long.toString(horaire));
			dicoDoc.put("id", doc.getId());
			dicoDoc.put("titre", doc.getTitre());
			dicoDoc.put("duree", Long.toString(doc.getDuree()));
			vDocs.add(dicoDoc);
		}
		
		dico.put("documents", vDocs);
		dico.put("nbDocs", vDocs.size());
		return dico;
	}
	
	/**
	 * Retourne la liste des documents programm�s
	 * @return Hastable
	 */
	private /*pure*/ Hashtable getDocumentsProgrammes() {
		logger.info("Programme.getDocumentsProgrammes()");
		
		//On cr�e un vecteur pour contenir les objets documents instanci�s
		Hashtable docs = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id_doc, calage FROM composer WHERE id_prog = '"+id+"';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		logger.info("Programme.getDocumentsProgrammes() : "+resultats.size()+" docment(s) trouv�(s)");
		
		// Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id_doc"));
			long calage = (Long)dico.get("calage");
			docs.put(calage, Document.getById(id));
		}
		
		return docs;
	}
	
	/**
	 * Retourne l'identifiant du programme
	 * @return String
	 */
	public /*pure*/ String getId() {
		return id;
	}

	/**
	 * Retourne le titre du programme
	 * @return String
	 */
	public /*pure*/ String getTitre() {
		return titre;
	}

	/**
	 * Retourne la th�matique du programme
	 * @return String
	 */
	public /*pure*/ String getThematique() {
		return thematique;
	}
	
	/**
	 * Retourne les documents associ�s au programme
	 * @return Vector of Document
	 */
	public /*pure*/ Hashtable getDocuments() {
		return documents;
	}

	/**
	 * Retourne l'�tat du programme
	 * @return boolean
	 */
	public /*pure*/ boolean getEtat() {
		return etat;
	}
	
	/**
	 * Retourne la dur�e du programme
	 * @return long
	 */
	public /*pure*/ long getDuree() {
		return duree;
	}
	
//#### SETTERS ####
//#################
	
	/**
	 * Met � jour l'attribut duree
	 * @param String titre
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setDuree(long duree) /*throws SQLException*/ {
		logger.debug("D�marrage: setDuree");
		String requete = "UPDATE programme SET duree = '" + duree + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.duree = duree;
			logger.debug("Arr�t: setDuree");
		}
		logger.debug("Arr�t: setDuree");
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
		String requete = "UPDATE programme SET titre = '" + titre + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.titre = titre;
			logger.debug("Arr�t: setTitre");
		}
		logger.debug("Arr�t: setTitre");
		return nbRows>0;
	}
	
	/**
	 * Met � jour l'attribut thematique
	 * @param String thematique
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setThematique(String thematique) /*throws SQLException*/ {
		logger.debug("D�marrage: setThematique");
		String requete = "UPDATE programme SET thematique = '" + thematique + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.thematique = thematique;
			logger.debug("Arr�t: setThematique");
		}
		logger.debug("Arr�t: setThematique");
		return nbRows>0;
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

	public /*pure*/ boolean enDiffusion() {
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