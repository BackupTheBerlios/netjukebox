import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.InitialContext;

public class Recherche {
	public static void main(String[] args) 
{
try {
Hashtable<String,String> env = new Hashtable<String,String>();
	
env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
env.put("java.naming.provider.url", "ldap://localhost:389/dc=netjukebox,dc=com");

Context ictx = new InitialContext(env);
NamingEnumeration<NameClassPair> e = ictx.list("ou=admin");
while (e.hasMore()) {
    System.out.println("name: " + e.next().getName());
    }
}

catch (javax.naming.NamingException e) {
    System.err.println("Exception " + e);
    }
}
}

