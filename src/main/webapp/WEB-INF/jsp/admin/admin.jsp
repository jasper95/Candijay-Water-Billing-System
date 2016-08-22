<%@ include file="../fragments/postAuth/taglibs.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="icon" href="${STATIC_URL}img/cws.ico">
        <title>Admin</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
        <sec:csrfMetaTags/>
    </head>
    <body>
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <div id="wrapper">
            <jsp:include page="../fragments/postAuth/sidebar.jsp"/>
            <div id="page-content-wrapper">
                <div class="page-content">
                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-sm-12" style= "padding-top: 20px;">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h3 class="panel-title">Graphs Overview for 2016</h3>
                                    </div>
                                    <div class="panel-body">
                                        <ul id="chart-menu" class="nav nav-tabs">
                                            <li class="active"><a id="tab1" data-toggle="tab" href="#menu1">Collection, Collectibles and Expenses</a></li>
                                            <li><a data-toggle="tab" href="#menu2">Water Consumption</a></li>
                                        </ul>
                                        <div id="charts" class="tab-content">
                                            <div id="menu1" class="tab-pane fade in active">
                                                <canvas id="chart1" width="300" height="300"></canvas>
                                            </div>
                                            <div id="menu2" class="tab-pane fade">
                                                <canvas id="chart2" width="300" height="300"></canvas>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h3 class="panel-title">System Overview</h3>
                                    </div>
                                    <div class="panel-body">
                                        <div class="col-sm-4">
                                            <div class="overview-wrapper">
                                                <em>Active Accounts</em>
                                                <span>${activeAccounts}</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="overview-wrapper">
                                                <em>Inactive Accounts</em>
                                                <span>${inactiveAccounts}</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="overview-wrapper">
                                                <em>Warning Accounts</em>
                                                <span>${warningAccounts}</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="overview-wrapper">
                                                <em>Active Users</em>
                                                <span>${activeUsers}</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="overview-wrapper">
                                                <em>Inactive Users</em>
                                                <span>${inactiveUsers}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h3 class="panel-title">System Settings</h3>
                                    </div>
                                    <div class="panel-body">
                                        <div class="col-sm-4">
                                            <div class="overview-wrapper">
                                                <em>Penalty Rate</em>
                                                <span>${settings.penalty}</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="overview-wrapper">
                                                <em>Basic Rate</em>
                                                <span>${settings.basic}</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="overview-wrapper">
                                                <em>PES Rate</em>
                                                <span>${settings.pes}</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="overview-wrapper">
                                                <em>Depreciation Fund Rate</em>
                                                <span>${settings.depreciationFund}</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="overview-wrapper">
                                                <em>System Loss Rate</em>
                                                <span>${settings.systemLoss}</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="overview-wrapper">
                                                <em>Consecutive Debts Allowed</em>
                                                <span>${settings.debtsAllowed}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <input type="hidden" value="${pageContext.servletContext.contextPath}/admin/" id="admin-uri"/>
        <script src="${WEB_JARS}jquery/2.0.3/jquery.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${STATIC_URL}js/helpers/form-validation.js"></script>
        <script src="${STATIC_URL}js/global.js"></script>
        <script src="${STATIC_URL}js/Chart.bundle.min.js"></script>
        <script src="${STATIC_URL}js/admin/home.js"></script>
    </body>
</html>