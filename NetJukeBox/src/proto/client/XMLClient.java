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
	 * Tentative de connexion au seveur XML
	 * @param String ip
	 * @return boolean
	 */
	public boolean testConnectXML(String ip) {
		
			System.err.println("INFO: Tentative de connexion au serveur XML...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(ip);
				
				// Adresse la requête et affiche les résultats
				String result = (String)clientXML.execute("Systeme.testConnectXML", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
	}
	
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
		if (etatConnecte) {
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
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}
	
	/**
	 * Création d'un canal
	 * @param String nom
	 * @param String utilMax
	 * @return boolean
	 */
	public boolean creerCanal(String nom, String utilMax) {
		
		//Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Création d'un canal...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(nom);
				params.addElement(utilMax);
				
				// Adresse la requête et affiche les résultats
				String result = (String)clientXML.execute("Systeme.creerCanal", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}
	
	/**
	 * Création d'un programme
	 * @param String titre
	 * @param String thematique
	 * @return boolean
	 */
	public boolean creerProgramme(String titre, String thematique) {
		
		//Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Création d'un programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(titre);
				params.addElement(thematique);
				
				// Adresse la requête et affiche les résultats
				String result = (String)clientXML.execute("Systeme.creerProgramme", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}
	
	/**
	 * Création d'un document
	 * @param String titre
	 * @param String duree
	 * @param String jour
	 * @param String mois
	 * @param String annee
	 * @param String source
	 * @param String langue
	 * @param String genre
	 * @param String fichier
	 * @return boolean
	 */
	public boolean creerDocument(String titre, String duree, String jour, String mois, String annee, String source, String langue, String genre, String fichier) {
		
		//Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Création d'un document...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(titre);
				params.addElement(duree);
				params.addElement(jour);
				params.addElement(mois);
				params.addElement(annee);
				params.addElement(source);
				params.addElement(langue);
				params.addElement(genre);
				params.addElement(fichier);
				
				// Adresse la requête et affiche les résultats
				String result = (String)clientXML.execute("Systeme.creerDocument", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}
	
	/**
	 * Ajouter un document à un programme
	 * @param String idDoc
	 * @param String idProg
	 * @return boolean
	 */
	public boolean ajouterDocumentProgramme(String idDoc, String idProg) {
		
		//Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Ajout du document au programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(idDoc);
				params.addElement(idProg);
				
				// Adresse la requête et affiche les résultats
				String result = (String)clientXML.execute("Systeme.ajouterDocumentProgramme", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}
	
	/**
	 * Diffuser un programme sur un canal
	 * @param String idProg
	 * @param String idCanal
	 * @return boolean
	 */
	public boolean diffuserProgramme(String idProg, String idCanal) {
		
		//Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Diffusion du programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(idProg);
				params.addElement(idCanal);
				
				// Adresse la requête et affiche les résultats
				String result = (String)clientXML.execute("Systeme.diffuserProgramme", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}
	
	/**
	 * Planifier un programme sur un canal
	 * @param String idProg
	 * @param String idCanal
	 * @param String jour
	 * @param String mois
	 * @param String annee
	 * @param String heure
	 * @param String minute
	 * @param String seconde 
	 * @return boolean
	 */
	public boolean planifierProgramme(String idProg, String idCanal, String jour, String mois, String annee, String heure, String minute, String seconde) {
		
		//Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Planification du programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(idProg);
				params.addElement(idCanal);
				params.addElement(jour);
				params.addElement(mois);
				params.addElement(annee);
				params.addElement(heure);
				params.addElement(minute);
				params.addElement(seconde);
				
				// Adresse la requête et affiche les résultats
				String result = (String)clientXML.execute("Systeme.planifierProgramme", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}
	
	/**
	 * Ecouter un canal
	 * @param String idCanal
	 * @return String
	 */
	public String ecouterCanal(String idCanal) {
		
		//Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Ecoute d'un canal...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(idCanal);
				
				// Adresse la requête et affiche les résultats
				return (String)clientXML.execute("Systeme.ecouterCanal", params);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}
		
		//Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}
	
	/**
	 * Lancer la diffusion d'un canal
	 * @param String idCanal
	 * @return boolean
	 */
	public boolean startCanal(String idCanal) {
		
		//Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Diffusion d'un canal...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(idCanal);
				
				// Adresse la requête et affiche les résultats
				String result = (String)clientXML.execute("Systeme.startCanal", params);
				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}
	
	/**
	 * Stopper la dissuion d'un canal
	 * @param String idCanal
	 * @return boolean
	 */
	public boolean stopCanal(String idCanal) {
		
		//Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Arrêt d'un canal...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(idCanal);
				
				// Adresse la requête et affiche les résultats
				String result = (String)clientXML.execute("Systeme.stopCanal", params);
				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}
}
