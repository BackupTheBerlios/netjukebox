package proto.serveur;

public class TestLdap {
		
	public static void main(String[] args) {
		Ldap ldap = Ldap.getInstance();
		ldap.openLdap("com.sun.jndi.ldap.LdapCtxFactory", "ldap://localhost:389/dc=netjukebox,dc=com", 
				"simple", "cn=admin,dc=netjukebox,dc=com", "mot2passe");
		
		ldap.executeCreer("gentaz", "Dominique gentaz", "Dominique", "domi@gmail.com", "domi", "respprog");
		ldap.executeSupprimer("Titi", "usager");
		ldap.ModifieAttributs("mail", "new@gmail", "gentaz", "respprog");
		ldap.getAttributs("gentaz", "respprog");		
	}
}
