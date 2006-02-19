(* Serveur Ocaml*)

(* Le serveur générique establish_server décrit ci-dessous est une fonction
qui prend en premier argument la fonction de service (server_fun) chargée de
traiter les requêtes et, en second, l'adresse de la socket dans le domaine 
Internet qui sera à l'écoute des requêtes. Cette fonction utilise la fonction
auxiliaire domain_of qui extrait le domaine d'une socket à partir de son adresse.*)
let establish_server server_fun sockaddr =
   let domain = domain_of sockaddr in
   (*let domain = domain_of sockaddr in*)
   let sock = Unix.socket domain Unix.SOCK_STREAM 0 
   in Unix.bind sock sockaddr ;
      Unix.listen sock 3;
      while true do
        let (s, caller) = Unix.accept sock 
        in match Unix.fork() with
               0 -> if Unix.fork() <> 0 then exit 0 ; 
                    let inchan = Unix.in_channel_of_descr s 
                    and outchan = Unix.out_channel_of_descr s 
                    in server_fun inchan outchan ;
                       close_in inchan ;
                       close_out outchan ;
                       exit 0
             | id -> Unix.close s; ignore(Unix.waitpid [] id)
      done ;;
(*val establish_server :
 (in_channel -> out_channel -> 'a) -> Unix.sockaddr -> unit = <fun>*)
   
(* Pour construire complètement un serveur en tant qu'exécutable autonome
paramétré par le numéro de port, on écrit la fonction main_serveur qui prend
toujours en paramètre la fonction de service. Elle utilise le paramètre de la
ligne de commande comme numéro de port du service. On utilise la fonction
auxiliaire get_my_addr qui retourne l'adresse de la machine locale. *)
let get_my_addr () =
   (Unix.gethostbyname(Unix.gethostname())).Unix.h_addr_list.(0) ;;
(* val get_my_addr : unit -> Unix.inet_addr = <fun>*)

let main_serveur serv_fun =
   if Array.length Sys.argv < 2 then Printf.eprintf "usage : serv_up port\n"
   else try
          let port =  int_of_string Sys.argv.(1) in (* Mettre N° port en dur *)
          let mon_adresse = get_my_addr() 
          in establish_server serv_fun  (Unix.ADDR_INET(mon_adresse, port))
        with
          Failure("int_of_string") -> 
            Printf.eprintf "serv_up : bad port number\n" ;;
(* val main_serveur : (in_channel -> out_channel -> 'a) -> unit = <fun>*)

(* La mécanique générale est en place. Pour l'illustrer, il reste à définir
le service. Celui-ci est un convertisseur de chaînes en majuscules. Il attend
une ligne sur le canal d'entrée, la convertit et l'écrit sur le canal de sortie
en vidant le tampon.*)
let uppercase_service ic oc =
   try while true do    
         let s = input_line ic in 
         let r = String.uppercase s 
         in output_string oc (r^"\n") ; flush oc
       done
   with _ -> Printf.printf "Fin du traitement\n" ; flush stdout ; exit 0 ;;
(* val uppercase_service : in_channel -> out_channel -> unit = <fun> *)

(* our récupérer correctement les exceptions provenant du module Unix,
on encapsule l'appel au démarrage du service dans la fonction ad hoc du
module Unix : *)
let go_uppercase_service () = 
   Unix.handle_unix_error main_serveur uppercase_service ;;
(* val go_uppercase_service : unit -> unit = <fun>*)