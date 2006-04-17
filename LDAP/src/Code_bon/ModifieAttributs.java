package Code_bon;

import java.util.Hashtable;
import java.util.Date;
import javax.naming.*;
import javax.naming.directory.*;

class ModifieAttributs {
public static void main(String[] args) {
	
	Hashtable<String,String> env = new Hashtable<String,String>();
	env.put(DirContext.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
	env.put(DirContext.PROVIDER_URL, "ldap://localhost:389/dc=netjukebox,dc=com");
	env.put(Context.SECURITY_AUTHENTICATION, "simple");
	env.put(Context.SECURITY_PRINCIPAL, "cn=admin,dc=netjukebox,dc=com");
	env.put(Context.SECURITY_CREDENTIALS, "secret");
	
try {

	/* get a handle to an Initial DirContext */
	DirContext ctx = new InitialDirContext(env);
	
	/* construct the list of modifications to make */
	ModificationItem[] mods = new ModificationItem[2];
	Attribute mod0 = new BasicAttribute("mail", "new@gmail");
	
	// Update mail attribute
	mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, mod0);
	
	
	/* Delete the description attribute altogether */
	Attribute mod1 = new BasicAttribute("description","Dernière modification : " + (new Date()).toString());
	mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, mod1);
		
	/* make the change */
	ctx.modifyAttributes("sn=bollard,ou=respprog", mods);
	System.out.println( "La modification est faite!." );

	} 
	catch (NamingException e) {
		System.err.println("La modification a échoué. " + e);
}
}
}