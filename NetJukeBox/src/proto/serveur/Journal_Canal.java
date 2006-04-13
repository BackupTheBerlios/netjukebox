package proto.serveur;

import java.io.*;
import java.text.*;
import java.util.Date;


/**
 * <p></p>
 * 
 */
public class Journal_Canal {

/**
 * <p>Represents ...</p>
 * 
 */
    public int id_jourcanal;

/**
 * <p>Represents ...</p>
 * 
 */
    public java.util.Date date_jourcanal;

    /**
     * <p>Does ...</p>
     * @throws IOException 
     * 
     */
    public void creer(String idCanal) throws IOException {        
    	Date date = new Date();
    	SimpleDateFormat formateur = new SimpleDateFormat("dd-MM-yyyy");
    	//System.out.println("date : " + date.toString());
    	//System.out.println("date formatée : " + formateur.format(date));
    	String nom_journal = (idCanal + "_" + formateur.format(date) + ".txt");
    	System.out.println("Le journal s'appelle : " + nom_journal);
    	new EcrireFichier(nom_journal, "toto");
    } 


    public static void main (String[] args) throws IOException {
    	Journal_Canal j = new Journal_Canal();
    	j.creer("C1");
    }
    
    
/**
 * <p>Does ...</p>
 * 
 */
    public void archiver(String idCanal, String date, String idProg, int Nbconnect) {        
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
			System.out.println("Le programme est archivé dans le fichier : "+ fichier_archive);
    } 

/**
 * <p>Does ...</p>
 * 
 */
    public void consulter() {        
        // your code here
    } 
 }

