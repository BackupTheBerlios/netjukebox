package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class recherchePermForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String id;
	private String libelle;
	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		id = null;
		libelle = null;
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
		return ae;
	}

	public String getId()	{
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getLibelle()	{
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
}