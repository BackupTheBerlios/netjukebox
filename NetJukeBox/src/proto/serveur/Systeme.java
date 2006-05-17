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
	 * Utilisateurs connect�s aux syst�me
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
	 * Connexion � la base de donn�es
	 */
	private Jdbc base;
	
	/**
	 * Pr�f�rences
	 */
	private Preferences prefs;
	
	/**
	 * Connexion � l'annuaire LDAP
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
		
		//On initialise la connection � la base
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
		
		//On initialise la connection � l'annuaire LDAP
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
	
		//R�cup�ration des information du serveur SMTP
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
	 * Signale au client que le seveur est bien connect�
	 * @param String ip
	 * @return String
	 */
	public String testConnectXML(String ip) {
		System.out.println("Test de connexion envoy� par client XML "+ip);
		return Boolean.toString(true);
	}
	
	// UTILISATEUR
	//*******************************************************
	
	/**
	 * V�rifie que l'utilisateur � la permission requise
	 * @param String login
	 * @param String perm
	 * @return boolean
	 */
	private boolean verifPermission(String login, String perm) {
		
		//On r�cup�re l'utilisateur
		Utilisateur u = (Utilisateur)utilisateurs.get(login);
		
		//On retourne le r�sultat
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
		
		//On v�rifie que l'utilisateur n'est pas d�j� connect�
		if (!utilisateurs.containsKey(loginldap)) {

			//On initialise la connection � l'annuaire LDAP
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
				
				//On l'ajoute � la liste des utilisateurs connect�s au syst�me
				utilisateurs.put(loginldap, util);
				
				System.out.println("Utilisateur "+loginldap+" connect�");
				return Boolean.toString(true);
			} else {
			
				//Sinon, connexion refus�e
				System.out.println("Utilisateur "+loginldap+" non connect�");
				return Boolean.toString(false);
			}
		} else {
		
			//Sinon, connexion refus�e
			System.out.println("Utilisateur "+loginldap+" d�j� connect�");
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
		
		//S'il y a des utilisateurs connect�s
		if (utilisateurs.size()>0) {
			
			//On recherche l'utilisateur en question parmi cette liste
			if (utilisateurs.containsKey(login)) {
				
				//On r�cup�re l'objet utilisateur
				Utilisateur util = (Utilisateur)utilisateurs.get(login);
				
				//On le d�connecte
				util.deconnexion();
				
				//On le supprime de la liste des utilisateurs connect�s au syst�me
				utilisateurs.remove(login);
				
				System.out.println("Utilisateur "+login+" deconnect�");
				return Boolean.toString(true);
			}
			System.out.println("Utilisateur "+login+" introuvable");
			return Boolean.toString(false);
		}
		System.out.println("Aucun utilisateur n'est connect�");
		return Boolean.toString(false);
	}

	/**
	 * Recherche un utilisateur � partir de son login
	 * @param login
	 * @return Utilisateur
	 * @throws NamingException 
	 */
	public Utilisateur chercherUtilisateur(String login) throws NamingException {
		
		//On v�rifie que le couple login existe
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "inscription")) {
		
			//On v�rifie que le login n'existe pas
			if (!Utilisateur.verifierLogin(log)) {
				
				//On cr�e l'utilisateur
				Utilisateur u = Utilisateur.create(log, pass, nom, prenom, email, pays, role);
				
				//Si l'utilisateur a bien �t� cr��
				if (u != null) {
					//On l'ajoute � la liste des utilisateurs du syst�me
					utilisateurs.put(u.getLogin(), u);
					
					System.out.println("Utilisateur '"+log+"' cr��");
					new EnvoiMail(host, port, from, log, nom, prenom, email, pass, pays);
					return Boolean.toString(true);
				}
				
				//Sinon, cr�ation a �chou�
				System.out.println("Utilisateur '"+log+"' non cr��");
				return Boolean.toString(false);
			}
			
			//Sinon, cr�ation refus�e
			System.out.println("Utilisateur '"+log+"' d�j� existant. Inscription annul�e");
			return Boolean.toString(false);
		}
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Inscription annul�e");
		return Boolean.toString(false);
	}

	/**
	 * Suppression d'un compte utilisateur
	 * @param Login
	 * @throws NamingException
	 */
	public String supprimerUtilisateur(String Login) throws NamingException {
		
		System.out.println("Suppression de l'utilisateur "+Login);
		
		//V�rification de l'existance du login � supprimer et suppression 
		if (Utilisateur.verifierLogin(Login)) {
			Utilisateur.deleteByLogin(Login);
			
			return Boolean.toString(true);
		} else {
		//Sinon annulation
			return Boolean.toString(false);
		}
	}

	/**
	 * R�cup�ration de l'objet � modifier
	 * @param login
	 * @return Vector
	 * @throws NamingException
	 */
	@SuppressWarnings("unchecked")
	public Vector rechercherUtilisateur(String login) throws NamingException {
		System.out.println("Recherche des attributs de l'utilisateur : " + login);

		//On v�rifie que l'utilisateur a la permission
			if (Utilisateur.verifierLogin(login)) {
				Utilisateur u = Utilisateur.getByLogin(login);
			
				//Vecteur d'attributs � retourner
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
				// Sinon, cr�ation refus�e
				System.out.println("Permission non accord�e. Recherche non effectu�es");
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

		//On v�rifie que l'utilisateur a la permission
		if (Utilisateur.verifierLogin(login)) {
			
			//On r�cup�re les attributs
			Utilisateur u = Utilisateur.getByLogin(login);
			String role = u.getRole();
			
			//v�rification du nouveau login compl�t�
			if (newlogin.length() == 0) {
				newlogin = u.getLogin();
			}
			
			//v�rification du nouveau nom compl�t�
			if (nom.length() == 0) {
				nom = u.getNom();
			}
			
			//v�rification du nouveau pr�nom compl�t�
			if (prenom.length() == 0 ) {
				prenom = u.getPrenom();
			}
			
			//v�rification du nouveau mail compl�t�
			if (mail.length() == 0) {
				mail = u.getMail();
			}
			
			//v�rification du nouveau pays compl�t�
			if (pays.length() == 0) {
				pays = u.getPays();
			}
			
			//v�rification du nouveau password compl�t�
			if (pwd.length() == 0) {
				pwd = u.getPwd();
			}
			
			//On modifie les attributs
			u.modifierInfos(login, role, newlogin, pwd, nom, prenom, mail, pays);
			System.out.println("Utilisateur '"+login+"' modifi�");
			return Boolean.toString(true);
		} else {
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Utilisateur non modifi�");
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
	 * Cr�ation d'un document
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

		System.out.println("Cr�ation du document "+titre);

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "creerDocument")) {
		
			//On v�rifie que le document n'existe pas
			if (DocumentFactory.getByTitre(titre) == null) {
				
				//On cr�e le document
				Document d = DocumentFactory.create(titre, Integer.parseInt(duree), Integer.parseInt(jour), Integer.parseInt(mois), Integer.parseInt(annee), source, langue, genre, fichier, artiste, interprete, compositeur);
				
				//Si le document a bien �t� cr��
				if (d != null) {
					//On l'ajoute � la liste des documents du syst�me
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
					
					//M�J de l'objet document et de la base
					d.setFichier(fichierResult);

					//Suppression du fichier initial
					File f = new File(fichierInit);
					f.delete(); 
					
					System.out.println("Document '"+titre+"' cr��");
					return Boolean.toString(true);
				}
				
				//Sinon, cr�ation a �chou�
				System.out.println("Document '"+titre+"' non cr��");
				return Boolean.toString(false);
			}
			
			//Sinon, cr�ation refus�e
			System.out.println("Document '"+titre+"' non cr��");
			return Boolean.toString(false);
		}
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Document non cr��");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "listerDocuments")) {
		
			//On cr�e le vecteur
			Vector vDocuments = new Vector();
			
			//On r�cup�re la liste des documents
			Enumeration listeDocs = DocumentFactory.getInstances().elements();
			Document d;
			
			while (listeDocs.hasMoreElements()) {
				d = (Document)listeDocs.nextElement();
				vDocuments.addElement(d.getAttributesDictionary());
			}
			
			return vDocuments;
		}
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Documents non list�s");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "rechercherDocument")) {
			
			//Vecteur de documents � retourner
			Vector vDocuments = new Vector();
		
			//On r�cup�re la liste des documents
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
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Recherche non effectu�es");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "supprimerDocument")) {
		
			//On v�rifie que le document existe
			if (DocumentFactory.containsId(id)) {
				
				//On r�cup�re l'objet document
				Document d = (Document)DocumentFactory.getById(id);

				//Si le document a bien �t� supprim�
				if (d.supprimer()) {
					
					//On le retire de la liste des documents du syst�me
					//documents.remove(id);
					File f = new File(d.getFichier());
					f.delete(); 
					
					System.out.println("Document '"+id+"' supprim�");
					return Boolean.toString(true);
				}
				
				//Sinon, suppression �chou�e
				System.out.println("Document '"+id+"' non supprim�");
				return Boolean.toString(false);
			}
			
			//Sinon, document introuvable
			System.out.println("Document '"+id+"' introuvable");
			return Boolean.toString(false);
		}
		// Sinon, suppression refus�e
		System.out.println("Permission non accord�e. Document non supprim�");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "modifierDocument")) {
		
			//On v�rifie que le document existe
			if (DocumentFactory.containsId(id)) {
				
				//On r�cup�re le doc
				Document d = (Document)DocumentFactory.getById(id);
				
				//On modifie le document
				if (d.modifier(titre, Integer.parseInt(duree), Integer.parseInt(jour), Integer.parseInt(mois), Integer.parseInt(annee), source, langue, genre, fichier, artiste, interprete, compositeur)) {
				
					System.out.println("Document '"+id+"' modifi�");
					return Boolean.toString(true);
				}
				
				//Sinon, cr�ation a �chou�
				System.out.println("Document '"+id+"' non modifi�");
				return Boolean.toString(false);
			}
			
			//Sinon, cr�ation refus�e
			System.out.println("Document '"+id+"' non trouv�");
			return Boolean.toString(false);
		}
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Document non modifi�");
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
        //On v�rifie que l'utilisateur a la permission
        if (Utilisateur.verifierLogin(login)) {
           
            //On r�cup�re les attributs
            Utilisateur u = Utilisateur.getByLogin(login);
           
            String nom = u.getNom();
            String prenom = u.getPrenom();
            String email = u.getMail();
            String pass = u.getPwd();
            String pays = u.getPays();
                   
            new EnvoiMail(host, port, from, login, nom, prenom, email, pass, pays);
            return Boolean.toString(true);
           
        } else {
            // Sinon, cr�ation refus�e
            System.out.println("Permission non accord�e. Mail non envoy�");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "infoDocument")) {
		
			//On v�rifie que le document existe
			if (DocumentFactory.containsId(id)) {
				Document d = (Document)DocumentFactory.getById(id);
				return d.getAttributesDictionary();
			}
			else {
				System.out.println("Document indisponible");
				return null;
			}
		}
		// Sinon, refus�
		System.out.println("Permission non accord�e. Document non affich�");
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
	 * Cr�ation d'un programme
	 * @param String login
	 * @param String titre
	 * @param String thematique
	 * @return String
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public String creerProgramme(String login, String titre, String thematique) throws SQLException {

		System.out.println("Cr�ation du programme "+titre);
		
		// On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "creerProgramme")) {
		
			//On v�rifie que le programme n'existe pas
			if (ProgrammeFactory.getByTitre(titre) == null) {
				
				//On cr�e le programme
				@SuppressWarnings("unused")
				Programme p = ProgrammeFactory.create(titre, thematique);
				
				//On l'ajoute � la liste des programmes du syst�me
				//programmes.put(p.getId(), p);
				
				System.out.println("Programme '"+titre+"' cr��");
				return Boolean.toString(true);
			}
			
			//Sinon, cr�ation refus�e
			System.out.println("Programme '"+titre+"' non cr��");
			return Boolean.toString(false);
		}
		//Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Programme non cr��");
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
		
		// On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "ajouterDocumentProgramme")) {
		
			//On v�rifie que le programme et le document existent
			if (ProgrammeFactory.containsId(idProg) && DocumentFactory.containsId(idDoc)) {
				
				//On r�cup�re les objets
				Document d = (Document)DocumentFactory.getById(idDoc);
				Programme p = (Programme)ProgrammeFactory.getById(idProg);
				
				//On ajoute le document au programme
				p.ajouterDocument(d);
				
				System.out.println("Document "+idDoc+" ajout� au programme "+idProg);
				return Boolean.toString(true);
				
			}
			
			//Sinon, ajout refus�
			System.out.println("Le document "+idDoc+" n'a pas �t� ajout� au programme "+idProg);
			return Boolean.toString(false);
		}
		//Sinon, ajout refus�
		System.out.println("Permission non accord�e. Le document non programm�");
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
		System.out.println("Retirer du programme "+idProg+ " le document cal� � "+calage);
		
		// On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "retirerDocumentProgramme")) {
		
			//On v�rifie que le programme
			if (ProgrammeFactory.containsId(idProg)) {
				
				//On r�cup�re les objets
				Programme p = (Programme)ProgrammeFactory.getById(idProg);
				
				//On ajoute le document au programme
				p.retirerDocument(calage);
				
				System.out.println("Document retir� du programme "+idProg);
				return Boolean.toString(true);
				
			}
			
			//Sinon, ajout refus�
			System.out.println("Le document n'a pas �t� retir� au programme "+idProg);
			return Boolean.toString(false);
		}
		//Sinon, ajout refus�
		System.out.println("Permission non accord�e. Le document non retir�");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "listerProgrammes")) {
		
			//On cr�e le vecteur
			Vector vProgrammes = new Vector();
			
			//On r�cup�re la liste des documents
			Enumeration listeProgrammes = ProgrammeFactory.getInstances().elements();
			Programme p = null;
			
			while (listeProgrammes.hasMoreElements()) {
				p = (Programme)listeProgrammes.nextElement();
				vProgrammes.addElement(p.getAttributesDictionary());
			}
			
			return vProgrammes;
		}
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Programmes non list�s");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "rechercherProgramme")) {
			
			//Vecteur de programmes � retourner
			Vector vProgrammes = new Vector();
		
			//On r�cup�re la liste des programmes
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
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Recherche non effectu�es");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "supprimerProgramme")) {
		
			//On v�rifie que le programme existe
			if (ProgrammeFactory.containsId(id)) {
				
				//On r�cup�re l'objet document
				Programme p = (Programme)ProgrammeFactory.getById(id);

				//On le supprime. Si succ�s...
				if (p.supprimer()) {
					
					//On le retire de la liste des programmes du syst�me
					//programmes.remove(id);
					
					System.out.println("Programme '"+id+"' supprim�");
					return Boolean.toString(true);
				}
				
				//Sinon, suppression �chou�e
				System.out.println("Programme '"+id+"' non supprim�");
				return Boolean.toString(false);
			}
			
			//Sinon, programme introuvable
			System.out.println("Programme '"+id+"' introuvable");
			return Boolean.toString(false);
		}
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Programme non supprim�");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "modifierProgramme")) {
		
			//On v�rifie que le programme existe
			if (ProgrammeFactory.containsId(id)) {
				
				//On r�cup�re le prog
				Programme p = (Programme)ProgrammeFactory.getById(id);
				
				//On modifie le programme
				if (p.modifier(titre, thematique)) {
				
					System.out.println("Programme '"+id+"' modifi�");
					return Boolean.toString(true);
				}
				
				//Sinon, cr�ation a �chou�
				System.out.println("Programme '"+id+"' non modifi�");
				return Boolean.toString(false);
			}
			
			//Sinon, cr�ation refus�e
			System.out.println("Programme '"+id+"' non trouv�");
			return Boolean.toString(false);
		}
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Programme non modifi�");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "infoProgramme")) {
		
			//On v�rifie que le programme existe
			if (ProgrammeFactory.containsId(id)) {
				Programme p = (Programme)ProgrammeFactory.getById(id);
				return p.getAttributesDictionary();
			}
			else {
				System.out.println("Programme indisponible");
				return null;
			}
		}
		
		// Sinon, refus�
		System.out.println("Permission non accord�e. Programme non affich�");
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
	 * Cr�ation d'un canal
	 * @param String login
	 * @param String nom
	 * @param String utilMax
	 * @return String
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	@SuppressWarnings("unchecked")
	public String creerCanal(String login, String nom, String utilMax) throws NumberFormatException, SQLException {

		System.out.println("Cr�ation du canal "+nom);
		
		// On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "creerCanal")) {
		
			//On v�rifie que le canal n'existe pas
			if (CanalFactory.getByNom(nom) == null) {
				
				//On cr�e le canal
				@SuppressWarnings("unused")
				Canal c = CanalFactory.create(nom, Integer.parseInt(utilMax));
				
				//On l'ajoute � la liste des canaux du syst�me
				//canaux.put(c.getId(), c);
				
				System.out.println("Canal '"+nom+"' cr��");
				return Boolean.toString(true);
				
			}
			
			//Sinon, cr�ation refus�e
			System.out.println("Canal '"+nom+"' non cr��");
			return Boolean.toString(false);
		}
		//Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Canal non cr��");
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
		
		// On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "planifierCanal")) {
		
			//On v�rifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal) && ProgrammeFactory.containsId(idProg)) {
				
				//On r�cup�re les objets
				Canal c = (Canal)CanalFactory.getById(idCanal);
				Programme p = (Programme)ProgrammeFactory.getById(idProg);
				
				//On planifie le programme
				if (c.planifierProgramme(p, Integer.parseInt(jour), Integer.parseInt(mois), Integer.parseInt(annee), Integer.parseInt(heure), Integer.parseInt(minute), Integer.parseInt(seconde))) {
				
					System.out.println("Programme "+idProg+" planifi� sur le canal "+idCanal);
					return Boolean.toString(true);
				}
				else {
					System.out.println("Programme "+idProg+" non planifi� sur le canal "+idCanal);
					return Boolean.toString(false);
				}
				
			}
			
			//Sinon, ajout refus�
			System.out.println("Le programme "+idProg+" n'a pas �t� plnifi� sur le canal "+idCanal);
			return Boolean.toString(false);
		}
		
		//Sinon, ajout refus�
		System.out.println("Permission non accord�e. Programme non planifi�");
		return Boolean.toString(false);
	}
	
	/**
	 * D�planifier un programme sur un canal
	 * @param String login
	 * @param String idCanal
	 * @param String calage
	 * @return String
	 */
	public String deplanifierProgramme(String login, String idCanal, String calage) {

		System.out.println("D�planification d'un programme sur le canal "+idCanal);
		
		// On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "deplanifierCanal")) {
		
			//On v�rifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal)) {
				
				//On r�cup�re les objets
				Canal c = (Canal)CanalFactory.getById(idCanal);
				
				//On deplanifie le programme
				if (c.annulerPlanification(Long.parseLong(calage))) {
				
					System.out.println("Programme d�planifi� du canal "+idCanal);
					return Boolean.toString(true);
				}
				else {
					System.out.println("Programme non d�planifi� du le canal "+idCanal);
					return Boolean.toString(false);
				}
				
			}
			
			//Sinon, ajout refus�
			System.out.println("Le programme n'a pas �t� d�planifi� sur le canal "+idCanal);
			return Boolean.toString(false);
		}
		
		//Sinon, ajout refus�
		System.out.println("Permission non accord�e. Programme non d�planifi�");
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
		
		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "diffuserProgramme")) {
		
			//On v�rifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal) && ProgrammeFactory.containsId(idProg)) {
				
				//On r�cup�re les objets
				Canal c = (Canal)CanalFactory.getById(idCanal);
				Programme p = (Programme)ProgrammeFactory.getById(idProg);
				
				//On diffuse le programme
				if (!c.isRTPstarted()) c.createRTPServer(ipStreaming, portStreaming++, prefs.node("streaming").get("publicite", null));
				c.diffuserProgramme(p);
				
				System.out.println("Programme "+idProg+" en diffusion sur le canal "+idCanal);
				return Boolean.toString(true);
				
			}
			
			//Sinon, ajout refus�
			System.out.println("La diffusion du programme "+idProg+" n'a pas �t� lanc�e sur le canal "+idCanal);
			return Boolean.toString(false);
		}
		
		//Sinon, ajout refus�
		System.out.println("Permission non accord�e. Diffusion non lanc�e");
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
		
		//	On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "ecouterCanal")) {
		
			//On v�rifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal)) {
				
				//On r�cup�re l'objet canal
				Canal c = (Canal)CanalFactory.getById(idCanal);
				System.out.println("Construction de l'url du canal "+idCanal);
				String url = c.getUrlStreaming();
				System.out.println("URL: "+url);
				return url;
			}
			
			//Sinon, ajout refus�
			System.out.println("Canal "+idCanal+" inconnu");
			return null;
		}
		
		//Sinon, ajout refus�
		System.out.println("Permission non accord�e.");
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
		
		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "ecouterCanal")) {
		
			//On v�rifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal)) {
				
				//On r�cup�re l'objet canal
				Canal c = (Canal)CanalFactory.getById(idCanal);
				if (!c.isRTPstarted()) c.createRTPServer(ipStreaming, portStreaming++, prefs.node("streaming").get("publicite", null));
				c.startDiffusion();
				return Boolean.toString(true);
			}
			
			//Sinon, ajout refus�
			System.out.println("Canal "+idCanal+" inconnu");
			return Boolean.toString(false);
		}
		
		//Sinon, ajout refus�
		System.out.println("Permission non accord�e. Canal non d�marr�");
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
		
		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "ecouterCanal")) {
		
			//On v�rifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal)) {
				
				//On r�cup�re l'objet canal
				Canal c = (Canal)CanalFactory.getById(idCanal);
				c.stopDiffusion();
				return Boolean.toString(true);
			}
			
			//Sinon, ajout refus�
			System.out.println("Canal "+idCanal+" inconnu");
			return Boolean.toString(false);
		}
		
		//Sinon, ajout refus�
		System.out.println("Permission non accord�e. Canal non arr�t�");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "listerCanaux")) {
		
			//On cr�e le vecteur
			Vector vCanaux = new Vector();
			
			//On r�cup�re la liste des documents
			Enumeration listeCanaux = CanalFactory.getInstances().elements();
			Canal c;
			
			while (listeCanaux.hasMoreElements()) {
				c = (Canal)listeCanaux.nextElement();
				vCanaux.addElement(c.getAttributesDictionary());
			}
			
			return vCanaux;
		}
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Canaux non list�s");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "rechercherCanal")) {
			
			//Vecteur de canaux � retourner
			Vector vCanaux = new Vector();
		
			//On r�cup�re la liste des programmes
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
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Recherche non effectu�es");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "supprimerCanal")) {
		
			//On v�rifie que le canal existe
			if (CanalFactory.containsId(id)) {
				
				//On r�cup�re l'objet canal
				Canal c = (Canal)CanalFactory.getById(id);

				//Si le document a bien �t� supprim�
				if (c.supprimer()) {
					
					//On le retire de la liste des canaux du syst�me
					//canaux.remove(id);
					
					System.out.println("Canal '"+id+"' supprim�");
					return Boolean.toString(true);
				}
				
				//Sinon, suppression �chou�e
				System.out.println("Canal '"+id+"' non supprim�");
				return Boolean.toString(false);
			}
			
			//Sinon, canal introuvable
			System.out.println("Canal '"+id+"' introuvable");
			return Boolean.toString(false);
		}
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Canal non supprim�");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "modifierCanal")) {
		
			//On v�rifie que le canal existe
			if (CanalFactory.containsId(id)) {
				
				//On r�cup�re le canal
				Canal c = (Canal)CanalFactory.getById(id);
				
				//On modifie le canal
				if (c.modifier(nom, Integer.valueOf(utilMax))) {
				
					System.out.println("Canal '"+id+"' modifi�");
					return Boolean.toString(true);
				}
				
				//Sinon, cr�ation a �chou�
				System.out.println("Canal '"+id+"' non modifi�");
				return Boolean.toString(false);
			}
			
			//Sinon, cr�ation refus�e
			System.out.println("Canal '"+id+"' non trouv�");
			return Boolean.toString(false);
		}
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Canal non modifi�");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "infoCanal")) {
		
			//On v�rifie que le canal existe
			if (CanalFactory.containsId(id)) {
				Canal c = (Canal)CanalFactory.getById(id);
				return c.getAttributesDictionary();
			}
			else {
				System.out.println("Canal indisponible");
				return null;
			}
		}
		// Sinon, refus�
		System.out.println("Permission non accord�e. Canal non affich�");
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
	 * Cr�ation d'un contrat
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

		System.out.println("Cr�ation du contrat");
		
		// On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "creerContrat")) {
		
			//On v�rifie que le canal n'existe pas
			if (ContratFactory.getByTitre(titre) == null) {
				
				//On cr�e le canal
				@SuppressWarnings("unused")
				Contrat c = ContratFactory.create(titre, Integer.parseInt(jourSignature), Integer.parseInt(moisSignature),
						Integer.parseInt(anneeSignature), Integer.parseInt(jourExpiration), Integer.parseInt(moisExpiration),
						Integer.parseInt(anneeExpiration), signataire, modeReglement, type);
				
				//On l'ajoute � la liste des contrats du syst�me
				//contrats.put(c.getId(), c);
				
				System.out.println("Contrat cr��");
				return Boolean.toString(true);
				
			}
			
			//Sinon, cr�ation refus�e
			System.out.println("Contrat non cr��");
			return Boolean.toString(false);
		}
		//Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Contrat non cr��");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "listerContrats")) {
		
			//On cr�e le vecteur
			Vector vContrats = new Vector();
			
			//On r�cup�re la liste des documents
			Enumeration listeContrats = ContratFactory.getInstances().elements();
			Contrat c;
			
			while (listeContrats.hasMoreElements()) {
				c = (Contrat)listeContrats.nextElement();
				vContrats.addElement(c.getAttributesDictionary());
			}
			
			return vContrats;
		}
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Contrats non list�s");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "infoContrat")) {
		
			//On v�rifie que le canal existe
			if (ContratFactory.containsId(id)) {
				Contrat c = (Contrat)ContratFactory.getById(id);
				return c.getAttributesDictionary();
			}
			else {
				System.out.println("Contrat indisponible");
				return null;
			}
		}
		// Sinon, refus�
		System.out.println("Permission non accord�e. Contrat non affich�");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "supprimerContrat")) {
		
			//On v�rifie que le canal existe
			if (ContratFactory.containsId(id)) {
				
				//On r�cup�re l'objet canal
				Contrat c = (Contrat)ContratFactory.getById(id);

				//Si le document a bien �t� supprim�
				if (c.supprimer()) {
					
					//On le retire de la liste des canaux du syst�me
					//contrats.remove(id);
					
					System.out.println("Contrat '"+id+"' supprim�");
					return Boolean.toString(true);
				}
				
				//Sinon, suppression �chou�e
				System.out.println("Contrat '"+id+"' non supprim�");
				return Boolean.toString(false);
			}
			
			//Sinon, canal introuvable
			System.out.println("Contrat '"+id+"' introuvable");
			return Boolean.toString(false);
		}
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Contrat non supprim�");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "modifierContrat")) {
		
			//On v�rifie que le canal existe
			if (ContratFactory.containsId(id)) {
				
				//On r�cup�re le canal
				Contrat c = (Contrat)ContratFactory.getById(id);
				
				//On modifie le canal
				if (c.modifier(titre, Integer.parseInt(jourSignature), Integer.parseInt(moisSignature), Integer.parseInt(anneeSignature),
						Integer.parseInt(jourExpiration), Integer.parseInt(moisExpiration), Integer.parseInt(anneeExpiration),
						signataire, modeReglement, type)) {
				
					System.out.println("Contrat '"+id+"' modifi�");
					return Boolean.toString(true);
				}
				
				//Sinon, cr�ation a �chou�
				System.out.println("Contrat '"+id+"' non modifi�");
				return Boolean.toString(false);
			}
			
			//Sinon, cr�ation refus�e
			System.out.println("Contrat '"+id+"' non trouv�");
			return Boolean.toString(false);
		}
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Contrat non modifi�");
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
		
		// On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "ajouterDocumentContrat")) {
		
			//On v�rifie que le contrat et le document existent
			if (ContratFactory.containsId(idContrat) && DocumentFactory.containsId(idDoc)) {
				
				//On r�cup�re les objets
				Contrat c = (Contrat)ContratFactory.getById(idContrat);
				Document d = (Document)DocumentFactory.getById(idDoc);
				
				//On ajoute le contractant au contrat
				c.ajouterDocument(d);
				
				System.out.println("Document "+idDoc+" ajout� au contrat "+idContrat);
				return Boolean.toString(true);
				
			}
			
			//Sinon, ajout refus�
			System.out.println("Le document "+idDoc+" n'a pas �t� ajout� au contrat "+idContrat);
			return Boolean.toString(false);
		}
		//Sinon, ajout refus�
		System.out.println("Permission non accord�e. Document non ajout�");
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
		
		// On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "retirerDocumentContrat")) {
		
			//On v�rifie que le programme et le document existent
			if (ContratFactory.containsId(idContrat) && DocumentFactory.containsId(idDoc)) {
				
				//On r�cup�re les objets
				Contrat c = (Contrat)ContratFactory.getById(idContrat);
				
				//On retire le document du contrat
				c.retirerDocument(idDoc);
				
				System.out.println("Document retir� du contrat "+idContrat);
				return Boolean.toString(true);
				
			}
			
			//Sinon, ajout refus�
			System.out.println("Le document n'a pas �t� retir� du contrat "+idContrat);
			return Boolean.toString(false);
		}
		//Sinon, ajout refus�
		System.out.println("Permission non accord�e. Document non retir�");
		return Boolean.toString(false);
	}
	
//	 CONTRACTANT
//*******************************************************

	/**
	 * Cr�ation d'un contractant
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

		System.out.println("Cr�ation du contractant");
		
		// On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "creerContractant")) {
		
			//On v�rifie que le canal n'existe pas
			if (ContractantFactory.getByNom(nom) == null) {
				
				//On cr�e le canal
				@SuppressWarnings("unused")
				Contractant c = ContractantFactory.create(nom, adresse, codePostal, ville, telephone, fax, mail, type);
				
				//On l'ajoute � la liste des contractants du syst�me
				//contractants.put(c.getId(), c);
				
				System.out.println("Contractant cr��");
				return Boolean.toString(true);
				
			}
			
			//Sinon, cr�ation refus�e
			System.out.println("Contractant non cr��");
			return Boolean.toString(false);
		}
		//Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Contractant non cr��");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "listerContractants")) {
		
			//On cr�e le vecteur
			Vector vContractants = new Vector();
			
			//On r�cup�re la liste des documents
			Enumeration listeContractants = ContractantFactory.getInstances().elements();
			
			while (listeContractants.hasMoreElements()) {
				Contractant c = (Contractant)listeContractants.nextElement();
				vContractants.addElement(c.getAttributesDictionary());
			}
			
			return vContractants;
		}
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Contractants non list�s");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "infoContractant")) {
		
			//On v�rifie que le canal existe
			if (ContractantFactory.containsId(id)) {
				Contractant c = (Contractant)ContractantFactory.getById(id);
				return c.getAttributesDictionary();
			}
			else {
				System.out.println("Contractant indisponible");
				return null;
			}
		}
		// Sinon, refus�
		System.out.println("Permission non accord�e. Contractant non affich�");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "supprimerContractant")) {
		
			//On v�rifie que le canal existe
			if (ContractantFactory.containsId(id)) {
				
				//On r�cup�re l'objet canal
				Contractant c = (Contractant)ContractantFactory.getById(id);

				//Si le document a bien �t� supprim�
				if (c.supprimer()) {
					
					//On le retire de la liste des canaux du syst�me
					//contractants.remove(id);
					
					System.out.println("Contractant '"+id+"' supprim�");
					return Boolean.toString(true);
				}
				
				//Sinon, suppression �chou�e
				System.out.println("Contractant '"+id+"' non supprim�");
				return Boolean.toString(false);
			}
			
			//Sinon, canal introuvable
			System.out.println("Contractant '"+id+"' introuvable");
			return Boolean.toString(false);
		}
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Contractant non supprim�");
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

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "modifierContractant")) {
		
			//On v�rifie que le canal existe
			if (ContractantFactory.containsId(id)) {
				
				//On r�cup�re le canal
				Contractant c = (Contractant)ContractantFactory.getById(id);
				
				//On modifie le canal
				if (c.modifier(nom, adresse, codePostal, ville, telephone, fax, mail, type)) {
				
					System.out.println("Contractant '"+id+"' modifi�");
					return Boolean.toString(true);
				}
				
				//Sinon, cr�ation a �chou�
				System.out.println("Contractant '"+id+"' non modifi�");
				return Boolean.toString(false);
			}
			
			//Sinon, cr�ation refus�e
			System.out.println("Contractant '"+id+"' non trouv�");
			return Boolean.toString(false);
		}
		// Sinon, cr�ation refus�e
		System.out.println("Permission non accord�e. Contractant non modifi�");
		return Boolean.toString(false);
	}
}
