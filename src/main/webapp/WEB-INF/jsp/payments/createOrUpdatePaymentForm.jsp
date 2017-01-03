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
        <link href="${WEB_JARS}jquery-ui/1.10.3/themes/base/jquery-ui.css" rel="stylesheet"/>
    </head>
    <body>
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <div id="content-loader" class="loader"></div>
        <div id="main-content" style="display: none;" class="container">
            <h2>Create Payment</h2>
            <hr/>
            <div id="search-filters2" class="alert alert-success form-wrapper">
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
                    <div class="col-md-3 pull-align-right vertical-center filter-btn-wrapper">
                        <a id="filterClearButton2" type="button" class="btn btn-danger list-filter-btn"><i class="fa fa-remove fa-fw"></i> Reset </a>
                        <a id="filterButton2" type="button" class="btn btn-primary list-filter-btn"><i class="fa fa-search fa-fw"></i> Search </a>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <form:form cssClass="row" action="${pageContext.servletContext.contextPath}/admin/payments" modelAttribute="searchForm" id="fetchAccount">
                        <jsp:include page="../fragments/postAuth/form-alerts.jsp"/>
                        <form:hidden id="acc-nb" path="accountNumber"/>
                    </form:form>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12 main">
                    <div class="table-responsive">
                        <datatables:table deferLoading="0" deferRender="true" dom="tp" displayLength="3" cssClass="table table-striped" id="account" url="/admin/accounts/datatable-search" serverSide="true" filterPlaceholder="none" filterSelector="#filterButton2" filterClearSelector="#filterClearButton2">
                            <datatables:column sortable="false" title="Select" renderFunction="custom-rendering#selectAcountBtn"/>
                            <datatables:column title="Acct No." name="number" filterable="true" property="number" sortable="false"/>
                            <datatables:column name="lastname" title="Last Name" property="customer.lastname" sortInitOrder="0" sortInitDirection="asc" sortable="false"/>
                            <datatables:column name="firstName" title="First Name" property="customer.firstName" sortInitOrder="1" sortInitDirection="asc" sortable="false"/>
                            <datatables:column name="gender" title="Gender" property="customer.gender" sortable="false"/>
                            <datatables:column name="address" title="Barangay" property="address.brgy" sortable="false"/>
                            <datatables:column name="zone" title="Zone" property="address.locationCode" sortable="false"/>
                            <datatables:column visible="false" property="id" />
                            <dandelion:bundle excludes="jquery"/>
                            <datatables:extraJs bundles="search-helper" placeholder="before_end_document_ready" />
                            <datatables:extraJs bundles="session-timeout" placeholder="before_end_document_ready"/>
                        </datatables:table>
                    </div>
                </div>
            </div>
            <hr/>
            <div id="crt-mr-found" tabindex="0">
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
                                <h3 class="panel-title">Recent Payments</h3>
                            </div>
                            <div class="panel-body">
                                <div class="table-responsive">
                                    <div id="acc-no" style="display: none;"></div>
                                    <a id="filterButton"></a>
                                    <datatables:table cssClass="table table-striped" id="payment" filterPlaceholder="none" filterSelector="#filterButton" serverSide="true" url="${pageContext.servletContext.contextPath}/admin/payments/datatable-search" displayLength="3" dom="tp" >
                                        <datatables:column property="account.id" filterable="true" visible="false" selector="acc-no"/>
                                        <datatables:column property="id" sortInitOrder="0" sortInitDirection="desc" visible="false"/>
                                        <datatables:column title="Schedule" renderFunction="custom-rendering#monthAndYear" sortable="false" cssCellClass="schedule"/>
                                        <datatables:column title="OR number" property="receiptNumber" sortable="false" default="---" cssCellClass="or-number"/>
                                        <datatables:column title="Amount Paid" name="amount-paid" property="amountPaid" sortable="false" renderFunction="custom-rendering#toPeso" cssCellClass="payment-amount"/>
                                        <datatables:column title="Payment Date" name="date" property="date" cssCellClass="payment-date"/>
                                        <datatables:column title="Edit" renderFunction="custom-rendering#readingActions" searchable="false" sortable="false"/>
                                        <datatables:column title="Audit" sortable="false" renderFunction="custom-rendering#audit"/>
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
                                        <cws:input id="or-num" name="payment.receiptNumber" label="OR Number" icon="money" placeholder="Enter or no." required="false" size="12"/>
                                    </div>
                                    <div class="col-sm-12 form-group">
                                        <cws:input id="amount-paid" name="payment.amountPaid" label="Amount" icon="money" placeholder="Enter amount" required="true" size="12"/>
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
        <script src="${WEB_JARS}jquery/2.0.3/jquery.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap-dialog.min.js"></script>
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