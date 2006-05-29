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
				<th><bean:message key = "attr.thematique" /></th>
				<th><bean:message key = "attr.duree" /></th>
				<th><bean:message key = "attr.nbdoc" /></th>
			</tr>
		<thead>
		
		<tbody>
		<logic:iterate id= "prog" name = "Resultat">
			<tr>
				<td><bean:write name = "prog" property = "id" /></td>
				<td><bean:write name = "prog" property = "titre" /></td>
				<td><bean:write name = "prog" property = "thematique" /></td>
				<td><bean:write name = "prog" property = "duree" /></td>
				<td><bean:write name = "prog" property = "nbDocs" /></td>
			</tr>
		</logic:iterate>

		</tbody>		
	</table>	
	</logic:present>

	</center>
	</body>
</html>