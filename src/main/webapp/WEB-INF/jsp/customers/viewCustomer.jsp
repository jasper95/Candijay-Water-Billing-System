<%-- 
    Document   : viewCustomer
    Created on : May 9, 2015, 9:20:49 PM
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
        <title>View Customer</title>
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
            <h2 class="sub-header" style="margin-top:15px;"><a type="button" href="${pageContext.servletContext.contextPath}/admin/customers"><img src="${STATIC_URL}img/back.png" style="margin-bottom:5px;" class=""></a> View Customer    
            <a type="button" class="btn btn-ctm btn-primary" style="float:right;" href="${pageContext.servletContext.contextPath}/admin/customers/${customer.id}/update"> Update </a>           
            </h2>
            <c:if test="${updateSuccess == 1}">
                <div class="alert alert-success" style="border-radius:5px;"><br>
                    <center><h4>This customer is successfully updated!</h4></center>
                </div>
            </c:if>
            <p><img class="un-pw-img" src="${STATIC_URL}img/un.png" style="margin-top:-5px;margin-right:9px;"><strong> ${customer.lastname}, ${customer.firstName}</strong></p>
            <p><img class="un-pw-img" src="${STATIC_URL}img/gender.png" style="margin-top:-5px; margin-right:10px;"><strong> ${customer.gender}</strong></p>
            <p><img class="un-pw-img" src="${STATIC_URL}img/mobile.png" style="margin-top:-5px;margin-right:10px;"><strong> ${customer.contactNumber}</strong></p>
            <p><img class="un-pw-img" src="${STATIC_URL}img/occupation.png" style="margin-top:-5px;margin-right:10px;"><strong> ${customer.occupation}</strong></p>
            <p>
                <strong>Accounts</strong>
            </p>
            <a id="reload"></a>
            <div class="table-stripedesponsive">
                <datatables:table dom="t" cssClass="table table-striped" reloadSelector="#reload" id="accounts" url="${requestScope['javax.servlet.forward.request_uri']}/accounts">
                    <datatables:column name="id" property="id" visible="false" sortInitDirection="false"/>
                    <datatables:column renderFunction="custom-rendering#accountUrl" title="Acct No." name="number" property="number" sortable="false" sortInitDirection="false"/>
                    <datatables:column name="address" title="Barangay" property="address.brgy" sortable="false"/>
                    <datatables:column name="zone" title="Zone" property="address.locationCode" sortable="false"/>
                    <datatables:column name="status" title="Status" property="status" sortable="false"/>
                    <datatables:column title="Action" renderFunction="custom-rendering#activateAccount" sortable="false"/>
                </datatables:table>
                <input type="hidden" id="context-path" value="${pageContext.servletContext.contextPath}"/>
            </div>
            <br/>
            <a type="button" href="${pageContext.servletContext.contextPath}/admin/accounts/${customer.id}/new" class="btn btn-success" style="float:right;"> Add Account </a>
        </div>
        <jsp:include page="../fragments/footer.jsp"/>
        <script src="${STATIC_URL}js/customers/view.js"></script>  
    </body>
</html>