package Code_bon;

import javax.naming.*;
import javax.naming.directory.*;

import java.util.Hashtable;
//Affiche tous les attributs d'un utilisateur ou d'une branche

class GetAttributs {
    static void printAttrs(Attributes attrs) {
	if (attrs == null) {
	    System.out.println("No attributes");
	} else {

	    try {
		for (NamingEnumeration ae = attrs.getAll();
		     ae.hasMore();) {
		    Attribute attr = (Attribute)ae.next();

		    for (NamingEnumeration e = attr.getAll();
			 e.hasMore();
			 System.out.println(attr.getID()+ ":" + e.next()));
		}
	    } catch (NamingException e) {
		e.printStackTrace();
	    }
	}
    }

    public static void main(String[] args) {

			Hashtable<String,String> env = new Hashtable<String,String>();
			env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
			env.put("java.naming.provider.url", "ldap://localhost:389/dc=netjukebox,dc=com");
	try {
	    DirContext ctx = new InitialDirContext(env);

	    Attributes answer = ctx.getAttributes("sn=bollard,ou=respprog");

	    printAttrs(answer);

	    ctx.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
