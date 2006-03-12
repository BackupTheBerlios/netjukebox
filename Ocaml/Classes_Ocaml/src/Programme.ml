(* CLASSE PROGRAMME *)
(*
open Lire
open RechercherDocument
*)
open Ecrire
(*open Make_Liste*)

class programme((id_prog:string), (titre:string), (thematique:string)) =
object

	val mutable id_prog = id_prog
	val mutable titre = titre
	val mutable thematique = thematique
	val mutable duree = 0
	val mutable etat_archive = "false"
	val mutable fichier_archive = "_Archive.txt"

	method get_titre = (*titre*)
	Printf.printf "%s\n" titre 

	method get_id_prog = (*id_prog*)
	Printf.printf "%s\n" id_prog
		
	method get_thematique = (*thematique*)
	Printf.printf "%s\n" thematique
	
	method get_duree = (*duree*)
	Printf.printf "%i\n" duree;
		
	method get_etat_archive = (*etat_archive*)
	Printf.printf "%s\n" etat_archive;
	
	method archiver = 
		if etat_archive = "false" then
			begin
			etat_archive <- "true";
			(*//Test affichage état :print_string etat_archive; print_newline();*)
			(*Création du nom du fichier Archive:*)
			fichier_archive <- id_prog ^ fichier_archive;
			(*Sauvegarde de la liste des documents dans un fichier:*)
			sauve(fichier_archive, [id_prog](*id_prog est à remplacer par la liste des docs audio*));
			Printf.printf "Le programme est archivé dans le fichier : %s\n" fichier_archive;
			end
		else
		begin
		Printf.printf "Erreur : le programme est deja archive";
		print_newline();
		end

	val mutable liste = []
	method ajouterdocument(id_doc) = liste <- id_doc :: liste;

	method get_liste = liste

	
	
	(*method endiffusion = 0
	method ajouterprogramme = 0
	method listerdocument = 0
	method diffuserprogramme = 0
	method verrouillerdocument = 0
	method ajout = 0
	method deverrouillerdocument = 0
	method settitre = 0
	method retraitdocument = 0
	method retraitprogramme = 0
	method insertioninfos = 0
	method planifier = 0
	method setdiffusion = 0
	method arreterdiffusionprogramme = 0
	method relancerdiffusionprogramme = 0
	method retirerdocument = 0 
	method retraitdoc = 0*)

end;;