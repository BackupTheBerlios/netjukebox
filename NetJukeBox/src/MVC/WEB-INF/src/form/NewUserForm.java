package form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class NewUserForm extends ActionForm {

	private String login;
	private String pass;
	private String nom;
	private String prenom;
	private String mail;
	private String pays;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		login = null;
		pass = null;
		nom = null;
		prenom = null;
		mail = null;
		pays = null;
	}

	/** 
	 * Method validate
	 * @param ActionMapping mapping
	 * @param HttpServletRequest request
	 * @return ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping,	HttpServletRequest request)	{
		
		ActionErrors ae = new ActionErrors();

		if (login.length() == 0)
			ae.add(login, new ActionMessage("error.login"));
		if (pass.length() == 0)
			ae.add(login, new ActionMessage("error.password"));
		if (nom.length() == 0)
			ae.add(login, new ActionMessage("error.nom"));
		if (prenom.length() == 0)
			ae.add(login, new ActionMessage("error.prenom"));
		if (mail.length() == 0)
			ae.add(login, new ActionMessage("error.mail"));
		if (pays.length() == 0)
			ae.add(login, new ActionMessage("error.pays"));
		
		return ae;
	}

	public String getPass()	{
		return pass;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public String getPays() {
		return pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}
}
