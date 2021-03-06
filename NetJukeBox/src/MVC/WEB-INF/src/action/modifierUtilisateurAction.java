package action;

import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.supprUtilForm;

public class modifierUtilisateurAction extends Action {

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

		session.setAttribute("logUtil", login);
		response.setContentType("text/html");
		
		Vector vUtil = clientXML.rechercherUtilisateur(sessionLogin, login);
		
		if (vUtil!=null && vUtil.size()>0) {		
			session.setAttribute("Resultat" , vUtil);
			return mapping.findForward("ok");
		} else {
			String erreur = "WARNING: Aucun utilisateur disponible";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}
	}
}