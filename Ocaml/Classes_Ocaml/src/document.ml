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
	
	method getId = id
	method getTitre = titre
	method getDateCreation = dateCreation
	method getSource = source
	method getDuree = duree
	method getEtat = etat
	method getLangue = langue
	method getGenre = genre
	method getFichier = fichier
	
	method modifier () = 
  end;;