<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">


<html>

<head>
<title>Transferring to SRCM regapp ...</title>
</head>

<body onload="setTimeout('document.authnetReturnForm.submit();', 1000)">

	<div style="text-align: center; font-size: 1.5em; padding-top: 10em;">
		Processing response from Authorize.Net. Please wait... <br><br>
		<img alt="Loading ..." src="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/images/ajaxloadingbar.gif"><br><br><br>
	</div>
	<form name="authnetReturnForm" action="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/app/payment" method="post">			
		<input type="hidden" name="x_response_code" value="${param.x_response_code}">
		<input type="hidden" name="x_response_reason_text" value="${param.x_response_reason_text}">
		<input type="hidden" name="x_trans_id" value="${param.x_trans_id}">
		<input type="hidden" name="trackingNo" value="${param.trackingNo}">			
	</form>

</body>

</html>