<%-- 
    Document   : viewAccount
    Created on : May 28, 2015, 1:20:02 AM
    Author     : Bert
--%>
<%@ include file="../fragments/postAuth/taglibs.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="${STATIC_URL}img/cws.ico">
        <title>View Account</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_gen.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_dashboard.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_sticky-footer-navbar.css" rel="stylesheet">
        <script src="${WEB_JARS}jquery/2.0.3/jquery.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <sec:csrfMetaTags/>
    </head>
    <body class="cstm">
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <jsp:include page="../fragments/postAuth/sidebar.jsp"/>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h2 class="sub-header" style="margin-top:15px;">
                <a href="${pageContext.servletContext.contextPath}/admin/accounts"><img src="<c:url value="/resources/img/back.png"/>" style="margin-bottom:5px;" class="">
                </a> View Account
                <a type="button" href="${pageContext.servletContext.contextPath}/admin/accounts/${account.number}/update" class="btn btn-ctm btn-primary" style="float:right;"> Update </a>
            </h2>
            <c:if test="${updateSuccess == 1}">
                <div class="alert alert-success" style="border-radius:5px;"><br>
                    <center><h4>This account is successfully updated!</h4></center>
                </div>
            </c:if>
            <p><img class="un-pw-img" src="<c:url value="/resources/img/un.png" />" style="margin-top:-5px;margin-right:10px;"><strong> <a href="${pageContext.servletContext.contextPath}/admin/customers/${account.customer.id}"> ${account.customer.lastname}, ${account.customer.firstName} (${account.number})</a></strong></p>
            <c:choose >
                <c:when test="${account.getStatus() == 'ACTIVE'}">
                    <c:set var="statusCss" value="label-success"/>
                </c:when>
                <c:otherwise>
                    <c:set var="statusCss" value="label-danger"/>
                </c:otherwise>
            </c:choose>
            <p><img class="un-pw-img" src="<c:url value="/resources/img/status.png"/>" style="margin-top:-5px; margin-right:10px;"><span class="label ${statusCss}" style="font-size:15px;"> ${account.status} </span></p>
            <p><img class="un-pw-img" src="<c:url value="/resources/img/standing_balance.png"/>" style="margin-top:-5px;margin-right:10px;"><strong> Php ${account.accountStandingBalance}</strong></p>
            <p><img class="un-pw-img" src="<c:url value="/resources/img/address.PNG"/>" style="margin-top:-5px;margin-right:10px;"><strong> ${account.address.brgy} , Zone ${account.address.locationCode}</strong></p>
            <p>
                <strong>Devices</strong>
            </p>
            <a id="reload"></a>
            <div class="table-stripedesponsive">
                <datatables:table dom="t" reloadSelector="#reload" cssClass="table table-striped" id="devices" url="${requestScope['javax.servlet.forward.request_uri']}/devices">
                    <datatables:column title="Meter Code" property="meterCode" sortable="false" sortInitDirection="false"/>
                    <datatables:column title="Meter Brand" property="brand" sortable="false" sortInitDirection="false"/>
                    <datatables:column title="Active" property="active" renderFunction="custom-rendering#trueToYes" sortable="false"/>
                    <datatables:column title="Last Reading" property="lastReading" sortable="false"/>
                    <datatables:column title="Action" renderFunction="custom-rendering#activateDevice" sortable="false"/>
                </datatables:table>
            </div>
            <br/>
            <div>
                <p>
                    <strong>Add Device</strong>
                </p>
                <form:form id="add-device" action="${requestScope['javax.servlet.forward.request_uri']}/create-device" modelAttribute="deviceForm">
                     <div id="form-error" class="alert alert-danger" style="border-radius:5px; display: none;"></div>
                    <div id="form-success" class="alert alert-success" style="border-radius:5px; display: none;"></div>
                    <div class="navbar-form">
                        <cws:inputField cssStyle="background-image: url('${STATIC_URL}img/meter_code.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="meterCode" label="Meter Code"/>
                    </div>
                     <div class="navbar-form">
                         <cws:inputField cssStyle="background-image: url('${STATIC_URL}img/meter_code.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="brand" label="Brand"/>
                    </div>
                    <br/>
                    <button type="submit" class="btn btn-ctm btn-primary" > Create Device </button> 
                </form:form>
            </div>
        </div>
        <input id="activate-url" type="hidden" value="${pageContext.servletContext.contextPath}/admin/accounts/activate-device/"/>
        <script src="${STATIC_URL}js/accounts/view.js"></script>
    </body>
    <jsp:include page="../fragments/footer.jsp"/>
</html>
