package proto.serveur;

import junit.framework.TestCase;

public class ProgrammeTest extends TestCase {
	Programme p;
	
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(ProgrammeTest.class);
	}

	public ProgrammeTest(String arg) {
		super(arg);
	}

	protected void setUp() throws Exception {
		super.setUp();
		p=new Programme ("id","titre","thematique",1);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		p=null;
	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.Programme(String, String, String, long)'
	 */
	public void testProgramme() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.create(String, String)'
	 */
	public void testCreate() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.getByTitre(String)'
	 */
	public void testGetByTitre() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.getById(String)'
	 */
	public void testGetById() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.getAll()'
	 */
	public void testGetAll() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.deleteById(String)'
	 */
	public void testDeleteById() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.getAttributesDictionary()'
	 */
	public void testGetAttributesDictionary() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.supprimer()'
	 */
	public void testSupprimer() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.getId()'
	 */
	public void testGetId() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.getTitre()'
	 */
	public void testGetTitre() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.getThematique()'
	 */
	public void testGetThematique() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.getDocuments()'
	 */
	public void testGetDocuments() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.getEtat()'
	 */
	public void testGetEtat() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.getDuree()'
	 */
	public void testGetDuree() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.setDuree(long)'
	 */
	public void testSetDuree() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.setTitre(String)'
	 */
	public void testSetTitre() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.setThematique(String)'
	 */
	public void testSetThematique() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.ajouterDocument(Document)'
	 */
	public void testAjouterDocument() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.retirerDocument(Document)'
	 */
	public void testRetirerDocumentDocument() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.listerDocuments()'
	 */
	public void testListerDocuments() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.ajouterProgramme(Programme)'
	 */
	public void testAjouterProgramme() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.archiver()'
	 */
	public void testArchiver() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.enDiffusion()'
	 */
	public void testEnDiffusion() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.DiffuserProgramme(String)'
	 */
	public void testDiffuserProgramme() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.Ajout(String)'
	 */
	public void testAjout() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.RetraitProgramme(String)'
	 */
	public void testRetraitProgramme() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.Planifier(Date, int, String)'
	 */
	public void testPlanifier() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.SetDiffusion()'
	 */
	public void testSetDiffusion() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.ArreterDiffusionProgramme(String)'
	 */
	public void testArreterDiffusionProgramme() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.RelancerDiffusionProgramme(String)'
	 */
	public void testRelancerDiffusionProgramme() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.retirerDocument(String)'
	 */
	public void testRetirerDocumentString() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Programme.retraitDoc(String)'
	 */
	public void testRetraitDoc() {

	}

}
