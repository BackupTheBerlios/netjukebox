package proto.serveur;
import java.io.*;

public class LireFichier {
String source, scan;
String Ipup[] = new String[256];
int i =0;

	public LireFichier(String source, String scan) {
		this.source = source;
		this.scan = scan;
		try {
			String ligne;
			BufferedReader fichier = new BufferedReader(new FileReader(source));
			while ((ligne = fichier.readLine()) != null) {
				if (ligne.equals("up")) {
					System.out.println(ligne);
					Ipup[i] = fichier.readLine();
					i++;
				}
			}
			fichier.close();
		} catch (Exception e) {
			e.printStackTrace();
        }
	}
}

