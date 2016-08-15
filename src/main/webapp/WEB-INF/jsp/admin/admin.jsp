<%-- 
    Document   : admin
    Created on : May 15, 2015, 10:34:30 PM
    Author     : Bert
--%>
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
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h3 class="panel-title">System Overview</h3>
                                    </div>
                                    <div class="panel-body">
                                        <div class="col-sm-4">
                                            <div class="overview-wrapper">
                                                <em>Active Accounts</em>
                                                <span>12</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="overview-wrapper">
                                                <em>Inactive Accounts</em>
                                                <span>12</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="overview-wrapper">
                                                <em>Warning Accounts</em>
                                                <span>12</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="overview-wrapper">
                                                <em>Accounts with no Reading for Jan 2016</em>
                                                <span>12</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-6" style="padding-left: 0">
                                            <div class="overview-wrapper">
                                                <div class="col-sm-8">
                                                    <em>Reading Encoding Progress</em>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div class="progress" style="margin-bottom: 5px">
                                                        <div class="progress-bar progress-bar-success progress-bar-striped active" role="progressbar"
                                                             aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width:40%">
                                                            40%
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="overview-wrapper">
                                                <em>Accounts with no Payments for Jan 2016</em>
                                                <span>12</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-6" style="padding-left: 0">
                                            <div class="overview-wrapper">
                                                <div class="col-sm-8">
                                                    <em>Payments Encoding Progress</em>
                                                </div>
                                                <div class="col-sm-4">
                                                    <div class="progress" style="margin-bottom: 5px">
                                                        <div class="progress-bar progress-bar-success progress-bar-striped active" role="progressbar"
                                                             aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width:40%">
                                                            40%
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="overview-wrapper">
                                                <em>Active Users</em>
                                                <span>12</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="overview-wrapper">
                                                <em>Inactive Users</em>
                                                <span>12</span>
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
                                                <span>12</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="overview-wrapper">
                                                <em>Basic Rate</em>
                                                <span>12</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="overview-wrapper">
                                                <em>PES Rate</em>
                                                <span>12</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="overview-wrapper">
                                                <em>Depreciation Fund Rate</em>
                                                <span>12</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="overview-wrapper">
                                                <em>System Loss Rate</em>
                                                <span>12</span>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="overview-wrapper">
                                                <em>Consecutive Debts Allowed</em>
                                                <span>12</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
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
                                                <h3>Chart 1</h3>
                                                <canvas id="chart1" width="300" height="300"></canvas>
                                            </div>
                                            <div id="menu2" class="tab-pane fade">
                                                <h3>Chart 2</h3>
                                                <canvas id="chart2" width="300" height="300"></canvas>
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