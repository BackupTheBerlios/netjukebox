package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class infoForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String id;

	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		id = null;
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
		
		if (id.length() == 0) {
			ae.add(id, new ActionError("error.id"));
		}
		return ae;
	}

	public String getId()	{
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}