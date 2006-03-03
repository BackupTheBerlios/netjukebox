(* Debut du code *)
class canal ((id_canal:string), (nom_canal:string),(flux_max:int),(id_proto:int),(id_journal:int),(id_etat:string)) =
object
val id_canal = id_canal
val nom_canal =nom_canal
val flux_max = flux_max
val id_proto = id_proto
val id_journal = id_journal
val id_etat = id_etat

method get_id_canal = id_canal
method get_nom_canal = nom_canal
method get_flux_max = flux_max
method get_id_proto = id_proto
method get_id_journal = id_journal
method get_id_etat = id_etat
(*
method canal_initialise=bool
method canal_actif= bool
method canal_attente=bool
method canal_ouvert=bool
method canal_plein=bool
method canal_panne=bool
method canal_supprime=bool

method creer_canal(id_canal,nom_canal,flux_max,id_proto,id_journal,id_etat)= bool
method rechercher_canal(id_canal,nom_canal,flux_max,id_proto,id_journal,id_etat)= bool
method modifier_canal(id_canal,nom_canal,flux_max,id_proto,id_journal,id_etat)= bool
method supprimer_canal(id_canal,nom_canal,flux_max,id_proto,id_journal,id_etat)= bool
method afficher_canal_actif(id_canal,nom_canal,flux_max,id_proto,id_journal,id_etat)= bool
method gerer_canal(id_canal,nom_canal,flux_max,id_proto,id_journal,id_etat)= bool
method lister_canaux_gerer

*)

end ;;

(* Fin du code *)