<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='listeContractant'/></title>
	</head>
	<body>
	
	<center><h2>LISTER LES CONTRACTANTS</h2>
	<html:errors />
	<html:form action="/listeContractant">


		<html:submit />

	
	</html:form>
	
	</center>
	</body>
</html>