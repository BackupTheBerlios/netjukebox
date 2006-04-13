package proto.serveur;

import java.io.*;

public class EcrireFichier {
	String nom_fichier;
	String donnee;
	File fichier = null;

	public EcrireFichier(String nom_fichier, String donnee) {
		this.nom_fichier = nom_fichier;
		this.donnee = donnee;
		String chemin = "/home/admindg/workspace/NetJukeBox/";
			try {
				if (fichier == null){ //&& fichier.isFile()){
					File fichier = new File(chemin + nom_fichier);
					BufferedWriter ecrire = new BufferedWriter(new FileWriter(fichier));
					ecrire.write(donnee);
					ecrire.newLine();
					ecrire.close();
				} else {
					//fichier = new File(chemin + nom_fichier);
					BufferedWriter ecrire = new BufferedWriter(new FileWriter(nom_fichier));
					ecrire.write(donnee);
					ecrire.newLine();
					ecrire.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}