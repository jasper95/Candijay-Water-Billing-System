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
    <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
    <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
    <link href="${STATIC_URL}css/stylesheet_sticky-footer-navbar.css" rel="stylesheet">
    <sec:csrfMetaTags/>
</head>
<body>
<jsp:include page="../fragments/postAuth/header.jsp"/>
<div id="wrapper">
    <jsp:include page="../fragments/postAuth/sidebar.jsp"/>
    <div id="page-content-wrapper">
        <div class="page-content">
            <div class="container-fluid">
                <h2>Bills</h2>
                <div class="alert alert-info form-wrapper">
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
                        <div class="col-md-3" id="bill-id">
                            <label>Transaction Number</label>
                        </div>
                        <div class="col-md-3" id="reading-month">
                            <label>Month</label>
                        </div>
                        <div class="col-md-3" id="reading-year">
                            <label>Year</label>
                        </div>
                        <div class="pull-align-right col-md-3 vertical-center filter-btn-wrapper">
                            <a id="filterClearButton" type="button" class="btn btn-danger"><i class="fa fa-remove fa-fw"></i> Clear </a>
                            <a id="filterButton" type="button" class="btn btn-primary"><i class="fa fa-search fa-fw"></i> Search </a>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="pull-align-right col-sm-12 form-inline">
                        <div class="form-group">
                            <button type="button" class="btn btn-success" id="apply" ><i class="fa fa-eyedropper fa-fw"></i>Export Invoice as PDF</button>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12 main">
                        <div class="table-responsive">
                            <form:form id="form" method="POST" modelAttribute="checkboxes" cssClass="table table-striped" action="${requestScope['javax.servlet.forward.request_uri']}">
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
                                    <datatables:column title="Total" name="amount" property="netCharge" renderFunction="custom-rendering#toPeso"/>
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
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${WEB_JARS}jquery/2.0.3/jquery.js"></script>
<script src="${STATIC_URL}js/bootstrap.min.js"></script>
<script src="${STATIC_URL}js/bills/list.js"></script>
</body>
</html>