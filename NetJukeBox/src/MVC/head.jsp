<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	
	<body>
	<center>
	<h1>SITE NETJUKEBOX</h1>

	<html:errors />
	<html:form action="/deconnect" target="_parent">
	
	<html:submit value="déconnexion" />
	
	</html:form>
	
	</center>
	</body>
</html>