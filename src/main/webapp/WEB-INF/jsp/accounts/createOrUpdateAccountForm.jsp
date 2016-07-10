<%-- 
    Document   : createOrUpdateAccount
    Created on : May 9, 2015, 7:45:26 PM
    Author     : Bert
--%>
<%@ include file="../fragments/postAuth/taglibs.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="icon" href="${STATIC_URL}img/cws.ico">
        <title>${accountForm.account.id == null ? 'Create' : 'Update' } Account</title>
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
            <c:set var="AddOrUpdate" value="${accountForm.account.id == null ? 'Add' : 'Update' }"/>
            <c:choose >
                <c:when test="${accountForm.account.id == null}">
                    <c:set var="AddOrUpdate" value="Add"/>
                    <c:url var="backUrl" value="/admin/accounts"/>                    
                </c:when>
                <c:otherwise>
                    <c:set var="AddOrUpdate" value="Update"/>
                    <spring:url var="backUrl" value="/admin/accounts/{accountId}">
                        <spring:param name="accountId" value="${accountForm.account.id}"/>
                    </spring:url>
                </c:otherwise>
            </c:choose>
            <h2 class="sub-header" style="margin-top:15px;"><a type="button" href="${backUrl}"><img src="${STATIC_URL}img/back.png"  style="margin-bottom:5px;" class=""></a> ${AddOrUpdate} Account</h2>
            <form:form modelAttribute="accountForm" method="post" id="add-customer-form">
                <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.accountForm'].allErrors}">
                   <div class="alert alert-danger" style="border-radius:5px;"><br>
                       <h4>Oh snap! You got an error!</h4><hr>
                       <form:errors path="*" cssClass="error" />
                   </div>
                </c:if>
                <div class="table-stripedesponsive">
                    <h3>${customer.lastname}, ${customer.firstName}</h3>
                    <div class="navbar-form" >
                        <cws:inputField cssStyle="background-image: url('${STATIC_URL}img/meter_code.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="device.meterCode" label="Meter Code"/>
                    </div>
                    <div class="navbar-form" >
                        <cws:inputField cssStyle="background-image: url('${STATIC_URL}img/meter_code.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="device.brand" label="Brand"/>
                    </div>
                    <div class="navbar-form" >
                        <form:select path="address.brgy" cssClass="form-control frm-ent-accnt-nmb log-un" cssStyle="background-image: url('${STATIC_URL}img/address.PNG');background-size: contain; background-repeat:no-repeat; padding-left: 45px;">
                            <form:option value="" label="Select Barangay"/>
                            <form:option value="Tugas" label="Tugas"/>
                            <form:option value="Poblacion" label="Poblacion"/>
                        </form:select>
                    </div>
                    <div class="navbar-form" >
                        <form:select path="address.locationCode" cssClass="form-control frm-ent-accnt-nmb log-un" cssStyle="background-image: url('${STATIC_URL}img/address.PNG');background-size: contain; background-repeat:no-repeat; padding-left: 45px;">
                            <form:option value="" label="Select Zone"/>
                            <form:option value="1" label="1"/>
                            <form:option value="2" label="2"/>
                            <form:option value="3" label="3"/>
                            <form:option value="4" label="4"/>
                            <form:option value="5" label="5"/>
                        </form:select>
                    </div>
                    <button type="submit" class="btn btn-primary">Submit</button>
                </div>
            </form:form>

        </div>
        <jsp:include page="../fragments/footer.jsp"/>
    </body>
</html>
