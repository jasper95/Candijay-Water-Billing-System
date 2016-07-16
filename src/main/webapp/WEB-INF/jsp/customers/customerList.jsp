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
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_sticky-footer-navbar.css" rel="stylesheet">
        <script src="${WEB_JARS}jquery/2.0.3/jquery.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
    </head>
    <body>
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <div id="wrapper">
            <jsp:include page="../fragments/postAuth/sidebar.jsp"/>
            <div id="page-content-wrapper">
                <div class="page-content">
                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-sm-10">
                                <h2>Customers</h2>
                            </div>
                            <div class="col-sm-2 vertical-center">
                                <a type="button" class="btn btn-ctm btn-default" href="customers/new">Create Customer</a>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 alert">
                                <div class="alert alert-info">
                                    <div class="form-inline">
                                        <div id="last-name" class="form-group">
                                            <label>Lastname</label>
                                        </div>
                                        <div id="first-name" class="form-group">
                                            <label>Firstname</label>
                                        </div>
                                    </div>
                                    <div class="form-inline vertical-center">
                                        <a id="filterButton" type="button" class="btn btn-primary"> Apply </a>
                                        <a id="filterClearButton" type="button" class="btn btn-danger"> Clear </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 main">
                                <div class="table-responsive">
                                    <datatables:table id="customer" cssClass="table table-striped" dom="ltipr" url="/admin/customers/datatable-search" serverSide="true" filterPlaceholder="none" filterSelector="#filterButton" filterClearSelector="#filterClearButton">
                                        <datatables:column title="ID" property="id" filterable="true" />
                                        <datatables:column visible="false" searchable="true" property="lastname" selector="last-name"/>
                                        <datatables:column visible="false" searchable="true" property="firstName" selector="first-name"/>
                                        <datatables:column sortable="false" filterable="false" title="Name" renderFunction="custom-rendering#customerUrl"/>
                                        <datatables:column title="Gender" filterable="false" sortable="false" property="gender"/>
                                        <datatables:column title="Occupation" filterable="false" sortable="false" property="occupation"/>
                                        <datatables:column title="Contact" filterable="false" sortable="false" property="contactNumber"/>
                                        <datatables:column title="Members" filterable="false" sortable="false" property="familyMembersCount"/>
                                        <datatables:column filterable="false" title="Actions" renderFunction="custom-rendering#customerListActions" sortable="false"/>
                                        <dandelion:bundle excludes="jquery"/>
                                        <datatables:extraJs bundles="customer" placeholder="before_end_document_ready" />
                                    </datatables:table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>