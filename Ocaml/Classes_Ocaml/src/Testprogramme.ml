(*Fichier de TEST Classe Programme*)
open Programme

let p1 = new programme("1p", "toto", "titi");;
p1#get_titre;;
p1#get_id_prog;;
p1#get_thematique;;
p1#get_duree;;
p1#get_etat_archive;;
p1#archiver;;
p1#ajouterdocument("toto");;
p1#ajouterdocument("tata");;
p1#ajouterdocument("tutu");;
p1#ajouterdocument("titi");;
List.iter print_string p1#get_liste;;