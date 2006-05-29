<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='planifprog'/></title>
	</head>
	<body>
	
	<center><h2>PLANNIFIER UN PROGRAMME</h2>
	<html:errors />
	<html:form action="/planifprog">

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
				<td><bean:message key='attr.jour' /></td>
				<td><html:text property="jour" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.mois' /></td>
				<td><html:text property="mois" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.annee' /></td>
				<td><html:text property="annee" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.heure' /></td>
				<td><html:text property="heure" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.minute' /></td>
				<td><html:text property="minute" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.seconde' /></td>
				<td><html:text property="seconde" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><html:submit /></td>
			</tr>
		</table>
	
	</html:form>
	
	</center>
	</body>
</html>