package action;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.difProgForm;

public class difProgAction extends Action {

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
		
		difProgForm difForm = (difProgForm)form;

		String idcanal = difForm.getIdcanal();
		String idprog = difForm.getIdprog();

		response.setContentType("text/html");
		
		boolean dif = clientXML.diffuserProgramme(sessionLogin, idcanal, idprog);
		
		if (dif) {
			String result = "INFO: Diffusion du programme lancée sur le canal";
			
			session.setAttribute("Resultat" , result);
			return mapping.findForward("ok");
		} else {
			String erreur = "ERREUR: Diffusion du programme non lancée sur le canal";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}	
	}
}