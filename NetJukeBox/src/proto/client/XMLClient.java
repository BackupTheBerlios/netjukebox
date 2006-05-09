package proto.client;

import java.net.MalformedURLException;
import java.util.Dictionary;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpc;
import org.apache.xmlrpc.XmlRpcClient;

/**
 * Client XML (envoie les requ�tes au serveur XML du serveur principal)
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
	 * Etat connect�
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
		
		//Si le client XML n'est pas d�j� initialis�
		if (clientXML == null) {
			System.err.println("INFO: Initialisation XMLRPC en cours...");
			try {
				// Utilise lengthpilote Sax d'Apache Xerces
				XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");
				
				// Creation du client & identification du serveur
				clientXML = new XmlRpcClient("http://"+ip+":"+port);

				System.err.println("INFO: Client Streaming d�marr�");
				
			} catch (ClassNotFoundException e) {
				System.err.println("ERREUR: Impossible de localiser le pilote Sax");
			} catch (MalformedURLException e) {
				System.err.println("ERREUR: URL non conforme au format du serveur XML-RPC");

			} catch (Exception e) {
				System.out.println("ERREUR: " + e);
			}
			
		//Sinon, d�j� initialis�
		} else {
			System.err.println("WARNING: Client XML d�j� initialis� !");
		}
	}

	
// METHODES DYNAMIQUES
//**************************************
	
	/**
	 * Tentative de connexion au seveur XML
	 * @param String ip
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean testConnectXML(String ip) {
		
			System.err.println("INFO: Tentative de connexion au serveur XML...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(ip);
				
				// Adresse la requ�te et affiche les r�sultats
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
	@SuppressWarnings("unchecked")
	public boolean connexion(String login, String pwd) {
		
		//Si l'utilisateur n'est pas connect� au seveur
		if (!etatConnecte) {
			System.err.println("INFO: Connexion en cours...");
			this.login = login;
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(pwd);
				
				// Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.connexion", params);
				
				etatConnecte = Boolean.parseBoolean(result);
				return etatConnecte;
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, d�j� connect�
		} else {
			System.err.println("WARNING: Client d�j� connect� au serveur !");
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
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: D�connexion en cours...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				
				// Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.deconnexion", params);
				boolean estDeconnecte = Boolean.parseBoolean(result);
				
				etatConnecte = !estDeconnecte;
				return estDeconnecte;
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, n'est pas connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return false;
		}
	}
	
	/**
	 * Cr�ation d'un canal
	 * @param String nom
	 * @param String utilMax
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean creerCanal(String nom, String utilMax) {
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Cr�ation d'un canal...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(nom);
				params.addElement(utilMax);
				
				// Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.creerCanal", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return false;
		}
	}
	
	/**
	 * Cr�ation d'un programme
	 * @param String titre
	 * @param String thematique
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean creerProgramme(String titre, String thematique) {
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Cr�ation d'un programme...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(titre);
				params.addElement(thematique);
				
				// Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.creerProgramme", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return false;
		}
	}
	
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
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean creerDocument(String titre, String duree, String jour, String mois, String annee, String source, String langue, String genre, String fichier) {
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Cr�ation d'un document...");
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
				
				// Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.creerDocument", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Ajout du document au programme...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idDoc);
				params.addElement(idProg);
				
				// Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.ajouterDocumentProgramme", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Retirer un document du programme...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idProg);
				params.addElement(calage);
				
				// Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.retirerDocumentProgramme", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Diffusion du programme...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idProg);
				params.addElement(idCanal);
				
				// Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.diffuserProgramme", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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
	public boolean planifierProgramme(String idProg, String idCanal, String jour, String mois, String annee, String heure, String minute, String seconde) {
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Planification du programme...");
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
				String result = (String)clientXML.execute("Systeme.planifierProgramme", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: D�planification du programme...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idCanal);
				params.addElement(calage);
				
				// Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.deplanifierProgramme", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Ecoute d'un canal...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idCanal);
				
				// Adresse la requ�te et affiche les r�sultats
				return (String)clientXML.execute("Systeme.ecouterCanal", params);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Diffusion d'un canal...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idCanal);
				
				// Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.startCanal", params);
				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Arr�t d'un canal...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(idCanal);
				
				// Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.stopCanal", params);
				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return false;
		}
	}
	
	/**
	 * Lister les documents disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerDocuments() {
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Liste des documents...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				
				// Adresse la requ�te et affiche les r�sultats
				return (Vector)clientXML.execute("Systeme.listerDocuments", params);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return null;
		}
	}
	
	/**
	 * Lister les programmes disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerProgrammes() {
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Liste des programmes...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				
				// Adresse la requ�te et affiche les r�sultats
				return (Vector)clientXML.execute("Systeme.listerProgrammes", params);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return null;
		}
	}
	
	/**
	 * Lister les canaux disponibles sur le serveur
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector listerCanaux() {
		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Liste des canaux...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				
				// Adresse la requ�te et affiche les r�sultats
				return (Vector)clientXML.execute("Systeme.listerCanaux", params);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return null;
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
	public boolean inscription(String log, String pass, String role, String email, String nom, String prenom, String pays) {
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Inscription d'un utilisateur...");
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
				String result = (String)clientXML.execute("Systeme.inscription", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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
			System.err.println("INFO: Suppression du compte ...");
			try {
				//Cr�ation de la requ�te
				Vector params = new Vector();	
				params.addElement(login);
				
				//Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.supprimerUtilisateur", params);
				return Boolean.parseBoolean(result);	

			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		}
		else {
			System.err.println("WARNING: Client non connect� au serveur !");
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
		 // Si l'utilisateur est connect� au seveur
			if (etatConnecte) {
				System.err.println("INFO: Recherche des attributs...");
				try {
					// Cr�ation de la requ�te
					Vector params = new Vector();
					params.addElement(login);
					
					// Adresse la requ�te et affiche les r�sultats
					return (Vector)clientXML.execute("Systeme.rechercherUtilisateur", params);
				} catch (Exception e) {
					System.err.println("ERREUR : " + e);
					return null;
				}
			//Sinon, non connect�
			} else {
				System.err.println("WARNING: Client non connect� au serveur !");
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
	public boolean modifierUtilisateur(String newlogin, String pwd, String Nom, String Prenom, String Email, String Pays) {
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Modification des attributs...");
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
					String result = (String)clientXML.execute("Systeme.modifierUtilisateur", params);

					//return Boolean.parseBoolean(result);
					return true;
					
				} catch (Exception e) {
					System.err.println("ERREUR : " + e);
					return false;
				}
			
			//Sinon, non connect�
			} else {
				System.err.println("WARNING: Client non connect� au serveur !");
				return false;
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
	 * @return Vector
	 */
	@SuppressWarnings("unchecked")
	public Vector rechercherDocument(String id, String titre, String duree, String jour, String mois, String annee, String source, String langue, String genre, String fichier) {

		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Recherche des documents...");
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
				
				// Adresse la requ�te et affiche les r�sultats
				return (Vector)clientXML.execute("Systeme.rechercherDocument", params);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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

		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Recherche des programmes...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(titre);
				params.addElement(thematique);
				
				// Adresse la requ�te et affiche les r�sultats
				return (Vector)clientXML.execute("Systeme.rechercherProgramme", params);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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

		// Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Recherche des canaux...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(nom);
				params.addElement(utilMax);
				
				// Adresse la requ�te et affiche les r�sultats
				return (Vector)clientXML.execute("Systeme.rechercherCanal", params);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Suppression d'un document...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				
				// Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.supprimerDocument", params);
				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return false;
		}
	}
	
	/**
	 * Supprimer un programme
	 * @param String id
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean supprimerProgramme(String id) {
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Suppression d'un programme...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				
				// Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.supprimerProgramme", params);
				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return false;
		}
	}
	
	/**
	 * Supprimer un canal
	 * @param String id
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean supprimerCanal(String id) {
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Suppression d'un canal...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				
				// Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.supprimerCanal", params);
				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean modifierDocument(String id, String titre, String duree, String jour, String mois, String annee, String source, String langue, String genre, String fichier) {
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Modification d'un document...");
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
				
				// Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.modifierDocument", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Modification d'un programme...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(titre);
				params.addElement(thematique);
				
				// Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.modifierProgramme", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Modification d'un canal...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				params.addElement(nom);
				params.addElement(utilMax);
				
				// Adresse la requ�te et affiche les r�sultats
				String result = (String)clientXML.execute("Systeme.modifierCanal", params);

				return Boolean.parseBoolean(result);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return false;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
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
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Informations sur un document...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				
				// Adresse la requ�te et affiche les r�sultats
				return (Dictionary)clientXML.execute("Systeme.infoDocument", params);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return null;
		}
	}
	
	/**
	 * Informations d'un programme
	 * @param String id
	 * @return Dictionary
	 */
	@SuppressWarnings("unchecked")
	public Dictionary infoProgramme(String id) {
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Informations sur un programme...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				
				// Adresse la requ�te et affiche les r�sultats
				return (Dictionary)clientXML.execute("Systeme.infoProgramme", params);
				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return null;
		}
	}
	
	/**
	 * Informations d'un canal
	 * @param String id
	 * @return Dictionary
	 */
	@SuppressWarnings("unchecked")
	public Dictionary infoCanal(String id) {
		
		//Si l'utilisateur est connect� au seveur
		if (etatConnecte) {
			System.err.println("INFO: Informations sur un canal...");
			try {
				// Cr�ation de la requ�te
				Vector params = new Vector();
				params.addElement(login);
				params.addElement(id);
				
				// Adresse la requ�te et affiche les r�sultats
				return (Dictionary)clientXML.execute("Systeme.infoCanal", params);

				
			} catch (Exception e) {
				System.err.println("ERREUR : " + e);
				return null;
			}
		
		//Sinon, non connect�
		} else {
			System.err.println("WARNING: Client non connect� au serveur !");
			return null;
		}
	}
	
    /**
     * Recherche du mot de passe de l'utilisateur
     * @param String login
     * @return boolean
     */
    @SuppressWarnings("unchecked")
	public boolean recherchepwd(String login) {
       
        //Si l'utilisateur est connect� au seveur
        if (etatConnecte) {
            System.err.println("INFO: Recherche du mot de passe...");
            try {
                // Cr�ation de la requ�te
                Vector params = new Vector();
                params.addElement(login);
               
                // Adresse la requ�te et affiche les r�sultats
                String result = (String)clientXML.execute("Systeme.rechercherpwd", params);

                return Boolean.parseBoolean(result);
               
            } catch (Exception e) {
                System.err.println("ERREUR : " + e);
                return false;
            }
       
        //Sinon, non connect�
        } else {
            System.err.println("WARNING: Client non connect� au serveur !");
            return false;
        }
    }
}

