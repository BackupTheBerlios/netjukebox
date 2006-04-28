package proto.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Dictionary;
import java.util.Vector;
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
	 * Etat connecté
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
			
			// On démarre le menu
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
		
		//On démarre le menu
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
		
		//Si le client XML n'est pas déjà initialisé
		if (clientXML == null) {
		
			//On initialise le client
			clientXML = new XMLClient(ip, port);
			
			try {
				//On essaye de se connecter au serveur XML
				if (clientXML.testConnectXML(InetAddress.getLocalHost().getHostAddress())) {
					System.err.println("INFO: Serveur XML contacté avec succès !");
				}
				else System.err.println("WARNING: Serveur XML injoignable !");
			} catch (Exception e) {
				System.err.println("ERREUR: "+e);
			}
			
		//Sinon, déjà initialisé
		} else {
			System.err.println("WARNING: Client XML déjà initialisé !");
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
						if (etatConnecte) System.err.println("INFO: Utilisateur connecté");
						else System.err.println("ERREUR: Utilisateur non connecté");
					}
					else {
						System.err.print("WARNING: Vous êtes déjà connecté au serveur !");
					}
				}
				
				// DECONNEXION
				if (ligne.equalsIgnoreCase("deconnexion")) {
					if (etatConnecte) {
						etatConnecte = !clientXML.deconnexion();
						if (!etatConnecte) System.err.println("INFO: Utilisateur déconnecté");
						else System.err.println("ERREUR: Utilisateur toujours connecté");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
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
						if (connecte) System.err.println("INFO: Canal créé");
						else System.err.println("ERREUR: Canal non créé");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
					}
				}
				
				// CREERPROGRAMME
				if  (ligne.equalsIgnoreCase("creerProgramme")) {
					if (etatConnecte) {
						System.out.print("Titre du programme: ");
						String titre = lire();
						System.out.print("Thématique: ");
						String thematique = lire();
						boolean connecte = clientXML.creerProgramme(titre, thematique);
						if (connecte) System.err.println("INFO: Programme créé");
						else System.err.println("ERREUR: Programme non créé");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
					}
				}
				
				// CREERDOCUMENT
				if  (ligne.equalsIgnoreCase("creerDocument")) {
					if (etatConnecte) {
						System.out.print("Titre du document: ");
						String titre = lire();
						System.out.print("Durée: ");
						String duree = lire();
						System.out.print("Jour de parution: ");
						String jour = lire();
						System.out.print("Mois de parution: ");
						String mois = lire();
						System.out.print("Année de parution: ");
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
						if (connecte) System.err.println("INFO: Document créé");
						else System.err.println("ERREUR: Document non créé");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
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
						if (connecte) System.err.println("INFO: Document ajouté au programme");
						else System.err.println("ERREUR: Document non ajouté au progamme");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
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
						if (connecte) System.err.println("INFO: Diffusion du programme lancée sur le canal");
						else System.err.println("ERREUR: Diffusion du programme non lancée sur le canal");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
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
						System.out.print("Année: ");
						String annee = lire();
						System.out.print("Heure: ");
						String heure = lire();
						System.out.print("Minute: ");
						String minute = lire();
						System.out.print("Seconde: ");
						String seconde = lire();
						boolean connecte = clientXML.planifierProgramme(idProg, idCanal, jour, mois, annee, heure, minute, seconde);
						if (connecte) System.err.println("INFO: Programme planifié sur le canal");
						else System.err.println("ERREUR: Programme non planifié");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
					}
				}
				
				// STARTCANAL
				if  (ligne.equalsIgnoreCase("startCanal")) {
					if (etatConnecte) {
						System.out.print("ID du canal source: ");
						String idCanal = lire();
						if (clientXML.startCanal(idCanal)) {
							System.err.println("INFO: Diffusion du canal lancée");
						}
						else System.err.println("ERREUR: Diffusion du canal non lancée");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
					}
				}
				
				// STOPCANAL
				if  (ligne.equalsIgnoreCase("stopCanal")) {
					if (etatConnecte) {
						System.out.print("ID du canal source: ");
						String idCanal = lire();
						if (clientXML.stopCanal(idCanal)) {
							System.err.println("INFO: Canal arrété");
						}
						else System.err.println("ERREUR: Canal non stoppé");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
					}
				}
				
				// STARTPLAYER
				if  (ligne.equalsIgnoreCase("startPlayer")) {
					if (etatConnecte) {
						System.out.print("ID du canal source: ");
						String idCanal = lire();
						String urlPlayer = clientXML.ecouterCanal(idCanal);
						if (urlPlayer != null) {
							//RTPClient player = RTPClient.getInstance();
							RTPClient2 player = RTPClient2.getInstance();
							player.start(urlPlayer);
							System.err.println("INFO: Ecoute du canal lancée");
						}
						else System.err.println("ERREUR: Ecoute du canal non lancée");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
					}
				}
				
				// STOPPLAYER
				if  (ligne.equalsIgnoreCase("stopPlayer")) {
					if (etatConnecte) {
						//RTPClient player = RTPClient.getInstance();
						RTPClient2 player = RTPClient2.getInstance();
						player.stop();
						System.err.println("INFO: Player arrété");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
					}
				}
				
				// LISTERDOCUMENT
				if  (ligne.equalsIgnoreCase("listerDocument")) {
					if (etatConnecte) {
						Vector vDocuments = clientXML.listerDocuments();
						if (vDocuments!=null && vDocuments.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary d;
							for (int i=0; i<vDocuments.size(); i++){
								d = (Dictionary)vDocuments.get(i);
								System.out.println("----------------- Document -------------------");
								System.out.println("Id: "+d.get("id"));
								System.out.println("Titre: "+d.get("titre"));
								System.out.println("Durée: "+d.get("duree"));
								System.out.println("Date: "+d.get("date"));
								System.out.println("Genre: "+d.get("genre"));
								System.out.println("Source: "+d.get("source"));
								System.out.println("Langue: "+d.get("langue"));
								System.out.println("Fichier: "+d.get("fichier"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Documents listés");
						}
						else System.err.println("WARNING: Aucun document disponible");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
					}
				}
				
				// LISTERPROGRAMME
				if  (ligne.equalsIgnoreCase("listerProgramme")) {
					if (etatConnecte) {
						Vector vProgrammes = clientXML.listerProgrammes();
						if (vProgrammes!=null && vProgrammes.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary p;
							for (int i=0; i<vProgrammes.size(); i++){
								p = (Dictionary)vProgrammes.get(i);
								System.out.println("----------------- Programme -------------------");
								System.out.println("Id: "+p.get("id"));
								System.out.println("Titre: "+p.get("titre"));
								System.out.println("Thématique: "+p.get("thematique"));
								System.out.println("Durée: "+p.get("duree"));
								System.out.println("Nb docs: "+p.get("nbDocs"));
								System.out.println("-----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Programmes listés");
						}
						else System.err.println("WARNING: Aucun programme disponible");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
					}
				}
				
				// LISTERCANAL
				if  (ligne.equalsIgnoreCase("listerCanal")) {
					if (etatConnecte) {
						Vector vCanaux = clientXML.listerCanaux();
						if (vCanaux!=null && vCanaux.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary c;
							for (int i=0; i<vCanaux.size(); i++){
								c = (Dictionary)vCanaux.get(i);
								System.out.println("----------------- Canal -------------------");
								System.out.println("Id: "+c.get("id"));
								System.out.println("Nom: "+c.get("nom"));
								System.out.println("NbMax auditeurs: "+c.get("utilMax"));
								System.out.println("-------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Programmes listés");
						}
						else System.err.println("WARNING: Aucun canal disponible");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
					}
				}
				
				// INSCRIPTION
				if  (ligne.equalsIgnoreCase("inscription")) {
					if (etatConnecte) {
						System.out.print("prenom: ");
						String prenom = lire();
						System.out.print("nom: ");
						String nom = lire();
						System.out.print("login: ");
						String log = lire();
						System.out.print("password: ");
						String pass = lire();
						System.out.print("email: ");
						String email = lire();
						System.out.print("role: ");
						String role = lire();
						System.out.print("pays: ");
						String pays = lire();
						
						if (clientXML.inscription(log, pass, role, email, nom, prenom, pays)) {
							System.err.println("INFO: Utilisateur inscrit");
						}
						else System.err.println("WARNING: Inscription impossible");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
					}
				}
				
				// RECHERCHERDOCUMENT
				if  (ligne.equalsIgnoreCase("rechercherDocument")) {
					if (etatConnecte) {
						System.out.print("ID du document: ");
						String id = lire();
						System.out.print("Titre: ");
						String titre = lire();
						System.out.print("Durée: ");
						String duree = lire();
						System.out.print("Jour de parution: ");
						String jour = lire();
						System.out.print("Mois de parution: ");
						String mois = lire();
						System.out.print("Année de parution: ");
						String annee = lire();
						System.out.print("Source: ");
						String source = lire();
						System.out.print("Langue: ");
						String langue = lire();
						System.out.print("Genre: ");
						String genre = lire();
						System.out.print("Fichier: ");
						String fichier = lire();
						Vector vDocuments = clientXML.rechercherDocument(id, titre, duree, jour, mois, annee, source, langue, genre, fichier);
						if (vDocuments!=null && vDocuments.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary d;
							for (int i=0; i<vDocuments.size(); i++){
								d = (Dictionary)vDocuments.get(i);
								System.out.println("----------------- Document -------------------");
								System.out.println("Id: "+d.get("id"));
								System.out.println("Titre: "+d.get("titre"));
								System.out.println("Durée: "+d.get("duree"));
								System.out.println("Date: "+d.get("date"));
								System.out.println("Genre: "+d.get("genre"));
								System.out.println("Source: "+d.get("source"));
								System.out.println("Langue: "+d.get("langue"));
								System.out.println("Fichier: "+d.get("fichier"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Document recherché");
						}
						else System.err.println("WARNING: Aucun document disponible");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
					}
				}
				
				// RECHERCHERPROGRAMME
				if  (ligne.equalsIgnoreCase("rechercherProgramme")) {
					if (etatConnecte) {
						System.out.print("ID du programme: ");
						String id = lire();
						System.out.print("Titre: ");
						String titre = lire();
						System.out.print("Thématique: ");
						String thematique = lire();
						Vector vProgrammes = clientXML.rechercherProgramme(id, titre, thematique);
						if (vProgrammes!=null && vProgrammes.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary d;
							for (int i=0; i<vProgrammes.size(); i++){
								d = (Dictionary)vProgrammes.get(i);
								System.out.println("----------------- Programme -------------------");
								System.out.println("Id: "+d.get("id"));
								System.out.println("Titre: "+d.get("titre"));
								System.out.println("Thématique: "+d.get("thematique"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Programme recherché");
						}
						else System.err.println("WARNING: Aucun programme disponible");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
					}
				}
				
				// RECHERCHERCANAL
				if  (ligne.equalsIgnoreCase("rechercherCanal")) {
					if (etatConnecte) {
						System.out.print("ID du canal: ");
						String id = lire();
						System.out.print("Nom: ");
						String nom = lire();
						System.out.print("NbMax d'auditeurs: ");
						String utilMax = lire();
						Vector vCanaux = clientXML.rechercherCanal(id, nom, utilMax);
						if (vCanaux!=null && vCanaux.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary d;
							for (int i=0; i<vCanaux.size(); i++){
								d = (Dictionary)vCanaux.get(i);
								System.out.println("----------------- Canal -------------------");
								System.out.println("Id: "+d.get("id"));
								System.out.println("Nom: "+d.get("nom"));
								System.out.println("NbMax d'auditeurs: "+d.get("utilMax"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Canal recherché");
						}
						else System.err.println("WARNING: Aucun canal disponible");
					}
					else {
						System.err.print("WARNING: Vous n'êtes pas connecté au serveur !");
					}
				}
				
				// HELP
				if (ligne.equalsIgnoreCase("help")) {
					System.out.println("Commandes disponibles:");
					System.out.println(" connexion : ouvrir une session sur le serveur");
					System.out.println(" deconnexion : fermer la session");
					System.out.println(" creerCanal : créer un canal");
					System.out.println(" creerProgramme : créer un programme");
					System.out.println(" creerDocument : créer un document");
					System.out.println(" ajouterDocumentProgramme : ajouter un document à un programme");
					System.out.println(" diffuserProgramme : diffuser un programme sur un canal");
					System.out.println(" startCanal : lancer la diffusion d'un canal");
					System.out.println(" stopCanal : stopper la diffusion d'un canal");
					System.out.println(" startPlayer : lancer l'écoute un canal");
					System.out.println(" stopPlayer : stopper l'écoute d'un canal");
					System.out.println(" listerDocument : lister les documents disponibles");
					System.out.println(" listerProgramme : lister les programmes disponibles");
					System.out.println(" listerCanal : lister les canaux disponibles");
					System.out.println(" inscription : inscription d'un nouvel utilisateur");
					System.out.println(" rechercherDocument : rechercher un document");
					System.out.println(" rechercherProgramme : rechercher un programme");
					System.out.println(" rechercherCanal : rechercher un canal");
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
		
		//Fichier d'initialisation par défaut (si pas de paramètres)
		String filename = args.length > 0 ? args[0] : "src/proto/client/client.ini";
		Preferences prefs = new IniFile(new File(filename));
		
		//On démarre le client
		new Client(prefs.node("serveur").get("ip", null), prefs.node("serveur").get("port", null));
		
	}
}