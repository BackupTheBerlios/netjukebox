package proto.serveur;

import java.sql.SQLException;
//import java.util.Hashtable;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Vector;

public class TestJdbc {
	static String requete;
	public static void main (String args[]) throws SQLException {
		String nom = "pipi";
		String prenom = "pépé";
		String tel = "111111";	
		//String requete = "INSERT INTO Annuaire VALUES ('" + nom + "', '" + prenom + "', '" +tel + "');"; 
		Jdbc base = Jdbc.getInstance();
		base.openDB("org.postgresql.Driver", "jdbc:postgresql://192.168.0.2:5432/NetJukeBox", "postgres", "postgres");
		//base = Jdbc.getInstance();
		//base.executeUpdate(requete);
		String requete1 = "SELECT nom FROM Annuaire WHERE nom = 'papa';";
		Vector resultats = base.executeQuery(requete1);
		
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			System.out.println(dico.get("nom"));
			//DICTIONARY : Affichage des données du dictionnaire
			//Enumeration donnee = dico.elements();
			//Enumeration colonne = dico.keys();
			//for(int i = 0; i < dico.size(); i++){
				//System.out.print(colonne.nextElement() + " : "); 
				//System.out.println(donnee.nextElement());
			//}
		}
	}
}	
		