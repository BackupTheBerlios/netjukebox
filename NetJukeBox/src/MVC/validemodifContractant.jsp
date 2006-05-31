<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<html>
	<head>
		<title><bean:message key='modifierContractant'/></title>
	</head>
	<body>
	
	<center><h2>MODIFIER LES DONNEES DU CONTRACTANT</h2>
	<html:errors />
	<html:form action="/validemodifContractant">

		<logic:iterate id="contractant" name="Resultat">

		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.id' /></td>
				<td><html:text name="contractant" property="id" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.nom' /></td>
				<td><html:text name="contractant" property="nom" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.adresse' /></td>
				<td><html:text name="contractant" property="adresse" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.codepostal' /></td>
				<td><html:text name="contractant" property="codePostal" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.ville' /></td>
				<td><html:text name="contractant" property="ville" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.telephone' /></td>
				<td><html:text name="contractant" property="telephone" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.fax' /></td>
				<td><html:text name="contractant" property="fax" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.mail' /></td>
				<td><html:text name="contractant" property="mail" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.type' /></td>
				<td><html:text name="contractant" property="type" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><html:submit /></td>
			</tr>
		</table>
	
		</logic:iterate> 
	
	</html:form>
	
	</center>
	</body>
</html>