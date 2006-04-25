package proto.serveur;

import java.sql.SQLException;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;
import proto.serveur.*;

public class TestClasses {
	
	public static Ldap ldap;
	
	public static void main (String[] args) throws AddressException, MessagingException, SQLException, NamingException {
		ldap = Ldap.getInstance();
		ldap.openLdap("com.sun.jndi.ldap.LdapCtxFactory", "ldap://localhost:389/dc=netjukebox,dc=com", "simple", "admin", "mot2passe", "admin", "dc=netjukebox,dc=com");
		Utilisateur U1 = Utilisateur.create("Login2", "MotDePasse2", "Nom2", "Prenom2", "dominique.gentaz@tiscali.fr", "Pays", "usager");
		//new EnvoiMail("Login1", "Nom1" ,"Prenom1" ,"dominique.gentaz@tiscali.fr", "MotDePasse", "Pays");
		
		
		/**
		Document D1 = Document.create("TitreD1", 3, "01", "01", "2006", "SourceD1", "LangueD1", "GenreD1", "FichierD1");

		Document D2 = Document.create("TitreD2", 3, "01", "01", "2006", "SourceD2", "LangueD2", "GenreD2", "FichierD2");

		Document D3 = Document.create("TitreD3", 3, "01", "01", "2006", "SourceD3", "LangueD3", "GenreD3", "FichierD3");

		Document D4 = Document.create("TitreD4", 3, "01", "01", "2006", "SourceD4", "LangueD4", "GenreD4", "FichierD4");

		Programme P1 = Programme.create("TOTO", "Thema");
		P1.ajouterDocument(D1);
		P1.ajouterDocument(D2);
		P1.ajouterDocument(D3);
		P1.ajouterDocument(D4);

		P1.retirerDocument(D2);

		Programme P2 = Programme.create("TITI", "Thema");
		Document D5 = Document.create("TitreD5", 3, "01", "01", "2006", "SourceD5", "LangueD5", "GenreD5", "FichierD5");
		Document D6 = Document.create("TitreD6", 3, "01", "01", "2006", "SourceD6", "LangueD6", "GenreD6", "FichierD6");
		P2.ajouterDocument(D5);
		P2.ajouterDocument(D6);

		P1.ajouterProgramme(P2);

		Programme P3 = Programme.create("TATA", "Thema");
		Document D7 = Document.create("TitreD7", 3, "01", "07", "2010", "SourceD7", "LangueD7", "GenreD7", "FichierD7");
		P3.ajouterDocument(D7);

		P1.ajouterProgramme(P3);

		P1.archiver();
		P1.archiver();

		D1.compterVerrouProgramme();
		D1.getProgrammesArchives();
		D2.compterVerrouProgramme();
		D2.getProgrammesArchives();
	
		D1.getDateCreation();
		D7.getDateCreation();
		
		Canal C1 = Canal.create("Rock", 20);
		//C1.createRTPServer("192.168.1.255", 2224);
		C1.diffuserProgramme(P1);
		*/
	}
}