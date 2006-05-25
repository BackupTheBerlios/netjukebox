package proto.serveur;

import java.io.*;
import java.text.*;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Journal d'un canal
 */
public class JournalCanal {
	/**
	 * Cr�e le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(JournalCanal.class);

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
	 * Cr�ation
	 */
	public void creer(String idCanal) throws IOException {
		logger.debug("D�marrage: creer");
		Date date = new Date();
		SimpleDateFormat formateur = new SimpleDateFormat("dd-MM-yyyy");
		// logger.info("date : " + date.toString());
		// logger.info("date format�e : " + formateur.format(date));
		String nom_journal = (idCanal + "_" + formateur.format(date) + ".txt");
		logger.info("Le journal s'appelle : " + nom_journal);
		new EcrireFichier(nom_journal, "toto");
		logger.debug("Arr�t: creer");
	}

	/**
	 * Archivage
	 */
	public void archiver(String idCanal, String date, String idProg,
			int Nbconnect) {
		logger.debug("D�marrage: archiver");
		int i;
		String donnee, fichier_archive;
		// Cr�ation du nom complet du fichier Archive de tous les documents
		// composant le programme

		// Sauvegarde de la liste des documents du programme dans le fichier
		// Archive
		for (i = 0; i < this.documents.size(); i++) {
			donnee = (String) this.documents.elementAt(i);
			new EcrireFichier(fichier_archive, donnee);
		}
		logger.info("Le programme est archiv� dans le fichier : "
				+ fichier_archive);
		logger.debug("Arr�t: archiver");
	}

	/**
	 * Consultation
	 */
	public void consulter() {
		logger.debug("D�marrage: consulter");
		// your code here
		logger.debug("Arr�t: consulter");
	}

	/*
	 * public static void main (String[] args) throws IOException {
	 * JournalCanal j = new JournalCanal(); j.creer("C1"); }
	 */
	
}
