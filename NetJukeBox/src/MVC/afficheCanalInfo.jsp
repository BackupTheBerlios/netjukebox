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
				<th><bean:message key = "attr.nbmaxutil" /></th>
				<th><bean:message key = "attr.nbprog" /></th>
			</tr>
		<thead>
		
		<tbody>
		<logic:iterate id= "canal" name = "Resultat">
			<tr>
				<td><bean:write name = "canal" property = "id" /></td>
				<td><bean:write name = "canal" property = "nom" /></td>
				<td><bean:write name = "canal" property = "utilMax" /></td>
				<td><bean:write name = "canal" property = "nbProgs" /></td>
			</tr>
		</logic:iterate>
			
		<thead>
			<tr><td colspan="5"><center><b>"Programmes programmés :"</b></center></td>
			</tr>
			<tr>
				<th><bean:message key = "attr.id" /></th>
				<th><bean:message key = "attr.titre" /></th>
				<th><bean:message key = "attr.duree" /></th>
				<th><bean:message key = "attr.horaire" /></th>
				<th><bean:message key = "attr.calage" /></th>
			</tr>
		<thead>
		<logic:iterate id= "prog" name = "Programmes">
			<tr>
				<td><bean:write name = "prog" property = "id" /></td>
				<td><bean:write name = "prog" property = "titre" /></td>
				<td><bean:write name = "prog" property = "duree" /></td>
				<td><bean:write name = "prog" property = "horaire" /></td>
				<td><bean:write name = "prog" property = "calage" /></td>
			</tr>	
		</logic:iterate>		
		
		</tbody>		
	</table>	
	</logic:present>

	</center>
	</body>
</html>