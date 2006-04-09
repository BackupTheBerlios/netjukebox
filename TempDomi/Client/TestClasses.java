package Client;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import Serveur.*;

public class TestClasses {
	public static void main (String[] args) throws AddressException, MessagingException {
		
		UTILISATEUR U1 = new UTILISATEUR();
		U1.Creer("Login1", "Nom1" ,"Prenom1" ,"dominique.gentaz@tiscali.fr", "MotDePasse", "Pays");
		new EnvoiMail("Login1", "Nom1" ,"Prenom1" ,"dominique.gentaz@tiscali.fr", "MotDePasse", "Pays");
		
		DOCUMENT D1 = new DOCUMENT();
		D1.Creer("D1", "TitreD1", 3, 01, 01, 2006, "SourceD1", "LangueD1", "GenreD1", "FichierD1");
		
		DOCUMENT D2 = new DOCUMENT();
		D2.Creer("D2", "TitreD2", 3, 01, 01, 2006, "SourceD2", "LangueD2", "GenreD2", "FichierD2");
		
		DOCUMENT D3 = new DOCUMENT();
		D3.Creer("D3", "TitreD3", 3, 01, 01, 2006, "SourceD3", "LangueD3", "GenreD3", "FichierD3");
		
		DOCUMENT D4 = new DOCUMENT();
		D4.Creer("D4", "TitreD4", 3, 01, 01, 2006, "SourceD4", "LangueD4", "GenreD4", "FichierD4");
		
		PROGRAMME P1 = new PROGRAMME();
		P1.Creer("P1", "TOTO", "Thema");
		P1.AjouterDocument(D1);
		P1.AjouterDocument(D2);
		P1.AjouterDocument(D3);
		P1.AjouterDocument(D4);
		
		P1.RetraitDocument(D2);
		
		PROGRAMME P2 = new PROGRAMME();
		P2.Creer("P2", "TITI", "Thema");
		DOCUMENT D5 = new DOCUMENT();
		D5.Creer("D5", "TitreD5", 3, 01, 01, 2006, "SourceD5", "LangueD5", "GenreD5", "FichierD5");
		DOCUMENT D6 = new DOCUMENT();
		D6.Creer("D6", "TitreD6", 3, 01, 01, 2006, "SourceD6", "LangueD6", "GenreD6", "FichierD6");
		P2.AjouterDocument(D5);
		P2.AjouterDocument(D6);
		
		P1.AjouterProgramme(P2);
		
		PROGRAMME P3 = new PROGRAMME();
		P3.Creer("P3", "TATA", "Thema");
		DOCUMENT D7 = new DOCUMENT();
		D7.Creer("D7", "TitreD7", 3, 01, 01, 2006, "SourceD7", "LangueD7", "GenreD7", "FichierD7");
		P3.AjouterDocument(D7);
				
		P1.AjouterProgramme(P3);
				
		P1.archiver();
		P1.archiver();
		
		CANAL C1 = new CANAL();
		C1.Creer("C1", "Rock", 20, "2224");
		C1.DiffuserProgramme(P1);
		
		
	}
}
