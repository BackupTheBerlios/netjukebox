<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='rechercheContrat'/></title>
	</head>
	<body>
	
	<center><h2>RECHERCHER CONTRAT</h2>
	<html:errors />
	<html:form action="/rechercheContrat">

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
				<td><bean:message key='attr.joursig' /></td>
				<td><html:text property="joursig" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.moissig' /></td>
				<td><html:text property="moissig" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.anneesig' /></td>
				<td><html:text property="anneesig" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.jourexp' /></td>
				<td><html:text property="jourexp" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.moisexp' /></td>
				<td><html:text property="moisexp" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.anneeexp' /></td>
				<td><html:text property="anneeexp" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.idcontractant' /></td>
				<td><html:text property="idcontractant" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.reg' /></td>
				<td><html:text property="reg" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.type' /></td>
				<td><html:text property="type" /></td>
			</tr>
				<td colspan="2" align="center"><html:submit /></td>
			</tr>
		</table>
	
	</html:form>
	
	</center>
	</body>
</html>