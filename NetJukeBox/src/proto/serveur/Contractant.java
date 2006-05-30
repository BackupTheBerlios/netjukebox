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
	 * Cr�e le logger de la classe
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
	 * Num�ro de t�l�phone
	 */
	private String telephone;

	/**
	 * Num�ro de fax
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
	 * Retourne la liste des contrats associ�s � ce contrat
	 * @return Hastable
	 */
	public void setContratsAssocies() {
		logger.debug("D�marrage: setContratsAssocies");
			
		//On cr�e un vecteur pour contenir les objets contrats instanci�s
		Hashtable contrats = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id FROM contrat WHERE id_contractant = '"+id+"';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		logger.info("Contractant.getContratAssocies() : "+resultats.size()+" contrat(s) trouv�(s)");
		
		// Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id"));
			contrats.put(id, ContratFactory.getById(id));
		}
		this.contrats = contrats;
		logger.debug("Arr�t: setContratsAssocies");
	}

	/**
	 * Ajoute le programme qui ins�re le contrat dans sa liste
	 * @param Programme prog
	 */
	public void ajouterContrat(Contrat contrat) {
		logger.debug("D�marrage: ajouterContrat");
		//Si le contrat n'est pas d�j� associ�
		if (!contrats.containsKey(contrat.getId())) {
			
			//On associe le contrat
			contrats.put(contrat.getId(), contrat);
		}
		
		logger.info("Contrat ajout�");
		logger.debug("Arr�t: ajouterContrat");
	}

	/**
	 * Supprime de sa liste le contrat
	 * @param String
	 */
	public void retirerContrat(String idCon) {
		logger.debug("D�marrage: retirerContrat");
		
		//On enl�ve le contrat
		contrats.remove(idCon);
		
		logger.info("Contrat retir�");
		logger.debug("Arr�t: retirerContrat");
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
		logger.debug("D�marrage: modifier");

		String requete = "UPDATE contractant SET nom = '" + nom + "', adresse = '" + adresse +
			"', cp = '"+ codePostal + "', ville = '" + ville + "', telephone = '" + telephone +
			"', fax = '" + fax + "', mail = '" + mail + "', type = '"+ type + "' WHERE id = '" + id + "';";
		
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
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
		logger.debug("Arr�t: modifier");
		return nbRows>0;
	}
	
	/**
	 * D�truit le contractant et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		logger.debug("D�marrage: supprimer");
		
		//On supprime les associations document/programme
		Contrat c;
		for (int i=0; i<contrats.size(); i++){
			c = (Contrat)contrats.get(i);
			c.supprimer();
		}
		ContractantFactory.deleteById(this.id);
		
		//On supprime les infos de la base
		logger.debug("Arr�t: supprimer");
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
	 * @param adresse adresse � d�finir.
	 */
	public boolean setAdresse(String adresse) {
		logger.debug("D�marrage: setAdresse");
		String requete = "UPDATE contractant SET adresse = '" + adresse + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.adresse = adresse;
		}
		logger.debug("Arr�t: setAdresse");
		return nbRows>0;
	}


	/**
	 * @param codePostal codePostal � d�finir.
	 */
	public boolean setCodePostal(String codePostal) {
		logger.debug("D�marrage: setCodePostal");
		String requete = "UPDATE contractant SET cp = '" + codePostal + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.codePostal = codePostal;
		}
		logger.debug("Arr�t: setCodePostal");
		return nbRows>0;
	}

	/**
	 * @param fax fax � d�finir.
	 */
	public boolean setFax(String fax) {
		logger.debug("D�marrage: setFax");
		String requete = "UPDATE contractant SET fax = '" + fax + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.fax = fax;
		}
		logger.debug("Arr�t: setFax");
		return nbRows>0;
	}

	/**
	 * @param mail mail � d�finir.
	 */
	public boolean setMail(String mail) {
		logger.debug("D�marrage: setMail");
		String requete = "UPDATE contractant SET mail = '" + mail + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.mail = mail;
		}
		logger.debug("Arr�t: setMail");
		return nbRows>0;
	}


	/**
	 * @param nom nom � d�finir.
	 */
	public boolean setNom(String nom) {
		logger.debug("D�marrage: setNom");
		String requete = "UPDATE contractant SET nom = '" + nom + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.nom = nom;
		}
		logger.debug("Arr�t: setNom");
		return nbRows>0;
	}


	/**
	 * @param telephone telephone � d�finir.
	 */
	public boolean setTelephone(String telephone) {
		logger.debug("D�marrage: setTelephone");
		String requete = "UPDATE contractant SET type = '" + type + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.type = type;
		}
		logger.debug("Arr�t: setTelephone");
		return nbRows>0;
	}


	/**
	 * @param type type � d�finir.
	 */
	public boolean setType(String type) {
		logger.debug("D�marrage: setType");
		String requete = "UPDATE contractant SET type = '" + type + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.type = type;
		}
		logger.debug("Arr�t: setType");
		return nbRows>0;
	}


	/**
	 * @param ville ville � d�finir.
	 */
	public boolean setVille(String ville) {
		logger.debug("D�marrage: setVille");
		String requete = "UPDATE contractant SET ville = '" + ville + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.ville = ville;
		}
		logger.debug("Arr�t: setVille");
		return nbRows>0;
	}
}
