(* CLASSE PROGRAMME *)
(*
open Lire
*)
open Ecrire

class programme((id_prog:string), (titre:string), (thematique:string)) =
object

	val mutable id_prog = id_prog
	val mutable titre = titre
	val mutable thematique = thematique
	val mutable duree = 0
	val mutable etat_archive = "false"
	val mutable fichier_archive = "_Archive.txt"
		
	method get_titre =
	Printf.printf "%s\n" titre 

	method get_id_prog =
	Printf.printf "%s\n" id_prog
		
	method get_thematique =
	Printf.printf "%s\n" thematique
	
	method get_duree =
	print_int duree;
	print_newline();
	
	method get_etat = 
	print_string etat_archive;
	print_newline();

	method archiver = 	
		if etat_archive = "false" then
			begin
			etat_archive <- "true";
			(*Test affichage état :print_string etat_archive; print_newline();*)
			(*Création du nom du fichier Archive:*)
			fichier_archive <- id_prog ^ fichier_archive;
			(*Sauvegarde de la liste des documents dans un fichier:*)
			sauve(fichier_archive, [id_prog]);
			Printf.printf "Le programme est archivé dans le fichier : %s\n" fichier_archive;
			print_newline();
			end
		else
		begin
		print_string "Erreur : le programme est deja archive";
		print_newline();
		end

		method ajouterdocument = 0
		
		
		
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