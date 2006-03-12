(*Création d'une liste vide*)
let liste = [];;

(*Ajout d'un élément dans la liste*)
let ajoutliste = function(toto) -> 
let liste = toto :: liste in liste;;

(*Affichage des éléments d'une liste*)
let rec affiche = function
  [] -> ()
| x :: l -> Printf.printf "%s\n" x; Printf.printf " "; affiche l in
affiche liste;;