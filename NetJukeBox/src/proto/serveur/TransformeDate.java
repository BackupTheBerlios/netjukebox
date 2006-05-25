package proto.serveur;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

public class TransformeDate {
	/**
	 *  Crée le logger de la classe
	 */
	private static final Logger logger = Logger.getLogger(TransformeDate.class);
	
	public Date d;

	public TransformeDate(String sDate) {
		logger.debug("Démarrage: TransformeDate");
		try {
			d = stringToDate(sDate, "dd-MM-yyyy");
		} catch(Exception e) {
			logger.error("Exception :", e);
			logger.debug("Arrêt: TransformeDate");
		}
	}

	public static Date stringToDate(String sDate, String sFormat) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
		return sdf.parse(sDate);
	}
}
