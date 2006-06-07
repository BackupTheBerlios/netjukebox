<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='changerroleutilisateur'/></title>
	</head>
	<body>
	
	<center><h2>CHANGER LE ROLE D'UN UTILISATEUR</h2>
	<html:errors />
	<html:form action="/changerroleutil">

		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.idrole' /></td>
				<td><html:text property="id1" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.login' /></td>
				<td><html:text property="id2" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><html:submit /></td>
			</tr>
		</table>
	
	</html:form>
	
	</center>
	</body>
</html>