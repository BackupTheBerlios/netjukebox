<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
	<head>
		<title><bean:message key='sommaire.title'/></title>
	</head>
	
	
	<FRAMESET ROWS="11%,80%">

	<FRAME SRC="head.jsp" NAME="haut">

	<FRAMESET COLS="20%,80%"> 
	<FRAME SRC="sommaire.jsp" NAME="gauche">
	<FRAME NAME="droite">

	</FRAMESET> 

</html>