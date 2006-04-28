package proto.serveur;

import junit.framework.TestCase;

public class CanalTest extends TestCase {
	Canal c;
	
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(CanalTest.class);
	}

	public CanalTest(String arg) {
		super(arg);
	}

	protected void setUp() throws Exception {
		super.setUp();
		c=new Canal ("id","nom", 30);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		c=null;
	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.Canal(String, String, int)'
	 */
	public void testCanal() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.create(String, int)'
	 */
	public void testCreate() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.getByNom(String)'
	 */
	public void testGetByNom() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.getById(String)'
	 */
	public void testGetById() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.getAll()'
	 */
	public void testGetAll() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.deleteById(String)'
	 */
	public void testDeleteById() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.getAttributesDictionary()'
	 */
	public void testGetAttributesDictionary() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.getUrlStreaming()'
	 */
	public void testGetUrlStreaming() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.createRTPServer(String, int, String)'
	 */
	public void testCreateRTPServer() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.isRTPstarted()'
	 */
	public void testIsRTPstarted() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.startDiffusion()'
	 */
	public void testStartDiffusion() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.stopDiffusion()'
	 */
	public void testStopDiffusion() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.supprimer()'
	 */
	public void testSupprimer() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.estActif()'
	 */
	public void testEstActif() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.getId()'
	 */
	public void testGetId() {
		assertEquals(c.getId(),"id");
	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.getNom()'
	 */
	public void testGetNom() {
		assertEquals(c.getNom(),"nom");
	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.getUtilMax()'
	 */
	public void testGetUtilMax() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.insertionInfos(String, String, int)'
	 */
	public void testInsertionInfos() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.verifierPlanification(Date, int, String)'
	 */
	public void testVerifierPlanification() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.bloquerPlage(String, Date, int)'
	 */
	public void testBloquerPlage() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.diffuserProgramme(Programme)'
	 */
	public void testDiffuserProgramme() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.setActif()'
	 */
	public void testSetActif() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.arreterDiffusionCanal(int)'
	 */
	public void testArreterDiffusionCanal() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.deconnecterAuditeur(String)'
	 */
	public void testDeconnecterAuditeur() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.connecterAuditeur(String)'
	 */
	public void testConnecterAuditeur() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.relanceDiffusionCanal(String)'
	 */
	public void testRelanceDiffusionCanal() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.setNom(String)'
	 */
	public void testSetNom() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Canal.setUtilMax(int)'
	 */
	public void testSetUtilMax() {

	}

}
