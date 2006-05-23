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
	
// METHODES STATIQUES
//***************************************
	
//	/**
//	 * Création du contrat en base
//	 * @param String titre
//	 * @param int jourSignature
//	 * @param int moisSignature
//	 * @param int anneeSignature
//	 * @param int jourExpiration
//	 * @param int moisExpiration
//	 * @param int anneExpiration
//	 * @param String idContractant
//	 * @param String modeReglement
//	 * @param String type
//	 * @return Contrat
//	 */
//	public static Contrat create(String titre, int jourSignature, int moisSignature, int anneeSignature,
//			int jourExpiration, int moisExpiration, int anneeExpiration, String idContractant,
//			String modeReglement, String type) /*throws SQLException*/ {
//		
//		System.out.println("Contrat.create()");
//		
//		//On assemble la date de signature
//		GregorianCalendar dateSignature = new GregorianCalendar(anneeSignature, moisSignature-1, jourSignature);
//		
//		//On assemble la date d'expiration
//		GregorianCalendar dateExpiration = new GregorianCalendar(anneeExpiration, moisExpiration-1, jourExpiration);
//		
//		//On crée le contractant dans la base
//		String requete = "INSERT INTO contrat (titre, signature, expiration, id_contractant, reglement, type) VALUES ('" +
//			titre + "', '" + dateSignature.getTimeInMillis() + "', '" + dateExpiration.getTimeInMillis() +
//			"', '" + idContractant + "', '" + modeReglement + "', '" + type + "');"; 
//		
//		Jdbc base = Jdbc.getInstance();
//		int nbRows = base.executeUpdate(requete);
//		
//		//Si l'insertion s'est bien déroulée
//		if (nbRows>0) {
//			
//			//On instancie l'objet correspondant
//			Contrat c = Contrat.getByTitre(titre);
//			
//			//On informe le contractant de la création du contrat
//			Contractant ct = c.getContractant();
//			ct.ajouterContrat(c);
//			
//			//On retourne ensuite un objet pour ce contrat
//			return c;
//		}
//		
//		//Sinon on retourne un objet vide
//		return null;
//	}
//	
//	/**
//	 * Instancie un objet contrat après avoir récupéré ces infos depuis la base à partir de son id
//	 * @param String id
//	 * @return Contrat
//	 */
//	public static Contrat getById(String id) /*throws SQLException*/ {
//		
//		System.out.println("Contrat.getById("+id+")");
//		
//		String requete = "SELECT * FROM contrat WHERE id = '" + id + "';";
//
//		Jdbc base = Jdbc.getInstance();
//		Vector resultats = base.executeQuery(requete);
//		
//		//S'il y a un resultat
//		if (resultats!=null && resultats.size()>0) {
//			
//			//On prend le 1er élément
//			Dictionary dico = (Dictionary) resultats.firstElement();
//			
//			//On mappe les champs
//			String idCon = String.valueOf((Integer)dico.get("id"));
//			String titre = (String)dico.get("titre");
//			String idContractant = (String)dico.get("id_contractant");
//			String modeReglement = (String)dico.get("reglement");
//			String type = (String)dico.get("type");
//			
//			//On assemble la date de signature
//			GregorianCalendar dateSignature = new GregorianCalendar();
//			dateSignature.setTimeInMillis(Long.valueOf((String)dico.get("signature")));
//			int jourSignature = dateSignature.get(GregorianCalendar.DATE);
//			int moisSignature = dateSignature.get(GregorianCalendar.MONTH);
//			int anneeSignature = dateSignature.get(GregorianCalendar.YEAR);
//			
//			//On assemble la date d'expiration
//			GregorianCalendar dateExpiration = new GregorianCalendar();
//			dateExpiration.setTimeInMillis(Long.valueOf((String)dico.get("expiration")));
//			int jourExpiration = dateExpiration.get(GregorianCalendar.DATE);
//			int moisExpiration = dateExpiration.get(GregorianCalendar.MONTH);
//			int anneeExpiration = dateExpiration.get(GregorianCalendar.YEAR);
//			
//			System.out.println("-------- Contrat -----------");
//			System.out.println("Nb de champs: "+dico.size());
//			System.out.println("ID: "+idCon);
//			System.out.println("Titre: "+titre);
//			System.out.println("Signature: "+jourSignature+"/"+moisSignature+"/"+anneeSignature);
//			System.out.println("Expiration: "+jourExpiration+"/"+moisExpiration+"/"+anneeExpiration);
//			System.out.println("Contractant: "+idContractant);
//			System.out.println("Mode de règlement: "+modeReglement);
//			System.out.println("Type: "+type);
//			System.out.println("-----------------------------");
//			
//			//On retourne l'objet
//			return new Contrat(idCon, titre, dateSignature, dateExpiration, idContractant, modeReglement, type);
//		}
//		
//		//Sinon, on retourne un objet vide
//		return null;
//	}
//	
//	/**
//	 * Instancie un objet contrat après avoir récupéré ces infos depuis la base à partir de son titre
//	 * @param String id
//	 * @return Contrat
//	 */
//	public static Contrat getByTitre(String titre) /*throws SQLException*/ {
//		
//		System.out.println("Contrat.getByTitre("+titre+")");
//		
//		String requete = "SELECT * FROM contrat WHERE titre = '" + titre + "';";
//
//		Jdbc base = Jdbc.getInstance();
//		Vector resultats = base.executeQuery(requete);
//		
//		//S'il y a un resultat
//		if (resultats!=null && resultats.size()>0) {
//			
//			//On prend le 1er élément
//			Dictionary dico = (Dictionary) resultats.firstElement();
//			
//			//On mappe les champs
//			String id = String.valueOf((Integer)dico.get("id"));
//			String titreCon = (String)dico.get("titre");
//			String idContractant = (String)dico.get("id_contractant");
//			String modeReglement = (String)dico.get("reglement");
//			String type = (String)dico.get("type");
//			
//			//On assemble la date de signature
//			GregorianCalendar dateSignature = new GregorianCalendar();
//			dateSignature.setTimeInMillis(Long.valueOf((String)dico.get("signature")));
//			int jourSignature = dateSignature.get(GregorianCalendar.DATE);
//			int moisSignature = dateSignature.get(GregorianCalendar.MONTH);
//			int anneeSignature = dateSignature.get(GregorianCalendar.YEAR);
//			
//			//On assemble la date d'expiration
//			GregorianCalendar dateExpiration = new GregorianCalendar();
//			dateExpiration.setTimeInMillis(Long.valueOf((String)dico.get("expiration")));
//			int jourExpiration = dateExpiration.get(GregorianCalendar.DATE);
//			int moisExpiration = dateExpiration.get(GregorianCalendar.MONTH);
//			int anneeExpiration = dateExpiration.get(GregorianCalendar.YEAR);
//			
//			System.out.println("-------- Contrat -----------");
//			System.out.println("Nb de champs: "+dico.size());
//			System.out.println("ID: "+id);
//			System.out.println("Titre: "+titreCon);
//			System.out.println("Signature: "+jourSignature+"/"+moisSignature+"/"+anneeSignature);
//			System.out.println("Expiration: "+jourExpiration+"/"+moisExpiration+"/"+anneeExpiration);
//			System.out.println("Contractant: "+idContractant);
//			System.out.println("Mode de règlement: "+modeReglement);
//			System.out.println("Type: "+type);
//			System.out.println("-----------------------------");
//			
//			//On retourne l'objet
//			return new Contrat(id, titreCon, dateSignature, dateExpiration, idContractant, modeReglement, type);
//		}
//		
//		//Sinon, on retourne un objet vide
//		return null;
//	}
//	
//	/**
//	 * Retourne un vecteur d'objets contrat instanciés à partir de toutes les infos de la base
//	 * @return Hashtable
//	 * @throws SQLException 
//	 */
//
//	public static Hashtable getAll() /*throws SQLException*/ {
//		
//		System.out.println("Contrat.getAll()");
//		
//		//On crée un vecteur pour contenir les objets documents instanciés
//		Hashtable contrats = new Hashtable();
//		
//		//On va chercher dans la liste des id de tous les documents
//		String requete = "SELECT id FROM contrat;";
//		Jdbc base = Jdbc.getInstance();
//		Vector resultats = base.executeQuery(requete);
//		
//		System.out.println("Contrat.getAll() : "+resultats.size()+" contrat(s) trouvé(s)");
//		
//		//Pour chaque document, on instancie un objet que l'on stocke dans le vecteur
//		for (int j = 0; j < resultats.size(); j++) {
//			Dictionary dico = (Dictionary) resultats.elementAt(j);
//			String id = String.valueOf((Integer)dico.get("id"));
//			contrats.put(id, Contrat.getById(id));
//		}
//		
//		//On retourne le vecteur contenant les objets contractants instanciés
//		return contrats;
//	}
//	
//	/**
//	 * Détruit les infos d'un contrat contenues dans la base
//	 * @param id
//	 * @return
//	 * @throws SQLException 
//	 */
//	public static boolean deleteById(String id) /*throws SQLException*/ {
//		
//		//On supprime le contractant de la base, en partant d'un id
//		String requete = "DELETE FROM contrat WHERE id = '" + id + "';";
//		Jdbc base = Jdbc.getInstance();
//		int nbRows = base.executeUpdate(requete);
//		
//		//On retourne le resultat de l'opération (succès/échec)
//		return nbRows>0;
//	}
	
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
	public boolean ajouterDocument(String idDoc) {
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
		
		//On assemble la date de signature
		GregorianCalendar dateSignature = new GregorianCalendar(anneeSignature, moisSignature-1, jourSignature);
		
		//On assemble la date d'expiration
		GregorianCalendar dateExpiration = new GregorianCalendar(anneeExpiration, moisExpiration-1, jourExpiration);
		
		String requete = "UPDATE contrat SET signature = '" + dateSignature.getTimeInMillis() + "', expiration = '" + dateExpiration.getTimeInMillis() +
			"', id_contractant = '"+ idContractant + "', reglement = '" + modeReglement + "', type = '" + type + "', titre = '" + titre + "' WHERE id = '" + id + "';";
		
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
		return ContractantFactory.deleteById(id);
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
		dico.put("dateSignature", dateSignature.get(GregorianCalendar.DAY_OF_MONTH)+"/"+dateSignature.get(GregorianCalendar.MONTH)+"/"+dateSignature.get(GregorianCalendar.YEAR));
		dico.put("dateExpiration", dateExpiration.get(GregorianCalendar.DAY_OF_MONTH)+"/"+dateExpiration.get(GregorianCalendar.MONTH)+"/"+dateExpiration.get(GregorianCalendar.YEAR));
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
		String requete = "UPDATE contrat SET titre = '" + titre + "' WHERE id = '" + id + "';";
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