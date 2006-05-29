<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>

	<head>
		<title><bean:message key='index.title'/></title>
	</head>

	<body>

	<center><h2>BIENVENUE SUR LE NETJUKEBOX</h2><br /><br />

	<html:errors />

	<html:form action="/login">

		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.login' /></td>
				<td><html:text property="login" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.password' /></td>
				<td><html:password property="pass" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><html:submit /></td>
			</tr>
		</table>
	</html:form>

	<a href="addUser.jsp"><bean:message key='Inscription'/></a><br /><br />
	<a href="pwdPerdu.jsp"><bean:message key='pwdPerdu'/>
	
	</center>
	</body>

</html>