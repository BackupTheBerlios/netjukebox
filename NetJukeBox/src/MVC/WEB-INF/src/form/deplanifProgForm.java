package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class deplanifProgForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String idcanal;
	private String calage;


	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		idcanal = null;
		calage = null;
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
		if (calage.length() == 0)	{
			ae.add(idcanal, new ActionError("error.calage"));
		}

		return ae;
	}

	public String getIdcanal() {
		return idcanal;
	}

	public void setIdcanal(String idcanal) {
		this.idcanal = idcanal;
	}
	
	public String getCalage() {
		return calage;
	}

	public void setCalage(String calage) {
		this.calage = calage;
	}
}