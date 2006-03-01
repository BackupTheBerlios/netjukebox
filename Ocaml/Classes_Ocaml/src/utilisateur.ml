class utilisateur ((login:string), (nom:string),(prenom:string),(mail:string),(pwd:string),(pays:string)) =
object
val login = login
val nom = nom
val prenom = prenom
val mail = mail
val pwd = pwd
val pays = pays
   
method get_login = login
method get_nom = nom
method get_prenom = prenom
method get_mail = mail
method get_login = login
method get_pwd = pwd
method get_pays = pays
(*
method virtual deconnecter = bool
method connecter = bool
method connection = 0
method connecter=(login,pwd)=bool
method estconnecte= bool
method verifierpwd(pwd,login)= bool
method inscrire(nom,prenom,mail,pwd,pays)= bool
method inscription(nom,prenom,mail,pwd,pays)= bool
method supprimerinfo = 0
method modifierinfo(login) = 0
method supprimerutilisateur(login)= 0
method selectionner = 0
method verifconnecte = 0
method suprimer = 0
method modifier(nom,prenom,mail,pwd,pays)= bool
method majinfos(nom,prenom,mail,pwd,pays)= bool
*)

end ;;

