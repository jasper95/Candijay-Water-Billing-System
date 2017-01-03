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
            <div class="row" style="margin-bottom: 10px">
                <div class="col-sm-9">
                    <h2>Create Reading</h2>
                </div>
                <div class="col-sm-3 vertical-center text-right">
                    <button type="button" id="acc-tbr" class="btn btn-default">Accounts To Be Read</button>
                </div>
            </div>
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
                    <form:form method="post" cssClass="row" action="${pageContext.servletContext.contextPath}/admin/reading" modelAttribute="searchForm" id="fetchAccount">
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
                    <div class="col-sm-7">
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
                                        <img class="info-img" src="${STATIC_URL}img/current_reading.png">
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
                                <h3 class="panel-title">Recent Readings</h3>
                            </div>
                            <div class="panel-body">
                                <div class="table-responsive">
                                    <div id="acc-no" style="display: none;"></div>
                                    <a id="filterButton"></a>
                                    <a id="mrListReload"></a>
                                    <datatables:table cssClass="table table-striped" reloadSelector="#mrListReload" id="reading" filterPlaceholder="none" filterSelector="#filterButton" serverSide="true" url="${pageContext.servletContext.contextPath}/admin/reading/datatable-search" displayLength="3" dom="tp" >
                                        <datatables:column property="account.id" filterable="true" visible="false" selector="acc-no"/>
                                        <datatables:column property="id" sortInitOrder="0" sortInitDirection="desc" visible="false"/>
                                        <datatables:column title="Schedule" renderFunction="custom-rendering#monthAndYear" sortable="false" cssCellClass="schedule"/>
                                        <datatables:column title="Consumption" name="consume" property="consumption" sortable="false" cssCellClass="consumption"/>
                                        <datatables:column title="Reading" name="reading" property="readingValue" cssCellClass="reading" sortable="false"/>
                                        <datatables:column title="Status" name="invoice.status" property="invoice.status" sortable="false"/>
                                        <datatables:column title="Edit" renderFunction="custom-rendering#readingActions" searchable="false" sortable="false"/>
                                        <datatables:column title="Audit" sortable="false" renderFunction="custom-rendering#audit"/>
                                        <datatables:column title="Delete" sortable="false" renderFunction="custom-rendering#deleteItem"/>
                                        <datatables:extraJs bundles="months" placeholder="after_all"/>
                                        <dandelion:bundle excludes="jquery"/>
                                    </datatables:table>
                                    <input type="hidden" id="row-num">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-5">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">Add Reading</h3>
                            </div>
                            <div class="panel-body">
                                <form:form modelAttribute="meterReadingForm" method="post" id="add-meterReading-form">
                                    <jsp:include page="../fragments/postAuth/form-alerts.jsp"/>
                                    <form:hidden path="accountId"/>
                                    <div class="col-sm-12 form-group">
                                        <cws:select id="reading-month" name="meterReading.schedule.month" label="Reading Month" icon="calendar" required="true" placeholder="Select month" items="${monthOptions}" size="12"/>
                                    </div>
                                    <div class="col-sm-12 form-group">
                                        <cws:select id="reading-year" items="${yearOptions}" name="meterReading.schedule.year" label="Reading Year" icon="calendar" required="true" placeholder="Select year" size="12"/>
                                    </div>
                                    <div class="col-sm-12 form-group">
                                        <cws:input id="readingVal" name="meterReading.readingValue" label="Reading value" icon="tachometer" placeholder="Enter reading" required="true" size="12"/>
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
        <jsp:include page="../fragments/modals/reading-form.jsp"/>
        <jsp:include page="../fragments/modals/reading-info.jsp"/>
        <jsp:include page="../fragments/modals/no-reading-info.jsp"/>
        <script src="${WEB_JARS}jquery/2.0.3/jquery.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap-toggle.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap-dialog.min.js"></script>
        <script src="${STATIC_URL}js/helpers/form-validation.js"></script>
        <script src="${STATIC_URL}js/global.js"></script>
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
