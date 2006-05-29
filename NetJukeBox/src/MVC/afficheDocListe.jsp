<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<html>
	<head>
		<title><bean:message key='affiche.title'/></title>
	</head>
	<body>
	<center><h2>RESULTAT</h2>
	
	<logic:present name = "Resultat">
	<table border="1">
		<thead>
			<tr>
				<th><bean:message key = "attr.id" /></th>
				<th><bean:message key = "attr.titre" /></th>
				<th><bean:message key = "attr.duree" /></th>
				<th><bean:message key = "attr.date" /></th>
				<th><bean:message key = "attr.genre" /></th>
				<th><bean:message key = "attr.source" /></th>
				<th><bean:message key = "attr.langue" /></th>
				<th><bean:message key = "attr.fichier" /></th>
				<th><bean:message key = "attr.artiste" /></th>
				<th><bean:message key = "attr.interprete" /></th>
				<th><bean:message key = "attr.compositeur" /></th>
			</tr>
		<thead>
		
		<tbody>
		<logic:iterate id= "doc" name = "Resultat">
			<tr>
				<td><bean:write name = "doc" property = "id" /></td>
				<td><bean:write name = "doc" property = "titre" /></td>
				<td><bean:write name = "doc" property = "duree" /></td>
				<td><bean:write name = "doc" property = "date" /></td>
				<td><bean:write name = "doc" property = "genre" /></td>
				<td><bean:write name = "doc" property = "source" /></td>
				<td><bean:write name = "doc" property = "langue" /></td>
				<td><bean:write name = "doc" property = "fichier" /></td>
				<td><bean:write name = "doc" property = "artiste" /></td>
				<td><bean:write name = "doc" property = "interprete" /></td>
				<td><bean:write name = "doc" property = "compositeur" /></td>
			</tr>
		</logic:iterate>

		</tbody>		
	</table>	
	</logic:present>

	</center>
	</body>
</html>