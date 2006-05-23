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
 * Contrat {dur�e_contrat=date_expiration-date_signature}
 */
public class Contrat {
	/**
	 * Cr�e le logger de la classe
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
	 * Mode de r�glement
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
//	 * Cr�ation du contrat en base
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
//		//On cr�e le contractant dans la base
//		String requete = "INSERT INTO contrat (titre, signature, expiration, id_contractant, reglement, type) VALUES ('" +
//			titre + "', '" + dateSignature.getTimeInMillis() + "', '" + dateExpiration.getTimeInMillis() +
//			"', '" + idContractant + "', '" + modeReglement + "', '" + type + "');"; 
//		
//		Jdbc base = Jdbc.getInstance();
//		int nbRows = base.executeUpdate(requete);
//		
//		//Si l'insertion s'est bien d�roul�e
//		if (nbRows>0) {
//			
//			//On instancie l'objet correspondant
//			Contrat c = Contrat.getByTitre(titre);
//			
//			//On informe le contractant de la cr�ation du contrat
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
//	 * Instancie un objet contrat apr�s avoir r�cup�r� ces infos depuis la base � partir de son id
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
//			//On prend le 1er �l�ment
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
//			System.out.println("Mode de r�glement: "+modeReglement);
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
//	 * Instancie un objet contrat apr�s avoir r�cup�r� ces infos depuis la base � partir de son titre
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
//			//On prend le 1er �l�ment
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
//			System.out.println("Mode de r�glement: "+modeReglement);
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
//	 * Retourne un vecteur d'objets contrat instanci�s � partir de toutes les infos de la base
//	 * @return Hashtable
//	 * @throws SQLException 
//	 */
//
//	public static Hashtable getAll() /*throws SQLException*/ {
//		
//		System.out.println("Contrat.getAll()");
//		
//		//On cr�e un vecteur pour contenir les objets documents instanci�s
//		Hashtable contrats = new Hashtable();
//		
//		//On va chercher dans la liste des id de tous les documents
//		String requete = "SELECT id FROM contrat;";
//		Jdbc base = Jdbc.getInstance();
//		Vector resultats = base.executeQuery(requete);
//		
//		System.out.println("Contrat.getAll() : "+resultats.size()+" contrat(s) trouv�(s)");
//		
//		//Pour chaque document, on instancie un objet que l'on stocke dans le vecteur
//		for (int j = 0; j < resultats.size(); j++) {
//			Dictionary dico = (Dictionary) resultats.elementAt(j);
//			String id = String.valueOf((Integer)dico.get("id"));
//			contrats.put(id, Contrat.getById(id));
//		}
//		
//		//On retourne le vecteur contenant les objets contractants instanci�s
//		return contrats;
//	}
//	
//	/**
//	 * D�truit les infos d'un contrat contenues dans la base
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
//		//On retourne le resultat de l'op�ration (succ�s/�chec)
//		return nbRows>0;
//	}
	
// METHODES DYNAMIQUES
//***************************************
	
	/**
	 * Retourne la liste des documents associ�s � ce contrat
	 * @return Hastable
	 */
	public void setDocumentsAssocies() {
		
		logger.debug("D�marrage: setDocumentsAssocies");
		
		//On cr�e un vecteur pour contenir les objets documents instanci�s
		Hashtable docs = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id_doc FROM contratdoc WHERE id_contrat = '"+id+"';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		logger.info("Contrat.getDocumentsAssocies() : "+resultats.size()+" document(s) trouv�(s)");
		
		// Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = (String)dico.get("id_doc");
			docs.put(id, DocumentFactory.getById(id));
		}
		
		this.documents=docs;
		logger.debug("Arr�t: setDocumentsAssocies");
	}
	
	/**
	 * Ajoute un document
	 * @param Programme prog
	 */
	public void ajouterDocument(Document doc) {
		logger.debug("D�marrage: ajouterDocument");
		//On ajoute le document au contrat
		String requete = "INSERT INTO contratdoc (id_contrat, id_doc) VALUES ('" + id + "', '" + doc.getId() + "');";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien d�roul�e
		if (nbRows>0) {
			
			//On met � jour le vecteurs d'association
			documents.put(doc.getId(), doc);
			
			//On signale au document cet ajout
			doc.ajouterContrat(this);
		}
		
		logger.info("Document ajout�");
		logger.debug("Arr�t: ajouterDocument");
	}

	/**
	 * Supprime de sa liste le document
	 * @param boolean
	 */
	public boolean ajouterDocument(String idDoc) {
		logger.debug("D�marrage: ajouterDocument");
		// On retire le document du CONTRAT
		String requete = "DELETE FROM contratdoc WHERE id_contrat='"+this.id+"' AND id_doc='"+idDoc+"';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la suppression s'est bien d�roul�e
		if (nbRows>0) {
			
			//On r�cup�re le doc
			Document doc = (Document)documents.get(idDoc);
			
			//On signale au document ce retrait
			doc.retirerContrat(this.id);
			
			//On met � jour le vecteur d'association
			documents.remove(idDoc);
			logger.debug("Arr�t: ajouterDocument");
			return true;
		}
		
		//Sinon
		logger.debug("Arr�t: ajouterDocument");
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
		logger.debug("D�marrage: modifier");
		
		//On assemble la date de signature
		GregorianCalendar dateSignature = new GregorianCalendar(anneeSignature, moisSignature-1, jourSignature);
		
		//On assemble la date d'expiration
		GregorianCalendar dateExpiration = new GregorianCalendar(anneeExpiration, moisExpiration-1, jourExpiration);
		
		String requete = "UPDATE contrat SET signature = '" + dateSignature.getTimeInMillis() + "', expiration = '" + dateExpiration.getTimeInMillis() +
			"', id_contractant = '"+ idContractant + "', reglement = '" + modeReglement + "', type = '" + type + "', titre = '" + titre + "' WHERE id = '" + id + "';";
		
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.titre = titre;
			this.dateSignature = dateSignature;
			this.dateExpiration = dateExpiration;
			this.contractant = (Contractant)ContractantFactory.getById(idContractant);
			this.modeReglement = modeReglement;
			this.type = type;
		}
		logger.debug("Arr�t: modifier");
		return nbRows>0;
	}
	
	/**
	 * D�truit le contrat et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		logger.debug("D�marrage: supprimer");
		//On supprime les associations contrat/document
		for (int i=0; i<documents.size(); i++){
			Document d = (Document)documents.get(i);
			d.retirerContrat(this.id);
		}
		
		//On supprime l'association contrat/contractant
		contractant.retirerContrat(this.id);
		
		//On supprime les infos de la base
		logger.debug("Arr�t: supprimer");
		return ContractantFactory.deleteById(id);
	}
	
//#### GETTERS ####
//#################	
	
	/**
	 * Calcule la dur�e du contrat
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
	 * @param dateExpiration dateExpiration � d�finir.
	 */
	public boolean setDateExpiration(int jour, int mois, int annee) {
		logger.debug("D�marrage: setDateExpiration");
		GregorianCalendar date = new GregorianCalendar(annee, mois-1, jour);
		
		String requete = "UPDATE contrat SET expiration = '" + date.getTimeInMillis() + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.dateExpiration = date;
		}
		logger.debug("Arr�t: setDateExpiration");
		return nbRows>0;
	}

	/**
	 * @param dateSignature dateSignature � d�finir.
	 */
	public boolean setDateSignature(int jour, int mois, int annee) {
		logger.debug("D�marrage: setDateSignature");
		GregorianCalendar date = new GregorianCalendar(annee, mois-1, jour);
		
		String requete = "UPDATE contrat SET signature = '" + date.getTimeInMillis() + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.dateSignature = date;
		}
		logger.debug("Arr�t: setDateSignature");
		return nbRows>0;
	}

	/**
	 * @param modeReglement modeReglement � d�finir.
	 */
	public boolean setModeReglement(String modeReglement) {
		logger.debug("D�marrage: setModeReglement");
		String requete = "UPDATE contrat SET reglement = '" + modeReglement + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.modeReglement = modeReglement;
		}
		logger.debug("Arr�t: setModeReglement");
		return nbRows>0;
	}

	/**
	 * @param signataire signataire � d�finir.
	 */
	public boolean setContractant(String idContractant) {
		logger.debug("D�marrage: setContractant");
		String requete = "UPDATE contrat SET id_contractant = '" + idContractant + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.contractant = (Contractant)ContractantFactory.getById(idContractant);
		}
		logger.debug("Arr�t: setContractant");
		return nbRows>0;
	}

	/**
	 * @param type type � d�finir.
	 */
	public boolean setType(String type) {
		logger.debug("D�marrage: setType");
		String requete = "UPDATE contrat SET type = '" + type + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.type = type;
		}
		logger.debug("Arr�t: setType");
		return nbRows>0;
	}
	
	/**
	 * @param type titre � d�finir.
	 */
	public boolean setTitre(String titre) {
		logger.debug("D�marrage: setTitre");
		String requete = "UPDATE contrat SET titre = '" + titre + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attribut de l'objet
		if (nbRows>0) {
			this.titre = titre;
		}
		logger.debug("Arr�t: setTitre");
		return nbRows>0;
	}

}