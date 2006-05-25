package proto.serveur;

import java.sql.*;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * Interface la base de donn�es avec JDBC
 */
public class Jdbc {
	
	/**
	 * Cr�e le logger de la classe
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
	 * Login d'acc�s � la base
	 */
	private String login;
	
	/**
	 * Mot de passe d'acc�s � la base
	 */
	private String pwd;
	
	/**
	 * Connection courante � la base
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
     * Effectue une requ�te UPDATE, INSERT ou DELETE
     * @param String requete
     * @return int
     */
    public int executeUpdate(String requete) {
    	logger.debug("D�marrage: executeUpdate");	
    	try {
    		//Requ�te
    		Statement st = connect.createStatement();
    		int row = st.executeUpdate(requete);
    		
    		//On applique la modif
    		connect.commit();
    		
    		//On ferme
			st.close();
			
			//On retourne le nombre de lignes affect�es (0 = �chec)
			logger.debug("Arr�t: executeUpdate");
			return row; 
	    } catch (SQLException e) {
			logger.error("ERREUR: Probl�me de requ�te \n" + e);
			logger.debug("Arr�t: executeUpdate");
			return 0;
	    }
    }
    
    /**
     * Effectue une requete SELECT et retourne les r�sultats
     * @param String requete
     * @return Vector of Dictionary
     */
    public Vector executeQuery(String requete) {
    	logger.debug("D�marrage: executeQuery");	
    	try {
    		
    		//On ex�cute la requ�te et on en sort des r�sultats
			Statement st = connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = st.executeQuery(requete);
			ResultSetMetaData rsmd = rs.getMetaData();
			
			//Vecteur de lignes de r�sultats
			Vector lignes = new Vector();
			
			//Dictionnaire (= 1 ligne de r�sultat)
			Dictionary ligne;

			//Nb de colonnes
			int colomnCount = rsmd.getColumnCount();
			
			//Tant qu'il y a une ligne de r�sultat � traiter
			while (rs.next()) {
				
				//On initialise le Dictionnary pour cette ligne
				ligne = new Hashtable();
				
				//Pour chaque champ
				for (int i = 1; i <= colomnCount; i++) {
					
					//R�cup�ration du nom de la colonne
					Object cle = rsmd.getColumnName(i);
					
					//R�cup�ration de la donn�e
					Object valeur = rs.getObject(i);
					
					//On ajoute le champ � la ligne
					ligne.put(cle, valeur);
				}
				
				//On ajoute la ligne au Vecteur de lignes final
				lignes.add(ligne);
			}
			
			//Fermeture JDBC
			rs.close();
			st.close();
			
			//Retour du Vector de Dictionary
	    	logger.debug("Arr�t: executeQuery");
			return lignes;
			
	    } catch (SQLException e) {
			logger.error("ERREUR: Probl�me de requ�te \n" + e);
	    	logger.debug("Arr�t: executeQuery");
			return null;
		}
    }
    
    /**
     * Connecte � la base de donn�es
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
     * Se connecte � la base de donn�es
     * @return
     */
    public boolean openDB() {
    	logger.debug("D�marrage: openDB");	
		if (connect == null) {
			try {
				Class.forName(driver);
				connect = DriverManager.getConnection(url, login, pwd);
				connect.setAutoCommit(false);
				logger.debug("Arr�t: openDB");
				return true;
			}
			catch (ClassNotFoundException e) {
				logger.error("ERREUR: Impossible de charger le pilote JDBC : " + e);
				logger.debug("Arr�t: openDB");
				return false;
			} catch (SQLException e) {
				logger.error("ERREUR: Connexion � la base impossible : " + e);
				logger.debug("Arr�t: openDB");	
				return false;
			}
			
		}
		System.err.println("ERREUR: D�j� connect� � la base");
		return false;
	}
	
    /**
     * Ferme la connexion � la base de donn�es
     * @return boolean
     */
	public boolean closeDB() {
		logger.debug("D�marrage : closeDB");	
		if (connect != null) {
			try {
				connect.close();
				logger.debug("Arr�t : closeDB");
				return true;
			} catch (SQLException e) {
				logger.error("ERREUR: Connexion � la base impossible : \n" + e);
				logger.debug("Arr�t : closeDB");
				return false;
			}
		}
		logger.error("ERREUR: Non connect� � la base");
		logger.debug("Arr�t : closeDB");
		return false;
	}
}