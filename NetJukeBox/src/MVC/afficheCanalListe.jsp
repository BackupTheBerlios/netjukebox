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
				<th><bean:message key = "attr.nom" /></th>
				<th><bean:message key = "attr.nbmaxutil" /></th>
			</tr>
		<thead>
		
		<tbody>
		<logic:iterate id= "canal" name = "Resultat">
			<tr>
				<td><bean:write name = "canal" property = "id" /></td>
				<td><bean:write name = "canal" property = "nom" /></td>
				<td><bean:write name = "canal" property = "utilMax" /></td>
			</tr>
		</logic:iterate>

		</tbody>		
	</table>	
	</logic:present>

	</center>
	</body>
</html>