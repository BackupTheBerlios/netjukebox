package action;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.deplanifProgForm;

public class deplanifProgAction extends Action {

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
		
		deplanifProgForm deplanifForm = (deplanifProgForm)form;

		String idcanal = deplanifForm.getIdcanal();
		String calage = deplanifForm.getCalage();

		response.setContentType("text/html");
		
		boolean deplanifie = clientXML.deplanifierProgramme(sessionLogin, idcanal, calage);
		
		if (deplanifie) {
			String result = "INFO: Programme déplanifié sur le canal";
			
			session.setAttribute("Resultat" , result);
			return mapping.findForward("ok");
		} else {
			String erreur = "ERREUR: Programme non déplanifié";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}	
	}
}