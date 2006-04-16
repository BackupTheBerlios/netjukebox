package proto.serveur;

import java.sql.SQLException;

public class TestJdbc {
	static String requete;
	public static void main (String args[]) throws SQLException {
		String nom = "toto";
		String prenom = "titi";
		String tel = "0000000000";	
		String requete = "INSERT INTO Annuaire VALUES ('" + nom + "', '" + prenom + "', '" +tel + "');"; 
		new Jdbc(requete);
		String requete1 = "Select nom from Annuaire";
		new Jdbc(requete1);

	
	}
}	
		