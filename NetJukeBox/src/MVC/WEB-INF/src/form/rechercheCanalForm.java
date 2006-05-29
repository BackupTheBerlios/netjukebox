package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class rechercheCanalForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String id;
	private String nom;
	private String nbmaxutil;

	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		id = null;
		nom = null;
		nbmaxutil = null;
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

	public String getNbmaxutil() {
		return nbmaxutil;
	}

	public void setNbmaxutil(String nbmaxutil) {
		this.nbmaxutil = nbmaxutil;
	}
}