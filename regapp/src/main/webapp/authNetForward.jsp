<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">


<html>

<head>
<title>Transferring to authorize.net ...</title>
</head>

<body onload="setTimeout('document.authnetForm.submit();', 3 * 1000)">

	<div style="text-align: center; font-size: 1.5em; padding-top: 10em;">
		Transferring to Authorize.Net. Please wait... <br><br>
		<img alt="Loading ..." src="<%=request.getContextPath() %>/images/ajaxloadingbar.gif"><br><br><br>
	</div>
	<form name="authnetForm" action="${authNetBean.gatewayUrl}" method="post">

		<input type="hidden" name="x_login" value="${authNetBean.loginID}">
		<input type="hidden" name="x_amount" value="${registration.amountToPay}">
		<input type="hidden" name="x_currency_code" value="${authNetBean.currency}">
		<input type="hidden" name="x_description" value="Voluntary Donation for ${selectedSeminar.title} :Tracking Number -  ${registration.seminarRegistrantId} ">
		<input type="hidden" name="x_invoice_num" value="${selectedSeminar.seminarId}-${registration.seminarRegistrantId}">
		<input type="hidden" name="x_fp_sequence" value="${authNetBean.sequence}">
		<input type="hidden" name="x_fp_timestamp" value="${authNetBean.timeStamp}">
		<input type="hidden" name="x_fp_hash" value="${authNetBean.fingerPrint}">
		<input type="hidden" name="x_test_request" value="${authNetBean.testMode}">
		<input type="hidden" name="x_show_form" value="PAYMENT_FORM">
		<input type="hidden" name="x_relay_response" value="true">
		<input type="hidden" name="x_relay_url" value="${authNetBean.returnUrl}">

		<input type="hidden" name="x_First_Name" value="${registration.firstName}">
		<input type="hidden" name="x_Last_Name" value="${registration.lastName}">
		<input type="hidden" name="x_Address" value="${registration.ccStreetAddress}">
		<input type="hidden" name="x_City" value="${registration.ccCityDetail}">
		<input type="hidden" name="x_State" value="${registration.ccState}">
		<input type="hidden" name="x_Zip" value="${registration.ccZip}">
		<input type="hidden" name="x_Country" value="${registration.ccCountry}">
		<input type="hidden" name="x_Email" value="${registration.email}">

		<input type="hidden" name="x_Ship_To_First_Name" value="${registration.firstName}">
		<input type="hidden" name="x_Ship_To_Last_Name" value="${registration.lastName}">
		<input type="hidden" name="x_Ship_To_Address" value="${registration.ccStreetAddress}">
		<input type="hidden" name="x_Ship_To_City" value="${registration.ccCityDetail}">
		<input type="hidden" name="x_Ship_To_State" value="${registration.ccState}">
		<input type="hidden" name="x_Ship_To_Zip" value="${registration.ccZip}">
		<input type="hidden" name="x_Ship_To_Country" value="${registration.ccCountry}">
		
	</form>

</body>

</html>