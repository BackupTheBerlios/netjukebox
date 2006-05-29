<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<html>
	<head>
		<title><bean:message key='modifierDoc'/></title>
	</head>
	<body>
	
	<center><h2>MODIFIER LES DONNEES DU DOCUMENT</h2>
	<html:errors />
	<html:form action="/validemodifDoc" method="post">

		<logic:iterate id="doc" name="Resultat">
		
		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.id' /></td>
				<td><html:text name="doc" property="id" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.titre' /></td>
				<td><html:text name="doc" property="titre" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.duree' /></td>
				<td><html:text name="doc" property="duree" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.jour' /></td>
				<td><html:text name="doc" property="jour" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.mois' /></td>
				<td><html:text name="doc" property="mois" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.annee' /></td>
				<td><html:text name="doc" property="annee" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.source' /></td>
				<td><html:text name="doc" property="source" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.langue' /></td>
				<td><html:text name="doc" property="langue" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.genre' /></td>
				<td><html:text name="doc" property="genre" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.fichier' /></td>
				<td><html:file name="doc" property="fichier" /></td>				
			</tr>
			<tr>
				<td><bean:message key='attr.artiste' /></td>
				<td><html:text property="artiste" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.interprete' /></td>
				<td><html:text name="doc" property="interprete" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.compositeur' /></td>
				<td><html:text name="doc" property="compositeur" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><html:submit /></td>
			</tr>
		</table>
		
		</logic:iterate> 
	
	</html:form>
	
	</center>
	</body>
</html>