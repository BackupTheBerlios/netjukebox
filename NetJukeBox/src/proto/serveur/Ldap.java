package proto.serveur;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;

public class Ldap {
	private static Ldap instance;
	private String driver; //= "com.sun.jndi.ldap.LdapCtxFactory";
	private String url; //= "ldap://localhost:389/dc=netjukebox,dc=com";
	private String auth; //= "simple";
	private String login; //= "cn=admin,dc=netjukebox,dc=com";
	private String pwd; //= "mot2passe";
	private static InitialDirContext connect = null;
	
//	 CONSTRUCTEUR
//	********************************************

		public Ldap() {
		}
	    
//	 METHODES STATIQUES
//	********************************************
	public static synchronized Ldap getInstance(){
		if (instance == null)
			instance = new Ldap();
		return instance;
	}
	
//  METHODES DYNAMIQUES
//	********************************************	
	public boolean openLdap(String driver, String url, String auth, String login, String pwd) {
    	this.driver = driver;
    	this.url = url;
    	this.login = login;
    	this.pwd = pwd;
    	this.auth = auth;
    	
    	return this.openLdap();
    }
	
	public boolean openLdap() {
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(DirContext.INITIAL_CONTEXT_FACTORY,driver);
		env.put(DirContext.PROVIDER_URL, url);
		env.put(Context.SECURITY_AUTHENTICATION, auth);
		env.put(Context.SECURITY_PRINCIPAL, login);
		env.put(Context.SECURITY_CREDENTIALS, pwd);
	    if (connect == null) {
	    	try {
	    		// Connexion à l'annuaire
	    		connect = new InitialDirContext(env);
	    		System.out.println("Vous avez été connecté à la base "+ env);
	    		return true;
	    	} catch (NamingException e) {
	    		e.printStackTrace();
	    		return false;
	    	}
	    }
	    return false;
	}
	
	public boolean closeldap() throws NamingException {
		if (connect != null) {
			connect.close();
			return true;
		}
    	System.out.println("Vous avez été déconnecté de la base.");
		return false;
	}

	public boolean executeSupprimer(String login, String role) throws NamingException {
		String requete =  "uid=" + login + ",ou=" + role;
		Dictionary attr = getAttributs(login, role);
		if (attr == null) {
			return false;
		} else 
			try {
				connect.destroySubcontext(requete);
				System.out.println( "Entrée supprimée " + requete + "." );
				return true;
			} catch (NameNotFoundException e) {
				System.out.println( "Cette entrée" + requete + " n'existe pas dans cette branche.");
				return false;
			} 
			catch (NamingException e) {
				System.err.println("Il y a un probleme pour suppression de donnée!" + e);
				return false;
			}
	}
	
	public boolean ModifieAttributs(String champs, String donnee, String nom, String role) {
		String requete = "uid=" + nom + ",ou=" + role;
		Dictionary attr = getAttributs(nom, role);
		if (attr == null) {
			return false;
		} else
		try {
			/* construct the list of modifications to make */
			ModificationItem[] mods = new ModificationItem[2];
			Attribute mod0 = new BasicAttribute(champs, donnee);
			// Update mail attribute
			mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, mod0);
			/* Delete the description attribute altogether */
			Attribute mod1 = new BasicAttribute("description","Dernière modification : " + (new Date()).toString());
			mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, mod1);
			/* make the change */
			connect.modifyAttributes(requete, mods);
			System.out.println( "La modification est faite!." );
			return true;
		} 
		catch (NamingException e) {
			System.err.println("La modification a échoué. " + e);
			return false;
		}
	}

	public boolean executeCreer(String login, String pwd, String nom, String prenom, String mail, String pays, String role) {
		// entry's DN
		String entryDN = "uid=" + login + ",ou=" + role;
		String personne = nom + " " + prenom;
		// entry's attributes
		Attribute uid = new BasicAttribute("uid", login);
		Attribute userPassword = new BasicAttribute("userPassword", pwd);
		Attribute sn = new BasicAttribute("sn", nom);
		Attribute givenName = new BasicAttribute("givenName", prenom);
		Attribute cn = new BasicAttribute("cn", personne);
		Attribute email = new BasicAttribute("mail", mail);
		Attribute p = new BasicAttribute("st", pays);
		Attribute r = new BasicAttribute("ou", role);
    	
		Attribute oc = new BasicAttribute("objectClass");
    	oc.add("top");
    	oc.add("person");
    	oc.add("organizationalPerson");
    	oc.add("inetOrgPerson");
    	
    	try {
    		// build the entry
    		Attributes entry = new BasicAttributes();
    		entry.put(uid);
    		entry.put(userPassword);
    		entry.put(sn);
    		entry.put(givenName);
    		entry.put(cn);
    		entry.put(email);
    		entry.put(p);
    		entry.put(r);
    		entry.put(oc);
    		// Add the entry
    		connect.createSubcontext(entryDN, entry);
    		System.out.println( "L'utilisateur "+ uid +" est crée sous "+entryDN+"!");
    		return true ;
    	} catch (NamingException e) {
    		System.err.println("Erreur pour la création de l'utilisateur "+ uid +"!"+ e);
    		return false;
    	}
	}

	public Dictionary getAttributs(String login, String role) {
		String requete = "uid=" + login + ",ou=" + role;
		try {
			Attributes answer = connect.getAttributes(requete);
			Dictionary ligne = printAttrs(answer);
			return ligne;
		} catch (Exception e) {
			System.out.println("Erreur l'utilisateur : "+ login + " n'existe pas");
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	Dictionary printAttrs(Attributes attrs) {
		Dictionary ligne = new Hashtable();
		if (attrs == null) {
    		System.out.println("No attributes");
    	} else {
    		try {
    			for (NamingEnumeration ae = attrs.getAll();
	    			ae.hasMore();) {
    				Attribute attr = (Attribute)ae.next();

    				for (NamingEnumeration e = attr.getAll(); 
    					e.hasMore();
     					ligne.put(attr.getID(), e.next()));
    			}	
    			return ligne;
    		} catch (NamingException e) {
    			System.out.println("Erreur l'utilisateur n'existe pas");
    			return null;
    		}
    	}
		return ligne;
	}	
	
	public boolean changerRole(String login, String ancienrole, String nouveaurole){
		String ancien = "uid=" + login + ", ou=" + ancienrole;
		String nouveau = "uid=" + login + ", ou=" + nouveaurole;
		try {
			connect.rename(ancien, nouveau);
			//System.out.println(connect.lookup(nouveau));
		    return true;
		} catch (NamingException e) {
			System.out.println("Erreur l'utilisateur n'existe pas");
		    return false;
		}
	}
}