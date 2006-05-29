package form;

import javax.servlet.http.*;
import org.apache.struts.action.*;

@SuppressWarnings("serial")
public class rechercheDocForm extends ActionForm {
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