package proto.serveur;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(AllTests.class);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for proto.serveur");
		//$JUnit-BEGIN$
		suite.addTestSuite(DocumentTest.class);
		suite.addTestSuite(UtilisateurTest.class);
		suite.addTestSuite(ProgrammeTest.class);
		suite.addTestSuite(CanalTest.class);
		//$JUnit-END$
		return suite;
	}

}
