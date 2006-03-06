class document ((id:string), (titre:string), (date:string), (source:string), (duree:int), (langue:string), (genre:string), (fichier:string)) =
  object
	val mutable id = id
	val mutable titre = titre
	val mutable dateCreation = date
	val mutable source = source
	val mutable duree = duree
	val mutable etat = "NON_DIFFUSE"
	val mutable langue = langue
	val mutable genre = genre
	val mutable fichier = fichier
	val mutable verrou = 0
	(*val programmes = Array.create*)
	val mutable programmes = []
	
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
	
	method compterVerrou = verrou
	method poserVerrou = verrou+1
	method enleverVerrou = verrou-1
	
	method ajouterProgramme(idProg) = List.concat programmes [idProg]
	(*method enleverProgramme(idProg) = Array.append idProg*)
	
	(*
	method getProgrammesArchives =
	method modifier() =
	method supprimer =
	method nommerFichier(fichier) =
	*)
end;;