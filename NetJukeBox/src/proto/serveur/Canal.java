package proto.serveur;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Canal de diffusion
 */
public class Canal {

// ATTRIBUTS DU CANAL (propres � un objet)
// *************************************************

	/**
	 * Identifiant
	 */
	private String id;

	/**
	 * Nom du canal
	 */
	private String nom;

	/**
	 * Nombre maximal d'auditeurs
	 */
	private int utilMax;

	
	/**
	 * Port de diffusion
	 */
	private String port;
	
	/**
	 * Etat du canal
	 */
	private String etat;

	/**
	 * #DEFINITION#
	 */
	private Diffusion diffusion;

	/**
	 * Historisation
	 */
	private Journal_Canal journal;
	
	
	/**
	 * Auditeurs du canal
	 */
	private Vector auditeurs;
	
	/**
	 * RTP Server
	 */
	private RTPServer RTP = null;
	
	/**
	 * Liste de programmes � diffuser
	 */
	private Hashtable programmes;

// CONSTRUCTEUR (cr�� une instance de la classe : un objet)
// *************************************************
	
	/**
	 * Constructeur
	 * @param id
	 * @param nom
	 * @param fluxMax
	 */
	public Canal(String id, String nom, int utilMax) {
		this.auditeurs = new Vector();
		this.id = id;
		this.nom = nom;
		this.utilMax = utilMax;
		//this.programmes = getProgrammesPlanifies();
	}

// METHODES STATIQUES (propres � la classe, inaccessibles depuis un objet)
// *************************************************
	
	/**
	 * Cr�ation du canal en base
	 * @param String nom
	 * @param int utilmax
	 * @return Canal
	 * @throws SQLException 
	 */
	public static Canal create(String nom, int utilmax) /*throws SQLException*/ {
		
		System.out.println("Canal.create()");
		
		String requete = "INSERT INTO canal (nom, utilmax) VALUES ('" + nom + "', '" + utilmax + "');";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si l'insertion s'est bien d�roul�e
		if (nbRows>0) {
			//On retourne ensuite un objet pour ce canal
			return Canal.getByNom(nom);
		}
		
		//Sinon, retourne un objet vide
		return null;
	}
	
	/**
	 * Cr�� et remplit un objet avec les infos issues de la base, � partir d'un nom
	 * @param String nom
	 * @return Canal
	 * @throws SQLException 
	 */
	public static Canal getByNom(String nom) /*throws SQLException*/ {
		
		System.out.println("Canal.getByNom("+nom+")");
		
		//On va chercher les infos depuis la base, en partant d'un nom
		String requete = "SELECT * FROM canal WHERE nom = '" + nom + "';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		// S'il y a un resultat
		if (resultats != null && resultats.size()>0) {
			
			//On prend le 1er �l�ment
			Dictionary dico = (Dictionary) resultats.firstElement();
			
			//On mappe les champs
			String id = String.valueOf((Integer)dico.get("id"));
			String nomCanal = (String)dico.get("nom");
			int utilMax = (int)(Integer)dico.get("utilmax");
			
			System.out.println("-------- Canal -----------");
			System.out.println("Nb de champs: "+dico.size());
			System.out.println("ID: "+id);
			System.out.println("Nom: "+nomCanal);
			System.out.println("Nb max d'auditeurs: "+utilMax);
			System.out.println("-----------------------------");
			
			//On retourne l'objet
			return new Canal(id, nomCanal, utilMax);
		}
		
		/*
		//------------ TEST SANS JDBC ---------------
		if (nom.equalsIgnoreCase("classic")) {
			//On retourne un objet canal configur�
			return new Canal("1", "classic", 10);
		}
		//-------------------------------------------
		*/
		
		//Sinon, on retourne un objet vide
		return null;
		
	}
	
	/**
	 * Cr�� et remplit un objet avec les infos issues de la base, � partir d'un id
	 * @param String id
	 * @return Canal
	 * @throws SQLException 
	 */
	public static Canal getById(String id) /*throws SQLException*/ {
		
		System.out.println("Canal.getById("+id+")");
		
		//On va chercher les infos depuis la base, en partant d'un id
		String requete = "SELECT * FROM canal WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		// S'il y a un resultat
		if (resultats != null && resultats.size()>0) {
			
			//On prend le 1er �l�ment
			Dictionary dico = (Dictionary) resultats.firstElement();
			
			//On mappe les champs
			String idCanal = String.valueOf((Integer)dico.get("id"));
			String nom = (String)dico.get("nom");
			int utilMax = (int)(Integer)dico.get("utilmax");
			
			System.out.println("-------- Canal -----------");
			System.out.println("Nb de champs: "+dico.size());
			System.out.println("ID: "+idCanal);
			System.out.println("Nom: "+nom);
			System.out.println("Nb max d'auditeurs: "+utilMax);
			System.out.println("-----------------------------");
			
			//On retourne l'objet
			return new Canal(idCanal, nom, utilMax);
		}
		
		/*
		//------------ TEST SANS JDBC ---------------
		if (id.equalsIgnoreCase("1")) {
			//On retourne un objet canal configur�
			return new Canal("1", "classic", 10);
		}
		//-------------------------------------------
		*/
		
		//Sinon, on retourne un objet vide
		return null;
	}
	
	/**
	 * Retourne un vecteur d'objets canaux instanci�s � partir de toutes les infos de la base 
	 * @return Hashtable
	 * @throws SQLException 
	 */
	public static Hashtable getAll() /*throws SQLException*/ {
		
		System.out.println("Canal.getAll()");
		
		//On cr�e un vecteur pour contenir les objets canaux instanci�s
		Hashtable canaux = new Hashtable();
		
		String requete = "SELECT * FROM canal;";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		System.out.println("Canal.getAll() : "+resultats.size()+" canal(canaux) trouv�(s)");
		
		// Pour chaque canal, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id"));
			canaux.put(id, Canal.getById(id));
		}
		
		/*
		//-------- TEST SANS JDBC -------------
		canaux.put("1", Canal.getById("1"));
		//-------------------------------------
		*/
		
		//On retourne le vecteur contenant les objets canaux instanci�s
		return canaux;
	}
	
	/**
	 * D�truit les infos d'un canal contenues dans la base
	 * @param String id
	 * @return boolean
	 * @throws SQLException 
	 */
	public static boolean deleteById(String id) /*throws SQLException*/ {
		
		//On supprime le canal de la base, en partant d'un id
		String requete = "DELETE FROM canal WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//On retourne le resultat de l'op�ration (succ�s/�chec)
		return nbRows>0;
	}

// METHODES DYNAMIQUES (propres � un objet, inaccessibles depuis la classe)
// *************************************************
	
	/**
	 * Planifie un programme � diffuser
	 * @param Programme p
	 * @param String jour
	 * @param String mois
	 * @param String mois
	 * @param String annee
	 * @param String heure
	 * @param String minute
	 * @param String seconde
	 * @return boolean
	 */
	public boolean planifierProgramme(Programme p, int jour, int mois, int annee, int heure, int minute, int seconde) {
		
		//Timestamp date = new Timestamp(annee, mois, jour, heure, minute, seconde, 0);
		
		
		return true;
	}
	
	
	/**
	 * Retourne la liste des programmes planifi�s
	 * @return Hastable
	 */
	private Hashtable getProgrammesPlanifies() {
		System.out.println("Canal.getProgrammesPlanifies()");
		
		//On cr�e un vecteur pour contenir les objets documents instanci�s
		Hashtable progs = new Hashtable();
		
		//On va chercher dans la base la liste des id de tous les programmes
		String requete = "SELECT id_prog, calage FROM diffuser WHERE id_canal = '"+id+"';";
		Jdbc base = Jdbc.getInstance();
		Vector resultats = base.executeQuery(requete);
		
		System.out.println("Canal.getProgrammesPlanifies() : "+resultats.size()+" programme(s) trouv�(s)");
		
		// Pour chaque programme, on instancie un objet que l'on stocke dans le vecteur
		for (int j = 0; j < resultats.size(); j++) {
			Dictionary dico = (Dictionary) resultats.elementAt(j);
			String id = String.valueOf((Integer)dico.get("id_prog"));
			progs.put(id, Programme.getById(id));
		}
		
		return progs;
	}
	
	/**
	 * Modifie les attributs
	 * @param String nom
	 * @param int utilMax
	 * @return boolean
	 */
	public boolean modifier(String nom, int utilMax) {
		
		String requete = "UPDATE canal SET nom = '" + nom + "', utilMax = '" + utilMax + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.nom = nom;
			this.utilMax = utilMax;
		}
		return nbRows>0;
	}
	
	/**
	 * Retourne l'ensemble des attributs sous la forme d'un dictionnaire
	 * @return Dictionary
	 */
	public Dictionary getAttributesDictionary() {
		
		Dictionary dico = new Hashtable();
		
		dico.put("id", id);
		dico.put("nom", nom);
		dico.put("utilMax", utilMax);
		
		return dico;
	}
	
	/**
	 * Retourne l'URL du canal
	 * @return String
	 */
	public String getUrlStreaming() {
		if (RTP!=null) {
			return RTP.getUrl();
		}
		return null;
	}
	
	/**
	 * Cr�ation du RTPServer
	 * @param ip
	 * @param port
	 */
	public void createRTPServer(String ip, int port, String publicite) {
		
		//Si le RTPServer n'existe pas, on le cr�e
		if (this.RTP == null) this.RTP = new RTPServer(ip, port, publicite);
	}
	
	public boolean isRTPstarted() {
		return RTP != null;
	}
	
	/**
	 * Lance la diffusion
	 */
	public void startDiffusion() {
		
		//Si le RTPServer existe, on le stoppe
		if (this.RTP != null) this.RTP.diffuser();
	}
	
	/**
	 * Arr�te la diffusion
	 */
	public void stopDiffusion() {
		
		//Si le RTPServer existe, on le stoppe
		if (this.RTP != null) this.RTP.stop();
	}

	/**
	 * D�truit le canal et ses infos en base
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean supprimer() /*throws SQLException*/ {
		
		//On stoppe la diffusion �ventuelle
		this.stopDiffusion();
		
		//On supprime les infos de la base
		return Canal.deleteById(this.id);
	}

	/**
	 * V�rifie si le canal est actif
	 * @return boolean
	 */
	public boolean estActif() {
		return etat.equalsIgnoreCase("ACTIF");
	}

	/**
	 * Retourne l'identifiant du canal
	 * @return String
	 */
	public String getId() {
		return id;
	}

	/**
	 * Retourne le nom du canal
	 * @return String
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Retourne le nombre maximal d'auditeurs support� par le canal
	 * @return int
	 */
	public int getUtilMax() {
		return utilMax;
	}

	/**
	 * Insertion d'informations
	 * @param id
	 * @param nom
	 * @param fluxMax
	 */
	public void insertionInfos(String id, String nom, int fluxMax) {
		// your code here
	}

	/**
	 * V�rifie si la tranche horaire est libre pour une planification
	 * @param jour
	 * @param heure
	 * @param idCanal
	 * @return
	 */
	public boolean verifierPlanification(Date Jour, int heure, String idCanal) {
		// your code here
		return false;
	}

	/**
	 * Bloquer (r�server) une plage horaire pour une planification
	 * @param idProgramme
	 * @param jour
	 * @param heure
	 */
	public void bloquerPlage(String IdeProgramme, Date jour, int heure) {
		// your code here
	}
	
	/**
	 * Diffuser un programme
	 * @param Programme
	 */
	public void diffuserProgramme(Programme p) {        
		//p.getTitre();
		//p.diffuser();
		System.out.println("Le programme " +  p.getTitre() + " est en cours de diffusion");
		
		Vector medias = new Vector();
		Enumeration cles = p.getDocuments().keys();
		
		for(int i = 0; i < p.getDocuments().size(); i++) {
			medias.addElement(((Document)p.getDocuments().get(cles.nextElement())).getFichier());
		}
		
		if (RTP!=null) {
			RTP.programmer(medias);
			RTP.diffuser();
		}
	} 
	

	/**
	 * Rend le canal actif
	 */
	public void setActif() {
		etat = "ACTIF";
	}

	/**
	 * Arr�te la diffusion du canal
	 * @param idCanal
	 */
	public void arreterDiffusionCanal(int idCanal) {
		// your code here
	}

	/**
	 * D�connecter un auditeur du canal
	 * @param idAuditeur
	 */
	public void deconnecterAuditeur(String idAuditeur) {
		if (auditeurs.contains(idAuditeur)) auditeurs.removeElement(idAuditeur);
	}
	
	/**
	 * Connecter un auditeur au canal
	 * @param idAuditeur
	 */
	public void connecterAuditeur(String idAuditeur) {
		if (!auditeurs.contains(idAuditeur)) auditeurs.addElement(idAuditeur);
	}

	/**
	 * Relance la diffusion d'un canal
	 * @param idCanal
	 */
	public void relanceDiffusionCanal(String idCanal) {
		// your code here
	}
	
	/**
	 * Met � jour l'attribut nom
	 * @param String nom
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setNom(String nom) /*throws SQLException*/ {
		String requete = "UPDATE canal SET nom = '" + nom + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.nom = nom;
		}
		return nbRows>0;
	}
	
	/**
	 * Met � jour l'attribut utilMax
	 * @param int utilmax
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean setUtilMax(int utilmax) /*throws SQLException*/ {
		String requete = "UPDATE canal SET utilmax = '" + utilmax + "' WHERE id = '" + id + "';";
		Jdbc base = Jdbc.getInstance();
		int nbRows = base.executeUpdate(requete);
		
		//Si la mise � jour s'est bien d�roul�e, on synchronise l'attibut de l'objet
		if (nbRows>0) {
			this.utilMax = utilmax;
		}
		return nbRows>0;
	}
}
