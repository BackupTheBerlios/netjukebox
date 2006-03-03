let init = ref false;;
external db_init: unit -> unit = "db_init";;
external finish: unit -> unit = "db_finish";;
external db_connect: string -> string -> string -> string -> int -> int = "db_connect";;

let connect localhost postgres postgres netjukebox =
if not !init then begin
	db_init ();
	init := true
end;
db_connect localhost postgres postgres netjukebox;;


