package proto.serveur;

import org.apache.log4j.Logger;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Utlisateur du Net-JukeBox
 */
public class Utilisateur {
	/**
	 * Cr�e le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(Utilisateur.class);

// ATTRIBUTS
//**********************************************
	/**
	 * Login : nom du compte utilisateur
	 */
	private String login;

	/**
	 * Mot de passe du compte utilisateur
	 */
	private String pwd;

	/**
	 * Nom de l'utilisateur
	 */
	private String nom;

	/**
	 * Pr�nom de l'utilisateur
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
	 * Permissions
	 */
	private Hashtable permissions;

// METHODES STATIQUES
//*************************************************

	/**
	 * Cr�ation de l'utilisateur en base (= inscription)
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
		
	    //PropertyConfigurator.configure("src/proto/serveur/log4j.prop");
		PropertyConfigurator.configure("C:/Documents and Settings/Marie Rubini/Mes documents/workspace/NetJukeBox/proto/serveur/log4j.prop");
		logger.debug("D�marrage: Utilisateur.create");
				
		Ldap ldap = Ldap.getInstance();
		ldap.executeCreer(login, pwd, nom, prenom, mail, pays, role);
		
		logger.debug("Arr�t: Utilisateur.create");
		
		//On retourne un objet configur� avec les infos issues de LDAP
		return Utilisateur.getByLogin(login);
		
	}

	/**
	 * Insancie un objet utilisateur apr�s avoir r�cup�r� ces infos depuis LDAP � partir de son login
	 * @param login
	 * @throws NamingException 
	 */
	@SuppressWarnings("static-access")
	public static Utilisateur getByLogin(String log) throws NamingException {
		logger.debug("D�marrage: Utilisateur.getByLogin");
		
		Ldap ldap = Ldap.getInstance();
		Dictionary resultats = ldap.getLogin(log);
		Enumeration donnee = resultats.elements();
		
		// S'il y a un resultat
		if (resultats != null && resultats.size()>0) {
			
			Attributes result = (Attributes) donnee.nextElement();
			Attribute uid = result.get("uid");
			Attribute pwd = result.get("userPassword");
			Attribute ou = result.get("ou");
			Attribute sn = result.get("sn");
			Attribute givenName = result.get("givenName");
			Attribute mail = result.get("mail");
			Attribute st = result.get("st");
			
			byte[] encPassword = (byte[])pwd.get(0);
			String encStrPassword = new String(encPassword);
			
			System.out.println("-------- Utilisateur -----------");
			System.out.println("Nb de champs: "+result.size());
			System.out.println("Login : " + uid.get());
			System.out.println("Mot de passe : " + encStrPassword);
			System.out.println("Nom : " + sn.get());
			System.out.println("Prenom : " + givenName.get());
			System.out.println("Mail : " + mail.get());
			System.out.println("Pays : " + st.get());
			System.out.println("role : " + ou.get());
			System.out.println("-----------------------------");
			
			String login = (String) uid.get();
			String nom = (String) sn.get();
			String prenom = (String) givenName.get();
			String email = (String) mail.get();
			String pays = (String) st.get();
			String role = (String) ou.get();
			
			logger.debug("Arr�t: Utilisateur.GetByLogin");
			
			//On retourne l'objet
			return new Utilisateur(login, encStrPassword, nom, prenom, email, pays, role);
		}			
		
		/**
		if (log.equalsIgnoreCase("toto")) {

		if (log.equalsIgnoreCase("toto")) {

			//On retourne un objet utilisateur configur�
			return new Utilisateur("toto", "toto", "Toto", "Toto", "toto@netjukebox.com", "France", "role");
		}*/
		//Sinon, on retourne un objet vide
		return null;
	}
		
	/**
	 * D�truit les infos d'un canal contenues dans la base
	 * @param login
	 * @return
	 * @throws NamingException 
	 */
	public static boolean deleteByLogin(String login) throws NamingException {

		logger.debug("D�marrage: deleteByLogin");				
		//On supprime l'utilisateur de l'annuaire, en partant d'un login
		Ldap ldap = Ldap.getInstance();
		//On retourne le resultat de l'op�ration (succ�s/�chec)
		if (ldap.executeSupprimer(login)) {
			logger.debug("Arr�t: deleteByLogin");
			return true;
		} else {
			logger.debug("Arr�t: deleteByLogin");
			return false;
		}
	}
	
	/**
	 * V�rifie l'existence du Login
	 * @param pwd
	 * @param login
	 * @throws NamingException 
	 */
	public static /*@ pure @*/ boolean verifierLogin(String login) throws NamingException {
		
		logger.debug("D�marrage: verifierLogin");
		
		String log, r, encStrPassword;
		
		Ldap ldap = Ldap.getInstance();
		Dictionary resultats = ldap.getLogin(login);
		try {
			Enumeration donnee = resultats.elements();
			// S'il y a un resultat
			if (resultats != null && resultats.size()>0) {
				Attributes result = (Attributes) donnee.nextElement();
				Attribute uid = result.get("uid");
				Attribute role = result.get("ou");
				Attribute at = result.get("userPassword");
				byte[] encPassword = (byte[])at.get(0);
				encStrPassword = new String(encPassword);
				log = (String) uid.get();
				r = (String) role.get();
				

				logger.debug("Arr�t: verifierLogin");
				return true;
				//return log.equalsIgnoreCase(login); 
			} else {
				logger.info("err : " + login + " non pr�sent" );
				logger.debug("Arr�t: verifierLogin");
				return false;
			}
		} catch (Exception e){
			logger.fatal("Erreur le login : " + login + " n'existe pas");
			logger.fatal("Arr�t: verifierLogin");
			return false;
		}
		//return (login.equalsIgnoreCase("toto"));
	}

	/**
	 * Verifie le couple login & mot de passe
	 * @param pwd
	 * @param login
	 * @return boolean
	 */
	public static /*@ pure @*/ boolean verifierPwd(String login, String pwd) {
		logger.debug("D�marrage: verifierPwd");
		
		String encStrPassword = null;
		String log = null;
		
		Ldap ldap = Ldap.getInstance();
		Dictionary resultats = ldap.getLogin(login);
		try {
			Enumeration donnee = resultats.elements();
			// S'il y a un resultat
			if (resultats != null && resultats.size()>0) {
				Attributes result = (Attributes) donnee.nextElement();
				Attribute uid = result.get("uid");
				Attribute at = result.get("userPassword");
				byte[] encPassword = (byte[])at.get(0);
				encStrPassword = new String(encPassword);
				log = (String) uid.get();
			}
			
			if ((login.equalsIgnoreCase(log)) && (pwd.equalsIgnoreCase(encStrPassword))) {
				logger.info(login + " & " + pwd + " correct" );
				logger.debug("Arr�t: verifierPwd");
				return true;
				
				//return log.equalsIgnoreCase(login); 
				} else {
					logger.info("err : " + login + " & " + pwd + " incorrect" );
					logger.debug("Arr�t: verifierPwd");
					return false;	
					
				}
		} catch (Exception e){
			logger.fatal("Erreur le login : " + login + " n'existe pas");
			logger.fatal("Arr�t: verifierPwd");
			return false;
		}
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
		public Utilisateur(String login, String pwd, String nom, String prenom, String mail, String pays, String role) {

			this.login = login;
			this.pwd = pwd;
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
		logger.debug("D�marrage: supprimer");
		
		//On d�connecte l'utilisateur
		this.deconnecter();
		
		//On supprime ses infos de l'annuaire
		boolean returnboolean = Utilisateur.deleteByLogin(this.login);
		logger.debug("Arr�t: supprimer");
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
	public boolean modifierInfos(String login, String role, String newlogin, String pwd, String nom, String prenom, String mail, String pays) throws NamingException {
		
		logger.debug("D�marrage: modifierInfos");
		Ldap ldap = Ldap.getInstance();
		ldap.ModifieAttributs(login, role, newlogin, pwd, nom, prenom, mail, pays);
		logger.debug("Arr�t: modifierInfos");
		return true;
	}

	/**
	 * Attribuer une permission � l'utilisateur
	 * @param Id_Perm
	 * @param Lib_Perm
	 * @return boolean
	 */
	public boolean setPermission(int Id_Perm, String Lib_Perm) {
		// your code here
		return false;
	}

	/**
	 * V�rification de l'�tat de connexion de l'utilisateur
	 * @return
	 */
	public /*@ pure @*/boolean verifConnecte() {
		// your code here
		return false;
	}

	/**
	 * Retourne le login de l'utilisateur
	 * @return String
	 */
	public /*@ pure @*/ String getLogin() {
		return login;
	}

	/**
	 * Retourne le mot de passe de l'utilisateur
	 * @return String
	 */
	public /*@ pure @*/ String getPwd() {
		return pwd;
	}	
	
	/**
	 * Retourne le nom de l'utilisateur
	 * @return String
	 */
	public /*@ pure @*/ String getNom() {
		return nom;
	}

	/**
	 * Retourne le pr�nom de l'utilisateur
	 * @return String
	 */
	public /*@ pure @*/ String getPrenom() {
		return prenom;
	}

	/**
	 * Retourne l'adresse email de l'utilisateur
	 * @return String
	 */
	public /*@ pure @*/ String getMail() {
		return mail;
	}

	/**
	 * Retourne le pays de l'utilisateur
	 * @return String
	 */
	public /*@ pure @*/String getPays() {
		return pays;
	}
	
	/**
	 * Retourne le role de l'utilisateur
	 * @return String
	 */
	public /*@ pure @*/ String getRole() {
		return role;
	}

	/**
	 * V�rification de l'�tat de connexion de l'utilisateur
	 * @return
	 */
	public /*@ pure @*/ boolean estConnecte() {
		return false;
	}

	/**
	 * S�lectionne l'utilisateur
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
	 * Mise � jour des informations de l'utilisateur
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
	 * Retourne les permissions attribu�es � l'utilisateur
	 */
	public /*@ pure @*/ void getPermissions() {
		// your code here
	}

	/**
	 * Ajouter une permision � l'utilisateur
	 * @param Id_Perm
	 * @return boolean
	 */
	@SuppressWarnings("static-access")
	public void ajouterPermission(String login, String ancienrole, String nouveaurole) {
		
		logger.debug("D�marrage: ajouterPermission");
		Ldap ldap = Ldap.getInstance();
		ldap.changerRole(login, ancienrole, nouveaurole);
		logger.debug("Arr�t: ajouterPermission");
	}

	/**
	 * Retirer une permission � l'utilisateur 
	 * @param Id_Perm
	 * @return
	 */
	public boolean retirerPermission(int Id_Perm) {
		// your code here
		return false;
	}

	/**
	 * D�finir une permission
	 * @param Id_Perm
	 * @param Lib_Perm
	 * @return
	 */
	public boolean definirPermission(int Id_Perm, String Lib_Perm) {
		// your code here
		return false;
	}
	
	/**
	 * V�rfie que l'utilisateur a bien la permission requise
	 * @param String perm
	 * @return boolean
	 */
	public /*@ pure @*/ boolean verifPermission(String perm) {

		//if (permssions.containsKey(perm)) return permssions.get(perm);
		//else return false;
				
		return true;
	}
}