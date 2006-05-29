<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='addContractant'/></title>
	</head>
	<body>
	
	<center><h2>CREER CONTRACTANT</h2>
	<html:errors />
	<html:form action="/newContractant">

		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.nom' /></td>
				<td><html:text property="nom" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.adresse' /></td>
				<td><html:text property="adresse" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.codepostal' /></td>
				<td><html:text property="codepostal" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.ville' /></td>
				<td><html:text property="ville" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.telephone' /></td>
				<td><html:text property="telephone" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.fax' /></td>
				<td><html:text property="fax" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.mail' /></td>
				<td><html:text property="mail" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.type' /></td>
				<td><html:text property="type" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><html:submit /></td>
			</tr>
		</table>
	
	</html:form>
	
	</center>
	</body>
</html>