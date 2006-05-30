package action;

import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.listeForm;

public class listePermAction extends Action {

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
		
		@SuppressWarnings("unused") listeForm progForm = (listeForm)form;


		response.setContentType("text/html");
		
		Vector vPerm = clientXML.listerPermissions(sessionLogin);
		
		if (vPerm!=null) {
			
			session.setAttribute("Resultat" , vPerm);
			return mapping.findForward("ok");
		} else {
			String erreur = "WARNING: Aucune permission disponible";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}
	}
}