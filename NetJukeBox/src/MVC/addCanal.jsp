<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='addCanal'/></title>
	</head>
	<body>
	
	<center><h2>CREER CANAL</h2>
	<html:errors />
	<html:form action="/newCanal">

		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.nom' /></td>
				<td><html:text property="nom" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.nbmaxutil' /></td>
				<td><html:text property="nbmaxutil" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><html:submit /></td>
			</tr>
		</table>
	
	</html:form>
	
	</center>
	</body>
</html>