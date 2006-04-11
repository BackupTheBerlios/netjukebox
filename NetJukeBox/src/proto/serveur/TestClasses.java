package proto.serveur;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import proto.serveur.*;

public class TestClasses {
	public static void main (String[] args) throws AddressException, MessagingException {
		
		Utilisateur U1 = new Utilisateur();
		U1.Creer("Login1", "Nom1" ,"Prenom1" ,"dominique.gentaz@tiscali.fr", "MotDePasse", "Pays");
		//new EnvoiMail("Login1", "Nom1" ,"Prenom1" ,"dominique.gentaz@tiscali.fr", "MotDePasse", "Pays");
		
		Document D1 = new Document();
		D1.Creer("D1", "TitreD1", 3, 01, 01, 2006, "SourceD1", "LangueD1", "GenreD1", "FichierD1");
		
		Document D2 = new Document();
		D2.Creer("D2", "TitreD2", 3, 01, 01, 2006, "SourceD2", "LangueD2", "GenreD2", "FichierD2");
		
		Document D3 = new Document();
		D3.Creer("D3", "TitreD3", 3, 01, 01, 2006, "SourceD3", "LangueD3", "GenreD3", "FichierD3");
		
		Document D4 = new Document();
		D4.Creer("D4", "TitreD4", 3, 01, 01, 2006, "SourceD4", "LangueD4", "GenreD4", "FichierD4");
		
		Programme P1 = new Programme();
		P1.Creer("P1", "TOTO", "Thema");
		P1.ajouterDocument(D1);
		P1.ajouterDocument(D2);
		P1.ajouterDocument(D3);
		P1.ajouterDocument(D4);
		
		P1.retraitDocument(D2);
		
		Programme P2 = new Programme();
		P2.Creer("P2", "TITI", "Thema");
		Document D5 = new Document();
		D5.Creer("D5", "TitreD5", 3, 01, 01, 2006, "SourceD5", "LangueD5", "GenreD5", "FichierD5");
		Document D6 = new Document();
		D6.Creer("D6", "TitreD6", 3, 01, 01, 2006, "SourceD6", "LangueD6", "GenreD6", "FichierD6");
		P2.ajouterDocument(D5);
		P2.ajouterDocument(D6);
		
		P1.AjouterProgramme(P2);
		
		Programme P3 = new Programme();
		P3.Creer("P3", "TATA", "Thema");
		Document D7 = new Document();
		D7.Creer("D7", "TitreD7", 3, 01, 01, 2006, "SourceD7", "LangueD7", "GenreD7", "FichierD7");
		P3.ajouterDocument(D7);
				
		P1.AjouterProgramme(P3);
				
		P1.archiver();
		P1.archiver();
		
		Canal C1 = new Canal();
		C1.creer("C1", "Rock", 20, "2224");
		C1.diffuserProgramme(P1);		
	}
}