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
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 * Interface la base ldap avec JNDI
 */
public class Ldap {
	/**
	 * Singleton
	 */
	private static Ldap instance;
	
	/**
	 * Nom du driver
	 */
	private String driver; //= "com.sun.jndi.ldap.LdapCtxFactory";
	
	/**
	 * URL de la base
	 */
	private String url; //= "ldap://localhost:389/dc=netjukebox,dc=com";
	
	/**
	 * Mode d'authentification à la base
	 */
	private String auth; //= "simple";
	
	/**
	 * Login d'accès à la base
	 */
	private String login; //= "cn=admin,dc=netjukebox,dc=com";
	
	/**
	 * Mot de passe d'accès à la base
	 */
	private String pwd; //= "mot2passe";
	
	/**
	 * Role de l'usager
	 */
	private String role;
	
	/**
	 * Nom de l'annuaire ldap
	 */
	private String base;
	
	/**
	 * Connection courante à la base
	 */
	private static InitialDirContext connect = null;
	
//	 CONSTRUCTEUR
//	********************************************
	/**
	 * Constructeur vide (Singleton)
	 */
		public Ldap() {
		}
	    
//	 METHODES STATIQUES
//	********************************************
	/**
	 * Retourne l'instance du Singleton
	 */
		public static synchronized Ldap getInstance(){
		if (instance == null)
			instance = new Ldap();
		return instance;
	}
	
//  METHODES DYNAMIQUES
//	********************************************	
	/**
	 * Connecte à la base ldap
     * @param String driver
     * @param String url
     * @param String auth
     * @param String login
     * @param String pwd
	 */
	public boolean openLdap(String driver, String url, String auth, String login, String pwd, String role, String base) {
    	this.driver = driver;
    	this.url = url;
    	this.login = login;
    	this.pwd = pwd;
    	this.auth = auth;
    	this.role = role;
    	this.base = base;
    	
    	return this.openLdap();
    }
	
	/**
	 * Se connecte à la base ldap
	 * @return boolean
	 */
	public boolean openLdap() {
		String log = "uid=" + login + ",ou=" + role + "," + base;
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(DirContext.INITIAL_CONTEXT_FACTORY,driver);
		env.put(DirContext.PROVIDER_URL, url);
		env.put(Context.SECURITY_AUTHENTICATION, auth);
		env.put(Context.SECURITY_PRINCIPAL, log);
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
	
	/**
	 * Ferme la connexion à la base de données
	 * @return boolean
	 * @throws NamingException
	 */
	public boolean closeldap() throws NamingException {
		if (connect != null) {
			connect.close();
			return true;
		}
    	System.out.println("Vous avez été déconnecté de la base.");
		return false;
	}
	
	/**
	 * Supprime un utilisateur
	 * @param login
	 * @param role
	 * @return
	 * @throws NamingException
	 */
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
				System.err.println("Il y a un probleme pour suppression de donnée! " + e);
				return false;
			}
	}
	
	/**
	 * Modifie les attributs d'un utilisateur
	 * @param champs
	 * @param donnee
	 * @param nom
	 * @param role
	 * @return
	 */
	public boolean ModifieAttributs(String champs, String donnee, String nom, String role) {
		String requete = "uid=" + nom + ",ou=" + role;
		Dictionary attr = getLogin(nom);
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
	
	/**
	 * Crée un utilisateur
	 * @param login
	 * @param pwd
	 * @param nom
	 * @param prenom
	 * @param mail
	 * @param pays
	 * @param role
	 * @return
	 */
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
	
	/**
	 * Change le role d'un utilisateur du Netjukebox
	 * @param login
	 * @param ancienrole
	 * @param nouveaurole
	 * @return
	 */
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
	
	/**
	 * 
	 * @param log
	 * @return
	 */
	public Dictionary getLogin(String login){
		try {
			// Specify the ids of the attributes to return
			String[] attr = {"uid", "userPassword", "sn", "givenName", "cn", "mail", "st", "ou"};
			SearchControls ctls = new SearchControls();
			ctls.setReturningAttributes(attr);
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			String filter = "uid=" + login;
			// Search for objects using filter
			NamingEnumeration answer = connect.search("", filter, ctls);
			//Print the answer
			Dictionary resultat = printSearchEnumeration(answer);
			return resultat;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Recherche du login dans les branches de l'annuaire
	 * @param answer
	 * @return Dictionary
	 */
	@SuppressWarnings("unchecked")
	private Dictionary printSearchEnumeration(NamingEnumeration answer) {
		Dictionary resultat = new Hashtable();
		try {
		    while (answer.hasMore()) {
		    	SearchResult sr = (SearchResult) answer.next();
		    	resultat.put("resultat", sr.getAttributes());
		    }
		    return resultat;
		} catch (NamingException e) {
		    e.printStackTrace();
		    return  null;
		}
	}
	
	
	/**
	 * Récupère les attributs d'un utilisateur
	 * @param login
	 * @param role
	 * @return
	 */
	
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
	
	/**
	 * Récupère les attributs d'un utilisateur
	 * @param attrs
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	private Dictionary printAttrs(Attributes attrs) {
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
	
}