<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	
	<body>
	
	<center><h1>OK</h1>
	<br /><br />
	
	<logic:present name = "Resultat" scope = "session">
	
		<bean:write name = "Resultat" scope = "session" />

	</logic:present>
	
	</center>
	</body>
</html>