package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * Contractant
 */
public class Contractant {
	/**
	 * Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(Contractant.class);
// ATTRIBUTS
// ************************

	/**
	 * Identifiant
	 */
	private String id;

	/**
	 * Nom
	 */
	private String nom;

	/**
	 * Adresse
	 */
	private String adresse;

	/**
	 * Code Postal
	 */
	private String codePostal;

	/**
	 * Ville
	 */
	private String ville;

	/**
	 * Numéro de téléphone
	 */
	private String telephone;

	/**
	 * Numéro de fax
	 */
	private String fax;

	/**
	 * Adresse email
	 */
	private String mail;

	/**
	 * Contrats
	 */
	private Hashtable contrats = new Hashtable();
	
	/**
	 * Type (artiste | maison de disque)
	 */
	private String type;
	
// CONSTRUCTEUR
//******************************************
	
	/**
	 * Constructeur
	 * @param String id
	 * @param String nom
	 * @param String adresse
	 * @param String codePostal
	 * @param String ville
	 * @param String telephone
	 * @param String fax
	 * @param String mail
	 * @param String type
	 */
	public Contractant(String id, String nom, String adresse, String codePostal,
			String ville, String telephone, String fax, String mail, String type) {
		
		this.id = id;
		this.nom = nom;
		this.adresse = adresse;
		this.codePostal = codePostal;
		this.ville = ville;
		this.telephone = telephone;
		this.fax = fax;
		this.mail = mail;
		this.type = type;
		
		//this.contrats = getContratsAssocies();
	}

	
// METHODES DYNAMIQUES
//******************************************
	
	/**
	 * Retourne la liste des contrats associés à ce contrat
	 * @return Hastable
	 */
	public void setContratsAssocies() {
		logger.debug("Démarrage: setContratsAssocies");
			
		//On crée un vecteur pour contenir les objets contrats instanciés
		Hashtable contrats = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id FROM contrat WHERE id_contractant = '"+id+"';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		logger.info("Contractant.getContratAssocies() : "+resultats.size()+" contrat(s) trouvé(s)");
		
		// Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id"));
			contrats.put(id, ContratFactory.getById(id));
		}
		this.contrats = contrats;
		logger.debug("Arrêt: setContratsAssocies");
	}

	/**
	 * Ajoute le programme qui insère le contrat dans sa liste
	 * @param Programme prog
	 */
	public void ajouterContrat(Contrat contrat) {
		logger.debug("Démarrage: ajouterContrat");
		//Si le contrat n'est pas déjà associé
		if (!contrats.containsKey(contrat.getId())) {
			
			//On associe le contrat
			contrats.put(contrat.getId(), contrat);
		}
		
		logger.info("Contrat ajouté");
		logger.debug("Arrêt: ajouterContrat");
	}

	/**
	 * Supprime de sa liste le contrat
	 * @param String
	 */
	public void retirerContrat(String idCon) {
		logger.debug("Démarrage: retirerContrat");
		
		//On enlève le contrat
		contrats.remove(idCon);
		
		logger.info("Contrat retiré");
		logger.debug("Arrêt: retirerContrat");
	}

	/**
	 * Modifie les attributs
	 * @param String nom
	 * @param String adresse
	 * @param String codePostal
	 * @param String ville
	 * @param String telephone
	 * @param String fax
	 * @param String mail
	 * @param String type
	 * @return boolean
	 */
	public boolean modifier(String nom, String adresse, String codePostal, String ville,
			String telephone, String fax, String mail, String type) {
		logger.debug("Démarrage: modifier");

		String requete = "UPDATE contractant SET nom = '" + nom + "', adresse = '" + adresse +
			"', cp = '"+ codePostal + "', ville = '" + ville + "', telephone = '" + telephone +
			"', fax = '" + fax + "', mail = '" + mail + "', type = '"+ type + "' WHERE id = '" + id + "';";
		
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.nom = nom;
			this.adresse = adresse;
			this.codePostal = codePostal;
			this.ville = ville;
			this.telephone = telephone;
			this.fax = fax;
			this.mail = mail;
			this.type = type;
		}
		logger.debug("Arrêt: modifier");
		return nbRows>0;
	}
	
	/**
	 * Détruit le contractant et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		logger.debug("Démarrage: supprimer");
		
		//On supprime les associations document/programme
		Contrat c;
		for (int i=0; i<contrats.size(); i++){
			c = (Contrat)contrats.get(i);
			c.supprimer();
		}
		ContractantFactory.deleteById(this.id);
		
		//On supprime les infos de la base
		logger.debug("Arrêt: supprimer");
		return ContratFactory.deleteById(id);
	}

//#### GETTERS ####
//#################
	
	/**
	 * Retourne l'ensemble des attributs sous la forme d'un dictionnaire
	 * @return Dictionary
	 */
	public /*pure*/ Dictionary getAttributesDictionary() {
		
		Dictionary dico = new Hashtable();
		
		dico.put("id", id);
		dico.put("nom", nom);
		dico.put("adresse", adresse);
		dico.put("codePostal", codePostal);
		dico.put("ville", ville);
		dico.put("telephone", telephone);
		dico.put("fax", fax);
		dico.put("mail", mail);
		dico.put("type", type);
		
		//Liste des contrats
		Vector vContrats = new Vector();
		Enumeration listeContrats = contrats.elements();
		while (listeContrats.hasMoreElements()) {
			Contrat c = (Contrat)listeContrats.nextElement();
			Dictionary dicoContrat = new Hashtable();
			dicoContrat.put("id", c.getId());
			dicoContrat.put("titre", c.getTitre());
			vContrats.add(dicoContrat);
		}
		dico.put("contrats", vContrats);
		
		return dico;
	}
	
	/**
	 * @return Renvoie adresse.
	 */
	public /*pure*/ String getAdresse() {
		return adresse;
	}


	/**
	 * @return Renvoie codePostal.
	 */
	public /*pure*/ String getCodePostal() {
		return codePostal;
	}


	/**
	 * @return Renvoie contrats.
	 */
	public /*pure*/ Hashtable getContrats() {
		return contrats;
	}


	/**
	 * @return Renvoie fax.
	 */
	public /*pure*/ String getFax() {
		return fax;
	}


	/**
	 * @return Renvoie id.
	 */
	public /*pure*/ String getId() {
		return id;
	}


	/**
	 * @return Renvoie mail.
	 */
	public /*pure*/ String getMail() {
		return mail;
	}


	/**
	 * @return Renvoie nom.
	 */
	public /*pure*/ String getNom() {
		return nom;
	}


	/**
	 * @return Renvoie telephone.
	 */
	public /*pure*/ String getTelephone() {
		return telephone;
	}


	/**
	 * @return Renvoie type.
	 */
	public /*pure*/ String getType() {
		return type;
	}


	/**
	 * @return Renvoie ville.
	 */
	public /*pure*/ String getVille() {
		return ville;
	}

//#### SETTERS ####
//#################
	
	/**
	 * @param adresse adresse à définir.
	 */
	public boolean setAdresse(String adresse) {
		logger.debug("Démarrage: setAdresse");
		String requete = "UPDATE contractant SET adresse = '" + adresse + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.adresse = adresse;
		}
		logger.debug("Arrêt: setAdresse");
		return nbRows>0;
	}


	/**
	 * @param codePostal codePostal à définir.
	 */
	public boolean setCodePostal(String codePostal) {
		logger.debug("Démarrage: setCodePostal");
		String requete = "UPDATE contractant SET cp = '" + codePostal + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.codePostal = codePostal;
		}
		logger.debug("Arrêt: setCodePostal");
		return nbRows>0;
	}

	/**
	 * @param fax fax à définir.
	 */
	public boolean setFax(String fax) {
		logger.debug("Démarrage: setFax");
		String requete = "UPDATE contractant SET fax = '" + fax + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.fax = fax;
		}
		logger.debug("Arrêt: setFax");
		return nbRows>0;
	}

	/**
	 * @param mail mail à définir.
	 */
	public boolean setMail(String mail) {
		logger.debug("Démarrage: setMail");
		String requete = "UPDATE contractant SET mail = '" + mail + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.mail = mail;
		}
		logger.debug("Arrêt: setMail");
		return nbRows>0;
	}


	/**
	 * @param nom nom à définir.
	 */
	public boolean setNom(String nom) {
		logger.debug("Démarrage: setNom");
		String requete = "UPDATE contractant SET nom = '" + nom + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.nom = nom;
		}
		logger.debug("Arrêt: setNom");
		return nbRows>0;
	}


	/**
	 * @param telephone telephone à définir.
	 */
	public boolean setTelephone(String telephone) {
		logger.debug("Démarrage: setTelephone");
		String requete = "UPDATE contractant SET type = '" + type + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.type = type;
		}
		logger.debug("Arrêt: setTelephone");
		return nbRows>0;
	}


	/**
	 * @param type type à définir.
	 */
	public boolean setType(String type) {
		logger.debug("Démarrage: setType");
		String requete = "UPDATE contractant SET type = '" + type + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.type = type;
		}
		logger.debug("Arrêt: setType");
		return nbRows>0;
	}


	/**
	 * @param ville ville à définir.
	 */
	public boolean setVille(String ville) {
		logger.debug("Démarrage: setVille");
		String requete = "UPDATE contractant SET ville = '" + ville + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.ville = ville;
		}
		logger.debug("Arrêt: setVille");
		return nbRows>0;
	}
}
