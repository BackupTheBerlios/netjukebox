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
	 * M�thode de test pour 'proto.serveur.Document.Document(String, String, int, String, String, String, String, String, String, String)'
	 */
	public void testDocument() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.create(String, int, String, String, String, String, String, String, String)'
	 */
	public void testCreate() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.getByTitre(String)'
	 */
	public void testGetByTitre() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.getById(String)'
	 */
	public void testGetById() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.getAll()'
	 */
	public void testGetAll() {
	
	}




	/*
	 * M�thode de test pour 'proto.serveur.Document.deleteById(String)'
	 */
	public void testDeleteById() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.supprimer()'
	 */
	public void testSupprimer() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.getAttributesDictionary()'
	 */
	public void testGetAttributesDictionary() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.getId()'
	 */
	public void testGetId() {
		assertEquals(d.getId(),"id");
	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.getTitre()'
	 */
	public void testGetTitre() {
		assertEquals(d.getTitre(),"titre");
	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.getDuree()'
	 */
	public void testGetDuree() {
		assertEquals(d.getDuree(),1);
	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.getSource()'
	 */
	public void testGetSource() {
		assertEquals(d.getSource(),"source");
	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.getLangue()'
	 */
	public void testGetLangue() {
		assertEquals(d.getLangue(),"langue");
	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.getGenre()'
	 */
	public void testGetGenre() {
		assertEquals(d.getGenre(),"genre");
	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.getFichier()'
	 */
	public void testGetFichier() {
		assertEquals(d.getFichier(),"fichier");
	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.getEtat()'
	 */
	public void testGetEtat() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.compterVerrouProgramme()'
	 */
	public void testCompterVerrouProgramme() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.getProgrammesArchives()'
	 */
	public void testGetProgrammesArchives() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.ajouterProgramme(Programme)'
	 */
	public void testAjouterProgramme() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.enleverProgramme(String)'
	 */
	public void testEnleverProgramme() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.getDateCreation()'
	 */
	public void testGetDateCreation() {
		
	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.setFichier(String)'
	 */
	public void testSetFichier() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.setGenre(String)'
	 */
	public void testSetGenre() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.setLangue(String)'
	 */
	public void testSetLangue() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.setSource(String)'
	 */
	public void testSetSource() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.setAnnee(String)'
	 */
	public void testSetAnnee() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.setMois(String)'
	 */
	public void testSetMois() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.setJour(String)'
	 */
	public void testSetJour() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.setTitre(String)'
	 */
	public void testSetTitre() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.setDuree(int)'
	 */
	public void testSetDuree() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.modifier(String, int, String, String, String, String, String, String, String)'
	 */
	public void testModifier() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.enLecture()'
	 */
	public void testEnLecture() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.estProgramme()'
	 */
	public void testEstProgramme() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.nommerFichier(String)'
	 */
	public void testNommerFichier() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Document.selectionner()'
	 */
	public void testSelectionner() {

	}

}
