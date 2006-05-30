package action;

import java.io.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import plugin.XMLClient;
import form.NewDocForm;

public class NewDocAction extends Action {

	/**
	 * Client XMLRPC
	 */
	@SuppressWarnings("unused")
	private XMLClient clientXML = null;
	
	/**
	 * Chemin pour déposer le fichier audio
	 */
	private String chemin = "/home/netjukebox/";
	
	/**
	 * Booléen validant le dépôt du fichier
	 */
	@SuppressWarnings("unused")
	private boolean depot = false;
	
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
		
		NewDocForm docForm = (NewDocForm)form;
		HttpSession session = request.getSession();
		clientXML = (XMLClient) session.getAttribute("client");
		sessionLogin = (String) session.getAttribute("login");
		
		FormFile fichier = docForm.getFichier();
        String fileName = fichier.getFileName();
        String path = chemin + fileName;
        
        try {
            InputStream inStream = fichier.getInputStream();
            FileOutputStream outStream = new FileOutputStream(path);
            while (inStream.available() > 0) {
                  outStream.write(inStream.read());
                  depot = true;
            }
        } catch (FileNotFoundException fnfe) {
        	return null;
        }

        if (depot == true) {
        	String titre = docForm.getTitre();
			String duree = docForm.getDuree();
			String jour = docForm.getJour();
			String mois = docForm.getMois();
			String annee = docForm.getAnnee();
			String source = docForm.getSource();
			String langue = docForm.getLangue();
			String genre = docForm.getGenre();
			String artiste = docForm.getArtiste();
			String interprete = docForm.getInterprete();
			String compositeur = docForm.getCompositeur();
			
			boolean cree = clientXML.creerDocument(sessionLogin, titre, duree, jour, mois, 
				annee, source, langue, genre, path, artiste, interprete, compositeur);
		
			if (cree) {
				String result = "INFO: Document créé";
				
				session.setAttribute("Resultat" , result);
				return mapping.findForward("ok");
			} else {
				File file = new File(path);
				file.delete();
				
				String erreur = "ERREUR: Document non créé";
				
				session.setAttribute("Resultat" , erreur);
				return mapping.findForward("failed");
			}
        } else {
        	String erreur = "ERREUR: Document non créé";
			
			session.setAttribute("Resultat" , erreur);
        	return mapping.findForward("failed");
        }		   
	}
}