class serv_socket p = 
   object (self)
     val port = p 
     val mutable sock = ThreadUnix.socket Unix.PF_INET Unix.SOCK_STREAM 0
 
     initializer 
       let mon_adresse = get_my_addr () 
       in Unix.bind sock (Unix.ADDR_INET(mon_adresse,port)) ;
          Unix.listen sock 3
    
     method private client_addr = function 
         Unix.ADDR_INET(host,_) -> Unix.string_of_inet_addr host
       | _ -> "Unexpected client"
 
     method run () = 
       while(true) do 
         let (sd,sa) = ThreadUnix.accept sock in 
         let connexion = new connexion(sd,sa) 
         in Printf.printf "TRACE.serv: nouvelle connexion de %s\n\n"
                          (self#client_addr sa) ;
         ignore (connexion#start ())
       done
   end ;;