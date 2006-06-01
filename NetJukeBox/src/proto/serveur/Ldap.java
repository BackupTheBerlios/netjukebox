package proto.serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
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
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Interface la base ldap avec JNDI
 */
public class Ldap{
	/**
	 * Cr�e le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(Ldap.class);

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
	 * Mode d'authentification � la base
	 */
	private String auth; // = "simple";
	
	/**
	 * Login d'acc�s � la base
	 */
	private String login; //= "cn=admin,dc=netjukebox,dc=com";
	
	/**
	 * Mot de passe d'acc�s � la base
	 */
	private String pwd; //= "mot2passe";
	
	/**
	 * Role de l'usager
	 */
	private String role; // = "usager";
	
	/**
	 * Nom de l'annuaire ldap
	 */
	private String base;
	
	/**
	 * Connection courante � l'annuaire
	 */
	private static InitialDirContext connectanonyme = null;
	
	/**
	 * Connection via login & mot de passe � l'annuaire
	 */
	public DirContext connect = null;
	
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
	 * Connecte � la base ldap
     * @param String driver
     * @param String url
     * @param String auth
     * @param String login
     * @param String pwd
	 */
	public boolean openLdap(String driver, String url, String auth, String login, String pwd, String base) {
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
	 * Se connecte � la base ldap
	 * @return boolean
	 */
public boolean openLdap() {
			//PropertyConfigurator.configure("C:/Documents and Settings/Marie Rubini/Mes documents/workspace/NetJukeBox/proto/serveur/log4j.prop");
			logger.debug("D�marrage: openLdap");
			
		String role = null;
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(DirContext.INITIAL_CONTEXT_FACTORY,driver);
		env.put(DirContext.PROVIDER_URL, url);	
		
	    if (connect == null) {
	    	try {
	    		//Connexion � l'annuaire
	    		connectanonyme = new InitialDirContext(env);
	    		
	    		Dictionary resultats = getLogin(login);
				logger.info("login :"+ login);
				logger.info(resultats);
					    		
	    		try {
	    			Enumeration donnee = resultats.elements();
	    			Attributes result = (Attributes) donnee.nextElement();
	    			Attribute ou = result.get("ou");
	    			role = (String) ou.get();
	    		} catch(Exception e) {
					logger.fatal("openLdap :", e);
					}

				logger.info("role: "+ role);			
	    		String log = "uid=" + login + ",ou=" + role + "," + base;
	    		
	      		connect = (DirContext) connectanonyme.lookup("");
	    		connect.addToEnvironment(Context.SECURITY_AUTHENTICATION, auth);
	    		connect.addToEnvironment(Context.SECURITY_PRINCIPAL, log);
	    		connect.addToEnvironment(Context.SECURITY_CREDENTIALS, pwd);
	    
				logger.info("Vous avez �t� connect� � la base " + env);								
				logger.debug("Arr�t: openLdap");				
	    		return true;
	    		
	    	} catch (NamingException e) {
				logger.error("openLdap: "+ e);			
				logger.debug("Arr�t: openLdap");
				return false;
	    	}
	    }
		logger.debug("Arr�t: openLdap");
		return false;
	}
	
	/**
	 * Ferme la connexion � la base de donn�es
	 * @return boolean
	 * @throws NamingException
	 */
	public boolean closeldap() throws NamingException {
		logger.debug("D�marrage: closeldap");
	
		if (connect != null) {
			connect.close();
			logger.debug("Arr�t: closeldap");
			return true;
		}
		
		logger.info("Vous avez �t� d�connect� de la base.");
		logger.debug("Arr�t: closeldap");
		return false;
	}
	
	/**
	 * Supprime un utilisateur
	 * @param login
	 * @param role
	 * @return
	 * @throws NamingException
	 */
	
public boolean executeSupprimer(String login) throws NamingException {
		logger.debug("D�marrage: executeSupprimer");
			
		Dictionary resultat = getLogin(login);
		try {
			Enumeration donnee = resultat.elements();
			Attributes result = (Attributes) donnee.nextElement();
			Attribute log = result.get("uid");
			Attribute branche = result.get("ou");
			String l =(String) log.get();
			String b =(String) branche.get();
			logger.info("L'utilisateur " + l +" en tant que "+ b + " existe bien.");
			connect.destroySubcontext("uid="+l+",ou="+ b);
			logger.info("L'utilisateur " + l + " r�f�renc� en tant que " + b + " a �t� supprim�.");
			logger.debug("Arr�t: executeSupprimer");
			return true;
		}
		catch (Exception e){
			logger.error("L'utilisateur "+ login + " n'existe pas.");
			logger.debug("Arr�t: executeSupprimer");
			return false;
			} 
		}

	/**
	 * Modifie les attributs d'un utilisateur
	 * @param nom
	 * @param role
	 * @param nouveau nom
	 * @param nouveau prenom
	 * @param nouveau mail
	 * @param nouveau pays
	 * @return
	 * @throws NamingException 
	 */
	public boolean ModifieAttributs(String login, String role, String newlogin, String newpwd, String newnom, String newprenom, String newmail, String newpays) throws NamingException {
		logger.debug("D�marrage: ModifieAttributs");
		String log=login;
		
		if (!newlogin.equalsIgnoreCase(login)) {
			Dictionary resultat = getLogin(newlogin);
			try {
				Enumeration donnee = resultat.elements();
				Attributes result = (Attributes) donnee.nextElement();
				Attribute l = result.get("uid");
				logger.info(l.get());				
				String r =(String) l.get();
				logger.info("L'utilisateur : "+ newlogin+ " existe d�j�! Votre login ne peut �tre modifi�!");
				log=login;
			} catch (Exception e){
				logger.info("L'utilisateur : "+ newlogin+ " n'existe pas. Votre login va �tre modifi�!");
				connect.rename("uid=" + login + ",ou=" + role,"uid=" + newlogin + ",ou=" + role );
				log=newlogin;
			}
		} 
					
		Dictionary attr = getAttributs(log, role);	
		String requete = "uid=" + log + ",ou=" + role;
		String nomactuel = (String) attr.get("cn");
		String prenomactuel = (String) attr.get("givenName");
		String mailactuel = (String) attr.get("mail");
		String paysactuel = (String) attr.get("st");
		String pwdactuel = (String) attr.get("userPassword");
		
		ModificationItem[] mods = new ModificationItem[6];
		try {
			// Remplace la valeur de l'attribut SN avec la nouvelle valeur
		    if (!newnom.equalsIgnoreCase(nomactuel))
		    mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
			new BasicAttribute("sn", newnom));
		    		
		    // Remplace la valeur de l'attribut MAIL avec la nouvelle valeur
		    if (!newmail.equalsIgnoreCase(mailactuel))
		    mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
			new BasicAttribute("mail", newmail));
		    
		    // Remplace la valeur de l'attribut GIVENNAME avec la nouvelle valeur
		    if (!newprenom.equalsIgnoreCase(prenomactuel))
		    mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
			new BasicAttribute("givenName", newprenom));
		    
		    // Remplace la valeur de l'attribut ST avec la nouvelle valeur
		    if (!newpays.equalsIgnoreCase(paysactuel))
		    mods[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
			new BasicAttribute("st", newpays));

		    // Remplace la valeur de l'attribut ST avec la nouvelle valeur
		    if (!newpwd.equalsIgnoreCase(pwdactuel))
		    mods[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
			new BasicAttribute("userPassword", newpwd));
		    
		    String newcn;
		    // Remplace la valeur de l'attribut CN avec la nouvelle valeur
		    if (!newnom.equalsIgnoreCase(nomactuel)) {
				if (!newprenom.equalsIgnoreCase(prenomactuel)) { 
					newcn=(newnom + " " + newprenom);
				} else { newcn=(newnom + " " + prenomactuel);
	 			}
			} else {
				if (!newprenom.equalsIgnoreCase(prenomactuel)) {
					newcn=(nomactuel + " " + newprenom);
				} else { newcn=(nomactuel+ " " + prenomactuel);
				}
			}
		    mods[5] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("cn", newcn));
			
			logger.info("test");
		    // Modifie les attributs de l'objet
		    connect.modifyAttributes(requete, mods);
			logger.info("Les modifications sont faites!");
			logger.debug("Arr�t: ModifieAttributs");
			return true;
		} catch (NamingException e) {
			logger.error(" Les modifications ont �chou�.");
			logger.debug("Arr�t: ModifieAttributs");
			return false;
		}
	}
	
	/**
	 * Cr�e un utilisateur
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
		logger.debug("D�marrage: executeCreer");
	
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
			logger.info("L'utilisateur "+ login + " est cr�e. Il appartient au groupe " + role + "!");
			logger.debug("Arr�t: executeCreer");
			return true ;
    	} catch (NamingException e) {
			logger.error("L'utilisateur "+ login + " n'a pas pu �tre cr��!");
			logger.debug("Arr�t: executeCreer");
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
		logger.debug("D�marrage: changerRole");
		String ancien = "uid=" + login + ", ou=" + ancienrole;
		String nouveau = "uid=" + login + ", ou=" + nouveaurole;
		try {
			connect.rename(ancien, nouveau);
			//System.out.println(connect.lookup(nouveau));
			logger.debug("Arr�t: changerRole");
			return true;
		} catch (NamingException e) {
			logger.error("L'utilisateur n'existe pas!");
			logger.debug("Arr�t: changerRole");
			return false;
		}
	}
	
	/**
	 * V�rification de la pr�sence du login dans l'annuaire
	 * @param log
	 * @return
	 */
	public Dictionary getLogin(String login){
		logger.debug("D�marrage: getLogin");
		try {
			// Specify the ids of the attributes to return
			String[] attr = {"uid", "userPassword", "sn", "givenName", "cn", "mail", "st", "ou"};
			SearchControls ctls = new SearchControls();
			ctls.setReturningAttributes(attr);
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			String filter = "uid=" + login;
			// Search for objects using filter
			NamingEnumeration answer = connectanonyme.search("", filter, ctls);
			//Print the answer
			Dictionary resultat = printSearchEnumeration(answer);
			logger.debug("Arr�t: getLogin");
			return resultat;
		} catch (Exception e) {
			logger.error("getLogin: "+ e);
			logger.debug("Arr�t: getLogin");
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
		    	resultat.put(resultat, sr.getAttributes());
		    	System.out.println(sr.getAttributes());
		    }
		    return resultat;
		} catch (NamingException e) {
			logger.error("printSearchEnumeration: ", e);
		    return  null;
		}
	}
	
	/**
	 * R�cup�re les attributs d'un utilisateur
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
			logger.error("L'utilisateur : "+ login + " n'existe pas");
			}
			return null;
		}
	

	/**
	 * R�cup�re les attributs d'un utilisateur
	 * @param attrs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Dictionary printAttrs(Attributes attrs) {
		Dictionary ligne = new Hashtable();
		if (attrs == null) {
			logger.info("Pas d'attributs � afficher!");
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
				logger.error("L'utilisateur n'existe pas!");
				return null;
			}
		}
		return ligne;
	}

	/**
	 * R�cup�re les groupes de l'annuaire
	 */

	public Vector ListeGroupe() {
		logger.debug("D�marrage: getSchema");
		Vector result=new Vector();
		try {
	    	String searchBase    = "";
			String searchFilter  = "(objectclass=*)";
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope( SearchControls.ONELEVEL_SCOPE );
			    
	        NamingEnumeration answer = connectanonyme.search( searchBase,searchFilter,constraints );
	       
	           	while (answer.hasMore()) {
		       		result = printSearchEnumeration2(answer);
		       	}
	        logger.debug("Arr�t: getSchema");
	       	return result;
	       	
		} catch (Exception e) {
			logger.error("getSchema: "+ e);
			logger.debug("Arr�t: getSchema");
			return null;
		}
	}

	private Vector printSearchEnumeration2(NamingEnumeration answer) {
		Vector vec = new Vector(100); int i=0;
		try {
			while (answer.hasMore()) {
				SearchResult sr = (SearchResult) answer.next();
				vec.insertElementAt(sr.getName(),i);
				i++;
			}
			return vec;
	    
		} catch (NamingException e) {
			logger.error("printSearchEnumeration: ", e);
			return  null;
		}
	}
	

	/**
	 * Cr�ation d'un groupe de l'annuaire
	 */

public void CreationGroupe(String groupe) {
		logger.debug("D�marrage: CreationGroupe");
		
		try {
			Attributes attrs = new BasicAttributes(true); 
		    Attribute objclass = new BasicAttribute("objectclass");
		    objclass.add("top");
		    objclass.add("organizationalUnit");
		    attrs.put(objclass);

		    Context result = connectanonyme.createSubcontext("ou="+"groupe", attrs);

		    NamingEnumeration list = connectanonyme.list("");

		    while (list.hasMore()) {
			NameClassPair nc = (NameClassPair)list.next();
			logger.info(nc);
		    }
		    
		    logger.error("La cr�ation du groupe "+ groupe +"a �chou�!");
		    logger.debug("Arr�t: Cr�ationGroupe");
		    result.close();

		} catch (NamingException e) {
			logger.error("ERREUR: "+ e);
		    logger.error("La cr�ation du groupe "+ groupe +"a �chou�!");
		    logger.debug("Arr�t: Cr�ationGroupe");
		}
}
    /**
	 * Suppression d'un groupe de l'annuaire
	 */

public void SupprimerGroupe(String groupe) {
		logger.debug("D�marrage: SupprimerGroupe");
	
 				try {
				    connect.destroySubcontext("ou=test");
				    NamingEnumeration list = connect.list("");

				    while (list.hasMore()) {
					NameClassPair nc = (NameClassPair)list.next();
					System.out.println(nc);
				    }

				    logger.info("Le groupe "+groupe+" a �t� supprim�!");
					logger.debug("Arr�t: SupprimerGroupe");
				    
				
		} catch (Exception e) {
			logger.error("ERREUR SupprimerGroupe: "+ e);
			logger.info("Le groupe "+groupe+" n'a pas pu �tre supprim�!");
			logger.debug("Arr�t: SupprimerGroupe");

		}
}    
}


