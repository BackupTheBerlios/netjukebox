package serveur;
import java.io.*;

public class EcrireFichier {
String destination;
String donnee;

	public EcrireFichier(String destination, String donnee) {
		this.destination = destination;
		this.donnee = donnee;
			try {
				BufferedWriter fichier = new BufferedWriter(new FileWriter(destination, true));
				fichier.write(donnee);
				fichier.newLine();
				fichier.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
