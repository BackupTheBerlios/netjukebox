<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<html>
	<head>
		<title><bean:message key='modifierUtilisateur'/></title>
	</head>
	<body>
	
	<center><h2>MODIFIER LES DONNEES DE L'UTILISATEUR</h2>
	<html:errors />
	<html:form action="/validemodifUtil">

		<logic:iterate id="vUtil" name="Resultat" length = "1">
		
		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.login' /></td>
				<td><html:text name="vUtil" property="login" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.nom' /></td>
				<td><html:text name="vUtil" property="nom" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.prenom' /></td>
				<td><html:text name="vUtil" property="prenom" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.mail' /></td>
				<td><html:text name="vUtil" property="mail" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.pays' /></td>
				<td><html:text name="vUtil" property="pays" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.password' /></td>
				<td><html:text name="vUtil" property="pwd" /></td>
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