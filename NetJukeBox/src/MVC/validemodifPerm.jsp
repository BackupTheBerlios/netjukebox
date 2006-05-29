<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<html>
	<head>
		<title><bean:message key='modifierPerm'/></title>
	</head>
	<body>
	
	<center><h2>MODIFIER LES DONNEES DE LA PERMISSION</h2>
	<html:errors />
	<html:form action="/validemodifPerm">

		<logic:iterate id="perm" name="Resultat">
		
		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.id' /></td>
				<td><html:text name="perm" property="id" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.libelle' /></td>
				<td><html:text name="perm" property="libelle" /></td>
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