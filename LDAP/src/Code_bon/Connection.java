package Code_bon;

import java.util.Hashtable;
import javax.naming.directory.*;
import javax.naming.*;
//connection et d�connection � la base

class Connection 
{
    public static void main(String[] args) {
   	
    Hashtable<String,String> env = new Hashtable<String,String>();
	
    env.put(DirContext.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
	env.put(DirContext.PROVIDER_URL, "ldap://localhost:389/dc=netjukebox,dc=com");
try {
	DirContext ctx = new InitialDirContext(env);
    System.out.println("Vous avez �t� connect� � la base "+ env);
    ctx.close();
    System.out.println("Vous avez �t� d�connect� de la base.");
	 }
		
	catch (NamingException e) {
	e.printStackTrace();
	}
}
}
