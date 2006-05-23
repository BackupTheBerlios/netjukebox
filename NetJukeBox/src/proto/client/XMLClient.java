package proto.client;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.util.Dictionary;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.xmlrpc.WebServer;
import org.apache.xmlrpc.XmlRpc;
import org.apache.xmlrpc.XmlRpcClient;

import proto.serveur.Systeme;
import proto.serveur.Utilisateur;


/**
 * Client XML (envoie les requêtes au serveur XML du serveur principal)
 * 
 * @author philippeB
 */
public class XMLClient {
	/**
	 * Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(XMLClient.class);

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
		
		//Fichier de configuration de la journalisation
		//PropertyConfigurator.configure("src/proto/serveur/log4j.prop");
		//PropertyConfigurator.configure("/home/netjukebox/Workspace/NetJukeBox/proto/serveur/log4j.prop");
		PropertyConfigurator.configure("C:/Documents and Settings/Marie Rubini/Mes documents/workspace/NetJukeBox/proto/serveur/log4jClient.prop");
		
		logger.debug("Démarrage: XMLCllient");
		// Si le client XML n'est pas déjà initialisé
		if (clientXML == null) {
			logger.info("INFO: Initialisation XMLRPC en cours...");
			try {
				// Utilise lengthpilote Sax d'Apache Xerces
				XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");

				// Creation du client & identification du serveur
				clientXML = new XmlRpcClient("http://" + ip + ":" + port);
				
				//Creation du serveur xmlrpc
				WebServer server = new WebServer(Integer.parseInt(port));
				
				// Enregistre la classe du gestionnaire
				server.addHandler("PingXML", new PingXML());

				logger.info("INFO: Client Streaming démarré");

			} catch (ClassNotFoundException e) {
				logger.error("ERREUR: Impossible de localiser le pilote Sax");
				logger.debug("Arrêt: XMLCllient");
			} catch (MalformedURLException e) {
				logger.error("ERREUR: URL non conforme au format du serveur XML-RPC");
				logger.debug("Arrêt: XMLCllient");
			} catch (Exception e) {
				logger.error("ERREUR: "+ e);
				logger.debug("Arrêt: XMLCllient");
			}

			// Sinon, déjà initialisé
		} else {
			logger.warn("WARNING: Client XML déjà initialisé !");
			logger.debug("Arrêt: XMLCllient");
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
		logger.debug("Démarrage: testConnectXML");
		logger.info("INFO: Tentative de connexion au serveur XML...");
		try {
			// Création de la requête
			Vector params = new Vector();
			params.addElement(ip);

			// Adresse la requête et affiche les résultats
			String result = (String) clientXML.execute(
					"Systeme.testConnectXML", params);
			
			logger.debug("Arrêt: testConnectXML");
			return Boolean.parseBoolean(result);			

		} catch (Exception e) {
			logger.error("ERREUR : " + e);
			logger.debug("Arrêt: testConnectXML");
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
		logger.debug("Démarrage: creerCanal");
		// Si l'utilisateur est connecté au seveur
		
		if (etatConnecte) {
			logger.info("INFO: Création d'un canal...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(nom);
				params.addElement(utilMax);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.creerCanal", params);
				logger.debug("Arrêt: creerCanal");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: creerCanal");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: creerCanal");
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
		logger.debug("Démarrage: diffuserProgramme");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Diffusion du programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idProg);
				params.addElement(idCanal);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.diffuserProgramme", params);
				logger.debug("Arrêt: diffuserProgramme");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: diffuserProgramme");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: diffuserProgramme");
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
		logger.debug("Démarrage: planifierProgramme");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Planification du programme...");
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
				logger.debug("Arrêt: planifierProgramme");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: planifierProgramme");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: planifierProgramme");
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
		logger.debug("Démarrage: deplanifierProgramme");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Déplanification du programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idCanal);
				params.addElement(calage);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.deplanifierProgramme", params);
				logger.debug("Arrêt: deplanifierProgramme");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: deplanifierProgramme");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: deplanifierProgramme");
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
		logger.debug("Démarrage: ecouterCanal");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Ecoute d'un canal...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idCanal);

				// Adresse la requête et affiche les résultats
				logger.debug("Arrêt: ecouterCanal");
				return (String) clientXML.execute("Systeme.ecouterCanal",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: ecouterCanal");
				return null;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: ecouterCanal");
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
		logger.debug("Démarrage: startCanal ");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Diffusion d'un canal...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idCanal);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.startCanal", params);
				logger.debug("Arrêt: startCanal ");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.warn("ERREUR : " + e);
				logger.debug("Arrêt: startCanal ");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: startCanal ");
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
		logger.debug("Démarrage: stopCanal");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Arrêt d'un canal...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idCanal);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute("Systeme.stopCanal",
						params);
				logger.debug("Arrêt: stopCanal");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: stopCanal");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: stopCanal");
			return false;
		}
	}

	/**
	 * Lister les canaux disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerCanaux() {
		logger.debug("Démarrage: listerCanaux");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Liste des canaux...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				logger.debug("Arrêt: listerCanaux");
				return (Vector) clientXML.execute("Systeme.listerCanaux",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: listerCanaux");
				return null;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: listerCanaux");
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
		logger.debug("Démarrage: rechercherCanal");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Recherche des canaux...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(nom);
				params.addElement(utilMax);

				// Adresse la requête et affiche les résultats
				logger.debug("Arrêt: rechercherCanal");
				return (Vector) clientXML.execute("Systeme.rechercherCanal",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: rechercherCanal");
				return null;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: rechercherCanal");
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
		logger.debug("Démarrage: supprimerCanal");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Suppression d'un canal...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerCanal", params);
				logger.debug("Arrêt: supprimerCanal");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: supprimerCanal");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: supprimerCanal");
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
		logger.debug("Démarrage: modifierCanal");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Modification d'un canal...");
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
				logger.debug("Arrêt: modifierCanal");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: modifierCanal");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: modifierCanal");
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
		logger.debug("Démarrage: infoCanal");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Informations sur un canal...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				logger.debug("Arrêt: infoCanal");
				return (Dictionary) clientXML.execute("Systeme.infoCanal",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: infoCanal");
				return null;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: infoCanal");
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
		logger.debug("Démarrage: creerProgramme");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Création d'un programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(titre);
				params.addElement(thematique);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.creerProgramme", params);
				logger.debug("Arrêt: creerProgramme");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: creerProgramme");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: creerProgramme");
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
		logger.debug("Démarrage: ajouterDocumentProgramme");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Ajout du document au programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idDoc);
				params.addElement(idProg);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.ajouterDocumentProgramme", params);
				logger.debug("Arrêt: ajouterDocumentProgramme");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: ajouterDocumentProgramme");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: ajouterDocumentProgramme");
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
		logger.debug("Démarrage: retirerDocumentProgramme");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Retirer un document du programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idProg);
				params.addElement(calage);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.retirerDocumentProgramme", params);
				logger.debug("Arrêt: retirerDocumentProgramme");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: retirerDocumentProgramme");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: retirerDocumentProgramme");
			return false;
		}
	}

	/**
	 * Lister les programmes disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerProgrammes() {
		logger.debug("Démarrage: listerProgrammes");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Liste des programmes...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				logger.debug("Arrêt: listerProgrammes");
				return (Vector) clientXML.execute("Systeme.listerProgrammes",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: listerProgrammes");
				return null;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: listerProgrammes");
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
		logger.debug("Démarrage: rechercherProgramme");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Recherche des programmes...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(titre);
				params.addElement(thematique);

				// Adresse la requête et affiche les résultats
				logger.debug("Arrêt: rechercherProgramme");
				return (Vector) clientXML.execute(
						"Systeme.rechercherProgramme", params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: rechercherProgramme");
				return null;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: rechercherProgramme");
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
		logger.debug("Démarrage: rechercherProgramme");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Suppression d'un programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerProgramme", params);
				logger.debug("Arrêt: rechercherProgramme");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: rechercherProgramme");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: rechercherProgramme");
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
		logger.debug("Démarrage: modifierProgramme");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Modification d'un programme...");
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
				logger.debug("Arrêt: modifierProgramme");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: modifierProgramme");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: modifierProgramme");
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
		logger.debug("Démarrage: infoProgramme");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Informations sur un programme...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				logger.debug("Arrêt: infoProgramme");
				return (Dictionary) clientXML.execute("Systeme.infoProgramme",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: infoProgramme");
				return null;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: infoProgramme");
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
		logger.debug("Démarrage: creerDocument");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Création d'un document...");
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
				logger.debug("Arrêt: creerDocument");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: creerDocument");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: creerDocument");
			return false;
		}
	}

	/**
	 * Lister les documents disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerDocuments() {
		logger.debug("Démarrage: listerDocuments");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Liste des documents...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				logger.debug("Arrêt: listerDocuments");
				return (Vector) clientXML.execute("Systeme.listerDocuments",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: listerDocuments");
				return null;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: listerDocuments");
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
		logger.debug("Démarrage: rechercherDocument");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Recherche des documents...");
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
				logger.debug("Arrêt: rechercherDocument");
				return (Vector) clientXML.execute("Systeme.rechercherDocument",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: rechercherDocument");
				return null;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: rechercherDocument");
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
		logger.debug("Démarrage: supprimerDocument");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Suppression d'un document...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerDocument", params);
				logger.debug("Arrêt: supprimerDocument");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: supprimerDocument");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: supprimerDocument");
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
		logger.debug("Démarrage: modifierDocument");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Modification d'un document...");
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
				logger.debug("Arrêt: modifierDocument");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: modifierDocument");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: modifierDocument");
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
		logger.debug("Démarrage: infoDocument");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Informations sur un document...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				logger.debug("Arrêt: infoDocument");
				return (Dictionary) clientXML.execute("Systeme.infoDocument",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: infoDocument");
				return null;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: infoDocument");
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
		logger.debug("Démarrage: connexion");
		// Si l'utilisateur n'est pas connecté au seveur
		if (!etatConnecte) {
			logger.info("INFO: Connexion en cours...");
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
				logger.debug("Arrêt: connexion");
				return etatConnecte;

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: connexion");
				return false;
			}

			// Sinon, déjà connecté
		} else {
			logger.warn("WARNING: Client déjà connecté au serveur !");
			logger.debug("Arrêt: connexion");
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
		logger.debug("Démarrage: deconnexion");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Déconnexion en cours...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.deconnexion", params);
				boolean estDeconnecte = Boolean.parseBoolean(result);

				etatConnecte = !estDeconnecte;
				logger.debug("Arrêt: deconnexion");
				return estDeconnecte;

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: deconnexion");
				return false;
			}

			// Sinon, n'est pas connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: deconnexion");
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
		logger.debug("Démarrage: inscription");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Inscription d'un utilisateur...");
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
				logger.debug("Arrêt: inscription");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: inscription");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: inscription");
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
		logger.debug("Démarrage:  ");
		if (etatConnecte) {
			logger.info("INFO: Suppression du compte ...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerUtilisateur", params);
				logger.debug("Arrêt: supprimerUtilisateur");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: supprimerUtilisateur");
				return false;
			}
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: supprimerUtilisateur");
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
		logger.debug("Démarrage: rechercherUtilisateur");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Recherche des attributs...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				logger.debug("Arrêt: rechercherUtilisateur");
				return (Vector) clientXML.execute(
						"Systeme.rechercherUtilisateur", params);
			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: rechercherUtilisateur");
				return null;
			}
			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: rechercherUtilisateur");
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
		logger.debug("Démarrage: modifierUtilisateur");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Modification des attributs...");
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
				logger.debug("Arrêt: modifierUtilisateur");
				return true;

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: modifierUtilisateur");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: modifierUtilisateur");
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
		logger.debug("Démarrage: recherchepwd");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Recherche du mot de passe...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.rechercherpwd", params);
				logger.debug("Arrêt: recherchepwd");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: recherchepwd");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: recherchepwd");
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
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: recherchepwd");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: recherchepwd");
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
		logger.debug("Démarrage: creerContrat");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Création d'un contrat...");
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
				logger.debug("Arrêt: creerContrat");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: creerContrat");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: creerContrat");
			return false;
		}
	}

	/**
	 * Lister les contractants disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerContrats() {
		logger.debug("Démarrage: listerContrats");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Liste des contrats...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				logger.debug("Arrêt: listerContrats");
				return (Vector) clientXML.execute("Systeme.listerContrats",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: listerContrats");
				return null;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: listerContrats");
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
		logger.debug("Démarrage: infoContrat");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Informations sur un contrat...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				logger.debug("Arrêt: infoContrat");
				return (Dictionary) clientXML.execute("Systeme.infoContrat",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: infoContrat");
				return null;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: infoContrat");
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
		logger.debug("Démarrage: rechercherContrat");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Recherche des contrats...");
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
				logger.debug("Arrêt: rechercherContrat");
				return (Vector) clientXML.execute("Systeme.rechercherContrats",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: rechercherContrat");
				return null;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: rechercherContrat");
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
		logger.debug("Démarrage: supprimerContrat");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Suppression d'un contrat...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerContrat", params);
				logger.debug("Arrêt: supprimerContrat");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: supprimerContrat");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: supprimerContrat");
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
		logger.debug("Démarrage: modifierContrat");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Modification d'un contrat...");
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
				logger.debug("Arrêt: modifierContrat");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: modifierContrat");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: modifierContrat");
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
		logger.debug("Démarrage: ajouterDocumentContrat");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Ajout du document au contrat...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idContrat);
				params.addElement(idDoc);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.ajouterDocumentContrat", params);
				logger.debug("Arrêt: ajouterDocumentContrat");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: ajouterDocumentContrat");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: ajouterDocumentContrat");
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
		logger.debug("Démarrage: retirerDocumentContrat");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Retirer un document du contrat...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idContrat);
				params.addElement(idDoc);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.retirerDocumentContrat", params);
				logger.debug("Arrêt: retirerDocumentContrat");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: retirerDocumentContrat");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: retirerDocumentContrat");
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
		logger.debug("Démarrage: creerContractant");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Création d'un contractant...");
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
				logger.debug("Arrêt: creerContractant");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: creerContractant");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: creerContractant");
			return false;
		}
	}

	/**
	 * Lister les contractants disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerContractants() {
		logger.debug("Démarrage: listerContractants");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Liste des contractants...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requête et affiche les résultats
				logger.debug("Arrêt: listerContractants");
				return (Vector) clientXML.execute("Systeme.listerContractants",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: listerContractants");
				return null;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: listerContractants");
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
		logger.debug("Démarrage: infoContractant");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Informations sur un contractant...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				logger.debug("Arrêt: infoContractant");
				return (Dictionary) clientXML.execute("Systeme.infoContractant",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: infoContractant");
				return null;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: infoContractant");
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
		logger.debug("Démarrage: rechercherContractant");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Recherche des contractants...");
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
				logger.debug("Arrêt: rechercherContractant");
				return (Vector) clientXML.execute("Systeme.rechercherContractants",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: rechercherContractant");
				return null;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: rechercherContractant");
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
		logger.debug("Démarrage: supprimerContractant ");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Suppression d'un contractant...");
			try {
				// Création de la requête
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requête et affiche les résultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerContractant", params);
				logger.debug("Arrêt: supprimerContractant ");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: supprimerContractant ");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: supprimerContractant ");
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
		logger.debug("Démarrage: modifierContractant");
		// Si l'utilisateur est connecté au seveur
		if (etatConnecte) {
			logger.info("INFO: Modification d'un contractant...");
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
				logger.debug("Arrêt: modifierContractant");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arrêt: modifierContractant");
				return false;
			}

			// Sinon, non connecté
		} else {
			logger.warn("WARNING: Client non connecté au serveur !");
			logger.debug("Arrêt: modifierContractant");
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