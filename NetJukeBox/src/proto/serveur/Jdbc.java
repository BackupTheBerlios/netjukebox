package proto.serveur;

import java.sql.*;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * Interface la base de données avec JDBC
 */
public class Jdbc {
	
	/**
	 * Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(Jdbc.class);
	
// ATTRIBUTS
//*******************************************
	
	/**
	 * Singleton
	 */
	private static Jdbc instance;
	
	/**
	 * Nom du driver
	 */
	private String driver;
	
	/**
	 * URL de la base
	 */
	private String url;
	
	/**
	 * Login d'accès à la base
	 */
	private String login;
	
	/**
	 * Mot de passe d'accès à la base
	 */
	private String pwd;
	
	/**
	 * Connection courante à la base
	 */
	private Connection connect = null;
	
// CONSTRUCTEUR
//********************************************
    
	/**
	 * Constructeur vide (Singleton)
	 */
	public Jdbc() {
	}
    
// METHODES STATIQUES
//********************************************
    
    /**
     *	Retourne l'instance du Singleton
     */
	public static synchronized Jdbc getInstance(){
		if (instance == null)
			instance = new Jdbc();
		return instance;
	}

         
    
// METHODES DYNAMIQUES
//********************************************
    
    /**
     * Effectue une requête UPDATE, INSERT ou DELETE
     * @param String requete
     * @return int
     */
    public int executeUpdate(String requete) {
    	logger.debug("Démarrage: executeUpdate");	
    	try {
    		//Requête
    		Statement st = connect.createStatement();
    		int row = st.executeUpdate(requete);
    		
    		//On applique la modif
    		connect.commit();
    		
    		//On ferme
			st.close();
			
			//On retourne le nombre de lignes affectées (0 = échec)
			logger.debug("Arrêt: executeUpdate");
			return row; 
	    } catch (SQLException e) {
			logger.error("ERREUR: Problème de requête \n" + e);
			logger.debug("Arrêt: executeUpdate");
			return 0;
	    }
    }
    
    /**
     * Effectue une requete SELECT et retourne les résultats
     * @param String requete
     * @return Vector of Dictionary
     */
    public Vector executeQuery(String requete) {
    	logger.debug("Démarrage: executeQuery");	
    	try {
    		
    		//On exécute la requête et on en sort des résultats
			Statement st = connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = st.executeQuery(requete);
			ResultSetMetaData rsmd = rs.getMetaData();
			
			//Vecteur de lignes de résultats
			Vector lignes = new Vector();
			
			//Dictionnaire (= 1 ligne de résultat)
			Dictionary ligne;

			//Nb de colonnes
			int colomnCount = rsmd.getColumnCount();
			
			//Tant qu'il y a une ligne de résultat à traiter
			while (rs.next()) {
				
				//On initialise le Dictionnary pour cette ligne
				ligne = new Hashtable();
				
				//Pour chaque champ
				for (int i = 1; i <= colomnCount; i++) {
					
					//Récupération du nom de la colonne
					Object cle = rsmd.getColumnName(i);
					
					//Récupération de la donnée
					Object valeur = rs.getObject(i);
					
					//On ajoute le champ à la ligne
					ligne.put(cle, valeur);
				}
				
				//On ajoute la ligne au Vecteur de lignes final
				lignes.add(ligne);
			}
			
			//Fermeture JDBC
			rs.close();
			st.close();
			
			//Retour du Vector de Dictionary
	    	logger.debug("Arrêt: executeQuery");
			return lignes;
			
	    } catch (SQLException e) {
			logger.error("ERREUR: Problème de requête \n" + e);
	    	logger.debug("Arrêt: executeQuery");
			return null;
		}
    }
    
    /**
     * Connecte à la base de données
     * @param String driver
     * @param String url
     * @param String login
     * @param String password
     */
    public /*pure*/ boolean openDB(String driver, String url, String login, String pwd) {
    	this.driver = driver;
    	this.url = url;
    	this.login = login;
    	this.pwd = pwd;
    	
    	return this.openDB();
    }
    
    /**
     * Se connecte à la base de données
     * @return
     */
    public boolean openDB() {
    	logger.debug("Démarrage: openDB");	
		if (connect == null) {
			try {
				Class.forName(driver);
				connect = DriverManager.getConnection(url, login, pwd);
				connect.setAutoCommit(false);
				logger.debug("Arrêt: openDB");
				return true;
			}
			catch (ClassNotFoundException e) {
				logger.error("ERREUR: Impossible de charger le pilote JDBC : " + e);
				logger.debug("Arrêt: openDB");
				return false;
			} catch (SQLException e) {
				logger.error("ERREUR: Connexion à la base impossible : " + e);
				logger.debug("Arrêt: openDB");	
				return false;
			}
			
		}
		System.err.println("ERREUR: Déjà connecté à la base");
		return false;
	}
	
    /**
     * Ferme la connexion à la base de données
     * @return boolean
     */
	public boolean closeDB() {
		logger.debug("Démarrage : closeDB");	
		if (connect != null) {
			try {
				connect.close();
				logger.debug("Arrêt : closeDB");
				return true;
			} catch (SQLException e) {
				logger.error("ERREUR: Connexion à la base impossible : \n" + e);
				logger.debug("Arrêt : closeDB");
				return false;
			}
		}
		logger.error("ERREUR: Non connecté à la base");
		logger.debug("Arrêt : closeDB");
		return false;
	}
}