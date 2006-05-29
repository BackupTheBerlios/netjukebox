<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='addUser.title'/></title>
	</head>
	<body>
	
	<center><h2>INSCRIPTION AU NETJUKEBOX</h2>
	<html:errors />
	<html:form action="/newUser">

		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.login' /></td>
				<td><html:text property="login" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.password' /></td>
				<td><html:text property="pass" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.nom' /></td>
				<td><html:text property="nom" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.prenom' /></td>
				<td><html:text property="prenom" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.mail' /></td>
				<td><html:text property="mail" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.pays' /></td>
				<td><html:text property="pays" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><html:submit /></td>
			</tr>
		</table>
	
	</html:form>
	
	</center>
	</body>
</html>