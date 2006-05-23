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
 * Client XML (envoie les requ�tes au serveur XML du serveur principal)
 * 
 * @author philippeB
 */
public class XMLClient {
	/**
	 * Cr�e le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(XMLClient.class);

// ATTRIBUTS
// **************************************

	/**
	 * Client XMLRPC
	 */
	private XmlRpcClient clientXML = null;

	/**
	 * Etat connect�
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
		
		logger.debug("D�marrage: XMLCllient");
		// Si le client XML n'est pas d�j� initialis�
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

				logger.info("INFO: Client Streaming d�marr�");

			} catch (ClassNotFoundException e) {
				logger.error("ERREUR: Impossible de localiser le pilote Sax");
				logger.debug("Arr�t: XMLCllient");
			} catch (MalformedURLException e) {
				logger.error("ERREUR: URL non conforme au format du serveur XML-RPC");
				logger.debug("Arr�t: XMLCllient");
			} catch (Exception e) {
				logger.error("ERREUR: "+ e);
				logger.debug("Arr�t: XMLCllient");
			}

			// Sinon, d�j� initialis�
		} else {
			logger.warn("WARNING: Client XML d�j� initialis� !");
			logger.debug("Arr�t: XMLCllient");
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
		logger.debug("D�marrage: testConnectXML");
		logger.info("INFO: Tentative de connexion au serveur XML...");
		try {
			// Cr�ation de la requ�te
			Vector params = new Vector();
			params.addElement(ip);

			// Adresse la requ�te et affiche les r�sultats
			String result = (String) clientXML.execute(
					"Systeme.testConnectXML", params);
			
			logger.debug("Arr�t: testConnectXML");
			return Boolean.parseBoolean(result);			

		} catch (Exception e) {
			logger.error("ERREUR : " + e);
			logger.debug("Arr�t: testConnectXML");
			return false;
		}
	}

// CANAL
// ------------------------------------------------------------
	/**
	 * Cr�ation d'un canal
	 * @param String nom
	 * @param String utilMax
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean creerCanal(String nom, String utilMax) {
		logger.debug("D�marrage: creerCanal");
		// Si l'utilisateur est connect� au seveur
		
		if (etatConnecte) {
			logger.info("INFO: Cr�ation d'un canal...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(nom);
				params.addElement(utilMax);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.creerCanal", params);
				logger.debug("Arr�t: creerCanal");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: creerCanal");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: creerCanal");
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
		logger.debug("D�marrage: diffuserProgramme");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Diffusion du programme...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idProg);
				params.addElement(idCanal);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.diffuserProgramme", params);
				logger.debug("Arr�t: diffuserProgramme");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: diffuserProgramme");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: diffuserProgramme");
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
		logger.debug("D�marrage: planifierProgramme");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Planification du programme...");
			try {
				// Cr�ation de la requ�te
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

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.planifierProgramme", params);
				logger.debug("Arr�t: planifierProgramme");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: planifierProgramme");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: planifierProgramme");
			return false;
		}
	}

	/**
	 * D�planifier un programme sur un canal
	 * @param String idCanal
	 * @param String calage
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean deplanifierProgramme(String idCanal, String calage) {
		logger.debug("D�marrage: deplanifierProgramme");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: D�planification du programme...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idCanal);
				params.addElement(calage);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.deplanifierProgramme", params);
				logger.debug("Arr�t: deplanifierProgramme");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: deplanifierProgramme");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: deplanifierProgramme");
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
		logger.debug("D�marrage: ecouterCanal");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Ecoute d'un canal...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idCanal);

				// Adresse la requ�te et affiche les r�sultats
				logger.debug("Arr�t: ecouterCanal");
				return (String) clientXML.execute("Systeme.ecouterCanal",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: ecouterCanal");
				return null;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: ecouterCanal");
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
		logger.debug("D�marrage: startCanal ");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Diffusion d'un canal...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idCanal);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.startCanal", params);
				logger.debug("Arr�t: startCanal ");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.warn("ERREUR : " + e);
				logger.debug("Arr�t: startCanal ");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: startCanal ");
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
		logger.debug("D�marrage: stopCanal");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Arr�t d'un canal...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idCanal);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute("Systeme.stopCanal",
						params);
				logger.debug("Arr�t: stopCanal");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: stopCanal");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: stopCanal");
			return false;
		}
	}

	/**
	 * Lister les canaux disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerCanaux() {
		logger.debug("D�marrage: listerCanaux");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Liste des canaux...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requ�te et affiche les r�sultats
				logger.debug("Arr�t: listerCanaux");
				return (Vector) clientXML.execute("Systeme.listerCanaux",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: listerCanaux");
				return null;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: listerCanaux");
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
		logger.debug("D�marrage: rechercherCanal");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Recherche des canaux...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(nom);
				params.addElement(utilMax);

				// Adresse la requ�te et affiche les r�sultats
				logger.debug("Arr�t: rechercherCanal");
				return (Vector) clientXML.execute("Systeme.rechercherCanal",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: rechercherCanal");
				return null;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: rechercherCanal");
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
		logger.debug("D�marrage: supprimerCanal");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Suppression d'un canal...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerCanal", params);
				logger.debug("Arr�t: supprimerCanal");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: supprimerCanal");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: supprimerCanal");
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
		logger.debug("D�marrage: modifierCanal");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Modification d'un canal...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(nom);
				params.addElement(utilMax);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.modifierCanal", params);
				logger.debug("Arr�t: modifierCanal");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: modifierCanal");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: modifierCanal");
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
		logger.debug("D�marrage: infoCanal");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Informations sur un canal...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requ�te et affiche les r�sultats
				logger.debug("Arr�t: infoCanal");
				return (Dictionary) clientXML.execute("Systeme.infoCanal",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: infoCanal");
				return null;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: infoCanal");
			return null;
		}
	}

// PROGRAMME
// ------------------------------------------------------------

	/**
	 * Cr�ation d'un programme
	 * @param String titre
	 * @param String thematique
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean creerProgramme(String titre, String thematique) {
		logger.debug("D�marrage: creerProgramme");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Cr�ation d'un programme...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(titre);
				params.addElement(thematique);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.creerProgramme", params);
				logger.debug("Arr�t: creerProgramme");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: creerProgramme");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: creerProgramme");
			return false;
		}
	}

	/**
	 * Ajouter un document � un programme
	 * @param String idDoc
	 * @param String idProg
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean ajouterDocumentProgramme(String idDoc, String idProg) {
		logger.debug("D�marrage: ajouterDocumentProgramme");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Ajout du document au programme...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idDoc);
				params.addElement(idProg);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.ajouterDocumentProgramme", params);
				logger.debug("Arr�t: ajouterDocumentProgramme");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: ajouterDocumentProgramme");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: ajouterDocumentProgramme");
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
		logger.debug("D�marrage: retirerDocumentProgramme");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Retirer un document du programme...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idProg);
				params.addElement(calage);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.retirerDocumentProgramme", params);
				logger.debug("Arr�t: retirerDocumentProgramme");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: retirerDocumentProgramme");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: retirerDocumentProgramme");
			return false;
		}
	}

	/**
	 * Lister les programmes disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerProgrammes() {
		logger.debug("D�marrage: listerProgrammes");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Liste des programmes...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requ�te et affiche les r�sultats
				logger.debug("Arr�t: listerProgrammes");
				return (Vector) clientXML.execute("Systeme.listerProgrammes",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: listerProgrammes");
				return null;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: listerProgrammes");
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
		logger.debug("D�marrage: rechercherProgramme");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Recherche des programmes...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(titre);
				params.addElement(thematique);

				// Adresse la requ�te et affiche les r�sultats
				logger.debug("Arr�t: rechercherProgramme");
				return (Vector) clientXML.execute(
						"Systeme.rechercherProgramme", params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: rechercherProgramme");
				return null;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: rechercherProgramme");
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
		logger.debug("D�marrage: rechercherProgramme");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Suppression d'un programme...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerProgramme", params);
				logger.debug("Arr�t: rechercherProgramme");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: rechercherProgramme");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: rechercherProgramme");
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
		logger.debug("D�marrage: modifierProgramme");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Modification d'un programme...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(titre);
				params.addElement(thematique);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.modifierProgramme", params);
				logger.debug("Arr�t: modifierProgramme");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: modifierProgramme");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: modifierProgramme");
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
		logger.debug("D�marrage: infoProgramme");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Informations sur un programme...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requ�te et affiche les r�sultats
				logger.debug("Arr�t: infoProgramme");
				return (Dictionary) clientXML.execute("Systeme.infoProgramme",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: infoProgramme");
				return null;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: infoProgramme");
			return null;
		}
	}

// DOCUMENT
// ------------------------------------------------------------

	/**
	 * Cr�ation d'un document
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
		logger.debug("D�marrage: creerDocument");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Cr�ation d'un document...");
			try {
				// Cr�ation de la requ�te
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

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.creerDocument", params);
				logger.debug("Arr�t: creerDocument");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: creerDocument");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: creerDocument");
			return false;
		}
	}

	/**
	 * Lister les documents disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerDocuments() {
		logger.debug("D�marrage: listerDocuments");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Liste des documents...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requ�te et affiche les r�sultats
				logger.debug("Arr�t: listerDocuments");
				return (Vector) clientXML.execute("Systeme.listerDocuments",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: listerDocuments");
				return null;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: listerDocuments");
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
		logger.debug("D�marrage: rechercherDocument");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Recherche des documents...");
			try {
				// Cr�ation de la requ�te
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

				// Adresse la requ�te et affiche les r�sultats
				logger.debug("Arr�t: rechercherDocument");
				return (Vector) clientXML.execute("Systeme.rechercherDocument",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: rechercherDocument");
				return null;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: rechercherDocument");
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
		logger.debug("D�marrage: supprimerDocument");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Suppression d'un document...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerDocument", params);
				logger.debug("Arr�t: supprimerDocument");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: supprimerDocument");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: supprimerDocument");
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
		logger.debug("D�marrage: modifierDocument");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Modification d'un document...");
			try {
				// Cr�ation de la requ�te
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

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.modifierDocument", params);
				logger.debug("Arr�t: modifierDocument");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: modifierDocument");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: modifierDocument");
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
		logger.debug("D�marrage: infoDocument");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Informations sur un document...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requ�te et affiche les r�sultats
				logger.debug("Arr�t: infoDocument");
				return (Dictionary) clientXML.execute("Systeme.infoDocument",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: infoDocument");
				return null;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: infoDocument");
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
		logger.debug("D�marrage: connexion");
		// Si l'utilisateur n'est pas connect� au seveur
		if (!etatConnecte) {
			logger.info("INFO: Connexion en cours...");
			this.login = login;
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(pwd);
				params.addElement(InetAddress.getLocalHost().getHostAddress());

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute("Systeme.connexion",
						params);

				etatConnecte = Boolean.parseBoolean(result);
				logger.debug("Arr�t: connexion");
				return etatConnecte;

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: connexion");
				return false;
			}

			// Sinon, d�j� connect�
		} else {
			logger.warn("WARNING: Client d�j� connect� au serveur !");
			logger.debug("Arr�t: connexion");
			return false;
		}
	}

	/**
	 * D�connecte l'utilisateur du serveur principal
	 * @param String login
	 * @param String pwd
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean deconnexion() {
		logger.debug("D�marrage: deconnexion");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: D�connexion en cours...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.deconnexion", params);
				boolean estDeconnecte = Boolean.parseBoolean(result);

				etatConnecte = !estDeconnecte;
				logger.debug("Arr�t: deconnexion");
				return estDeconnecte;

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: deconnexion");
				return false;
			}

			// Sinon, n'est pas connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: deconnexion");
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
		logger.debug("D�marrage: inscription");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Inscription d'un utilisateur...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(log);
				params.addElement(pass);
				params.addElement(role);
				params.addElement(email);
				params.addElement(nom);
				params.addElement(prenom);
				params.addElement(pays);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.inscription", params);
				logger.debug("Arr�t: inscription");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: inscription");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: inscription");
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
		logger.debug("D�marrage:  ");
		if (etatConnecte) {
			logger.info("INFO: Suppression du compte ...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerUtilisateur", params);
				logger.debug("Arr�t: supprimerUtilisateur");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: supprimerUtilisateur");
				return false;
			}
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: supprimerUtilisateur");
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
		logger.debug("D�marrage: rechercherUtilisateur");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Recherche des attributs...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requ�te et affiche les r�sultats
				logger.debug("Arr�t: rechercherUtilisateur");
				return (Vector) clientXML.execute(
						"Systeme.rechercherUtilisateur", params);
			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: rechercherUtilisateur");
				return null;
			}
			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: rechercherUtilisateur");
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
		logger.debug("D�marrage: modifierUtilisateur");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Modification des attributs...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(newlogin);
				params.addElement(pwd);
				params.addElement(Nom);
				params.addElement(Prenom);
				params.addElement(Email);
				params.addElement(Pays);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.modifierUtilisateur", params);

				// return Boolean.parseBoolean(result);
				logger.debug("Arr�t: modifierUtilisateur");
				return true;

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: modifierUtilisateur");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: modifierUtilisateur");
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
		logger.debug("D�marrage: recherchepwd");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Recherche du mot de passe...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.rechercherpwd", params);
				logger.debug("Arr�t: recherchepwd");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: recherchepwd");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: recherchepwd");
			return false;
		}
	}
	
	/**
	 * Ajouter une permssion � un utilisateur
	 * @param String idPermission
	 * @param String logUtil
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean ajouterPermissionUtilisateur(String idPermission, String logUtil) {

		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Ajout d'une permission � un utilisateur...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(logUtil);
				params.addElement(idPermission);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute("Systeme.ajouterPermissionUtilisateur", params);

				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: recherchepwd");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: recherchepwd");
			return false;
		}
	}

	/**
	 * Retirer une permission � un utilisateur
	 * @param String idPermission
	 * @param String logUtil
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean retirerPermissionUtilisateur(String idPermission, String logUtil) {

		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Retirer une permission � un utilisateur...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(logUtil);
				params.addElement(idPermission);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute("Systeme.retirerPermissionUtilisateur", params);

				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return false;
		}
	}

// CONTRAT
//------------------------------------------------------------

	/**
	 * Cr�ation d'un contrat
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
		logger.debug("D�marrage: creerContrat");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Cr�ation d'un contrat...");
			try {
				// Cr�ation de la requ�te
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

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.creerContrat", params);
				logger.debug("Arr�t: creerContrat");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: creerContrat");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: creerContrat");
			return false;
		}
	}

	/**
	 * Lister les contractants disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerContrats() {
		logger.debug("D�marrage: listerContrats");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Liste des contrats...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requ�te et affiche les r�sultats
				logger.debug("Arr�t: listerContrats");
				return (Vector) clientXML.execute("Systeme.listerContrats",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: listerContrats");
				return null;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: listerContrats");
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
		logger.debug("D�marrage: infoContrat");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Informations sur un contrat...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requ�te et affiche les r�sultats
				logger.debug("Arr�t: infoContrat");
				return (Dictionary) clientXML.execute("Systeme.infoContrat",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: infoContrat");
				return null;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: infoContrat");
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
		logger.debug("D�marrage: rechercherContrat");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Recherche des contrats...");
			try {
				// Cr�ation de la requ�te
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

				// Adresse la requ�te et affiche les r�sultats
				logger.debug("Arr�t: rechercherContrat");
				return (Vector) clientXML.execute("Systeme.rechercherContrats",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: rechercherContrat");
				return null;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: rechercherContrat");
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
		logger.debug("D�marrage: supprimerContrat");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Suppression d'un contrat...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerContrat", params);
				logger.debug("Arr�t: supprimerContrat");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: supprimerContrat");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: supprimerContrat");
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
		logger.debug("D�marrage: modifierContrat");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Modification d'un contrat...");
			try {
				// Cr�ation de la requ�te
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

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.modifierContrat", params);
				logger.debug("Arr�t: modifierContrat");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: modifierContrat");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: modifierContrat");
			return false;
		}
	}
	
	/**
	 * Ajouter un document � un contrat
	 * @param String idContrat
	 * @param String idDoc
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean ajouterDocumentContrat(String idContrat, String idDoc) {
		logger.debug("D�marrage: ajouterDocumentContrat");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Ajout du document au contrat...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idContrat);
				params.addElement(idDoc);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.ajouterDocumentContrat", params);
				logger.debug("Arr�t: ajouterDocumentContrat");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: ajouterDocumentContrat");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: ajouterDocumentContrat");
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
		logger.debug("D�marrage: retirerDocumentContrat");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Retirer un document du contrat...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idContrat);
				params.addElement(idDoc);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.retirerDocumentContrat", params);
				logger.debug("Arr�t: retirerDocumentContrat");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: retirerDocumentContrat");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: retirerDocumentContrat");
			return false;
		}
	}
	
//	CONTRACTANT
//------------------------------------------------------------	
	
	/**
	 * Cr�ation d'un contractant
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
		logger.debug("D�marrage: creerContractant");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Cr�ation d'un contractant...");
			try {
				// Cr�ation de la requ�te
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

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.creerContractant", params);
				logger.debug("Arr�t: creerContractant");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: creerContractant");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: creerContractant");
			return false;
		}
	}

	/**
	 * Lister les contractants disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerContractants() {
		logger.debug("D�marrage: listerContractants");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Liste des contractants...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requ�te et affiche les r�sultats
				logger.debug("Arr�t: listerContractants");
				return (Vector) clientXML.execute("Systeme.listerContractants",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: listerContractants");
				return null;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: listerContractants");
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
		logger.debug("D�marrage: infoContractant");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Informations sur un contractant...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requ�te et affiche les r�sultats
				logger.debug("Arr�t: infoContractant");
				return (Dictionary) clientXML.execute("Systeme.infoContractant",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: infoContractant");
				return null;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: infoContractant");
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
		logger.debug("D�marrage: rechercherContractant");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Recherche des contractants...");
			try {
				// Cr�ation de la requ�te
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

				// Adresse la requ�te et affiche les r�sultats
				logger.debug("Arr�t: rechercherContractant");
				return (Vector) clientXML.execute("Systeme.rechercherContractants",
						params);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: rechercherContractant");
				return null;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: rechercherContractant");
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
		logger.debug("D�marrage: supprimerContractant ");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Suppression d'un contractant...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.supprimerContractant", params);
				logger.debug("Arr�t: supprimerContractant ");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: supprimerContractant ");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: supprimerContractant ");
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
		logger.debug("D�marrage: modifierContractant");
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			logger.info("INFO: Modification d'un contractant...");
			try {
				// Cr�ation de la requ�te
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

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute(
						"Systeme.modifierContractant", params);
				logger.debug("Arr�t: modifierContractant");
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				logger.error("ERREUR : " + e);
				logger.debug("Arr�t: modifierContractant");
				return false;
			}

			// Sinon, non connect�
		} else {
			logger.warn("WARNING: Client non connect� au serveur !");
			logger.debug("Arr�t: modifierContractant");
			return false;
		}
	}

//ROLE
//------------------------------------------------------------	
	
	/**
	 * Cr�ation d'un r�le
	 * @param String id
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean creerRole(String id) {

		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Cr�ation d'un r�le...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute("Systeme.creerRole", params);

				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return false;
		}
	}

	/**
	 * Lister les r�les disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerRoles() {
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Liste des r�les...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requ�te et affiche les r�sultats
				return (Vector) clientXML.execute("Systeme.listerRoles", params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return null;
		}
	}
	
	/**
	 * Informations d'un r�le
	 * @param String id
	 * @return Dictionary
	 */
	@SuppressWarnings("unchecked")
	public Dictionary infoRole(String id) {

		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Informations sur un r�le...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requ�te et affiche les r�sultats
				return (Dictionary) clientXML.execute("Systeme.infoRole", params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return null;
		}
	}

	/**
	 * Supprimer un r�le
	 * @param String id
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean supprimerRole(String id) {

		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Suppression d'un r�le...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute("Systeme.supprimerRole", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return false;
		}
	}
	
	/**
	 * Ajouter une permssion � un r�le
	 * @param String idPermission
	 * @param String idRole
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean ajouterPermissionRole(String idPermission, String idRole) {

		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Ajout d'une permission � un r�le...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idRole);
				params.addElement(idPermission);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute("Systeme.ajouterPermissionRole", params);

				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return false;
		}
	}

	/**
	 * Retirer une permission � un r�le
	 * @param String idPermission
	 * @param String idRole
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean retirerPermissionRole(String idPermission, String idRole) {

		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Retirer une permission � un r�le...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idRole);
				params.addElement(idPermission);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute("Systeme.retirerPermissionRole", params);

				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return false;
		}
	}
	
//PERMISSION
//------------------------------------------------------------	
		
	/**
	 * Cr�ation d'une permission
	 * @param String id
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean creerPermission(String id, String libelle) {

		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Cr�ation d'une permission...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(libelle);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute("Systeme.creerPermission", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
				// Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return false;
		}
	}

	/**
	 * Lister les permissions disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerPermissions() {
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Liste des permissions...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);

				// Adresse la requ�te et affiche les r�sultats
				return (Vector) clientXML.execute("Systeme.listerPermissions", params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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

		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Informations sur une permission...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requ�te et affiche les r�sultats
				return (Dictionary) clientXML.execute("Systeme.infoPermission", params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

		// Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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

		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Suppression d'une permission...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute("Systeme.supprimerPermission", params);
				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;

			}
		// Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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

		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Recherche des permissions...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(libelle);

				// Adresse la requ�te et affiche les r�sultats
				return (Vector) clientXML.execute("Systeme.rechercherPermissions", params);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}

			// Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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

		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Modification d'une permission...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(libelle);

				// Adresse la requ�te et affiche les r�sultats
				String result = (String) clientXML.execute("Systeme.modifierPermission", params);

				return Boolean.parseBoolean(result);

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}

			// Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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
	 * Signale au serveur que le client est bien connect�
	 * @return String
	 */
	public String testConnectXML() {
		System.out.println("Test de connexion envoy� par serveur NetJukeBox");
		return Boolean.toString(true);
	}

}