package proto.serveur;


import java.sql.SQLException;
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
	private int portXML;
	
	
	/**
	 * Utilisateurs connect�s aux syst�me
	 */
	private Hashtable utilisateurs = new Hashtable();
	
	
	/**
	 * Canaux de diffusion
	 */
	private Hashtable canaux = new Hashtable();
	
	/**
	 * Documents
	 */
	private Hashtable documents = new Hashtable();
	
	/**
	 * Programmes
	 */
	private Hashtable programmes = new Hashtable();
	
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
		String authldap = prefs.node("ldap").get("auth", null);
		String loginldap = prefs.node("ldap").get("login", null);
		String pwdldap = prefs.node("ldap").get("pwd", null);
		String role = prefs.node("ldap").get("role", null);
		
		String urlldap = typeldap + "://" + ipldap + ":" + portldap + "/" + baseNameldap;
		
		System.err.println("LDAP Driver: " + driverldap);
		System.err.println("LDAP URL: " + urlldap);
		System.err.println("LDAP Login: " + loginldap);
		System.err.println("LDAP Pwd: " + pwdldap);
		
		ldap = Ldap.getInstance();
		ldap.openLdap(driverldap, urlldap, authldap, loginldap, pwdldap, role, baseNameldap);
	
		//R�cup�ration des information du serveur SMTP
		this.host = prefs.node("smtp").get("host", null);
		this.port = prefs.node("smtp").get("port", null);
		this.from = prefs.node("smtp").get("from", null);
		
		System.err.println("Adresse du serveur SMTP : " + host);
		System.err.println("Port du serveur SMTP : " + port);
		System.err.println("Mail envoyeur depuis le serveur SMTP : " + from);
		
		//On initialise les listes de canaux, programmes, documents
		this.canaux = Canal.getAll();
		this.programmes = Programme.getAll();
		this.documents = Document.getAll();
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
	public String connexion(String login, String pwd) throws NamingException {
		
		System.out.println("Connexion de l'utilisateur "+ login);
		
		//On v�rifie que l'utilisateur n'est pas d�j� connect�
		if (!utilisateurs.containsKey(login)) {
		
			//On v�rifie que le couple login/pwd existe
			//if (Utilisateur.verifierPwd(pwd, login)) {
				
				//On r�cup�re l'utilisateur depuis la base
				Utilisateur util = Utilisateur.getByLogin(login);
				
				//On connecte l'utilisateur
				util.connexion();
				
				//On l'ajoute � la liste des utilisateurs connect�s au syst�me
				utilisateurs.put(login, util);
				
				System.out.println("Utilisateur "+login+" connect�");
				return Boolean.toString(true);
				
			//}
			
			//Sinon, connexion refus�e
			//System.out.println("Utilisateur "+login+" non connect�");
			//return Boolean.toString(false);
		}
		
		//Sinon, connexion refus�e
		System.out.println("Utilisateur "+login+" d�j� connect�");
		return Boolean.toString(false);
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
	public String inscription(String login, String log, String pass, String role, String email, String nom, String prenom, String pays) throws NamingException, AddressException, MessagingException {
		
		System.out.println("Inscription de l'utilisateur "+log);

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "inscription")) {
		
			//On v�rifie que le login n'existe pas
			if (!Utilisateur.verifierLogin(log)) {
				
				//On cr�e le document
				Utilisateur u = Utilisateur.create(log, pass, nom, prenom, email, pays, role);
				
				//Si le document a bien �t� cr��
				if (u != null) {
					//On l'ajoute � la liste des documents du syst�me
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

	public void suppressionUtilisateur() {
		// your code here
	}

	public void rechercheUtilisateur() {
		// your code here
	}

	public void rechercherUtilisateur(String Login) {
		// your code here
	}

	public void selectionnerUtilisateur(String Login) {
		// your code here
	}

	public void supprimerUtilisateur(String Login) {
		// your code here
	}
	
	public boolean emailValide(String Email) {
		// your code here
		return false;
	}

	public void modifierUtilisateur() {
		// your code here
	}

	public void modifierUtil(String Login) {
		// your code here
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
	 * @return String
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	public String creerDocument(String login, String titre, String duree, String jour, String mois, String annee, String source, String langue, String genre, String fichier) throws NumberFormatException, SQLException {

		System.out.println("Cr�ation du document "+titre);

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "creerDocument")) {
		
			//On v�rifie que le document n'existe pas
			if (Document.getByTitre(titre) == null) {
				
				//On cr�e le document
				Document d = Document.create(titre, Integer.parseInt(duree), jour, mois, annee, source, langue, genre, fichier);
				
				//Si le document a bien �t� cr��
				if (d != null) {
					//On l'ajoute � la liste des documents du syst�me
					documents.put(d.getId(), d);
					
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
	public Vector listerDocuments(String login) {

		System.out.println("Liste des documents disponibles");

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "listerDocuments")) {
		
			//On cr�e le vecteur
			Vector vDocuments = new Vector();
			
			//On r�cup�re la liste des documents
			Enumeration listeDocs = documents.elements();
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
	 * @return Vector
	 */
	public Vector rechercherDocument(String login, String id, String titre, String duree, String jour, String mois, String annee, String source, String langue, String genre, String fichier) {

		System.out.println("Recherche d'un document");

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "rechercherDocument")) {
			
			//Vecteur de documents � retourner
			Vector vDocuments = new Vector();
		
			//On r�cup�re la liste des documents
			Enumeration listeDocs = documents.elements();
			Document d;
			
			while (listeDocs.hasMoreElements()) {
				d = (Document)listeDocs.nextElement();
				
				if ((id.length()>0 && d.getId().contains(id))
					|| (titre.length()>0 && d.getTitre().contains(titre))
					|| (genre.length()>0 && d.getGenre().contains(genre))
					|| (fichier.length()>0 && d.getFichier().contains(fichier))
					|| (source.length()>0 && d.getSource().contains(source))
					|| (langue.length()>0 && d.getLangue().contains(langue))
					|| (duree.length()>0 && Integer.toString(d.getDuree()).contains(duree))) {
					
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
			if (documents.containsKey(id)) {
				
				//On r�cup�re l'objet document
				Document d = (Document)documents.get(id);

				//Si le document a bien �t� supprim�
				if (d.supprimer()) {
					
					//On le retire de la liste des documents du syst�me
					documents.remove(id);
					
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

	public void ajouterDocument() {
		// your code here
	}

	public boolean documentExistant() {
		// your code here
		return false;
	}

	public void modifierDocument() {
		// your code here
	}

	public void modifierDoc(String Id_Doc) {
		// your code here
	}

	public void selectionnerDocument(String Id_Doc) {
		// your code here
	}

	public void rechercheDocument() {
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
	public String creerProgramme(String login, String titre, String thematique) throws SQLException {

		System.out.println("Cr�ation du programme "+titre);
		
		// On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "creerProgramme")) {
		
			//On v�rifie que le programme n'existe pas
			if (Programme.getByTitre(titre) == null) {
				
				//On cr�e le programme
				Programme p = Programme.create(titre, thematique);
				
				//On l'ajoute � la liste des programmes du syst�me
				programmes.put(p.getId(), p);
				
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
			if (programmes.containsKey(idProg) && documents.containsKey(idDoc)) {
				
				//On r�cup�re les objets
				Document d = (Document)documents.get(idDoc);
				Programme p = (Programme)programmes.get(idProg);
				
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
	 * Lister les programmes disponibles
	 * @param String login
	 * @return Vector
	 */
	public Vector listerProgrammes(String login) {

		System.out.println("Liste des programmes disponibles");

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "listerProgrammes")) {
		
			//On cr�e le vecteur
			Vector vProgrammes = new Vector();
			
			//On r�cup�re la liste des documents
			Enumeration listeProgrammes = programmes.elements();
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
	public Vector rechercherProgramme(String login, String id, String titre, String thematique) {

		System.out.println("Recherche d'un programme");

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "rechercherProgramme")) {
			
			//Vecteur de programmes � retourner
			Vector vProgrammes = new Vector();
		
			//On r�cup�re la liste des programmes
			Enumeration listeProgs = programmes.elements();
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
			if (programmes.containsKey(id)) {
				
				//On r�cup�re l'objet document
				Programme p = (Programme)programmes.get(id);

				//Si le programme a bien �t� supprim�
				if (p.supprimer()) {
					
					//On le retire de la liste des programmes du syst�me
					programmes.remove(id);
					
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

	public void ConfirmerSupprimerProgramme(Boolean SuppressionProgramme) {
		// your code here
	}

	public void SaisirInfosProgramme(String Id_Prog, String Titre) {
		// your code here
	}

	public void RechercherDocument(String Id_Doc) {
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
	public String creerCanal(String login, String nom, String utilMax) throws NumberFormatException, SQLException {

		System.out.println("Cr�ation du canal "+nom);
		
		// On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "creerCanal")) {
		
			//On v�rifie que le canal n'existe pas
			if (Canal.getByNom(nom) == null) {
				
				//On cr�e le canal
				Canal c = Canal.create(nom, Integer.parseInt(utilMax));
				
				//On l'ajoute � la liste des canaux du syst�me
				canaux.put(c.getId(), c);
				
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
			if (canaux.containsKey(idCanal) && programmes.containsKey(idProg)) {
				
				//On r�cup�re les objets
				Canal c = (Canal)canaux.get(idCanal);
				Programme p = (Programme)programmes.get(idProg);
				
				//On planifie le programme
				//c.planifierProgramme(p);
				
				System.out.println("Programme "+idProg+" planifi� sur le canal "+idCanal);
				return Boolean.toString(true);
				
			}
			
			//Sinon, ajout refus�
			System.out.println("Le programme "+idProg+" n'a pas �t� palnifi� sur le canal "+idCanal);
			return Boolean.toString(false);
		}
		
		//Sinon, ajout refus�
		System.out.println("Permission non accord�e. Programme non planifi�");
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
			if (canaux.containsKey(idCanal) && programmes.containsKey(idProg)) {
				
				//On r�cup�re les objets
				Canal c = (Canal)canaux.get(idCanal);
				Programme p = (Programme)programmes.get(idProg);
				
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
			if (canaux.containsKey(idCanal)) {
				
				//On r�cup�re l'objet canal
				Canal c = (Canal)canaux.get(idCanal);
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
			if (canaux.containsKey(idCanal)) {
				
				//On r�cup�re l'objet canal
				Canal c = (Canal)canaux.get(idCanal);
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
			if (canaux.containsKey(idCanal)) {
				
				//On r�cup�re l'objet canal
				Canal c = (Canal)canaux.get(idCanal);
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
	public Vector listerCanaux(String login) {

		System.out.println("Liste des canaux disponibles");

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "listerCanaux")) {
		
			//On cr�e le vecteur
			Vector vCanaux = new Vector();
			
			//On r�cup�re la liste des documents
			Enumeration listeCanaux = canaux.elements();
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
	public Vector rechercherCanal(String login, String id, String nom, String utilMax) {

		System.out.println("Recherche d'un canal");

		//On v�rifie que l'utilisateur a la permission
		if (verifPermission(login, "rechercherCanal")) {
			
			//Vecteur de canaux � retourner
			Vector vCanaux = new Vector();
		
			//On r�cup�re la liste des programmes
			Enumeration listeCanx = canaux.elements();
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
			if (canaux.containsKey(id)) {
				
				//On r�cup�re l'objet canal
				Canal c = (Canal)canaux.get(id);

				//Si le document a bien �t� supprim�
				if (c.supprimer()) {
					
					//On le retire de la liste des canaux du syst�me
					canaux.remove(id);
					
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
	

	public void ValiderCreationCanal() {
		// your code here
	}

	public void PlannifierUnProgramme() {
		// your code here
	}

	public ArrayIndexOutOfBoundsException ListeDocs() {
		// your code here
		return null;
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

	public void RechercherUnCanal() {
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

	public void AjoutCanal(String Id_Canal) {
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

	public void ModifierUnProgramme() {
		// your code here
	}

	public void ModifierProgramme(String Id_Prog) {
		// your code here
	}

	public void ChoixModification(String Choix) {
		// your code here
	}

	public void VerifierDuree(int Duree) {
		// your code here
	}

	public void RechercherCanal(String Id_Canal, String NomCanal, int Flux_Max) {
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

	public void RechercherUnProgramme() {
		// your code here
	}

	public void ArretDiffusion(boolean Choix) {
		// your code here
	}

	public void AjouterDocumentProgramme() {
		// your code here
	}
}
