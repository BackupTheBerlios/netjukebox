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
		//clientXML = (XMLClient) session.getAttribute("client");
		clientXML = XMLClient.getInstance();
		sessionLogin = (String) session.getAttribute("login");
		
		modifierContratForm contratForm = (modifierContratForm)form;

		String id = contratForm.getId();
		String titre = contratForm.getTitre();
		String joursig = contratForm.getJourSignature();
		String moissig = contratForm.getMoisSignature();
		String anneesig = contratForm.getAnneeSignature();
		String jourexp = contratForm.getJourExpiration();
		String moisexp = contratForm.getMoisExpiration();
		String anneeexp = contratForm.getAnneeExpiration();
		String idcontractant = contratForm.getContractant();
		String reg = contratForm.getModeReglement();
		String type = contratForm.getType();

		response.setContentType("text/html");
		
		boolean modifie = clientXML.modifierContrat(sessionLogin, id, titre, joursig, moissig, anneesig,
				jourexp, moisexp, anneeexp, idcontractant, reg, type);
		
		if (modifie) {
			String result = "INFO: Contrat modifié";
			
			session.setAttribute("Resultat" , result);
			return mapping.findForward("ok");
		} else {
			String erreur = "ERREUR: Contrat non modifié";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}	
	}
}