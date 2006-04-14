package proto.serveur;

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
	 * #DEFINITION#
	 */
	private Attribuer attribuer;

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
	 * @return Utilisateur
	 */
	public static Utilisateur create(String login, String pwd, String nom, String prenom, String mail, String pays){
		
		//On ins�re les infos dans l'annuaire
		//***********
		// => LDAP <=
		//***********
		
		//On retourne un objet configur� avec les infos issues de LDAP
		return Utilisateur.getByLogin(login);
	}
	 
	/**
	 * Insancie un objet utilisateur apr�s avoir r�cup�r� ces infos depuis LDAP � partir de son login
	 * @param login
	 */
	public static Utilisateur getByLogin(String login) {

		  //On r�cup�re les infos depuis l'annuaire
		  //***********
		  // => LDAP <=
		  //***********

		  //On retourne une nouvelle instance d'Utilisateur avec ces infos
		  return new Utilisateur("toto", "toto", "Toto", "Toto", "toto@netjukebox.com", "France");
	}
	
	/**
	 * D�truit les infos d'un canal contenues dans la base
	 * @param login
	 * @return
	 */
	public static boolean deleteByLogin(String login) {
		
		//On supprime l'utilisateur de l'annuaire, en partant d'un login
		
		//************
		// => LDAP <=
		//************
		
		//On retourne le resultat de l'op�ration (succ�s/�chec)
		return true;
	}

	/**
	 * V�rifie l'existence du couple Login/Pwd
	 * @param pwd
	 * @param login
	 */
	public static boolean verifierPwd(String pwd, String login) {
		//*************
		// => LDAP <=
		//*************
		
		return (pwd=="toto" && login=="toto");
	}
	
	/**
	 * V�rifie l'existence du Login
	 * @param pwd
	 * @param login
	 */
	public static boolean verifierLogin(String login) {
		//*************
		// => LDAP <=
		//*************
		
		return (login=="toto");
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
		public Utilisateur(String login, String pwd, String nom, String prenom, String mail, String pays) {
			
			this.login = login;
			this.pwd = pwd;
			this.nom=nom;
			this.prenom=prenom;
			this.mail=mail;
			this.pays=pays;
		}
	
// METHODES DYNAMIQUES
//*************************************************
	
	/**
	 * Supprimer l'utilisateur
	 * @return boolean
	 */
	public boolean supprimer() {
		
		//On d�connecte l'utilisateur
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
	 */
	public void modifierInfos() {
		// your code here
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
	 * Retourne le pr�nom de l'utilisateur
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
	 * Retourne le mot de passe de l'utilisateur
	 * @return String
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * Retourne le pays de l'utilisateur
	 * @return String
	 */
	public String getPays() {
		return pays;
	}

	/**
	 * V�rification de l'�tat de connexion de l'utilisateur
	 * @return
	 */
	public boolean estConnecte() {
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
	public void getPermissions() {
		// your code here
	}

	/**
	 * Ajouter une permision � l'utilisateur
	 * @param Id_Perm
	 * @return boolean
	 */
	public boolean ajouterPermission(int Id_Perm) {
		// your code here
		return false;
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
}
