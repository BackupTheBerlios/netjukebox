package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class LoginForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String pass;
	private String login;

	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		login = null;
		pass = null;
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
		if (pass.length() == 0)	{
			ae.add(login, new ActionError("error.password"));
		}
		return ae;
	}

	public String getPass()	{
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
}
