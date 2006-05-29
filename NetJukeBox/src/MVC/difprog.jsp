<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='difprog'/></title>
	</head>
	<body>
	
	<center><h2>DIFFUSER UN PROGRAMME</h2>
	<html:errors />
	<html:form action="/difprog">

		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.idprog' /></td>
				<td><html:text property="idprog" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.idcanal' /></td>
				<td><html:text property="idcanal" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><html:submit /></td>
			</tr>
		</table>
	
	</html:form>
	
	</center>
	</body>
</html>