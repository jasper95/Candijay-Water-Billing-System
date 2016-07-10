<%-- 
    Document   : createOrUpdatePaymentForm
    Created on : Aug 22, 2015, 5:16:55 PM
    Author     : 201244055
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
        <title>${createOrUpdate} Payment</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_gen.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_dashboard.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_sticky-footer-navbar.css" rel="stylesheet">
        <script src="${WEB_JARS}jquery/2.0.3/jquery.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${WEB_JARS}jquery-ui/1.10.3/ui/jquery.ui.core.js"></script>
        <script src="${WEB_JARS}jquery-ui/1.10.3/ui/jquery.ui.datepicker.js"></script>
        <link href="${WEB_JARS}jquery-ui/1.10.3/themes/base/jquery-ui.css" rel="stylesheet"/>
    </head>
    <body class="cstm">
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <jsp:include page="../fragments/postAuth/sidebar.jsp"/>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h2 class="sub-header" style="margin-top:15px;"><a type="button" href="${pageContext.servletContext.contextPath}/admin/payments"><img src="<c:url value="/resources/img/back.png"/>"  style="margin-bottom:5px;" class=""></a>${createOrUpdate} Payment</h2>
            <div style="display: ${createOrUpdate == 'Create' ? 'anything' : 'none'}"/> 
                <form:form modelAttribute="searchForm" id="fetchAccount"  action="${pageContext.servletContext.contextPath}/admin/payments">
                    <div id="search-error" class="alert alert-danger" style="border-radius:5px;display:none">Account does not exists</div>
                    <cws:inputField cssStyle="background-image: url('${STATIC_URL}img/un.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="accountNumber" label="Account Number"/><input type="submit" class="btn btn-ctm btn-primary" value="Find"/>    
                </form:form>
                <hr>
            </div>
            <div id="crt-mr-found">
                <p><img class="un-pw-img" src="${STATIC_URL}img/un.png" style="margin-top:-5px;margin-right:9px;"><strong> <span id="full-name" ></span></strong></p>
                <p><img class="un-pw-img" src="${STATIC_URL}img/address.PNG" style="margin-top:-5px; margin-right:10px;"><strong><span id="address"></span></strong></p>
                <p><img class="un-pw-img" src="${STATIC_URL}img/status.png" style="margin-top:-5px; margin-right:10px;"><span id="status" style="font-size:15px;"></span></p>
                <hr>
                <p><strong>Latest Bill:</strong></p>
                <div class="table-stripedesponsive">
                  <table class="table table-striped" id="recent-readings">
                    <thead>
                        <tr>
                            <th>Transaction No.</th>
                            <th>Schedule</th>
                            <th>Due Date</th>
                            <th>Amount</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>             
                    </tbody>
                  </table>
                </div>
                <hr>
                <p>
                    <strong>${createOrUpdate} Payment</strong>
                </p>
                <form:form modelAttribute="paymentForm" method="POST" action="${pageContext.servletContext.contextPath}/admin/payments/process-payment" id="payment-form">
                    <div id="validation-error" class="alert alert-danger" style="border-radius:5px; display: none;"></div>
                    <div id="validation-success" class="alert alert-success" style="border-radius:5px; display: none;"></div>
                    <form:hidden path="accountId"/>
                    <c:if test="${createOrUpdate == 'Update'}"> 
                        <form:hidden path="payment.id"/>
                    </c:if>
                    <div class="navbar-form">
                        <cws:inputField cssStyle="background-image: url('${STATIC_URL}img/date.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="payment.amountPaid" id="amount-paid" label="Amount"/>
                    </div>
                    <div class="navbar-form">
                        <cws:inputField cssStyle="background-image: url('${STATIC_URL}img/date.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="payment.discount" id="discount" label="Discount"/>
                    </div>
                    <div class="navbar-form" >
                        <cws:inputField cssStyle="background-image: url('${STATIC_URL}img/date.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="payment.date" id="date" label="Payment Date"/>
                    </div>
                    <button type="submit" class="btn btn-primary">${createOrUpdate} Payment</button>
                </form:form>
            </div>          
        </div>
        <jsp:include page="../fragments/footer.jsp"/>
        <script>
            $(function () {
                $("#date").datepicker({ dateFormat: 'yy/mm/dd'});
            });
        </script>
        <c:choose>
            <c:when test="${createOrUpdate == 'Create'}">
                <script src="${STATIC_URL}js/payments/create.js"></script>
            </c:when>
            <c:otherwise>
                <script src="${STATIC_URL}js/payments/update.js"></script>
            </c:otherwise>
        </c:choose>
    </body>
</html>