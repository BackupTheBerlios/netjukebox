<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<html>
	
	<body>
	
	<center><h1>ERREUR</h1>
	<br /><br />
	
	<logic:present name = "Resultat" scope = "session">
	
		<bean:write name = "Resultat" scope = "session" />

	</logic:present>
	
	</center>
	
	</body>
</html>