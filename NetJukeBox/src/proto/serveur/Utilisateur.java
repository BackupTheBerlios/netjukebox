package proto.serveur;

import org.apache.log4j.Logger;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;


/**
 * Utlisateur du Net-JukeBox
 */
public class Utilisateur {
	/**
	 * Crée le logger de la classe
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
	private Role role;

	/**
	 * Permissions
	 */
	private Hashtable permissions;
	
	/**
	 * Etat
	 */
	private String etat = "DECONNECTE";

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
		logger.debug("Démarrage: Utilisateur.create");
		Ldap ldap = Ldap.getInstance();
		ldap.executeCreer(login, pwd, nom, prenom, mail, pays, role);
		//On retourne un objet configuré avec les infos issues de LDAP
		logger.debug("Arrêt: Utilisateur.create");
		return Utilisateur.getByLogin(login);
	}

	/**
	 * Insancie un objet utilisateur après avoir récupéré ces infos depuis LDAP à partir de son login
	 * @param login
	 * @throws NamingException 
	 */
	@SuppressWarnings("static-access")
	public static Utilisateur getByLogin(String log) throws NamingException {
		logger.debug("Démarrage: Utilisateur.getByLogin");
		
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
					
			//On retourne l'objet
			logger.debug("Arrêt: Utilisateur.GetByLogin");
			return new Utilisateur(login, encStrPassword, nom, prenom, email, pays, role);
		}			
	
		/**
		if (log.equalsIgnoreCase("toto")) {

			//On retourne un objet utilisateur configuré
			return new Utilisateur("toto", "toto", "Toto", "Toto", "toto@netjukebox.com", "France", "role");
		}*/
		//Sinon, on retourne un objet vide*
		logger.debug("Arrêt: Utilisateur.GetByLogin");
		return null;
	}
		
	/**
	 * Détruit les infos d'un canal contenues dans la base
	 * @param login
	 * @return
	 * @throws NamingException 
	 */
	public static boolean deleteByLogin(String login) throws NamingException {

		logger.debug("Démarrage: deleteByLogin");				
		//On supprime l'utilisateur de l'annuaire, en partant d'un login
		Ldap ldap = Ldap.getInstance();
		//On retourne le resultat de l'opération (succès/échec)
		if (ldap.executeSupprimer(login)) {
			logger.debug("Arrêt: deleteByLogin");
			return true;
		} else {
			logger.debug("Arrêt: deleteByLogin");
			return false;
		}
	}
	
	/**
	 * Vérifie l'existence du Login
	 * @param pwd
	 * @param login
	 * @throws NamingException 
	 */
	public static /*@ pure @*/ boolean verifierLogin(String login) throws NamingException {
		
		logger.debug("Démarrage: verifierLogin");
		
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

				logger.debug("Arrêt: verifierLogin");
				return true;
				//return log.equalsIgnoreCase(login); 
			} else {
				logger.info("Err: " + login + " non présent" );
				logger.debug("Arrêt: verifierLogin");
				return false;
			}
		} catch (Exception e){
			logger.error("Erreur, le login : " + login + " n'existe pas");
			logger.debug("Arrêt: verifierLogin");
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
		logger.debug("Démarrage: verifierPwd");
		
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
				logger.debug("Arrêt: verifierPwd");
				return true;
				
				//return log.equalsIgnoreCase(login); 
				} else {
					logger.info("err : " + login + " & " + pwd + " incorrect" );
					logger.debug("Arrêt: verifierPwd");
					return false;	
					
				}
		} catch (Exception e){
			logger.error("Erreur le login : " + login + " n'existe pas");
			logger.debug("Arrêt: verifierPwd");
			return false;
		}
	}
	
// CONSTRUCTEUR
//************************************************	
		
		/**
		 * Constructeur
		 * @param String login
		 * @param String pwd
		 * @param String nom
		 * @param String prenom
		 * @param String mail
		 * @param String pays
		 * @param String role
		 */
		public Utilisateur(String login, String pwd, String nom, String prenom, String mail, String pays, String role) {

			this.login = login;
			this.pwd = pwd;
			this.nom=nom;
			this.prenom=prenom;
			this.mail=mail;
			this.pays=pays;
			this.role=RoleFactory.getById(role);
		}
	
// METHODES DYNAMIQUES
//*************************************************
	
	/**
	 * Supprimer l'utilisateur
	 * @return boolean
	 * @throws NamingException 
	 */
	public boolean supprimer() throws NamingException {
		logger.debug("Démarrage: supprimer");
		
		//On déconnecte l'utilisateur
		this.deconnecter();
		
		//On supprime ses infos de l'annuaire
		logger.debug("Arrêt: supprimer");
		if (Utilisateur.deleteByLogin(this.login)) {
			
			//On supprime les associations Utilisateur/permission
			Enumeration idPerms = permissions.keys();
			while (idPerms.hasMoreElements()) {
				retirerPermission((String)idPerms.nextElement());
			}
			return true;
		}
		return false;
	}	
		
		
	/**
	 * Deconnecter l'utilisateur
	 * @return boolean
	 */
	public boolean deconnecter() {
		this.etat = "DECONNECTE";
		return true;
	}


	/**
	 * Connecter l'utilisateur
	 * @return boolean
	 */
	public boolean connecter() {
		this.etat = "CONNNECTE";
		return true;
	}

	/**
	 * Lister les utilisateurs
	 * @return Vector
	 * @throws NamingException 
	 */
	public static Vector listerUtilisateur() throws NamingException {
		Ldap ldap = Ldap.getInstance();
		Vector liste = ldap.listerUtilisateur();
		Vector vUtil = new Vector();
		Dictionary d = null;
		for(int i=0; i < liste.size(); i++)
            if(liste.elementAt(i) != null){
            	Attributes dico = (Attributes)liste.elementAt(i);
            	Attribute uidAtt = (Attribute) dico.get("uid");
    			String uid = (String)uidAtt.get();
    			d.put("uid",uid);
    			Attribute snAtt = (Attribute) dico.get("sn");
    			String sn = (String)snAtt.get();
    			d.put("sn",sn);
    			Attribute givenNameAtt = (Attribute) dico.get("givenName");
    			String givenName = (String)givenNameAtt.get();
    			d.put("givenName",givenName);
    			Attribute mailAtt = (Attribute) dico.get("mail");
    			String mail = (String)mailAtt.get();
    			d.put("mail",mail);
    			Attribute stAtt = (Attribute) dico.get("st");
    			String st = (String)stAtt.get();
    			d.put("st",st);
    			Attribute ouAtt = (Attribute) dico.get("ou");
    			String ou = (String)ouAtt.get();
    			d.put("ou",ou);
    			vUtil.add(d);
            }
		return vUtil;
	}
		
	/**
	 * Modifier les informations de l'utilisateur
	 * @throws NamingException 
	 */
	public boolean modifier(String login, String role, String newlogin, String pwd, String nom, String prenom, String mail, String pays) throws NamingException {
		logger.debug("Démarrage: modifierInfos");
		
		Ldap ldap = Ldap.getInstance();
		ldap.ModifieAttributs(login, role, newlogin, pwd, nom, prenom, mail, pays);
		this.login = login;
		this.pwd = pwd;
		this.nom=nom;
		this.prenom=prenom;
		this.mail=mail;
		this.pays=pays;
		this.role=RoleFactory.getById(role);
		
		logger.debug("Arrêt: modifierInfos");
		return true;
	}

	/**
	 * Vérification de l'état de connexion de l'utilisateur
	 * @return boolean
	 */
	public /*@ pure @*/ boolean estConnecte() {
		return etat.equalsIgnoreCase("CONNECTE");
	}

	/**
	 * Retirer une permission à l'utilisateur 
	 * @param String idPerm
	 * @return boolean
	 */
	public boolean retirerPermission(String idPerm) {
		logger.debug("Démarrage: retirerPermission");
		
		// On retire la permission du rôle
		String requete = "DELETE FROM attribuer WHERE login='"+login+"' AND id_permission='"+idPerm+"';";
		System.err.println(requete);
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la suppression s'est bien déroulée
		if (nbRows>0) {
			
			//On met à jour le vecteur d'association
			permissions.remove(idPerm);
			logger.debug("Arrêt: retirerPermission");			
			return true;
		}
		//Sinon
		logger.debug("Arrêt: retirerPermission");
		return false;
	}
	
	/**
	 * Ajouter une permission à l'utilisateur 
	 * @param String idPerm
	 * @return boolean
	 */
	public boolean ajouterPermission(Permission perm) {
		logger.debug("Démarrage: ajouterPermission");
		//On ajoute la permission au rôle
		String requete = "INSERT INTO attribuer (login, id_permission) VALUES ('" + login + "', '" + perm.getId()+"');";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien déroulée
		if (nbRows>0) {
			
			//On met à jour le vecteurs d'association
			permissions.put(perm.getId(), perm);
			logger.debug("Arrêt: ajouterPermission");
			return true;
		}
		logger.debug("Arrêt: ajouterPermission");
		return false;
	}

	
	/**
	 * Vérfie que l'utilisateur a bien la permission requise
	 * @param String idPerm
	 * @return boolean
	 */
	public /*@ pure @*/ boolean verifPermission(String idPerm) {
		return (role.getPermissions().containsKey(idPerm) || permissions.containsKey(idPerm));
	}

//#### GETTERS ####	
	
	/**
	 * Retourne les permissions attribuées à l'utilisateur
	 * @return Hashtable
	 */
	public /*@ pure @*/ Hashtable getPermissions() {
		return permissions;
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
	 * Retourne le prénom de l'utilisateur
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
	public /*@ pure @*/ Role getRole() {
		return role;
	}
	
//	#### SETTERS ####	

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
	 * Change le rôle de l'utilisateur
	 * @param String role
	 * @return boolean
	 */
	@SuppressWarnings("static-access")
	public void setRole(String nouveauRole) {
		logger.debug("Démarrage: changerRole");
		
		Ldap ldap = Ldap.getInstance();
		ldap.changerRole(login, role.getId(), nouveauRole);
		this.role = RoleFactory.getById(nouveauRole);
		
		logger.debug("Arrêt: changerRole");
	}
}