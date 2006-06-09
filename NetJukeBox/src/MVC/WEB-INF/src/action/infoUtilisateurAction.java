package action;

import java.util.Dictionary;
import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.supprUtilForm;

public class infoUtilisateurAction extends Action {

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
		
		supprUtilForm utilForm = (supprUtilForm)form;

		String login = utilForm.getLogin();

		response.setContentType("text/html");
		
		Vector vUtil = clientXML.infoUtilisateur(sessionLogin, login);
		
		if (vUtil!=null) {
			Dictionary v = (Dictionary)vUtil.get(1);
			
			Vector vPermUtil = (Vector) v.get("permutil");
			Vector vPermRole = (Vector) v.get("permrole");
			
			session.setAttribute("Resultat" , vUtil);
			session.setAttribute("PermUtil" , vPermUtil);
			session.setAttribute("PermRole" , vPermRole);
			
			return mapping.findForward("ok");
		} else {
			String erreur = "WARNING: Aucun canal disponible";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}
	}
}