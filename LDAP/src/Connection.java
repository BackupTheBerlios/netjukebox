import javax.naming.*;
import java.util.Hashtable;

class Connection {
    public static void main(String[] args) {
   	Hashtable<String,String> env = new Hashtable<String,String>();
	env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
	env.put(Context.PROVIDER_URL, "ldap://localhost:389/dc=netjukebox,dc=com");

	try {
		Context ictx = new InitialContext(env);
	    ictx.close();

	} catch (NamingException e) {
	    e.printStackTrace();
	}
    }
}
