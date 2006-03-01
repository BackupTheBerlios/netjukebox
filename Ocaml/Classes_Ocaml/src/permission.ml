class permission ((id_perm:string), (lib_perm:string)) =
object

val mutable id_perm = id_perm
val mutable lib_perm = lib_perm

method get_permission =
	Printf.printf "N° permission : %s\n" id_perm;
	Printf.printf "Libellé permission : %s\n" lib_perm;
	
method ajouter = 0 

method supprimer = 0

method modifier = 0

end ;;



