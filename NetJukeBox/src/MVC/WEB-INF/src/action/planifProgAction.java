package action;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.planifProgForm;

public class planifProgAction extends Action {

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
		
		planifProgForm planifForm = (planifProgForm)form;

		String idprog = planifForm.getIdprog();
		String idcanal = planifForm.getIdcanal();
		String jour = planifForm.getJour();
		String mois = planifForm.getMois();
		String annee = planifForm.getAnnee();
		String heure = planifForm.getHeure();
		String minute = planifForm.getMinute();
		String seconde = planifForm.getSeconde();

		response.setContentType("text/html");
		
		boolean planifie = clientXML.planifierProgramme(sessionLogin, idprog, idcanal, jour, mois, annee, heure, minute, seconde);
		
		if (planifie) {
			String result = "INFO: Programme planifié sur le canal";
			
			session.setAttribute("Resultat" , result);
			return mapping.findForward("ok");
		} else {
			String erreur = "ERREUR: Programme non planifié";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}	
	}
}