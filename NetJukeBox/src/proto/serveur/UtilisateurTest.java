package proto.serveur;

import junit.framework.TestCase;

public class UtilisateurTest extends TestCase {
	
	Utilisateur u;
	
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(UtilisateurTest.class);
	}

	public UtilisateurTest(String arg) {
		super(arg);
	}

	protected void setUp() throws Exception {
		super.setUp();
		u= new Utilisateur ("login","pwd","nom","prenom","mail","pays","role");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		u=null;
	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.create(String, String, String, String, String, String, String)'
	 */
	public void testCreate() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.getByLogin(String)'
	 */
	public void testGetByLogin() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.deleteByLogin(String, String)'
	 */
	public void testDeleteByLogin() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.verifierLogin(String)'
	 */
	public void testVerifierLogin() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.Utilisateur()'
	 */
	public void testUtilisateur() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.Utilisateur(String, String, String, String, String, String)'
	 */
	public void testUtilisateurStringStringStringStringStringString() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.supprimer()'
	 */
	public void testSupprimer() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.deconnecter()'
	 */
	public void testDeconnecter() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.deconnexion()'
	 */
	public void testDeconnexion() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.connecter(String, String)'
	 */
	public void testConnecter() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.connexion()'
	 */
	public void testConnexion() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.modifierInfos()'
	 */
	public void testModifierInfos() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.setPermission(int, String)'
	 */
	public void testSetPermission() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.verifConnecte()'
	 */
	public void testVerifConnecte() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.getLogin()'
	 */
	public void testGetLogin() {
		assertEquals(u.getLogin(),"login");
	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.getNom()'
	 */
	public void testGetNom() {
		assertEquals(u.getNom(),"nom");
	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.getPrenom()'
	 */
	public void testGetPrenom() {
		assertEquals(u.getPrenom(),"prenom");
	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.getMail()'
	 */
	public void testGetMail() {
		assertEquals(u.getMail(),"mail");
	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.getPays()'
	 */
	public void testGetPays() {
		assertEquals(u.getPays(),"pays");
	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.getRole()'
	 */
	public void testGetRole() {
		assertEquals(u.getRole(),"role");
	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.estConnecte()'
	 */
	public void testEstConnecte() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.selectionner()'
	 */
	public void testSelectionner() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.modifier(String, String, String, String, String)'
	 */
	public void testModifier() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.majInfos(String, String, String, String, String)'
	 */
	public void testMajInfos() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.getPermissions()'
	 */
	public void testGetPermissions() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.ajouterPermission(String, String, String)'
	 */
	public void testAjouterPermission() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.retirerPermission(int)'
	 */
	public void testRetirerPermission() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.definirPermission(int, String)'
	 */
	public void testDefinirPermission() {

	}

	/*
	 * M�thode de test pour 'proto.serveur.Utilisateur.verifPermission(String)'
	 */
	public void testVerifPermission() {

	}

}
