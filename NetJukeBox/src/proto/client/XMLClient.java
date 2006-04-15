package proto.client;

import java.net.MalformedURLException;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpc;
import org.apache.xmlrpc.XmlRpcClient;

/**
 * Client XML (envoie les requêtes au serveur XML du serveur principal)
 * @author philippeB
 */
public class XMLClient {
	
// ATTRIBUTS
//**************************************
	
	/**
	 * Client XMLRPC
	 */
	private XmlRpcClient clientXML = null;
	
	/**
	 * Etat connecté
	 */
	private boolean etatConnecte = false;
	
	/**
	 * Login
	 */
	private String login;
	
// CONSTRUCTEUR
//**************************************
	
	/**
	 * Initialise le client XMLRPC et se connecte au serveur XMLRPC
	 * @param String ip
	 * @param String port
	 */
	public XMLClient(String ip, String port) {
		
		//Si le client XML n'est pas déjà initialisé
		if (clientXML == null) {
			System.err.println("INFO: Initialisation XMLRPC en cours...");
			try {
				// Utilise lengthpilote Sax d'Apache Xerces
				XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");
				
				// Creation du client & identification du serveur
				clientXML = new XmlRpcClient("http://"+ip+":"+port);
				System.err.println("INFO: Client Streaming démarré");
				
			} catch (ClassNotFoundException e) {
				System.err.println("ERREUR: Impossible de localiser le pilote Sax");
			} catch (MalformedURLException e) {
				System.err.println("ERREUR: URL non conforme au format du serveur XML-RPC");

			} catch (Exception e) {
				System.out.println("ERREUR: " + e);
			}
			
		//Sinon, déjà initialisé
		} else {
			System.err.println("WARNING: Client XML déjà initialisé !");
		}
	}

	
// METHODES DYNAMIQUES
//**************************************
	
	/**
	 * Identifie l'utilisateur sur le serveur principal
	 * @param String login
	 * @param String pwd
	 * @return boolean
	 */
	public boolean connexion(String login, String pwd) {
		
		//Si l'utilisateur n'est pas connecté au seveur
		if (!etatConnecte) {
			System.err.println("INFO: Connexion en cours...");
			this.login = login;
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(pwd);
				
				// Adresse la requête et affiche les résultats
				String result = (String)clientXML.execute("Systeme.connexion", params);
				
				etatConnecte = Boolean.parseBoolean(result);
				return etatConnecte;
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, déjà connecté
		} else {
			System.err.println("WARNING: Client déjà connecté au serveur !");
			return false;
		}
	}
	
	/**
	 * Déconnecte l'utilisateur du serveur principal
	 * @param String login
	 * @param String pwd
	 * @return boolean
	 */
	public boolean deconnexion() {
		
		//Si l'utilisateur est connecté au seveur
		if (!etatConnecte) {
			System.err.println("INFO: Déconnexion en cours...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				
				// Adresse la requête et affiche les résultats
				String result = (String)clientXML.execute("Systeme.deconnexion", params);
				boolean estDeconnecte = Boolean.parseBoolean(result);
				
				etatConnecte = !estDeconnecte;
				return estDeconnecte;
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, n'est pas connecté
		} else {
			System.err.println("WARNING: Le client n'est pas connecté au serveur !");
			return false;
		}
	}
}
