package proto.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.prefs.Preferences;

import org.ini4j.IniFile;

//Arguments : 192.168.0.2 10000


/**
 * Client du Net-JukeBox (classe principale)
 */
public class Client {
	
// ATTRIBUTS
//***********************************
	
	/**
	 * Client XMLRPC
	 */
	private XMLClient clientXML = null;
	
	/**
	 * Etat connect�
	 */
	private boolean etatConnecte = false;
	

// CONSTRUCTEUR
//***********************************
	
	/**
	 * Constructeur (main sans args)
	 */
	public Client() {
		
		// Demande d'infos pour le Client XML
		BufferedReader reader = null;
		try{
			reader= new BufferedReader(new InputStreamReader(System.in));
			System.out.println("###########################################");
			System.out.print("IP du serveur: ");
			String ip = reader.readLine();
			System.out.print("Port du serveur: ");
			String port = reader.readLine();
			
			// On initialise le client XML
			initializeXML(ip, port);
			
			// On d�marre le menu
			menu();
		} catch (Exception e){
			System.out.println(e);
			System.exit(1);
		}
	}
	
	/**
	 * Constructeur (main avec args)
	 * @param ip
	 * @param port
	 */
	public Client(String ip, String port) {
		
		//On initialise le client XML
		initializeXML(ip, port);
		
		//On d�marre le menu
		menu();
	}
	
// METHODES DYNAMIQUES
//***********************************
	
	/**
	 * Initialise le client XMLRPC et se connecte au serveur XMLRPC
	 * @param String ip
	 * @param String port
	 */
	private void initializeXML(String ip, String port) {
		
		//Si le client XML n'est pas d�j� initialis�
		if (clientXML == null) {
		
			//On initialise le client
			clientXML = new XMLClient(ip, port);
			
			try {
				//On essaye de se connecter au serveur XML
				if (clientXML.testConnectXML(InetAddress.getLocalHost().getHostAddress())) {
					System.err.println("INFO: Serveur XML contact� avec succ�s !");
				}
				else System.err.println("WARNING: Serveur XML injoignable !");
			} catch (Exception e) {
				System.err.println("ERREUR: "+e);
			}
			
		//Sinon, d�j� initialis�
		} else {
			System.err.println("WARNING: Client XML d�j� initialis� !");
		}
	}
	
	
// DIALOGUE AVEC L'UTILISATEUR (MENU)
//***********************************
	
	private void menu() {
		
		//Gestion des saisies clavier
		BufferedReader reader = null;
		String ligne;
		
		System.out.println();
		System.out.println("####################################################");
		System.out.println("       Bienvenue sur le client Net-JukeBox");
		System.out.println();
		System.out.println("Entrez 'help' pour lister les commandes disponibles");
		System.out.println("####################################################");
		
		try{
			reader= new BufferedReader(new InputStreamReader(System.in));
			do {
				System.out.println();
				System.out.print("> ");
				ligne = reader.readLine();
				
				// CONNEXION
				if  (ligne.equalsIgnoreCase("connexion")) {
					if (!etatConnecte) {
						System.out.print("Login: ");
						String login = lire();
						System.out.print("Pwd: ");
						String pwd = lire();
						etatConnecte = clientXML.connexion(login, pwd);
						if (etatConnecte) System.err.println("INFO: Utilisateur connect�");
						else System.err.println("ERREUR: Utilisateur non connect�");
					}
					else {
						System.err.print("WARNING: Vous �tes d�j� connect� au serveur !");
					}
				}
				
				// DECONNEXION
				if (ligne.equalsIgnoreCase("deconnexion")) {
					if (etatConnecte) {
						etatConnecte = !clientXML.deconnexion();
						if (!etatConnecte) System.err.println("INFO: Utilisateur d�connect�");
						else System.err.println("ERREUR: Utilisateur toujours connect�");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// CREERCANAL
				if  (ligne.equalsIgnoreCase("creerCanal")) {
					if (etatConnecte) {
						System.out.print("Nom du canal: ");
						String nom = lire();
						System.out.print("Nombre maximal d'auditeurs: ");
						String utilMax = lire();
						boolean cree = clientXML.creerCanal(nom, utilMax);
						if (cree) System.err.println("INFO: Canal cr��");
						else System.err.println("ERREUR: Canal non cr��");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// CREERPROGRAMME
				if  (ligne.equalsIgnoreCase("creerProgramme")) {
					if (etatConnecte) {
						System.out.print("Titre du programme: ");
						String titre = lire();
						System.out.print("Th�matique: ");
						String thematique = lire();
						boolean cree = clientXML.creerProgramme(titre, thematique);
						if (cree) System.err.println("INFO: Programme cr��");
						else System.err.println("ERREUR: Programme non cr��");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// CREERDOCUMENT
				if  (ligne.equalsIgnoreCase("creerDocument")) {
					if (etatConnecte) {
						System.out.print("Titre du document: ");
						String titre = lire();
						System.out.print("Dur�e: ");
						String duree = lire();
						System.out.print("Jour de parution: ");
						String jour = lire();
						System.out.print("Mois de parution: ");
						String mois = lire();
						System.out.print("Ann�e de parution: ");
						String annee = lire();
						System.out.print("Source: ");
						String source = lire();
						System.out.print("Langue: ");
						String langue = lire();
						System.out.print("Genre: ");
						String genre = lire();
						System.out.print("Fichier: ");
						String fichier = lire();
						boolean cree = clientXML.creerDocument(titre, duree, jour, mois, annee, source, langue, genre, fichier);
						if (cree) System.err.println("INFO: Document cr��");
						else System.err.println("ERREUR: Document non cr��");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// AJOUTERDOCUMENTPROGRAMME
				if  (ligne.equalsIgnoreCase("ajouterDocumentProgramme")) {
					if (etatConnecte) {
						System.out.print("ID du document source: ");
						String idDoc = lire();
						System.out.print("ID du programme cible: ");
						String idProg = lire();
						boolean ajoute = clientXML.ajouterDocumentProgramme(idDoc, idProg);
						if (ajoute) System.err.println("INFO: Document ajout� au programme");
						else System.err.println("ERREUR: Document non ajout� au progamme");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// DIFFUSERPROGRAMME
				if  (ligne.equalsIgnoreCase("diffuserProgramme")) {
					if (etatConnecte) {
						System.out.print("ID du programme source: ");
						String idProg = lire();
						System.out.print("ID du canal cible: ");
						String idCanal = lire();
						boolean diffuse = clientXML.diffuserProgramme(idProg, idCanal);
						if (diffuse) System.err.println("INFO: Diffusion du programme lanc�e sur le canal");
						else System.err.println("ERREUR: Diffusion du programme non lanc�e sur le canal");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// PLANIFIERPROGRAMME
				if  (ligne.equalsIgnoreCase("planifierProgramme")) {
					if (etatConnecte) {
						System.out.print("ID du programme source: ");
						String idProg = lire();
						System.out.print("ID du canal cible: ");
						String idCanal = lire();
						System.out.print("Jour de planification: ");
						String jour = lire();
						System.out.print("Mois: ");
						String mois = lire();
						System.out.print("Ann�e: ");
						String annee = lire();
						System.out.print("Heure: ");
						String heure = lire();
						System.out.print("Minute: ");
						String minute = lire();
						System.out.print("Seconde: ");
						String seconde = lire();
						boolean planifie = clientXML.planifierProgramme(idProg, idCanal, jour, mois, annee, heure, minute, seconde);
						if (planifie) System.err.println("INFO: Programme planifi� sur le canal");
						else System.err.println("ERREUR: Programme non planifi�");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// STARTCANAL
				if  (ligne.equalsIgnoreCase("startCanal")) {
					if (etatConnecte) {
						System.out.print("ID du canal source: ");
						String idCanal = lire();
						if (clientXML.startCanal(idCanal)) {
							System.err.println("INFO: Diffusion du canal lanc�e");
						}
						else System.err.println("ERREUR: Diffusion du canal non lanc�e");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// STOPCANAL
				if  (ligne.equalsIgnoreCase("stopCanal")) {
					if (etatConnecte) {
						System.out.print("ID du canal source: ");
						String idCanal = lire();
						if (clientXML.stopCanal(idCanal)) {
							System.err.println("INFO: Canal arr�t�");
						}
						else System.err.println("ERREUR: Canal non stopp�");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// STARTPLAYER
				if  (ligne.equalsIgnoreCase("startPlayer")) {
					if (etatConnecte) {
						System.out.print("ID du canal source: ");
						String idCanal = lire();
						String urlPlayer = clientXML.ecouterCanal(idCanal);
						if (urlPlayer != null) {
							//RTPClient player = RTPClient.getInstance();
							RTPClient2 player = RTPClient2.getInstance();
							player.start(urlPlayer);
							System.err.println("INFO: Ecoute du canal lanc�e");
						}
						else System.err.println("ERREUR: Ecoute du canal non lanc�e");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// STOPPLAYER
				if  (ligne.equalsIgnoreCase("stopPlayer")) {
					if (etatConnecte) {
						//RTPClient player = RTPClient.getInstance();
						RTPClient2 player = RTPClient2.getInstance();
						player.stop();
						System.err.println("INFO: Player arr�t�");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// LISTERDOCUMENT
				if  (ligne.equalsIgnoreCase("listerDocument")) {
					if (etatConnecte) {
						Vector vDocuments = clientXML.listerDocuments();
						if (vDocuments!=null && vDocuments.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary d;
							for (int i=0; i<vDocuments.size(); i++){
								d = (Dictionary)vDocuments.get(i);
								System.out.println("----------------- Document -------------------");
								System.out.println("Id: "+d.get("id"));
								System.out.println("Titre: "+d.get("titre"));
								System.out.println("Dur�e: "+d.get("duree"));
								System.out.println("Date: "+d.get("date"));
								System.out.println("Genre: "+d.get("genre"));
								System.out.println("Source: "+d.get("source"));
								System.out.println("Langue: "+d.get("langue"));
								System.out.println("Fichier: "+d.get("fichier"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Documents list�s");
						}
						else System.err.println("WARNING: Aucun document disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// LISTERPROGRAMME
				if  (ligne.equalsIgnoreCase("listerProgramme")) {
					if (etatConnecte) {
						Vector vProgrammes = clientXML.listerProgrammes();
						if (vProgrammes!=null && vProgrammes.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary p;
							for (int i=0; i<vProgrammes.size(); i++){
								p = (Dictionary)vProgrammes.get(i);
								System.out.println("----------------- Programme -------------------");
								System.out.println("Id: "+p.get("id"));
								System.out.println("Titre: "+p.get("titre"));
								System.out.println("Th�matique: "+p.get("thematique"));
								System.out.println("Dur�e: "+p.get("duree"));
								System.out.println("Nb docs: "+p.get("nbDocs"));
								System.out.println("-----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Programmes list�s");
						}
						else System.err.println("WARNING: Aucun programme disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// LISTERCANAL
				if  (ligne.equalsIgnoreCase("listerCanal")) {
					if (etatConnecte) {
						Vector vCanaux = clientXML.listerCanaux();
						if (vCanaux!=null && vCanaux.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary c;
							for (int i=0; i<vCanaux.size(); i++){
								c = (Dictionary)vCanaux.get(i);
								System.out.println("----------------- Canal -------------------");
								System.out.println("Id: "+c.get("id"));
								System.out.println("Nom: "+c.get("nom"));
								System.out.println("NbMax auditeurs: "+c.get("utilMax"));
								System.out.println("Nb progs: "+c.get("nbProgs"));
								System.out.println("-------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Programmes list�s");
						}
						else System.err.println("WARNING: Aucun canal disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// INSCRIPTION
				if  (ligne.equalsIgnoreCase("inscrireUtilisateur")) {
					if (etatConnecte) {
						System.out.print("prenom: ");
						String prenom = lire();
						System.out.print("nom: ");
						String nom = lire();
						System.out.print("login: ");
						String log = lire();
						System.out.print("password: ");
						String pass = lire();
						System.out.print("email: ");
						String email = lire();
						System.out.print("role: ");
						String role = lire();
						System.out.print("pays: ");
						String pays = lire();
						
						if (clientXML.inscription(log, pass, role, email, nom, prenom, pays)) {
							System.err.println("INFO: Utilisateur inscrit");
						}
						else System.err.println("WARNING: Inscription impossible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				//SUPPRIMERUTILISATEUR
				if (ligne.equalsIgnoreCase("supprimerUtilisateur")) {
					if (etatConnecte) {
						System.out.print("login: ");
						String log = lire();
						
						if (clientXML.supprimerUtilisateur(log)) {
							System.err.println("INFO: Utilisateur supprim�");
						}
						else System.err.println("WARNING: Suppression impossible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				
				// RECHERCHERDOCUMENT
				if  (ligne.equalsIgnoreCase("rechercherDocument")) {
					if (etatConnecte) {
						System.out.print("ID du document: ");
						String id = lire();
						System.out.print("Titre: ");
						String titre = lire();
						System.out.print("Dur�e: ");
						String duree = lire();
						System.out.print("Jour de parution: ");
						String jour = lire();
						System.out.print("Mois de parution: ");
						String mois = lire();
						System.out.print("Ann�e de parution: ");
						String annee = lire();
						System.out.print("Source: ");
						String source = lire();
						System.out.print("Langue: ");
						String langue = lire();
						System.out.print("Genre: ");
						String genre = lire();
						System.out.print("Fichier: ");
						String fichier = lire();
						Vector vDocuments = clientXML.rechercherDocument(id, titre, duree, jour, mois, annee, source, langue, genre, fichier);
						if (vDocuments!=null && vDocuments.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary d;
							for (int i=0; i<vDocuments.size(); i++){
								d = (Dictionary)vDocuments.get(i);
								System.out.println("----------------- Document -------------------");
								System.out.println("Id: "+d.get("id"));
								System.out.println("Titre: "+d.get("titre"));
								System.out.println("Dur�e: "+d.get("duree"));
								System.out.println("Date: "+d.get("date"));
								System.out.println("Genre: "+d.get("genre"));
								System.out.println("Source: "+d.get("source"));
								System.out.println("Langue: "+d.get("langue"));
								System.out.println("Fichier: "+d.get("fichier"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Document recherch�");
						}
						else System.err.println("WARNING: Aucun document disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// RECHERCHERPROGRAMME
				if  (ligne.equalsIgnoreCase("rechercherProgramme")) {
					if (etatConnecte) {
						System.out.print("ID du programme: ");
						String id = lire();
						System.out.print("Titre: ");
						String titre = lire();
						System.out.print("Th�matique: ");
						String thematique = lire();
						Vector vProgrammes = clientXML.rechercherProgramme(id, titre, thematique);
						if (vProgrammes!=null && vProgrammes.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary d;
							for (int i=0; i<vProgrammes.size(); i++){
								d = (Dictionary)vProgrammes.get(i);
								System.out.println("----------------- Programme -------------------");
								System.out.println("Id: "+d.get("id"));
								System.out.println("Titre: "+d.get("titre"));
								System.out.println("Th�matique: "+d.get("thematique"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Programme recherch�");
						}
						else System.err.println("WARNING: Aucun programme disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// RECHERCHERCANAL
				if  (ligne.equalsIgnoreCase("rechercherCanal")) {
					if (etatConnecte) {
						System.out.print("ID du canal: ");
						String id = lire();
						System.out.print("Nom: ");
						String nom = lire();
						System.out.print("NbMax d'auditeurs: ");
						String utilMax = lire();
						Vector vCanaux = clientXML.rechercherCanal(id, nom, utilMax);
						if (vCanaux!=null && vCanaux.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary d;
							for (int i=0; i<vCanaux.size(); i++){
								d = (Dictionary)vCanaux.get(i);
								System.out.println("----------------- Canal -------------------");
								System.out.println("Id: "+d.get("id"));
								System.out.println("Nom: "+d.get("nom"));
								System.out.println("NbMax d'auditeurs: "+d.get("utilMax"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Canal recherch�");
						}
						else System.err.println("WARNING: Aucun canal disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// SUPPRIMERDOCUMENT
				if  (ligne.equalsIgnoreCase("supprimerDocument")) {
					if (etatConnecte) {
						System.out.print("ID du document ");
						String id = lire();
						if (clientXML.supprimerDocument(id)) {
							System.err.println("INFO: Document supprim�");
						}
						else System.err.println("ERREUR: Suppression du document �chou�e");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// SUPPRIMERPROGRAMME
				if  (ligne.equalsIgnoreCase("supprimerProgramme")) {
					if (etatConnecte) {
						System.out.print("ID du progamme ");
						String id = lire();
						if (clientXML.supprimerProgramme(id)) {
							System.err.println("INFO: Programme supprim�");
						}
						else System.err.println("ERREUR: Suppression du programme �chou�e");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// SUPPRIMERCANAL
				if  (ligne.equalsIgnoreCase("supprimerCanal")) {
					if (etatConnecte) {
						System.out.print("ID du canal ");
						String id = lire();
						if (clientXML.supprimerCanal(id)) {
							System.err.println("INFO: Canal supprim�");
						}
						else System.err.println("ERREUR: Suppression du canal �chou�e");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// MODIFIERDOCUMENT
				if  (ligne.equalsIgnoreCase("modifierDocument")) {
					if (etatConnecte) {
						
						//ID du doc � modifier
						System.out.print("ID du document � modifier : ");
						String id = lire();
						
						//On affiche ses infos
						Vector vDocuments = clientXML.rechercherDocument(id, "", "", "", "", "", "", "", "", "");
						if (vDocuments!=null && vDocuments.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary d;
							for (int i=0; i<vDocuments.size(); i++){
								d = (Dictionary)vDocuments.get(i);
								System.out.println("----------------- Document -------------------");
								System.out.println("Id: "+d.get("id"));
								System.out.println("Titre: "+d.get("titre"));
								System.out.println("Dur�e: "+d.get("duree"));
								System.out.println("Date: "+d.get("date"));
								System.out.println("Genre: "+d.get("genre"));
								System.out.println("Source: "+d.get("source"));
								System.out.println("Langue: "+d.get("langue"));
								System.out.println("Fichier: "+d.get("fichier"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Document recherch�");
							
							//Si le doc existe, on le modifie
							System.out.print("Nouveau titre: ");
							String titre = lire();
							System.out.print("Dur�e: ");
							String duree = lire();
							System.out.print("Jour de parution: ");
							String jour = lire();
							System.out.print("Mois de parution: ");
							String mois = lire();
							System.out.print("Ann�e de parution: ");
							String annee = lire();
							System.out.print("Source: ");
							String source = lire();
							System.out.print("Langue: ");
							String langue = lire();
							System.out.print("Genre: ");
							String genre = lire();
							System.out.print("Fichier: ");
							String fichier = lire();
							boolean modifie = clientXML.modifierDocument(id, titre, duree, jour, mois, annee, source, langue, genre, fichier);
							if (modifie) System.err.println("INFO: Document modifi�");
							else System.err.println("ERREUR: Document non modifi�");
						}
						else System.err.println("WARNING: Aucun document disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// RECHERCHERUTILISATEUR
				if  (ligne.equalsIgnoreCase("rechercherUtilisateur")) {
					if (etatConnecte) {
						Vector vUtilisateur = clientXML.rechercherUtilisateur();
						if (vUtilisateur!=null && vUtilisateur.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary v;
							for (int i=0; i<vUtilisateur.size(); i++){
								System.out.println(vUtilisateur.get(i));
								/**
								v = (Dictionary)vUtilisateur.get(i);
								System.out.println("----------------- Utilisateur -------------------");
								System.out.println("Id: "+v.get("login"));
								System.out.println("Titre: "+v.get("nom"));
								System.out.println("Dur�e: "+v.get("prenom"));
								System.out.println("Date: "+v.get("mail"));
								System.out.println("Genre: "+v.get("pays"));
								System.out.println("----------------------------------------------");
								System.out.println();
								*/
							}
							System.err.println("INFO: Utilisateur recherch�");
						}
						else System.err.println("WARNING: Aucune donn�es disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				//MODIFIERUTILISATEUR
				if  (ligne.equalsIgnoreCase("modifierUtilisateur")) {
					if (etatConnecte) {
											
						//On affiche ses infos
						Vector vUtilisateur = clientXML.rechercherUtilisateur();
						if (vUtilisateur!=null && vUtilisateur.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary d;
							for (int i=0; i<vUtilisateur.size(); i++){
								
								System.out.println(vUtilisateur.get(i));
								
								/**
								d = (Dictionary)vUtilisateur.get(i);
								System.out.println("----------------- Utilisateur -------------------");
								System.out.println("Login: "+d.get("login"));
								System.out.println("Nom: "+d.get("nom"));
								System.out.println("Pr�nom: "+d.get("prenom"));
								System.out.println("Email: "+d.get("mail"));
								System.out.println("Pays: "+d.get("pays"));
								System.out.println("----------------------------------------------");
								System.out.println();*/
								
								
							}		
							//System.err.print("INFO: Attributs recherch�");
									
							System.out.print("Nouveau Login: ");
							String login = lire();
							System.out.print("Nouveau mot de passe: ");
							String pwd = lire();
							System.out.print("Nouveau Nom: ");
							String nom = lire();
							System.out.print("Nouveau Prenom: ");
							String prenom = lire();
							System.out.print("Nouveau Mail: ");
							String mail = lire();
							System.out.print("Nouveau Pays: ");
							String pays = lire();
							
							boolean modifie = clientXML.modifierUtilisateur(login, pwd, nom, prenom, mail, pays);
							if (modifie) System.err.println("INFO: Attributs modifi�s");
							else System.err.println("ERREUR: Attributs non modifi�s");
						}
						else System.err.println("WARNING: Aucun Attributs disponibles");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// MODIFIERPROGRAMME
				if  (ligne.equalsIgnoreCase("modifierProgramme")) {
					if (etatConnecte) {
						
						//ID du doc � modifier
						System.out.print("ID du programme � modifier : ");
						String id = lire();
						
						//On affiche ses infos
						Vector vProgrammes = clientXML.rechercherProgramme(id, "", "");
						if (vProgrammes!=null && vProgrammes.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary d;
							for (int i=0; i<vProgrammes.size(); i++){
								d = (Dictionary)vProgrammes.get(i);
								System.out.println("----------------- Programme -------------------");
								System.out.println("Id: "+d.get("id"));
								System.out.println("Titre: "+d.get("titre"));
								System.out.println("Th�matique: "+d.get("thematique"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Programme recherch�");
							
							//Si le prog existe, on le modifie
							System.out.print("Nouveau titre: ");
							String titre = lire();
							System.out.print("Th�matique: ");
							String thematique = lire();
							boolean modifie = clientXML.modifierProgramme(id, titre, thematique);
							if (modifie) System.err.println("INFO: Programme modifi�");
							else System.err.println("ERREUR: Programme non modifi�");
						}
						else System.err.println("WARNING: Aucun programme disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// MODIFIERCANAL
				if  (ligne.equalsIgnoreCase("modifierCanal")) {
					if (etatConnecte) {
						
						//ID du doc � modifier
						System.out.print("ID du canal � modifier : ");
						String id = lire();
						
						//On affiche ses infos
						Vector vCanaux = clientXML.rechercherCanal(id, "", "");
						if (vCanaux!=null && vCanaux.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary d;
							for (int i=0; i<vCanaux.size(); i++){
								d = (Dictionary)vCanaux.get(i);
								System.out.println("----------------- Canal -------------------");
								System.out.println("Id: "+d.get("id"));
								System.out.println("Nom: "+d.get("nom"));
								System.out.println("NbMax d'auditeurs: "+d.get("utilMax"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Canal recherch�");
							
							//Si le prog existe, on le modifie
							System.out.print("Nouveau nom: ");
							String nom = lire();
							System.out.print("NbMax d'auditeurs: ");
							String utilMax = lire();
							boolean modifie = clientXML.modifierCanal(id, nom, utilMax);
							if (modifie) System.err.println("INFO: Canal modifi�");
							else System.err.println("ERREUR: Canal non modifi�");
						}
						else System.err.println("WARNING: Aucun canal disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				//INFODOCUMENT
				if  (ligne.equalsIgnoreCase("infoDocument")) {
					if (etatConnecte) {
						//ID du doc
						System.out.print("ID du programme : ");
						String id = lire();
						
						Dictionary d = clientXML.infoDocument(id);
						
						if (d != null) {
							System.out.println("----------------- Document -------------------");
							System.out.println("Id: "+d.get("id"));
							System.out.println("Titre: "+d.get("titre"));
							System.out.println("Dur�e: "+d.get("duree"));
							System.out.println("Date: "+d.get("date"));
							System.out.println("Genre: "+d.get("genre"));
							System.out.println("Source: "+d.get("source"));
							System.out.println("Langue: "+d.get("langue"));
							System.out.println("Fichier: "+d.get("fichier"));
							System.out.println("----------------------------------------------");
							System.out.println();
							System.err.println("INFO: Document affich�");
						}
						else System.err.println("WARNING: Aucun document disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// INFOPROGRAMME
				if  (ligne.equalsIgnoreCase("infoProgramme")) {
					if (etatConnecte) {
						//ID du prog
						System.out.print("ID du programme : ");
						String id = lire();
						
						Dictionary p = clientXML.infoProgramme(id);
						
						if (p!=null) {
							System.out.println("----------------- Programme -------------------");
							System.out.println("Id: "+p.get("id"));
							System.out.println("Titre: "+p.get("titre"));
							System.out.println("Th�matique: "+p.get("thematique"));
							System.out.println("Dur�e: "+p.get("duree"));
							System.out.println("Nb docs: "+p.get("nbDocs"));
							System.out.println("------------ Documents programm�s ------------");
							
							Vector vDocs = (Vector)p.get("documents");
							Dictionary doc;
							for (int i=0; i<vDocs.size(); i++) {
								doc = (Dictionary)vDocs.get(i);
								System.out.println("IdDoc: "+doc.get("id"));
								System.out.println("Titre: "+doc.get("titre"));
								System.out.println("Dur�e: "+doc.get("duree"));
								System.out.println("Calage: "+doc.get("calage"));
								System.out.println();
							}
							System.out.println("-------------------------------------------");
							System.err.println("INFO: Programme affich�");
						}
						else System.err.println("WARNING: Aucun programme disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// INFOCANAL
				if  (ligne.equalsIgnoreCase("infoCanal")) {
					if (etatConnecte) {
						//ID du canal
						System.out.print("ID du canal : ");
						String id = lire();
						
						Dictionary c = clientXML.infoCanal(id);
						if (c!=null) {
							System.out.println("----------------- Canal -------------------");
							System.out.println("Id: "+c.get("id"));
							System.out.println("Nom: "+c.get("nom"));
							System.out.println("NbMax auditeurs: "+c.get("utilMax"));
							System.out.println("Nb progs: "+c.get("nbProgs"));
							System.out.println("----------- Programmes planifi�s ----------");
							
							Vector vProgs = (Vector)c.get("programmes");
							Dictionary prog;
							GregorianCalendar horaire = new GregorianCalendar();
							String dateHoraire;
							for (int i=0; i<vProgs.size(); i++) {
								prog = (Dictionary)vProgs.get(i);
								horaire.setTimeInMillis(Long.parseLong((String)prog.get("calage")));
								dateHoraire = horaire.get(GregorianCalendar.DATE) + "/" +
											  horaire.get(GregorianCalendar.MONTH) + "/" +
											  horaire.get(GregorianCalendar.YEAR) + " - " +
											  horaire.get(GregorianCalendar.HOUR_OF_DAY) + ":" +
											  horaire.get(GregorianCalendar.MINUTE) + ":" +
											  horaire.get(GregorianCalendar.SECOND);
								
								System.out.println("IdProg: "+prog.get("id"));
								System.out.println("Titre: "+prog.get("titre"));
								System.out.println("Dur�e: "+prog.get("duree"));
								System.out.println("Horaire: "+dateHoraire);
								System.out.println("Calage: "+prog.get("calage"));
								System.out.println();
							}
							System.out.println("-------------------------------------------");
							System.err.println("INFO: Canal affich�");
						}
						else System.err.println("WARNING: Aucun canal disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// HELP
				if (ligne.equalsIgnoreCase("help")) {
					System.out.println("Commandes disponibles:");
					System.out.println(" connexion : ouvrir une session sur le serveur");
					System.out.println(" deconnexion : fermer la session");
					System.out.println(" creerCanal : cr�er un canal");
					System.out.println(" creerProgramme : cr�er un programme");
					System.out.println(" creerDocument : cr�er un document");
					System.out.println(" ajouterDocumentProgramme : ajouter un document � un programme");
					System.out.println(" diffuserProgramme : diffuser un programme sur un canal");
					System.out.println(" startCanal : lancer la diffusion d'un canal");
					System.out.println(" stopCanal : stopper la diffusion d'un canal");
					System.out.println(" startPlayer : lancer l'�coute un canal");
					System.out.println(" stopPlayer : stopper l'�coute d'un canal");
					System.out.println(" listerDocument : lister les documents disponibles");
					System.out.println(" listerProgramme : lister les programmes disponibles");
					System.out.println(" listerCanal : lister les canaux disponibles");
					System.out.println(" inscrireUtilisateur : inscription d'un nouvel utilisateur");
					System.out.println(" supprimerUtilisateur : suppression d'un utilisateur");
					System.out.println(" rechercherUtilisateur : rechercher vos attributs");
					System.out.println(" modifierUtilisateur : modifier vos attributs");
					System.out.println(" rechercherDocument : rechercher un document");
					System.out.println(" rechercherProgramme : rechercher un programme");
					System.out.println(" rechercherCanal : rechercher un canal");
					System.out.println(" supprimerDocument : supprimer un document");
					System.out.println(" supprimerProgramme : supprimer un programme");
					System.out.println(" supprimerCanal : supprimer un canal");
					System.out.println(" modifierDocument : modifier un document");
					System.out.println(" modifierProgramme : modifier un programme");
					System.out.println(" modifierCanal : modifier un canal");
					System.out.println(" infoDocument : afficher les informations sur un document");
					System.out.println(" infoProgramme : afficher les informations sur un programme");
					System.out.println(" infoCanal : afficher les informations sur un canal");
					System.out.println(" end : terminer");
					System.out.println(" help : lister les commandes disponibles");
				}
				
				// END
				if (ligne.equalsIgnoreCase("end")) {
					if (etatConnecte) clientXML.deconnexion();
					System.out.println();
					System.out.println("###########################################");
					System.out.println("Fermeture du client");
					System.out.println("###########################################");
					System.exit(1);
				}
				
			} while (!ligne.equalsIgnoreCase("end"));
			
		} catch (Exception e){
			System.err.println("ERREUR:" + e);
		}
	}
	
	/**
	 * Lecture d'une ligne en console
	 * @return
	 */
	private String lire() {

		BufferedReader reader = null;
		try{
			reader= new BufferedReader(new InputStreamReader(System.in));
			return reader.readLine();
		} catch (Exception e){
			System.out.println(e);
			return null;
		}
	}
	
// MAIN
//***********************************
	public static void main(String args[]) throws Exception {
		
		//USAGE : java Client [filename.ini]
		
		//Fichier d'initialisation par d�faut (si pas de param�tres)
		String filename = args.length > 0 ? args[0] : "src/proto/client/client.ini";
		//String filename = args.length > 0 ? args[0] : "C:/Documents and Settings/Marie Rubini/Mes documents/workspace/NetJukeBox/proto/client/client.ini ";
		Preferences prefs = new IniFile(new File(filename));
		
		//On d�marre le client
		new Client(prefs.node("serveur").get("ip", null), prefs.node("serveur").get("port", null));
		
	}
}