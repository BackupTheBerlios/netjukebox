package action;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.infoForm;

public class stopCanalAction extends Action {

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
		//clientXML = (XMLClient) session.getAttribute("client");
		clientXML = XMLClient.getInstance();
		sessionLogin = (String) session.getAttribute("login");
		
		infoForm canalForm = (infoForm)form;

		String id = canalForm.getId();

		response.setContentType("text/html");	
		
		if (clientXML.stopCanal(sessionLogin, id)) {
			String result = "INFO: Canal arrété";
			
			session.setAttribute("Resultat" , result);
			return mapping.findForward("ok");
		} else {
			String erreur = "ERREUR: Canal non stoppé";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}
	}
}