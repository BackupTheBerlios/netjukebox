package Client;

import java.io.*;

class Test {
	public static void main (String[] argv) throws IOException {
		String ligne;
		BufferedReader entree = new BufferedReader(new InputStreamReader(System.in));
		ligne = entree.readLine();
		System.out.println(ligne);
	}
}