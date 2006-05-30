package action;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.modifierContratForm;

public class validemodifContratAction extends Action {

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
		
		modifierContratForm contratForm = (modifierContratForm)form;

		String id = contratForm.getId();
		String titre = contratForm.getTitre();
		String joursig = contratForm.getJoursig();
		String moissig = contratForm.getMoissig();
		String anneesig = contratForm.getAnneesig();
		String jourexp = contratForm.getJourexp();
		String moisexp = contratForm.getMoisexp();
		String anneeexp = contratForm.getAnneeexp();
		String idcontractant = contratForm.getIdcontractant();
		String reg = contratForm.getReg();
		String type = contratForm.getType();

		response.setContentType("text/html");
		
		boolean modifie = clientXML.modifierContrat(sessionLogin, id, titre, joursig, moissig, anneesig,
				jourexp, moisexp, anneeexp, idcontractant, reg, type);
		
		if (modifie) {
			String result = "INFO: Contrat modifi�";
			
			session.setAttribute("Resultat" , result);
			return mapping.findForward("ok");
		} else {
			String erreur = "ERREUR: Contrat non modifi�";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}	
	}
}