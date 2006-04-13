package proto.serveur;

import java.util.Date;
import java.text.SimpleDateFormat;

public class TransformeDate {
	public Date d;

	public TransformeDate(String sDate) {
		try {
			d = stringToDate(sDate, "dd-MM-yyyy");
		} catch(Exception e) {
			System.err.println("Exception :");
			e.printStackTrace();
		}
	}

	public static Date stringToDate(String sDate, String sFormat) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
		return sdf.parse(sDate);
	}
}
