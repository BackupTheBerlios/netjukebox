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
	public String login;

	/**
	 * Mot de passe du compte utilisateur
	 */
	public String pwd;

	/**
	 * Nom de l'utilisateur
	 */
	public String nom;

	/**
	 * Prénom de l'utilisateur
	 */
	public String prenom;

	/**
	 * Adresse email de l'utilisateur
	 */
	public String mail;

	/**
	 * Pays de l'utilisateur
	 */
	public String pays;

	/**
	 * #DEFINITION#
	 */
	public Attribuer attribuer;

// METHODES STATIQUES
//*************************************************

	/**
	 * Vérifie l'existence du couple Login/Pwd
	 * @param pwd
	 * @param login
	 */
	public static boolean VérifierPwd(String pwd, String login) {
		//*************
		// => LDAP <=
		//*************
		
		return (pwd=="toto" && login=="toto");
	}
	
// METHODES DYNAMIQUES
//*************************************************
	
	/**
	 * Constructeur
	 */
	public Utilisateur() {
	}
	
	/**
	 * Constructeur
	 */
	public Utilisateur(String login) {
		super();
		
		//************
		// => LDAP <=
		//************
		
		this.login = login;
		this.pwd = "toto";
		this.nom="toto";
		this.prenom="toto";
		this.mail="toto@netjukebox.net";
		this.pays="France";
	}
	
	/**
	 * Deconnecterl'utilisateur
	 * @return
	 */
	public Boolean Deconnecter() {
		// your code here
		return null;
	}

	/**
	 * Deconnexion de l'utilisateur
	 */
	public void Deconnexion() {
		// your code here
	}

	/**
	 * Connecter l'utilisateur
	 * @param Login
	 * @param Pwd
	 * @return
	 */
	public boolean Connecter(String Login, String Pwd) {
		// your code here
		return false;
	}

	/**
	 * Connexion de l'utilisateurs
	 * @return
	 */
	public boolean Connexion() {
		// your code here
		return false;
	}

	/**
	 * Inscrire l'utilisateur
	 * @param Nom
	 * @param Prenom
	 * @param Mail
	 * @param Pwd
	 * @param Pays
	 * @return
	 */
	public Boolean Inscrire(String Nom, String Prenom, String Mail, String Pwd,
			String Pays) {
		// your code here
		return null;
	}

	/**
	 * Inscription de l'utilisateur
	 * @param Nom
	 * @param Prenom
	 * @param Mail
	 * @param Pwd
	 * @param Pays
	 * @return
	 */
	public Boolean Inscription(String Nom, String Prenom, String Mail,
			String Pwd, java.util.List Pays) {
		// your code here
		return null;
	}

	/**
	 * Supprimer l'utilisateur
	 * @param Login
	 */
	public void SupprimerUtilisateur(String Login) {
		// your code here
	}
	
	/**
	 * Supprimer les informations relatives à l'utilisateur
	 */
	public void SupprimerInfos() {
		// your code here
	}



	/**
	 * Modifier les informations de l'utilisateur
	 */
	public void ModifierInfos() {
		// your code here
	}

	

	/**
	 * Attribuer une permission à l'utilisateur
	 * @param Id_Perm
	 * @param Lib_Perm
	 * @return
	 */
	public Boolean SetPermission(int Id_Perm, String Lib_Perm) {
		// your code here
		return null;
	}

	/**
	 * Vérification de l'état de connexion de l'utilisateur
	 * @return
	 */
	public Boolean VérifConnecté() {
		// your code here
		return null;
	}

	/**
	 * Supprimer l'utilisateur
	 */
	public void supprimer() {
		// your code here
	}

	/**
	 * Retourne le login de l'utilisateur
	 * @return
	 */
	public String GetLogin() {
		// your code here
		return null;
	}

	/**
	 * Retourne le nom de l'utilisateur
	 * @return
	 */
	public String GetNom() {
		// your code here
		return null;
	}

	/**
	 * Retourne le prénom de l'utilisateur
	 * @return
	 */
	public String GetPrenom() {
		// your code here
		return null;
	}

	/**
	 * Retourne l'adresse email de l'utilisateur
	 * @return
	 */
	public String GetMail() {
		// your code here
		return null;
	}

	/**
	 * Retourne le mot de passe de l'utilisateur
	 * @return
	 */
	public String GetPwd() {
		// your code here
		return null;
	}

	/**
	 * Retourne le pays de l'utilisateur
	 * @return
	 */
	public String GetPays() {
		// your code here
		return null;
	}

	/**
	 * Vérification de l'état de connexion de l'utilisateur
	 * @return
	 */
	public Boolean EstConnecté() {
		// your code here
		return null;
	}

	/**
	 * Sélectionne l'utilisateur
	 */
	public void Selectionner() {
		// your code here
	}

	/**
	 * Modifier l'utilisateur
	 * @param Nom
	 * @param Prenom
	 * @param Mail
	 * @param Pwd
	 * @param Pays
	 * @return
	 */
	public Boolean Modifier(String Nom, String Prenom, String Mail, String Pwd,
			String Pays) {
		// your code here
		return null;
	}

	/**
	 * Mise à jour des informations de l'utilisateur
	 * @param Nom
	 * @param Prenom
	 * @param Mail
	 * @param Pwd
	 * @param Pays
	 * @return
	 */
	public Boolean MajInfos(String Nom, String Prenom, String Mail, String Pwd,
			String Pays) {
		// your code here
		return null;
	}

	/**
	 * Retourne les permissions attribuées à l'utilisateur
	 */
	public void GetPermissions() {
		// your code here
	}

	/**
	 * Ajouter une permision à l'utilisateur
	 * @param Id_Perm
	 * @return
	 */
	public Boolean ajouterPermission(int Id_Perm) {
		// your code here
		return null;
	}

	/**
	 * Retirer une permission à l'utilisateur 
	 * @param Id_Perm
	 * @return
	 */
	public Boolean RetirerPermission(int Id_Perm) {
		// your code here
		return null;
	}

	/**
	 * Définir une permission
	 * @param Id_Perm
	 * @param Lib_Perm
	 * @return
	 */
	public Boolean DefinirPermission(int Id_Perm, String Lib_Perm) {
		// your code here
		return null;
	}
}
