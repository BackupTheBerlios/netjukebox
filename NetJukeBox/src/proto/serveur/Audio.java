package proto.serveur;

/**
 * Document AUDIO
 */
public class Audio extends Document {

// ATTRIBUTS
//************************************
	
	/**
	 * Artiste
	 */
	private String artiste;

	/**
	 * Interprète
	 */
	private String interprete;

	/**
	 * Compositeur
	 */
	private String compositeur;

// CONSTRUCTEUR
//************************************
	
	/**
	 * Constructeur
	 * @param String id
	 * @param String titre
	 * @param int duree
	 * @param int jour
	 * @param int mois
	 * @param int annee
	 * @param String source
	 * @param String langue
	 * @param String genre
	 * @param String fichier
	 * @param String artiste
	 * @param String interprete
	 * @param String compositeur
	 */
	public Audio(String id, String titre, int duree, int jour,
			int mois, int annee, String source, String langue, String genre,
			String fichier, String artiste, String interprete, String compositeur) {
		
		//Appel du constructeur Document avec les infos globales
		//super(id, titre, duree, jour, mois, annee, source, langue, genre, fichier);
		
		//Initialisation des attributs spécifiques
		this.artiste = artiste;
		this.interprete = interprete;
		this.compositeur = compositeur;
	}
	
// METHODES STATIQUES
//************************************
	
// METHODES DYNAMIQUES
//************************************
	/**
	 * @return Renvoie artiste.
	 */
	public String getArtiste() {
		return artiste;
	}

	/**
	 * @param artiste artiste à définir.
	 */
	public void setArtiste(String artiste) {
		this.artiste = artiste;
	}

	/**
	 * @return Renvoie compositeur.
	 */
	public String getCompositeur() {
		return compositeur;
	}

	/**
	 * @param compositeur compositeur à définir.
	 */
	public void setCompositeur(String compositeur) {
		this.compositeur = compositeur;
	}

	/**
	 * @return Renvoie interprete.
	 */
	public String getInterprete() {
		return interprete;
	}

	/**
	 * @param interprete interprete à définir.
	 */
	public void setInterprete(String interprete) {
		this.interprete = interprete;
	}
}
