<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='supprContrat'/></title>
	</head>
	<body>
	
	<center><h2>SUPPRIMER CONTRAT</h2>
	<html:errors />
	<html:form action="/supprContrat">

		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.id' /></td>
				<td><html:text property="id" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><html:submit /></td>
			</tr>
		</table>
	
	</html:form>
	
	</center>
	</body>
</html>