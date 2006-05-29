<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<html>
	<head>
		<title><bean:message key='modifierCanal'/></title>
	</head>
	<body>
	
	<center><h2>MODIFIER LES DONNEES DU CANAL</h2>
	<html:errors />
	<html:form action="/validemodifCanal">

		<logic:iterate id="vCanaux" name="Resultat">
		
		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.id' /></td>
				<td><html:text name="vCanaux" property="id" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.nom' /></td>
				<td><html:text name="vCanaux" property="nom" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.nbmaxutil' /></td>
				<td><html:text name="vCanaux" property="utilMax" /></td>
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