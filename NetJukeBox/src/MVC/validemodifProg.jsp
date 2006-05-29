<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<html>
	<head>
		<title><bean:message key='modifierProg'/></title>
	</head>
	<body>
	
	<center><h2>MODIFIER LES DONNEES DU PROGRAMME</h2>
	<html:errors />
	<html:form action="/validemodifProg">

		<logic:iterate id="vProg" name="Resultat">
		
		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.id' /></td>
				<td><html:text name="vProg" property="id" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.titre' /></td>
				<td><html:text name="vProg" property="titre" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.thematique' /></td>
				<td><html:text name="vProg" property="thematique" /></td>
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