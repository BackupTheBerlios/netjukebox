package action;

import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import plugin.XMLClient;
import form.rechercheDocForm;

public class rechercheDocAction extends Action {

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
		
		rechercheDocForm DocForm = (rechercheDocForm)form;

		String id = DocForm.getId();
		String titre = DocForm.getTitre();
		String duree = DocForm.getDuree();
		String jour = DocForm.getJour();
		String mois = DocForm.getMois();
		String annee = DocForm.getAnnee();
		String source = DocForm.getSource();
		String langue = DocForm.getLangue();
		String genre = DocForm.getGenre();
		String fichier = DocForm.getFichier();
		String artiste = DocForm.getArtiste();
		String interprete = DocForm.getInterprete();
		String compositeur = DocForm.getCompositeur();
				
		response.setContentType("text/html");
		
		Vector vProg = clientXML.rechercherDocument(sessionLogin, id, titre, duree, jour, mois, annee, 
				source, langue, genre, fichier, artiste, interprete, compositeur);
		
		if (vProg!=null && vProg.size()>0) {
			session.setAttribute("Resultat" , vProg);
			return mapping.findForward("ok");
		} else {
			String erreur = "WARNING: Aucun document disponible";
			
			session.setAttribute("Resultat" , erreur);
			return mapping.findForward("failed");
		}
	}
}