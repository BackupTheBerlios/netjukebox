package proto.serveur;

import junit.framework.TestCase;

public class DocumentTest extends TestCase {
	
	Document d;

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(DocumentTest.class);
	}

	public DocumentTest(String arg) {
		super(arg);
	}

	protected void setUp() throws Exception {
		super.setUp();
		d = new Document ("id","titre",1,"jour","mois","annee","source","langue","genre","fichier");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		super.tearDown();
		d=null;
	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.Document(String, String, int, String, String, String, String, String, String, String)'
	 */
	public void testDocument() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.create(String, int, String, String, String, String, String, String, String)'
	 */
	public void testCreate() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.getByTitre(String)'
	 */
	public void testGetByTitre() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.getById(String)'
	 */
	public void testGetById() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.getAll()'
	 */
	public void testGetAll() {
	
	}




	/*
	 * Méthode de test pour 'proto.serveur.Document.deleteById(String)'
	 */
	public void testDeleteById() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.supprimer()'
	 */
	public void testSupprimer() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.getAttributesDictionary()'
	 */
	public void testGetAttributesDictionary() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.getId()'
	 */
	public void testGetId() {
		assertEquals(d.getId(),"id");
	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.getTitre()'
	 */
	public void testGetTitre() {
		assertEquals(d.getTitre(),"titre");
	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.getDuree()'
	 */
	public void testGetDuree() {
		assertEquals(d.getDuree(),1);
	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.getSource()'
	 */
	public void testGetSource() {
		assertEquals(d.getSource(),"source");
	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.getLangue()'
	 */
	public void testGetLangue() {
		assertEquals(d.getLangue(),"langue");
	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.getGenre()'
	 */
	public void testGetGenre() {
		assertEquals(d.getGenre(),"genre");
	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.getFichier()'
	 */
	public void testGetFichier() {
		assertEquals(d.getFichier(),"fichier");
	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.getEtat()'
	 */
	public void testGetEtat() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.compterVerrouProgramme()'
	 */
	public void testCompterVerrouProgramme() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.getProgrammesArchives()'
	 */
	public void testGetProgrammesArchives() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.ajouterProgramme(Programme)'
	 */
	public void testAjouterProgramme() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.enleverProgramme(String)'
	 */
	public void testEnleverProgramme() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.getDateCreation()'
	 */
	public void testGetDateCreation() {
		
	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.setFichier(String)'
	 */
	public void testSetFichier() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.setGenre(String)'
	 */
	public void testSetGenre() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.setLangue(String)'
	 */
	public void testSetLangue() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.setSource(String)'
	 */
	public void testSetSource() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.setAnnee(String)'
	 */
	public void testSetAnnee() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.setMois(String)'
	 */
	public void testSetMois() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.setJour(String)'
	 */
	public void testSetJour() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.setTitre(String)'
	 */
	public void testSetTitre() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.setDuree(int)'
	 */
	public void testSetDuree() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.modifier(String, int, String, String, String, String, String, String, String)'
	 */
	public void testModifier() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.enLecture()'
	 */
	public void testEnLecture() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.estProgramme()'
	 */
	public void testEstProgramme() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.nommerFichier(String)'
	 */
	public void testNommerFichier() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Document.selectionner()'
	 */
	public void testSelectionner() {

	}

}
