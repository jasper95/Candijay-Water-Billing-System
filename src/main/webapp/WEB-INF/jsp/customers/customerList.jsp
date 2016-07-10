<%-- 
    Document   : search
    Created on : Apr 20, 2015, 8:07:08 PM
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
        <title>Customers</title>
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
                <h2 class="sub-header">
                    Customers
                    <a type="button" class="btn btn-ctm btn-default" style="float:right;" href="customers/new">Create Customer</a>
                </h2>
            <div class="alert alert-info container" role="alert">
                <p><strong>Filters:</strong></p>
                <div class="row">
                    <div class="col-md-3" id="last-name">Lastname</div>
                    <div class="col-md-3" id="first-name">Firstname</div>
                </div>
                <br/>
                <a id="filterButton" type="button" class="btn btn-ctm btn-primary"> Apply </a> 
                <a id="filterClearButton" type="button" class="btn btn-ctm btn-info"> Clear </a>
            </div>                
            <div class="table-stripedesponsive">
                <datatables:table id="customer" dom="ltipr" url="/admin/customers/datatable-search" serverSide="true" filterPlaceholder="none" filterSelector="#filterButton" filterClearSelector="#filterClearButton">
                    <datatables:column title="ID" property="id" filterable="true" />
                    <datatables:column visible="false" searchable="true" property="lastname" selector="last-name"/>
                    <datatables:column visible="false" searchable="true" property="firstName" selector="first-name"/>
                    <datatables:column sortable="false" filterable="false" title="Name" renderFunction="custom-rendering#customerUrl"/>
                    <datatables:column title="Gender" filterable="false" sortable="false" property="gender"/>
                    <datatables:column title="Occupation" filterable="false" sortable="false" property="occupation"/>
                    <datatables:column title="Contact" filterable="false" sortable="false" property="contactNumber"/>
                    <datatables:column title="Members" filterable="false" sortable="false" property="familyMembersCount"/>
                    <datatables:column filterable="false" title="Actions" renderFunction="custom-rendering#customerListActions" sortable="false"/>
                    <datatables:extraJs bundles="customer" placeholder="before_end_document_ready" />
                </datatables:table>
            </div>
            </div>
        <jsp:include page="../fragments/footer.jsp"/>
    </body>
</html>