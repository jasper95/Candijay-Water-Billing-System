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
        <title>Reports</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/bootstrap-toggle.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/bootstrap-dialog.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
        <sec:csrfMetaTags/>
    </head>
    <body>
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <div class="container">
            <div class="row">
                <div class="col-sm-6 col-centered">
                    <h2>Generate Reports</h2>
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">Reports Form</h3>
                        </div>
                        <div class="panel-body">
                            <form id="main-form">
                                <jsp:include page="../fragments/postAuth/form-alerts.jsp"/>
                                <div class="col-sm-12 form-group">
                                    <div class="col-md-12 has-feedback ">
                                        <label class="control-label" for="category">Report Category <span style="color:red">&#42</span></label>
                                        <div class="input-group">
                                            <span class="input-group-addon"><i class="fa fa-home fa-fw"></i></span>
                                            <select id="category" name="brgy" autocomplete="off" class="form-control input-md">
                                                <option value="">--Select category--</option>
                                                <option value="1">Customers Accountability</option>
                                                <option value="2">Tables</option>
                                                <option value="3">Charts</option>
                                            </select>
                                            <span class="glyphicon  form-control-feedback" aria-hidden="true"></span>
                                        </div>
                                    </div>
                                </div>
                            </form>
                            <form:form modelAttribute="addressForm" id="acctblityForm" cssStyle="display: none">
                                <form:hidden id="print-brgy" path="printBrgy" value="1"/>
                                <div class="col-sm-12 form-group">
                                    <cws:select id="ad-type" name="type" label="Report Type" icon="bar-chart" required="true" placeholder="Select type" size="12" items="${typeOptionsAcctblty}"/>
                                </div>
                                <div class="col-sm-12 form-group barangay">
                                    <cws:select id="ad-barangay" name="barangay" items2="${brgyOptions}" placeholder="Select brgy" label="Barangay" icon="home" required="true" size="12"/>
                                </div>
                                <div class="col-sm-12 form-group zone" style="display:none;">
                                    <cws:select id="ad-zone" name="zone" label="Zone" icon="home" required="true" placeholder="Select zone" items2="${zoneOptions}" size="12"/>
                                </div>
                                <div class="col-sm-12 form-group">
                                    <div class="col-sm-4">
                                        <label class="control-label" style="display:block">Print By</label>
                                        <input id="acctability-toggle" type="checkbox" checked data-width="100px" data-toggle="toggle" data-on="Barangay" data-off="Zone" data-onstyle="warning" data-offstyle="info">
                                    </div>
                                </div>
                            </form:form>
                            <form:form modelAttribute="reportForm" id="reportForm" cssStyle="display: none">
                                <form:hidden value="0" id="summary-status" path="summary"/>
                                <div class="col-sm-12 form-group">
                                    <cws:select id="rp-type" name="type" label="Report Type" icon="bar-chart" required="true" placeholder="Select type" size="12" items="${typeOptionsReport}"/>
                                </div>
                                <div class="col-sm-12 form-group">
                                    <cws:select id="rp-year" items="${yearOptions}" name="year" label="Year" icon="calendar" required="true" placeholder="Select year" size="12"/>
                                </div>
                                <div class="col-sm-12 form-group">
                                    <cws:select id="rp-month" name="month" label="Month" icon="calendar" required="true" placeholder="Select month" items="${monthOptions}" size="12"/>
                                </div>
                                <div class="col-sm-12 form-group barangay">
                                    <cws:select id="rp-barangay" name="barangay" items2="${brgyOptions}" placeholder="Select brgy" label="Barangay" icon="home" required="true" size="12"/>
                                </div>
                                <div class="col-sm-12 form-group">
                                    <div class="col-sm-4">
                                        <label class="control-label" style="display:block">Summary</label>
                                        <input id="summary-toggle" type="checkbox" data-width="100px" data-toggle="toggle" data-on="<i class='fa fa-check'></i> Enabled" data-off="<i class='fa fa-remove'></i> Disabled">
                                    </div>
                                </div>
                            </form:form>
                            <form:form modelAttribute="chartForm" id="chartForm" cssStyle="display:none">
                                <div class="col-sm-12 form-group">
                                    <cws:select id="ch-type" name="type" label="Report Type" icon="bar-chart" required="true" placeholder="Select type" size="12" items="${typeOptionsChart}"/>
                                </div>
                                <div class="col-sm-12 form-group">
                                    <cws:select id="ch-year" items="${yearOptions}" name="year" label="Year" icon="calendar" required="true" placeholder="Select year" size="12"/>
                                </div>
                            </form:form>
                            <input type="hidden" value="${pageContext.servletContext.contextPath}/admin/reports" id="form-action"/>
                            <div class="col-sm-12 form-group">
                                <div class="form-group">
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <div class="pull-right">
                                            <button id="submit" class="btn btn-primary" disabled>Generate</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="${WEB_JARS}jquery/2.0.3/jquery.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap-dialog.min.js"></script>
        <script src="${STATIC_URL}js/helpers/form-validation.js"></script>
        <script src="${STATIC_URL}js/global.js"></script>
        <script src="${STATIC_URL}js/bootstrap-toggle.min.js"></script>
        <script src="${STATIC_URL}js/helpers/reports-helper.js"></script>
        <script src="${STATIC_URL}js/reports/reports.js"></script>
    </body>
</html>