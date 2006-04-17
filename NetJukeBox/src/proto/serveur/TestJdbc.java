package proto.serveur;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Dictionary;
import java.util.Enumeration;

public class TestJdbc {
	static String requete;
	public static void main (String args[]) throws SQLException {
		String nom = "papa";
		String prenom = "pupu";
		String tel = "0000000000";	
		String requete = "INSERT INTO Annuaire VALUES ('" + nom + "', '" + prenom + "', '" +tel + "');"; 
		//new Jdbc(requete);
		String requete1 = "SELECT * FROM Annuaire;";// WHERE nom = 'toto';";
		new Jdbc(requete1);
		Dictionary dico = Jdbc.retour();

		//DICTIONARY : Affichage des données du dictionnaire
		Enumeration donnee = dico.elements();
		Enumeration colonne = dico.keys();
		for(int i = 0; i < dico.size(); i++){
			//Enumeration donnee = dico.elements();
			//Enumeration colonne = dico.keys();
			System.out.print(colonne.nextElement() + " : "); 
			System.out.println(donnee.nextElement());
		}
	}
}	
		