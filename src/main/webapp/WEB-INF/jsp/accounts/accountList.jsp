<%-- 
    Document   : accountList
    Created on : May 9, 2015, 7:46:44 PM
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
        <title>Accounts</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_sticky-footer-navbar.css" rel="stylesheet">
        <script src="${WEB_JARS}jquery/2.0.3/jquery.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <sec:csrfMetaTags/>
    </head>
    <body>
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <div id="wrapper">
            <jsp:include page="../fragments/postAuth/sidebar.jsp"/>
            <div id="page-content-wrapper">
                <div class="page-content">
                    <div class="container-fluid">
                        <h2>Accounts</h2>
                        <div class="alert alert-info">
                            <div class="row">
                                <div class="col-md-3" id="acct-no">Acct #</div>
                                <div class="col-md-3" id="acct-lastname">Lastname</div>
                                <div class="col-md-3" id="acct-firstname">Firstname</div>
                                <div class="col-md-3" id="acct-brgy">Barangay</div>
                            </div>
                            <div class="row">
                                <div class="col-md-3" id="acct-status">Status</div>
                            </div>
                            <div class="row vertical-center">
                                <a id="filterButton" type="button" class="btn btn-primary"> Apply </a>
                                <a id="filterClearButton" type="button" class="btn btn-danger"> Clear </a>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 main">
                                <div class="table-responsive">
                                    <form:form id="form" modelAttribute="checkboxes" method="POST" action="${requestScope['javax.servlet.forward.request_uri']}">
                                        <datatables:table dom="ltipr" cssClass="table table-striped" id="account" url="/admin/accounts/datatable-search" serverSide="true" filterPlaceholder="none" filterSelector="#filterButton" filterClearSelector="#filterClearButton">
                                            <datatables:column renderFunction="custom-rendering#accountUrl" title="Acct No." name="number" filterable="true" property="number" sortable="false"/>
                                            <datatables:column name="lastname" title="Last Name" property="customer.lastname" sortInitOrder="0" sortInitDirection="asc"/>
                                            <datatables:column name="firstName" title="First Name" property="customer.firstName" sortInitOrder="1" sortInitDirection="asc"/>
                                            <datatables:column name="gender" title="Gender" property="customer.gender" sortable="false"/>
                                            <datatables:column name="balance" title="Balance" property="accountStandingBalance" renderFunction="custom-rendering#toPeso"/>
                                            <datatables:column name="contact" title="Contact" property="customer.contactNumber" sortable="false"/>
                                            <datatables:column name="address" title="Barangay" property="address.brgy" sortable="false"/>
                                            <datatables:column name="zone" title="Zone" property="address.locationCode" sortable="false"/>
                                            <datatables:column name="status" title="Status" property="status" sortable="false"/>
                                            <datatables:column visible="false" property="id" />
                                            <datatables:column  sortable="false" cssCellStyle="text-align:center;" renderFunction="custom-rendering#checkbox">
                                                <datatables:columnHead>
                                                    <input type="checkbox" id="master-checkbox" />
                                                </datatables:columnHead>
                                            </datatables:column>
                                            <dandelion:bundle excludes="jquery"/>
                                            <datatables:extraJs bundles="account" placeholder="before_end_document_ready" />
                                        </datatables:table>
                                    </form:form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="${STATIC_URL}js/accounts/list.js"></script>
    </body>
</html>
<%--<jsp:include page="../fragments/postAuth/header.jsp"/>
        <jsp:include page="../fragments/postAuth/sidebar.jsp"/>
            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                <h2 class="sub-header">
                    Accounts
                </h2>
                <div class="alert alert-info container" role="alert">
                    <p><strong>Filters:</strong></p>
                    <div class="row">
                        <div class="col-md-3" id="acct-no">Acct #</div>
                        <div class="col-md-3" id="acct-lastname">Lastname</div>
                        <div class="col-md-3" id="acct-firstname">Firstname</div>
                        <div class="col-md-3" id="acct-brgy">Barangay</div>
                    </div>
                    <div class="row">
                        <div class="col-md-3" id="acct-status">Status</div>
                    </div>
                    <br>
                    <a id="filterButton" type="button" class="btn btn-ctm btn-primary"> Apply </a>
                    <a id="filterClearButton" type="button" class="btn btn-ctm btn-info"> Clear </a>
                </div>
                <div class="table-stripedesponsive">
                        <div style="text-align:right; padding-bottom:1em;">
                            <select name="action" id="action">
                                <option value="" selected>Select action</option>
                                <option value="1">Generate Notice of Disconnection</option>
                                <option value="2">Deactivate Accounts</option>
                                <option value="3">Warning Accounts</option>
                                <option value="4">Activate Accounts</option>
                            </select>
                            <button id="apply" >Apply Action</button>
                        </div>
                        <form:form id="form" modelAttribute="checkboxes" method="POST" action="${requestScope['javax.servlet.forward.request_uri']}">
                            <datatables:table dom="ltipr" cssClass="table table-striped" id="account" url="/admin/accounts/datatable-search" serverSide="true" filterPlaceholder="none" filterSelector="#filterButton" filterClearSelector="#filterClearButton">
                                <datatables:column renderFunction="custom-rendering#accountUrl" title="Acct No." name="number" filterable="true" property="number" sortable="false"/>
                                <datatables:column name="lastname" title="Last Name" property="customer.lastname" sortInitOrder="0" sortInitDirection="asc"/>
                                <datatables:column name="firstName" title="First Name" property="customer.firstName" sortInitOrder="1" sortInitDirection="asc"/>
                                <datatables:column name="gender" title="Gender" property="customer.gender" sortable="false"/>
                                <datatables:column name="balance" title="Balance" property="accountStandingBalance" renderFunction="custom-rendering#toPeso"/>
                                <datatables:column name="contact" title="Contact" property="customer.contactNumber" sortable="false"/>
                                <datatables:column name="address" title="Barangay" property="address.brgy" sortable="false"/>
                                <datatables:column name="zone" title="Zone" property="address.locationCode" sortable="false"/>
                                <datatables:column name="status" title="Status" property="status" sortable="false"/>
                                <datatables:column visible="false" property="id" />
                                <datatables:column  sortable="false" cssCellStyle="text-align:center;" renderFunction="custom-rendering#checkbox">
                                    <datatables:columnHead>
                                        <input type="checkbox" id="master-checkbox" />
                                    </datatables:columnHead>
                                </datatables:column>
                                <dandelion:bundle excludes="jquery"/>
                                <datatables:extraJs bundles="account" placeholder="before_end_document_ready" />
                            </datatables:table>
                        </form:form>
                        <input type="hidden" id="context-path" value="${pageContext.servletContext.contextPath}"/>
                </div>
            </div>
        <jsp:include page="../fragments/footer.jsp"/>
        <script src="${STATIC_URL}js/accounts/list.js"></script>--%>