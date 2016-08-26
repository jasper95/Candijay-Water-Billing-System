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
        <link href="${STATIC_URL}css/bootstrap-dialog.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_sticky-footer-navbar.css" rel="stylesheet">
        <sec:csrfMetaTags/>
    </head>
    <body>
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <div id="content-loader" class="loader"></div>
        <div id="main-content" style="display: none;" class="container-fluid">
            <div class="row">
                <div class="col-sm-10">
                    <h2>Meter Reading</h2>
                </div>
                <div class="col-sm-2 vertical-center">
                    <a type="button" class="btn btn-ctm btn-default"  href="${pageContext.servletContext.contextPath}/admin/reading/new/">Create Reading</a>
                </div>
            </div>
            <div class="alert alert-info form-wrapper">
                <div id="reading-id" style="display:none"></div>
                <div class="col-sm-12 form-group">
                    <div class="col-md-3" id="acct-no">
                        <label>Account Number</label>
                    </div>
                    <div class="col-md-3" id="acct-lastname">
                        <label>Lastname</label>
                    </div>
                    <div class="col-md-3" id="acct-firstname">
                        <label>Firstname</label>
                    </div>
                    <div class="col-md-3" id="acct-brgy">
                        <label>Barangay</label>
                    </div>
                </div>
                <div class="col-sm-12 form-group">
                    <div class="col-md-3" id="acct-zone">
                        <label>Zone</label>
                    </div>
                    <div class="col-md-3" id="reading-month">
                        <label>Reading Month</label>
                    </div>
                    <div class="col-md-3" id="reading-year">
                        <label>Reading Year</label>
                    </div>
                    <div class="pull-align-right col-md-3 vertical-center filter-btn-wrapper">
                        <a id="filterClearButton" type="button" class="btn btn-danger list-filter-btn"><i class="fa fa-remove fa-fw"></i> Reset </a>
                        <a id="filterButton" type="button" class="btn btn-primary list-filter-btn"><i class="fa fa-search fa-fw"></i> Search </a>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 main">
                    <div class="table-responsive">
                        <datatables:table deferLoading="0" deferRender="true" cssClass="table table-striped" dom="ltipr" id="reading" url="/admin/reading/datatable-search" serverSide="true" filterPlaceholder="none" filterSelector="#filterButton" filterClearSelector="#filterClearButton">
                            <datatables:column property="id" filterable="true" visible="false" sortInitDirection="desc" sortInitOrder="0"/>
                            <datatables:column title="Acct No." name="account-number" property="account.number" filterable="true" sortable="false"/>
                            <datatables:column title="Last Name" name="lastname" property="account.customer.lastname" sortable="false"/>
                            <datatables:column title="First Name" name="firstname"  property="account.customer.firstName" sortable="false"/>
                            <datatables:column title="Month" name="month" property="schedule.month" renderFunction="custom-rendering#month" sortable="true" cssCellClass="month"/>
                            <datatables:column title="Year" name="year" property="schedule.year" sortable="true" cssCellClass="year"/>
                            <datatables:column name="barangay" property="account.address.brgy" visible="false" sortable="false"/>
                            <datatables:column name="zone" property="account.address.locationCode" visible="false" sortable="false"/>
                            <datatables:column title="Consumption" name="consume" property="consumption" cssCellClass="consumption" sortable="false"/>
                            <datatables:column title="Reading" name="reading" property="readingValue" cssCellClass="reading" sortable="false"/>
                            <datatables:column title="Status" name="invoice.status" property="invoice.status" sortable="false"/>
                            <datatables:column title="Edit" renderFunction="custom-rendering#readingActions" searchable="false" sortable="false"/>
                            <datatables:column title="Audit" sortable="false" renderFunction="custom-rendering#audit"/>
                            <datatables:extraJs bundles="mreading" placeholder="before_end_document_ready"/>
                            <datatables:extraJs bundles="months" placeholder="after_all"/>
                            <dandelion:bundle excludes="jquery"/>
                        </datatables:table>
                        <input type="hidden" id="row-num">
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="../fragments/modals/reading-form.jsp"/>
        <jsp:include page="../fragments/modals/reading-info.jsp"/>
        <script src="${WEB_JARS}jquery/2.0.3/jquery.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap-dialog.min.js"></script>
        <script src="${STATIC_URL}js/helpers/form-validation.js"></script>
        <script src="${STATIC_URL}js/global.js"></script>
        <script src="${STATIC_URL}js/meter-reading/list.js"></script>
        <script src="${STATIC_URL}js/meter-reading/edit.js"></script>
        <script>
            $(document).ready(function(){
                $('#content-loader').hide()
                $('#main-content').show();
            });
        </script>
    </body>
</html>