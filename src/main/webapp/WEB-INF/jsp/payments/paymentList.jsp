<%-- 
    Document   : paymentList
    Created on : Aug 22, 2015, 12:33:37 PM
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
        <title>Payments</title>
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
            <h2 class="sub-header" style="margin-top:15px;">
                Payments
                <a type="button" class="btn btn-ctm btn-default" style="float:right;" href="payments/new"> 
                    Create Payment 
                </a>
            </h2>
            <div class="alert alert-info container" role="alert">
                <p><strong>Filters:</strong></p>
                <div class="row">
                    <div class="col-md-3" id="acct-no">Acct #</div>
                    <div class="col-md-3" id="acct-lastname">Lastname</div>
                    <div class="col-md-3" id="acct-firstname">Firstname</div>
                    <div class="col-md-3" id="acct-brgy">Barangay</div>
                </div>
                <div class="row">
                    <div class="col-md-3" id="payment-id">OR #</div>
                    <div class="col-md-3" id="payment-month">Month</div>
                    <div class="col-md-3" id="payment-year">Year</div>
                    <div class="col-md-3" id="payment-date">Date</div>
                </div>
                <br>
                <a id="filterButton" type="button" class="btn btn-ctm btn-primary"> Apply </a> 
                <a id="filterClearButton" type="button" class="btn btn-ctm btn-info"> Clear </a>
            </div>
            <div class="table-stripedesponsive">
                <datatables:table  dom="ltipr" id="payment" cssClass="table table-striped" url="/admin/payments/datatable-search" serverSide="true" filterPlaceholder="none" filterSelector="#filterButton" filterClearSelector="#filterClearButton" >
                    <datatables:column title="OR #" name="bill-id" property="id" filterable="true" visible="false" sortInitDirection="desc" sortInitOrder="0"/>        
                    <datatables:column title="Acct #" name="account-number" property="account.number" sortable="false"/>
                    <datatables:column title="Month" name="month" property="invoice.schedule.month" renderFunction="custom-rendering#month" />
                    <datatables:column title="Year" name ="year" property="invoice.schedule.year"/>
                    <datatables:column title="Lastname" name="lastname" property="account.customer.lastname" sortable="false"/>
                    <datatables:column title="Firstname" name="firstName" property="account.customer.firstName" sortable="false"/>
                    <datatables:column title="Barangay" name="brgy" property="account.address.brgy" sortable="false"/>
                    <datatables:column title="Zone" name="zone" property="account.address.locationCode" sortable="false"/>
                    <datatables:column title="Total Due" name="totalDue" property="invoice.netCharge" renderFunction="custom-rendering#toPeso"/>
                    <datatables:column title="Discount" name="discount" property="discount" renderFunction="custom-rendering#toPeso"/>
                    <datatables:column title="Paid" name="amount-paid" property="amountPaid" renderFunction="custom-rendering#toPeso"/>
                    <datatables:column title="Date" name="date" property="date" renderFunction="custom-rendering#paidDate"/>
                    <datatables:column title="Actions" renderFunction="custom-rendering#readingActions" searchable="false" sortable="false"/>
                    <datatables:extraJs bundles="payment" placeholder="before_end_document_ready"/>
                    <dandelion:bundle excludes="jquery"/>
                </datatables:table>    
            </div>
        </div>
        <input type="hidden" id="request-uri" value="${requestScope['javax.servlet.forward.request_uri']}"/>
        <jsp:include page="../fragments/footer.jsp"/>
        <script src="${STATIC_URL}js/payments/list.js"></script>      
    </body>
</html>