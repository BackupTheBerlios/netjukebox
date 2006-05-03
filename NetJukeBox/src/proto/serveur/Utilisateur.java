package proto.serveur;

import java.util.Dictionary;
import java.util.Enumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

/**
 * Utlisateur du Net-JukeBox
 */
public class Utilisateur {

// ATTRIBUTS
//**********************************************
	/**
	 * Login : nom du compte utilisateur
	 */
	private String login;

	/**
	 * Mot de passe du compte utilisateur
	 */
	//private String pwd;

	/**
	 * Nom de l'utilisateur
	 */
	private String nom;

	/**
	 * Prénom de l'utilisateur
	 */
	private String prenom;

	/**
	 * Adresse email de l'utilisateur
	 */
	private String mail;

	/**
	 * Pays de l'utilisateur
	 */
	private String pays;

	/**
	 * Role de l'utilisateur
	 */
	private String role;

	/**
	 * #DEFINITION#
	 */
	//private Attribuer attribuer;

// METHODES STATIQUES
//*************************************************

	/**
	 * Création de l'utilisateur en base (= inscription)
	 * @param String login
	 * @param String pwd
	 * @param String nom
	 * @param String prenom
	 * @param String mail
	 * @param String pays
	 * @param String role
	 * @return Utilisateur
	 * @throws NamingException 
	 */
	public static Utilisateur create(String login, String pwd, String nom, String prenom, String mail, String pays, String role) throws NamingException {
		
		Ldap ldap = Ldap.getInstance();
		ldap.executeCreer(login, pwd, nom, prenom, mail, pays, role);
		
		//On retourne un objet configuré avec les infos issues de LDAP
		return Utilisateur.getByLogin(login);
		
	}
	 
	/**
	 * Insancie un objet utilisateur après avoir récupéré ces infos depuis LDAP à partir de son login
	 * @param login
	 * @throws NamingException 
	 */
	@SuppressWarnings("static-access")
	public static Utilisateur getByLogin(String log) throws NamingException {
		
		Ldap ldap = Ldap.getInstance();
		Dictionary resultats = ldap.getLogin(log);
		Enumeration donnee = resultats.elements();
		
		// S'il y a un resultat
		if (resultats != null && resultats.size()>0) {
			
			Attributes result = (Attributes) donnee.nextElement();
			Attribute uid = result.get("uid");
			Attribute ou = result.get("ou");
			Attribute sn = result.get("sn");
			Attribute givenName = result.get("givenName");
			Attribute mail = result.get("mail");
			Attribute st = result.get("st");
									
			System.out.println("-------- Utilisateur -----------");
			System.out.println("Nb de champs: "+result.size());
			System.out.println("Login : " + uid.get());
			System.out.println("Nom : " + sn.get());
			System.out.println("Prenom : " + givenName.get());
			System.out.println("Mail : " + mail.get());
			System.out.println("Pays : " + st.get());
			System.out.println("role : " + ou.get());
			System.out.println("-----------------------------");
			
			String login = (String) uid.get();
			//String passwd = (String) pwd.get();
			String nom = (String) sn.get();
			String prenom = (String) givenName.get();
			String email = (String) mail.get();
			String pays = (String) st.get();
			String role = (String) ou.get();
			
			//On retourne l'objet
			return new Utilisateur(login, nom, prenom, email, pays, role);
		}
		
		/**
		if (log.equalsIgnoreCase("toto")) {

		if (log.equalsIgnoreCase("toto")) {

			//On retourne un objet utilisateur configuré
			return new Utilisateur("toto", "toto", "Toto", "Toto", "toto@netjukebox.com", "France", "role");
		}*/
		//Sinon, on retourne un objet vide
		return null;
	}
		
	/**
	 * Détruit les infos d'un canal contenues dans la base
	 * @param login
	 * @return
	 * @throws NamingException 
	 */
	public static boolean deleteByLogin(String login) throws NamingException {
		
		//On supprime l'utilisateur de l'annuaire, en partant d'un login
		Ldap ldap = Ldap.getInstance();
		ldap.executeSupprimer(login);
		
		//On retourne le resultat de l'opération (succès/échec)
		return true;
	}
	
	/**
	 * Vérifie l'existence du Login
	 * @param pwd
	 * @param login
	 * @throws NamingException 
	 */
	public static boolean verifierLogin(String login) throws NamingException {
		String log, pwd, r;
		Ldap ldap = Ldap.getInstance();
		Dictionary resultats = ldap.getLogin(login);
		try {
			Enumeration donnee = resultats.elements();
			// S'il y a un resultat
			if (resultats != null && resultats.size()>0) {
				Attributes result = (Attributes) donnee.nextElement();
				Attribute uid = result.get("uid");
				Attribute passwd = result.get("userPassword");
				Attribute role = result.get("ou");
				log = (String) uid.get();
				pwd = (String) passwd.get();
				r = (String) role.get();
				ldap.openLdap("com.sun.jndi.ldap.LdapCtxFactory", "ldap://localhost:389/dc=netjukebox,dc=com", "simple", log, pwd, r, "dc=netjukebox,dc=com");
				//return log.equalsIgnoreCase(login); 
			} //else return false;
			return true;
		} catch (Exception e){
			System.out.println("Erreur le login : " + login + " nexiste pas");
			return false;
		}
		//return (login.equalsIgnoreCase("toto"));
	}
	
	
// CONSTRUCTEURS
//************************************************	
		
		/**
		 * Constructeur simple
		 */
		public Utilisateur() {
		}
		
		/**
		 * Constructeur complet
		 */
		public Utilisateur(String login, String nom, String prenom, String mail, String pays, String role) {

			this.login = login;
			this.nom=nom;
			this.prenom=prenom;
			this.mail=mail;
			this.pays=pays;
			this.role=role;
		}
	
// METHODES DYNAMIQUES
//*************************************************
	
	/**
	 * Supprimer l'utilisateur
	 * @return boolean
	 * @throws NamingException 
	 */
	public boolean supprimer() throws NamingException {
		
		//On déconnecte l'utilisateur
		this.deconnecter();
		
		//On supprime ses infos de l'annuaire
		return Utilisateur.deleteByLogin(this.login);
	}	
		
		
	/**
	 * Deconnecterl'utilisateur
	 * @return
	 */
	public boolean deconnecter() {
		// your code here
		return false;
	}

	/**
	 * Deconnexion de l'utilisateur
	 */
	public void deconnexion() {
		// your code here
	}

	/**
	 * Connecter l'utilisateur
	 * @param Login
	 * @param Pwd
	 * @return
	 */
	public boolean connecter(String Login, String Pwd) {
		// your code here
		return false;
	}

	/**
	 * Connexion de l'utilisateurs
	 * @return
	 */
	public boolean connexion() {
		// your code here
		return true;
	}

	/**
	 * Modifier les informations de l'utilisateur
	 * @throws NamingException 
	 */
	public void modifierInfos(String login, String role,String newlogin,String nom, String prenom, String mail, String pays) throws NamingException {
		
		Ldap ldap = Ldap.getInstance();
		ldap.ModifieAttributs(login, role, newlogin, nom, prenom, mail, pays);
	}

	/**
	 * Attribuer une permission à l'utilisateur
	 * @param Id_Perm
	 * @param Lib_Perm
	 * @return boolean
	 */
	public boolean setPermission(int Id_Perm, String Lib_Perm) {
		// your code here
		return false;
	}

	/**
	 * Vérification de l'état de connexion de l'utilisateur
	 * @return
	 */
	public boolean verifConnecte() {
		// your code here
		return false;
	}

	/**
	 * Retourne le login de l'utilisateur
	 * @return String
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Retourne le nom de l'utilisateur
	 * @return String
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Retourne le prénom de l'utilisateur
	 * @return String
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * Retourne l'adresse email de l'utilisateur
	 * @return String
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * Retourne le pays de l'utilisateur
	 * @return String
	 */
	public String getPays() {
		return pays;
	}
	
	/**
	 * Retourne le role de l'utilisateur
	 * @return String
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Vérification de l'état de connexion de l'utilisateur
	 * @return
	 */
	public boolean estConnecte() {
		return false;
	}

	/**
	 * Sélectionne l'utilisateur
	 */
	public void selectionner() {
		// your code here
	}

	/**
	 * Modifier l'utilisateur
	 * @param Nom
	 * @param Prenom
	 * @param Mail
	 * @param Pwd
	 * @param Pays
	 * @return boolean
	 */
	public boolean modifier(String Nom, String Prenom, String Mail, String Pwd, String Pays) {
		// your code here
		return false;
	}

	/**
	 * Mise à jour des informations de l'utilisateur
	 * @param Nom
	 * @param Prenom
	 * @param Mail
	 * @param Pwd
	 * @param Pays
	 * @return boolean
	 */
	public boolean majInfos(String Nom, String Prenom, String Mail, String Pwd, String Pays) {
		// your code here
		return false;
	}

	/**
	 * Retourne les permissions attribuées à l'utilisateur
	 */
	public void getPermissions() {
		// your code here
	}

	/**
	 * Ajouter une permision à l'utilisateur
	 * @param Id_Perm
	 * @return boolean
	 */
	@SuppressWarnings("static-access")
	public void ajouterPermission(String login, String ancienrole, String nouveaurole) {
		Ldap ldap = Ldap.getInstance();
		ldap.changerRole(login, ancienrole, nouveaurole);
	}

	/**
	 * Retirer une permission à l'utilisateur 
	 * @param Id_Perm
	 * @return
	 */
	public boolean retirerPermission(int Id_Perm) {
		// your code here
		return false;
	}

	/**
	 * Définir une permission
	 * @param Id_Perm
	 * @param Lib_Perm
	 * @return
	 */
	public boolean definirPermission(int Id_Perm, String Lib_Perm) {
		// your code here
		return false;
	}
	
	/**
	 * Vérfie que l'utilisateur a bien la permission requise
	 * @param String perm
	 * @return boolean
	 */
	public boolean verifPermission(String perm) {

		//if (permssions.containsKey(perm)) return permssions.get(perm);
		//else return false;
				
		return true;
	}
}