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
                <div class="col-sm-4 vertical-center text-right">
                    <button type="button" id="fn-payment-btn" class="btn btn-default">Finalize Payments</button>
                    <a type="button" class="btn btn-ctm btn-default" href="${pageContext.servletContext.contextPath}/admin/payments/new">Create Payments</a>
                </div>
            </div>
            <div id="search-filters" class="alert alert-info form-wrapper" style="padding-bottom: 0px">
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
                    <div class="table-responsive table">
                        <form:form id="form" method="POST" modelAttribute="checkboxes" action="${pageContext.servletContext.contextPath}/admin/payments">
                            <datatables:table deferLoading="0" deferRender="true" dom="ltipr" id="payment" cssClass="table table-striped" url="${spring:mvcUrl('datatables-api#payments').build()}" serverSide="true" filterPlaceholder="none" filterSelector="#filterButton" filterClearSelector="#filterClearButton" >
                                <datatables:column name="payment-id" property="id" filterable="true" visible="false" sortInitDirection="desc" sortInitOrder="0"/>
                                <datatables:column sortable="false" renderFunction="custom-rendering#checkboxPayment">
                                    <datatables:columnHead>
                                        <input type="checkbox" id="master-checkbox" />
                                    </datatables:columnHead>
                                </datatables:column>
                                <datatables:column name="account-number" property="account.number" visible="false"/>
                                <datatables:column name="lastname" property="account.customer.lastname" visible="false"/>
                                <datatables:column name="firstName" property="account.customer.firstName" visible="false"/>
                                <datatables:column name="month" property="schedule.month" visible="false"/>
                                <datatables:column name ="year" property="schedule.year" visible="false"/>
                                <datatables:column name="brgy" visible="false" property="account.address.brgy" sortable="false"/>
                                <datatables:column title="Name" property="account.customer.name" sortable="false" renderFunction="custom-rendering#customerUrlReadingList"/>
                                <datatables:column title="Date" name="date" property="date" sortable="false" cssCellClass="payment-date"/>
                                <datatables:column title="OR number" property="receiptNumber" sortable="false" default="---" cssCellClass="or-number"/>
                                <datatables:column title="Paid" name="amount-paid" property="amountPaid" sortable="false"  renderFunction="custom-rendering#toPeso" cssCellClass="payment-amount"/>
                                <datatables:column title="Type" property="type" sortable="false"/>
                                <datatables:column title="Due" property="invoiceTotal" sortable="false" renderFunction="custom-rendering#toPeso"/>
                                <datatables:column title="Balance" property="balance" sortable="false" renderFunction="custom-rendering#toPeso"/>
                                <datatables:column title="Actions" sortable="false" renderFunction="custom-rendering#paymentListActions"/>
                                <datatables:extraJs bundles="payment" placeholder="before_end_document_ready"/>
                                <datatables:extraJs bundles="session-timeout" placeholder="before_end_document_ready"/>
                                <dandelion:bundle excludes="jquery"/>
                            </datatables:table>
                        </form:form>
                        <input type="hidden" id="row-num">
                        <input type="hidden" id="acc-balance-val"/>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="../fragments/modals/payment-form.jsp"/>
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