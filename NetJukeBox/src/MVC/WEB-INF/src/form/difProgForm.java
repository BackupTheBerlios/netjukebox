package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class difProgForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String idcanal;
	private String idprog;


	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		idcanal = null;
		idprog = null;
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

		if (idcanal.length() == 0) {
			ae.add(idcanal, new ActionError("error.id"));
		}
		if (idprog.length() == 0)	{
			ae.add(idcanal, new ActionError("error.id"));
		}

		return ae;
	}

	public String getIdcanal() {
		return idcanal;
	}

	public void setIdcanal(String idcanal) {
		this.idcanal = idcanal;
	}
	
	public String getIdprog() {
		return idprog;
	}

	public void setIdprog(String idprog) {
		this.idprog = idprog;
	}
}