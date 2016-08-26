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
        <title>${createOrUpdate} Payment</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/bootstrap-dialog.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_sticky-footer-navbar.css" rel="stylesheet">
        <link href="${WEB_JARS}jquery-ui/1.10.3/themes/base/jquery-ui.css" rel="stylesheet"/>
    </head>
    <body>
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <div id="content-loader" class="loader"></div>
        <div id="main-content" style="display: none;" class="container-fluid">
            <h2>Create Payment</h2>
            <hr/>
            <form:form cssClass="row" action="${pageContext.servletContext.contextPath}/admin/payments" modelAttribute="searchForm" id="fetchAccount">
                <div class="col-sm-4">
                    <div class="search-wrapper">
                        <div class="search-in-wrapper">
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
                                <form:input id="acc-nb" path="accountNumber" class="form-control" required="true" placeholder="Enter Account no." autocomplete="off"/>
                            </div>
                            <span class="field-error"></span>
                        </div>
                        <button id="search-btn"  class="btn btn-primary btn-search"><i class="fa fa-search fa-fw"></i> Search </button>
                    </div>
                </div>
            </form:form>
            <hr/>
            <div id="crt-mr-found">
                <div class="row">
                    <div class="col-sm-8">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">Account Details</h3>
                            </div>
                            <div class="panel-body">
                                <div class="col-sm-6">
                                    <div class="info-wrapper">
                                        <img class="info-img" src="${STATIC_URL}img/un.png">
                                        <div class="info-text">
                                            <span id="full-name"></span>
                                        </div>
                                    </div>
                                    <div class="info-wrapper">
                                        <img class="info-img" src="${STATIC_URL}img/address.PNG">
                                        <div class="info-text">
                                            <span id="address"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-6">
                                    <div class="info-wrapper">
                                        <img class="info-img" src="${STATIC_URL}img/standing_balance.png">
                                        <div class="info-text">
                                            <span id="last-reading"></span>
                                        </div>
                                    </div>
                                    <div class="info-wrapper">
                                        <img class="info-img" src="${STATIC_URL}img/status.png">
                                        <div class="info-text">
                                            <span id="status"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">Recent Bills</h3>
                            </div>
                            <div class="panel-body">
                                <div class="table-responsive">
                                    <div id="acc-no" style="display: none;"></div>
                                    <a id="filterButton"></a>
                                    <datatables:table cssClass="table table-striped" id="payment" filterPlaceholder="none" filterSelector="#filterButton" serverSide="true" url="${pageContext.servletContext.contextPath}/admin/bills/datatable-search" displayLength="3" dom="tp" >
                                        <datatables:column property="account.id" filterable="true" visible="false" selector="acc-no"/>
                                        <datatables:column property="id" sortInitOrder="0" sortInitDirection="desc" visible="false"/>
                                        <datatables:column title="Schedule" renderFunction="custom-rendering#monthAndYear" sortable="false" cssCellClass="schedule"/>
                                        <datatables:column title="Total Due" name="totalDue" property="netCharge" sortable="false" renderFunction="custom-rendering#toPeso" cssCellClass="invoice-due"/>
                                        <datatables:column title="OR number" property="payment.receiptNumber" sortable="false" default="---" cssCellClass="or-number"/>
                                        <datatables:column title="Discount" name="discount" property="payment.discount" sortable="false" renderFunction="custom-rendering#toPeso" cssCellClass="payment-discount"/>
                                        <datatables:column title="Amount Paid" name="amount-paid" property="payment.amountPaid" sortable="false" renderFunction="custom-rendering#toPeso" cssCellClass="payment-amount"/>
                                        <datatables:column title="Payment Date" name="date" property="payment.date" renderFunction="custom-rendering#paidDate" cssCellClass="payment-date"/>
                                        <datatables:column title="Edit" renderFunction="custom-rendering#createPaymentAction" searchable="false" sortable="false"/>
                                        <datatables:column title="Audit" sortable="false" renderFunction="custom-rendering#auditPayment2"/>
                                        <datatables:extraJs bundles="months" placeholder="after_all"/>
                                        <dandelion:bundle excludes="jquery"/>
                                    </datatables:table>
                                    <input type="hidden" id="row-num">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">Add Payment</h3>
                            </div>
                            <div class="panel-body">
                                <form:form modelAttribute="paymentForm" method="post" id="add-payment-form">
                                    <jsp:include page="../fragments/postAuth/form-alerts.jsp"/>
                                    <form:hidden path="accountId"/>
                                    <div class="col-sm-12 form-group">
                                        <cws:input id="or-num" name="payment.receiptNumber" label="OR Number" icon="money" placeholder="Enter or no." required="true" size="12"/>
                                    </div>
                                    <div class="col-sm-12 form-group">
                                        <cws:input id="amount-paid" name="payment.amountPaid" label="Amount" icon="money" placeholder="Enter amount" required="true" size="12"/>
                                    </div>
                                    <div class="col-sm-12 form-group">
                                        <cws:input id="discount" name="payment.discount" label="Discount" icon="money" placeholder="Enter discount" required="true" size="12"/>
                                    </div>
                                    <div class="col-sm-12 form-group">
                                        <cws:input id="date" name="payment.date" label="Date" icon="calendar" placeholder="Choose date" required="true" readOnly="true" size="12"/>
                                    </div>
                                    <div class="col-sm-12 form-group">
                                        <div class="form-group">
                                            <div class="col-sm-offset-2 col-sm-10">
                                                <div class="pull-right">
                                                    <button class="btn btn-primary" type="submit">Save</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="../fragments/modals/payment-form.jsp"/>
        <jsp:include page="../fragments/modals/payment-info.jsp"/>
        <script src="${WEB_JARS}jquery/2.0.3/jquery.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${STATIC_URL}js/helpers/form-validation.js"></script>
        <script src="${STATIC_URL}js/global.js"></script>
        <script src="${STATIC_URL}js/bootstrap-dialog.min.js"></script>
        <script src="${WEB_JARS}jquery-ui/1.10.3/ui/jquery.ui.core.js"></script>
        <script src="${WEB_JARS}jquery-ui/1.10.3/ui/jquery.ui.datepicker.js"></script>
        <script src="${STATIC_URL}js/helpers/search-helper.js"></script>
        <script src="${STATIC_URL}js/payments/create.js"></script>
        <script src="${STATIC_URL}js/payments/update.js"></script>
        <script>
            $(document).ready(function(){
                $('#content-loader').hide()
                $('#main-content').show();
            });
        </script>
    </body>
</html>