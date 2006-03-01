class document (idD, titreD, dateC, sourceD, dureeD, etatD, langueD, genreD, fichierD) =
  object
	val mutable id = idD
	val mutable titre = titreD
	val mutable dateCreation = dateC
	val mutable source = sourceD
	val mutable duree = dureeD
	val mutable etat = etatD
	val mutable langue = langueD
	val mutable genre = genreD
	val mutable fichier = fichierD
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
	
	method enLecture = etat = 'EN_LECTURE'
	method estProgramme = etat = 'EST_PROGRAMME'
	
	method compterVerrouProgramme = verrou
	method poserVerrou = verrou+1 <- verrou
	method enleverVerrou = verrou-1 <- verrou
	
	(*
	method getProgrammesArchives =
	method ajouterProgramme(idProg) =
	method enleverProgramme(idProg) =
	method modifier() =
	method supprimer =
	method nommerFichier(fichier) =
	*)
  end;;