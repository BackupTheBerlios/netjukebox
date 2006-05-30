package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class modifierDocForm extends ActionForm {
	// --------------------------------------------------------- Instance Variables
	private String id;
	private String titre;
	private String duree;
	private String jour;
	private String mois;
	private String annee;
	private String source;
	private String langue;
	private String genre;
	private String fichier;
	private String artiste;
	private String interprete;
	private String compositeur;

	// --------------------------------------------------------- Methods
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		id = null;
		titre = null;
		duree = null;
		jour = null;
		mois = null;
		annee = null;
		source = null;
		langue = null;
		genre = null;
		fichier = null;
		artiste = null;
		interprete = null;
		compositeur = null;
	}

	/** 
	 * Method validate
	 * @param ActionMapping mapping
	 * @param HttpServletRequest request
	 * @return ActionErrors
	 */
	@SuppressWarnings("deprecation")
	public ActionErrors validate(ActionMapping mapping,	HttpServletRequest request)	{
		ActionErrors ae = new ActionErrors();
		
		if (id.length() == 0) {
			ae.add(id, new ActionError("error.id"));
		}
		if (titre.length() == 0) {
			ae.add(id, new ActionError("error.titre"));
		}
		if (duree.length() == 0) {
			ae.add(id, new ActionError("error.duree"));
		}
		if (jour.length() == 0) {
			ae.add(id, new ActionError("error.jour"));
		}
		if (mois.length() == 0) {
			ae.add(id, new ActionError("error.mois"));
		}
		if (annee.length() == 0) {
			ae.add(id, new ActionError("error.annee"));
		}
		if (source.length() == 0) {
			ae.add(id, new ActionError("error.source"));
		}
		if (langue.length() == 0) {
			ae.add(id, new ActionError("error.langue"));
		}
		if (genre.length() == 0) {
			ae.add(id, new ActionError("error.genre"));
		}
		if (fichier.length() == 0) {
			ae.add(id, new ActionError("error.fichier"));
		}
		if (artiste.length() == 0) {
			ae.add(id, new ActionError("error.artiste"));
		}
		if (interprete.length() == 0) {
			ae.add(id, new ActionError("error.interprete"));
		}
		if (compositeur.length() == 0) {
			ae.add(id, new ActionError("error.compositeur"));
		}
		
		return ae;
	}

	public String getId()	{
		return id;
	}
	
	public String getTitre()	{
		return titre;
	}

	public String getDuree() {
		return duree;
	}
	
	public String getJour() {
		return jour;
	}
	
	public String getMois() {
		return mois;
	}
	
	public String getAnnee() {
		return annee;
	}
	
	public String getSource() {
		return source;
	}
	
	public String getLangue() {
		return langue;
	}
	
	public String getGenre() {
		return genre;
	}
	
	public String getFichier() {
		return fichier;
	}
	
	public String getArtiste() {
		return artiste;
	}
	
	public String getInterprete() {
		return interprete;
	}
	
	public String getCompositeur() {
		return compositeur;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setTitre(String titre) {
		this.titre = titre;
	}

	public void setDuree(String duree) {
		this.duree = duree;
	}
	
	public void setJour(String jour) {
		this.jour = jour;
	}
	
	public void setMois(String mois) {
		this.mois = mois;
	}
	
	public void setAnnee(String annee) {
		this.annee = annee;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public void setLangue(String langue) {
		this.langue = langue;
	}
	
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public void setFichier(String fichier) {
		this.fichier = fichier;
	}
	
	public void setArtiste(String artiste) {
		this.artiste = artiste;
	}
	
	public void setInterprete(String interprete) {
		this.interprete = interprete;
	}
	
	public void setCompositeur(String compositeur) {
		this.compositeur = compositeur;
	}	
}