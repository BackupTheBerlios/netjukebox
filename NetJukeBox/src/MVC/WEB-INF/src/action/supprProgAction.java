package action;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.supprForm;

public class supprProgAction extends Action {

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
	public ActionForward execute(ActionMapping mapping,	ActionForm form,
		HttpServletRequest request,	HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		//clientXML = (XMLClient) session.getAttribute("client");
		clientXML = XMLClient.getInstance();
		sessionLogin = (String) session.getAttribute("login");
		
		supprForm docForm = (supprForm)form;

		String id = docForm.getId();

		response.setContentType("text/html");
		
		boolean suppr = clientXML.supprimerProgramme(sessionLogin, id);
		
		if (suppr) {
			String result = "INFO: Programme supprim�";
			
			session.setAttribute("Resultat" , result);
			return mapping.findForward("ok");
		} else {
			String erreur = "ERREUR: Suppression du programme �chou�e";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}	
	}
}