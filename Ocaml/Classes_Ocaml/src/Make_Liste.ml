(*Cr�ation d'une liste vide*)
let liste = [];;

(*Ajout d'un �l�ment dans la liste*)
let ajoutliste = function(toto) -> 
let liste = toto :: liste in liste;;

(*Affichage des �l�ments d'une liste*)
let rec affiche = function
  [] -> ()
| x :: l -> Printf.printf "%s\n" x; Printf.printf " "; affiche l in
affiche liste;;