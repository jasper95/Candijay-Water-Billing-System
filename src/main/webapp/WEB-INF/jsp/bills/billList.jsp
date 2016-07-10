<%-- 
    Document   : billList
    Created on : Aug 21, 2015, 11:40:34 AM
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
        <title>Bills</title>
        <sec:csrfMetaTags/>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_gen.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_dashboard.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_sticky-footer-navbar.css" rel="stylesheet">
        <script src="${WEB_JARS}jquery/2.0.3/jquery.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
    </head>
    <body class="cstm">
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <jsp:include page="../fragments/postAuth/sidebar.jsp"/>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h2 class="sub-header" style="margin-top:15px;">
                Bills
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
                    <div class="col-md-3" id="bill-id">Transaction No</div>
                    <div class="col-md-3" id="reading-month">Month</div>
                    <div class="col-md-3" id="reading-year">Year</div>
                </div>
                <br>
                <a id="filterButton" type="button" class="btn btn-ctm btn-primary"> Apply </a> 
                <a id="filterClearButton" type="button" class="btn btn-ctm btn-info"> Clear </a>
            </div>        
            <div class="table-stripedesponsive">
                <form:form id="form" method="POST" modelAttribute="checkboxes" action="${requestScope['javax.servlet.forward.request_uri']}">
                    <div style="text-align:right; padding-bottom:1em;">
                        <button type="submit">Export Invoice as PDF</button>
                    </div>
                    <datatables:table dom="ltipr" id="invoice" cssClass="table table-striped" url="/admin/bills/datatable-search" serverSide="true" filterPlaceholder="none" filterSelector="#filterButton" filterClearSelector="#filterClearButton">
                        <datatables:column title="Trans. #" name="bill-id" property="id" filterable="true" visible="false" sortInitOrder="0" sortInitDirection="desc"/>
                        <datatables:column title="Acct #" property="account.number"/>
                        <datatables:column visible="false" property="account.customer.lastname"/>
                        <datatables:column visible="false" property="account.customer.firstName"/>
                        <datatables:column title="Month" name="month" property="schedule.month" renderFunction="custom-rendering#month" />
                        <datatables:column title="Year" name ="year" property="schedule.year"/>
                        <datatables:column title="Brgy" name="brgy" property="account.address.brgy" sortable="false"/>
                        <datatables:column title="Zone" name="zone" property="account.address.locationCode" sortable="false"/>
                        <datatables:column title="Arrears" name="arrears" property="arrears" renderFunction="custom-rendering#toPeso"/>
                        <datatables:column title="Penalty" name="penalty" property="penalty" renderFunction="custom-rendering#toPeso"/>
                        <datatables:column title="Basic" name="basic" property="basic" renderFunction="custom-rendering#toPeso"/>
                        <datatables:column title="SysLoss" name="sysLoss" property="systemLoss" renderFunction="custom-rendering#toPeso"/>
                        <datatables:column title="DepFund" name="depFund" property="depreciationFund" renderFunction="custom-rendering#toPeso"/>
                        <datatables:column title="Others" name="others" property="others" renderFunction="custom-rendering#toPeso"/>
                        <datatables:column title="Total Due" name="amount" property="netCharge" renderFunction="custom-rendering#toPeso"/>
                        <datatables:column title="Status" name="status" property="status" sortable="false"/>
                        <datatables:column  sortable="false" cssCellStyle="text-align:center;" renderFunction="custom-rendering#checkbox">
                                <datatables:columnHead> 
                                    <input type="checkbox" id="master-checkbox" />
                                </datatables:columnHead>
                        </datatables:column>
                        <dandelion:bundle excludes="jquery"/>
                        <datatables:extraJs bundles="invoice" placeholder="before_end_document_ready" />
                    </datatables:table>
                </form:form>
            </div>
        </div>
        <jsp:include page="../fragments/footer.jsp"/>
        <script src="${STATIC_URL}js/bills/list.js"></script>
    </body>
</html>
