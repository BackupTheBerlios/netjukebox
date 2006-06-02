<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='player'/></title>
	</head>
	<body>
	<center>

	<h2>Net-JukeBox : RTP Client</h2>
	<br> <br>

	<jsp:plugin type="applet" codebase="/MVC" code="HelloWorldApplet">

		<jsp:params>
			<jsp:param name="audio" value="On" />
			<jsp:param name="audiosession" value="55.24.1.51" />
			<jsp:param name="audioport" value="58502" />
		</jsp:params>

		<jsp:fallback>
			Immpossible de charger le Client
    	</jsp:fallback>

	</jsp:plugin>

	</center>
	</body>
</html>