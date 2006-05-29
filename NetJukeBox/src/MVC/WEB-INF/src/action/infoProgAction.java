package action;

import java.util.Dictionary;
import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.infoForm;

public class infoProgAction extends Action {

	/**
	 * Client XMLRPC
	 */
	private XMLClient clientXML = null;
	
	/**
	 * Login de l'utilisateur sur la session
	 */
	private String sessionLogin;
	
	/** 
	 * Method execute
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @return ActionForward
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping,	ActionForm form,
		HttpServletRequest request,	HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		clientXML = (XMLClient) session.getAttribute("client");
		sessionLogin = (String) session.getAttribute("login");
		
		infoForm progForm = (infoForm)form;

		String id = progForm.getId();

		response.setContentType("text/html");
		
		Dictionary dProg = clientXML.infoProgramme(sessionLogin, id);
		Vector vProg = new Vector();
		
		if (dProg!=null) {
			vProg.add(dProg);
			Vector vDocs = (Vector)dProg.get("documents");
			
			session.setAttribute("Resultat" , vProg);
			session.setAttribute("Documents" , vDocs);
			
			return mapping.findForward("ok");
		} else {
			String erreur = "WARNING: Aucun programme disponible";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}
	}
}