package proto.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.prefs.Preferences;
import javax.naming.directory.Attribute;
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
	
	/**
	 * Login
	 */
	private String login=null;

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
			clientXML = XMLClient.getInstance(ip, port);
			
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

//### CONNEXION ###				
				
				// CONNEXION
				if  (ligne.equalsIgnoreCase("connexion")) {
					if (!etatConnecte) {
						System.out.print("Login: ");
						String login = lire();
						System.out.print("Pwd: ");
						String pwd = lire();
						etatConnecte = clientXML.connexion(login, pwd);
						if (etatConnecte) {
							this.login = login;
							System.err.println("INFO: Utilisateur connect�");
						}
						else System.err.println("ERREUR: Utilisateur non connect�");
					}
					else {
						System.err.print("WARNING: Vous �tes d�j� connect� au serveur !");
					}
				}
				
				// DECONNEXION
				if (ligne.equalsIgnoreCase("deconnexion")) {
					if (etatConnecte) {
						etatConnecte = !clientXML.deconnexion(login);
						if (!etatConnecte) System.err.println("INFO: Utilisateur d�connect�");
						else System.err.println("ERREUR: Utilisateur toujours connect�");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
			
//### DOCUMENT ###	
				
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
						System.out.print("Artiste: ");
						String artiste = lire();
						System.out.print("Interpr�te: ");
						String interprete = lire();
						System.out.print("Compositeur: ");
						String compositeur = lire();
						boolean cree = clientXML.creerDocument(login, titre, duree, jour, mois, annee, source, langue, genre, fichier, artiste, interprete, compositeur);
						if (cree) System.err.println("INFO: Document cr��");
						else System.err.println("ERREUR: Document non cr��");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// LISTERDOCUMENT
				if  (ligne.equalsIgnoreCase("listerDocument")) {
					if (etatConnecte) {
						Vector vDocuments = clientXML.listerDocuments(login);
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
								System.out.println("Artiste: "+d.get("artiste"));
								System.out.println("Interpr�te: "+d.get("interprete"));
								System.out.println("Compositeur: "+d.get("compositeur"));
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
						System.out.print("Artiste: ");
						String artiste = lire();
						System.out.print("Interpr�te: ");
						String interprete = lire();
						System.out.print("Compositeur: ");
						String compositeur = lire();
						Vector vDocuments = clientXML.rechercherDocument(login, id, titre, duree, jour, mois, annee, source, langue, genre, fichier, artiste, interprete, compositeur);
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
								System.out.println("Artiste: "+d.get("artiste"));
								System.out.println("Interpr�te: "+d.get("interprete"));
								System.out.println("Compositeur: "+d.get("compositeur"));
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
				
				// SUPPRIMERDOCUMENT
				if  (ligne.equalsIgnoreCase("supprimerDocument")) {
					if (etatConnecte) {
						System.out.print("ID du document ");
						String id = lire();
						if (clientXML.supprimerDocument(login, id)) {
							System.err.println("INFO: Document supprim�");
						}
						else System.err.println("ERREUR: Suppression du document �chou�e");
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
						Vector vDocuments = clientXML.rechercherDocument(login, id, "", "", "", "", "", "", "", "", "", "", "", "");
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
								System.out.println("Artiste: "+d.get("artiste"));
								System.out.println("Interpr�te: "+d.get("interprete"));
								System.out.println("Compositeur: "+d.get("compositeur"));
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
							System.out.print("Artiste: ");
							String artiste = lire();
							System.out.print("Interpr�te: ");
							String interprete = lire();
							System.out.print("Compositeur: ");
							String compositeur = lire();
							boolean modifie = clientXML.modifierDocument(login, id, titre, duree, jour, mois, annee, source, langue, genre, fichier, artiste, interprete, compositeur);
							if (modifie) System.err.println("INFO: Document modifi�");
							else System.err.println("ERREUR: Document non modifi�");
						}
						else System.err.println("WARNING: Aucun document disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// INFODOCUMENT
				if  (ligne.equalsIgnoreCase("infoDocument")) {
					if (etatConnecte) {
						//ID du doc
						System.out.print("ID du document : ");
						String id = lire();
						
						Dictionary d = clientXML.infoDocument(login, id);
						
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
							System.out.println("Artiste: "+d.get("artiste"));
							System.out.println("Interpr�te: "+d.get("interprete"));
							System.out.println("Compositeur: "+d.get("compositeur"));
							System.out.println("------------ Programmes associ�s --------------");
							
							Vector vProgs = (Vector)d.get("programmes");
							Dictionary prog;
							for (int i=0; i<vProgs.size(); i++) {
								prog = (Dictionary)vProgs.get(i);
								System.out.println("Prog: "+prog);
							}
							System.out.println("------------ Contrats associ�s --------------");
							
							Vector vConts = (Vector)d.get("contrats");
							Dictionary cont;
							for (int i=0; i<vConts.size(); i++) {
								cont = (Dictionary)vConts.get(i);
								System.out.println("Contrat: "+cont);
							}
							System.out.println("---------------- Diffusions -----------------");
							
							Vector vDiffs = (Vector)d.get("audimats");
							Dictionary audimat;
							for (int i=0; i<vDiffs.size(); i++) {
								audimat = (Dictionary)vDiffs.get(i);
								System.out.println("Diffusion: "+audimat);
							}
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
				
//### PROGRAMME ###				
				
				// CREERPROGRAMME
				if  (ligne.equalsIgnoreCase("creerProgramme")) {
					if (etatConnecte) {
						System.out.print("Titre du programme: ");
						String titre = lire();
						System.out.print("Th�matique: ");
						String thematique = lire();
						boolean cree = clientXML.creerProgramme(login, titre, thematique);
						if (cree) System.err.println("INFO: Programme cr��");
						else System.err.println("ERREUR: Programme non cr��");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// LISTERPROGRAMME
				if  (ligne.equalsIgnoreCase("listerProgramme")) {
					if (etatConnecte) {
						Vector vProgrammes = clientXML.listerProgrammes(login);
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
				
				// RECHERCHERPROGRAMME
				if  (ligne.equalsIgnoreCase("rechercherProgramme")) {
					if (etatConnecte) {
						System.out.print("ID du programme: ");
						String id = lire();
						System.out.print("Titre: ");
						String titre = lire();
						System.out.print("Th�matique: ");
						String thematique = lire();
						Vector vProgrammes = clientXML.rechercherProgramme(login, id, titre, thematique);
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
				
				// SUPPRIMERPROGRAMME
				if  (ligne.equalsIgnoreCase("supprimerProgramme")) {
					if (etatConnecte) {
						System.out.print("ID du progamme ");
						String id = lire();
						if (clientXML.supprimerProgramme(login, id)) {
							System.err.println("INFO: Programme supprim�");
						}
						else System.err.println("ERREUR: Suppression du programme �chou�e");
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
						Vector vProgrammes = clientXML.rechercherProgramme(login, id, "", "");
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
							boolean modifie = clientXML.modifierProgramme(login, id, titre, thematique);
							if (modifie) System.err.println("INFO: Programme modifi�");
							else System.err.println("ERREUR: Programme non modifi�");
						}
						else System.err.println("WARNING: Aucun programme disponible");
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
						
						Dictionary p = clientXML.infoProgramme(login, id);
						
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
								System.out.println("Doc: "+doc);
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
				
				// AJOUTERDOCUMENTPROGRAMME
				if  (ligne.equalsIgnoreCase("ajouterDocumentProgramme")) {
					if (etatConnecte) {
						System.out.print("ID du document source: ");
						String idDoc = lire();
						System.out.print("ID du programme cible: ");
						String idProg = lire();
						boolean ajoute = clientXML.ajouterDocumentProgramme(login, idDoc, idProg);
						if (ajoute) System.err.println("INFO: Document ajout� au programme");
						else System.err.println("ERREUR: Document non ajout� au progamme");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// RETIRERDOCUMENTPROGRAMME
				if  (ligne.equalsIgnoreCase("retirerDocumentProgramme")) {
					if (etatConnecte) {
						System.out.print("ID du programme source: ");
						String idProg = lire();
						System.out.print("Document cal� au temps : ");
						String calage = lire();
						boolean retire = clientXML.retirerDocumentProgramme(login, idProg, calage);
						if (retire) System.err.println("INFO: Document retir� du programme");
						else System.err.println("ERREUR: Document non retir� du progamme");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
//### CANAL ###				
				
				// CREERCANAL
				if  (ligne.equalsIgnoreCase("creerCanal")) {
					if (etatConnecte) {
						System.out.print("Nom du canal: ");
						String nom = lire();
						System.out.print("Nombre maximal d'auditeurs: ");
						String utilMax = lire();
						boolean cree = clientXML.creerCanal(login, nom, utilMax);
						if (cree) System.err.println("INFO: Canal cr��");
						else System.err.println("ERREUR: Canal non cr��");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// LISTERCANAL
				if  (ligne.equalsIgnoreCase("listerCanal")) {
					if (etatConnecte) {
						Vector vCanaux = clientXML.listerCanaux(login);
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
				
				// RECHERCHERCANAL
				if  (ligne.equalsIgnoreCase("rechercherCanal")) {
					if (etatConnecte) {
						System.out.print("ID du canal: ");
						String id = lire();
						System.out.print("Nom: ");
						String nom = lire();
						System.out.print("NbMax d'auditeurs: ");
						String utilMax = lire();
						Vector vCanaux = clientXML.rechercherCanal(login, id, nom, utilMax);
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
				
				// SUPPRIMERCANAL
				if  (ligne.equalsIgnoreCase("supprimerCanal")) {
					if (etatConnecte) {
						System.out.print("ID du canal ");
						String id = lire();
						if (clientXML.supprimerCanal(login, id)) {
							System.err.println("INFO: Canal supprim�");
						}
						else System.err.println("ERREUR: Suppression du canal �chou�e");
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
						Vector vCanaux = clientXML.rechercherCanal(login, id, "", "");
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
							boolean modifie = clientXML.modifierCanal(login, id, nom, utilMax);
							if (modifie) System.err.println("INFO: Canal modifi�");
							else System.err.println("ERREUR: Canal non modifi�");
						}
						else System.err.println("WARNING: Aucun canal disponible");
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
						
						Dictionary c = clientXML.infoCanal(login, id);
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
								
								System.out.println("Prog: {id="+prog.get("id")+", titre="+prog.get("titre")+", duree="+prog.get("duree")+", horaire="+dateHoraire+", calage="+prog.get("calage")+"}");
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
				
				// STARTCANAL
				if  (ligne.equalsIgnoreCase("startCanal")) {
					if (etatConnecte) {
						System.out.print("ID du canal source: ");
						String idCanal = lire();
						if (clientXML.startCanal(login, idCanal)) {
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
						if (clientXML.stopCanal(login, idCanal)) {
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
						String urlPlayer = clientXML.ecouterCanal(login, idCanal);
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
				
				// DIFFUSERPROGRAMME
				if  (ligne.equalsIgnoreCase("diffuserProgramme")) {
					if (etatConnecte) {
						System.out.print("ID du programme source: ");
						String idProg = lire();
						System.out.print("ID du canal cible: ");
						String idCanal = lire();
						boolean diffuse = clientXML.diffuserProgramme(login, idProg, idCanal);
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
						boolean planifie = clientXML.planifierProgramme(login, idProg, idCanal, jour, mois, annee, heure, minute, seconde);
						if (planifie) System.err.println("INFO: Programme planifi� sur le canal");
						else System.err.println("ERREUR: Programme non planifi�");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// DEPLANIFIERPROGRAMME
				if  (ligne.equalsIgnoreCase("deplanifierProgramme")) {
					if (etatConnecte) {
						System.out.print("ID du canal: ");
						String idCanal = lire();
						System.out.print("Programme cal� au temps: ");
						String calage = lire();
						boolean planifie = clientXML.deplanifierProgramme(login, idCanal, calage);
						if (planifie) System.err.println("INFO: Programme d�planifi� sur le canal");
						else System.err.println("ERREUR: Programme non d�planifi�");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
	
//### UTILISATEUR ###				
				
				// INSCRIPTION
				if  (ligne.equalsIgnoreCase("inscrireUtilisateur")) {
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
					System.out.print("pays: ");
					String pays = lire();

					if (!etatConnecte) {
						login = "anonymous";
						String pwd = "anonymous";
						clientXML.connexion(login, pwd);
					}

					if (clientXML.inscription(login, log, pass, email, nom, prenom, pays)) {
						System.err.println("INFO: Utilisateur inscrit");
						
						if (login.equalsIgnoreCase("anonymous")) {
							clientXML.deconnexion(login);
						}
						
					} else {
						System.err.println("WARNING: Inscription impossible");
					}
				}
				
				//RECHERCHERMOTDEPASSE
				if  (ligne.equalsIgnoreCase("rechercherPassword")) {
					System.out.print("login: ");
					String log = lire();
				
					login = "anonymous";
					String pwd = "anonymous";
					clientXML.connexion(login, pwd);
					
					if (clientXML.recherchepwd(login, log)) {
						System.err.println("INFO: Mot de passe envoy�");
						
						clientXML.deconnexion(login);
						
					} else {
						System.err.println("WARNING: Erreur");
					}
				}
				
				//SUPPRIMERUTILISATEUR
				if (ligne.equalsIgnoreCase("supprimerUtilisateur")) {
					if (etatConnecte) {
						System.out.print("login: ");
						String log = lire();
						
						if (clientXML.supprimerUtilisateur(login, log)) {
							System.err.println("INFO: Utilisateur supprim�");
						}
						else System.err.println("WARNING: Suppression impossible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// RECHERCHERUTILISATEUR
				if  (ligne.equalsIgnoreCase("rechercherUtilisateur")) {
					if (etatConnecte) {
						System.out.print("login: ");
						String log = lire();
						Vector vUtilisateur = clientXML.rechercherUtilisateur(login, log);
						if (vUtilisateur!=null && vUtilisateur.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary v;
								
								v = (Dictionary)vUtilisateur.get(1);
								System.out.println("----------------- Utilisateur -------------------");
								System.out.println("Login: "+v.get("login"));
								System.out.println("Mot de passe: "+v.get("pwd"));
								System.out.println("Nom: "+v.get("nom"));
								System.out.println("Prenom: "+v.get("prenom"));
								System.out.println("Mail: "+v.get("mail"));
								System.out.println("Pays: "+v.get("pays"));
								System.out.println("Role: "+v.get("role"));
								System.out.println("----------------------------------------------");
								System.out.println();
								
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
						System.out.print("login: ");
						String logUtil = lire();					
						//On affiche ses infos
						Vector vUtilisateur = clientXML.rechercherUtilisateur(login, logUtil);
						if (vUtilisateur!=null && vUtilisateur.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary d;
							//for (int i=0; i<vUtilisateur.size(); i++){
								
								System.out.println(vUtilisateur.get(1));
								
								
								d = (Dictionary)vUtilisateur.get(1);
								System.out.println("----------------- Utilisateur -------------------");
								System.out.println("Login: "+d.get("login"));
								System.out.println("Pwd: "+d.get("pwd"));
								System.out.println("Nom: "+d.get("nom"));
								System.out.println("Pr�nom: "+d.get("prenom"));
								System.out.println("Email: "+d.get("mail"));
								System.out.println("Pays: "+d.get("pays"));
								System.out.println("----------------------------------------------");
								System.out.println();
								
								
							//}		
							//System.err.print("INFO: Attributs recherch�");
									
							System.out.print("Nouveau Login: ");
							String newLogUtil = lire();
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
							
							boolean modifie = clientXML.modifierUtilisateur(login, logUtil, newLogUtil, pwd, nom, prenom, mail, pays);
							if (modifie) System.err.println("INFO: Attributs modifi�s");
							else System.err.println("ERREUR: Attributs non modifi�s");
						}
						else System.err.println("WARNING: Aucun Attributs disponibles");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				//LISTERUTILISATEUR
				if  (ligne.equalsIgnoreCase("listerUtilisateur")) {
					if (etatConnecte) {
						//On affiche ses infos
						Vector vUtilisateurs = clientXML.listerUtilisateur(login);
						
						if (vUtilisateurs!=null && vUtilisateurs.size()>0) {
							//Parcours du vecteur, affichage des infos
							
							for(int i=0; i < vUtilisateurs.size(); i++)
						            if(vUtilisateurs.elementAt(i) != null){
						            	Dictionary dico = (Dictionary)vUtilisateurs.elementAt(i);
						            							            	
						            	String uid  = (String) dico.get("uid");
						    			//String pwd =  = (String) dico.get("pwd");
						            	String sn = (String) dico.get("sn");
						            	String givenName = (String) dico.get("givenName");
						    			String mail = (String) dico.get("mail");
						    			String st = (String) dico.get("st");
						    			String ou = (String) dico.get("ou");
						    							    	
						    			System.out.println("----------------- Utilisateur -------------------");
										System.out.println("Identifiant: "+uid);
										//System.out.println("Mot de passe: "+pwd);
										System.out.println("Nom: "+sn);
										System.out.println("Pr�nom: "+givenName);
										System.out.println("Mail: "+mail);
										System.out.println("Pays: "+st);
										System.out.println("R�le: "+ou);
										System.out.println("----------------------------------------------");
										System.out.println();
							}
							System.err.println("INFO: Utilisateurs list�s");
						} else System.err.println("WARNING: Aucun utilisateur disponible");
						
					} else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				//INFOS UTILISATEUR
				if  (ligne.equalsIgnoreCase("infoUtilisateur")) {
					if (etatConnecte) {
						System.out.print("login: ");
						String idlogin = lire();
						
						//On affiche ses infos
						Vector vUtilisateur = clientXML.infoUtilisateur(login, idlogin);
							
							if (vUtilisateur!=null && vUtilisateur.size()>0) {
								//Parcours du vecteur, affichage des infos
								Dictionary v;
									
									v = (Dictionary)vUtilisateur.get(1);
									System.out.println("----------------- Utilisateur -------------------");
									System.out.println("Login: "+v.get("login"));
									System.out.println("Mot de passe: "+v.get("pwd"));
									System.out.println("Nom: "+v.get("nom"));
									System.out.println("Prenom: "+v.get("prenom"));
									System.out.println("Mail: "+v.get("mail"));
									System.out.println("Pays: "+v.get("pays"));
									System.out.println("Role: "+v.get("role"));
									System.out.println("----------------------------------------------");
									System.out.println();
									
								System.out.println("INFO: Infos Permissions de l'Utilisateur :");
								System.out.println();
								
								//R�cup�ration des permissions exceptionnelles de l'utilisateur
								Vector v1 = (Vector) v.get("permutil");
								for(int i = 0; i < v1.size(); i++){
									Dictionary d = (Dictionary) v1.get(i);
									System.out.println(d.get("id") + " : " + d.get("libelle"));
								}
								System.out.println();
								System.out.println("INFO: Permissions exptionnelles de l'Utilisateur");
								System.out.println("----------------------------------------------");
								System.out.println();
								
								//R�cup�ration des permissions de l'utilisateur li�es � son r�le 
								Vector v2 = (Vector) v.get("permrole");
								for(int i = 0; i < v2.size(); i++){
									Dictionary d = (Dictionary) v2.get(i);
									System.out.println(d.get("id") + " : " + d.get("libelle"));
								}
								
								System.out.println();
								System.out.println("INFO: Permissions de l'Utilisateur li�es au role");
								System.out.println("----------------------------------------------");
								
							} else System.err.println("WARNING: Aucune donn�es disponible");
						} else {
							System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
						}
				}
				
				// AJOUTERPERMISSIONUTILISATEUR
				if  (ligne.equalsIgnoreCase("ajouterPermissionUtilisateur")) {
					if (etatConnecte) {
						System.out.print("ID de la permission source: ");
						String idPerm = lire();
						System.out.print("ID de l'utilisateur cible: ");
						String idUtilisateur = lire();
						boolean ajoute = clientXML.ajouterPermissionUtilisateur(login, idPerm, idUtilisateur);
						if (ajoute) System.err.println("INFO: Permission ajout�e � l'utilisateur");
						else System.err.println("ERREUR: Permission non ajout�e � l'utilisateur");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// RETIRERPERMISSIONUTILISATEUR
				if  (ligne.equalsIgnoreCase("retirerPermissionUtilisateur")) {
					if (etatConnecte) {
						System.out.print("ID de l'utilisateur source: ");
						String idUtilisateur = lire();
						System.out.print("ID de la permission � retirer : ");
						String idPerm = lire();
						boolean retire = clientXML.retirerPermissionUtilisateur(login, idPerm, idUtilisateur);
						if (retire) System.err.println("INFO: Permission retir�e � l'utilisateur");
						else System.err.println("ERREUR: Permission non retir�e � l'utilisateur");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				//CHANGERROLEUTILISATEUR
				if  (ligne.equalsIgnoreCase("changerRoleUtilisateur")) {
					if (etatConnecte) {
						System.out.print("ID de l'utilisateur source: ");
						String idUtilisateur = lire();
						System.out.print("ID du nouveau role : ");
						String idRole = lire();
						boolean change = clientXML.changerRoleUtilisateur(login, idRole, idUtilisateur);
						if (change) System.err.println("INFO: Role de l'utilisateur chang�");
						else System.err.println("ERREUR: Role de l'utilisateur non chang�");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				
//### CONTRAT ###
				
				// CREERCONTRAT
				if  (ligne.equalsIgnoreCase("creerContrat")) {
					if (etatConnecte) {
						System.out.print("Titre: ");
						String titre = lire();
						System.out.print("Jour (Signature): ");
						String jourSignature = lire();
						System.out.print("Mois (Signature): ");
						String moisSignature = lire();
						System.out.print("Ann�e (Signature): ");
						String anneeSignature = lire();
						System.out.print("Jour (Expiration): ");
						String jourExpiration = lire();
						System.out.print("Mois (Expiration): ");
						String moisExpiration = lire();
						System.out.print("Ann�e (Expiration): ");
						String anneeExpiration = lire();
						System.out.print("ID Contractant: ");
						String idContractant = lire();
						System.out.print("Mode de r�glement: ");
						String modeReglement = lire();
						System.out.print("Type: ");
						String type = lire();
						boolean cree = clientXML.creerContrat(login, titre, jourSignature, moisSignature, anneeSignature, jourExpiration, moisExpiration, anneeExpiration, idContractant, modeReglement, type);
						if (cree) System.err.println("INFO: Contrat cr��");
						else System.err.println("ERREUR: Contrat non cr��");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// LISTERCONTRATS
				if  (ligne.equalsIgnoreCase("listerContrat")) {
					if (etatConnecte) {
						Vector vContrats = clientXML.listerContrats(login);
						if (vContrats!=null && vContrats.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary c;
							for (int i=0; i<vContrats.size(); i++){
								c = (Dictionary)vContrats.get(i);
								System.out.println("----------------- Contrat -------------------");
								System.out.println("Id: "+c.get("id"));
								System.out.println("Titre: "+c.get("titre"));
								System.out.println("Signature: "+c.get("dateSignature"));
								System.out.println("Expiration: "+c.get("dateExpiration"));
								System.out.println("Contractant: "+c.get("contractant"));
								System.out.println("R�glement: "+c.get("modeReglement"));
								System.out.println("Type: "+c.get("type"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Contrats list�s");
						}
						else System.err.println("WARNING: Aucun contrat disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// RECHERCHERCONTRAT
				if  (ligne.equalsIgnoreCase("rechercherContrat")) {
					if (etatConnecte) {
						System.out.print("ID du contrat: ");
						String id = lire();
						System.out.print("Titre: ");
						String titre = lire();
						System.out.print("Jour (Signature): ");
						String jourSignature = lire();
						System.out.print("Mois (Signature): ");
						String moisSignature = lire();
						System.out.print("Ann�e (Signature): ");
						String anneeSignature = lire();
						System.out.print("Jour (Expiration): ");
						String jourExpiration = lire();
						System.out.print("Mois (Expiration): ");
						String moisExpiration = lire();
						System.out.print("Ann�e (Expiration): ");
						String anneeExpiration = lire();
						System.out.print("ID Contractant: ");
						String idContractant = lire();
						System.out.print("Mode de r�glement: ");
						String modeReglement = lire();
						System.out.print("Type: ");
						String type = lire();
						Vector vContrats = clientXML.rechercherContrat(login, id, titre, jourSignature, moisSignature, anneeSignature,
								jourExpiration, moisExpiration, anneeExpiration, idContractant, modeReglement, type);
						
						if (vContrats!=null && vContrats.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary c;
							for (int i=0; i<vContrats.size(); i++){
								c = (Dictionary)vContrats.get(i);
								System.out.println("----------------- Contrat -------------------");
								System.out.println("Id: "+c.get("id"));
								System.out.println("Titre: "+c.get("titre"));
								System.out.println("Signature: "+c.get("dateSignature"));
								System.out.println("Expiration: "+c.get("dateExpiration"));
								System.out.println("Contractant: "+c.get("contractant"));
								System.out.println("R�glement: "+c.get("modeReglement"));
								System.out.println("Type: "+c.get("type"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Contrat recherch�");
						}
						else System.err.println("WARNING: Aucun contrat disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// SUPPRIMERCONTRAT
				if  (ligne.equalsIgnoreCase("supprimerContrat")) {
					if (etatConnecte) {
						System.out.print("ID du contrat ");
						String id = lire();
						if (clientXML.supprimerContrat(login, id)) {
							System.err.println("INFO: Contrat supprim�");
						}
						else System.err.println("ERREUR: Suppression du contrat �chou�e");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// MODIFIERCONTRAT
				if  (ligne.equalsIgnoreCase("modifierContrat")) {
					if (etatConnecte) {
						
						//ID du doc � modifier
						System.out.print("ID du contrat � modifier : ");
						String id = lire();
						
						//On affiche ses infos
						Vector vContrats = clientXML.rechercherContrat(login, id, "", "", "", "", "", "", "", "", "", "");
						if (vContrats!=null && vContrats.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary c;
							for (int i=0; i<vContrats.size(); i++){
								c = (Dictionary)vContrats.get(i);
								System.out.println("----------------- Contrat -------------------");
								System.out.println("Id: "+c.get("id"));
								System.out.println("Titre: "+c.get("titre"));
								System.out.println("Signature: "+c.get("dateSignature"));
								System.out.println("Expiration: "+c.get("dateExpiration"));
								System.out.println("Contractant: "+c.get("contractant"));
								System.out.println("R�glement: "+c.get("modeReglement"));
								System.out.println("Type: "+c.get("type"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Contrat recherch�");
							
							//Si le doc existe, on le modifie
							System.out.print("Nouveau titre: ");
							String titre = lire();
							System.out.print("Jour (Signature): ");
							String jourSignature = lire();
							System.out.print("Mois (Signature): ");
							String moisSignature = lire();
							System.out.print("Ann�e (Signature): ");
							String anneeSignature = lire();
							System.out.print("Jour (Expiration): ");
							String jourExpiration = lire();
							System.out.print("Mois (Expiration): ");
							String moisExpiration = lire();
							System.out.print("Ann�e (Expiration): ");
							String anneeExpiration = lire();
							System.out.print("ID Contractant: ");
							String idContractant = lire();
							System.out.print("Mode de r�glement: ");
							String modeReglement = lire();
							System.out.print("Type: ");
							String type = lire();
							boolean modifie = clientXML.modifierContrat(login, id, titre, jourSignature, moisSignature, anneeSignature,
									jourExpiration, moisExpiration, anneeExpiration, idContractant, modeReglement, type);
							if (modifie) System.err.println("INFO: Contrat modifi�");
							else System.err.println("ERREUR: Contrat non modifi�");
						}
						else System.err.println("WARNING: Aucun contrat disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// INFOCONTRAT
				if  (ligne.equalsIgnoreCase("infoContrat")) {
					if (etatConnecte) {
						//ID du doc
						System.out.print("ID du contrat : ");
						String id = lire();
						
						Dictionary c = clientXML.infoContrat(login, id);
						
						if (c != null) {
							System.out.println("----------------- Contrat -------------------");
							System.out.println("Id: "+c.get("id"));
							System.out.println("Titre: "+c.get("titre"));
							System.out.println("Signature: "+c.get("dateSignature"));
							System.out.println("Expiration: "+c.get("dateExpiration"));
							System.out.println("Contractant: "+c.get("contractant"));
							System.out.println("R�glement: "+c.get("modeReglement"));
							System.out.println("Type: "+c.get("type"));
							System.out.println("------------ Documents associ�s -------------");
							
							Vector vDocs = (Vector)c.get("documents");
							Dictionary doc;
							for (int i=0; i<vDocs.size(); i++) {
								doc = (Dictionary)vDocs.get(i);
								System.out.println("Doc: "+doc);
							}
							System.out.println("----------------------------------------------");
							System.out.println();
							System.err.println("INFO: Contrat affich�");
						}
						else System.err.println("WARNING: Aucun contrat disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// AJOUTERDOCUMENTCONTRAT
				if  (ligne.equalsIgnoreCase("ajouterDocumentContrat")) {
					if (etatConnecte) {
						System.out.print("ID du document source: ");
						String idDoc = lire();
						System.out.print("ID du contrat cible: ");
						String idContrat = lire();
						boolean ajoute = clientXML.ajouterDocumentContrat(login, idContrat, idDoc);
						if (ajoute) System.err.println("INFO: Document ajout� au contrat");
						else System.err.println("ERREUR: Document non ajout� au contrat");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// RETIRERDOCUMENTCONTRAT
				if  (ligne.equalsIgnoreCase("retirerDocumentContrat")) {
					if (etatConnecte) {
						System.out.print("ID du contrat source: ");
						String idContrat = lire();
						System.out.print("ID du document � retirer : ");
						String idDoc = lire();
						boolean retire = clientXML.retirerDocumentContrat(login, idContrat, idDoc);
						if (retire) System.err.println("INFO: Document retir� du contrat");
						else System.err.println("ERREUR: Document non retir� du contrat");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
//### CONTRACTANT ###
				
				// CREERCONTRACTANT
				if  (ligne.equalsIgnoreCase("creerContractant")) {
					if (etatConnecte) {
						System.out.print("Nom du contractant: ");
						String nom = lire();
						System.out.print("Adresse: ");
						String adresse = lire();
						System.out.print("Code Postal: ");
						String codePostal = lire();
						System.out.print("Ville: ");
						String ville = lire();
						System.out.print("T�l�phone: ");
						String telephone = lire();
						System.out.print("Fax: ");
						String fax = lire();
						System.out.print("Email: ");
						String mail = lire();
						System.out.print("Type: ");
						String type = lire();
						boolean cree = clientXML.creerContractant(login, nom, adresse, codePostal, ville, telephone, fax, mail, type);
						if (cree) System.err.println("INFO: Contractant cr��");
						else System.err.println("ERREUR: Contractant non cr��");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// LISTERCONTRACTANTS
				if  (ligne.equalsIgnoreCase("listerContractant")) {
					if (etatConnecte) {
						Vector vContractants = clientXML.listerContractants(login);
						if (vContractants!=null && vContractants.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary c;
							for (int i=0; i<vContractants.size(); i++){
								c = (Dictionary)vContractants.get(i);
								System.out.println("--------------- Contractant -----------------");
								System.out.println("Id: "+c.get("id"));
								System.out.println("Nom: "+c.get("nom"));
								System.out.println("Adresse: "+c.get("adresse"));
								System.out.println("Code postal: "+c.get("codePostal"));
								System.out.println("Ville: "+c.get("ville"));
								System.out.println("T�l�phone: "+c.get("telephone"));
								System.out.println("Fax: "+c.get("fax"));
								System.out.println("Mail: "+c.get("mail"));
								System.out.println("Type: "+c.get("type"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Contractants list�s");
						}
						else System.err.println("WARNING: Aucun contractant disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// RECHERCHERCONTRACTANT
				if  (ligne.equalsIgnoreCase("rechercherContractant")) {
					if (etatConnecte) {
						System.out.print("ID du contractant: ");
						String id = lire();
						System.out.print("Nom: ");
						String nom = lire();
						System.out.print("Adresse: ");
						String adresse = lire();
						System.out.print("Code postal: ");
						String codePostal = lire();
						System.out.print("Ville: ");
						String ville = lire();
						System.out.print("T�l�phone: ");
						String telephone = lire();
						System.out.print("Fax: ");
						String fax = lire();
						System.out.print("Email: ");
						String mail = lire();
						System.out.print("Type: ");
						String type = lire();
						Vector vContractants = clientXML.rechercherContractant(login, id, nom, adresse, codePostal, ville, telephone, fax, mail, type);
						if (vContractants!=null && vContractants.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary c;
							for (int i=0; i<vContractants.size(); i++){
								c = (Dictionary)vContractants.get(i);
								System.out.println("--------------- Contractant -----------------");
								System.out.println("Id: "+c.get("id"));
								System.out.println("Nom: "+c.get("nom"));
								System.out.println("Adresse: "+c.get("adresse"));
								System.out.println("Code postal: "+c.get("codePostal"));
								System.out.println("Ville: "+c.get("ville"));
								System.out.println("T�l�phone: "+c.get("telephone"));
								System.out.println("Fax: "+c.get("fax"));
								System.out.println("Mail: "+c.get("mail"));
								System.out.println("Type: "+c.get("type"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Contractant recherch�");
						}
						else System.err.println("WARNING: Aucun contractant disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// SUPPRIMERCONTRACTANT
				if  (ligne.equalsIgnoreCase("supprimerContractant")) {
					if (etatConnecte) {
						System.out.print("ID du contractant ");
						String id = lire();
						if (clientXML.supprimerContractant(login, id)) {
							System.err.println("INFO: Contractant supprim�");
						}
						else System.err.println("ERREUR: Suppression du contractant �chou�e");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// MODIFIERCONTRACTANT
				if  (ligne.equalsIgnoreCase("modifierContractant")) {
					if (etatConnecte) {
						
						//ID du doc � modifier
						System.out.print("ID du contractant � modifier : ");
						String id = lire();
						
						//On affiche ses infos
						Vector vContractants = clientXML.rechercherContractant(login, id, "", "", "", "", "", "", "", "");
						if (vContractants!=null && vContractants.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary c;
							for (int i=0; i<vContractants.size(); i++){
								c = (Dictionary)vContractants.get(i);
								System.out.println("--------------- Contractant -----------------");
								System.out.println("Id: "+c.get("id"));
								System.out.println("Nom: "+c.get("nom"));
								System.out.println("Adresse: "+c.get("adresse"));
								System.out.println("Code postal: "+c.get("codePostal"));
								System.out.println("Ville: "+c.get("ville"));
								System.out.println("T�l�phone: "+c.get("telephone"));
								System.out.println("Fax: "+c.get("fax"));
								System.out.println("Mail: "+c.get("mail"));
								System.out.println("Type: "+c.get("type"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Contractant recherch�");
							
							//Si le doc existe, on le modifie
							System.out.print("Nouveau nom: ");
							String nom = lire();
							System.out.print("Adresse: ");
							String adresse = lire();
							System.out.print("Code Postal: ");
							String codePostal = lire();
							System.out.print("Ville: ");
							String ville = lire();
							System.out.print("T�l�phone: ");
							String telephone = lire();
							System.out.print("Fax: ");
							String fax = lire();
							System.out.print("Email: ");
							String mail = lire();
							System.out.print("Type: ");
							String type = lire();
							boolean modifie = clientXML.modifierContractant(login, id, nom, adresse, codePostal, ville, telephone, fax, mail, type);
							if (modifie) System.err.println("INFO: Contractant modifi�");
							else System.err.println("ERREUR: Contractant non modifi�");
						}
						else System.err.println("WARNING: Aucun contractant disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// INFOCONTRACTANT
				if  (ligne.equalsIgnoreCase("infoContractant")) {
					if (etatConnecte) {
						//ID du doc
						System.out.print("ID du contractant : ");
						String id = lire();
						
						Dictionary c = clientXML.infoContractant(login, id);
						
						if (c != null) {
							System.out.println("--------------- Contractant -----------------");
							System.out.println("Id: "+c.get("id"));
							System.out.println("Nom: "+c.get("nom"));
							System.out.println("Adresse: "+c.get("adresse"));
							System.out.println("Code postal: "+c.get("codePostal"));
							System.out.println("Ville: "+c.get("ville"));
							System.out.println("T�l�phone: "+c.get("telephone"));
							System.out.println("Fax: "+c.get("fax"));
							System.out.println("Mail: "+c.get("mail"));
							System.out.println("Type: "+c.get("type"));
							System.out.println("------------- Contrats associ�s --------------");
							
							Vector vContrats = (Vector)c.get("contrats");
							Dictionary contrat;
							for (int i=0; i<vContrats.size(); i++) {
								contrat = (Dictionary)vContrats.get(i);
								System.out.println("Contrat: "+contrat);
							}
							System.out.println("----------------------------------------------");
							System.out.println();
							System.err.println("INFO: Contractant affich�");
						}
						else System.err.println("WARNING: Aucun contractant disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
//### PERMISSION ###
				
				// CREERPERMISSION
				if  (ligne.equalsIgnoreCase("creerPermission")) {
					if (etatConnecte) {
						System.out.print("Code de la permission: ");
						String id = lire();
						System.out.print("Libell�: ");
						String libelle = lire();
						boolean cree = clientXML.creerPermission(login, id, libelle);
						if (cree) System.err.println("INFO: Permission cr��e");
						else System.err.println("ERREUR: Permission non cr��e");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// LISTERPERMISSIONS
				if  (ligne.equalsIgnoreCase("listerPermission")) {
					if (etatConnecte) {
						Vector vPermissions = clientXML.listerPermissions(login);
						if (vPermissions!=null && vPermissions.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary c;
							for (int i=0; i<vPermissions.size(); i++){
								c = (Dictionary)vPermissions.get(i);
								System.out.println("--------------- Permission -----------------");
								System.out.println("Id: "+c.get("id"));
								System.out.println("Libell�: "+c.get("libelle"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Permissions list�es");
						}
						else System.err.println("WARNING: Aucune permission disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// RECHERCHERPERMISSION
				if  (ligne.equalsIgnoreCase("rechercherPermission")) {
					if (etatConnecte) {
						System.out.print("ID de la permission: ");
						String id = lire();
						System.out.print("Libell�: ");
						String libelle = lire();
						Vector vPermissions = clientXML.rechercherPermission(login, id, libelle);
						if (vPermissions!=null && vPermissions.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary c;
							for (int i=0; i<vPermissions.size(); i++){
								c = (Dictionary)vPermissions.get(i);
								System.out.println("--------------- Permission -----------------");
								System.out.println("Id: "+c.get("id"));
								System.out.println("Libell�: "+c.get("libelle"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Permission recherch�e");
						}
						else System.err.println("WARNING: Aucune permission disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// SUPPRIMERPERMISSION
				if  (ligne.equalsIgnoreCase("supprimerPermission")) {
					if (etatConnecte) {
						System.out.print("ID de la permission ");
						String id = lire();
						if (clientXML.supprimerPermission(login, id)) {
							System.err.println("INFO: Permission supprim�e");
						}
						else System.err.println("ERREUR: Suppression de la permission �chou�e");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// MODIFIERPERMISSION
				if  (ligne.equalsIgnoreCase("modifierPermission")) {
					if (etatConnecte) {
						
						//ID du doc � modifier
						System.out.print("ID de la permission � modifier : ");
						String id = lire();
						
						//On affiche ses infos
						Vector vPermissions = clientXML.rechercherPermission(login, id, "");
						if (vPermissions!=null && vPermissions.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary c;
							for (int i=0; i<vPermissions.size(); i++){
								c = (Dictionary)vPermissions.get(i);
								System.out.println("--------------- Permission -----------------");
								System.out.println("Id: "+c.get("id"));
								System.out.println("Libell�: "+c.get("libelle"));
								System.out.println("----------------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: Permission recherch�e");
							
							//Si la permission existe, on la modifie
							System.out.print("Nouveau libell�: ");
							String libelle = lire();
							
							boolean modifie = clientXML.modifierPermission(login, id, libelle);
							if (modifie) System.err.println("INFO: Permission modifi�e");
							else System.err.println("ERREUR: Permission non modifi�e");
						}
						else System.err.println("WARNING: Aucune permission disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// INFOPERMISSION
				if  (ligne.equalsIgnoreCase("infoPermission")) {
					if (etatConnecte) {
						//ID du doc
						System.out.print("ID de la permission : ");
						String id = lire();
						
						Dictionary c = clientXML.infoPermission(login, id);
						
						if (c != null) {
							System.out.println("--------------- Permission -----------------");
							System.out.println("Id: "+c.get("id"));
							System.out.println("Libell�: "+c.get("libelle"));
							System.out.println("----------------------------------------------");
							System.out.println();
							System.err.println("INFO: Permission affich�e");
						}
						else System.err.println("WARNING: Aucun permission disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
//### ROLE ###
				
				// CREERROLE
				if  (ligne.equalsIgnoreCase("creerRole")) {
					if (etatConnecte) {
						System.out.print("Code du r�le: ");
						String id = lire();
						boolean cree = clientXML.creerRole(login, id);
						if (cree) System.err.println("INFO: R�le cr��");
						else System.err.println("ERREUR: R�le non cr��");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// LISTERROLES
				if  (ligne.equalsIgnoreCase("listerRole")) {
					if (etatConnecte) {
						Vector vRoles = clientXML.listerRoles(login);
						if (vRoles!=null && vRoles.size()>0) {
							//Parcours du vecteur, affichage des infos
							Dictionary c;
							for (int i=0; i<vRoles.size(); i++){
								c = (Dictionary)vRoles.get(i);
								System.out.println("--------------- R�le -----------------");
								System.out.println("Id: "+c.get("id"));
								System.out.println("--------------------------------------");
								System.out.println();
							}
							System.err.println("INFO: R�le list�s");
						}
						else System.err.println("WARNING: Aucun r�le disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// SUPPRIMERROLE
				if  (ligne.equalsIgnoreCase("supprimerRole")) {
					if (etatConnecte) {
						System.out.print("ID du r�le: ");
						String id = lire();
						if (clientXML.supprimerRole(login, id)) {
							System.err.println("INFO: R�le supprim�");
						}
						else System.err.println("ERREUR: Suppression de lu r�le �chou�e");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// INFOROLE
				if  (ligne.equalsIgnoreCase("infoRole")) {
					if (etatConnecte) {
						//ID du doc
						System.out.print("ID du r�le: ");
						String id = lire();
						
						Dictionary c = clientXML.infoRole(login, id);
						
						if (c != null) {
							System.out.println("--------------- R�le -----------------");
							System.out.println("Id: "+c.get("id"));
							System.out.println("-------- Permissions associ�s --------");
							
							Vector vPermissions = (Vector)c.get("permissions");
							Dictionary permission;
							for (int i=0; i<vPermissions.size(); i++) {
								permission = (Dictionary)vPermissions.get(i);
								System.out.println("Permission: "+permission);
							}
							System.out.println("--------------------------------------");
							System.out.println();
							System.err.println("INFO: R�le affich�");
						}
						else System.err.println("WARNING: Aucun r�le disponible");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// AJOUTERPERMISSIONROLE
				if  (ligne.equalsIgnoreCase("ajouterPermissionRole")) {
					if (etatConnecte) {
						System.out.print("ID de la permission source: ");
						String idPerm = lire();
						System.out.print("ID du r�le cible: ");
						String idRole = lire();
						boolean ajoute = clientXML.ajouterPermissionRole(login, idPerm, idRole);
						if (ajoute) System.err.println("INFO: Permission ajout�e au r�le");
						else System.err.println("ERREUR: Permission non ajout�e au r�le");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
				// RETIRERPERMISSIONROLE
				if  (ligne.equalsIgnoreCase("retirerPermissionRole")) {
					if (etatConnecte) {
						System.out.print("ID du role source: ");
						String idRole = lire();
						System.out.print("ID de la permission � retirer : ");
						String idPerm = lire();
						boolean retire = clientXML.retirerPermissionRole(login, idPerm, idRole);
						if (retire) System.err.println("INFO: Permission retir�e au r�le");
						else System.err.println("ERREUR: Permission non retir�e au r�le");
					}
					else {
						System.err.print("WARNING: Vous n'�tes pas connect� au serveur !");
					}
				}
				
//### MENU ###
				
				// HELP
				if (ligne.equalsIgnoreCase("help")) {
					System.out.println("Commandes disponibles:");
					System.out.println(" connexion : ouvrir une session sur le serveur");
					System.out.println(" deconnexion : fermer la session");
					
					System.out.println(" creerCanal : cr�er un canal");
					System.out.println(" modifierCanal : modifier un canal");
					System.out.println(" supprimerCanal : supprimer un canal");
					System.out.println(" rechercherCanal : rechercher un canal");
					System.out.println(" listerCanal : lister les canaux disponibles");
					System.out.println(" infoCanal : afficher les informations sur un canal");
					System.out.println(" startCanal : lancer la diffusion d'un canal");
					System.out.println(" stopCanal : stopper la diffusion d'un canal");
					System.out.println(" planifierProgramme : palnifie la diffusion d'un programme sur un canal");
					System.out.println(" deplanifierProgramme : d�planifier la diffusion d'un programme sur un canal");
					System.out.println(" startPlayer : lancer l'�coute un canal");
					System.out.println(" stopPlayer : stopper l'�coute d'un canal");
					
					System.out.println(" creerProgramme : cr�er un programme");
					System.out.println(" modifierProgramme : modifier un programme");
					System.out.println(" supprimerProgramme : supprimer un programme");
					System.out.println(" rechercherProgramme : rechercher un programme");
					System.out.println(" listerProgramme : lister les programmes disponibles");
					System.out.println(" infoProgramme : afficher les informations sur un programme");
					System.out.println(" ajouterDocumentProgramme : ajouter un document � un programme");
					System.out.println(" retirerDocumentProgramme : retirer un document d'un programme");
					System.out.println(" diffuserProgramme : diffuser un programme sur un canal");
					
					System.out.println(" creerDocument : cr�er un document");
					System.out.println(" modifierDocument : modifier un document");
					System.out.println(" supprimerDocument : supprimer un document");
					System.out.println(" rechercherDocument : rechercher un document");
					System.out.println(" listerDocument : lister les documents disponibles");
					System.out.println(" infoDocument : afficher les informations sur un document");
					
					System.out.println(" inscrireUtilisateur : inscription d'un nouvel utilisateur");
					System.out.println(" modifierUtilisateur : modifier vos attributs");
					System.out.println(" supprimerUtilisateur : suppression d'un utilisateur");
					System.out.println(" rechercherUtilisateur : rechercher vos attributs");
					System.out.println(" listerUtilisateur : lister les utilisateurs");
					System.out.println(" ajouterPermissionUtilisateur : ajouter une permission � un utilisateur");
					System.out.println(" retirerPermissionUtilisateur : retirer une permission d'un utilisateur");
					System.out.println(" changerRoleUtilisateur : changer le role d'un utilisateur");
					System.out.println(" infoUtilisateur : afficher les informations sur un utilisateur");
					System.out.println(" rechercherPassword : rechercher mot de passe");
					
					System.out.println(" creerContractant : cr�er un contractant");
					System.out.println(" modifierContractant : modifier un contractant");
					System.out.println(" supprimerContractant : supprimer un contractant");
					System.out.println(" rechercherContractant : rechercher un contractant");
					System.out.println(" listerContractant : lister les contractants disponibles");
					System.out.println(" infoContractant : afficher les informations sur un contractant");
					
					System.out.println(" creerContrat : cr�er un contrat");
					System.out.println(" modifierContrat : modifier un contrat");
					System.out.println(" supprimerContrat : supprimer un contrat");
					System.out.println(" rechercherContrat : rechercher un contrat");
					System.out.println(" listerContrat : lister les contrats disponibles");
					System.out.println(" infoContrat : afficher les informations sur un contrat");
					System.out.println(" ajouterDocumentContrat : ajouter un document � un contrat");
					System.out.println(" retirerDocumentContrat : retirer un document d'un contrat");
					
					System.out.println(" creerPermission : cr�er une permission");
					System.out.println(" modifierPermission : modifier une permission");
					System.out.println(" supprimerPermission : supprimer une permission");
					System.out.println(" rechercherPermission : rechercher une permission");
					System.out.println(" listerPermission : lister les permissions disponibles");
					System.out.println(" infoPermission : afficher les informations sur une permission");
					
					System.out.println(" creerRole : cr�er un r�le");
					System.out.println(" supprimerRole : supprimer un r�le");
					System.out.println(" listerRole : lister les r�les disponibles");
					System.out.println(" infoRole : afficher les informations sur un r�le");
					System.out.println(" ajouterPermissionRole : ajouter une permission � un r�le");
					System.out.println(" retirerPermissionRole : retirer une permission d'un r�le");

					System.out.println(" end : terminer");
					System.out.println(" help : lister les commandes disponibles");
				}
				
				// END
				if (ligne.equalsIgnoreCase("end")) {
					if (etatConnecte) clientXML.deconnexion(login);
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
		//String filename = args.length > 0 ? args[0] : "src/proto/client/client.ini";
		String filename = args.length > 0 ? args[0] : "/home/admindg/Workspace/NetJukeBox/proto/client/client.ini";
		//String filename = args.length > 0 ? args[0] : "C:/Documents and Settings/Marie Rubini/Mes documents/workspace/NetJukeBox/proto/client/client.ini ";

		Preferences prefs = new IniFile(new File(filename));
		
		//On d�marre le client
		new Client(prefs.node("serveur").get("ip", null), prefs.node("serveur").get("port", null));
		
	}
}