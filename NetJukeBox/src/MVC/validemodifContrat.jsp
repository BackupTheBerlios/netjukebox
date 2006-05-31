<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<html>
	<head>
		<title><bean:message key='modifierContrat'/></title>
	</head>
	<body>
	
	<center><h2>MODIFIER LES DONNEES DU CONTRAT</h2>
	<html:errors />
	<html:form action="/validemodifContrat">

		<logic:iterate id="contrat" name="Resultat">
		
		<table width="45%" border="0">
			<tr>
				<td><bean:message key='attr.id' /></td>
				<td><html:text name="contrat" property="id" /></td>
			</tr>			
			<tr>
				<td><bean:message key='attr.titre' /></td>
				<td><html:text name="contrat" property="titre" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.joursig' /></td>
				<td><html:text name="contrat" property="joursig" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.moissig' /></td>
				<td><html:text name="contrat" property="moissig" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.anneesig' /></td>
				<td><html:text name="contrat" property="anneesig" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.jourexp' /></td>
				<td><html:text name="contrat" property="jourexp" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.moisexp' /></td>
				<td><html:text name="contrat" property="moisexp" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.anneeexp' /></td>
				<td><html:text name="contrat" property="anneeexp" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.idcontractant' /></td>
				<td><html:text name="contrat" property="idcontractant" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.reg' /></td>
				<td><html:text name="contrat" property="reg" /></td>
			</tr>
			<tr>
				<td><bean:message key='attr.type' /></td>
				<td><html:text name="contrat" property="type" /></td>
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