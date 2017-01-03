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
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/bootstrap-dialog.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
        <sec:csrfMetaTags/>
    </head>
    <body>
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <div id="content-loader" class="loader"></div>
        <div id="main-content" style="display:none" class="container">
            <h2>Bills</h2>
            <div id="search-filters" class="alert alert-info form-wrapper">
                <div class="col-sm-12">
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
                <div class="col-sm-12">
                    <div class="col-md-3" id="acct-zone">
                        <label>Zone</label>
                    </div>
                    <div class="col-md-3" id="reading-month">
                        <label>Month</label>
                    </div>
                    <div class="col-md-3" id="reading-year">
                        <label>Year</label>
                    </div>
                    <div class="col-md-3" id="inv-st">
                        <label>Status</label>
                    </div>
                </div>
                <div class="col-sm-12">
                    <div class="pull-align-right col-md-3 vertical-center filter-btn-wrapper">
                        <a id="filterClearButton" type="button" class="btn btn-danger list-filter-btn"><i class="fa fa-remove fa-fw"></i> Reset </a>
                        <a id="filterButton" type="button" class="btn btn-primary list-filter-btn"><i class="fa fa-search fa-fw"></i> Search </a>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="pull-align-right col-sm-12 form-inline">
                    <div class="form-group">
                        <button type="button" class="btn btn-success" id="apply" ><i class="fa fa-file-text-o fa-fw"></i>Export Bills as PDF</button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 main">
                    <div class="table-responsive">
                        <form:form id="form" method="POST" modelAttribute="checkboxes" cssClass="table table-striped" action="${pageContext.servletContext.contextPath}/admin/bills">
                            <datatables:table deferLoading="0" deferRender="true" dom="ltipr" id="invoice" cssClass="table table-striped" url="/admin/bills/datatable-search" serverSide="true" filterPlaceholder="none" filterSelector="#filterButton" filterClearSelector="#filterClearButton">
                                <datatables:column title="Trans. #" name="bill-id" property="id" filterable="true" cssCellClass="id" visible="false" sortInitOrder="0" sortInitDirection="desc"/>
                                <datatables:column  sortable="false" cssCellStyle="text-align:center;" renderFunction="custom-rendering#checkbox">
                                    <datatables:columnHead>
                                        <input type="checkbox" id="master-checkbox" />
                                    </datatables:columnHead>
                                </datatables:column>
                                <datatables:column title="Acct #" property="account.number" sortable="false"/>
                                <datatables:column title="Lastname" property="account.customer.lastname" sortable="false"/>
                                <datatables:column title="Firstname" property="account.customer.firstName" sortable="false"/>
                                <datatables:column title="Month" name="month" property="schedule.month" renderFunction="custom-rendering#month" />
                                <datatables:column title="Year" name ="year" property="schedule.year" />
                                <datatables:column name="brgy" property="account.address.brgy" visible="false"/>
                                <datatables:column name="zone" property="account.address.locationCode" visible="false"/>
                                <datatables:column title="Status" name="status" property="status" sortable="false"/>
                                <datatables:column title="Discount" name="discount" property="discount"  sortable="false" cssCellClass="discount" renderFunction="custom-rendering#toPeso"/>
                                <datatables:column title="Remaining Due" name="amount" property="remainingTotal" sortable="false" cssCellClass="total-due" renderFunction="custom-rendering#toPeso"/>
                                <datatables:column title="Edit Discount" sortable="false" renderFunction="custom-rendering#editDiscount"/>
                                <dandelion:bundle excludes="jquery"/>
                                <datatables:extraJs bundles="invoice" placeholder="before_end_document_ready" />
                            </datatables:table>
                        </form:form>
                        <input type="hidden" id="row-num">
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="../fragments/modals/bill-discount-form.jsp"/>
        <script src="${WEB_JARS}jquery/2.0.3/jquery.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap-dialog.min.js"></script>
        <script src="${STATIC_URL}js/helpers/form-validation.js"></script>
        <script src="${STATIC_URL}js/global.js"></script>
        <script src="${STATIC_URL}js/helpers/reports-helper.js"></script>
        <script src="${STATIC_URL}js/bills/list.js"></script>
        <script>
            $(document).ready(function(){
                $('#content-loader').hide()
                $('#main-content').show();
            });
        </script>
    </body>
</html>