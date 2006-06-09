package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * Programme de diffusion
 */
public class Programme {
	/**
	 * Crée le logger de la classe
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
	private TreeMap documents = new TreeMap();
	
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
		//this.documents = getDocumentsProgrammes();
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
		logger.debug("Démarrage: modifier");
		String requete = "UPDATE programme SET titre = '" + titre.replace("'", "''") + "', thematique = '" + thematique.replace("'", "''") + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.titre = titre;
			this.thematique = thematique;
			logger.debug("Arrêt: modifier");
		}
		logger.debug("Arrêt: modifier");
		return nbRows>0;
	}
	
	/**
	 * Détruit le programme et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		logger.debug("Démarrage: supprimer");
		//On supprime les associations programme/document
		Document d;
		for (int i=0; i<documents.size(); i++){
			d = (Document)documents.get(i);
			d.retirerProgramme(id);
		}
		
		//On supprime les infos de la base
		logger.debug("Arrêt: supprimer");
		return ProgrammeFactory.deleteById(this.id);
	}
	
	/**
	 * Ajoute un document au programme
	 * @param Document doc
	 */
	public void ajouterDocument(Document doc) {
		logger.debug("Démarrage: ajouterDocument");
		//On ajoute le document au programme
		String requete = "INSERT INTO composer (id_prog, id_doc, calage) VALUES ('" + id + "', '" + doc.getId() + "', '" +duree+"');";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien déroulée
		if (nbRows>0) {
			
			//On met à jour le vecteurs d'association
			documents.put(duree, doc);
			
			//On met à jour la durée du programme
			this.setDuree(duree+doc.getDuree());
			
			//On signale au document cet ajout
			doc.ajouterProgramme(this);
			logger.debug("Arrêt: ajouterDocument");
		}
	}

	/**
	 * Retire un document du programme
	 * @param long calage
	 * @return boolean
	 */
	public boolean retirerDocument(long calage) {
		logger.debug("Démarrage: retirerDocument");
		// On retire le document du programme
		String requete = "DELETE FROM composer WHERE id_prog='"+this.id+"' AND calage='"+calage+"';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la suppression s'est bien déroulée
		if (nbRows>0) {
			
			//On récupère le doc
			Document doc = (Document)documents.get(calage);
			
			//On met à jour la durée du programme
			this.setDuree(duree-doc.getDuree());
			
			//On signale au document ce retrait
			doc.retirerProgramme(this.id);
			
			//On met à jour le vecteur d'association
			documents.remove(calage);
			
			logger.debug("Arrêt: retirerDocument");			
			return true;
		}
		
		//Sinon
		logger.debug("Arrêt: retirerDocument");
		return false;
	}
	
	/**
	 * Retire un document du programme
	 * @param String id
	 * @return boolean
	 */
	public boolean retirerDocument(String id) {
		logger.debug("Démarrage: retirerDocument");
		// On retire le document du programme
		String requete = "DELETE FROM composer WHERE id_prog='"+this.id+"' AND id_doc='"+id+"';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la suppression s'est bien déroulée
		if (nbRows>0) {
			
			Iterator calages = documents.keySet().iterator();
			while (calages.hasNext()) {
				long calage = (Long)calages.next();
				Document doc = (Document)documents.get(calage);
				if (doc.getId().equals(id)) {
					documents.remove(calage);
					
					//On met à jour la durée du programme
					setDuree(duree-doc.getDuree());
				}
			}
			
			logger.debug("Arrêt: retirerDocument");			
			return true;
		}
		
		//Sinon
		logger.debug("Arrêt: retirerDocument");
		return false;
	}
	
	/**
	 * Ajoute un programme (ensemble de documents) au programme
	 * @param Programme prog
	 */
	public void ajouterProgramme(Programme prog) {
		logger.debug("Démarrage: ajouterProgramme");
		//On récupère la liste des docs du programme à ajouter
		TreeMap docs = prog.getDocuments();
		Iterator listeDocs = docs.keySet().iterator();
		//Enumeration listeDocs = prog.getDocuments().elements();
		
		//Pour chaque doc, on l'ajoute au programme courant
		while (listeDocs.hasNext()) {
			ajouterDocument((Document)docs.get(listeDocs.next()));
		}
		logger.debug("Arrêt: ajouterProgramme");
	}
	
//#### GETTERS ####
//#################
	
	/**
	 * Retourne la liste des documents associés au programme
	 * @return Hashtable
	 */
	public /*pure*/ TreeMap listerDocuments() {
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
		
		Iterator horaires = documents.keySet().iterator();
		while (horaires.hasNext()) {
			long horaire = (Long)horaires.next();
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
	 * Retourne la liste des documents programmés
	 * @return Hastable
	 */
	public /*pure*/ void setDocumentsProgrammes() {
		logger.info("Programme.getDocumentsProgrammes()");
		
		//On crée un vecteur pour contenir les objets documents instanciés
		TreeMap docs = new TreeMap();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id_doc, calage FROM composer WHERE id_prog = '"+id+"';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		System.out.println("Programme.getDocumentsProgrammes() : "+resultats.size()+" document(s) trouvé(s)");
		logger.info("Programme.getDocumentsProgrammes() : "+resultats.size()+" docment(s) trouvé(s)");
		
		// Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id_doc"));
			long calage = (Long)dico.get("calage");
			docs.put(calage, DocumentFactory.getById(id));
		}
		
		this.documents=docs;
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
	 * Retourne la thématique du programme
	 * @return String
	 */
	public /*pure*/ String getThematique() {
		return thematique;
	}
	
	/**
	 * Retourne les documents associés au programme
	 * @return Vector of Document
	 */
	public /*pure*/ TreeMap getDocuments() {
		return documents;
	}

	/**
	 * Retourne l'état du programme
	 * @return boolean
	 */
	public /*pure*/ boolean getEtat() {
		return etat;
	}
	
	/**
	 * Retourne la durée du programme
	 * @return long
	 */
	public /*pure*/ long getDuree() {
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
		logger.debug("Démarrage: setDuree");
		String requete = "UPDATE programme SET duree = '" + duree + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.duree = duree;
			logger.debug("Arrêt: setDuree");
		}
		logger.debug("Arrêt: setDuree");
		return nbRows>0;
	}

	/**
	 * Met à jour l'attribut titre
	 * @param String titre
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setTitre(String titre) /*throws SQLException*/ {
		logger.debug("Démarrage: setTitre");
		String requete = "UPDATE programme SET titre = '" + titre.replace("'", "''") + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.titre = titre;
			logger.debug("Arrêt: setTitre");
		}
		logger.debug("Arrêt: setTitre");
		return nbRows>0;
	}
	
	/**
	 * Met à jour l'attribut thematique
	 * @param String thematique
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setThematique(String thematique) /*throws SQLException*/ {
		logger.debug("Démarrage: setThematique");
		String requete = "UPDATE programme SET thematique = '" + thematique.replace("'", "''") + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.thematique = thematique;
			logger.debug("Arrêt: setThematique");
		}
		logger.debug("Arrêt: setThematique");
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