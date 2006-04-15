package proto.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
			System.out.println("IP du serveur: ");
			String ip = reader.readLine();
			System.out.println("Port du serveur: ");
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
		
			clientXML = new XMLClient(ip, port);
			
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
		
		System.out.println("###########################################");
		System.out.println("Entrez 'end' pour terminer");
		System.out.println("###########################################");
		
		try{
			reader= new BufferedReader(new InputStreamReader(System.in));
			do {
				System.out.println("> ");
				ligne = reader.readLine();
				
				// CONNEXION
				if  (ligne.equalsIgnoreCase("connexion")) {
					System.out.println("Login: ");
					String login = lire();
					System.out.println("Pwd: ");
					String pwd = lire();
					boolean connecte = clientXML.connexion(login, pwd);
					if (connecte) System.err.println("INFO: Utilisateur connecté");
					else System.err.println("ERREUR: Utilisateur non connecté");
				}
				
				// DECONNEXION
				if (ligne.equalsIgnoreCase("deconnexion")) {
					boolean deconnecte = clientXML.deconnexion();
					if (deconnecte) System.err.println("INFO: Utilisateur déconnecté");
					else System.err.println("ERREUR: Utilisateur toujours connecté");
				}
				
			} while (!ligne.equalsIgnoreCase("end"));
		} catch (Exception e){
			System.out.println(e);
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
	public static void main(String args[]) {
		
		//S'il y a des paramètres en ligne de commande
		//Usage: java client [adresseServeur] [portServeur]
		if (args.length == 2) {
			new Client(args[0], args[1]);
		}
		
		//Sinon, on les demandera à l'utilisateur
		else {
			//On démarre le client
			new Client();
		}
		
	}
}