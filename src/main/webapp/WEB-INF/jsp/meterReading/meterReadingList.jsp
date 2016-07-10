<%-- 
    Document   : meterReadingList
    Created on : May 20, 2015, 6:38:18 PM
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
        <title>Readings</title>
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
                Meter Reading
                <a type="button" class="btn btn-ctm btn-default" style="float:right;" href="reading/new"> 
                    Create Meter Reading 
                </a>
            </h2>
            <div class="alert alert-info container" role="alert">
                <p><strong>Filters:</strong></p>
                <div class="row">
                    <div class="col-md-4" id="acct-no">Acct #</div>
                    <div class="col-md-4" id="acct-lastname">Lastname</div>
                    <div class="col-md-4" id="acct-firstname">Firstname</div>
                </div>
                <div class="row">
                    <div class="col-md-4" id="reading-month">Month</div>
                    <div class="col-md-4" id="reading-year">Year</div>
                    <div class="col-md-4" id="acct-brgy">Barangay</div>
                </div>
                <br>
                <a id="filterButton" type="button" class="btn btn-ctm btn-primary"> Apply </a> 
                <a id="filterClearButton" type="button" class="btn btn-ctm btn-info"> Clear </a>
            </div>
            <div class="table-stripedesponsive">                
                <datatables:table  dom="ltipr" id="reading" url="/admin/reading/datatable-search" serverSide="true" filterPlaceholder="none" filterSelector="#filterButton" filterClearSelector="#filterClearButton">          
                    <datatables:column property="id" filterable="true" visible="false" sortInitDirection="desc" sortInitOrder="0"/>
                    <datatables:column title="Acct No." name="account-number" property="account.number" filterable="true" sortable="false"/>
                    <datatables:column title="Last Name" name="lastname" property="account.customer.lastname"/>
                    <datatables:column title="First Name" name="firstname"  property="account.customer.firstName"/>
                    <datatables:column title="Month" name="month" property="schedule.month" renderFunction="custom-rendering#month" sortable="true"/>
                    <datatables:column title="Year" name="year" property="schedule.year" sortable="true"/>
                    <datatables:column title="Barangay" name="barangay" property="account.address.brgy" sortable="false"/>
                    <datatables:column title="Zone" name="zone" property="account.address.locationCode" sortable="false"/>
                    <datatables:column title="Consumption" name="consume" property="consumption"/>
                    <datatables:column title="Reading" name="reading" property="readingValue"/>
                    <datatables:column title="Status" name="invoice.status" property="invoice.status"/>
                    <datatables:column title="Actions" renderFunction="custom-rendering#readingActions" searchable="false" sortable="false"/>         
                    <datatables:extraJs bundles="mreading" placeholder="before_end_document_ready"/>
                </datatables:table>
            </div>
        </div>
        <jsp:include page="../fragments/footer.jsp"/>
        <script src="${STATIC_URL}js/meter-reading/list.js"></script>
    </body>
</html>