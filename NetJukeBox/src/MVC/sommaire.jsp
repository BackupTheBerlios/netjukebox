<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='sommaire.title'/></title>
	</head>
	<body>
	<center><h2>SOMMAIRE</h2><br /></center>
	<html:errors />

	<b>AJOUTS :</b><br>
	<a href="addUser.jsp" target="droite"><bean:message key='addUser'/></a><br />
	<a href="addDoc.jsp" target="droite"><bean:message key='addDoc'/></a><br />
	<a href="addProg.jsp" target="droite"><bean:message key='addProg'/></a><br />
	<a href="addCanal.jsp" target="droite"><bean:message key='addCanal'/></a><br />
	<a href="addContrat.jsp" target="droite"><bean:message key='addContrat'/></a><br />
	<a href="addContractant.jsp" target="droite"><bean:message key='addContractant'/></a><br />
	<a href="addPerm.jsp" target="droite"><bean:message key='addPerm'/></a><br />
	<a href="addRole.jsp" target="droite"><bean:message key='addRole'/></a><br />
	
	<b>SUPPRESSIONS :</b><br>
	<a href="supprUtil.jsp" target="droite"><bean:message key='supprUtil'/></a><br />
	<a href="supprCanal.jsp" target="droite"><bean:message key='supprCanal'/></a><br />
	<a href="supprProg.jsp" target="droite"><bean:message key='supprProg'/></a><br />
	<a href="supprDoc.jsp" target="droite"><bean:message key='supprDoc'/></a><br />
	<a href="supprContrat.jsp" target="droite"><bean:message key='supprContrat'/></a><br />
	<a href="supprContractant.jsp" target="droite"><bean:message key='supprContractant'/></a><br />
	<a href="supprPerm.jsp" target="droite"><bean:message key='supprPerm'/></a><br />
	<a href="supprRole.jsp" target="droite"><bean:message key='supprRole'/></a><br />

	<b>RECHERCHE :</b><br>
	<a href="rechercheDoc.jsp" target="droite"><bean:message key='rechercheDoc'/></a><br />
	<a href="rechercheProg.jsp" target="droite"><bean:message key='rechercheProg'/></a><br />
	<a href="rechercheCanal.jsp" target="droite"><bean:message key='rechercheCanal'/></a><br />
	<a href="rechercheUtilisateur.jsp" target="droite"><bean:message key='rechercheUtil'/></a><br />
	<a href="rechercheContrat.jsp" target="droite"><bean:message key='rechercheContrat'/></a><br />
	<a href="rechercheContractant.jsp" target="droite"><bean:message key='rechercheContractant'/></a><br />
	<a href="recherchePerm.jsp" target="droite"><bean:message key='recherchePerm'/></a><br />
	
	<b>INFOS :</b><br>
	<a href="infoDoc.jsp" target="droite"><bean:message key='infoDoc'/></a><br />
	<a href="infoProg.jsp" target="droite"><bean:message key='infoProg'/></a><br />
	<a href="infoCanal.jsp" target="droite"><bean:message key='infoCanal'/></a><br />
	<a href="infoUtil.jsp" target="droite"><bean:message key='infoUtil'/></a><br />
	<a href="infoContrat.jsp" target="droite"><bean:message key='infoContrat'/></a><br />
	<a href="infoContractant.jsp" target="droite"><bean:message key='infoContractant'/></a><br />
	<a href="infoPerm.jsp" target="droite"><bean:message key='infoPerm'/></a><br />
	<a href="infoRole.jsp" target="droite"><bean:message key='infoRole'/></a><br />
	
	<b>LISTE :</b><br>
	<a href="listeDoc.jsp" target="droite"><bean:message key='listeDoc'/></a><br />
	<a href="listeProg.jsp" target="droite"><bean:message key='listeProg'/></a><br />
	<a href="listeCanal.jsp" target="droite"><bean:message key='listeCanal'/></a><br />
	<a href="listeUtil.jsp" target="droite"><bean:message key='listeUtil'/></a><br />
	<a href="listeContrat.jsp" target="droite"><bean:message key='listeContrat'/></a><br />
	<a href="listeContractant.jsp" target="droite"><bean:message key='listeContractant'/></a><br />
	<a href="listePerm.jsp" target="droite"><bean:message key='listePerm'/></a><br />
	<a href="listeRole.jsp" target="droite"><bean:message key='listeRole'/></a><br />
	
	<b>MODIFIER :</b><br>
	<a href="modifierDoc.jsp" target="droite"><bean:message key='modifierDoc'/></a><br />
	<a href="modifierProg.jsp" target="droite"><bean:message key='modifierProg'/></a><br />
	<a href="modifierCanal.jsp" target="droite"><bean:message key='modifierCanal'/></a><br />
	<a href="modifierUtil.jsp" target="droite"><bean:message key='modifierUtilisateur'/></a><br />
	<a href="modifierContrat.jsp" target="droite"><bean:message key='modifierContrat'/></a><br />
	<a href="modifierContractant.jsp" target="droite"><bean:message key='modifierContractant'/></a><br />
	<a href="modifierPerm.jsp" target="droite"><bean:message key='modifierPerm'/></a><br />

	<b>PLANIFIER / DEPLANIFIER :</b><br>
	<a href="planifprog.jsp" target="droite"><bean:message key='planifprog'/></a><br />
	<a href="deplanifprog.jsp" target="droite"><bean:message key='deplanifprog'/></a><br />

	<b>DIFFUSER :</b><br>
	<a href="difprog.jsp" target="droite"><bean:message key='difprog'/></a><br />
	
	<b>START STOP CANAL :</b><br>
	<a href="startcanal.jsp" target="droite"><bean:message key='startcanal'/></a><br />
	<a href="stopcanal.jsp" target="droite"><bean:message key='stopcanal'/></a><br />
	
	<b>AJOUT SUPPRESSION DOCUMENT PROGRAMME :</b><br>
	<a href="ajouterdocprog.jsp" target="droite"><bean:message key='ajouterdocprog'/></a><br />
	<a href="retirerdocprog.jsp" target="droite"><bean:message key='retirerdocprog'/></a><br />
	
	<b>AJOUT SUPPRESSION CONTRAT DOCUMENT :</b><br>
	<a href="ajouterdoccontrat.jsp" target="droite"><bean:message key='ajoutdocumentcontrat'/></a><br />
	<a href="retirerdoccontrat.jsp" target="droite"><bean:message key='retirerdocumentcontrat'/></a><br />
	
	<b>AJOUT SUPPRESSION PERMISSIONS ROLE :</b><br>
	<a href="ajouterpermrole.jsp" target="droite"><bean:message key='ajoutpermissionrole'/></a><br />
	<a href="retirerpermrole.jsp" target="droite"><bean:message key='retirerpermissionrole'/></a><br />
	
	<b>AJOUT SUPPRESSION PERMISSIONS UTILISATEUR :</b><br>
	<a href="ajouterpermutil.jsp" target="droite"><bean:message key='ajoutpermissionutilisateur'/></a><br />
	<a href="retirerpermutil.jsp" target="droite"><bean:message key='retirerpermissionutilisateur'/></a><br />
	
	<b>CHANGER ROLE D'UN UTILISATEUR :</b><br>
	<a href="changerroleutil.jsp" target="droite"><bean:message key='changerroleutilisateur'/></a><br />
	
	<b>START STOP PLAYER :</b><br>
	<a href="startplayer.jsp" target="droite"><bean:message key='startplayer'/></a><br />
	<a href="stopplayer.jsp" target="droite"><bean:message key='stopplayer'/></a><br />
	
	</body>
</html>