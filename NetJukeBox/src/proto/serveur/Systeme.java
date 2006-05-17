package proto.serveur;

import java.io.*;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.prefs.Preferences;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;

/**
 * Classe contenant la logique principale du serveur principal
 */
public class Systeme {

// ATTRIBUTS DU SYSTEME
//************************************************************
	
	/**
	 * Adresse IP de diffusion (streaming) :  broadcast
	 */
	private String ipStreaming;
	
	/**
	 * Port de diffusion (streaming)
	 */
	private int portStreaming;
	
	/**
	 * Port du serveur XML
	 */
	@SuppressWarnings("unused")
	private int portXML;
	
	
	/**
	 * Utilisateurs connectés aux système
	 */
	private Hashtable utilisateurs = new Hashtable();
	
	/**
	 * Canaux de diffusion
	 */
	//private Hashtable canaux = new Hashtable();
	
	/**
	 * Documents
	 */
	//private Hashtable documents = new Hashtable();
	
	/**
	 * Programmes
	 */
	//private Hashtable programmes = new Hashtable();
	
	/**
	 * Contrats
	 */
	//private Hashtable contrats = new Hashtable();
	
	/**
	 * Contractants
	 */
	//private Hashtable contractants = new Hashtable();
	
	/**
	 * Connexion à la base de données
	 */
	private Jdbc base;
	
	/**
	 * Préférences
	 */
	private Preferences prefs;
	
	/**
	 * Connexion à l'annuaire LDAP
	 */
	private Ldap ldap;

	/**
	 * Adresse du serveur SMTP
	 */
	private String host;
	
	/**
	 * Port du serveur SMTP
	 */
	private String port;
	
	/**
	 * Adresse mail envoyeur
	 */
	private String from;
	
// CONSTRUCTEUR
//************************************************************
	
	/**
	 * Constructeur
	 * @throws SQLException 
	 */
	public Systeme(Preferences prefs) throws SQLException {
		
		this.prefs = prefs;
		this.ipStreaming = prefs.node("streaming").get("ip", null);
		this.portStreaming = Integer.parseInt(prefs.node("streaming").get("port", null));
		
		//On initialise la connection à la base
		String driverjdbc = prefs.node("jdbc").get("driver", null);
		String typejdbc = prefs.node("jdbc").get("type", null);
		String ipjdbc = prefs.node("jdbc").get("ip", null);
		String portjdbc = prefs.node("jdbc").get("port", null);
		String baseNamejdbc = prefs.node("jdbc").get("base", null);
		String loginjdbc = prefs.node("jdbc").get("login", null);
		String pwdjdbc = prefs.node("jdbc").get("pwd", null);
		
		String urljdbc = "jdbc:"+typejdbc+"://"+ipjdbc+":"+portjdbc+"/"+baseNamejdbc;
		
		System.err.println("BD Driver: "+driverjdbc);
		System.err.println("BD URL: "+urljdbc);
		System.err.println("BD Login: "+loginjdbc);
		System.err.println("BD Pwd: "+pwdjdbc);
		
		base = Jdbc.getInstance();
		base.openDB(driverjdbc, urljdbc, loginjdbc, pwdjdbc);
		
		//On initialise la connection à l'annuaire LDAP
		String driverldap = prefs.node("ldap").get("driver", null);
		String typeldap = prefs.node("ldap").get("type", null);
		String ipldap = prefs.node("ldap").get("ip", null);
		String portldap = prefs.node("ldap").get("port", null);
		String baseNameldap = prefs.node("ldap").get("base", null);
		String loginldap = prefs.node("ldap").get("login", null);
		String pwdldap = prefs.node("ldap").get("pwd", null);
		@SuppressWarnings("unused") String role = prefs.node("ldap").get("role", null);
		
		String urlldap = typeldap + "://" + ipldap + ":" + portldap + "/" + baseNameldap;
		
		System.err.println("LDAP Driver: " + driverldap);
		System.err.println("LDAP URL: " + urlldap);
		System.err.println("LDAP Login: " + loginldap);
		System.err.println("LDAP Pwd: " + pwdldap);
		
		//ldap = Ldap.getInstance();
		//ldap.openLdap(driverldap, urlldap, authldap, loginldap, pwdldap, role, baseNameldap);
	
		//Récupération des information du serveur SMTP
		this.host = prefs.node("smtp").get("host", null);
		this.port = prefs.node("smtp").get("port", null);
		this.from = prefs.node("smtp").get("from", null);
		
		System.err.println("Adresse du serveur SMTP : " + host);
		System.err.println("Port du serveur SMTP : " + port);
		System.err.println("Mail envoyeur depuis le serveur SMTP : " + from);
		
		//On initialise les listes de canaux, programmes, documents
		/*this.canaux = */CanalFactory.getAll();
		/*this.programmes = */ProgrammeFactory.getAll();
		/*this.documents = */DocumentFactory.getAll();
		/*this.contractants = */ContractantFactory.getAll();
		/*this.contrats = */ContratFactory.getAll();
	}
	
// METHODES DU SYSTEME
//************************************************************
	
	//XMLRPC
	//*******************************************************
	
	/**
	 * Signale au client que le seveur est bien connecté
	 * @param String ip
	 * @return String
	 */
	public String testConnectXML(String ip) {
		System.out.println("Test de connexion envoyé par client XML "+ip);
		return Boolean.toString(true);
	}
	
	// UTILISATEUR
	//*******************************************************
	
	/**
	 * Vérifie que l'utilisateur à la permission requise
	 * @param String login
	 * @param String perm
	 * @return boolean
	 */
	private boolean verifPermission(String login, String perm) {
		
		//On récupère l'utilisateur
		Utilisateur u = (Utilisateur)utilisateurs.get(login);
		
		//On retourne le résultat
		return u.verifPermission(perm);
	}
	
	/**
	 * Connexion d'un utilisateur
	 * @param login
	 * @param pwd
	 * @return String
	 * @throws NamingException 
	 */
	@SuppressWarnings("unchecked")
	public String connexion(String loginldap, String pwd) throws NamingException {
		
		System.out.println("Connexion de l'utilisateur "+ loginldap);
		
		//On vérifie que l'utilisateur n'est pas déjà connecté
		if (!utilisateurs.containsKey(loginldap)) {

			//On initialise la connection à l'annuaire LDAP
			String driverldap = prefs.node("ldap").get("driver", null);
			String typeldap = prefs.node("ldap").get("type", null);
			String ipldap = prefs.node("ldap").get("ip", null);
			String portldap = prefs.node("ldap").get("port", null);
			String baseNameldap = prefs.node("ldap").get("base", null);
			String authldap = prefs.node("ldap").get("auth", null);
				//String loginldap = prefs.node("ldap").get("login", null);
				//String pwdldap = prefs.node("ldap").get("pwd", null);
				//String role = prefs.node("ldap").get("role", null);
				
			String urlldap = typeldap + "://" + ipldap + ":" + portldap + "/" + baseNameldap;
				
			System.err.println("LDAP Driver: " + driverldap);
			System.err.println("LDAP URL: " + urlldap);
			System.err.println("LDAP Login: " + loginldap);
			System.err.println("LDAP Pwd: " + pwd);
				
			ldap = Ldap.getInstance();
			ldap.openLdap(driverldap, urlldap, authldap, loginldap, pwd, baseNameldap);
				
			if (Utilisateur.verifierPwd(loginldap, pwd)) {
				Utilisateur util = Utilisateur.getByLogin(loginldap);
				
				//On connecte l'utilisateur
				util.connexion();
				
				//On l'ajoute à la liste des utilisateurs connectés au système
				utilisateurs.put(loginldap, util);
				
				System.out.println("Utilisateur "+loginldap+" connecté");
				return Boolean.toString(true);
			} else {
			
				//Sinon, connexion refusée
				System.out.println("Utilisateur "+loginldap+" non connecté");
				return Boolean.toString(false);
			}
		} else {
		
			//Sinon, connexion refusée
			System.out.println("Utilisateur "+loginldap+" déjà connecté");
			return Boolean.toString(false);
		
		}
	}
	
	/**
	 * Deconnexion d'un utilisateur
	 * @param login
	 * @return String
	 */
	public String deconnexion(String login) {
		
		System.out.println("Deconnexion de l'utilisateur "+login);
		
		//S'il y a des utilisateurs connectés
		if (utilisateurs.size()>0) {
			
			//On recherche l'utilisateur en question parmi cette liste
			if (utilisateurs.containsKey(login)) {
				
				//On récupère l'objet utilisateur
				Utilisateur util = (Utilisateur)utilisateurs.get(login);
				
				//On le déconnecte
				util.deconnexion();
				
				//On le supprime de la liste des utilisateurs connectés au système
				utilisateurs.remove(login);
				
				System.out.println("Utilisateur "+login+" deconnecté");
				return Boolean.toString(true);
			}
			System.out.println("Utilisateur "+login+" introuvable");
			return Boolean.toString(false);
		}
		System.out.println("Aucun utilisateur n'est connecté");
		return Boolean.toString(false);
	}

	/**
	 * Recherche un utilisateur à partir de son login
	 * @param login
	 * @return Utilisateur
	 * @throws NamingException 
	 */
	public Utilisateur chercherUtilisateur(String login) throws NamingException {
		
		//On vérifie que le couple login existe
		if (Utilisateur.verifierLogin(login)) return Utilisateur.getByLogin(login);

		return null;
	}

	/**
	 * Inscription d'un utilisateur
	 * @param String login
	 * @param String log
	 * @param String pass
	 * @param String role
	 * @param String email
	 * @param String nom
	 * @param String prenom
	 * @param String pays
	 * @return String
	 * @throws MessagingException 
	 * @throws AddressException 
	 */
	@SuppressWarnings("unchecked")
	public String inscription(String login, String log, String pass, String role, String email, String nom, String prenom, String pays) throws NamingException, AddressException, MessagingException {
		
		System.out.println("Inscription de l'utilisateur "+log);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "inscription")) {
		
			//On vérifie que le login n'existe pas
			if (!Utilisateur.verifierLogin(log)) {
				
				//On crée l'utilisateur
				Utilisateur u = Utilisateur.create(log, pass, nom, prenom, email, pays, role);
				
				//Si l'utilisateur a bien été créé
				if (u != null) {
					//On l'ajoute à la liste des utilisateurs du système
					utilisateurs.put(u.getLogin(), u);
					
					System.out.println("Utilisateur '"+log+"' créé");
					new EnvoiMail(host, port, from, log, nom, prenom, email, pass, pays);
					return Boolean.toString(true);
				}
				
				//Sinon, création a échoué
				System.out.println("Utilisateur '"+log+"' non créé");
				return Boolean.toString(false);
			}
			
			//Sinon, création refusée
			System.out.println("Utilisateur '"+log+"' déjà existant. Inscription annulée");
			return Boolean.toString(false);
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Inscription annulée");
		return Boolean.toString(false);
	}

	/**
	 * Suppression d'un compte utilisateur
	 * @param Login
	 * @throws NamingException
	 */
	public String supprimerUtilisateur(String Login) throws NamingException {
		
		System.out.println("Suppression de l'utilisateur "+Login);
		
		//Vérification de l'existance du login à supprimer et suppression 
		if (Utilisateur.verifierLogin(Login)) {
			Utilisateur.deleteByLogin(Login);
			
			return Boolean.toString(true);
		} else {
		//Sinon annulation
			return Boolean.toString(false);
		}
	}

	/**
	 * Récupération de l'objet à modifier
	 * @param login
	 * @return Vector
	 * @throws NamingException
	 */
	@SuppressWarnings("unchecked")
	public Vector rechercherUtilisateur(String login) throws NamingException {
		System.out.println("Recherche des attributs de l'utilisateur : " + login);

		//On vérifie que l'utilisateur a la permission
			if (Utilisateur.verifierLogin(login)) {
				Utilisateur u = Utilisateur.getByLogin(login);
			
				//Vecteur d'attributs à retourner
				Vector vUtilisateur = new Vector();

				vUtilisateur.addElement(u.getLogin());
				vUtilisateur.addElement(u.getPwd());
				vUtilisateur.addElement(u.getNom());
				vUtilisateur.addElement(u.getPrenom());
				vUtilisateur.addElement(u.getMail());
				vUtilisateur.addElement(u.getPays());
				vUtilisateur.addElement(u.getRole());
				
				return vUtilisateur;
			} else {
				// Sinon, création refusée
				System.out.println("Permission non accordée. Recherche non effectuées");
				return null;
			}
	}

	/**
	 * Modification des attributs d'un utilisateur
	 * @param Login
	 * @param newLogin
	 * @param nom
	 * @param prenom
	 * @param mail
	 * @param pays
	 * @throws NamingException
	 */
	 public String modifierUtilisateur(String login, String newlogin, String pwd, String nom, String prenom, String mail, String pays) throws NamingException {

		//On vérifie que l'utilisateur a la permission
		if (Utilisateur.verifierLogin(login)) {
			
			//On récupère les attributs
			Utilisateur u = Utilisateur.getByLogin(login);
			String role = u.getRole();
			
			//vérification du nouveau login complété
			if (newlogin.length() == 0) {
				newlogin = u.getLogin();
			}
			
			//vérification du nouveau nom complété
			if (nom.length() == 0) {
				nom = u.getNom();
			}
			
			//vérification du nouveau prénom complété
			if (prenom.length() == 0 ) {
				prenom = u.getPrenom();
			}
			
			//vérification du nouveau mail complété
			if (mail.length() == 0) {
				mail = u.getMail();
			}
			
			//vérification du nouveau pays complété
			if (pays.length() == 0) {
				pays = u.getPays();
			}
			
			//vérification du nouveau password complété
			if (pwd.length() == 0) {
				pwd = u.getPwd();
			}
			
			//On modifie les attributs
			u.modifierInfos(login, role, newlogin, pwd, nom, prenom, mail, pays);
			System.out.println("Utilisateur '"+login+"' modifié");
			return Boolean.toString(true);
		} else {
		// Sinon, création refusée
		System.out.println("Permission non accordée. Utilisateur non modifié");
		return Boolean.toString(false);
		}
	}
	
	/**
	 * Changement de role d'un utilisateur
	 */
	
	public void changerPermission(){
		// your code here
	}
	 
	 
	 
	public void suppressionUtilisateur() {
		// your code here
	}

	public void rechercheUtilisateur() {
		// your code here
	}

	public void rechercher(String Login) {
		// your code here
	}

	public void selectionnerUtilisateur(String Login) {
		// your code here
	}
	
	public boolean emailValide(String Email) {
		// your code here
		return false;
	}
	
	public void getListePermissions() {
		// your code here
	}

	// DOCUMENT
	//*******************************************************
	
	/**
	 * Création d'un document
	 * @param String login
	 * @param String titre
	 * @param String duree
	 * @param String jour
	 * @param String mois
	 * @param String annee
	 * @param String source
	 * @param String langue
	 * @param String genre
	 * @param String fichier
	 * @param String artiste
	 * @param String interprete
	 * @param String compositeur
	 * @return String
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("unchecked")
	public String creerDocument(String login, String titre, String duree, String jour, String mois, String annee,
			String source, String langue, String genre, String fichier, String artiste, String interprete, String compositeur)throws NumberFormatException, SQLException, IOException, InterruptedException {

		System.out.println("Création du document "+titre);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "creerDocument")) {
		
			//On vérifie que le document n'existe pas
			if (DocumentFactory.getByTitre(titre) == null) {
				
				//On crée le document
				Document d = DocumentFactory.create(titre, Integer.parseInt(duree), Integer.parseInt(jour), Integer.parseInt(mois), Integer.parseInt(annee), source, langue, genre, fichier, artiste, interprete, compositeur);
				
				//Si le document a bien été créé
				if (d != null) {
					//On l'ajoute à la liste des documents du système
					//documents.put(d.getId(), d);
					
					//Conversion du fichier audio en .wav
					String mplayer = prefs.node("conversion").get("mplayer", null);
					String chemin = prefs.node("conversion").get("chemin", null);
					
					String fichierResult = chemin + d.getId() + ".wav";
					String fichierInit = d.getFichier();
					//Commande permettant la conversion
					String commande = mplayer + " -dumpaudio -dumpfile " + fichierResult + " " + fichierInit;
					System.err.println(commande);
					//Execution de la conversion
					@SuppressWarnings("unused")
					Process proc = Runtime.getRuntime().exec(commande);
					proc.waitFor();
					
					//MàJ de l'objet document et de la base
					d.setFichier(fichierResult);

					//Suppression du fichier initial
					File f = new File(fichierInit);
					f.delete(); 
					
					System.out.println("Document '"+titre+"' créé");
					return Boolean.toString(true);
				}
				
				//Sinon, création a échoué
				System.out.println("Document '"+titre+"' non créé");
				return Boolean.toString(false);
			}
			
			//Sinon, création refusée
			System.out.println("Document '"+titre+"' non créé");
			return Boolean.toString(false);
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Document non créé");
		return Boolean.toString(false);
	}
	
	/**
	 * Lister les documents disponibles
	 * @param String login
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerDocuments(String login) {

		System.out.println("Liste des documents disponibles");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "listerDocuments")) {
		
			//On crée le vecteur
			Vector vDocuments = new Vector();
			
			//On récupère la liste des documents
			Enumeration listeDocs = DocumentFactory.getInstances().elements();
			Document d;
			
			while (listeDocs.hasMoreElements()) {
				d = (Document)listeDocs.nextElement();
				vDocuments.addElement(d.getAttributesDictionary());
			}
			
			return vDocuments;
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Documents non listés");
		return null;
	}
	
	/**
	 * Recherche d'un document
	 * @param String login
	 * @param String id
	 * @param String titre
	 * @param String duree
	 * @param String jour
	 * @param String mois
	 * @param String annee
	 * @param String source
	 * @param String langue
	 * @param String genre
	 * @param String fichier
	 * @param String artiste
	 * @param String interprete
	 * @param Styring compositeur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector rechercherDocument(String login, String id, String titre, String duree, String jour, String mois, String annee, String source, String langue, String genre, String fichier, String artiste, String interprete, String compositeur) {

		System.out.println("Recherche d'un document");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "rechercherDocument")) {
			
			//Vecteur de documents à retourner
			Vector vDocuments = new Vector();
		
			//On récupère la liste des documents
			Enumeration listeDocs = DocumentFactory.getInstances().elements();
			Document d;
			
			while (listeDocs.hasMoreElements()) {
				d = (Document)listeDocs.nextElement();
				
				if ((id.length()>0 && d.getId().contains(id))
					|| (titre.length()>0 && d.getTitre().contains(titre))
					|| (genre.length()>0 && d.getGenre().contains(genre))
					|| (fichier.length()>0 && d.getFichier().contains(fichier))
					|| (source.length()>0 && d.getSource().contains(source))
					|| (langue.length()>0 && d.getLangue().contains(langue))
					|| (duree.length()>0 && Integer.toString(d.getDuree()).contains(duree))
					|| (artiste.length()>0 && d.getArtiste().contains(artiste))
					|| (interprete.length()>0 && d.getInterprete().contains(interprete))
					|| (compositeur.length()>0 && d.getCompositeur().contains(compositeur))) {
					
					vDocuments.addElement(d.getAttributesDictionary());
				}
			}
			
			return vDocuments;
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Recherche non effectuées");
		return null;
	}
	
	/**
	 * Suppression d'un document
	 * @param String login
	 * @param String id
	 * @return String
	 */
	public String supprimerDocument(String login, String id) {
		System.out.println("Suppression du document "+id);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "supprimerDocument")) {
		
			//On vérifie que le document existe
			if (DocumentFactory.containsId(id)) {
				
				//On récupère l'objet document
				Document d = (Document)DocumentFactory.getById(id);

				//Si le document a bien été supprimé
				if (d.supprimer()) {
					
					//On le retire de la liste des documents du système
					//documents.remove(id);
					File f = new File(d.getFichier());
					f.delete(); 
					
					System.out.println("Document '"+id+"' supprimé");
					return Boolean.toString(true);
				}
				
				//Sinon, suppression échouée
				System.out.println("Document '"+id+"' non supprimé");
				return Boolean.toString(false);
			}
			
			//Sinon, document introuvable
			System.out.println("Document '"+id+"' introuvable");
			return Boolean.toString(false);
		}
		// Sinon, suppression refusée
		System.out.println("Permission non accordée. Document non supprimé");
		return Boolean.toString(false);
	}
	
	/**
	 * Modification d'un document
	 * @param String login
	 * @param String id
	 * @param String titre
	 * @param String duree
	 * @param String jour
	 * @param String mois
	 * @param String annee
	 * @param String source
	 * @param String langue
	 * @param String genre
	 * @param String fichier
	 * @param String artiste
	 * @param String interprete
	 * @param String compositeur
	 * @return String
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	public String modifierDocument(String login, String id, String titre, String duree, String jour, String mois, String annee, String source, String langue, String genre, String fichier, String artiste, String interprete, String compositeur) throws NumberFormatException, SQLException {

		System.out.println("Modification du document "+id);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "modifierDocument")) {
		
			//On vérifie que le document existe
			if (DocumentFactory.containsId(id)) {
				
				//On récupère le doc
				Document d = (Document)DocumentFactory.getById(id);
				
				//On modifie le document
				if (d.modifier(titre, Integer.parseInt(duree), Integer.parseInt(jour), Integer.parseInt(mois), Integer.parseInt(annee), source, langue, genre, fichier, artiste, interprete, compositeur)) {
				
					System.out.println("Document '"+id+"' modifié");
					return Boolean.toString(true);
				}
				
				//Sinon, création a échoué
				System.out.println("Document '"+id+"' non modifié");
				return Boolean.toString(false);
			}
			
			//Sinon, création refusée
			System.out.println("Document '"+id+"' non trouvé");
			return Boolean.toString(false);
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Document non modifié");
		return Boolean.toString(false);
	}
	
	/**
     * Recherche du mot de passe d'un utilisateur
     * @param Login
     * @return String
     * @throws NamingException
     * @throws MessagingException
     * @throws AddressException
     */
    public String rechercherpwd(String login) throws NamingException, AddressException, MessagingException {
        //On vérifie que l'utilisateur a la permission
        if (Utilisateur.verifierLogin(login)) {
           
            //On récupère les attributs
            Utilisateur u = Utilisateur.getByLogin(login);
           
            String nom = u.getNom();
            String prenom = u.getPrenom();
            String email = u.getMail();
            String pass = u.getPwd();
            String pays = u.getPays();
                   
            new EnvoiMail(host, port, from, login, nom, prenom, email, pass, pays);
            return Boolean.toString(true);
           
        } else {
            // Sinon, création refusée
            System.out.println("Permission non accordée. Mail non envoyé");
            return Boolean.toString(false);
            }
    }
	
	/**
	 * Informations sur un document
	 * @param String login
	 * @param Strign id
	 * @return Dictionary
	 */
	public Dictionary infoDocument(String login, String id) {
		
		System.out.println("Infos sur un document");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "infoDocument")) {
		
			//On vérifie que le document existe
			if (DocumentFactory.containsId(id)) {
				Document d = (Document)DocumentFactory.getById(id);
				return d.getAttributesDictionary();
			}
			else {
				System.out.println("Document indisponible");
				return null;
			}
		}
		// Sinon, refusé
		System.out.println("Permission non accordée. Document non affiché");
		return null;
	}

	public void ajouterDocument() {
		// your code here
	}

	public boolean documentExistant() {
		// your code here
		return false;
	}

	public void selectionnerDocument(String Id_Doc) {
		// your code here
	}

	
	// PROGRAMME
	//*******************************************************

	/**
	 * Création d'un programme
	 * @param String login
	 * @param String titre
	 * @param String thematique
	 * @return String
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public String creerProgramme(String login, String titre, String thematique) throws SQLException {

		System.out.println("Création du programme "+titre);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "creerProgramme")) {
		
			//On vérifie que le programme n'existe pas
			if (ProgrammeFactory.getByTitre(titre) == null) {
				
				//On crée le programme
				@SuppressWarnings("unused")
				Programme p = ProgrammeFactory.create(titre, thematique);
				
				//On l'ajoute à la liste des programmes du système
				//programmes.put(p.getId(), p);
				
				System.out.println("Programme '"+titre+"' créé");
				return Boolean.toString(true);
			}
			
			//Sinon, création refusée
			System.out.println("Programme '"+titre+"' non créé");
			return Boolean.toString(false);
		}
		//Sinon, création refusée
		System.out.println("Permission non accordée. Programme non créé");
		return Boolean.toString(false);
	}
	
	/**
	 * Ajoute un document dans un programme
	 * @param String login
	 * @param String idDoc
	 * @param String idProg
	 * @return String
	 * @throws SQLException 
	 */
	public String ajouterDocumentProgramme(String login, String idDoc, String idProg) throws SQLException {
		System.out.println("Ajout du document "+idDoc+" au programme "+idProg);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "ajouterDocumentProgramme")) {
		
			//On vérifie que le programme et le document existent
			if (ProgrammeFactory.containsId(idProg) && DocumentFactory.containsId(idDoc)) {
				
				//On récupère les objets
				Document d = (Document)DocumentFactory.getById(idDoc);
				Programme p = (Programme)ProgrammeFactory.getById(idProg);
				
				//On ajoute le document au programme
				p.ajouterDocument(d);
				
				System.out.println("Document "+idDoc+" ajouté au programme "+idProg);
				return Boolean.toString(true);
				
			}
			
			//Sinon, ajout refusé
			System.out.println("Le document "+idDoc+" n'a pas été ajouté au programme "+idProg);
			return Boolean.toString(false);
		}
		//Sinon, ajout refusé
		System.out.println("Permission non accordée. Le document non programmé");
		return Boolean.toString(false);
	}
	
	/**
	 * Retirer un document d'un programme
	 * @param String login
	 * @param String idDoc
	 * @param String idProg
	 * @return String
	 * @throws SQLException 
	 */
	public String retirerDocumentProgramme(String login, String idProg, String calage) throws SQLException {
		System.out.println("Retirer du programme "+idProg+ " le document calé à "+calage);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "retirerDocumentProgramme")) {
		
			//On vérifie que le programme
			if (ProgrammeFactory.containsId(idProg)) {
				
				//On récupère les objets
				Programme p = (Programme)ProgrammeFactory.getById(idProg);
				
				//On ajoute le document au programme
				p.retirerDocument(calage);
				
				System.out.println("Document retiré du programme "+idProg);
				return Boolean.toString(true);
				
			}
			
			//Sinon, ajout refusé
			System.out.println("Le document n'a pas été retiré au programme "+idProg);
			return Boolean.toString(false);
		}
		//Sinon, ajout refusé
		System.out.println("Permission non accordée. Le document non retiré");
		return Boolean.toString(false);
	}
	
	/**
	 * Lister les programmes disponibles
	 * @param String login
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerProgrammes(String login) {

		System.out.println("Liste des programmes disponibles");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "listerProgrammes")) {
		
			//On crée le vecteur
			Vector vProgrammes = new Vector();
			
			//On récupère la liste des documents
			Enumeration listeProgrammes = ProgrammeFactory.getInstances().elements();
			Programme p = null;
			
			while (listeProgrammes.hasMoreElements()) {
				p = (Programme)listeProgrammes.nextElement();
				vProgrammes.addElement(p.getAttributesDictionary());
			}
			
			return vProgrammes;
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Programmes non listés");
		return null;
	}
	
	/**
	 * Recherche d'un programme
	 * @param String login
	 * @param String id
	 * @param String titre
	 * @param String thematique
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector rechercherProgramme(String login, String id, String titre, String thematique) {

		System.out.println("Recherche d'un programme");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "rechercherProgramme")) {
			
			//Vecteur de programmes à retourner
			Vector vProgrammes = new Vector();
		
			//On récupère la liste des programmes
			Enumeration listeProgs = ProgrammeFactory.getInstances().elements();
			Programme p;
			
			while (listeProgs.hasMoreElements()) {
				p = (Programme)listeProgs.nextElement();
				
				if ((id.length()>0 && p.getId().contains(id))
					|| (titre.length()>0 && p.getTitre().contains(titre))
					|| (thematique.length()>0 && p.getThematique().contains(thematique))) {
					
					vProgrammes.addElement(p.getAttributesDictionary());
				}
			}
			
			return vProgrammes;
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Recherche non effectuées");
		return null;
	}

	/**
	 * Suppression d'un programme
	 * @param String login
	 * @param String id
	 * @return String
	 */
	public String supprimerProgramme(String login, String id) {
		System.out.println("Suppression du programme "+id);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "supprimerProgramme")) {
		
			//On vérifie que le programme existe
			if (ProgrammeFactory.containsId(id)) {
				
				//On récupère l'objet document
				Programme p = (Programme)ProgrammeFactory.getById(id);

				//On le supprime. Si succés...
				if (p.supprimer()) {
					
					//On le retire de la liste des programmes du système
					//programmes.remove(id);
					
					System.out.println("Programme '"+id+"' supprimé");
					return Boolean.toString(true);
				}
				
				//Sinon, suppression échouée
				System.out.println("Programme '"+id+"' non supprimé");
				return Boolean.toString(false);
			}
			
			//Sinon, programme introuvable
			System.out.println("Programme '"+id+"' introuvable");
			return Boolean.toString(false);
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Programme non supprimé");
		return Boolean.toString(false);
	}
	
	/**
	 * Modification d'un programme
	 * @param String login
	 * @param String id
	 * @param String titre
	 * @param String thematique
	 * @return String
	 */
	public String modifierProgramme(String login, String id, String titre, String thematique) throws NumberFormatException, SQLException {

		System.out.println("Modification du programme "+id);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "modifierProgramme")) {
		
			//On vérifie que le programme existe
			if (ProgrammeFactory.containsId(id)) {
				
				//On récupère le prog
				Programme p = (Programme)ProgrammeFactory.getById(id);
				
				//On modifie le programme
				if (p.modifier(titre, thematique)) {
				
					System.out.println("Programme '"+id+"' modifié");
					return Boolean.toString(true);
				}
				
				//Sinon, création a échoué
				System.out.println("Programme '"+id+"' non modifié");
				return Boolean.toString(false);
			}
			
			//Sinon, création refusée
			System.out.println("Programme '"+id+"' non trouvé");
			return Boolean.toString(false);
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Programme non modifié");
		return Boolean.toString(false);
	}
	
	/**
	 * Informations sur un programme
	 * @param String login
	 * @param Strign id
	 * @return Dictionary
	 */
	public Dictionary infoProgramme(String login, String id) {
		
		System.out.println("Infos sur un programme");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "infoProgramme")) {
		
			//On vérifie que le programme existe
			if (ProgrammeFactory.containsId(id)) {
				Programme p = (Programme)ProgrammeFactory.getById(id);
				return p.getAttributesDictionary();
			}
			else {
				System.out.println("Programme indisponible");
				return null;
			}
		}
		
		// Sinon, refusé
		System.out.println("Permission non accordée. Programme non affiché");
		return null;
	}

	public void ConfirmerSupprimerProgramme(Boolean SuppressionProgramme) {
		// your code here
	}

	public void SaisirInfosProgramme(String Id_Prog, String Titre) {
		// your code here
	}

	public void AjouterProgrammeArchive(String Id_Prog) {
		// your code here
	}
	
	public void VerifierinfosProgramme(String Id_Prog, String Titre,
			String Thematique) {
		// your code here
	}
	
	// CANAL
	//*******************************************************

	/**
	 * Création d'un canal
	 * @param String login
	 * @param String nom
	 * @param String utilMax
	 * @return String
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	@SuppressWarnings("unchecked")
	public String creerCanal(String login, String nom, String utilMax) throws NumberFormatException, SQLException {

		System.out.println("Création du canal "+nom);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "creerCanal")) {
		
			//On vérifie que le canal n'existe pas
			if (CanalFactory.getByNom(nom) == null) {
				
				//On crée le canal
				@SuppressWarnings("unused")
				Canal c = CanalFactory.create(nom, Integer.parseInt(utilMax));
				
				//On l'ajoute à la liste des canaux du système
				//canaux.put(c.getId(), c);
				
				System.out.println("Canal '"+nom+"' créé");
				return Boolean.toString(true);
				
			}
			
			//Sinon, création refusée
			System.out.println("Canal '"+nom+"' non créé");
			return Boolean.toString(false);
		}
		//Sinon, création refusée
		System.out.println("Permission non accordée. Canal non créé");
		return Boolean.toString(false);
	}
	
	/**
	 * Planifier un programme sur un canal
	 * @param String login
	 * @param String idProg
	 * @param String idCanal
	 * @param String jour
	 * @param String mois
	 * @param String annee
	 * @param String heure
	 * @param String minute
	 * @param String seconde 
	 * @return String
	 */
	public String planifierProgramme(String login, String idProg, String idCanal, String jour, String mois, String annee, String heure, String minute, String seconde) {

		System.out.println("Planification du programme "+idProg+" sur le canal "+idCanal);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "planifierCanal")) {
		
			//On vérifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal) && ProgrammeFactory.containsId(idProg)) {
				
				//On récupère les objets
				Canal c = (Canal)CanalFactory.getById(idCanal);
				Programme p = (Programme)ProgrammeFactory.getById(idProg);
				
				//On planifie le programme
				if (c.planifierProgramme(p, Integer.parseInt(jour), Integer.parseInt(mois), Integer.parseInt(annee), Integer.parseInt(heure), Integer.parseInt(minute), Integer.parseInt(seconde))) {
				
					System.out.println("Programme "+idProg+" planifié sur le canal "+idCanal);
					return Boolean.toString(true);
				}
				else {
					System.out.println("Programme "+idProg+" non planifié sur le canal "+idCanal);
					return Boolean.toString(false);
				}
				
			}
			
			//Sinon, ajout refusé
			System.out.println("Le programme "+idProg+" n'a pas été plnifié sur le canal "+idCanal);
			return Boolean.toString(false);
		}
		
		//Sinon, ajout refusé
		System.out.println("Permission non accordée. Programme non planifié");
		return Boolean.toString(false);
	}
	
	/**
	 * Déplanifier un programme sur un canal
	 * @param String login
	 * @param String idCanal
	 * @param String calage
	 * @return String
	 */
	public String deplanifierProgramme(String login, String idCanal, String calage) {

		System.out.println("Déplanification d'un programme sur le canal "+idCanal);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "deplanifierCanal")) {
		
			//On vérifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal)) {
				
				//On récupère les objets
				Canal c = (Canal)CanalFactory.getById(idCanal);
				
				//On deplanifie le programme
				if (c.annulerPlanification(Long.parseLong(calage))) {
				
					System.out.println("Programme déplanifié du canal "+idCanal);
					return Boolean.toString(true);
				}
				else {
					System.out.println("Programme non déplanifié du le canal "+idCanal);
					return Boolean.toString(false);
				}
				
			}
			
			//Sinon, ajout refusé
			System.out.println("Le programme n'a pas été déplanifié sur le canal "+idCanal);
			return Boolean.toString(false);
		}
		
		//Sinon, ajout refusé
		System.out.println("Permission non accordée. Programme non déplanifié");
		return Boolean.toString(false);
	}
	
	/**
	 * Diffuser un programme sur un canal
	 * @param String login
	 * @param String idProg
	 * @param String idCanal
	 * @return String
	 */
	public String diffuserProgramme(String login, String idProg, String idCanal) {
		
		System.out.println("Diffusion du programme "+idProg+" sur le canal "+idCanal);
		
		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "diffuserProgramme")) {
		
			//On vérifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal) && ProgrammeFactory.containsId(idProg)) {
				
				//On récupère les objets
				Canal c = (Canal)CanalFactory.getById(idCanal);
				Programme p = (Programme)ProgrammeFactory.getById(idProg);
				
				//On diffuse le programme
				if (!c.isRTPstarted()) c.createRTPServer(ipStreaming, portStreaming++, prefs.node("streaming").get("publicite", null));
				c.diffuserProgramme(p);
				
				System.out.println("Programme "+idProg+" en diffusion sur le canal "+idCanal);
				return Boolean.toString(true);
				
			}
			
			//Sinon, ajout refusé
			System.out.println("La diffusion du programme "+idProg+" n'a pas été lancée sur le canal "+idCanal);
			return Boolean.toString(false);
		}
		
		//Sinon, ajout refusé
		System.out.println("Permission non accordée. Diffusion non lancée");
		return Boolean.toString(false);
	}
	
	/**
	 * Ecouter un canal (retourne l'URL du canal)
	 * @param String login
	 * @param String idCanal
	 * @return String
	 */
	public String ecouterCanal(String login, String idCanal) {
		System.out.println("Ecoute du canal "+idCanal);
		
		//	On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "ecouterCanal")) {
		
			//On vérifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal)) {
				
				//On récupère l'objet canal
				Canal c = (Canal)CanalFactory.getById(idCanal);
				System.out.println("Construction de l'url du canal "+idCanal);
				String url = c.getUrlStreaming();
				System.out.println("URL: "+url);
				return url;
			}
			
			//Sinon, ajout refusé
			System.out.println("Canal "+idCanal+" inconnu");
			return null;
		}
		
		//Sinon, ajout refusé
		System.out.println("Permission non accordée.");
		return null;
	}
	
	/**
	 * Lancer la diffusion d'un canal
	 * @param String login
	 * @param String idCanal
	 * @return String
	 */
	public String startCanal(String login, String idCanal) {
		System.out.println("Ecoute du canal "+idCanal);
		
		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "ecouterCanal")) {
		
			//On vérifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal)) {
				
				//On récupère l'objet canal
				Canal c = (Canal)CanalFactory.getById(idCanal);
				if (!c.isRTPstarted()) c.createRTPServer(ipStreaming, portStreaming++, prefs.node("streaming").get("publicite", null));
				c.startDiffusion();
				return Boolean.toString(true);
			}
			
			//Sinon, ajout refusé
			System.out.println("Canal "+idCanal+" inconnu");
			return Boolean.toString(false);
		}
		
		//Sinon, ajout refusé
		System.out.println("Permission non accordée. Canal non démarré");
		return Boolean.toString(false);
			
	}
	
	/**
	 * Stoppe la diffusion d'un canal
	 * @param String login
	 * @param String idCanal
	 * @return String
	 */
	public String stopCanal(String login, String idCanal) {
		System.out.println("Stopper le canal "+idCanal);
		
		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "ecouterCanal")) {
		
			//On vérifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal)) {
				
				//On récupère l'objet canal
				Canal c = (Canal)CanalFactory.getById(idCanal);
				c.stopDiffusion();
				return Boolean.toString(true);
			}
			
			//Sinon, ajout refusé
			System.out.println("Canal "+idCanal+" inconnu");
			return Boolean.toString(false);
		}
		
		//Sinon, ajout refusé
		System.out.println("Permission non accordée. Canal non arrété");
		return Boolean.toString(false);
	}
	
	/**
	 * Lister les canaux disponibles
	 * @param String login
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerCanaux(String login) {

		System.out.println("Liste des canaux disponibles");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "listerCanaux")) {
		
			//On crée le vecteur
			Vector vCanaux = new Vector();
			
			//On récupère la liste des documents
			Enumeration listeCanaux = CanalFactory.getInstances().elements();
			Canal c;
			
			while (listeCanaux.hasMoreElements()) {
				c = (Canal)listeCanaux.nextElement();
				vCanaux.addElement(c.getAttributesDictionary());
			}
			
			return vCanaux;
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Canaux non listés");
		return null;
	}
	
	/**
	 * Recherche d'un canal
	 * @param String login
	 * @param String id
	 * @param String nom
	 * @param String utilMax
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector rechercherCanal(String login, String id, String nom, String utilMax) {

		System.out.println("Recherche d'un canal");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "rechercherCanal")) {
			
			//Vecteur de canaux à retourner
			Vector vCanaux = new Vector();
		
			//On récupère la liste des programmes
			Enumeration listeCanx = CanalFactory.getInstances().elements();
			Canal c;
			
			while (listeCanx.hasMoreElements()) {
				c = (Canal)listeCanx.nextElement();
				
				if ((id.length()>0 && c.getId().contains(id))
					|| (nom.length()>0 && c.getNom().contains(nom))) {
					
					vCanaux.addElement(c.getAttributesDictionary());
				}
			}
			
			return vCanaux;
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Recherche non effectuées");
		return null;
	}
	
	/**
	 * Suppression d'un canal
	 * @param String login
	 * @param String id
	 * @return String
	 */
	public String supprimerCanal(String login, String id) {
		System.out.println("Suppression du canal "+id);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "supprimerCanal")) {
		
			//On vérifie que le canal existe
			if (CanalFactory.containsId(id)) {
				
				//On récupère l'objet canal
				Canal c = (Canal)CanalFactory.getById(id);

				//Si le document a bien été supprimé
				if (c.supprimer()) {
					
					//On le retire de la liste des canaux du système
					//canaux.remove(id);
					
					System.out.println("Canal '"+id+"' supprimé");
					return Boolean.toString(true);
				}
				
				//Sinon, suppression échouée
				System.out.println("Canal '"+id+"' non supprimé");
				return Boolean.toString(false);
			}
			
			//Sinon, canal introuvable
			System.out.println("Canal '"+id+"' introuvable");
			return Boolean.toString(false);
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Canal non supprimé");
		return Boolean.toString(false);
	}
	
	/**
	 * Modification d'un canal
	 * @param String login
	 * @param String id
	 * @param String nom
	 * @param String utilMax
	 * @return String
	 */
	public String modifierCanal(String login, String id, String nom, String utilMax) throws NumberFormatException, SQLException {

		System.out.println("Modification du canal "+id);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "modifierCanal")) {
		
			//On vérifie que le canal existe
			if (CanalFactory.containsId(id)) {
				
				//On récupère le canal
				Canal c = (Canal)CanalFactory.getById(id);
				
				//On modifie le canal
				if (c.modifier(nom, Integer.valueOf(utilMax))) {
				
					System.out.println("Canal '"+id+"' modifié");
					return Boolean.toString(true);
				}
				
				//Sinon, création a échoué
				System.out.println("Canal '"+id+"' non modifié");
				return Boolean.toString(false);
			}
			
			//Sinon, création refusée
			System.out.println("Canal '"+id+"' non trouvé");
			return Boolean.toString(false);
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Canal non modifié");
		return Boolean.toString(false);
	}
	
	/**
	 * Informations sur un canal
	 * @param String login
	 * @param Strign id
	 * @return Dictionary
	 */
	public Dictionary infoCanal(String login, String id) {
		
		System.out.println("Infos sur un canal");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "infoCanal")) {
		
			//On vérifie que le canal existe
			if (CanalFactory.containsId(id)) {
				Canal c = (Canal)CanalFactory.getById(id);
				return c.getAttributesDictionary();
			}
			else {
				System.out.println("Canal indisponible");
				return null;
			}
		}
		// Sinon, refusé
		System.out.println("Permission non accordée. Canal non affiché");
		return null;
	}
	

	public void ValiderCreationCanal() {
		// your code here
	}

	public void PlannifierUnProgramme() {
		// your code here
	}

	public void ConfirmerSupprimerCanal(boolean SuppressionCanal) {
		// your code here
	}

	public void AnnulerSupprimerProgramme(boolean Suppression) {
		// your code here
	}

	public void ConfirmerCreationCanal(boolean Confirmation) {
		// your code here
	}

	public void AnnulerSupprimerCanal(boolean Supprimer) {
		// your code here
	}

	public void VerifierInfosCanal(String Id_Canal, String NomCanal,
			int Flux_Max) {
		// your code here
	}

	public void ValiderAjout(boolean Validation) {
		// your code here
	}

	public void ValiderModification(boolean Validation) {
		// your code here
	}

	public void RetirerDocument(String Id_Doc) {
		// your code here
	}

	public void RetirerProgramme(String Id_Prog) {
		// your code here
	}

	public void AjoutDocument() {
		// your code here
	}

	public void AjouterProgramme() {
		// your code here
	}

	public void insertion(String choix) {
		// your code here
	}

	public void ValiderCreerProgramme() {
		// your code here
	}

	public void AjoutProgramme(String Id_Prog) {
		// your code here
	}

	public void SupprimerUnProgramme() {
		// your code here
	}

	public void RetraitCanal() {
		// your code here
	}

	public void ChoixModification(String Choix) {
		// your code here
	}

	public void VerifierDuree(int Duree) {
		// your code here
	}

	public void Retirer(String Id) {
		// your code here
	}

	public void SupprimerUnCanal() {
		// your code here
	}

	public void retraitProgramme() {
		// your code here
	}

	public boolean Modifier() {
		// your code here
		return false;
	}

	public void PlanifierSurCanal(String Id_Canal) {
		// your code here
	}

	public void VerifierJourHeure(java.util.Date Jour, int Heure) {
		// your code here
	}

	public void ArretDiffusion(boolean Choix) {
		// your code here
	}

	public void AjouterDocumentProgramme() {
		// your code here
	}
	
// CONTRAT
//*******************************************************

	/**
	 * Création d'un contrat
	 * @param String login
	 * @param String jourSignature
	 * @param String moisSignature
	 * @param String anneeSignature
	 * @param String jourExpiration
	 * @param String moisExpiration
	 * @param String anneeExpiration
	 * @param String signataire
	 * @param String modeReglement
	 * @param String type
	 * @return String
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public String creerContrat(String login, String titre, String jourSignature, String moisSignature,
			String anneeSignature, String jourExpiration, String moisExpiration, String anneeExpiration,
			String signataire, String modeReglement, String type) throws SQLException {

		System.out.println("Création du contrat");
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "creerContrat")) {
		
			//On vérifie que le canal n'existe pas
			if (ContratFactory.getByTitre(titre) == null) {
				
				//On crée le canal
				@SuppressWarnings("unused")
				Contrat c = ContratFactory.create(titre, Integer.parseInt(jourSignature), Integer.parseInt(moisSignature),
						Integer.parseInt(anneeSignature), Integer.parseInt(jourExpiration), Integer.parseInt(moisExpiration),
						Integer.parseInt(anneeExpiration), signataire, modeReglement, type);
				
				//On l'ajoute à la liste des contrats du système
				//contrats.put(c.getId(), c);
				
				System.out.println("Contrat créé");
				return Boolean.toString(true);
				
			}
			
			//Sinon, création refusée
			System.out.println("Contrat non créé");
			return Boolean.toString(false);
		}
		//Sinon, création refusée
		System.out.println("Permission non accordée. Contrat non créé");
		return Boolean.toString(false);
	}
	
	/**
	 * Lister les contrats disponibles
	 * @param String login
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerContrats(String login) {

		System.out.println("Liste des contrats disponibles");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "listerContrats")) {
		
			//On crée le vecteur
			Vector vContrats = new Vector();
			
			//On récupère la liste des documents
			Enumeration listeContrats = ContratFactory.getInstances().elements();
			Contrat c;
			
			while (listeContrats.hasMoreElements()) {
				c = (Contrat)listeContrats.nextElement();
				vContrats.addElement(c.getAttributesDictionary());
			}
			
			return vContrats;
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Contrats non listés");
		return null;
	}
	
	/**
	 * Informations sur un contrat
	 * @param String login
	 * @param Strign id
	 * @return Dictionary
	 */
	public Dictionary infoContrat(String login, String id) {
		
		System.out.println("Infos sur un contrat");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "infoContrat")) {
		
			//On vérifie que le canal existe
			if (ContratFactory.containsId(id)) {
				Contrat c = (Contrat)ContratFactory.getById(id);
				return c.getAttributesDictionary();
			}
			else {
				System.out.println("Contrat indisponible");
				return null;
			}
		}
		// Sinon, refusé
		System.out.println("Permission non accordée. Contrat non affiché");
		return null;
	}
	
	/**
	 * Suppression d'un contrat
	 * @param String login
	 * @param String id
	 * @return String
	 */
	public String supprimerContrat(String login, String id) {
		System.out.println("Suppression du contrat "+id);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "supprimerContrat")) {
		
			//On vérifie que le canal existe
			if (ContratFactory.containsId(id)) {
				
				//On récupère l'objet canal
				Contrat c = (Contrat)ContratFactory.getById(id);

				//Si le document a bien été supprimé
				if (c.supprimer()) {
					
					//On le retire de la liste des canaux du système
					//contrats.remove(id);
					
					System.out.println("Contrat '"+id+"' supprimé");
					return Boolean.toString(true);
				}
				
				//Sinon, suppression échouée
				System.out.println("Contrat '"+id+"' non supprimé");
				return Boolean.toString(false);
			}
			
			//Sinon, canal introuvable
			System.out.println("Contrat '"+id+"' introuvable");
			return Boolean.toString(false);
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Contrat non supprimé");
		return Boolean.toString(false);
	}
	
	/**
	 * Modification d'un contrat
	 * @param String login
	 * @param String id
	 * @param String titre
	 * @param String jourSignature
	 * @param String moisSignature
	 * @param String anneeSignature
	 * @param String jourExpiration
	 * @param String moisExpiration
	 * @param String anneeExpiration
	 * @param String signataire
	 * @param String modeReglement
	 * @param String type
	 * @return String
	 */
	public String modifierContrat(String login, String id, String titre, String jourSignature, String moisSignature, String anneeSignature,
			String jourExpiration, String moisExpiration, String anneeExpiration, String signataire, String modeReglement, String type) throws NumberFormatException, SQLException {

		System.out.println("Modification du contrat "+id);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "modifierContrat")) {
		
			//On vérifie que le canal existe
			if (ContratFactory.containsId(id)) {
				
				//On récupère le canal
				Contrat c = (Contrat)ContratFactory.getById(id);
				
				//On modifie le canal
				if (c.modifier(titre, Integer.parseInt(jourSignature), Integer.parseInt(moisSignature), Integer.parseInt(anneeSignature),
						Integer.parseInt(jourExpiration), Integer.parseInt(moisExpiration), Integer.parseInt(anneeExpiration),
						signataire, modeReglement, type)) {
				
					System.out.println("Contrat '"+id+"' modifié");
					return Boolean.toString(true);
				}
				
				//Sinon, création a échoué
				System.out.println("Contrat '"+id+"' non modifié");
				return Boolean.toString(false);
			}
			
			//Sinon, création refusée
			System.out.println("Contrat '"+id+"' non trouvé");
			return Boolean.toString(false);
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Contrat non modifié");
		return Boolean.toString(false);
	}
	
	/**
	 * Ajoute un document dans un contrat
	 * @param String login
	 * @param String idContrat
	 * @param String idDoc
	 * @return String
	 * @throws SQLException 
	 */
	public String ajouterDocumentContrat(String login, String idContrat, String idDoc) throws SQLException {
		System.out.println("Ajout du document "+idDoc+" au contrat "+idContrat);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "ajouterDocumentContrat")) {
		
			//On vérifie que le contrat et le document existent
			if (ContratFactory.containsId(idContrat) && DocumentFactory.containsId(idDoc)) {
				
				//On récupère les objets
				Contrat c = (Contrat)ContratFactory.getById(idContrat);
				Document d = (Document)DocumentFactory.getById(idDoc);
				
				//On ajoute le contractant au contrat
				c.ajouterDocument(d);
				
				System.out.println("Document "+idDoc+" ajouté au contrat "+idContrat);
				return Boolean.toString(true);
				
			}
			
			//Sinon, ajout refusé
			System.out.println("Le document "+idDoc+" n'a pas été ajouté au contrat "+idContrat);
			return Boolean.toString(false);
		}
		//Sinon, ajout refusé
		System.out.println("Permission non accordée. Document non ajouté");
		return Boolean.toString(false);
	}
	
	/**
	 * Retirer un document d'un contrat
	 * @param String login
	 * @param String idContrat
	 * @param String idDoc
	 * @return String
	 * @throws SQLException 
	 */
	public String retirerDocumentContrat(String login, String idContrat, String idDoc) throws SQLException {
		System.out.println("Retirer le document "+idDoc+ " du contrat "+idContrat);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "retirerDocumentContrat")) {
		
			//On vérifie que le programme et le document existent
			if (ContratFactory.containsId(idContrat) && DocumentFactory.containsId(idDoc)) {
				
				//On récupère les objets
				Contrat c = (Contrat)ContratFactory.getById(idContrat);
				
				//On retire le document du contrat
				c.retirerDocument(idDoc);
				
				System.out.println("Document retiré du contrat "+idContrat);
				return Boolean.toString(true);
				
			}
			
			//Sinon, ajout refusé
			System.out.println("Le document n'a pas été retiré du contrat "+idContrat);
			return Boolean.toString(false);
		}
		//Sinon, ajout refusé
		System.out.println("Permission non accordée. Document non retiré");
		return Boolean.toString(false);
	}
	
//	 CONTRACTANT
//*******************************************************

	/**
	 * Création d'un contractant
	 * @param String nom
	 * @param String adresse
	 * @param String codePostal
	 * @param String ville
	 * @param String telephone
	 * @param String fax
	 * @param String mail
	 * @param String type
	 * @return String
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public String creerContractant(String login, String nom, String adresse, String codePostal,
			String ville, String telephone, String fax, String mail, String type) throws SQLException {

		System.out.println("Création du contractant");
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "creerContractant")) {
		
			//On vérifie que le canal n'existe pas
			if (ContractantFactory.getByNom(nom) == null) {
				
				//On crée le canal
				@SuppressWarnings("unused")
				Contractant c = ContractantFactory.create(nom, adresse, codePostal, ville, telephone, fax, mail, type);
				
				//On l'ajoute à la liste des contractants du système
				//contractants.put(c.getId(), c);
				
				System.out.println("Contractant créé");
				return Boolean.toString(true);
				
			}
			
			//Sinon, création refusée
			System.out.println("Contractant non créé");
			return Boolean.toString(false);
		}
		//Sinon, création refusée
		System.out.println("Permission non accordée. Contractant non créé");
		return Boolean.toString(false);
	}	
	
	/**
	 * Lister les contractants disponibles
	 * @param String login
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerContractants(String login) {

		System.out.println("Liste des contractants disponibles");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "listerContractants")) {
		
			//On crée le vecteur
			Vector vContractants = new Vector();
			
			//On récupère la liste des documents
			Enumeration listeContractants = ContractantFactory.getInstances().elements();
			
			while (listeContractants.hasMoreElements()) {
				Contractant c = (Contractant)listeContractants.nextElement();
				vContractants.addElement(c.getAttributesDictionary());
			}
			
			return vContractants;
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Contractants non listés");
		return null;
	}
	
	/**
	 * Informations sur un contractant
	 * @param String login
	 * @param Strign id
	 * @return Dictionary
	 */
	public Dictionary infoContractant(String login, String id) {
		
		System.out.println("Infos sur un contractant");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "infoContractant")) {
		
			//On vérifie que le canal existe
			if (ContractantFactory.containsId(id)) {
				Contractant c = (Contractant)ContractantFactory.getById(id);
				return c.getAttributesDictionary();
			}
			else {
				System.out.println("Contractant indisponible");
				return null;
			}
		}
		// Sinon, refusé
		System.out.println("Permission non accordée. Contractant non affiché");
		return null;
	}
	
	/**
	 * Suppression d'un contractant
	 * @param String login
	 * @param String id
	 * @return String
	 */
	public String supprimerContractant(String login, String id) {
		System.out.println("Suppression du contractant "+id);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "supprimerContractant")) {
		
			//On vérifie que le canal existe
			if (ContractantFactory.containsId(id)) {
				
				//On récupère l'objet canal
				Contractant c = (Contractant)ContractantFactory.getById(id);

				//Si le document a bien été supprimé
				if (c.supprimer()) {
					
					//On le retire de la liste des canaux du système
					//contractants.remove(id);
					
					System.out.println("Contractant '"+id+"' supprimé");
					return Boolean.toString(true);
				}
				
				//Sinon, suppression échouée
				System.out.println("Contractant '"+id+"' non supprimé");
				return Boolean.toString(false);
			}
			
			//Sinon, canal introuvable
			System.out.println("Contractant '"+id+"' introuvable");
			return Boolean.toString(false);
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Contractant non supprimé");
		return Boolean.toString(false);
	}
	
	/**
	 * Modification d'un contractant
	 * @param String login
	 * @param String id
	 * @param String nom
	 * @param String adresse
	 * @param String codePostal
	 * @param String ville
	 * @param String telephone
	 * @param String fax
	 * @param String type
	 * @return String
	 */
	public String modifierContractant(String login, String id, String nom, String adresse, String codePostal,
			String ville, String telephone, String fax, String mail, String type) throws NumberFormatException, SQLException {

		System.out.println("Modification du contractant "+id);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "modifierContractant")) {
		
			//On vérifie que le canal existe
			if (ContractantFactory.containsId(id)) {
				
				//On récupère le canal
				Contractant c = (Contractant)ContractantFactory.getById(id);
				
				//On modifie le canal
				if (c.modifier(nom, adresse, codePostal, ville, telephone, fax, mail, type)) {
				
					System.out.println("Contractant '"+id+"' modifié");
					return Boolean.toString(true);
				}
				
				//Sinon, création a échoué
				System.out.println("Contractant '"+id+"' non modifié");
				return Boolean.toString(false);
			}
			
			//Sinon, création refusée
			System.out.println("Contractant '"+id+"' non trouvé");
			return Boolean.toString(false);
		}
		// Sinon, création refusée
		System.out.println("Permission non accordée. Contractant non modifié");
		return Boolean.toString(false);
	}
}
