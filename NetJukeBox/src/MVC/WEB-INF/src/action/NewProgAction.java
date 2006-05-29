package action;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.NewProgForm;

public class NewProgAction extends Action {

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
		
		NewProgForm progForm = (NewProgForm)form;
		
		HttpSession session = request.getSession();
		clientXML = (XMLClient) session.getAttribute("client");
		sessionLogin = (String) session.getAttribute("login");
		
		String titre = progForm.getTitre();
		String thematique = progForm.getThematique();
		
		response.setContentType("text/html");
		boolean cree = clientXML.creerProgramme(sessionLogin, titre, thematique);
		
		if (cree) {
			String result = "INFO: Programme créé";
			
			session.setAttribute("Resultat" , result);
			return mapping.findForward("ok");
		} else {
			String erreur = "ERREUR: Programme non créé";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}	
		
	}
}