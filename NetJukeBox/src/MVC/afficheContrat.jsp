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
				<th><bean:message key = "attr.titre" /></th>
				<th><bean:message key = "attr.joursig" /></th>
				<th><bean:message key = "attr.moissig" /></th>
				<th><bean:message key = "attr.anneesig" /></th>
				<th><bean:message key = "attr.jourexp" /></th>
				<th><bean:message key = "attr.moisexp" /></th>
				<th><bean:message key = "attr.anneeexp" /></th>
				<th><bean:message key = "attr.idcontractant" /></th>
				<th><bean:message key = "attr.reg" /></th>
				<th><bean:message key = "attr.type" /></th>
			</tr>
		<thead>
		
		<tbody>
		<logic:iterate id="contrat" name = "Resultat">
			<tr>
				<td><bean:write name = "contrat" property = "id" /></td>
				<td><bean:write name = "contrat" property = "titre" /></td>
				<td><bean:write name = "contrat" property = "joursig" /></td>
				<td><bean:write name = "contrat" property = "moissig" /></td>
				<td><bean:write name = "contrat" property = "anneesig" /></td>
				<td><bean:write name = "contrat" property = "jourexp" /></td>
				<td><bean:write name = "contrat" property = "moisexp" /></td>
				<td><bean:write name = "contrat" property = "anneeexp" /></td>
				<td><bean:write name = "contrat" property = "idcontractant" /></td>
				<td><bean:write name = "contrat" property = "reg" /></td>
				<td><bean:write name = "contrat" property = "type" /></td>
			</tr>
		</logic:iterate>			
		</tbody>
	</table>
	</logic:present>

	</center>
	</body>
</html>