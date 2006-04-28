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
	 * Méthode de test pour 'proto.serveur.Programme.Programme(String, String, String, long)'
	 */
	public void testProgramme() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.create(String, String)'
	 */
	public void testCreate() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.getByTitre(String)'
	 */
	public void testGetByTitre() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.getById(String)'
	 */
	public void testGetById() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.getAll()'
	 */
	public void testGetAll() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.deleteById(String)'
	 */
	public void testDeleteById() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.getAttributesDictionary()'
	 */
	public void testGetAttributesDictionary() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.supprimer()'
	 */
	public void testSupprimer() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.getId()'
	 */
	public void testGetId() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.getTitre()'
	 */
	public void testGetTitre() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.getThematique()'
	 */
	public void testGetThematique() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.getDocuments()'
	 */
	public void testGetDocuments() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.getEtat()'
	 */
	public void testGetEtat() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.getDuree()'
	 */
	public void testGetDuree() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.setDuree(long)'
	 */
	public void testSetDuree() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.setTitre(String)'
	 */
	public void testSetTitre() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.setThematique(String)'
	 */
	public void testSetThematique() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.ajouterDocument(Document)'
	 */
	public void testAjouterDocument() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.retirerDocument(Document)'
	 */
	public void testRetirerDocumentDocument() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.listerDocuments()'
	 */
	public void testListerDocuments() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.ajouterProgramme(Programme)'
	 */
	public void testAjouterProgramme() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.archiver()'
	 */
	public void testArchiver() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.enDiffusion()'
	 */
	public void testEnDiffusion() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.DiffuserProgramme(String)'
	 */
	public void testDiffuserProgramme() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.Ajout(String)'
	 */
	public void testAjout() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.RetraitProgramme(String)'
	 */
	public void testRetraitProgramme() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.Planifier(Date, int, String)'
	 */
	public void testPlanifier() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.SetDiffusion()'
	 */
	public void testSetDiffusion() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.ArreterDiffusionProgramme(String)'
	 */
	public void testArreterDiffusionProgramme() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.RelancerDiffusionProgramme(String)'
	 */
	public void testRelancerDiffusionProgramme() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.retirerDocument(String)'
	 */
	public void testRetirerDocumentString() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Programme.retraitDoc(String)'
	 */
	public void testRetraitDoc() {

	}

}
