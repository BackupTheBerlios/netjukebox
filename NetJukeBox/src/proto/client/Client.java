package proto.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.prefs.Preferences;

import org.ini4j.IniFile;

//Arguments : 192.168.0.2 10000


/**
 * Client du Net-JukeBox (classe principale)
 */
public class Client {
	
// ATTRIBUTS
//***********************************
	
	/**
	 * Client XMLRPC
	 */
	private XMLClient clientXML = null;
	
	/**
	 * Etat connect�
	 */
	private boolean etatConnecte = false;
	

// CONSTRUCTEUR
//***********************************
	
	/**
	 * Constructeur (main sans args)
	 */
	public Client() {
		
		// Demande d'infos pour le Client XML
		BufferedReader reader = null;
		try{
			reader= new BufferedReader(new InputStreamReader(System.in));
			System.out.println("###########################################");
			System.out.print("IP du serveur: ");
			String ip = reader.readLine();
			System.out.print("Port du serveur: ");
			String port = reader.readLine();
			
			// On initialise le client XML
			initializeXML(ip, port);
			
			// On d�marre le menu
			menu();
		} catch (Exception e){
			System.out.println(e);
			System.exit(1);
		}
	}
	
	/**
	 * Constructeur (main avec args)
	 * @param ip
	 * @param port
	 */
	public Client(String ip, String port) {
		
		//On initialise le client XML
		initializeXML(ip, port);
		
		//On d�marre le menu
		menu();
	}
	
// METHODES DYNAMIQUES
//***********************************
	
	/**
	 * Initialise le client XMLRPC et se connecte au serveur XMLRPC
	 * @param String ip
	 * @param String port
	 */
	private void initializeXML(String ip, String port) {
		
		//Si le client XML n'est pas d�j� initialis�
		if (clientXML == null) {
		
			//On initialise le client
			clientXML = new XMLClient(ip, port);
			
			try {
				//On essaye de se connecter au serveur XML
				if (clientXML.testConnectXML(InetAddress.getLocalHost().getHostAddress())) {
					System.err.println("INFO: Serveur XML contact� avec succ�s !");
				}
				else System.err.println("WARNING: Serveur XML injoignable !");
			} catch (Exception e) {
				System.err.println("ERREUR: "+e);
			}
			
		//Sinon, d�j� initialis�
		} else {
			System.err.println("WARNING: Client XML d�j� initialis� !");
		}
	}
	
	
// DIALOGUE AVEC L'UTILISATEUR (MENU)
//***********************************
	
	private void menu() {
		
		//Gestion des saisies clavier
		BufferedReader reader = null;
		String ligne;
		
		System.out.println();
		System.out.println("####################################################");
		System.out.println("       Bienvenue sur le client Net-JukeBox");
		System.out.println();
		System.out.println("Entrez 'help' pour lister les commandes disponibles");
		System.out.println("####################################################");
		
		try{
			reader= new BufferedReader(new InputStreamReader(System.in));
			do {
				System.out.println();
				System.out.print("> ");
				ligne = reader.readLine();
				
				// CONNEXION
				if  (ligne.equalsIgnoreCase("connexion")) {
					if (!etatConnecte) {
						System.out.print("Login: ");
						String login = lire();
						System.out.print("Pwd: ");
						String pwd = lire();
						etatConnecte = clientXML.connexion(login, pwd);
						if (etatConnecte) System.err.println("INFO: Utilisateur connect�");
						else System.err.println("ERREUR: Utilisateur non connect�");
					}
					else {
						System.err.print("WARNING: Vous �tes d�j� connect� au serveur !");
					}
				}
				
				// DECONNEXION
				if (ligne.equalsIgnoreCase("deconnexion")) {
					if (etatConnecte) {
						etatConnecte = !clientXML.deconnexion();
						if (!etatConnecte) System.err.println("INFO: Utilisateur d�connect�");
						else System.err.println("ERREUR: Utilisateur toujours connect�");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// CREERCANAL
				if  (ligne.equalsIgnoreCase("creerCanal")) {
					if (etatConnecte) {
						System.out.print("Nom du canal: ");
						String nom = lire();
						System.out.print("Nombre maximal d'auditeurs: ");
						String utilMax = lire();
						boolean connecte = clientXML.creerCanal(nom, utilMax);
						if (connecte) System.err.println("INFO: Canal cr��");
						else System.err.println("ERREUR: Canal non cr��");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// CREERPROGRAMME
				if  (ligne.equalsIgnoreCase("creerProgramme")) {
					if (etatConnecte) {
						System.out.print("Titre du programme: ");
						String titre = lire();
						System.out.print("Th�matique: ");
						String thematique = lire();
						boolean connecte = clientXML.creerProgramme(titre, thematique);
						if (connecte) System.err.println("INFO: Programme cr��");
						else System.err.println("ERREUR: Programme non cr��");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// CREERDOCUMENT
				if  (ligne.equalsIgnoreCase("creerDocument")) {
					if (etatConnecte) {
						System.out.print("Titre du document: ");
						String titre = lire();
						System.out.print("Dur�e: ");
						String duree = lire();
						System.out.print("Jour de parution: ");
						String jour = lire();
						System.out.print("Mois de parution: ");
						String mois = lire();
						System.out.print("Ann�e de parution: ");
						String annee = lire();
						System.out.print("Source: ");
						String source = lire();
						System.out.print("Langue: ");
						String langue = lire();
						System.out.print("Genre: ");
						String genre = lire();
						System.out.print("Fichier: ");
						String fichier = lire();
						boolean connecte = clientXML.creerDocument(titre, duree, jour, mois, annee, source, langue, genre, fichier);
						if (connecte) System.err.println("INFO: Document cr��");
						else System.err.println("ERREUR: Document non cr��");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// AJOUTERDOCUMENTPROGRAMME
				if  (ligne.equalsIgnoreCase("ajouterDocumentProgramme")) {
					if (etatConnecte) {
						System.out.print("ID du document source: ");
						String idDoc = lire();
						System.out.print("ID du programme cible: ");
						String idProg = lire();
						boolean connecte = clientXML.ajouterDocumentProgramme(idDoc, idProg);
						if (connecte) System.err.println("INFO: Document ajout� au programme");
						else System.err.println("ERREUR: Document non ajout� au progamme");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// DIFFUSERPROGRAMME
				if  (ligne.equalsIgnoreCase("diffuserProgramme")) {
					if (etatConnecte) {
						System.out.print("ID du programme source: ");
						String idProg = lire();
						System.out.print("ID du canal cible: ");
						String idCanal = lire();
						boolean connecte = clientXML.diffuserProgramme(idProg, idCanal);
						if (connecte) System.err.println("INFO: Diffusion du programme lanc�e sur le canal");
						else System.err.println("ERREUR: Diffusion du programme non lanc�e sur le canal");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// PLANIFIERPROGRAMME
				if  (ligne.equalsIgnoreCase("planifierProgramme")) {
					if (etatConnecte) {
						System.out.print("ID du programme source: ");
						String idProg = lire();
						System.out.print("ID du canal cible: ");
						String idCanal = lire();
						System.out.print("Jour de planification: ");
						String jour = lire();
						System.out.print("Mois: ");
						String mois = lire();
						System.out.print("Ann�e: ");
						String annee = lire();
						System.out.print("Heure: ");
						String heure = lire();
						System.out.print("Minute: ");
						String minute = lire();
						System.out.print("Seconde: ");
						String seconde = lire();
						boolean connecte = clientXML.planifierProgramme(idProg, idCanal, jour, mois, annee, heure, minute, seconde);
						if (connecte) System.err.println("INFO: Programme planifi� sur le canal");
						else System.err.println("ERREUR: Programme non planifi�");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// ECOUTERCANAL
				if  (ligne.equalsIgnoreCase("ecouterCanal")) {
					if (etatConnecte) {
						System.out.print("ID du canal source: ");
						String idCanal = lire();
						String url = clientXML.ecouterCanal(idCanal);
						if (url != null) {
							System.err.println("INFO: Ecoute du canal lanc�e");
							new RTPClient(url);
						}
						else System.err.println("ERREUR: Ecoute du canal non lanc�e");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// HELP
				if (ligne.equalsIgnoreCase("help")) {
					System.out.println("Commandes disponibles:");
					System.out.println(" connexion : ouvrir une session sur le serveur");
					System.out.println(" deconnexion : fermer la session");
					System.out.println(" creerCanal : cr�er un canal");
					System.out.println(" creerProgramme : cr�er un programme");
					System.out.println(" creerDocument : cr�er un document");
					System.out.println(" ajouterDocumentProgramme : ajouter un document � un programme");
					System.out.println(" diffuserProgramme : diffuser un programme sur un canal");
					System.out.println(" ecouterCanal : ecouter un canal");
					System.out.println(" end : terminer");
					System.out.println(" help : lister les commandes disponibles");
				}
				
				// END
				if (ligne.equalsIgnoreCase("end")) {
					if (etatConnecte) clientXML.deconnexion();
					System.out.println();
					System.out.println("###########################################");
					System.out.println("Fermeture du client");
					System.out.println("###########################################");
					System.exit(1);
				}
				
			} while (!ligne.equalsIgnoreCase("end"));
			
		} catch (Exception e){
			System.err.println("ERREUR:" + e);
		}
	}
	
	/**
	 * Lecture d'une ligne en console
	 * @return
	 */
	private String lire() {

		BufferedReader reader = null;
		try{
			reader= new BufferedReader(new InputStreamReader(System.in));
			return reader.readLine();
		} catch (Exception e){
			System.out.println(e);
			return null;
		}
	}
	
// MAIN
//***********************************
	public static void main(String args[]) throws Exception {
		
		//USAGE : java Client [filename.ini]
		
		//Fichier d'initialisation par d�faut (si pas de param�tres)
		String filename = args.length > 0 ? args[0] : "src/proto/client/client.ini";
		Preferences prefs = new IniFile(new File(filename));
		
		//On d�marre le client
		new Client(prefs.node("serveur").get("ip", null), prefs.node("serveur").get("port", null));
		
	}
}