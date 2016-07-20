<%-- 
    Document   : createOrUpdateMeterReading
    Created on : May 20, 2015, 6:39:33 PM
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
        <title>${createOrUpdate} Reading</title>
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
                        <h2>Create Reading</h2>
                        <hr/>
                        <form:form method="post" cssClass="row" action="${pageContext.servletContext.contextPath}/admin/reading" modelAttribute="searchForm" id="fetchAccount">
                            <div class="col-sm-4">
                                <div class="search-wrapper">
                                    <div class="search-in-wrapper">
                                        <div class="input-group">
                                            <span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
                                            <form:input id="acc-nb" path="accountNumber" class="form-control" required="true" placeholder="Enter Account no." autocomplete="off"/>
                                        </div>
                                        <span class="field-error"></span>
                                    </div>
                                    <button id="search-btn" type="submit" class="btn btn-primary btn-search"><i class="fa fa-search fa-fw"></i> Search </button>
                                </div>
                            </div>
                        </form:form>
                        <hr/>
                        <div id="crt-mr-found">
                            <div class="row">
                                <div class="col-sm-6">
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
                                                <datatables:table cssClass="table table-striped" id="reading" filterPlaceholder="none" filterSelector="#filterButton" serverSide="true" url="${pageContext.servletContext.contextPath}/admin/reading/datatable-search" displayLength="3" dom="tp" >
                                                    <datatables:column property="account.id" filterable="true" visible="false" selector="acc-no"/>
                                                    <datatables:column property="id" sortInitOrder="0" sortInitDirection="desc" visible="false"/>
                                                    <datatables:column title="Schedule" renderFunction="custom-rendering#monthAndYear" sortable="false"/>
                                                    <datatables:column title="Consumption" name="consume" property="consumption" sortable="false"/>
                                                    <datatables:column title="Reading" name="reading" property="readingValue" sortable="false"/>
                                                    <datatables:column title="Status" name="invoice.status" property="invoice.status" sortable="false"/>
                                                    <datatables:column title="Edit" renderFunction="custom-rendering#readingActions" searchable="false" sortable="false"/>
                                                    <datatables:extraJs bundles="months" placeholder="after_all"/>
                                                    <dandelion:bundle excludes="jquery"/>
                                                </datatables:table>
                                                <input type="hidden" id="row-num">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-6">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h3 class="panel-title">Add Reading</h3>
                                        </div>
                                        <div class="panel-body">
                                            <form:form modelAttribute="meterReadingForm" method="post" id="add-meterReading-form">
                                                <div class="col-sm-12 alert alert-danger global-errors"></div>
                                                <div class="col-sm-12 alert alert-success success-msg"></div>
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
                </div>
            </div>
        </div>
        <jsp:include page="../fragments/modals/reading-form.jsp"/>
        <script src="${WEB_JARS}jquery/2.0.3/jquery.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${STATIC_URL}js/helper.js"></script>
        <script src="${STATIC_URL}js/form-validation.js"></script>
        <script src="${STATIC_URL}js/meter-reading/create.js"></script>
        <script src="${STATIC_URL}js/meter-reading/edit.js"></script>
    </body>
</html>
