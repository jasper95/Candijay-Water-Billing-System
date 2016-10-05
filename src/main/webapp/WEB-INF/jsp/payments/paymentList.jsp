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
        <link href="${STATIC_URL}css/bootstrap-dialog.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
        <link href="${WEB_JARS}jquery-ui/1.10.3/themes/base/jquery-ui.css" rel="stylesheet"/>
        <sec:csrfMetaTags/>
    </head>
    <body>
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <div id="content-loader" class="loader"></div>
        <div id="main-content" style="display:none" class="container">
            <div class="row">
                <div class="col-sm-8">
                    <h2>Payments</h2>
                </div>
                <div class="col-sm-2 vertical-center">
                    <button type="button" id="fn-payment-btn" class="btn btn-default">Finalize Payments</button>
                </div>
                <div class="col-sm-2 vertical-center">
                    <a type="button" class="btn btn-ctm btn-default" href="${pageContext.servletContext.contextPath}/admin/payments/new">Create Payments</a>
                </div>
            </div>
            <div class="alert alert-info form-wrapper" style="padding-bottom: 0px">
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
                    <div class="col-md-3" id="or-number">
                        <label>OR number</label>
                    </div>
                    <div class="col-md-3" id="payment-month">
                        <label>Payment Month</label>
                    </div>
                    <div class="col-md-3" id="payment-year">
                        <label>Payment Year</label>
                    </div>
                    <div class="col-md-3" id="payment-date">
                        <label>Payment Date</label>
                    </div>
                </div>
                <div class="col-sm-12">
                    <div class="pull-align-right col-md-3 filter-btn-wrapper">
                        <a id="filterClearButton" type="button" class="btn btn-danger list-filter-btn"><i class="fa fa-remove fa-fw"></i> Reset </a>
                        <a id="filterButton" type="button" class="btn btn-primary list-filter-btn"><i class="fa fa-search fa-fw"></i> Search </a>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="pull-align-right col-sm-12 form-inline">
                    <div class="form-group">
                        <button type="button" class="btn btn-success" id="apply" ><i class="fa fa-file-text-o fa-fw"></i>Create Payment History</button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 main">
                    <div class="table-responsive">
                        <form:form id="form" method="POST" modelAttribute="checkboxes" cssClass="table table-striped" action="${pageContext.servletContext.contextPath}/admin/payments">
                            <datatables:table deferLoading="0" deferRender="true" dom="ltipr" id="payment" cssClass="table table-striped" url="/admin/payments/datatable-search" serverSide="true" filterPlaceholder="none" filterSelector="#filterButton" filterClearSelector="#filterClearButton" >
                                <datatables:column name="payment-id" property="id" filterable="true" visible="false" sortInitDirection="desc" sortInitOrder="0"/>
                                <datatables:column sortable="false" renderFunction="custom-rendering#checkbox">
                                    <datatables:columnHead>
                                        <input type="checkbox" id="master-checkbox" />
                                    </datatables:columnHead>
                                </datatables:column>
                                <datatables:column title="Acct #" name="account-number" property="account.number" sortable="false"/>
                                <datatables:column title="Lastname" name="lastname" property="account.customer.lastname" sortable="false"/>
                                <datatables:column title="Firstname" name="firstName" property="account.customer.firstName" sortable="false"/>
                                <datatables:column title="Month" name="month" property="invoice.schedule.month" renderFunction="custom-rendering#month" />
                                <datatables:column title="Year" name ="year" property="invoice.schedule.year"/>
                                <datatables:column name="brgy" visible="false" property="account.address.brgy" sortable="false"/>
                                <datatables:column title="OR number" property="receiptNumber" sortable="false" default="---" cssCellClass="or-number"/>
                                <datatables:column title="Discount" name="discount" property="discount" sortable="false" renderFunction="custom-rendering#toPeso" cssCellClass="payment-discount"/>
                                <datatables:column title="Paid" name="amount-paid" property="amountPaid" sortable="false"  renderFunction="custom-rendering#toPeso" cssCellClass="payment-amount"/>
                                <datatables:column title="Date" name="date" property="date" sortable="false" cssCellClass="payment-date"/>
                                <datatables:column title="Edit" renderFunction="custom-rendering#readingActions" searchable="false" sortable="false"/>
                                <datatables:column title="Audit" sortable="false" renderFunction="custom-rendering#audit"/>
                                <datatables:extraJs bundles="payment" placeholder="before_end_document_ready"/>
                                <datatables:extraJs bundles="session-timeout" placeholder="before_end_document_ready"/>
                                <dandelion:bundle excludes="jquery"/>
                            </datatables:table>
                        </form:form>
                        <input type="hidden" id="row-num">
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="../fragments/modals/payment-form.jsp"/>
        <jsp:include page="../fragments/modals/payment-info.jsp"/>
        <jsp:include page="../fragments/modals/finalize-payments-form.jsp"/>
        <script src="${WEB_JARS}jquery/2.0.3/jquery.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap-dialog.min.js"></script>
        <script src="${STATIC_URL}js/helpers/form-validation.js"></script>
        <script src="${STATIC_URL}js/global.js"></script>
        <script src="${WEB_JARS}jquery-ui/1.10.3/ui/jquery.ui.core.js"></script>
        <script src="${WEB_JARS}jquery-ui/1.10.3/ui/jquery.ui.datepicker.js"></script>
        <script src="${STATIC_URL}js/helpers/reports-helper.js"></script>
        <script src="${STATIC_URL}js/payments/list.js"></script>
        <script src="${STATIC_URL}js/payments/update.js"></script>
        <script>
            $(document).ready(function(){
                $('#content-loader').hide()
                $('#main-content').show();
            });
        </script>
    </body>
</html>