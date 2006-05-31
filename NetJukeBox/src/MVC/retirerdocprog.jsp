<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='retirerdocprog'/></title>
	</head>
	<body>
	
	<center><h2>RETIRER UN DOCUMENT AU PROGRAMME</h2>
	<html:errors />
	<html:form action="/retirerdocprog">

		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.doccaler' /></td>
				<td><html:text property="id1" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.idprog' /></td>
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