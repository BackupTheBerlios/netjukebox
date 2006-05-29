package action;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.rechercheProgForm;

public class validemodifProgAction extends Action {

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
		
		rechercheProgForm progForm = (rechercheProgForm)form;
		
		HttpSession session = request.getSession();
		clientXML = (XMLClient) session.getAttribute("client");
		sessionLogin = (String) session.getAttribute("login");
		
		String id = progForm.getId();
		String titre = progForm.getTitre();
		String thematique = progForm.getThematique();
		
		response.setContentType("text/html");
		boolean modifie = clientXML.modifierProgramme(sessionLogin, id, titre, thematique);
		
		if (modifie) {
			String result = "INFO: Programme modifié";
			
			session.setAttribute("Resultat" , result);
			return mapping.findForward("ok");
		} else {
			String erreur = "ERREUR: Programme non modifié";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}	
		
	}
}