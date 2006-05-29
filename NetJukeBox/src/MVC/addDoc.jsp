<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='addDoc'/></title>
	</head>
	<body>
	
	<center><h2>CREER DOCUMENT</h2>
	<html:errors />
	<html:form action="/newDoc" method="post" enctype="multipart/form-data">

		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.titre' /></td>
				<td><html:text property="titre" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.duree' /></td>
				<td><html:text property="duree" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.jour' /></td>
				<td><html:text property="jour" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.mois' /></td>
				<td><html:text property="mois" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.annee' /></td>
				<td><html:text property="annee" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.source' /></td>
				<td><html:text property="source" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.langue' /></td>
				<td><html:text property="langue" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.genre' /></td>
				<td><html:text property="genre" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.fichier' /></td>
				<td><html:file property="fichier" /></td>				
			</tr>
			<tr>
				<td><bean:message key='attr.artiste' /></td>
				<td><html:text property="artiste" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.interprete' /></td>
				<td><html:text property="interprete" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.compositeur' /></td>
				<td><html:text property="compositeur" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><html:submit /></td>
			</tr>
		</table>
	
	</html:form>
	
	</center>
	</body>
</html>