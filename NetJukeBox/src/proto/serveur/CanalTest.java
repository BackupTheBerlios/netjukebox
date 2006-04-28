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
	 * M�thode de test pour 'proto.serveur.Canal.Canal(String, String, int)'
	 */
	public void testCanal() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.create(String, int)'
	 */
	public void testCreate() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.getByNom(String)'
	 */
	public void testGetByNom() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.getById(String)'
	 */
	public void testGetById() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.getAll()'
	 */
	public void testGetAll() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.deleteById(String)'
	 */
	public void testDeleteById() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.getAttributesDictionary()'
	 */
	public void testGetAttributesDictionary() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.getUrlStreaming()'
	 */
	public void testGetUrlStreaming() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.createRTPServer(String, int, String)'
	 */
	public void testCreateRTPServer() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.isRTPstarted()'
	 */
	public void testIsRTPstarted() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.startDiffusion()'
	 */
	public void testStartDiffusion() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.stopDiffusion()'
	 */
	public void testStopDiffusion() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.supprimer()'
	 */
	public void testSupprimer() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.estActif()'
	 */
	public void testEstActif() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.getId()'
	 */
	public void testGetId() {
		assertEquals(c.getId(),"id");
	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.getNom()'
	 */
	public void testGetNom() {
		assertEquals(c.getNom(),"nom");
	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.getUtilMax()'
	 */
	public void testGetUtilMax() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.insertionInfos(String, String, int)'
	 */
	public void testInsertionInfos() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.verifierPlanification(Date, int, String)'
	 */
	public void testVerifierPlanification() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.bloquerPlage(String, Date, int)'
	 */
	public void testBloquerPlage() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.diffuserProgramme(Programme)'
	 */
	public void testDiffuserProgramme() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.setActif()'
	 */
	public void testSetActif() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.arreterDiffusionCanal(int)'
	 */
	public void testArreterDiffusionCanal() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.deconnecterAuditeur(String)'
	 */
	public void testDeconnecterAuditeur() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.connecterAuditeur(String)'
	 */
	public void testConnecterAuditeur() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.relanceDiffusionCanal(String)'
	 */
	public void testRelanceDiffusionCanal() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.setNom(String)'
	 */
	public void testSetNom() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Canal.setUtilMax(int)'
	 */
	public void testSetUtilMax() {

	}

}
