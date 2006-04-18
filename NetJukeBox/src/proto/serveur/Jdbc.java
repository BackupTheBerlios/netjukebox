package proto.serveur;

import java.sql.*;
import java.util.Dictionary;
//import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Interface la base de données avec JDBC
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
	
/*	
	//Permet l'envoi de requetes sql
	Statement state;
	
	//Permet la recuperation des donnees
	private ResultSet rs;
	
	//Création du vecteur encapsulant les différents dico
    static Vector vecteur = new Vector();
*/
    
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
    	try {
    		Statement st = connect.createStatement();
    		int row = st.executeUpdate(requete);
    		//connect.commit();
			st.close();
			return row; 
	    } catch (SQLException e) {
			System.err.println("ERREUR: Problème de requête \n" + e);
			return 0;
	    }
    }
    
    /**
     * Effectue une requete SELECT et retourne les résultats
     * @param String requete
     * @return Vector of Dictionary
     */
    public Vector executeQuery(String requete) {
    	try {
			Statement st = connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = st.executeQuery(requete);
			ResultSetMetaData rsmd = rs.getMetaData();
			Vector lignes = new Vector();

			int colomnCount = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= colomnCount; i++) {
					
					//Récupération du nom de la colonne
					Object cle = rsmd.getColumnName(i);
					
					//Récupération de la donnée
					Object valeur = rs.getObject(i);
					
					//Création d'un dictionnaire retournant les données
					Dictionary ligne = new Hashtable();
					ligne.put(cle, valeur);
					
					lignes.add(ligne);
				}
			}
			rs.close();
			st.close();
			return lignes;
	    } catch (SQLException e) {
			System.err.println("ERREUR: Problème de requête \n" + e);
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
    public boolean openDB(String driver, String url, String login, String pwd) {
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
		if (connect == null) {
			try {
				Class.forName(driver);
				connect = DriverManager.getConnection(url, login, pwd);
				connect.setAutoCommit(false);
				return true;
			}
			catch (ClassNotFoundException e) {
				System.err.println("ERREUR: Impossible de charger le pilote JDBC : \n" + e);
				return false;
			} catch (SQLException e) {
				System.err.println("ERREUR: Connexion à la base impossible : \n" + e);
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
		if (connect != null) {
			try {
				connect.close();
				return true;
			} catch (SQLException e) {
				System.err.println("ERREUR: Connexion à la base impossible : \n" + e);
				return false;
			}
		}
		System.err.println("ERREUR: Non connecté à la base");
		return false;
	}
    
/*    
	//Initialise les instances necessaires a la connexion a la base
	private void connectToDB(String requete) {
		initConnection("org.postgresql.Driver","jdbc:postgresql://192.168.0.2:5432/NetJukeBox", "postgres", "postgres");
		try {
			Statement state = connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = state.executeQuery(requete);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colomnCount = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= colomnCount; i++) {
					//Récupération du nom de la colonne
					Object colonne = rsmd.getColumnName(i);
					//Récupération de la donnée
					Object donnee = rs.getObject(i);
					//Création d'un dictionnaire retournant les données
					Dictionary dico = new Hashtable();
					dico.put(colonne, donnee);
					vecteur.add(dico);
				}
			}
			retour();
		} 
		catch (Exception e){
			System.out.println("connectToDB" + e);
		}
	}
	

	static Vector retour() {
		return vecteur;	
	}
	
	private void initConnection (String driver, String url, String login, String password) {
		try {
			Class.forName(driver);
			connect = DriverManager.getConnection(url, login, password);
			connect.setAutoCommit(false);
			//System.out.println("connexion reussie !!!");
		}
		catch (ClassNotFoundException e) {
			System.out.println("erreur du chargement du pilote JDBC : \n" + e);
			System.exit(0);
		} catch (SQLException e) {
			System.out.println("erreur de connexion a la base : \n" + e);
			System.exit(0);
		}
	}

	public void Commitmodif() {
		try {
			connect.commit();
			//System.out.println("Commit effectué !!!");
		}
		catch (Exception e){
			System.out.println("Commitmodif " + e);
		}
	}

	public Jdbc(String requete) throws SQLException {
		connectToDB(requete);
		Commitmodif();
		connect.close();
	}
*/
}