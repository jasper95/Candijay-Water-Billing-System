<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
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
        <title>${createOrUpdate} Reading</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/bootstrap-toggle.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/bootstrap-dialog.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
        <sec:csrfMetaTags/>
    </head>
    <body>
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <div id="content-loader" class="loader"></div>
        <div id="main-content" style="display: none;" class="container">
            <div class="row">
                <div class="col-md-9">
                    <h2 style="margin-top: 0px; margin-bottom: 5px">Create Reading</h2>
                </div>
                <div class="col-md-3 text-right">
                    <button type="button" id="acc-tbr" class="btn btn-default">Accounts To Be Read</button>
                </div>
            </div>
            <div class="row">
                <div id="search-filters2" style="margin-bottom: 10px" class="col-md-6 alert alert-success form-wrapper">
                    <div class="col-md-12">
                        <div class="col-md-4" id="acct-no">
                            <label>Account Number</label>
                        </div>
                        <div class="col-md-4" id="acct-lastname">
                            <label>Lastname</label>
                        </div>
                        <div class="col-md-4" id="acct-firstname">
                            <label>Firstname</label>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="col-md-4" id="acct-brgy">
                            <label>Barangay</label>
                        </div>
                        <div class="col-md-4" id="acct-zone">
                            <label>Purok</label>
                        </div>
                        <div class="col-md-4 pull-align-right vertical-center filter-btn-wrapper">
                            <a id="filterClearButton2" type="button" style="padding: 6px 4px 6px 0px" class="btn btn-danger list-filter-btn"><i class="fa fa-remove fa-fw"></i> Reset </a>
                            <a id="filterButton2" style="padding: 6px 4px 6px 0px" type="button" class="btn btn-primary list-filter-btn"><i class="fa fa-search fa-fw"></i> Search </a>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 main">
                    <div class="col-md-12">
                         <div id="search-results" class="panel panel-default">
                             <div class="panel-heading">
                                 <h3 class="panel-title">Search Results</h3>
                             </div>
                             <div class="panel-body panel-body-custom">
                                <div class="table-responsive">
                                    <datatables:table dom="tpi" displayLength="3" cssClass="table table-striped" id="account" url="${spring:mvcUrl('datatables-api#accounts').build()}" serverSide="true" filterPlaceholder="none" filterSelector="#filterButton2" filterClearSelector="#filterClearButton2">
                                        <datatables:column title="Acct No." name="number" filterable="true" property="number" renderFunction="custom-rendering#chooseAccount" sortable="false"/>
                                        <datatables:column name="lastname" title="Last Name" property="customer.lastname" sortInitOrder="0" sortInitDirection="asc" sortable="false"/>
                                        <datatables:column name="firstName" title="First Name" property="customer.firstName" sortInitOrder="1" sortInitDirection="asc" sortable="false"/>
                                        <datatables:column name="address" title="Barangay" property="address.brgy" visible="false" sortable="false"/>
                                        <datatables:column name="purok" property="purok" visible="false" sortable="false"/>
                                        <datatables:column visible="false" property="id" />
                                        <dandelion:bundle excludes="jquery"/>
                                        <datatables:callback type="info" function="autoSelectAccountCallback"/>
                                        <datatables:extraJs bundles="search-helper" placeholder="before_end_document_ready" />
                                        <datatables:extraJs bundles="session-timeout" placeholder="before_end_document_ready"/>
                                    </datatables:table>
                                </div>
                             </div>
                         </div>
                    </div>
                    <div class="col-md-12">
                        <div class="reading-ready panel panel-default" style="display: none; margin-bottom: 10px">
                            <div class="panel-heading clearfix">
                                <a id="back-results" class="btn btn-sm btn-default pull-right"><i class="fa fa-arrow-left fa-fw"></i>Back to Results</a>
                                <h3 style="padding-top: 10px" class="panel-title">Account Details</h3>
                            </div>
                            <div class="panel-body panel-body-custom">
                                <div class="col-md-6">
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
                                <div class="col-md-6">
                                    <div class="info-wrapper">
                                        <img class="info-img" src="${STATIC_URL}img/current_reading.png">
                                        <div class="info-text">
                                            <span id="last-reading"></span>
                                        </div>
                                    </div>
                                    <div class="info-wrapper">
                                        <img class="info-img" src="${STATIC_URL}img/status.png">
                                        <div class="info-text">
                                            <span id="status"></span>
                                            <a id="activate-acct-btn" style="display: none;" title="Activate Account" type="button" class="btn btn-xs btn-success"><i class="fa fa-bolt fa-fw"></i>Activate</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <form:form id="activate-account-form">
                        <input type="hidden" name="id" id="activate-account-input" />
                    </form:form>
                    <form:form method="post" cssClass="row" action="${pageContext.servletContext.contextPath}/admin/reading" modelAttribute="searchForm" id="fetchAccount">
                        <jsp:include page="../fragments/postAuth/form-alerts.jsp"/>
                        <form:hidden id="acc-nb" path="accountNumber"/>
                    </form:form>
                </div>
            </div>
            <div id="crt-mr-found" class="reading-ready" tabindex="0">
                <div class="row">
                    <div class="col-md-4">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">Add Reading</h3>
                            </div>
                            <div class="panel-body panel-body-custom">
                                <form:form modelAttribute="meterReadingForm" method="post" id="add-meterReading-form">
                                    <jsp:include page="../fragments/postAuth/form-alerts.jsp"/>
                                    <form:hidden path="accountId"/>
                                    <div class="col-md-12 form-group">
                                        <cws:select id="reading-month" name="meterReading.schedule.month" label="Reading Month" icon="calendar" required="true" placeholder="Select month" items="${monthOptions}" size="12"/>
                                    </div>
                                    <div class="col-md-12 form-group">
                                        <cws:select id="reading-year" items="${yearOptions}" name="meterReading.schedule.year" label="Reading Year" icon="calendar" required="true" placeholder="Select year" size="12"/>
                                    </div>
                                    <div class="col-md-9 form-group">
                                        <cws:input id="readingVal" name="meterReading.readingValue" label="Reading value" icon="tachometer" placeholder="Enter reading" required="true" size="12" moreClasses="is-number"/>
                                    </div>
                                    <div>
                                        <span><strong><em>Consumed</em></strong>:</span>
                                        <div style="display:inline-block">
                                           <span id="reading-consumption" style="font-size: 20px"><strong></strong></span>
                                        </div>
                                    </div>
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <div class="col-md-offset-2 col-md-10">
                                                <div class="pull-right">
                                                    <button class="btn btn-primary" id="cr-reading-submit" type="submit">Save</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                    <div id="bill-details-panel" class="col-md-4">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">Bill Details</h3>
                            </div>
                            <div class="panel-body panel-body-custom">
                                <div class="bill-detail-group clearfix">
                                    <div class="col-md-12">
                                        <span class="bd-group-header">Schedule</span>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="bill-detail-wrapper">
                                            <span class="bd-prop-h"><em>Month</em>:</span>
                                            <span id="bd-month" class="bd-val"></span>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="bill-detail-wrapper">
                                            <span class="bd-prop-h"><em>Year</em>:</span>
                                            <span id="bd-year" class="bd-val"></span>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="bill-detail-wrapper">
                                            <span class="bd-prop-h"><em>Due Date</em>:</span>
                                            <span id="bd-due-date" class="bd-val"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="bill-detail-group clearfix">
                                    <div class="col-md-12">
                                        <span class="bd-group-header">Water Consumption(Cu.m.)</span>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="bill-detail-wrapper">
                                            <span class="bd-prop-h"><em>Previous</em>:</span>
                                            <span id="bd-previous" class="bd-val"></span>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="bill-detail-wrapper">
                                            <span class="bd-prop-h"><em>Present</em>:</span>
                                            <span id="bd-present" class="bd-val"></span>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="bill-detail-wrapper">
                                            <span class="bd-prop-h"><em>Consumed</em>:</span>
                                            <span id="bd-consumed" class="bd-val"></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6 bill-detail-group">
                                    <span class="bd-group-header">Current Charges</span>
                                    <div class="bill-detail-wrapper">
                                        <span class="bd-prop-v"><em>Basic</em>:</span>
                                        <span id="bd-basic" class="bd-val pull-right"></span>
                                    </div>
                                    <div class="bill-detail-wrapper">
                                        <span class="bd-prop-v"><em>System Loss</em>:</span>
                                        <span id="bd-sys-loss" class="bd-val pull-right"></span>
                                    </div>
                                    <div class="bill-detail-wrapper">
                                        <span class="bd-prop-v"><em>Depreciation Fund</em>:</span>
                                        <span id="bd-dep-fund" class="bd-val pull-right"></span>
                                    </div>
                                    <div class="bill-detail-wrapper">
                                        <span class="bd-prop-v"><em>PES</em>:</span>
                                        <span id="bd-pes" class="bd-val pull-right"></span>
                                    </div>
                                    <div class="bill-detail-wrapper">
                                        <span class="bd-prop-v"><em>Total Current</em>:</span>
                                        <span id="bd-total-current1" class="bd-val pull-right"></span>
                                    </div>
                                    <div class="bill-detail-wrapper">
                                        <span class="bd-prop-v"><em>Status</em>:</span>
                                        <span id="bd-status" class="bd-val pull-right"></span>
                                    </div>
                                </div>
                                <div class="col-md-6 bill-detail-group">
                                    <span class="bd-group-header">Summary</span>
                                    <div class="bill-detail-wrapper">
                                        <span class="bd-prop-v"><em>Total Current</em>:</span>
                                        <span id="bd-total-current2" class="bd-val pull-right"></span>
                                    </div>
                                    <div class="bill-detail-wrapper">
                                        <span class="bd-prop-v"><em>Penalty</em>:</span>
                                        <span id="bd-penalty" class="bd-val pull-right"></span>
                                    </div>
                                    <div class="bill-detail-wrapper" style="color: red">
                                        <span class="bd-prop-v"><em>Arrears</em>:</span>
                                        <span id="bd-arrears" class="bd-val pull-right"></span>
                                    </div>
                                    <div class="bill-detail-wrapper">
                                        <span class="bd-prop-v"><em>Discount</em>:</span>
                                        <span id="bd-discount" class="bd-val pull-right"></span>
                                    </div>
                                    <div class="bill-detail-wrapper">
                                        <span class="bd-prop-v"><em>Total Due</em>:</span>
                                        <span id="bd-total-due" class="bd-val pull-right"></span>
                                    </div>
                                    <div class="bill-detail-wrapper" style="font-weight: 900">
                                        <span class="bd-prop-v"><em>Balance</em>:</span>
                                        <span id="bd-unpaid-due" class="bd-val pull-right"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">Recent Bills</h3>
                            </div>
                            <div class="panel-body panel-body-custom">
                                <div class="table-responsive">
                                    <div id="acc-no" style="display: none;"></div>
                                    <a id="filterButton"></a>
                                    <a id="mrListReload"></a>
                                    <datatables:table cssClass="table table-striped" reloadSelector="#mrListReload" id="reading" filterPlaceholder="none" filterSelector="#filterButton" serverSide="true" url="${spring:mvcUrl('datatables-api#readings').build()}" displayLength="3" dom="tpi" >
                                        <datatables:column property="account.id" filterable="true" visible="false" selector="acc-no"/>
                                        <datatables:column property="id" sortInitOrder="0" sortInitDirection="desc" visible="false"/>
                                        <datatables:column title="Bill" renderFunction="custom-rendering#scheduleAndStatus" sortable="false" cssCellClass="schedule"/>
                                        <datatables:column title="Actions" sortable="false" renderFunction="custom-rendering#recentReadingsActions"/>
                                        <datatables:callback type="info" function="autoClickLatestBillCallback"/>
                                        <dandelion:bundle excludes="jquery"/>
                                    </datatables:table>
                                    <button type="button" id="payment-history-btn" class="btn btn-default">Prev. Payments</button>
                                    <input type="hidden" id="last-reading-reference">
                                    <input type="hidden" id="row-num">
                                    <input type="hidden" id="readings-uri" value="${pageContext.servletContext.contextPath}/admin/reading/">
                                    <input type="hidden" id="accounts-uri" value="${pageContext.servletContext.contextPath}/admin/accounts/">
                                    <input type="hidden" id="bills-uri" value="${pageContext.servletContext.contextPath}/admin/bills/">
                                    <input type="hidden" id="payments-uri" value="${pageContext.servletContext.contextPath}/admin/payments/">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="../fragments/modals/reading-form.jsp"/>
        <jsp:include page="../fragments/modals/reading-info.jsp"/>
        <jsp:include page="../fragments/modals/no-reading-info.jsp"/>
        <jsp:include page="../fragments/modals/bill-discount-form.jsp"/>
        <jsp:include page="../fragments/modals/recent-payments-info.jsp"/>
        <script src="${WEB_JARS}jquery/2.0.3/jquery.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap-toggle.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap-dialog.min.js"></script>
        <script src="${STATIC_URL}js/helpers/form-validation.js"></script>
        <script src="${STATIC_URL}js/global.js"></script>
        <script src="${STATIC_URL}js/helpers/reports-helper.js"></script>
        <script src="${STATIC_URL}js/helpers/discount-helper.js"></script>
        <script src="${STATIC_URL}js/helpers/search-helper.js"></script>
        <script src="${STATIC_URL}js/meter-reading/create.js"></script>
        <script src="${STATIC_URL}js/meter-reading/edit.js"></script>
        <script>
            $(document).ready(function(){
                $('#content-loader').hide()
                $('#main-content').show();
            });
        </script>
    </body>
</html>
