package proto.serveur;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.Time;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpc;
import org.apache.xmlrpc.XmlRpcClient;

/**
 * Classe contenant la logique principale du serveur principal
 */
public class Systeme {
	/**
	 *  Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(Systeme.class);

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
	
	/**
	 * Timer
	 */
	private Timer timer;
	
	/**
	 * Threads de diffusion
	 */
	private Hashtable tasks;
	
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
		
		logger.info("BD Driver: "+driverjdbc);
		logger.info("BD URL: "+urljdbc);
		logger.info("BD Login: "+loginjdbc);
		logger.info("BD Pwd: "+pwdjdbc);
		
		base = Jdbc.getInstance();
		base.openDB(driverjdbc, urljdbc, loginjdbc, pwdjdbc);
		
		
		//On initialise la connection à l'annuaire LDAP
		String driverldap = prefs.node("ldap").get("driver", null);
		String typeldap = prefs.node("ldap").get("type", null);
		String ipldap = prefs.node("ldap").get("ip", null);
		String portldap = prefs.node("ldap").get("port", null);
		String baseNameldap = prefs.node("ldap").get("base", null);
		String authldap = prefs.node("ldap").get("auth", null);
		String loginldap = prefs.node("ldap").get("login", null);
		String pwdldap = prefs.node("ldap").get("pwd", null);
				
		String urlldap = typeldap + "://" + ipldap + ":" + portldap + "/" + baseNameldap;
		
		logger.info("LDAP Driver: " + driverldap);
		logger.info("LDAP URL: " + urlldap);
		logger.info("LDAP Authentification:" + authldap);
		logger.info("LDAP Login: " + loginldap);
		logger.info("LDAP Pwd: " + pwdldap);
		
		ldap = Ldap.getInstance();
		ldap.openLdap(driverldap, urlldap, authldap, loginldap, pwdldap, baseNameldap);
		 
	
		//Récupération des information du serveur SMTP
		this.host = prefs.node("smtp").get("host", null);
		this.port = prefs.node("smtp").get("port", null);
		this.from = prefs.node("smtp").get("from", null);
		
		logger.info("Adresse du serveur SMTP : " + host);
		logger.info("Port du serveur SMTP : " + port);
		logger.info("Mail envoyeur depuis le serveur SMTP : " + from);
		
		//On initialise les listes de canaux, programmes, documents
		CanalFactory.getAll();
		ProgrammeFactory.getAll();
		DocumentFactory.getAll();
		ContractantFactory.getAll();
		ContratFactory.getAll();
		RoleFactory.getAll();
		
		//"Ping" des clients
		this.timer = new Timer(true);
		this.tasks = new Hashtable();
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
		logger.info("Test de connexion envoyé par client XML "+ip);
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
	public String connexion(String loginldap, String pwd, String ipClient) throws NamingException {
		
		logger.info("Connexion de l'utilisateur "+ loginldap);
		
		//On vérifie que l'utilisateur n'est pas déjà connecté
		if (!utilisateurs.containsKey(loginldap)) {

			//On initialise la connection à l'annuaire LDAP
			String driverldap = prefs.node("ldap").get("driver", null);
			String typeldap = prefs.node("ldap").get("type", null);
			String ipldap = prefs.node("ldap").get("ip", null);
			String portldap = prefs.node("ldap").get("port", null);
			String baseNameldap = prefs.node("ldap").get("base", null);
			String authldap = prefs.node("ldap").get("auth", null);
			String login = prefs.node("ldap").get("login", null);
			String pwdldap = prefs.node("ldap").get("pwd", null);
							
			String urlldap = typeldap + "://" + ipldap + ":" + portldap + "/" + baseNameldap;
				
			logger.info("LDAP Driver: " + driverldap);
			logger.info("LDAP URL: " + urlldap);
			logger.info("LDAP Login: " + loginldap);
			logger.info("LDAP Pwd: " + pwd);
				
			ldap = Ldap.getInstance();
			ldap.openLdap(driverldap, urlldap, authldap, loginldap, pwd, baseNameldap);
				
			if (Utilisateur.verifierPwd(loginldap, pwd)) {
				Utilisateur util = Utilisateur.getByLogin(loginldap);
				
				//On connecte l'utilisateur
				util.connecter();
				
				//On l'ajoute à la liste des utilisateurs connectés au système
				utilisateurs.put(loginldap, util);
				
				//On planifie sa tâche PING
				PingTask task = new PingTask(loginldap, ipClient, portXML, this);
				//timer.scheduleAtFixedRate(task, 1000, 60000);
				
				logger.info("Utilisateur "+loginldap+" connecté");
				return Boolean.toString(true);
			} else {
			
				//Sinon, connexion refusée
				logger.info("Utilisateur "+loginldap+" non connecté");
				return Boolean.toString(false);
			}
		} else {
		
			//Sinon, connexion refusée
			logger.info("Utilisateur "+loginldap+" déjà connecté");
			return Boolean.toString(false);
		
		}
	}
	
	/**
	 * Deconnexion d'un utilisateur
	 * @param login
	 * @return String
	 */
	public String deconnexion(String login) {
		
		logger.info("Deconnexion de l'utilisateur "+login);
		
		//S'il y a des utilisateurs connectés
		if (utilisateurs.size()>0) {
			
			//On recherche l'utilisateur en question parmi cette liste
			if (utilisateurs.containsKey(login)) {
				
				//On récupère l'objet utilisateur
				Utilisateur util = (Utilisateur)utilisateurs.get(login);
				
				//On le déconnecte
				util.deconnecter();
				
				//On le retire l'auditeur des canaux
				Enumeration canaux = CanalFactory.getInstances().elements();
				while (canaux.hasMoreElements()) {
					Canal c = (Canal)canaux.nextElement();
					c.deconnecterAuditeur(login);
				}
				
				//On le supprime de la liste des utilisateurs connectés au système
				utilisateurs.remove(login);
				
				logger.info("Utilisateur "+login+" deconnecté");
				return Boolean.toString(true);
			}
			logger.info("Utilisateur "+login+" introuvable");
			return Boolean.toString(false);
		}
		logger.info("Aucun utilisateur n'est connecté");
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
		
		logger.info("Inscription de l'utilisateur "+log);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "inscriptionUtilisateur")) {
		
			//On vérifie que le login n'existe pas
			if (!Utilisateur.verifierLogin(log)) {
				
				//On crée l'utilisateur
				Utilisateur u = Utilisateur.create(log, pass, nom, prenom, email, pays, role);
				
				//Si l'utilisateur a bien été créé
				if (u != null) {
					//On l'ajoute à la liste des utilisateurs du système
					utilisateurs.put(u.getLogin(), u);
					
					logger.info("Utilisateur '"+log+"' créé");
					new EnvoiMail(host, port, from, log, nom, prenom, email, pass, pays);
					return Boolean.toString(true);
				}
				
				//Sinon, création a échoué
				logger.info("Utilisateur '"+log+"' non créé");
				return Boolean.toString(false);
			}
			
			//Sinon, opération refusée
			logger.info("Utilisateur '"+log+"' déjà existant. Inscription annulée");
			return Boolean.toString(false);
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Inscription annulée");
		return Boolean.toString(false);
	}

	/**
	 * Suppression d'un compte utilisateur
	 * @param Login
	 * @throws NamingException
	 */
	public String supprimerUtilisateur(String Login) throws NamingException {
		
		//On vérifie que l'utilisateur a la permission
		if (verifPermission(Login, "supprimerUtilisateur")) {
		
			logger.info("Suppression de l'utilisateur "+Login);
		
			//Vérification de l'existance du login à supprimer et suppression 
			if (Utilisateur.verifierLogin(Login)) {
				Utilisateur.deleteByLogin(Login);
			
				return Boolean.toString(true);
			} else {
				//Sinon annulation
				return Boolean.toString(false);
			}
		} else {
			//Sinon, opération refusée
			logger.info("Permission non accordée. Suppression annulée");
			return Boolean.toString(false);
		}
	}

	/**
	 * Récupération de l'objet recherché
	 * @param login
	 * @return Vector
	 * @throws NamingException
	 */
	@SuppressWarnings("unchecked")
	public Vector rechercherUtilisateur(String login) throws NamingException {
		logger.info("Recherche des attributs de l'utilisateur : " + login);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "rechercherUtilisateur")) {
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
				// Sinon, opération refusée
				logger.info("Permission non accordée. Recherche non effectuées");
				return null;
			}
	}
	
	/**
	 * Lister les utilisateur
	 * @param String login
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerUtilisateur(String login) {

		logger.info("Liste des utilisateurs");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "listerUtilisateur")) {
		
			//On crée le vecteur
			Vector vUtilisateurs = Utilisateur.listerUtilisateur();
			
			return vUtilisateurs;
		} else {
			// Sinon, opération refusée
			logger.info("Permission non accordée. Utilisateurs non listés");
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
			if (verifPermission(login, "modifierUtilisateur")) {
			
			//On récupère les attributs
			Utilisateur u = Utilisateur.getByLogin(login);
			String role = u.getRole().getId();
			
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
			u.modifier(login, role, newlogin, pwd, nom, prenom, mail, pays);
			logger.info("Utilisateur '"+login+"' modifié");
			return Boolean.toString(true);
		} else {
		// Sinon, opération refusée
		logger.info("Permission non accordée. Utilisateur non modifié");
		return Boolean.toString(false);
		}
	}
	
	/**
	 * Changement de role d'un utilisateur
	 * @param String login
	 * @param String role
	 * @return String
	 */
	public String changerRole(String login, String role) throws NamingException {
		
		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "changerRole")) {
			
			//On récupère l'utilisateur
			Utilisateur u = Utilisateur.getByLogin(login);

			//On change le rôle
			u.setRole(role);
			logger.info("Rôle de l'utilisateur '"+login+"' changé");
			return Boolean.toString(true);
		} else {
		// Sinon, opération refusée
		logger.info("Permission non accordée. Role non modifié");
		return Boolean.toString(false);
		}
			
	}
	 
	/**
	 * Attribuer une permission à un utilisateur
	 * @param String login
	 * @param String permission
	 * @return String
	 */
	public String ajouterPermissionUtilisateur(String login, String logUtil, String permission) throws NamingException {
		
		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "ajouterPermissionUtilisateur")) {
			
			//On vérifie que l'utilisateur et la permission existent
			if (utilisateurs.containsKey(logUtil) && PermissionFactory.containsId(permission)) {
				//On récupère l'utilisateur
				Utilisateur u = Utilisateur.getByLogin(logUtil);
				
				//On récupère la permission
				Permission p = PermissionFactory.getById(permission);

				//On attribue la permission
				u.ajouterPermission(p);
				
				logger.info("Permission ajoutée à l'utilisateur '"+login+"'");
				return Boolean.toString(true);
			}
			else {
				//Sinon, opération refusée
				logger.info("Permission non accordée. Permission non ajoutée");
				return Boolean.toString(false);
			}
		} else {
			// Sinon, opération refusée
			logger.info("Permission non accordée. Permission non ajoutée");
			return Boolean.toString(false);
		}
			
	}
	
	/**
	 * Retirer une permission à un utilisateur
	 * @param String login
	 * @param String permission
	 * @return String
	 */
	public String retirerPermissionUtilisateur(String login, String logUtil, String permission) throws NamingException {
		
		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "retirerPermissionUtilisateur")) {
			
			//On vérifie que l'utilisateur et la permission existent
			if (utilisateurs.containsKey(logUtil) && PermissionFactory.containsId(permission)) {
			
				//On récupère l'utilisateur
				Utilisateur u = Utilisateur.getByLogin(logUtil);
	
				//On retire la permission
				u.retirerPermission(permission);
				
				logger.info("Permission retirée à l'utilisateur '"+login+"'");
				return Boolean.toString(true);
			} else {
				// Sinon, opération refusée
				logger.info("Permission non accordée. Permission non retirée");
				return Boolean.toString(false);
			} 
		}else {
			// Sinon, opération refusée
			logger.info("Permission non accordée. Permission non retirée");
			return Boolean.toString(false);
		}
			
	}
	 
	public boolean emailValide(String Email) {
		// your code here
		return false;
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

		logger.info("Création du document "+titre);

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
					//if (!fichier.endsWith(".wav")) {
						//Conversion du fichier audio en .wav
						String mplayer = prefs.node("conversion").get("mplayer", null);
						String chemin = prefs.node("conversion").get("chemin", null);
						
						if (!chemin.endsWith("/")) chemin.concat("/");
						
						String fichierResult = chemin + d.getId() + ".wav";
						String fichierInit = d.getFichier();
						//Commande permettant la conversion
						String commande = mplayer + " -dumpaudio -dumpfile " + fichierResult + " " + fichierInit;
						//String commande = mplayer + " " + fichierInit+ " -af resample=22050:0:0 -ao pcm:file=" +fichierResult;
						//mplayer -vo null "/home/philippe/sinnerman.mp3" -ao pcm:file="/home/philippe/sinnerman.wav"
						logger.info(commande);
						//Execution de la conversion
						@SuppressWarnings("unused")
						Process proc = Runtime.getRuntime().exec(commande);
						proc.waitFor();
						
						//MàJ de l'objet document et de la base
						d.setFichier(fichierResult);
	
						//Suppression du fichier initial
						File f = new File(fichierInit);
						f.delete();
						
						
						//On détermine la durée
						String src = fichierResult;
						if (!fichierResult.startsWith("file:/")) {
							src = "file:/"+fichierResult;
						}

						try {
							Player p = Manager.createRealizedPlayer(new MediaLocator(src));
							Time dureeFichier = p.getDuration();
							d.setDuree((int)dureeFichier.getSeconds());
						} catch (Exception e) {
							logger.error("ERREUR: ", e);
						}
						
					//}
					
					logger.info("Document '"+titre+"' créé");
					return Boolean.toString(true);
				}
				
				//Sinon, création a échoué
				logger.info("Document '"+titre+"' non créé");
				return Boolean.toString(false);
			}
			
			//Sinon, opération refusée
			logger.info("Document '"+titre+"' non créé");
			return Boolean.toString(false);
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Document non créé");
		return Boolean.toString(false);
	}
	
	/**
	 * Lister les documents disponibles
	 * @param String login
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerDocument(String login) {

		logger.info("Liste des documents disponibles");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "listerDocument")) {
		
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
		// Sinon, opération refusée
		logger.info("Permission non accordée. Documents non listés");
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

		logger.info("Recherche d'un document");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "rechercherDocument")) {
			
			//Vecteur de documents à retourner
			Vector vDocuments = new Vector();
		
			//On récupère la liste des documents
			Enumeration listeDocs = DocumentFactory.getInstances().elements();
			Document d;
			
			while (listeDocs.hasMoreElements()) {
				d = (Document)listeDocs.nextElement();
				
				int jourP = d.getDate().get(GregorianCalendar.DAY_OF_MONTH);
				int moisP = d.getDate().get(GregorianCalendar.MONTH)+1;
				int anneeP = d.getDate().get(GregorianCalendar.YEAR);
				
				if ((id.length()>0 && d.getId().contains(id))
					|| (titre.length()>0 && d.getTitre().contains(titre))
					|| (genre.length()>0 && d.getGenre().contains(genre))
					|| (fichier.length()>0 && d.getFichier().contains(fichier))
					|| (source.length()>0 && d.getSource().contains(source))
					|| (langue.length()>0 && d.getLangue().contains(langue))
					|| (duree.length()>0 && Integer.toString(d.getDuree()).contains(duree))
					|| (artiste.length()>0 && d.getArtiste().contains(artiste))
					|| (interprete.length()>0 && d.getInterprete().contains(interprete))
					|| (compositeur.length()>0 && d.getCompositeur().contains(compositeur))
					|| (jour.length()>0 && jourP==Integer.parseInt(jour))
					|| (mois.length()>0 && moisP==Integer.parseInt(mois))
					|| (annee.length()>0 && anneeP==Integer.parseInt(annee))) {
					
					vDocuments.addElement(d.getAttributesDictionary());
				}
			}
			
			return vDocuments;
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Recherche non effectuées");
		return null;
	}
	
	/**
	 * Suppression d'un document
	 * @param String login
	 * @param String id
	 * @return String
	 */
	public String supprimerDocument(String login, String id) {
		logger.info("Suppression du document "+id);

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
					
					logger.info("Document '"+id+"' supprimé");
					return Boolean.toString(true);
				}
				
				//Sinon, suppression échouée
				logger.info("Document '"+id+"' non supprimé");
				return Boolean.toString(false);
			}
			
			//Sinon, document introuvable
			logger.info("Document '"+id+"' introuvable");
			return Boolean.toString(false);
		}
		// Sinon, suppression refusée
		logger.info("Permission non accordée. Document non supprimé");
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

		logger.info("Modification du document "+id);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "modifierDocument")) {
		
			//On vérifie que le document existe
			if (DocumentFactory.containsId(id)) {
				
				//On récupère le doc
				Document d = (Document)DocumentFactory.getById(id);
				
				//On modifie le document
				if (d.modifier(titre, Integer.parseInt(duree), Integer.parseInt(jour), Integer.parseInt(mois), Integer.parseInt(annee), source, langue, genre, fichier, artiste, interprete, compositeur)) {
				
					logger.info("Document '"+id+"' modifié");
					return Boolean.toString(true);
				}
				
				//Sinon, création a échoué
				logger.info("Document '"+id+"' non modifié");
				return Boolean.toString(false);
			}
			
			//Sinon, opération refusée
			logger.info("Document '"+id+"' non trouvé");
			return Boolean.toString(false);
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Document non modifié");
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
    public String rechercherPwd(String login) throws NamingException, AddressException, MessagingException {
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
            // Sinon, opération refusée
            logger.info("Permission non accordée. Mail non envoyé");
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
		
		logger.info("Infos sur un document");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "infoDocument")) {
		
			//On vérifie que le document existe
			if (DocumentFactory.containsId(id)) {
				Document d = (Document)DocumentFactory.getById(id);
				return d.getAttributesDictionary();
			}
			else {
				logger.info("Document indisponible");
				return null;
			}
		}
		// Sinon, refusé
		logger.info("Permission non accordée. Document non affiché");
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

		logger.info("Création du programme "+titre);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "creerProgramme")) {
		
			//On vérifie que le programme n'existe pas
			if (ProgrammeFactory.getByTitre(titre) == null) {
				
				//On crée le programme
				@SuppressWarnings("unused")
				Programme p = ProgrammeFactory.create(titre, thematique);
				
				//On l'ajoute à la liste des programmes du système
				//programmes.put(p.getId(), p);
				
				logger.info("Programme '"+titre+"' créé");
				return Boolean.toString(true);
			}
			
			//Sinon, opération refusée
			logger.info("Programme '"+titre+"' non créé");
			return Boolean.toString(false);
		}
		//Sinon, opération refusée
		logger.info("Permission non accordée. Programme non créé");
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
		logger.info("Ajout du document "+idDoc+" au programme "+idProg);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "ajouterDocumentProgramme")) {
		
			//On vérifie que le programme et le document existent
			if (ProgrammeFactory.containsId(idProg) && DocumentFactory.containsId(idDoc)) {
				
				//On récupère les objets
				Document d = (Document)DocumentFactory.getById(idDoc);
				Programme p = (Programme)ProgrammeFactory.getById(idProg);
				
				//On ajoute le document au programme
				p.ajouterDocument(d);
				
				logger.info("Document "+idDoc+" ajouté au programme "+idProg);
				return Boolean.toString(true);
				
			}
			
			//Sinon, ajout refusé
			logger.info("Le document "+idDoc+" n'a pas été ajouté au programme "+idProg);
			return Boolean.toString(false);
		}
		//Sinon, ajout refusé
		logger.info("Permission non accordée. Le document non programmé");
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
		logger.info("Retirer du programme "+idProg+ " le document calé à "+calage);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "retirerDocumentProgramme")) {
		
			//On vérifie que le programme
			if (ProgrammeFactory.containsId(idProg)) {
				
				//On récupère les objets
				Programme p = (Programme)ProgrammeFactory.getById(idProg);
				
				//On ajoute le document au programme
				if (p.retirerDocument(Long.parseLong(calage))) {
				
					logger.info("Document retiré du programme "+idProg);
					return Boolean.toString(true);
					
				} else {
					logger.info("Document non retiré du programme "+idProg);
					return Boolean.toString(false);
				}
			}
			
			//Sinon, ajout refusé
			logger.info("Le document n'a pas été retiré au programme "+idProg);
			return Boolean.toString(false);
		}
		//Sinon, ajout refusé
		logger.info("Permission non accordée. Le document non retiré");
		return Boolean.toString(false);
	}
	
	/**
	 * Lister les programmes disponibles
	 * @param String login
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerProgramme(String login) {

		logger.info("Liste des programmes disponibles");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "listerProgramme")) {
		
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
		// Sinon, opération refusée
		logger.info("Permission non accordée. Programmes non listés");
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

		logger.info("Recherche d'un programme");

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
		// Sinon, opération refusée
		logger.info("Permission non accordée. Recherche non effectuées");
		return null;
	}

	/**
	 * Suppression d'un programme
	 * @param String login
	 * @param String id
	 * @return String
	 */
	public String supprimerProgramme(String login, String id) {
		logger.info("Suppression du programme "+id);

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
					
					logger.info("Programme '"+id+"' supprimé");
					return Boolean.toString(true);
				}
				
				//Sinon, suppression échouée
				logger.info("Programme '"+id+"' non supprimé");
				return Boolean.toString(false);
			}
			
			//Sinon, programme introuvable
			logger.info("Programme '"+id+"' introuvable");
			return Boolean.toString(false);
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Programme non supprimé");
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

		logger.info("Modification du programme "+id);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "modifierProgramme")) {
		
			//On vérifie que le programme existe
			if (ProgrammeFactory.containsId(id)) {
				
				//On récupère le prog
				Programme p = (Programme)ProgrammeFactory.getById(id);
				
				//On modifie le programme
				if (p.modifier(titre, thematique)) {
				
					logger.info("Programme '"+id+"' modifié");
					return Boolean.toString(true);
				}
				
				//Sinon, création a échoué
				logger.info("Programme '"+id+"' non modifié");
				return Boolean.toString(false);
			}
			
			//Sinon, opération refusée
			logger.info("Programme '"+id+"' non trouvé");
			return Boolean.toString(false);
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Programme non modifié");
		return Boolean.toString(false);
	}
	
	/**
	 * Informations sur un programme
	 * @param String login
	 * @param Strign id
	 * @return Dictionary
	 */
	public Dictionary infoProgramme(String login, String id) {
		
		logger.info("Infos sur un programme");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "infoProgramme")) {
		
			//On vérifie que le programme existe
			if (ProgrammeFactory.containsId(id)) {
				Programme p = (Programme)ProgrammeFactory.getById(id);
				return p.getAttributesDictionary();
			}
			else {
				logger.info("Programme indisponible");
				return null;
			}
		}
		
		// Sinon, refusé
		logger.info("Permission non accordée. Programme non affiché");
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

		logger.info("Création du canal "+nom);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "creerCanal")) {
		
			//On vérifie que le canal n'existe pas
			if (CanalFactory.getByNom(nom) == null) {
				
				//On crée le canal
				@SuppressWarnings("unused")
				Canal c = CanalFactory.create(nom, Integer.parseInt(utilMax));
				
				//On l'ajoute à la liste des canaux du système
				//canaux.put(c.getId(), c);
				
				logger.info("Canal '"+nom+"' créé");
				return Boolean.toString(true);
				
			}
			
			//Sinon, opération refusée
			logger.info("Canal '"+nom+"' non créé");
			return Boolean.toString(false);
		}
		//Sinon, opération refusée
		logger.info("Permission non accordée. Canal non créé");
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

		logger.info("Planification du programme "+idProg+" sur le canal "+idCanal);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "planifierProgramme")) {
		
			//On vérifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal) && ProgrammeFactory.containsId(idProg)) {
				
				//On récupère les objets
				Canal c = (Canal)CanalFactory.getById(idCanal);
				Programme p = (Programme)ProgrammeFactory.getById(idProg);
				
				if (p.getDuree() >= Long.parseLong(prefs.node("streaming").get("dureeProgMin", null))) {
				
					//On planifie le programme
					if (c.planifierProgramme(p, Integer.parseInt(jour), Integer.parseInt(mois), Integer.parseInt(annee), Integer.parseInt(heure), Integer.parseInt(minute), Integer.parseInt(seconde))) {
					
						logger.info("Programme "+idProg+" planifié sur le canal "+idCanal);
						return Boolean.toString(true);
					}
					else {
						logger.info("Programme "+idProg+" non planifié sur le canal "+idCanal);
						return Boolean.toString(false);
					}
				}
				else {
					logger.info("Programme "+idProg+"trop court. Non planifié sur le canal "+idCanal);
					return Boolean.toString(false);
				}
				
			}
			
			//Sinon, ajout refusé
			logger.info("Le programme "+idProg+" n'a pas été plnifié sur le canal "+idCanal);
			return Boolean.toString(false);
		}
		
		//Sinon, ajout refusé
		logger.info("Permission non accordée. Programme non planifié");
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

		logger.info("Déplanification d'un programme sur le canal "+idCanal);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "deplanifierProgramme")) {
		
			//On vérifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal)) {
				
				//On récupère les objets
				Canal c = (Canal)CanalFactory.getById(idCanal);
				
				//On deplanifie le programme
				if (c.annulerPlanification(Long.parseLong(calage))) {
				
					logger.info("Programme déplanifié du canal "+idCanal);
					return Boolean.toString(true);
				}
				else {
					logger.info("Programme non déplanifié du le canal "+idCanal);
					return Boolean.toString(false);
				}
				
			}
			
			//Sinon, ajout refusé
			logger.info("Le programme n'a pas été déplanifié sur le canal "+idCanal);
			return Boolean.toString(false);
		}
		
		//Sinon, ajout refusé
		logger.info("Permission non accordée. Programme non déplanifié");
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
		
		logger.info("Diffusion du programme "+idProg+" sur le canal "+idCanal);
		
		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "diffuserProgramme")) {
		
			//On vérifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal) && ProgrammeFactory.containsId(idProg)) {
				
				//On récupère les objets
				Canal c = (Canal)CanalFactory.getById(idCanal);
				Programme p = (Programme)ProgrammeFactory.getById(idProg);
				
				if (p.getDuree() >= Long.parseLong(prefs.node("streaming").get("dureeProgMin", null))) {
				
					//On diffuse le programme
					if (!c.isRTPstarted()) c.createRTPServer(ipStreaming, portStreaming++, prefs.node("streaming").get("publicite", null));
					c.diffuserProgramme(p);
					
					logger.info("Programme "+idProg+" en diffusion sur le canal "+idCanal);
					return Boolean.toString(true);
				}
				else {
					//Sinon, ajout refusé
					logger.info("Programme trop court. La diffusion du programme "+idProg+" n'a pas été lancée sur le canal "+idCanal);
					return Boolean.toString(false);
				}
				
			}
			
			//Sinon, ajout refusé
			logger.info("La diffusion du programme "+idProg+" n'a pas été lancée sur le canal "+idCanal);
			return Boolean.toString(false);
		}
		
		//Sinon, ajout refusé
		logger.info("Permission non accordée. Diffusion non lancée");
		return Boolean.toString(false);
	}
	
	/**
	 * Ecouter un canal (retourne l'URL du canal)
	 * @param String login
	 * @param String idCanal
	 * @return String
	 */
	public String ecouterCanal(String login, String idCanal) {
		logger.info("Ecoute du canal "+idCanal);
		
		//	On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "ecouterCanal")) {
		
			//On vérifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal)) {
				
				//On retire l'auditeur des canaux
				Enumeration canaux = CanalFactory.getInstances().elements();
				while (canaux.hasMoreElements()) {
					Canal c = (Canal)canaux.nextElement();
					c.deconnecterAuditeur(login);
				}
				
				//On récupère l'objet canal sélectionné
				Canal c = (Canal)CanalFactory.getById(idCanal);
				
				//S'il y a encore de la place sur le canal
				if (c.getAuditeurs().size() < c.getUtilMax()) {
					//On le connecte au canal sélectionné
					c.connecterAuditeur(login);
					
					logger.info("Construction de l'url du canal "+idCanal);
					String url = c.getUrlStreaming();
					logger.info("URL: "+url);
					return url;
				}
				else {
					logger.info("Canal plein");
					return "";
				}
			}
			
			//Sinon, ajout refusé
			logger.info("Canal "+idCanal+" inconnu");
			return "";
		}
		
		//Sinon, ajout refusé
		logger.info("Permission non accordée.");
		return null;
	}
	
	/**
	 * Lancer la diffusion d'un canal
	 * @param String login
	 * @param String idCanal
	 * @return String
	 */
	public String startCanal(String login, String idCanal) {
		logger.info("Ecoute du canal "+idCanal);
		
		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "startCanal")) {
		
			//On vérifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal)) {
				
				//On récupère l'objet canal
				Canal c = (Canal)CanalFactory.getById(idCanal);
				if (!c.isRTPstarted()) c.createRTPServer(ipStreaming, portStreaming++, prefs.node("streaming").get("publicite", null));
				c.startDiffusion();
				return Boolean.toString(true);
			}
			
			//Sinon, ajout refusé
			logger.info("Canal "+idCanal+" inconnu");
			return Boolean.toString(false);
		}
		
		//Sinon, ajout refusé
		logger.info("Permission non accordée. Canal non démarré");
		return Boolean.toString(false);
			
	}
	
	/**
	 * Stoppe la diffusion d'un canal
	 * @param String login
	 * @param String idCanal
	 * @return String
	 */
	public String stopCanal(String login, String idCanal) {
		logger.info("Stopper le canal "+idCanal);
		
		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "stopCanal")) {
		
			//On vérifie que le canal et le programme existent
			if (CanalFactory.containsId(idCanal)) {
				
				//On récupère l'objet canal
				Canal c = (Canal)CanalFactory.getById(idCanal);
				c.stopDiffusion();
				return Boolean.toString(true);
			}
			
			//Sinon, ajout refusé
			logger.info("Canal "+idCanal+" inconnu");
			return Boolean.toString(false);
		}
		
		//Sinon, ajout refusé
		logger.info("Permission non accordée. Canal non arrété");
		return Boolean.toString(false);
	}
	
	/**
	 * Lister les canaux disponibles
	 * @param String login
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerCanal(String login) {

		logger.info("Liste des canaux disponibles");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "listerCanal")) {
		
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
		// Sinon, opération refusée
		logger.info("Permission non accordée. Canaux non listés");
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

		logger.info("Recherche d'un canal");

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
					|| (nom.length()>0 && c.getNom().contains(nom))
					|| (utilMax.length()>0 && c.getUtilMax()==Integer.parseInt(utilMax))) {
					
					vCanaux.addElement(c.getAttributesDictionary());
				}
			}
			
			return vCanaux;
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Recherche non effectuées");
		return null;
	}
	
	/**
	 * Suppression d'un canal
	 * @param String login
	 * @param String id
	 * @return String
	 */
	public String supprimerCanal(String login, String id) {
		logger.info("Suppression du canal "+id);

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
					
					logger.info("Canal '"+id+"' supprimé");
					return Boolean.toString(true);
				}
				
				//Sinon, suppression échouée
				logger.info("Canal '"+id+"' non supprimé");
				return Boolean.toString(false);
			}
			
			//Sinon, canal introuvable
			logger.info("Canal '"+id+"' introuvable");
			return Boolean.toString(false);
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Canal non supprimé");
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

		logger.info("Modification du canal "+id);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "modifierCanal")) {
		
			//On vérifie que le canal existe
			if (CanalFactory.containsId(id)) {
				
				//On récupère le canal
				Canal c = (Canal)CanalFactory.getById(id);
				
				//On modifie le canal
				if (c.modifier(nom, Integer.parseInt(utilMax))) {
				
					logger.info("Canal '"+id+"' modifié");
					return Boolean.toString(true);
				}
				
				//Sinon, création a échoué
				logger.info("Canal '"+id+"' non modifié");
				return Boolean.toString(false);
			}
			
			//Sinon, opération refusée
			logger.info("Canal '"+id+"' non trouvé");
			return Boolean.toString(false);
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Canal non modifié");
		return Boolean.toString(false);
	}
	
	/**
	 * Informations sur un canal
	 * @param String login
	 * @param Strign id
	 * @return Dictionary
	 */
	public Dictionary infoCanal(String login, String id) {
		
		logger.info("Infos sur un canal");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "infoCanal")) {
		
			//On vérifie que le canal existe
			if (CanalFactory.containsId(id)) {
				Canal c = (Canal)CanalFactory.getById(id);
				return c.getAttributesDictionary();
			}
			else {
				logger.info("Canal indisponible");
				return null;
			}
		}
		// Sinon, refusé
		logger.info("Permission non accordée. Canal non affiché");
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

		logger.info("Création du contrat");
		
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
				
				logger.info("Contrat créé");
				return Boolean.toString(true);
				
			}
			
			//Sinon, opération refusée
			logger.info("Contrat non créé");
			return Boolean.toString(false);
		}
		//Sinon, opération refusée
		logger.info("Permission non accordée. Contrat non créé");
		return Boolean.toString(false);
	}
	
	/**
	 * Recherche d'un contrat
	 * @param login
	 * @param id
	 * @param titre
	 * @param jourSignature
	 * @param moisSignature
	 * @param anneeSignature
	 * @param jourExpiration
	 * @param moisExpiration
	 * @param anneeExpiration
	 * @param idContractant
	 * @param modeReglement
	 * @param type
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector rechercherContrat(String login, String id, String titre, String jourSignature, 
			String moisSignature, String anneeSignature, String jourExpiration, String moisExpiration, 
			String anneeExpiration, String idContractant, String modeReglement, String type) {

		logger.info("Recherche d'un contrat");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "rechercherContrat")) {
			
			//Vecteur de contrats à retourner
			Vector vContrat = new Vector();
		
			Enumeration listeContrat = ContratFactory.getInstances().elements();
			Contrat c;
			
			while (listeContrat.hasMoreElements()) {
				c = (Contrat)listeContrat.nextElement();
				
				int jourS = c.getDateSignature().get(GregorianCalendar.DAY_OF_MONTH);
				int moisS = c.getDateSignature().get(GregorianCalendar.MONTH)+1;
				int anneeS = c.getDateSignature().get(GregorianCalendar.YEAR);
				int jourE = c.getDateExpiration().get(GregorianCalendar.DAY_OF_MONTH);
				int moisE = c.getDateExpiration().get(GregorianCalendar.MONTH)+1;
				int anneeE = c.getDateExpiration().get(GregorianCalendar.YEAR);
				
				if ((id.length()>0 && c.getId().contains(id))
					|| (titre.length()>0 && c.getTitre().contains(titre))
					|| (jourSignature.length()>0 && jourS==Integer.parseInt(jourSignature))
					|| (moisSignature.length()>0 && moisS==Integer.parseInt(moisSignature))
					|| (anneeSignature.length()>0 && anneeS==Integer.parseInt(anneeSignature))
					|| (jourExpiration.length()>0 && jourE==Integer.parseInt(jourExpiration))
					|| (moisExpiration.length()>0 && moisE==Integer.parseInt(moisExpiration))
					|| (anneeExpiration.length()>0 && anneeE==Integer.parseInt(anneeExpiration))
					|| (idContractant.length()>0 && c.getContractant().getId().contains(idContractant))
					|| (modeReglement.length()>0 && c.getModeReglement().contains(modeReglement))
					|| (type.length()>0 && c.getType().contains(type))) {
					
					vContrat.addElement(c.getAttributesDictionary());
				}
			}
			
			return vContrat;
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Recherche non effectuées");
		return null;
	}
	
	/**
	 * Lister les contrats disponibles
	 * @param String login
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerContrat(String login) {

		logger.info("Liste des contrats disponibles");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "listerContrat")) {
		
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
		// Sinon, opération refusée
		logger.info("Permission non accordée. Contrats non listés");
		return null;
	}
	
	/**
	 * Informations sur un contrat
	 * @param String login
	 * @param Strign id
	 * @return Dictionary
	 */
	public Dictionary infoContrat(String login, String id) {
		
		logger.info("Infos sur un contrat");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "infoContrat")) {
		
			//On vérifie que le canal existe
			if (ContratFactory.containsId(id)) {
				Contrat c = (Contrat)ContratFactory.getById(id);
				return c.getAttributesDictionary();
			}
			else {
				logger.info("Contrat indisponible");
				return null;
			}
		}
		// Sinon, refusé
		logger.info("Permission non accordée. Contrat non affiché");
		return null;
	}
	
	/**
	 * Suppression d'un contrat
	 * @param String login
	 * @param String id
	 * @return String
	 */
	public String supprimerContrat(String login, String id) {
		logger.info("Suppression du contrat "+id);

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
					
					logger.info("Contrat '"+id+"' supprimé");
					return Boolean.toString(true);
				}
				
				//Sinon, suppression échouée
				logger.info("Contrat '"+id+"' non supprimé");
				return Boolean.toString(false);
			}
			
			//Sinon, canal introuvable
			logger.info("Contrat '"+id+"' introuvable");
			return Boolean.toString(false);
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Contrat non supprimé");
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

		logger.info("Modification du contrat "+id);

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
				
					logger.info("Contrat '"+id+"' modifié");
					return Boolean.toString(true);
				}
				
				//Sinon, création a échoué
				logger.info("Contrat '"+id+"' non modifié");
				return Boolean.toString(false);
			}
			
			//Sinon, opération refusée
			logger.info("Contrat '"+id+"' non trouvé");
			return Boolean.toString(false);
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Contrat non modifié");
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
		logger.info("Ajout du document "+idDoc+" au contrat "+idContrat);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "ajouterDocumentContrat")) {
		
			//On vérifie que le contrat et le document existent
			if (ContratFactory.containsId(idContrat) && DocumentFactory.containsId(idDoc)) {
				
				//On récupère les objets
				Contrat c = (Contrat)ContratFactory.getById(idContrat);
				Document d = (Document)DocumentFactory.getById(idDoc);
				
				//On ajoute le contractant au contrat
				c.ajouterDocument(d);
				
				logger.info("Document "+idDoc+" ajouté au contrat "+idContrat);
				return Boolean.toString(true);
				
			}
			
			//Sinon, ajout refusé
			logger.info("Le document "+idDoc+" n'a pas été ajouté au contrat "+idContrat);
			return Boolean.toString(false);
		}
		//Sinon, ajout refusé
		logger.info("Permission non accordée. Document non ajouté");
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
		logger.info("Retirer le document "+idDoc+ " du contrat "+idContrat);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "retirerDocumentContrat")) {
		
			//On vérifie que le programme et le document existent
			if (ContratFactory.containsId(idContrat) && DocumentFactory.containsId(idDoc)) {
				
				//On récupère les objets
				Contrat c = (Contrat)ContratFactory.getById(idContrat);
				
				//On retire le document du contrat
				c.retirerDocument(idDoc);	
							
				logger.info("Document retiré du contrat "+idContrat);
				return Boolean.toString(true);
				
			}
			
			//Sinon, ajout refusé
			logger.info("Le document n'a pas été retiré du contrat "+idContrat);
			return Boolean.toString(false);
		}
		//Sinon, ajout refusé
		logger.info("Permission non accordée. Document non retiré");
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

		logger.info("Création du contractant");
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "creerContractant")) {
		
			//On vérifie que le canal n'existe pas
			if (ContractantFactory.getByNom(nom) == null) {
				
				//On crée le canal
				@SuppressWarnings("unused")
				Contractant c = ContractantFactory.create(nom, adresse, codePostal, ville, telephone, fax, mail, type);
				
				//On l'ajoute à la liste des contractants du système
				//contractants.put(c.getId(), c);
				
				logger.info("Contractant créé");
				return Boolean.toString(true);
				
			}
			
			//Sinon, opération refusée
			logger.info("Contractant non créé");
			return Boolean.toString(false);
		}
		//Sinon, opération refusée
		logger.info("Permission non accordée. Contractant non créé");
		return Boolean.toString(false);
	}	
	
	/**
	 * Recherche d'un contractant
	 * @param login
	 * @param id
	 * @param nom
	 * @param adresse
	 * @param codePostal
	 * @param ville
	 * @param telephone
	 * @param fax
	 * @param mail
	 * @param type
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector rechercherContractant(String login, String id, String nom, String adresse,
			String codePostal, String ville, String telephone, String fax,
			String mail, String type) {

		logger.info("Recherche d'un contractant");
		
		//Vecteur de contractants à retourner
		Vector vContractant = new Vector();
		
		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "rechercherContractant")) {
			
			Enumeration listeContractant = ContractantFactory.getInstances().elements();
			Contractant c;
			
			while (listeContractant.hasMoreElements()) {
				c = (Contractant)listeContractant.nextElement();
				
				if ((id.length()>0 && c.getId().contains(id))
					|| (nom.length()>0 && c.getNom().contains(nom))
					|| (adresse.length()>0 && c.getAdresse().contains(adresse))
					|| (codePostal.length()>0 && c.getCodePostal().contains(codePostal))
					|| (ville.length()>0 && c.getVille().contains(ville))
					|| (telephone.length()>0 && c.getTelephone().contains(telephone))
					|| (fax.length()>0 && c.getFax().contains(fax))
					|| (mail.length()>0 && c.getMail().contains(mail))
					|| (type.length()>0 && c.getType().contains(type))) {
					
					vContractant.addElement(c.getAttributesDictionary());
				}
			}
			logger.info("Recherche effectuée : "+vContractant.size()+" résultat(s)");
			return vContractant;
		} else {
			// Sinon, opération refusée
			logger.info("Permission non accordée. Recherche non effectuée");
			//return null;
			return vContractant;
		}
	}	
	
	/**
	 * Lister les contractants disponibles
	 * @param String login
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerContractant(String login) {

		logger.info("Liste des contractants disponibles");

		//On crée le vecteur
		Vector vContractants = new Vector();
		
		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "listerContractant")) {
			
			//On récupère la liste des documents
			Enumeration listeContractants = ContractantFactory.getInstances().elements();
			
			while (listeContractants.hasMoreElements()) {
				Contractant c = (Contractant)listeContractants.nextElement();
				vContractants.addElement(c.getAttributesDictionary());
			}
			
			return vContractants;
		} else {
			// Sinon, opération refusée
			logger.info("Permission non accordée. Contractants non listés");
			return vContractants;
		}
	}
	
	/**
	 * Informations sur un contractant
	 * @param String login
	 * @param Strign id
	 * @return Dictionary
	 */
	public Dictionary infoContractant(String login, String id) {
		
		logger.info("Infos sur un contractant");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "infoContractant")) {
		
			//On vérifie que le canal existe
			if (ContractantFactory.containsId(id)) {
				Contractant c = (Contractant)ContractantFactory.getById(id);
				return c.getAttributesDictionary();
			}
			else {
				logger.info("Contractant indisponible");
				return null;
			}
		}
		// Sinon, refusé
		logger.info("Permission non accordée. Contractant non affiché");
		return null;
	}
	
	/**
	 * Suppression d'un contractant
	 * @param String login
	 * @param String id
	 * @return String
	 */
	public String supprimerContractant(String login, String id) {
		logger.info("Suppression du contractant "+id);

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
					
					logger.info("Contractant '"+id+"' supprimé");
					return Boolean.toString(true);
				}
				
				//Sinon, suppression échouée
				logger.info("Contractant '"+id+"' non supprimé");
				return Boolean.toString(false);
			}
			
			//Sinon, canal introuvable
			logger.info("Contractant '"+id+"' introuvable");
			return Boolean.toString(false);
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Contractant non supprimé");
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

		logger.info("Modification du contractant "+id);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "modifierContractant")) {
		
			//On vérifie que le canal existe
			if (ContractantFactory.containsId(id)) {
				
				//On récupère le canal
				Contractant c = (Contractant)ContractantFactory.getById(id);
				
				//On modifie le canal
				if (c.modifier(nom, adresse, codePostal, ville, telephone, fax, mail, type)) {
				
					logger.info("Contractant '"+id+"' modifié");
					return Boolean.toString(true);
				}
				
				//Sinon, création a échoué
				logger.info("Contractant '"+id+"' non modifié");
				return Boolean.toString(false);
			}
			
			//Sinon, opération refusée
			logger.info("Contractant '"+id+"' non trouvé");
			return Boolean.toString(false);
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Contractant non modifié");
		return Boolean.toString(false);
	}
	
//PERMISSION
//*******************************************************

	/**
	 * Création d'une permission
	 * @param String id
	 * @param String libelle
	 * @return String
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public String creerPermission(String login, String id, String libelle) throws SQLException {

		logger.info("Création de la permission");
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "creerPermission")) {
		
			//On vérifie que le permission n'existe pas
			if (PermissionFactory.getById(id) == null) {
				
				//On crée le canal
				@SuppressWarnings("unused")
				Permission p = PermissionFactory.create(id, libelle);
					
				logger.info("Permission créée");
				return Boolean.toString(true);
				
			}
			
			//Sinon, opération refusée
			logger.info("Permission non créée");
			return Boolean.toString(false);
		}
		//Sinon, opération refusée
		logger.info("Permission non accordée. Permission non créée");
		return Boolean.toString(false);
	}	
	
	@SuppressWarnings("unchecked")
	public Vector rechercherPermission(String login, String id, String libelle) {

		logger.info("Recherche d'une permission");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "rechercherPermission")) {
			
			//Vecteur de contractants à retourner
			Vector vPerm = new Vector();
		
			Enumeration listePerm = PermissionFactory.getInstances().elements();
			Permission p;
			
			while (listePerm.hasMoreElements()) {
				p = (Permission)listePerm.nextElement();
				
				if ((id.length()>0 && p.getId().contains(id))
					|| (libelle.length()>0 && p.getLibelle().contains(libelle))) {
					
					vPerm.addElement(p.getAttributesDictionary());
				}
			}
			
			return vPerm;
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Recherche non effectuées");
		return null;
	}	
	
	
	
	/**
	 * Lister les permissions disponibles
	 * @param String login
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerPermission(String login) {

		logger.info("Liste des permissions disponibles");
		
		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "listerPermission")) {
		
			//On crée le vecteur
			Vector vPermissions = new Vector();
			
			//On récupère la liste des documents
			Enumeration listePermissions = PermissionFactory.getInstances().elements();
			
			while (listePermissions.hasMoreElements()) {
				Permission p = (Permission)listePermissions.nextElement();
				vPermissions.addElement(p.getAttributesDictionary());
			}
			
			return vPermissions;
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Permissions non listées");
		return null;
	}
		
	/**
	 * Informations sur une permission
	 * @param String login
	 * @param Strign id
	 * @return Dictionary
	 */
	public Dictionary infoPermission(String login, String id) {
			
		logger.info("Infos sur une permission");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "infoPermission")) {
		
			//On vérifie que la permission existe
			if (PermissionFactory.containsId(id)) {
				Permission p = (Permission)PermissionFactory.getById(id);
				return p.getAttributesDictionary();
			}
			else {
				logger.info("Permission indisponible");
				return null;
			}
		}
		// Sinon, refusé
		logger.info("Permission non accordée. Permission non affichée");
		return null;
	}
		
	/**
	 * Suppression d'une permission
	 * @param String login
	 * @param String id
	 * @return String
	 */
	public String supprimerPermission(String login, String id) {
		logger.info("Suppression de la permission "+id);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "supprimerPermission")) {
		
			//On vérifie que la permission existe
			if (PermissionFactory.containsId(id)) {
				
				//On récupère l'objet Permission
				Permission p = (Permission)PermissionFactory.getById(id);

				//Si la Permission a bien été supprimé
				if (p.supprimer()) {
					
					logger.info("Permission '"+id+"' supprimée");
					return Boolean.toString(true);
				}
					
				//Sinon, suppression échouée
				logger.info("Permission '"+id+"' non supprimée");
				return Boolean.toString(false);
			}
			
			//Sinon, Permission introuvable
			logger.info("Permission '"+id+"' introuvable");
			return Boolean.toString(false);
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Permission non supprimée");
		return Boolean.toString(false);
	}
		
	/**
	 * Modification d'une permission
	 * @param String login
	 * @param String id
	 * @param String libelle
	 * @return String
	 */
	public String modifierPermission(String login, String id, String libelle) throws SQLException {

		logger.info("Modification de la permission "+id);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "modifierPermission")) {
			
			//On vérifie que la permission existe
			if (PermissionFactory.containsId(id)) {
					
				//On récupère la Permission
				Permission p = (Permission)PermissionFactory.getById(id);
					
				//On modifie la Permission
				if (p.setLibelle(libelle)) {
					
					logger.info("Permission '"+id+"' modifiée");
					return Boolean.toString(true);
				}
					
				//Sinon, modification a échoué
				logger.info("Permission '"+id+"' non modifiée");
				return Boolean.toString(false);
			}
				
			//Sinon, modification refusée
			logger.info("Permission '"+id+"' non trouvée");
			return Boolean.toString(false);
		}
		// Sinon, modification refusée
		logger.info("Permission non accordée. Permission non modifiée");
		return Boolean.toString(false);
	}
		
// ROLE
//*******************************************************

	/**
	 * Création d'un rôle
	 * @param String id
	 * @return String
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public String creerRole(String login, String id) throws SQLException {

		logger.info("Création du rôle");
					
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "creerRole")) {
				
			//On vérifie que le permission n'existe pas
			if (!RoleFactory.containsId(id)) {
							
				//On crée le canal
				@SuppressWarnings("unused")
				Role r = RoleFactory.create(id);
						
				logger.info("Rôle créé");
				return Boolean.toString(true);
						
			}
						
			//Sinon, opération refusée
			logger.info("Rôle non créé");
			return Boolean.toString(false);
		}
		//Sinon, opération refusée
		logger.info("Permission non accordée. Rôle non créé");
		return Boolean.toString(false);
	}	
				
	/**
	 * Lister les rôles disponibles
	 * @param String login
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerRole(String login) {

		logger.info("Liste des rôles disponibles");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "listerRole")) {
					
			//On crée le vecteur
			Vector vRoles = new Vector();
						
			//On récupère la liste des documents
			Enumeration listeRoles = RoleFactory.getInstances().elements();
				
			while (listeRoles.hasMoreElements()) {
				Role r = (Role)listeRoles.nextElement();
				vRoles.addElement(r.getAttributesDictionary());
			}
						
			return vRoles;
		}
		// Sinon, opération refusée
		logger.info("Permission non accordée. Rôles non listés");
		return null;
	}
				
	/**
	 * Informations sur un rôle
	 * @param String login
	 * @param Strign id
	 * @return Dictionary
	 */
	public Dictionary infoRole(String login, String id) {
					
		logger.info("Infos sur un rôle");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "infoRole")) {
		
			//On vérifie que le rôle existe
			if (RoleFactory.containsId(id)) {
				Role r = (Role)RoleFactory.getById(id);
				return r.getAttributesDictionary();
			}
			else {
				logger.info("Rôle indisponible");
				return null;
			}
		}
		// Sinon, refusé
		logger.info("Permission non accordée. Rôle non affiché");
		return null;
	}
				
	/**
	 * Suppression d'un rôle
	 * @param String login
	 * @param String id
	 * @return String
	 */
	public String supprimerRole(String login, String id) {
		logger.info("Suppression du rôle "+id);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "supprimerRole")) {
			
			//On vérifie que le rôle existe
			if (RoleFactory.containsId(id)) {
						
				//On récupère l'objet Role
				Role r = (Role)RoleFactory.getById(id);

				//Si le Role a bien été supprimé
				if (r.supprimer()) {
								
					logger.info("Rôle '"+id+"' supprimé");
					return Boolean.toString(true);
				}
							
				//Sinon, suppression échouée
				logger.info("Rôle '"+id+"' non supprimé");
				return Boolean.toString(false);
			}
						
			//Sinon, Role introuvable
			logger.info("Rôle '"+id+"' introuvable");
			return Boolean.toString(false);
		}
		// Sinon, suppression refusée
		logger.info("Permission non accordée. Role non supprimé");
		return Boolean.toString(false);
	}
	
	/**
	 * Attribuer une permission à un rôle
	 * @param String login
	 * @param String permission
	 * @return String
	 */
	public String ajouterPermissionRole(String login, String role, String permission) throws NamingException {
		
		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "ajouterPermissionRole")) {
			
			//On vérifie que le rôle et la permission existent
			if (RoleFactory.containsId(role) && PermissionFactory.containsId(permission)) {
				
				//On récupère le rôle
				Role r = RoleFactory.getById(role);
				
				//On récupère la permission
				Permission p = PermissionFactory.getById(permission);

				//On attribue la permission
				r.ajouterPermission(p);
				
				logger.info("Permission ajoutée au rôle '"+role+"'");
				return Boolean.toString(true);
			}
			else {
				//Sinon, opération refusée
				logger.info("Permission ou rôle introuvable. Permission non ajoutée");
				return Boolean.toString(false);
			}
		} else {
			// Sinon, opération refusée
			logger.info("Permission non accordée. Permission non ajoutée");
			return Boolean.toString(false);
		}
			
	}
	
	/**
	 * Retirer une permission à un role
	 * @param String login
	 * @param String permission
	 * @return String
	 */
	public String retirerPermissionRole(String login, String role, String permission) throws NamingException {
		
		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "retirerPermissionRole")) {
			
			//On vérifie que le rôle et la permission existent
			if (RoleFactory.containsId(role) && PermissionFactory.containsId(permission)) {
			
				//On récupère le rôle
				Role r = RoleFactory.getById(role);
	
				//On retire la permission
				r.retirerPermission(permission);
				
				logger.info("Permission retirée au role '"+role+"'");
				return Boolean.toString(true);
			} else {
				// Sinon, opération refusée
				logger.info("Permission non accordée. Permission non retirée");
				return Boolean.toString(false);
			} 
		}else {
			// Sinon, opération refusée
			logger.info("Permission non accordée. Permission non retirée");
			return Boolean.toString(false);
		}
			
	}
}

/**
 * Thread de "ping" sur les clients
 */
class PingTask extends TimerTask {
	/**
	 *  Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(PingTask.class);
	
	private String login;
	private String ip;
	private int port;
	private XmlRpcClient clientXML;
	private Systeme sys;
	
	public PingTask(String login, String ip, int port, Systeme sys) {
		logger.error("TIMER: Programmation de la tâche "+login+"@"+ip);
		this.login = login;
		this.ip = ip;
		this.port = port;
		this.sys = sys;
		try {
			XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");
			this.clientXML = new XmlRpcClient("http://" + ip + ":" + (port+1));
		} catch (ClassNotFoundException e) {
			logger.info("ERREUR: Impossible de localiser le pilote Sax");
		} catch (MalformedURLException e) {
			logger.info("ERREUR: URL non conforme au format du serveur XML-RPC");
		}
	}
	
	public void run() {
		try {
			logger.error("TIMER: Lancement de la tâche "+login+"@"+ip);
			
			//Création de la requête
			Vector params = new Vector();

			// Adresse la requête et affiche les résultats
			String result = (String) clientXML.execute("TestXML.testConnectXML", params);		
		
		} catch (Exception e) {
			logger.info("ERREUR: ", e);
			//sys.deconnexion(login);
		}
	}
}