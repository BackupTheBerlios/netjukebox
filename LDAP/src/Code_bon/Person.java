package Code_bon;

public class Person {
    public String nom;
    public String prenom;
    public String prenomnom;
    public String passwd;
    public String mail;
    public String origine;    
   
    public Person(String sn, String givenName, String cn, String userPassword, String mail, String pays) {
    	this.nom = sn;
    	this.prenom = givenName;
    	this.prenom = cn;
    	this.passwd = userPassword;
    	this.mail = mail;
    	this.origine = pays;
	}

    public String toString() {
	return "Mon nom est " + nom + ", " + prenom + ".";
    }
}
