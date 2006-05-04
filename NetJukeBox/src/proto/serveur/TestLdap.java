package proto.serveur;

import java.util.Dictionary;
import java.util.Enumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

public class TestLdap {
		
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws NamingException {
		Ldap ldap = Ldap.getInstance();
		ldap.openLdap("com.sun.jndi.ldap.LdapCtxFactory", "ldap://localhost:389/dc=netjukebox,dc=com", 
				"simple", "admin" , "mot2passe", "admin", "dc=netjukebox,dc=com");
		
		//ldap.executeSupprimer("login2");
		
		//ldap.executeCreer("Login7", "MotDePasse", "Nom1", "Prenom1", "netjukebox@gmail.com", "Pays", "admin");
		ldap.executeCreer("login", "password", "toto", "titi", "toto@gmail.com", "france", "usager");
		
		ldap.executeCreer("log", "pass", "tata", "tutu", "tata@gmail.com", "france", "respprog");
		
		//ldap.changerRole("login", "usager", "admin");
		
		//ldap.ModifieAttributs("mail", "new@gmail", "login", "admin");
		
		/**
		Dictionary resultats = ldap.getAttributs("login", "usager");
		Enumeration donnee = resultats.elements();
		Enumeration colonne = resultats.keys();
		for(int i = 0; i < resultats.size(); i++){
			System.out.print(colonne.nextElement() + " : "); 
			System.out.println(donnee.nextElement());
		}
		*/
		
		Dictionary resultats = ldap.getLogin("admin");
		try {
		Enumeration donnee = resultats.elements();
		if (resultats != null && resultats.size()>0) {
		Attributes result = (Attributes) donnee.nextElement();
		Attribute log = result.get("uid");
		Attribute pwd = result.get("userPassword");
		Attribute at = result.get("userPassword");
		byte[] encPassword = (byte[])at.get(0);
		String encStrPassword = new String(encPassword);
		System.out.println(encStrPassword);
		Attribute role = result.get("ou");
		System.out.println(pwd.get());
		System.out.println(log.get());
		System.out.println(role.get());
		} else {
		System.out.println("Erreur");	
		}
		} catch (Exception e){
			System.out.println("Erreur le login : nexiste pas");
		}
	}
}
