package proto.client;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.util.Dictionary;
import java.util.TimerTask;
import java.util.Vector;
import org.apache.xmlrpc.WebServer;
import org.apache.xmlrpc.XmlRpc;
import org.apache.xmlrpc.XmlRpcClient;

/**
 * Client XML (envoie les requêtes au serveur XML du serveur principal)
 * 
 * @author philippeB
 */
public class XMLClient {

// ATTRIBUTS
// **************************************

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
// **************************************

	/**
	 * Initialise le client XMLRPC et se connecte au serveur XMLRPC
	 * @param String ip
	 * @param String port
	 */
	public XMLClient(String ip, String port) {
		
		// Si le client XML n'est pas déjà initialisé
		if (clientXML == null) {
			System.out.println("INFO: Initialisation XMLRPC en cours...");
			try {
				// Utilise lengthpilote Sax d'Apache Xerces
				XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");

				// Creation du client & identification du serveur
				clientXML = new XmlRpcClient("http://" + ip + ":" + port);
				
				//Creation du serveur xmlrpc
				WebServer server = new WebServer(Integer.parseInt(port));
				
				// Enregistre la classe du gestionnaire
				server.addHandler("PingXML", new PingXML());

				System.out.println("INFO: Client Streaming démarré");

			} catch (ClassNotFoundException e) {
				System.err.println("ERREUR: Impossible de localiser le pilote Sax");
			} catch (MalformedURLException e) {
				System.err.println("ERREUR: URL non conforme au format du serveur XML-RPC");
			} catch (Exception e) {
				System.err.println("ERREUR: "+ e);
			}

			// Sinon, déjà initialisé
		} else {
			System.err.println("WARNING: Client XML déjà initialisé !");
		}
	}

// METHODES DYNAMIQUES
// **************************************

	/**
	 * Tentative de connexion au seveur XML
	 * @param String ip
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean testConnectXML(String ip) {
		System.out.println("INFO: Tentative de connexion au serveur XML...");
		try {
			// Création de la requête
			Vector params = new Vector();
			params.addElement(ip);

			// Adresse la requête et affiche les résultats
			String result = (String) clientXML.execute(
					"Systeme.testConnectXML", params);
			
			return Boolean.parseBoolean(result);			

		} catch (Exception e) {
			System.err.println("ERREUR : " + e);
			return false;
		}
	}

// CANAL
// ------------------------------------------------------------
	/**
	 * Création d'un canal
	 * @param String nom
	 * @param String utilMax
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean creerCanal(String nom, String utilMax) {
		// Si l'utilisateur est connecté au seveur
		
		if (etatConnecte) {
			System.out.println("INFO: Création d'un canal...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(nom);
				params.addElement(utilMax);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.creerCanal", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
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
	@SuppressWarnings("unchecked")
	public boolean diffuserProgramme(String idProg, String idCanal) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Diffusion du programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idProg);
				params.addElement(idCanal);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.diffuserProgramme", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
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
	@SuppressWarnings("unchecked")
	public boolean planifierProgramme(String idProg, String idCanal,
			String jour, String mois, String annee, String heure,
			String minute, String seconde) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Planification du programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idProg);
				params.addElement(idCanal);
				params.addElement(jour);
				params.addElement(mois);
				params.addElement(annee);
				params.addElement(heure);
				params.addElement(minute);
				params.addElement(seconde);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.planifierProgramme", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Déplanifier un programme sur un canal
	 * @param String idCanal
	 * @param String calage
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean deplanifierProgramme(String idCanal, String calage) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Déplanification du programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idCanal);
				params.addElement(calage);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.deplanifierProgramme", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
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
	@SuppressWarnings("unchecked")
	public String ecouterCanal(String idCanal) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Ecoute d'un canal...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idCanal);

				// Adresse la requête et affiche les résultats
				return (String) clientXML.execute("Systeme.ecouterCanal",
						params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
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
	@SuppressWarnings("unchecked")
	public boolean startCanal(String idCanal) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Diffusion d'un canal...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idCanal);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.startCanal", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
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
	@SuppressWarnings("unchecked")
	public boolean stopCanal(String idCanal) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Arrêt d'un canal...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idCanal);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute("Systeme.stopCanal",
						params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Lister les canaux disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerCanaux() {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Liste des canaux...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				return (Vector) clientXML.execute("Systeme.listerCanaux",
						params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}

	/**
	 * Rechercher un canal
	 * @param String id
	 * @param String nom
	 * @param String utilMax
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector rechercherCanal(String id, String nom, String utilMax) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Recherche des canaux...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(nom);
				params.addElement(utilMax);

				// Adresse la requête et affiche les résultats
				return (Vector) clientXML.execute("Systeme.rechercherCanal",
						params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}

	/**
	 * Supprimer un canal
	 * @param String id
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean supprimerCanal(String id) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Suppression d'un canal...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerCanal", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Modification d'un canal
	 * @param String id
	 * @param String nom
	 * @param String utilMax
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean modifierCanal(String id, String nom, String utilMax) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Modification d'un canal...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(nom);
				params.addElement(utilMax);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.modifierCanal", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Informations d'un canal
	 * @param String id
	 * @return Dictionary
	 */
	@SuppressWarnings("unchecked")
	public Dictionary infoCanal(String id) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Informations sur un canal...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				return (Dictionary) clientXML.execute("Systeme.infoCanal",
						params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}

// PROGRAMME
// ------------------------------------------------------------

	/**
	 * Création d'un programme
	 * @param String titre
	 * @param String thematique
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean creerProgramme(String titre, String thematique) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Création d'un programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(titre);
				params.addElement(thematique);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.creerProgramme", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
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
	@SuppressWarnings("unchecked")
	public boolean ajouterDocumentProgramme(String idDoc, String idProg) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Ajout du document au programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idDoc);
				params.addElement(idProg);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.ajouterDocumentProgramme", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Retirer un document d'un programme
	 * @param String idDoc
	 * @param String idProg
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean retirerDocumentProgramme(String idProg, String calage) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Retirer un document du programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idProg);
				params.addElement(calage);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.retirerDocumentProgramme", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Lister les programmes disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerProgrammes() {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Liste des programmes...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				return (Vector) clientXML.execute("Systeme.listerProgrammes",
						params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}

	/**
	 * Rechercher un programme
	 * @param String id
	 * @param String titre
	 * @param String thematique
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector rechercherProgramme(String id, String titre, String thematique) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Recherche des programmes...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(titre);
				params.addElement(thematique);

				// Adresse la requête et affiche les résultats
				return (Vector) clientXML.execute(
						"Systeme.rechercherProgramme", params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}

	/**
	 * Supprimer un programme
	 * @param String id
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean supprimerProgramme(String id) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Suppression d'un programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerProgramme", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Modification d'un programme
	 * @param String id
	 * @param String titre
	 * @param String thematique
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean modifierProgramme(String id, String titre, String thematique) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Modification d'un programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(titre);
				params.addElement(thematique);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.modifierProgramme", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Informations d'un programme
	 * @param String id
	 * @return Dictionary
	 */
	@SuppressWarnings("unchecked")
	public Dictionary infoProgramme(String id) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Informations sur un programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				return (Dictionary) clientXML.execute("Systeme.infoProgramme",
						params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}

// DOCUMENT
// ------------------------------------------------------------

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
	 * @param String artiste
	 * @param String interprete
	 * @param String compositeur
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean creerDocument(String titre, String duree, String jour,
			String mois, String annee, String source, String langue,
			String genre, String fichier, String artiste, String interprete,
			String compositeur) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Création d'un document...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(titre);
				params.addElement(duree);
				params.addElement(jour);
				params.addElement(mois);
				params.addElement(annee);
				params.addElement(source);
				params.addElement(langue);
				params.addElement(genre);
				params.addElement(fichier);
				params.addElement(artiste);
				params.addElement(interprete);
				params.addElement(compositeur);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.creerDocument", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Lister les documents disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerDocuments() {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Liste des documents...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				return (Vector) clientXML.execute("Systeme.listerDocuments",
						params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}

	/**
	 * Rechercher un document
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
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector rechercherDocument(String id, String titre, String duree,
			String jour, String mois, String annee, String source,
			String langue, String genre, String fichier, String artiste,
			String interprete, String compositeur) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Recherche des documents...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(titre);
				params.addElement(duree);
				params.addElement(jour);
				params.addElement(mois);
				params.addElement(annee);
				params.addElement(source);
				params.addElement(langue);
				params.addElement(genre);
				params.addElement(fichier);
				params.addElement(artiste);
				params.addElement(interprete);
				params.addElement(compositeur);

				// Adresse la requête et affiche les résultats
				return (Vector) clientXML.execute("Systeme.rechercherDocument",
						params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}

	/**
	 * Supprimer un document
	 * @param String id
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean supprimerDocument(String id) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Suppression d'un document...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerDocument", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Modification d'un document
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
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean modifierDocument(String id, String titre, String duree,
			String jour, String mois, String annee, String source,
			String langue, String genre, String fichier, String artiste,
			String interprete, String compositeur) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Modification d'un document...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(titre);
				params.addElement(duree);
				params.addElement(jour);
				params.addElement(mois);
				params.addElement(annee);
				params.addElement(source);
				params.addElement(langue);
				params.addElement(genre);
				params.addElement(fichier);
				params.addElement(artiste);
				params.addElement(interprete);
				params.addElement(compositeur);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.modifierDocument", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Informations d'un document
	 * @param String id
	 * @return Dictionary
	 */
	@SuppressWarnings("unchecked")
	public Dictionary infoDocument(String id) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Informations sur un document...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				return (Dictionary) clientXML.execute("Systeme.infoDocument",
						params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}

// UTILISATEUR
// ------------------------------------------------------------

	/**
	 * Identifie l'utilisateur sur le serveur principal
	 * @param String login
	 * @param String pwd
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean connexion(String login, String pwd) {
		// Si l'utilisateur n'est pas connecté au seveur
		if (!etatConnecte) {
			System.out.println("INFO: Connexion en cours...");
			this.login = login;
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(pwd);
				params.addElement(InetAddress.getLocalHost().getHostAddress());

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute("Systeme.connexion",
						params);

				etatConnecte = Boolean.parseBoolean(result);
				return etatConnecte;

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, déjà connecté
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
	@SuppressWarnings("unchecked")
	public boolean deconnexion() {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Déconnexion en cours...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.deconnexion", params);
				boolean estDeconnecte = Boolean.parseBoolean(result);

				etatConnecte = !estDeconnecte;
				return estDeconnecte;

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, n'est pas connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}
	
	/**
	 * Inscription d'un utilisateur
	 * @param String log
	 * @param String pass
	 * @param String role
	 * @param String email
	 * @param String nom
	 * @param String prenom
	 * @param String pays
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean inscription(String log, String pass, String role,
			String email, String nom, String prenom, String pays) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Inscription d'un utilisateur...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(log);
				params.addElement(pass);
				params.addElement(role);
				params.addElement(email);
				params.addElement(nom);
				params.addElement(prenom);
				params.addElement(pays);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.inscription", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Suppression d'un compte utilisateur
	 * @param login
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean supprimerUtilisateur(String login) {
		if (etatConnecte) {
			System.out.println("INFO: Suppression du compte ...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerUtilisateur", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Recherche des attributs de l'utilisateur
	 * @param Login
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector rechercherUtilisateur() {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Recherche des attributs...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				return (Vector) clientXML.execute(
						"Systeme.rechercherUtilisateur", params);
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}
			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}

	/**
	 * Effectue les modifications des attributs de l'utilisateur
	 * @param Login
	 * @param newLogin
	 * @param Nom
	 * @param Prenom
	 * @param Email
	 * @param Pays
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean modifierUtilisateur(String newlogin, String pwd, String Nom,
			String Prenom, String Email, String Pays) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Modification des attributs...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(newlogin);
				params.addElement(pwd);
				params.addElement(Nom);
				params.addElement(Prenom);
				params.addElement(Email);
				params.addElement(Pays);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.modifierUtilisateur", params);

				// return Boolean.parseBoolean(result);
				return true;

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Recherche du mot de passe de l'utilisateur
	 * @param String login
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean recherchepwd(String login) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Recherche du mot de passe...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.rechercherpwd", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}
	
	/**
	 * Ajouter une permssion à un utilisateur
	 * @param String idPermission
	 * @param String logUtil
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean ajouterPermissionUtilisateur(String idPermission, String logUtil) {

		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Ajout d'une permission à un utilisateur...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(logUtil);
				params.addElement(idPermission);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute("Systeme.ajouterPermissionUtilisateur", params);

				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Retirer une permission à un utilisateur
	 * @param String idPermission
	 * @param String logUtil
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean retirerPermissionUtilisateur(String idPermission, String logUtil) {

		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Retirer une permission à un utilisateur...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(logUtil);
				params.addElement(idPermission);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute("Systeme.retirerPermissionUtilisateur", params);

				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

// CONTRAT
//------------------------------------------------------------

	/**
	 * Création d'un contrat
	 * @param String id
	 * @param String titre
	 * @param String jourSignature
	 * @param String moisSignature
	 * @param String anneeSignature
	 * @param String jourExpiration
	 * @param String moisExpiration
	 * @param String anneeExpiration
	 * @param String idContractant
	 * @param String modeReglement
	 * @param String type
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean creerContrat(String titre, String jourSignature, String moisSignature,
			String anneeSignature, String jourExpiration, String moisExpiration,
			String anneeExpiration, String idContractant, String modeReglement, String type) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Création d'un contrat...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(titre);
				params.addElement(jourSignature);
				params.addElement(moisSignature);
				params.addElement(anneeSignature);
				params.addElement(jourExpiration);
				params.addElement(moisExpiration);
				params.addElement(anneeExpiration);
				params.addElement(idContractant);
				params.addElement(modeReglement);
				params.addElement(type);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.creerContrat", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Lister les contractants disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerContrats() {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Liste des contrats...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				return (Vector) clientXML.execute("Systeme.listerContrats",
						params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}
	
	/**
	 * Informations d'un contrat
	 * @param String id
	 * @return Dictionary
	 */
	@SuppressWarnings("unchecked")
	public Dictionary infoContrat(String id) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Informations sur un contrat...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				return (Dictionary) clientXML.execute("Systeme.infoContrat",
						params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}
	
	/**
	 * Rechercher un contrat
	 * @param String id
	 * @param String titre
	 * @param String jourSignature
	 * @param String moisSignature
	 * @param String anneeSignature
	 * @param String jourExpiration
	 * @param String moisExpiration
	 * @param String anneeExpiration
	 * @param String idContractant
	 * @param String modeReglement
	 * @param String type
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector rechercherContrat(String id, String titre, String jourSignature, String moisSignature,
			String anneeSignature, String jourExpiration, String moisExpiration, String anneeExpiration,
			String idContractant, String modeReglement, String type) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Recherche des contrats...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(titre);
				params.addElement(jourSignature);
				params.addElement(moisSignature);
				params.addElement(anneeSignature);
				params.addElement(jourExpiration);
				params.addElement(moisExpiration);
				params.addElement(anneeExpiration);
				params.addElement(idContractant);
				params.addElement(modeReglement);
				params.addElement(type);

				// Adresse la requête et affiche les résultats
				return (Vector) clientXML.execute("Systeme.rechercherContrats",
						params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}

	/**
	 * Supprimer un contract
	 * @param String id
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean supprimerContrat(String id) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Suppression d'un contrat...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerContrat", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Modification d'un contrat
	 * @param String id
	 * @param String jourSignature
	 * @param String moisSignature
	 * @param String anneeSignature
	 * @param String jourExpiration
	 * @param String moisExpiration
	 * @param String anneeExpiration
	 * @param String signataire
	 * @param String modeReglement
	 * @param String type
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean modifierContrat(String id, String titre, String jourSignature, String moisSignature,
			String anneeSignature, String jourExpiration, String moisExpiration, String anneeExpiration,
			String signataire, String modeReglement, String type) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Modification d'un contrat...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(titre);
				params.addElement(jourSignature);
				params.addElement(moisSignature);
				params.addElement(anneeSignature);
				params.addElement(jourExpiration);
				params.addElement(moisExpiration);
				params.addElement(anneeExpiration);
				params.addElement(signataire);
				params.addElement(modeReglement);
				params.addElement(type);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.modifierContrat", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}
	
	/**
	 * Ajouter un document à un contrat
	 * @param String idContrat
	 * @param String idDoc
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean ajouterDocumentContrat(String idContrat, String idDoc) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Ajout du document au contrat...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idContrat);
				params.addElement(idDoc);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.ajouterDocumentContrat", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Retirer un document d'un contrat
	 * @param String idContrat
	 * @param String idDoc
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean retirerDocumentContrat(String idContrat, String idDoc) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Retirer un document du contrat...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idContrat);
				params.addElement(idDoc);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.retirerDocumentContrat", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}
	
//	CONTRACTANT
//------------------------------------------------------------	
	
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
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean creerContractant(String nom, String adresse, String codePostal,
			String ville, String telephone, String fax, String mail, String type) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Création d'un contractant...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(nom);
				params.addElement(adresse);
				params.addElement(codePostal);
				params.addElement(ville);
				params.addElement(telephone);
				params.addElement(fax);
				params.addElement(mail);
				params.addElement(type);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.creerContractant", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Lister les contractants disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerContractants() {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Liste des contractants...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				return (Vector) clientXML.execute("Systeme.listerContractants",
						params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}
	
	/**
	 * Informations d'un contractant
	 * @param String id
	 * @return Dictionary
	 */
	@SuppressWarnings("unchecked")
	public Dictionary infoContractant(String id) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Informations sur un contractant...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				return (Dictionary) clientXML.execute("Systeme.infoContractant",
						params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}
	
	/**
	 * Rechercher un contractant
	 * @param String id
	 * @param String nom
	 * @param String adresse
	 * @param String codePostal
	 * @param String ville
	 * @param String telephone
	 * @param String fax
	 * @param String mail
	 * @param String type
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector rechercherContractant(String id, String nom, String adresse,
			String codePostal, String ville, String telephone, String fax,
			String mail, String type) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Recherche des contractants...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(nom);
				params.addElement(adresse);
				params.addElement(codePostal);
				params.addElement(ville);
				params.addElement(telephone);
				params.addElement(fax);
				params.addElement(mail);
				params.addElement(type);

				// Adresse la requête et affiche les résultats
				return (Vector) clientXML.execute("Systeme.rechercherContractants",
						params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}

	/**
	 * Supprimer un contractant
	 * @param String id
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean supprimerContractant(String id) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Suppression d'un contractant...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerContractant", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Modification d'un contractant
	 * @param String id
	 * @param String nom
	 * @param String adresse
	 * @param String codePostal
	 * @param String ville
	 * @param String telephone
	 * @param String fax
	 * @param String mail
	 * @param String type
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean modifierContractant(String id, String nom, String adresse,
			String codePostal, String ville, String telephone, String fax,
			String mail, String type) {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.out.println("INFO: Modification d'un contractant...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(nom);
				params.addElement(adresse);
				params.addElement(codePostal);
				params.addElement(ville);
				params.addElement(telephone);
				params.addElement(fax);
				params.addElement(mail);
				params.addElement(type);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.modifierContractant", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

//ROLE
//------------------------------------------------------------	
	
	/**
	 * Création d'un rôle
	 * @param String id
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean creerRole(String id) {

		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Création d'un rôle...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute("Systeme.creerRole", params);

				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Lister les rôles disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerRoles() {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Liste des rôles...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				return (Vector) clientXML.execute("Systeme.listerRoles", params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}
	
	/**
	 * Informations d'un rôle
	 * @param String id
	 * @return Dictionary
	 */
	@SuppressWarnings("unchecked")
	public Dictionary infoRole(String id) {

		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Informations sur un rôle...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				return (Dictionary) clientXML.execute("Systeme.infoRole", params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}

	/**
	 * Supprimer un rôle
	 * @param String id
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean supprimerRole(String id) {

		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Suppression d'un rôle...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute("Systeme.supprimerRole", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}
	
	/**
	 * Ajouter une permssion à un rôle
	 * @param String idPermission
	 * @param String idRole
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean ajouterPermissionRole(String idPermission, String idRole) {

		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Ajout d'une permission à un rôle...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idRole);
				params.addElement(idPermission);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute("Systeme.ajouterPermissionRole", params);

				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Retirer une permission à un rôle
	 * @param String idPermission
	 * @param String idRole
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean retirerPermissionRole(String idPermission, String idRole) {

		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Retirer une permission à un rôle...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idRole);
				params.addElement(idPermission);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute("Systeme.retirerPermissionRole", params);

				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}
	
//PERMISSION
//------------------------------------------------------------	
		
	/**
	 * Création d'une permission
	 * @param String id
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean creerPermission(String id, String libelle) {

		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Création d'une permission...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(libelle);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute("Systeme.creerPermission", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
				// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}

	/**
	 * Lister les permissions disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerPermissions() {
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Liste des permissions...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				return (Vector) clientXML.execute("Systeme.listerPermissions", params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}
		
	/**
	 * Informations d'une permission
	 * @param String id
	 * @return Dictionary
	 */
	@SuppressWarnings("unchecked")
	public Dictionary infoPermission(String id) {

		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Informations sur une permission...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				return (Dictionary) clientXML.execute("Systeme.infoPermission", params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

		// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}

	/**
	 * Supprimer une permission
	 * @param String id
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean supprimerPermission(String id) {

		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Suppression d'une permission...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute("Systeme.supprimerPermission", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;

			}
		// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}
	
	/**
	 * Rechercher une permission
	 * @param String id
	 * @param String libelle
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector rechercherPermission(String id, String libelle) {

		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Recherche des permissions...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(libelle);

				// Adresse la requête et affiche les résultats
				return (Vector) clientXML.execute("Systeme.rechercherPermissions", params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return null;
		}
	}
	
	/**
	 * Modification d'une permission
	 * @param String id
	 * @param String libelle
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean modifierPermission(String id, String libelle) {

		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			System.err.println("INFO: Modification d'une permission...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(libelle);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute("Systeme.modifierPermission", params);

				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connecté
		} else {
			System.err.println("WARNING: Client non connecté au serveur !");
			return false;
		}
	}
}

/**
 * "ping"
 */
class PingXML{
	

	public PingXML() {
	}
	
	/**
	 * Signale au serveur que le client est bien connecté
	 * @return String
	 */
	public String testConnectXML() {
		System.out.println("Test de connexion envoyé par serveur NetJukeBox");
		return Boolean.toString(true);
	}

}