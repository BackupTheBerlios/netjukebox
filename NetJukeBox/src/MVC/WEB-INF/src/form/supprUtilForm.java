package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class supprUtilForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String login;

	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		login = null;
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
		return ae;
	}

	public String getLogin()	{
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
}