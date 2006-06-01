package action;

import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.rechercheContratForm;

public class rechercheContratAction extends Action {

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
		
		rechercheContratForm contratForm = (rechercheContratForm)form;

		String id = contratForm.getId();
		String titre = contratForm.getTitre();
		String joursig = contratForm.getJoursig();
		String moissig = contratForm.getMoissig();
		String anneesig = contratForm.getAnneesig();
		String jourexp = contratForm.getJourexp();
		String moisexp = contratForm.getMoisexp();
		String anneeexp = contratForm.getAnneeexp();
		String idcontractant = contratForm.getContractant();
		String reg = contratForm.getModeReglement();
		String type = contratForm.getType();
		
		response.setContentType("text/html");
		
		Vector vContrat = clientXML.rechercherContrat(sessionLogin, id, titre, joursig, moissig, 
				anneesig, jourexp, moisexp, anneeexp, idcontractant, reg, type);
		
		if (vContrat!=null && vContrat.size()>0) {
			session.setAttribute("Resultat" , vContrat);
			return mapping.findForward("ok");
		} else {
			String erreur = "WARNING: Aucun contrat disponible";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}
	}
}