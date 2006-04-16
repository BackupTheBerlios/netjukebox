package proto.serveur;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ExempleBasic {
   static Logger logger = Logger.getLogger(ExempleBasic.class);
     ExempleBasic(){
       PropertyConfigurator.configure("./log4j.prop");
   }

   public void start(){
       logger.info("Entering application.");
       System.out.println("hello world !");
       logger.info("Exiting application.");
   }

   public static void main(String[] args) {
       ExempleBasic ex = new ExempleBasic();
       ex.start();
   }
}