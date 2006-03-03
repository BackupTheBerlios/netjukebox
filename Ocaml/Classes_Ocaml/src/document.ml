class document ((id:string), (titre:string), (date:int), (source:string), (duree:int), (etat:string), (langue:string), (genre:string), (fichier:string)) =
  object
	val mutable id = id
	val mutable titre = titre
	val mutable dateCreation = date
	val mutable source = source
	val mutable duree = duree
	val mutable etat = etat
	val mutable langue = langue
	val mutable genre = genre
	val mutable fichier = fichier
	val mutable verrou = 0
	
	method getId = id
	method getTitre = titre
	method getDateCreation = dateCreation
	method getSource = source
	method getDuree = duree
	method getEtat = etat
	method getLangue = langue
	method getGenre = genre
	method getFichier = fichier
	
	method enLecture = etat = "EN_LECTURE"
	method estProgramme = etat = "EST_PROGRAMME"
	
	method compterVerrouProgramme = verrou
	method poserVerrou = verrou+1
	method enleverVerrou = verrou-1
	
	(*
	method getProgrammesArchives =
	method ajouterProgramme(idProg) =
	method enleverProgramme(idProg) =
	method modifier() =
	method supprimer =
	method nommerFichier(fichier) =
	*)
  end;;