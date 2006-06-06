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
				<th><bean:message key = "attr.libelle" /></th>
				<th><bean:message key = "attr.id" /></th>
			</tr>
		<thead>
		
		<tbody>
		<logic:iterate id="role" name = "Resultat">
			<tr>
				<td><bean:write name = "role" property = "libelle" /></td>
				<td><bean:write name = "role" property = "id" /></td>
			</tr>
		</logic:iterate>			
		</tbody>
	</table>
	</logic:present>

	</center>
	</body>
</html>