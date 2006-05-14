package proto.serveur;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Contractant
 */
public class Contractant {

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
	
	
// METHODES STATIQUES
//******************************************
	
//	/**
//	 * Création du contractant en base
//	 * @param String nom
//	 * @param String adresse
//	 * @param String codePostal
//	 * @param String ville
//	 * @param String telephone
//	 * @param String fax
//	 * @param String mail
//	 * @param String type
//	 * @return Contractant
//	 */
//	public static Contractant create(String nom, String adresse, String codePostal,
//			String ville, String telephone, String fax, String mail, String type) /*throws SQLException*/ {
//		
//		System.out.println("Contractant.create()");
//		
//		//On crée le contractant dans la base
//		String requete = "INSERT INTO contractant (nom, adresse, cp, ville, telephone, fax, mail, type) VALUES ('" +
//			nom + "', '" + adresse + "', '" + codePostal + "', '" + ville + "', '" +telephone + "', '" +
//			fax + "', '" + mail + "', '"+ type + "');"; 
//		
//		Jdbc base = Jdbc.getInstance();
//		int nbRows = base.executeUpdate(requete);
//		
//		//Si l'insertion s'est bien déroulée
//		if (nbRows>0) {
//			//On retourne ensuite un objet pour ce contractant
//			return Contractant.getByNom(nom);
//		}
//		
//		//Sinon on retourne un objet vide
//		return null;
//	}
//	
//	/**
//	 * Instancie un objet contractant après avoir récupéré ces infos depuis la base à partir de son id
//	 * @param String id
//	 * @return Contractant
//	 */
//	public static Contractant getById(String id) /*throws SQLException*/ {
//		
//		System.out.println("Contractant.getById("+id+")");
//		
//		String requete = "SELECT * FROM contractant WHERE id = '" + id + "';";
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
//			String nom = (String)dico.get("nom");
//			String adresse = (String)dico.get("adresse");
//			String codePostal = (String)dico.get("cp");
//			String ville = (String)dico.get("ville");
//			String telephone = (String)dico.get("telephone");
//			String fax = (String)dico.get("fax");
//			String mail = (String)dico.get("mail");
//			String type = (String)dico.get("type");
//			
//			System.out.println("-------- Contractant -----------");
//			System.out.println("Nb de champs: "+dico.size());
//			System.out.println("ID: "+idCon);
//			System.out.println("Nom: "+nom);
//			System.out.println("Adresse: "+adresse);
//			System.out.println("CP: "+codePostal);
//			System.out.println("Ville: "+ville);
//			System.out.println("Téléphone: "+telephone);
//			System.out.println("Fax: "+fax);
//			System.out.println("eMail: "+mail);
//			System.out.println("Type: "+type);
//			System.out.println("-----------------------------");
//			
//			//On retourne l'objet
//			return new Contractant(idCon, nom, adresse, codePostal, ville, telephone, fax, mail, type);
//		}
//		
//		//Sinon, on retourne un objet vide
//		return null;
//	}
//	
//	/**
//	 * Instancie un objet contractant après avoir récupéré ces infos depuis la base à partir de son nom
//	 * @param String nom
//	 * @return Contractant
//	 */
//	public static Contractant getByNom(String nom) /*throws SQLException*/ {
//		
//		System.out.println("Contractant.getByNom("+nom+")");
//		
//		String requete = "SELECT * FROM contractant WHERE nom = '" + nom + "';";
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
//			String nomCon = (String)dico.get("nom");
//			String adresse = (String)dico.get("adresse");
//			String codePostal = (String)dico.get("cp");
//			String ville = (String)dico.get("ville");
//			String telephone = (String)dico.get("telephone");
//			String fax = (String)dico.get("fax");
//			String mail = (String)dico.get("mail");
//			String type = (String)dico.get("type");
//			
//			System.out.println("-------- Contractant -----------");
//			System.out.println("Nb de champs: "+dico.size());
//			System.out.println("ID: "+id);
//			System.out.println("Nom: "+nomCon);
//			System.out.println("Adresse: "+adresse);
//			System.out.println("CP: "+codePostal);
//			System.out.println("Ville: "+ville);
//			System.out.println("Téléphone: "+telephone);
//			System.out.println("Fax: "+fax);
//			System.out.println("eMail: "+mail);
//			System.out.println("Type: "+type);
//			System.out.println("-----------------------------");
//			
//			//On retourne l'objet
//			return new Contractant(id, nomCon, adresse, codePostal, ville, telephone, fax, mail, type);
//		}
//		
//		//Sinon, on retourne un objet vide
//		return null;
//	}
//	
//	/**
//	 * Retourne un vecteur d'objets contractants instanciés à partir de toutes les infos de la base
//	 * @return Hashtable
//	 * @throws SQLException 
//	 */
//
//	public static Hashtable getAll() /*throws SQLException*/ {
//		
//		System.out.println("Contractant.getAll()");
//		
//		//On crée un vecteur pour contenir les objets documents instanciés
//		Hashtable contractants = new Hashtable();
//		
//		//On va chercher dans la liste des id de tous les documents
//		String requete = "SELECT id FROM contractant;";
//		Jdbc base = Jdbc.getInstance();
//		Vector resultats = base.executeQuery(requete);
//		
//		System.out.println("Contractant.getAll() : "+resultats.size()+" contractant(s) trouvé(s)");
//		
//		//Pour chaque document, on instancie un objet que l'on stocke dans le vecteur
//		for (int j = 0; j < resultats.size(); j++) {
//			Dictionary dico = (Dictionary) resultats.elementAt(j);
//			String id = String.valueOf((Integer)dico.get("id"));
//			contractants.put(id, Contractant.getById(id));
//		}
//		
//		//On retourne le vecteur contenant les objets contractants instanciés
//		return contractants;
//	}
//	
//	/**
//	 * Détruit les infos d'un contractant contenues dans la base
//	 * @param id
//	 * @return
//	 * @throws SQLException 
//	 */
//	public static boolean deleteById(String id) /*throws SQLException*/ {
//		
//		//On supprime le contractant de la base, en partant d'un id
//		String requete = "DELETE FROM contractant WHERE id = '" + id + "';";
//		Jdbc base = Jdbc.getInstance();
//		int nbRows = base.executeUpdate(requete);
//		
//		//On retourne le resultat de l'opération (succès/échec)
//		return nbRows>0;
//	}

	
// METHODES DYNAMIQUES
//******************************************
	
	/**
	 * Retourne la liste des contrats associés à ce contrat
	 * @return Hastable
	 */
	public void setContratsAssocies() {
		System.out.println("Contractant.setContratsAssocies()");
		
		//On crée un vecteur pour contenir les objets contrats instanciés
		Hashtable contrats = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id FROM contrat WHERE id_contractant = '"+id+"';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		System.out.println("Contractant.getContratAssocies() : "+resultats.size()+" contrat(s) trouvé(s)");
		
		// Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id"));
			contrats.put(id, ContratFactory.getById(id));
		}
		this.contrats = contrats;
	}

	/**
	 * Ajoute le programme qui insère le contrat dans sa liste
	 * @param Programme prog
	 */
	public void ajouterContrat(Contrat contrat) {
		
		//Si le contrat n'est pas déjà associé
		if (!contrats.containsKey(contrat.getId())) {
			
			//On associe le contrat
			contrats.put(contrat.getId(), contrat);
		}
		
		System.out.println("Contrat ajouté");
	}

	/**
	 * Supprime de sa liste le contrat
	 * @param String
	 */
	public void retirerContrat(String idCon) {
		
		//On enlève le contrat
		contrats.remove(idCon);
		
		System.out.println("Contrat retiré");
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
		return nbRows>0;
	}
	
	/**
	 * Détruit le contractant et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		
		//On supprime les associations document/programme
		Contrat c;
		for (int i=0; i<contrats.size(); i++){
			c = (Contrat)contrats.get(i);
			c.supprimer();
		}
		
		//On supprime les infos de la base
		return ContratFactory.deleteById(id);
	}

//#### GETTERS ####
//#################
	
	/**
	 * Retourne l'ensemble des attributs sous la forme d'un dictionnaire
	 * @return Dictionary
	 */
	public Dictionary getAttributesDictionary() {
		
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
	public String getAdresse() {
		return adresse;
	}


	/**
	 * @return Renvoie codePostal.
	 */
	public String getCodePostal() {
		return codePostal;
	}


	/**
	 * @return Renvoie contrats.
	 */
	public Hashtable getContrats() {
		return contrats;
	}


	/**
	 * @return Renvoie fax.
	 */
	public String getFax() {
		return fax;
	}


	/**
	 * @return Renvoie id.
	 */
	public String getId() {
		return id;
	}


	/**
	 * @return Renvoie mail.
	 */
	public String getMail() {
		return mail;
	}


	/**
	 * @return Renvoie nom.
	 */
	public String getNom() {
		return nom;
	}


	/**
	 * @return Renvoie telephone.
	 */
	public String getTelephone() {
		return telephone;
	}


	/**
	 * @return Renvoie type.
	 */
	public String getType() {
		return type;
	}


	/**
	 * @return Renvoie ville.
	 */
	public String getVille() {
		return ville;
	}

//#### SETTERS ####
//#################
	
	/**
	 * @param adresse adresse à définir.
	 */
	public boolean setAdresse(String adresse) {
		String requete = "UPDATE contractant SET adresse = '" + adresse + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.adresse = adresse;
		}
		return nbRows>0;
	}


	/**
	 * @param codePostal codePostal à définir.
	 */
	public boolean setCodePostal(String codePostal) {
		String requete = "UPDATE contractant SET cp = '" + codePostal + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.codePostal = codePostal;
		}
		return nbRows>0;
	}

	/**
	 * @param fax fax à définir.
	 */
	public boolean setFax(String fax) {
		String requete = "UPDATE contractant SET fax = '" + fax + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.fax = fax;
		}
		return nbRows>0;
	}

	/**
	 * @param mail mail à définir.
	 */
	public boolean setMail(String mail) {
		String requete = "UPDATE contractant SET mail = '" + mail + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.mail = mail;
		}
		return nbRows>0;
	}


	/**
	 * @param nom nom à définir.
	 */
	public boolean setNom(String nom) {
		String requete = "UPDATE contractant SET nom = '" + nom + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.nom = nom;
		}
		return nbRows>0;
	}


	/**
	 * @param telephone telephone à définir.
	 */
	public boolean setTelephone(String telephone) {
		String requete = "UPDATE contractant SET type = '" + type + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.type = type;
		}
		return nbRows>0;
	}


	/**
	 * @param type type à définir.
	 */
	public boolean setType(String type) {
		String requete = "UPDATE contractant SET type = '" + type + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.type = type;
		}
		return nbRows>0;
	}


	/**
	 * @param ville ville à définir.
	 */
	public boolean setVille(String ville) {
		String requete = "UPDATE contractant SET ville = '" + ville + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise à jour s'est bien déroulée, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.ville = ville;
		}
		return nbRows>0;
	}
}
