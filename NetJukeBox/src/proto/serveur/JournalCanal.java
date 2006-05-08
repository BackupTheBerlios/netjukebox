package proto.serveur;

import java.io.*;
import java.text.*;
import java.util.Date;

/**
 * Journal d'un canal
 */
public class JournalCanal {

// ATTRIBUTS
//******************************************
	
	/**
	 * Identifiant
	 */
	private int id;

	/**
	 * Date
	 */
	private Date date;

// CONSTRUCTEUR
//********************************************
	
	public JournalCanal() {
		
	}
	
// METHODES STATIQUES
//********************************************
	

// METHODES DYNAMIQUES
//********************************************
	
	/**
	 * Création
	 */
	public void creer(String idCanal) throws IOException {
		Date date = new Date();
		SimpleDateFormat formateur = new SimpleDateFormat("dd-MM-yyyy");
		// System.out.println("date : " + date.toString());
		// System.out.println("date formatée : " + formateur.format(date));
		String nom_journal = (idCanal + "_" + formateur.format(date) + ".txt");
		System.out.println("Le journal s'appelle : " + nom_journal);
		new EcrireFichier(nom_journal, "toto");
	}

	/**
	 * Archivage
	 */
	public void archiver(String idCanal, String date, String idProg,
			int Nbconnect) {
		int i;
		String donnee, fichier_archive;
		// Création du nom complet du fichier Archive de tous les documents
		// composant le programme

		// Sauvegarde de la liste des documents du programme dans le fichier
		// Archive
		for (i = 0; i < this.documents.size(); i++) {
			donnee = (String) this.documents.elementAt(i);
			new EcrireFichier(fichier_archive, donnee);
		}
		System.out.println("Le programme est archivé dans le fichier : "
				+ fichier_archive);
	}

	/**
	 * Consultation
	 */
	public void consulter() {
		// your code here
	}

	/*
	 * public static void main (String[] args) throws IOException {
	 * JournalCanal j = new JournalCanal(); j.creer("C1"); }
	 */
}
