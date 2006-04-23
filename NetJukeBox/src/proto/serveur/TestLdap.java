package proto.serveur;

import java.util.Dictionary;
import java.util.Enumeration;
import javax.naming.NamingException;

public class TestLdap {
		
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws NamingException {
		Ldap ldap = Ldap.getInstance();
		
		ldap.openLdap("com.sun.jndi.ldap.LdapCtxFactory", "ldap://localhost:389/dc=netjukebox,dc=com", 
				"simple", "cn=admin,dc=netjukebox,dc=com", "mot2passe");

		//ldap.executeCreer("login", "password", "toto", "titi", "toto@gmail.com", "france", "usager");
		//ldap.executeCreer("log", "pass", "tata", "tutu", "tata@gmail.com", "france", "respprog");
		//ldap.changerRole("login", "usager", "admin");
		
		//ldap.ModifieAttributs("mail", "new@gmail", "login", "admin");
		
		
		Dictionary resultats = ldap.getAttributs("login", "usager");
		Enumeration donnee = resultats.elements();
		Enumeration colonne = resultats.keys();
		for(int i = 0; i < resultats.size(); i++){
			System.out.print(colonne.nextElement() + " : "); 
			System.out.println(donnee.nextElement());
		}

		//ldap.executeSupprimer("login", "admin");
	}
}
