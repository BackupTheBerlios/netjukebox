package proto.serveur;

import java.sql.SQLException;
import java.util.Date;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Contrat {durée_contrat=date_expiration-date_signature}
 */
public class Contrat {

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
	 * Signataire
	 */
	private String signataire;

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
	private Hashtable documents;

	/**
	 * Contractants
	 */
	private Hashtable contractants;

// CONSTRUCTEUR
//***************************************
	
	/**
	 * Constructeur
	 * @param String id
	 * @param String titre
	 * @param Date dateSignatare
	 * @param Date dateExpiration
	 * @param String signataire
	 * @param String modeReglement
	 * @param String type
	 */
	public Contrat(String id, String titre, GregorianCalendar dateSignature, GregorianCalendar dateExpiration, String signataire, String modeReglement, String type) {
		this.id = id;
		this.titre = titre;
		this.dateSignature = dateSignature;
		this.dateExpiration = dateExpiration;
		this.signataire = signataire;
		this.modeReglement = modeReglement;
		this.type = type;
	}
	
// METHODES STATIQUES
//***************************************
	
	/**
	 * Création du contrat en base
	 * @param String titre
	 * @param int jourSignature
	 * @param int moisSignature
	 * @param int anneeSignature
	 * @param int jourExpiration
	 * @param int moisExpiration
	 * @param int anneExpiration
	 * @param String signataire
	 * @param String modeReglement
	 * @param String type
	 * @return Contrat
	 */
	public static Contrat create(String titre, int jourSignature, int moisSignature, int anneeSignature,
			int jourExpiration, int moisExpiration, int anneeExpiration, String signataire,
			String modeReglement, String type) /*throws SQLException*/ {
		
		System.out.println("Contrat.create()");
		
		//On assemble la date de signature
		GregorianCalendar dateSignature = new GregorianCalendar(anneeSignature, moisSignature-1, jourSignature);
		
		//On assemble la date d'expiration
		GregorianCalendar dateExpiration = new GregorianCalendar(anneeExpiration, moisExpiration-1, jourExpiration);
		
		//On crée le contractant dans la base
		String requete = "INSERT INTO contrat (titre, dateSignature, dateExpiration, signataire, modeReglement, type) VALUES ('" +
			titre + "', '" + dateSignature.getTimeInMillis() + "', '" + dateExpiration.getTimeInMillis() +
			"', '" + signataire + "', '" + modeReglement + "', '" + type + "');"; 
		
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien déroulée
		if (nbRows>0) {
			//On retourne ensuite un objet pour ce contractant
			//return Contrat.getByNom(nom);
		}
		
		//Sinon on retourne un objet vide
		return null;
	}
	
	/**
	 * Instancie un objet contrat après avoir récupéré ces infos depuis la base à partir de son id
	 * @param String id
	 * @return Contrat
	 */
	public static Contrat getById(String id) /*throws SQLException*/ {
		
		System.out.println("Contrat.getById("+id+")");
		
		String requete = "SELECT * FROM contrat WHERE id = '" + id + "';";

		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		//S'il y a un resultat
		if (resultats!=null && resultats.size()>0) {
			
			//On prend le 1er élément
			Dictionary dico = (Dictionary) resultats.firstElement();
			
			//On mappe les champs
			String idCon = String.valueOf((Integer)dico.get("id"));
			String titre = String.valueOf((Integer)dico.get("titre"));
			String signataire = (String)dico.get("signataire");
			String modeReglement = (String)dico.get("modeReglement");
			String type = (String)dico.get("type");
			
			//On assemble la date de signature
			GregorianCalendar dateSignature = new GregorianCalendar();
			dateSignature.setTimeInMillis(Long.valueOf((String)dico.get("dateSignature")));
			int jourSignature = dateSignature.get(GregorianCalendar.DATE);
			int moisSignature = dateSignature.get(GregorianCalendar.MONTH);
			int anneeSignature = dateSignature.get(GregorianCalendar.YEAR);
			
			//On assemble la date d'expiration
			GregorianCalendar dateExpiration = new GregorianCalendar();
			dateExpiration.setTimeInMillis(Long.valueOf((String)dico.get("dateExpiration")));
			int jourExpiration = dateExpiration.get(GregorianCalendar.DATE);
			int moisExpiration = dateExpiration.get(GregorianCalendar.MONTH);
			int anneeExpiration = dateExpiration.get(GregorianCalendar.YEAR);
			
			System.out.println("-------- Contractant -----------");
			System.out.println("Nb de champs: "+dico.size());
			System.out.println("ID: "+idCon);
			System.out.println("Titre: "+titre);
			System.out.println("Signature: "+jourSignature+"/"+moisSignature+"/"+anneeSignature);
			System.out.println("Expiration: "+jourExpiration+"/"+moisExpiration+"/"+anneeExpiration);
			System.out.println("Signataire: "+signataire);
			System.out.println("Mode de règlement: "+modeReglement);
			System.out.println("Type: "+type);
			System.out.println("-----------------------------");
			
			//On retourne l'objet
			return new Contrat(idCon, titre, dateSignature, dateExpiration, signataire, modeReglement, type);
		}
		
		//Sinon, on retourne un objet vide
		return null;
	}
	
	/**
	 * Instancie un objet contrat après avoir récupéré ces infos depuis la base à partir de son titre
	 * @param String id
	 * @return Contrat
	 */
	public static Contrat getByTitre(String titre) /*throws SQLException*/ {
		
		System.out.println("Contrat.getByTitre("+titre+")");
		
		String requete = "SELECT * FROM contrat WHERE titre = '" + titre + "';";

		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		//S'il y a un resultat
		if (resultats!=null && resultats.size()>0) {
			
			//On prend le 1er élément
			Dictionary dico = (Dictionary) resultats.firstElement();
			
			//On mappe les champs
			String id = String.valueOf((Integer)dico.get("id"));
			String titreCon = String.valueOf((Integer)dico.get("titre"));
			String signataire = (String)dico.get("signataire");
			String modeReglement = (String)dico.get("modeReglement");
			String type = (String)dico.get("type");
			
			//On assemble la date de signature
			GregorianCalendar dateSignature = new GregorianCalendar();
			dateSignature.setTimeInMillis(Long.valueOf((String)dico.get("dateSignature")));
			int jourSignature = dateSignature.get(GregorianCalendar.DATE);
			int moisSignature = dateSignature.get(GregorianCalendar.MONTH);
			int anneeSignature = dateSignature.get(GregorianCalendar.YEAR);
			
			//On assemble la date d'expiration
			GregorianCalendar dateExpiration = new GregorianCalendar();
			dateExpiration.setTimeInMillis(Long.valueOf((String)dico.get("dateExpiration")));
			int jourExpiration = dateExpiration.get(GregorianCalendar.DATE);
			int moisExpiration = dateExpiration.get(GregorianCalendar.MONTH);
			int anneeExpiration = dateExpiration.get(GregorianCalendar.YEAR);
			
			System.out.println("-------- Contractant -----------");
			System.out.println("Nb de champs: "+dico.size());
			System.out.println("ID: "+id);
			System.out.println("Titre: "+titreCon);
			System.out.println("Signature: "+jourSignature+"/"+moisSignature+"/"+anneeSignature);
			System.out.println("Expiration: "+jourExpiration+"/"+moisExpiration+"/"+anneeExpiration);
			System.out.println("Signataire: "+signataire);
			System.out.println("Mode de règlement: "+modeReglement);
			System.out.println("Type: "+type);
			System.out.println("-----------------------------");
			
			//On retourne l'objet
			return new Contrat(id, titreCon, dateSignature, dateExpiration, signataire, modeReglement, type);
		}
		
		//Sinon, on retourne un objet vide
		return null;
	}
	
	/**
	 * Retourne un vecteur d'objets contrat instanciés à partir de toutes les infos de la base
	 * @return Hashtable
	 * @throws SQLException 
	 */

	public static Hashtable getAll() /*throws SQLException*/ {
		
		System.out.println("Contrat.getAll()");
		
		//On crée un vecteur pour contenir les objets documents instanciés
		Hashtable contrats = new Hashtable();
		
		//On va chercher dans la liste des id de tous les documents
		String requete = "SELECT id FROM contrat;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		System.out.println("Contrat.getAll() : "+resultats.size()+" contrat(s) trouvé(s)");
		
		//Pour chaque document, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id"));
			contrats.put(id, Contrat.getById(id));
		}
		
		//On retourne le vecteur contenant les objets contractants instanciés
		return contrats;
	}
	
	/**
	 * Détruit les infos d'un contrat contenues dans la base
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		
		//On supprime le contractant de la base, en partant d'un id
		String requete = "DELETE FROM contrat WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//On retourne le resultat de l'opération (succès/échec)
		return nbRows>0;
	}
	
// METHODES DYNAMIQUES
//***************************************
	
	/**
	 * Ajoute un contractant
	 * @param Programme prog
	 */
	public void ajouterContractant(Contractant contractant) {
		
		//Si le contrat n'est pas déjà associé
		if (!contractants.containsKey(contractant.getId())) {
			
			//On associe le contrat
			contractants.put(contractant.getId(), contractant);
		}
		
		System.out.println("Contractant ajouté");
	}

	/**
	 * Supprime de sa liste le contractant
	 * @param String
	 */
	public void retirerContractant(String idCon) {
		
		//On enlève le contrat
		contractants.remove(idCon);
		
		System.out.println("Contractant retiré");
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
	 * @param String signataire
	 * @param String modeReglement
	 * @param String type
	 * @return boolean
	 */
	public boolean modifier(String titre, int jourSignature, int moisSignature, int anneeSignature,
			int jourExpiration, int moisExpiration, int anneeExpiration, String signataire,
			String modeReglement, String type) {

		
		//On assemble la date de signature
		GregorianCalendar dateSignature = new GregorianCalendar(anneeSignature, moisSignature-1, jourSignature);
		
		//On assemble la date d'expiration
		GregorianCalendar dateExpiration = new GregorianCalendar(anneeExpiration, moisExpiration-1, jourExpiration);
		
		String requete = "UPDATE contrat SET dateSignature = '" + dateSignature.getTimeInMillis() + "', dateExpiration = '" + dateExpiration.getTimeInMillis() +
			"', signataire = '"+ signataire + "', modeReglement = '" + modeReglement + "', type = '" + type + "', titre = '" + titre + "' WHERE id = '" + id + "';";
		
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.titre = titre;
			this.dateSignature = dateSignature;
			this.dateExpiration = dateExpiration;
			this.signataire = signataire;
			this.modeReglement = modeReglement;
			this.type = type;
		}
		return nbRows>0;
	}
	
	/**
	 * Détruit le contrat et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		
		//On supprime les associations contrat/contractant
		Contrat c;
		for (int i=0; i<contractants.size(); i++){
			c = (Contrat)contractants.get(i);
			c.supprimer();
		}
		
		//On supprime les infos de la base
		return Contrat.deleteById(id);
	}
	
//#### GETTERS ####
//#################	
	
	/**
	 * Calcule la durée du contrat
	 */
	public long getDureeContrat() {
		// your code here
		return dateExpiration.getTimeInMillis()-dateSignature.getTimeInMillis();
	}
	
	/**
	 * Retourne l'ensemble des attributs sous la forme d'un dictionnaire
	 * @return Dictionary
	 */
	public Dictionary getAttributesDictionary() {
		
		Dictionary dico = new Hashtable();
		
		dico.put("id", id);
		dico.put("titre", titre);
		dico.put("dateSignature", dateSignature.get(GregorianCalendar.DAY_OF_MONTH)+"/"+dateSignature.get(GregorianCalendar.MONTH)+"/"+dateSignature.get(GregorianCalendar.YEAR));
		dico.put("dateExpiration", dateExpiration.get(GregorianCalendar.DAY_OF_MONTH)+"/"+dateExpiration.get(GregorianCalendar.MONTH)+"/"+dateExpiration.get(GregorianCalendar.YEAR));
		dico.put("signataire", signataire);
		dico.put("modeReglement", modeReglement);
		dico.put("type", type);
		
		return dico;
	}

	/**
	 * @return Renvoie contractants.
	 */
	public Hashtable getContractants() {
		return contractants;
	}
	
	/**
	 * @return Renvoie titre.
	 */
	public String getTitre() {
		return titre;
	}

	/**
	 * @return Renvoie dateExpiration.
	 */
	public GregorianCalendar getDateExpiration() {
		return dateExpiration;
	}

	/**
	 * @return Renvoie dateSignature.
	 */
	public GregorianCalendar getDateSignature() {
		return dateSignature;
	}

	/**
	 * @return Renvoie documents.
	 */
	public Hashtable getDocuments() {
		return documents;
	}

	/**
	 * @return Renvoie id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return Renvoie modeReglement.
	 */
	public String getModeReglement() {
		return modeReglement;
	}

	/**
	 * @return Renvoie signataire.
	 */
	public String getSignataire() {
		return signataire;
	}

	/**
	 * @return Renvoie type.
	 */
	public String getType() {
		return type;
	}

//#### SETTERS ####
//#################		
	
	/**
	 * @param dateExpiration dateExpiration à définir.
	 */
	public boolean setDateExpiration(int jour, int mois, int annee) {
		GregorianCalendar date = new GregorianCalendar(annee, mois-1, jour);
		
		String requete = "UPDATE contrat SET dateExpiration = '" + date.getTimeInMillis() + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.dateExpiration = date;
		}
		return nbRows>0;
	}

	/**
	 * @param dateSignature dateSignature à définir.
	 */
	public boolean setDateSignature(int jour, int mois, int annee) {
		GregorianCalendar date = new GregorianCalendar(annee, mois-1, jour);
		
		String requete = "UPDATE contrat SET dateSignature = '" + date.getTimeInMillis() + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.dateSignature = date;
		}
		return nbRows>0;
	}

	/**
	 * @param modeReglement modeReglement à définir.
	 */
	public boolean setModeReglement(String modeReglement) {
		String requete = "UPDATE contrat SET modeReglement = '" + modeReglement + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.modeReglement = modeReglement;
		}
		return nbRows>0;
	}

	/**
	 * @param signataire signataire à définir.
	 */
	public boolean setSignataire(String signataire) {
		String requete = "UPDATE contrat SET signataire = '" + signataire + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.signataire = signataire;
		}
		return nbRows>0;
	}

	/**
	 * @param type type à définir.
	 */
	public boolean setType(String type) {
		String requete = "UPDATE contrat SET type = '" + type + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.type = type;
		}
		return nbRows>0;
	}
	
	/**
	 * @param type titre à définir.
	 */
	public boolean setTitre(String titre) {
		String requete = "UPDATE contrat SET titre = '" + titre + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.titre = titre;
		}
		return nbRows>0;
	}

}