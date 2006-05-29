package action;

import java.util.Dictionary;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.rechercheDocForm;

public class infoDocAction extends Action {

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
		clientXML = (XMLClient) session.getAttribute("client");
		sessionLogin = (String) session.getAttribute("login");
		
		rechercheDocForm DocForm = (rechercheDocForm)form;

		String id = DocForm.getId();
				
		response.setContentType("text/html");
		
		Dictionary ddoc = clientXML.infoDocument(sessionLogin, id);
		
		if (ddoc!=null) {
			session.setAttribute("Resultat" , ddoc);
			return mapping.findForward("ok");
		} else {
			String erreur = "WARNING: Aucun document disponible";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}
	}
}