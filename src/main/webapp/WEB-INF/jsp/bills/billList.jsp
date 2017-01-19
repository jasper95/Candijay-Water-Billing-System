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
                        <form:form id="checkbox-form" method="POST" modelAttribute="checkboxes" cssClass="table table-striped">
                            <datatables:table deferLoading="0" deferRender="true" dom="ltipr" id="invoice" cssClass="table table-striped" url="${spring:mvcUrl('datatables-api#bills').build()}" serverSide="true" filterPlaceholder="none" filterSelector="#filterButton" filterClearSelector="#filterClearButton">
                                <datatables:column name="bill-id" property="id" filterable="true" cssCellClass="id" visible="false" sortInitOrder="0" sortInitDirection="desc"/>
                                <datatables:column  sortable="false" cssCellStyle="text-align:center;" renderFunction="custom-rendering#checkbox">
                                    <datatables:columnHead>
                                        <input type="checkbox" id="master-checkbox" />
                                    </datatables:columnHead>
                                </datatables:column>
                                <datatables:column property="account.number" visible="false" renderFunction="custom-rendering#accountUrl"/>
                                <datatables:column property="account.customer.lastname" visible="false" sortable="false"/>
                                <datatables:column property="account.customer.firstName" visible="false" sortable="false"/>
                                <datatables:column name="month" property="schedule.month" visible="false"/>
                                <datatables:column name ="year" property="schedule.year" visible="false"/>
                                <datatables:column name="brgy" property="account.address.brgy" visible="false"/>
                                <datatables:column name="zone" property="account.address.locationCode" visible="false"/>
                                <datatables:column title="Status" name="status" property="status" sortable="false" visible="false"/>
                                <datatables:column title="Name" sortable="false" property="account.customer.name" renderFunction="custom-rendering#customerUrlReadingList"/>
                                <datatables:column title="Schedule" sortable="false" renderFunction="custom-rendering#monthAndYear"/>
                                <datatables:column title="Total Current" property="totalCurrent" sortable="false" renderFunction="custom-rendering#toPeso"/>
                                <datatables:column title="Arrears" property="arrears" sortable="false" renderFunction="custom-rendering#toPeso"/>
                                <datatables:column title="Discount" name="discount" property="discount"  sortable="false" cssCellClass="discount" renderFunction="custom-rendering#toPeso"/>
                                <datatables:column title="Total Due" property="netCharge" sortable="false" renderFunction="custom-rendering#toPeso"/>
                                <datatables:column title="Balance" name="amount" property="remainingTotal" cssCellClass="total-due" renderFunction="custom-rendering#toPeso"/>
                                <dandelion:bundle excludes="jquery"/>
                                <datatables:extraJs bundles="invoice" placeholder="before_end_document_ready" />
                            </datatables:table>
                        </form:form>
                        <input type="hidden" id="row-num">
                        <input type="hidden" id="bills-uri" value="${pageContext.servletContext.contextPath}/admin/bills/">
                    </div>
                </div>
            </div>
        </div>
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