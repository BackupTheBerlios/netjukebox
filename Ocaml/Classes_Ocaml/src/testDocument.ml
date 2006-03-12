(*Fichier de TEST Classe Document*) 
open Document 
 
let d1 = new document("d1", "toto", "01022006", "albumDeTest", 120, "FR", "jazz", "fichier1");; 
Printf.printf "Id: %s\n" d1#getId;; 
Printf.printf "Titre: %s\n" d1#getTitre;; 
Printf.printf "Date: %s\n" d1#getDateCreation;; 
Printf.printf "Source: %s\n" d1#getSource;;
Printf.printf "Duree: %i\n" d1#getDuree;; 
Printf.printf "Etat: %s\n" d1#getEtat;; 
Printf.printf "Langue: %s\n" d1#getLangue;;
Printf.printf "Genre: %s\n" d1#getGenre;;
Printf.printf "Fichier: %s\n" d1#getFichier;;
Printf.printf "EnLecture: %b\n" d1#enLecture;;
Printf.printf "EstProgramme: %b\n" d1#estProgramme;;
Printf.printf "NbVerrou: %i\n" d1#compterVerrou;;
Printf.printf "PoserVerrou: %i\n" d1#poserVerrou;;
Printf.printf "PoserVerrou: %i\n" d1#poserVerrou;;
Printf.printf "NbVerrou: %i\n" d1#compterVerrou;;
Printf.printf "EnleverVerrou: %i\n" d1#enleverVerrou;;
Printf.printf "EnleverVerrou: %i\n" d1#compterVerrou;;
d1#ajouterProgramme("test");;
Printf.printf "Programmes:";;
List.iter print_string d1#getProgrammes;;