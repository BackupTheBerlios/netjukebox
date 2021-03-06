package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class rechercheCanalForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String id;
	private String nom;
	private String utilMax;

	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		id = null;
		nom = null;
		utilMax = null;
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
	
	public String getNom()	{
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getutilMax() {
		return utilMax;
	}

	public void setutilMax(String utilMax) {
		this.utilMax = utilMax;
	}
}