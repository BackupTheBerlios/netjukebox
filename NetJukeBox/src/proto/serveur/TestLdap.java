package proto.serveur;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Vector;

import javax.naming.NameClassPair;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

public class TestLdap {
		
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws NamingException, IOException {
		Ldap ldap = Ldap.getInstance();
		ldap.openLdap("com.sun.jndi.ldap.LdapCtxFactory","ldap://55.24.8.10:389/dc=netjukebox,dc=com","simple", "admin", "admin","mot2passe");
		//ldap.executeSupprimer("bollard");
		//ldap.executeCreer("new", "new", "new", "new", "netjukebox@gmail.com", "Pays", "usager");
		//ldap.executeCreer("login", "password", "toto", "titi", "toto@gmail.com", "france", "usager");
		//ldap.executeCreer("log", "pass", "tata", "tutu", "tata@gmail.com", "france", "respprog");
		//ldap.changerRole("login", "usager", "admin");
	
		//ldap.ModifieAttributs("mail", "new@gmail", "login", "admin");
		
//CODE POUR LE LISTAGE DES GROUPES
	     	 Vector vect= ldap.ListeGroupe();
	         for(int i=0; i < vect.size(); i++)
	            if(vect.elementAt(i) != null)
	               System.out.println (vect.elementAt(i));
	            else System.out.println("vect[" + i + "] est null");
	         
//CODE POUR LA SUPPRESSION D'UN GROUPE
				
			ldap.CreationGroupe("test");
			ldap.SupprimerGroupe("test");
	        
	         /**if(vect.elementAt(1) != null){
	              vect.set(1, new Integer(1000));
	              }
	         System.out.println("après : " + vect);
	         vect.remove(0);
	         System.out.println("après remove : " + vect + "   et  taille = "   
	                              + vect.size());
	      }*/

}
}

	        
				
			
			/**
		Enumeration donnee = result.elements();
		Enumeration colonne = ((Object) result).keys();
		for(int i = 0; i < result.size(); i++){
			System.out.print(colonne.nextElement() + " : "); 
			System.out.println(donnee.nextElement());
			}	*/
		/**
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
	}*/
	


		
