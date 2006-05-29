package action;

import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.rechercheProgForm;

public class rechercheProgAction extends Action {

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
		
		rechercheProgForm progForm = (rechercheProgForm)form;

		String id = progForm.getId();
		String titre = progForm.getTitre();
		String thematique = progForm.getThematique();

		response.setContentType("text/html");
		
		Vector vProg = clientXML.rechercherProgramme(sessionLogin, id, titre, thematique);
		
		if (vProg!=null && vProg.size()>0) {
			session.setAttribute("Resultat" , vProg);
			return mapping.findForward("ok");
		} else {
			String erreur = "WARNING: Aucun programme disponible";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}
	}
}