import java.sql.*;

public class Jdbc {
//Connexion a la base
private Connection connect;
//Permet l'envoi de requetes sql
Statement state;
//Permet la recuperation des donnees


//Initialise les instances necessaires a la connexion a la base
private void connectToDB() {
initConnection("org.postgresql.Driver","jdbc:postgresql://localhost:5432/NetJukeBox", "postgres", "postgres");
try {
Statement state = connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
} catch (ClassNotFoundException e) {
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
} catch (Exception e){
System.out.println("Commitmodif " + e);
}
}

public Jdbc() {
connectToDB();
Commitmodif();
//Fermeture de la base nécessaire ???
}

public static void main (String args[]) {
new Jdbc();
}
}