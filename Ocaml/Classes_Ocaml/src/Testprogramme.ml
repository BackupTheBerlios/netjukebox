(*Fichier de TEST Classe Programme*)
open Programme

let p1 = new programme("1p", "toto", "titi");;
p1#get_titre;;
p1#get_id_prog;;
p1#get_thematique;;
p1#get_duree;;
p1#get_etat_archive;;
p1#archiver;;