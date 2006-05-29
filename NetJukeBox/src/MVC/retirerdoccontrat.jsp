<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='retirerdocumentcontrat'/></title>
	</head>
	<body>
	
	<center><h2>RETIRER UN DOCUMENT D'UN CONTRAT</h2>
	<html:errors />
	<html:form action="/retirerdoccontrat">

		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.iddoc' /></td>
				<td><html:text property="id1" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.idcontrat' /></td>
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