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
				<th><bean:message key = "attr.libelle" /></th>
			</tr>
		<thead>
		
		<tbody>
		<logic:iterate id="perm" name = "Resultat">
			<tr>
				<td><bean:write name = "perm" property = "id" /></td>
				<td><bean:write name = "perm" property = "libelle" /></td>
				</tr>
		</logic:iterate>			
		</tbody>
	</table>
	</logic:present>

	</center>
	</body>
</html>