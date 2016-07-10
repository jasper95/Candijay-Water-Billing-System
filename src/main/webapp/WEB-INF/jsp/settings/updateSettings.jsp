<%-- 
    Document   : updateSettings
    Created on : Jan 20, 2016, 9:29:48 AM
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
        <title>Settings</title>

        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_gen.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_dashboard.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_sticky-footer-navbar.css" rel="stylesheet">
        <sec:csrfMetaTags/>
        <spring:url value="/webjars/jquery/2.0.3/jquery.js" var="jQuery"/>
        <script src="${jQuery}"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
    </head>
    <body class="cstm">
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <jsp:include page="../fragments/postAuth/sidebar.jsp"/>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h2 class="sub-header" style="margin-top:15px;">Update Settings</h2>
            <c:if test="${updateSuccess == 1}">
                 <div >
                    <center><h4>Settings successfully updated!</h4></center>
                </div>
            </c:if>
            <form:form modelAttribute="settings" id="settingsForm" method="POST">
            <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.settings'].allErrors}">
                <form:errors path="*"/>
            </c:if>
                Penalty:<form:input path="penalty" /><br/>
                Basic:<form:input path="basic"/><br/>
                PES:<form:input path="pes"/><br/>
                Depreciation Fund:<form:input path="depreciationFund"/><br/>
                System loss:<form:input path="systemLoss"/><br/>
                <input type="submit" value="Submit"/>
            </form:form>
        </div>
        <jsp:include page="../fragments/footer.jsp"/>   
    </body>
</html>