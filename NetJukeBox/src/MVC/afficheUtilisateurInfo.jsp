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
				<th><bean:message key = "attr.login" /></th>
				<th><bean:message key = "attr.password" /></th>
				<th><bean:message key = "attr.nom" /></th>
				<th><bean:message key = "attr.prenom" /></th>
				<th><bean:message key = "attr.mail" /></th>
				<th><bean:message key = "attr.pays" /></th>
				<th><bean:message key = "attr.role" /></th>
			</tr>
		<thead>
		
		<tbody>
		<logic:iterate id= "utilisateur" name = "Resultat" length="1">
			<tr>
				<td><bean:write name = "utilisateur" property = "login" /></td>
				<td><bean:write name = "utilisateur" property = "pwd" /></td>
				<td><bean:write name = "utilisateur" property = "nom" /></td>
				<td><bean:write name = "utilisateur" property = "prenom" /></td>
				<td><bean:write name = "utilisateur" property = "mail" /></td>
				<td><bean:write name = "utilisateur" property = "pays" /></td>
				<td><bean:write name = "utilisateur" property = "role" /></td>
			</tr>
		</logic:iterate>
			
		<thead>
			<tr><td colspan="7"><center><b>"Permissions exceptionnelles de l'utilisateur :"</b></center></td>
			</tr>
			<tr>
				<th><bean:message key = "attr.idperm" /></th>
				<th><bean:message key = "attr.libelle" /></th>
			</tr>
		<thead>
		<logic:iterate id= "permutil" name = "PermUtil">
			<tr>
				<td><bean:write name = "permutil" property = "id" /></td>
				<td><bean:write name = "permutil" property = "libelle" /></td>
			</tr>	
		</logic:iterate>		
		
		<thead>
			<tr><td colspan="7"><center><b>"Permissions de l'utilisateur li�es au role :"</b></center></td>
			</tr>
			<tr>
				<th><bean:message key = "attr.idperm" /></th>
				<th><bean:message key = "attr.libelle" /></th>
			</tr>
		<thead>
		<logic:iterate id= "permrole" name = "PermRole">
			<tr>
				<td><bean:write name = "permrole" property = "id" /></td>
				<td><bean:write name = "permrole" property = "libelle" /></td>
			</tr>	
		</logic:iterate>
		
		</tbody>		
	</table>	
	</logic:present>

	</center>
	</body>
</html>