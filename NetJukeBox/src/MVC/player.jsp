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

	<jsp:plugin type="applet" code="RTPPlayerApplet.class"
			codebase="/MVC" archive="jmf.jar, sound.jar" >

	<jsp:params>	
		<jsp:param name="audio" value="On" />
		<jsp:param name="audiosession" value='<%=session.getAttribute("audiosession")%>' />   
		<jsp:param name="audioport" value='<%=session.getAttribute("audioport")%>' />
   </jsp:params>	
		
		
		<jsp:fallback>
			Immpossible de charger le Client
    	</jsp:fallback>

	</jsp:plugin>

	
	</center>
	</body>
</html>