package proto.serveur;

import java.sql.*;

public class Jdbc {
	//Connexion a la base
	private Connection connect;
	//Permet l'envoi de requetes sql
	Statement state;
	//Permet la recuperation des donnees
	private ResultSet rs;
	
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
					System.out.print(rs.getObject(i) + "\t");
				}
			}
		} 
		catch (Exception e){
			System.out.println("connectToDB" + e);
		}
	}

	private void initConnection (String driver, String url, String login, String password) {
		try {
			Class.forName(driver);
			connect = DriverManager.getConnection(url, login, password);
			connect.setAutoCommit(false);
			System.out.println("connexion reussie !!!");
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
			System.out.println("Commit effectué !!!");
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
}