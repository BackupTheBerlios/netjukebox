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
	 * Méthode de test pour 'proto.serveur.Utilisateur.create(String, String, String, String, String, String, String)'
	 */
	public void testCreate() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.getByLogin(String)'
	 */
	public void testGetByLogin() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.deleteByLogin(String, String)'
	 */
	public void testDeleteByLogin() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.verifierLogin(String)'
	 */
	public void testVerifierLogin() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.Utilisateur()'
	 */
	public void testUtilisateur() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.Utilisateur(String, String, String, String, String, String)'
	 */
	public void testUtilisateurStringStringStringStringStringString() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.supprimer()'
	 */
	public void testSupprimer() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.deconnecter()'
	 */
	public void testDeconnecter() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.deconnexion()'
	 */
	public void testDeconnexion() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.connecter(String, String)'
	 */
	public void testConnecter() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.connexion()'
	 */
	public void testConnexion() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.modifierInfos()'
	 */
	public void testModifierInfos() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.setPermission(int, String)'
	 */
	public void testSetPermission() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.verifConnecte()'
	 */
	public void testVerifConnecte() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.getLogin()'
	 */
	public void testGetLogin() {
		assertEquals(u.getLogin(),"login");
	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.getNom()'
	 */
	public void testGetNom() {
		assertEquals(u.getNom(),"nom");
	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.getPrenom()'
	 */
	public void testGetPrenom() {
		assertEquals(u.getPrenom(),"prenom");
	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.getMail()'
	 */
	public void testGetMail() {
		assertEquals(u.getMail(),"mail");
	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.getPays()'
	 */
	public void testGetPays() {
		assertEquals(u.getPays(),"pays");
	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.getRole()'
	 */
	public void testGetRole() {
		assertEquals(u.getRole(),"role");
	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.estConnecte()'
	 */
	public void testEstConnecte() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.selectionner()'
	 */
	public void testSelectionner() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.modifier(String, String, String, String, String)'
	 */
	public void testModifier() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.majInfos(String, String, String, String, String)'
	 */
	public void testMajInfos() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.getPermissions()'
	 */
	public void testGetPermissions() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.ajouterPermission(String, String, String)'
	 */
	public void testAjouterPermission() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.retirerPermission(int)'
	 */
	public void testRetirerPermission() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.definirPermission(int, String)'
	 */
	public void testDefinirPermission() {

	}

	/*
	 * Méthode de test pour 'proto.serveur.Utilisateur.verifPermission(String)'
	 */
	public void testVerifPermission() {

	}

}
