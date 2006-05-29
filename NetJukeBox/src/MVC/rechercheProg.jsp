<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='rechercheProg'/></title>
	</head>
	<body>
	
	<center><h2>RECHERCHER PROGRAMME</h2>
	<html:errors />
	<html:form action="/rechercheProg">

		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.id' /></td>
				<td><html:text property="id" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.titre' /></td>
				<td><html:text property="titre" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.thematique' /></td>
				<td><html:text property="thematique" /></td>
			</tr>
				<td colspan="2" align="center"><html:submit /></td>
			</tr>
		</table>
	
	</html:form>
	
	</center>
	</body>
</html>