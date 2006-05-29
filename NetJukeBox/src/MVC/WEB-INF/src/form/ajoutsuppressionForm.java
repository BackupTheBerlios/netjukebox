package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class ajoutsuppressionForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String id1;
	private String id2;


	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		id1 = null;
		id2 = null;
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

		if (id1.length() == 0) {
			ae.add(id1, new ActionError("error.id"));
		}
		if (id2.length() == 0)	{
			ae.add(id2, new ActionError("error.id"));
		}

		return ae;
	}

	public String getId1() {
		return id1;
	}

	public void setId1(String id1) {
		this.id1 = id1;
	}
	
	public String getId2() {
		return id2;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}
}