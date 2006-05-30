package action;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.modifierDocForm;

public class validemodifDocAction extends Action {

	/**
	 * Client XMLRPC
	 */
	@SuppressWarnings("unused")
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
		
		modifierDocForm docForm = (modifierDocForm)form;
		HttpSession session = request.getSession();
		clientXML = (XMLClient) session.getAttribute("client");
		sessionLogin = (String) session.getAttribute("login");
		
		String id = docForm.getId();
        String titre = docForm.getTitre();
		String duree = docForm.getDuree();
		String jour = docForm.getJour();
		String mois = docForm.getMois();
		String annee = docForm.getAnnee();
		String source = docForm.getSource();
		String langue = docForm.getLangue();
		String genre = docForm.getGenre();
		String fichier = docForm.getFichier();
		String artiste = docForm.getArtiste();
		String interprete = docForm.getInterprete();
		String compositeur = docForm.getCompositeur();
			
		boolean modifie = clientXML.modifierDocument(sessionLogin, id, titre, duree, jour, mois, annee, source, langue,
				genre, fichier, artiste, interprete, compositeur);
		
		if (modifie) {
			String result = "INFO: Document modifié";
				
			session.setAttribute("Resultat" , result);
			return mapping.findForward("ok");
		} else {
		   	String erreur = "ERREUR: Document non modifié";
			
			session.setAttribute("Resultat" , erreur);
        	return mapping.findForward("failed");
        }		   
	}
}