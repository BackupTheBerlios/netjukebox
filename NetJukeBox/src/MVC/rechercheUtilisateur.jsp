<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='rechercheUtil'/></title>
	</head>
	<body>
	
	<center><h2>RECHERCHER UTILISATEUR</h2>
	<html:errors />
	<html:form action="/rechercheUtilisateur">

		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.login' /></td>
				<td><html:text property="login" /></td>
			</tr>
			
				<td colspan="2" align="center"><html:submit /></td>
			</tr>
		</table>
	
	</html:form>
	
	</center>
	</body>
</html>