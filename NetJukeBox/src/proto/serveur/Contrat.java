package proto.serveur;

import java.sql.SQLException;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * Contrat {durée_contrat=date_expiration-date_signature}
 */
public class Contrat {
	/**
	 * Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(Contrat.class);

	/**
	 * Identifiant
	 */
	private String id;
	
	/**
	 * Titre
	 */
	private String titre;
	
	/**
	 * Date de signature
	 */
	private GregorianCalendar dateSignature;

	/**
	 * Date d'expiration
	 */
	private GregorianCalendar dateExpiration;

	/**
	 * Mode de règlement
	 */
	private String modeReglement;

	/**
	 * Type de contrat
	 */
	private String type;

	/**
	 * Documents
	 */
	private Hashtable documents = new Hashtable();

	/**
	 * Contractant
	 */
	private Contractant contractant;

// CONSTRUCTEUR
//***************************************
	
	/**
	 * Constructeur
	 * @param String id
	 * @param String titre
	 * @param Date dateSignatare
	 * @param Date dateExpiration
	 * @param String idContractant
	 * @param String modeReglement
	 * @param String type
	 */
	public Contrat(String id, String titre, GregorianCalendar dateSignature, GregorianCalendar dateExpiration, String idContractant, String modeReglement, String type) {
		this.id = id;
		this.titre = titre;
		this.dateSignature = dateSignature;
		this.dateExpiration = dateExpiration;
		this.modeReglement = modeReglement;
		this.type = type;
		
		this.contractant = (Contractant)ContractantFactory.getById(idContractant);
		//this.documents = getDocumentsAssocies();
	}

	
// METHODES DYNAMIQUES
//***************************************
	
	/**
	 * Retourne la liste des documents associés à ce contrat
	 * @return Hastable
	 */
	public void setDocumentsAssocies() {
		
		logger.debug("Démarrage: setDocumentsAssocies");
		
		//On crée un vecteur pour contenir les objets documents instanciés
		Hashtable docs = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id_doc FROM contratdoc WHERE id_contrat = '"+id+"';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		logger.info("Contrat.getDocumentsAssocies() : "+resultats.size()+" document(s) trouvé(s)");
		
		// Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = (String)dico.get("id_doc");
			docs.put(id, DocumentFactory.getById(id));
		}
		
		this.documents=docs;
		logger.debug("Arrêt: setDocumentsAssocies");
	}
	
	/**
	 * Ajoute un document
	 * @param Programme prog
	 */
	public void ajouterDocument(Document doc) {
		logger.debug("Démarrage: ajouterDocument");
		//On ajoute le document au contrat
		String requete = "INSERT INTO contratdoc (id_contrat, id_doc) VALUES ('" + id + "', '" + doc.getId() + "');";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien déroulée
		if (nbRows>0) {
			
			//On met à jour le vecteurs d'association
			documents.put(doc.getId(), doc);
			
			//On signale au document cet ajout
			doc.ajouterContrat(this);
		}
		
		logger.info("Document ajouté");
		logger.debug("Arrêt: ajouterDocument");
	}

	/**
	 * Supprime de sa liste le document
	 * @param boolean
	 */
	public boolean retirerDocument(String idDoc) {
		logger.debug("Démarrage: ajouterDocument");
		// On retire le document du CONTRAT
		String requete = "DELETE FROM contratdoc WHERE id_contrat='"+this.id+"' AND id_doc='"+idDoc+"';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la suppression s'est bien déroulée
		if (nbRows>0) {
			
			//On récupère le doc
			Document doc = (Document)documents.get(idDoc);
			
			//On signale au document ce retrait
			doc.retirerContrat(this.id);
			
			//On met à jour le vecteur d'association
			documents.remove(idDoc);
			logger.debug("Arrêt: ajouterDocument");
			return true;
		}
		
		//Sinon
		logger.debug("Arrêt: ajouterDocument");
		return false;
	}

	/**
	 * Modifie les attributs
	 * @param String titre
	 * @param int jourSignature
	 * @param int moisSignature
	 * @param int anneeSignature
	 * @param int jourExpiration
	 * @param int moisExpiration
	 * @param int anneExpiration
	 * @param String idContractant
	 * @param String modeReglement
	 * @param String type
	 * @return boolean
	 */
	public boolean modifier(String titre, int jourSignature, int moisSignature, int anneeSignature,
			int jourExpiration, int moisExpiration, int anneeExpiration, String idContractant,
			String modeReglement, String type) {
		logger.debug("Démarrage: modifier");
		
		String titreSQL = titre.replace("'", "''");
		
		//On assemble la date de signature
		GregorianCalendar dateSignature = new GregorianCalendar(anneeSignature, moisSignature-1, jourSignature);
		
		//On assemble la date d'expiration
		GregorianCalendar dateExpiration = new GregorianCalendar(anneeExpiration, moisExpiration-1, jourExpiration);
		
		String requete = "UPDATE contrat SET signature = '" + dateSignature.getTimeInMillis() + "', expiration = '" + dateExpiration.getTimeInMillis() +
			"', id_contractant = '"+ idContractant + "', reglement = '" + modeReglement + "', type = '" + type + "', titre = '" + titreSQL + "' WHERE id = '" + id + "';";
		
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.titre = titre;
			this.dateSignature = dateSignature;
			this.dateExpiration = dateExpiration;
			this.contractant = (Contractant)ContractantFactory.getById(idContractant);
			this.modeReglement = modeReglement;
			this.type = type;
		}
		logger.debug("Arrêt: modifier");
		return nbRows>0;
	}
	
	/**
	 * Détruit le contrat et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		logger.debug("Démarrage: supprimer");
		//On supprime les associations contrat/document
		for (int i=0; i<documents.size(); i++){
			Document d = (Document)documents.get(i);
			d.retirerContrat(this.id);
		}
		
		//On supprime l'association contrat/contractant
		contractant.retirerContrat(this.id);
		
		//On supprime les infos de la base
		logger.debug("Arrêt: supprimer");
		return ContratFactory.deleteById(id);
	}
	
//#### GETTERS ####
//#################	
	
	/**
	 * Calcule la durée du contrat
	 */
	public /*pure*/ long getDureeContrat() {
		// your code here
		return dateExpiration.getTimeInMillis()-dateSignature.getTimeInMillis();
	}
	
	/**
	 * Retourne l'ensemble des attributs sous la forme d'un dictionnaire
	 * @return Dictionary
	 */
	public /*pure*/ Dictionary getAttributesDictionary() {
		
		Dictionary dico = new Hashtable();
		
		dico.put("id", id);
		dico.put("titre", titre);
		dico.put("dateSignature", dateSignature.get(GregorianCalendar.DAY_OF_MONTH)+"/"+(dateSignature.get(GregorianCalendar.MONTH)+1)+"/"+dateSignature.get(GregorianCalendar.YEAR));
		dico.put("dateExpiration", dateExpiration.get(GregorianCalendar.DAY_OF_MONTH)+"/"+(dateExpiration.get(GregorianCalendar.MONTH)+1)+"/"+dateExpiration.get(GregorianCalendar.YEAR));
		
		dico.put("jourSignature", dateSignature.get(GregorianCalendar.DAY_OF_MONTH));
		dico.put("moisSignature", (dateSignature.get(GregorianCalendar.MONTH)+1));
		dico.put("anneeSignature", dateSignature.get(GregorianCalendar.YEAR));
		dico.put("jourExpiration", dateExpiration.get(GregorianCalendar.DAY_OF_MONTH));
		dico.put("moisExpiration", (dateExpiration.get(GregorianCalendar.MONTH)+1));
		dico.put("anneeExpiration", dateExpiration.get(GregorianCalendar.YEAR));
		
		dico.put("modeReglement", modeReglement);
		dico.put("type", type);
		
		//Liste des docs
		Vector vDocs = new Vector();
		Dictionary dicoDoc;
		Document doc;
		Enumeration listeDocs = documents.elements();
		while (listeDocs.hasMoreElements()) {
			doc = (Document)listeDocs.nextElement();
			dicoDoc = new Hashtable();
			dicoDoc.put("id", doc.getId());
			dicoDoc.put("titre", doc.getTitre());
			vDocs.add(dicoDoc);
		}
		dico.put("documents", vDocs);
		dico.put("nbDocs", vDocs.size());
		
		//Signataire
		Dictionary dicoContractant = new Hashtable();
		dicoContractant.put("id", contractant.getId());
		dicoContractant.put("nom", contractant.getNom());
		dicoContractant.put("type", contractant.getType());
		dico.put("contractant", dicoContractant);
		
		return dico;
	}
	
	/**
	 * @return Renvoie titre.
	 */
	public /*pure*/ String getTitre() {
		return titre;
	}

	/**
	 * @return Renvoie dateExpiration.
	 */
	public /*pure*/ GregorianCalendar getDateExpiration() {
		return dateExpiration;
	}

	/**
	 * @return Renvoie dateSignature.
	 */
	public /*pure*/ GregorianCalendar getDateSignature() {
		return dateSignature;
	}

	/**
	 * @return Renvoie documents.
	 */
	public /*pure*/ Hashtable getDocuments() {
		return documents;
	}

	/**
	 * @return Renvoie id.
	 */
	public /*pure*/ String getId() {
		return id;
	}

	/**
	 * @return Renvoie modeReglement.
	 */
	public /*pure*/ String getModeReglement() {
		return modeReglement;
	}

	/**
	 * @return Renvoie le contractant.
	 */
	public /*pure*/ Contractant getContractant() {
		return contractant;
	}

	/**
	 * @return Renvoie type.
	 */
	public /*pure*/ String getType() {
		return type;
	}

//#### SETTERS ####
//#################		
	
	/**
	 * @param dateExpiration dateExpiration à définir.
	 */
	public boolean setDateExpiration(int jour, int mois, int annee) {
		logger.debug("Démarrage: setDateExpiration");
		GregorianCalendar date = new GregorianCalendar(annee, mois-1, jour);
		
		String requete = "UPDATE contrat SET expiration = '" + date.getTimeInMillis() + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.dateExpiration = date;
		}
		logger.debug("Arrêt: setDateExpiration");
		return nbRows>0;
	}

	/**
	 * @param dateSignature dateSignature à définir.
	 */
	public boolean setDateSignature(int jour, int mois, int annee) {
		logger.debug("Démarrage: setDateSignature");
		GregorianCalendar date = new GregorianCalendar(annee, mois-1, jour);
		
		String requete = "UPDATE contrat SET signature = '" + date.getTimeInMillis() + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.dateSignature = date;
		}
		logger.debug("Arrêt: setDateSignature");
		return nbRows>0;
	}

	/**
	 * @param modeReglement modeReglement à définir.
	 */
	public boolean setModeReglement(String modeReglement) {
		logger.debug("Démarrage: setModeReglement");
		String requete = "UPDATE contrat SET reglement = '" + modeReglement + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.modeReglement = modeReglement;
		}
		logger.debug("Arrêt: setModeReglement");
		return nbRows>0;
	}

	/**
	 * @param signataire signataire à définir.
	 */
	public boolean setContractant(String idContractant) {
		logger.debug("Démarrage: setContractant");
		String requete = "UPDATE contrat SET id_contractant = '" + idContractant + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.contractant = (Contractant)ContractantFactory.getById(idContractant);
		}
		logger.debug("Arrêt: setContractant");
		return nbRows>0;
	}

	/**
	 * @param type type à définir.
	 */
	public boolean setType(String type) {
		logger.debug("Démarrage: setType");
		String requete = "UPDATE contrat SET type = '" + type + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.type = type;
		}
		logger.debug("Arrêt: setType");
		return nbRows>0;
	}
	
	/**
	 * @param type titre à définir.
	 */
	public boolean setTitre(String titre) {
		logger.debug("Démarrage: setTitre");
		String titreSQL = titre.replace("'", "''");
		String requete = "UPDATE contrat SET titre = '" + titreSQL + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.titre = titre;
		}
		logger.debug("Arrêt: setTitre");
		return nbRows>0;
	}

}