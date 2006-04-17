package Code_bon;

import java.util.Hashtable;
import javax.naming.*;
import javax.naming.directory.*;

class Supprimer {

public static void main(String[] args) {
	
	Hashtable<String,String> env = new Hashtable<String,String>();
	env.put(DirContext.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
	env.put(DirContext.PROVIDER_URL, "ldap://localhost:389/dc=netjukebox,dc=com");
	env.put(Context.SECURITY_AUTHENTICATION, "simple");
	env.put(Context.SECURITY_PRINCIPAL, "cn=admin,dc=netjukebox,dc=com");
	env.put(Context.SECURITY_CREDENTIALS, "secret");
	
	/* Donn�es de l'utilisateur a supprimer */
	String dn =  "cn=Jacques Smith, " + "ou=usager";

	try{
		DirContext ctx = new InitialDirContext(env);	
			try {
			/* Supprimer l'uilisateur sp�cifi� plus haut */
				ctx.destroySubcontext(dn);
				System.out.println( "Entr�e supprim�e " + dn + "." );
				} catch (NameNotFoundException e) {
					System.out.println( "Cette entr�e" + dn + " n'existe pas dans cette branche.");
				} 
		}
				catch (NamingException e) {
				System.err.println("Il y a un probleme pour suppression de donn�e!" + e);
				System.exit(1);
				}
	
		}

}