package proto.serveur;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;


public class TestLdap {
		
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws NamingException, IOException {
		Ldap ldap = Ldap.getInstance();
		ldap.openLdap("com.sun.jndi.ldap.LdapCtxFactory", "ldap://192.168.0.2:389/dc=netjukebox,dc=com", "simple",  "bollard", "phix", "dc=netjukebox,dc=com");
		//ldap.openLdap("com.sun.jndi.ldap.LdapCtxFactory","ldap://192.168.0.2:389/dc=netjukebox,dc=com","simple", "bollard","respprog","phix");
		//ldap.executeSupprimer("bollard");
		//ldap.executeCreer("new", "new", "new", "new", "netjukebox@gmail.com", "Pays", "usager");
		//ldap.executeCreer("login", "password", "toto", "titi", "toto@gmail.com", "france", "usager");
		//ldap.executeCreer("log", "pass", "tata", "tutu", "tata@gmail.com", "france", "respprog");
		//ldap.changerRole("login", "usager", "admin");
	
		//ldap.ModifieAttributs("mail", "new@gmail", "login", "admin");
		
//CODE POUR LE LISTAGE DES GROUPES
 

	
	    
//	       CODE POUR LE LISTAGE DES GROUPES

	    Vector vect1= ldap.listerUtilisateur();
	      for(int i=0; i < vect1.size(); i++)
		            if(vect1.elementAt(i) != null){
		            	Attributes dico = (Attributes)vect1.elementAt(i);
		            	Attribute uidAtt = (Attribute) dico.get("uid");
		    			String uid = (String)uidAtt.get();
		    			System.out.println(uid);
		    			Attribute snAtt = (Attribute) dico.get("sn");
		    			String sn = (String)snAtt.get();
		    			System.out.println(sn);
		    			Attribute givenNameAtt = (Attribute) dico.get("givenName");
		    			String givenName = (String)givenNameAtt.get();
		    			System.out.println(givenName);
		    			Attribute mailAtt = (Attribute) dico.get("mail");
		    			String mail = (String)mailAtt.get();
		    			System.out.println(mail);
		    			Attribute stAtt = (Attribute) dico.get("st");
		    			String st = (String)stAtt.get();
		    			System.out.println(st);
		    			Attribute ouAtt = (Attribute) dico.get("ou");
		    			String ou = (String)ouAtt.get();
		    			System.out.println(ou);
	     	 			}
	     	else System.out.println("vect[" + i + "] est null");
	
			
	

//CODE POUR LA SUPP	ESSION D'UN GROUPE
				
		//	ldap.CreationGroupe("test");
		//	ldap.SupprimerGroupe("test");
	        
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
	


		
