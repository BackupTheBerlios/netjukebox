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
	 * Utilisateurs connectés aux système
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
	
		//Récupération des information du serveur SMTP
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
	public String connexion(String login, String pwd) throws NamingException {
		
		System.out.println("Connexion de l'utilisateur "+ login);
		
		//On vérifie que l'utilisateur n'est pas déjà connecté
		if (!utilisateurs.containsKey(login)) {
		
			//On vérifie que le couple login/pwd existe
			//if (Utilisateur.verifierPwd(pwd, login)) {
				
				//On récupère l'utilisateur depuis la base
				Utilisateur util = Utilisateur.getByLogin(login);
				
				//On connecte l'utilisateur
				util.connexion();
				
				//On l'ajoute à la liste des utilisateurs connectés au système
				utilisateurs.put(login, util);
				
				System.out.println("Utilisateur "+login+" connecté");
				return Boolean.toString(true);
				
			//}
			
			//Sinon, connexion refusée
			//System.out.println("Utilisateur "+login+" non connecté");
			//return Boolean.toString(false);
		}
		
		//Sinon, connexion refusée
		System.out.println("Utilisateur "+login+" déjà connecté");
		return Boolean.toString(false);
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
	public String inscription(String login, String log, String pass, String role, String email, String nom, String prenom, String pays) throws NamingException, AddressException, MessagingException {
		
		System.out.println("Inscription de l'utilisateur "+log);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "inscription")) {
		
			//On vérifie que le login n'existe pas
			if (!Utilisateur.verifierLogin(log)) {
				
				//On crée le document
				Utilisateur u = Utilisateur.create(log, pass, nom, prenom, email, pays, role);
				
				//Si le document a bien été créé
				if (u != null) {
					//On l'ajoute à la liste des documents du système
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
	 * @return String
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	public String creerDocument(String login, String titre, String duree, String jour, String mois, String annee, String source, String langue, String genre, String fichier) throws NumberFormatException, SQLException {

		System.out.println("Création du document "+titre);

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "creerDocument")) {
		
			//On vérifie que le document n'existe pas
			if (Document.getByTitre(titre) == null) {
				
				//On crée le document
				Document d = Document.create(titre, Integer.parseInt(duree), jour, mois, annee, source, langue, genre, fichier);
				
				//Si le document a bien été créé
				if (d != null) {
					//On l'ajoute à la liste des documents du système
					documents.put(d.getId(), d);
					
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
	public Vector listerDocuments(String login) {

		System.out.println("Liste des documents disponibles");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "listerDocuments")) {
		
			//On crée le vecteur
			Vector vDocuments = new Vector();
			
			//On récupère la liste des documents
			Enumeration listeDocs = documents.elements();
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
	 * @return Vector
	 */
	public Vector rechercherDocument(String login, String id, String titre, String duree, String jour, String mois, String annee, String source, String langue, String genre, String fichier) {

		System.out.println("Recherche d'un document");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "rechercherDocument")) {
			
			//Vecteur de documents à retourner
			Vector vDocuments = new Vector();
		
			//On récupère la liste des documents
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
			if (documents.containsKey(id)) {
				
				//On récupère l'objet document
				Document d = (Document)documents.get(id);

				//Si le document a bien été supprimé
				if (d.supprimer()) {
					
					//On le retire de la liste des documents du système
					documents.remove(id);
					
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
	 * Création d'un programme
	 * @param String login
	 * @param String titre
	 * @param String thematique
	 * @return String
	 * @throws SQLException 
	 */
	public String creerProgramme(String login, String titre, String thematique) throws SQLException {

		System.out.println("Création du programme "+titre);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "creerProgramme")) {
		
			//On vérifie que le programme n'existe pas
			if (Programme.getByTitre(titre) == null) {
				
				//On crée le programme
				Programme p = Programme.create(titre, thematique);
				
				//On l'ajoute à la liste des programmes du système
				programmes.put(p.getId(), p);
				
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
			if (programmes.containsKey(idProg) && documents.containsKey(idDoc)) {
				
				//On récupère les objets
				Document d = (Document)documents.get(idDoc);
				Programme p = (Programme)programmes.get(idProg);
				
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
	 * Lister les programmes disponibles
	 * @param String login
	 * @return Vector
	 */
	public Vector listerProgrammes(String login) {

		System.out.println("Liste des programmes disponibles");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "listerProgrammes")) {
		
			//On crée le vecteur
			Vector vProgrammes = new Vector();
			
			//On récupère la liste des documents
			Enumeration listeProgrammes = programmes.elements();
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
	public Vector rechercherProgramme(String login, String id, String titre, String thematique) {

		System.out.println("Recherche d'un programme");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "rechercherProgramme")) {
			
			//Vecteur de programmes à retourner
			Vector vProgrammes = new Vector();
		
			//On récupère la liste des programmes
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
			if (programmes.containsKey(id)) {
				
				//On récupère l'objet document
				Programme p = (Programme)programmes.get(id);

				//Si le programme a bien été supprimé
				if (p.supprimer()) {
					
					//On le retire de la liste des programmes du système
					programmes.remove(id);
					
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
	 * Création d'un canal
	 * @param String login
	 * @param String nom
	 * @param String utilMax
	 * @return String
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	public String creerCanal(String login, String nom, String utilMax) throws NumberFormatException, SQLException {

		System.out.println("Création du canal "+nom);
		
		// On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "creerCanal")) {
		
			//On vérifie que le canal n'existe pas
			if (Canal.getByNom(nom) == null) {
				
				//On crée le canal
				Canal c = Canal.create(nom, Integer.parseInt(utilMax));
				
				//On l'ajoute à la liste des canaux du système
				canaux.put(c.getId(), c);
				
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
			if (canaux.containsKey(idCanal) && programmes.containsKey(idProg)) {
				
				//On récupère les objets
				Canal c = (Canal)canaux.get(idCanal);
				Programme p = (Programme)programmes.get(idProg);
				
				//On planifie le programme
				//c.planifierProgramme(p);
				
				System.out.println("Programme "+idProg+" planifié sur le canal "+idCanal);
				return Boolean.toString(true);
				
			}
			
			//Sinon, ajout refusé
			System.out.println("Le programme "+idProg+" n'a pas été palnifié sur le canal "+idCanal);
			return Boolean.toString(false);
		}
		
		//Sinon, ajout refusé
		System.out.println("Permission non accordée. Programme non planifié");
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
			if (canaux.containsKey(idCanal) && programmes.containsKey(idProg)) {
				
				//On récupère les objets
				Canal c = (Canal)canaux.get(idCanal);
				Programme p = (Programme)programmes.get(idProg);
				
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
			if (canaux.containsKey(idCanal)) {
				
				//On récupère l'objet canal
				Canal c = (Canal)canaux.get(idCanal);
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
			if (canaux.containsKey(idCanal)) {
				
				//On récupère l'objet canal
				Canal c = (Canal)canaux.get(idCanal);
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
			if (canaux.containsKey(idCanal)) {
				
				//On récupère l'objet canal
				Canal c = (Canal)canaux.get(idCanal);
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
	public Vector listerCanaux(String login) {

		System.out.println("Liste des canaux disponibles");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "listerCanaux")) {
		
			//On crée le vecteur
			Vector vCanaux = new Vector();
			
			//On récupère la liste des documents
			Enumeration listeCanaux = canaux.elements();
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
	public Vector rechercherCanal(String login, String id, String nom, String utilMax) {

		System.out.println("Recherche d'un canal");

		//On vérifie que l'utilisateur a la permission
		if (verifPermission(login, "rechercherCanal")) {
			
			//Vecteur de canaux à retourner
			Vector vCanaux = new Vector();
		
			//On récupère la liste des programmes
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
			if (canaux.containsKey(id)) {
				
				//On récupère l'objet canal
				Canal c = (Canal)canaux.get(id);

				//Si le document a bien été supprimé
				if (c.supprimer()) {
					
					//On le retire de la liste des canaux du système
					canaux.remove(id);
					
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
