package proto.serveur;

import java.sql.*;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Interface la base de donn�es avec JDBC
 */
public class Jdbc {
	
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
    	try {
    		//Requ�te
    		Statement st = connect.createStatement();
    		int row = st.executeUpdate(requete);
    		
    		//On applique la modif
    		connect.commit();
    		
    		//On ferme
			st.close();
			
			//On retourne le nombre de lignes affect�es (0 = �chec)
			return row; 
	    } catch (SQLException e) {
			System.err.println("ERREUR: Probl�me de requ�te \n" + e);
			return 0;
	    }
    }
    
    /**
     * Effectue une requete SELECT et retourne les r�sultats
     * @param String requete
     * @return Vector of Dictionary
     */
    public Vector executeQuery(String requete) {
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
			return lignes;
			
	    } catch (SQLException e) {
			System.err.println("ERREUR: Probl�me de requ�te \n" + e);
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
    public boolean openDB(String driver, String url, String login, String pwd) {
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
		if (connect == null) {
			try {
				Class.forName(driver);
				connect = DriverManager.getConnection(url, login, pwd);
				connect.setAutoCommit(false);
				return true;
			}
			catch (ClassNotFoundException e) {
				System.err.println("ERREUR: Impossible de charger le pilote JDBC : " + e);
				return false;
			} catch (SQLException e) {
				System.err.println("ERREUR: Connexion � la base impossible : " + e);
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
		if (connect != null) {
			try {
				connect.close();
				return true;
			} catch (SQLException e) {
				System.err.println("ERREUR: Connexion � la base impossible : \n" + e);
				return false;
			}
		}
		System.err.println("ERREUR: Non connect� � la base");
		return false;
	}
}