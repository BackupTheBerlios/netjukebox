package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class modifierUtilisateurForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String login;
	private String nom;
	private String prenom;
	private String pwd;
	private String mail;
	private String pays;
	
	
	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		login = null;
		nom = null;
		prenom = null;
		pwd = null;
		mail = null;
		pays = null;
	}

	/** 
	 * Method validate
	 * @param ActionMapping mapping
	 * @param HttpServletRequest request
	 * @return ActionErrors
	 */
	@SuppressWarnings("deprecation")
	public ActionErrors validate(ActionMapping mapping,	HttpServletRequest request)	{
		ActionErrors ae = new ActionErrors();
		
		if (login.length() == 0) {
			ae.add(login, new ActionError("error.login"));
		}
		if (nom.length() == 0) {
			ae.add(login, new ActionError("error.nom"));
		}
		if (prenom.length() == 0) {
			ae.add(login, new ActionError("error.prenom"));
		}
		if (mail.length() == 0) {
			ae.add(login, new ActionError("error.mail"));
		}
		if (pays.length() == 0) {
			ae.add(login, new ActionError("error.pays"));
		}
		//if (pwd.length() == 0) {
		//	ae.add(login, new ActionError("error.password"));
		//}
		
		return ae;
	}

	public String getLogin()	{
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getNom()	{
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
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
	
	public String getPass() {
		return pwd;
	}

	public void setPass(String pass) {
		this.pwd = pass;
	}
}