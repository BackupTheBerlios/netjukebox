<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='listeDoc'/></title>
	</head>
	<body>
	
	<center><h2>LISTER LES DOCUMENTS</h2>
	<html:errors />
	<html:form action="/listeDoc">


		<html:submit />

	
	</html:form>
	
	</center>
	</body>
</html>