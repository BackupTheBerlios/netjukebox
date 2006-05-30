<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<html>
	<head>
		<title><bean:message key='affiche.title'/></title>
	</head>
	<body>
	<center><h2>RESULTAT</h2>
	
	<logic:notPresent name = "Resultat">
	NON PRESENT
	</logic:notPresent>
	
	<logic:present name = "Resultat">
		<logic:empty name = "Resultat">
		PAS DE DONNEES
		</logic:empty>
	</logic:present>
	
	<logic:present name = "Resultat">
	<table border="1">
		<thead>
			<tr>
				<th><bean:message key = "attr.id" /></th>
				<th><bean:message key = "attr.nom" /></th>
				<th><bean:message key = "attr.adresse" /></th>
				<th><bean:message key = "attr.codepostal" /></th>
				<th><bean:message key = "attr.ville" /></th>
				<th><bean:message key = "attr.telephone" /></th>
				<th><bean:message key = "attr.fax" /></th>
				<th><bean:message key = "attr.mail" /></th>
				<th><bean:message key = "attr.type" /></th>
				</tr>
		<thead>
		
		<tbody>
		<logic:iterate id="contractant" name = "Resultat">
			<tr>
				<td><bean:write name = "contractant" property = "id" /></td>
				<td><bean:write name = "contractant" property = "nom" /></td>
				<td><bean:write name = "contractant" property = "adresse" /></td>
				<td><bean:write name = "contractant" property = "codePostal" /></td>
				<td><bean:write name = "contractant" property = "ville" /></td>
				<td><bean:write name = "contractant" property = "telephone" /></td>
				<td><bean:write name = "contractant" property = "fax" /></td>
				<td><bean:write name = "contractant" property = "mail" /></td>
				<td><bean:write name = "contractant" property = "type" /></td>
			</tr>
		</logic:iterate>			
		</tbody>
	</table>
	</logic:present>

	</center>
	</body>
</html>