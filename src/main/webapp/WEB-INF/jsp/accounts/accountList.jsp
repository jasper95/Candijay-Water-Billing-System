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
        <link href="${STATIC_URL}css/bootstrap-dialog.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
        <sec:csrfMetaTags/>
    </head>
    <body>
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <div id="content-loader" class="loader"></div>
        <div id="main-content" style="display:none" class="container">
            <h2>Accounts</h2>
            <div class="alert alert-info form-wrapper">
                <div class="col-sm-12 form-group">
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
                    <div class="col-md-3" id="acct-zone">
                        <label>Zone</label>
                    </div>
                    <div class="col-md-3" id="acct-status">
                        <label>Status</label>
                    </div>
                    <div class="col-md-3 pull-align-right vertical-center filter-btn-wrapper">
                        <a id="filterClearButton" type="button" class="btn btn-danger list-filter-btn"><i class="fa fa-remove fa-fw"></i> Reset </a>
                        <a id="filterButton" type="button" class="btn btn-primary list-filter-btn"><i class="fa fa-search fa-fw"></i> Search </a>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="pull-align-right col-sm-12 form-inline">
                    <div class="form-group">
                        <div class="form-group">
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
                                <select name="action" class="form-control" id="action">
                                    <option value="" selected>Select action</option>
                                    <option value="1">Generate Notice of Disconnection</option>
                                    <option value="2">Deactivate Accounts</option>
                                    <option value="3">Warning Accounts</option>
                                    <option value="4">Activate Accounts</option>
                                </select>
                            </div>
                        </div>
                        <button type="button" class="btn btn-success" id="apply" ><i class="fa fa-eyedropper fa-fw"></i>Apply</button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 main">
                    <div class="table-responsive">
                        <form:form id="form" modelAttribute="checkboxes" method="POST" action="${pageContext.servletContext.contextPath}/admin/accounts">
                            <datatables:table deferLoading="0" deferRender="true" dom="ltipr" cssClass="table table-striped" id="account" url="/admin/accounts/datatable-search" serverSide="true" filterPlaceholder="none" filterSelector="#filterButton" filterClearSelector="#filterClearButton">
                                <datatables:column  sortable="false" cssCellStyle="text-align:center;" renderFunction="custom-rendering#checkbox">
                                    <datatables:columnHead>
                                        <input type="checkbox" id="master-checkbox" />
                                    </datatables:columnHead>
                                </datatables:column>
                                <datatables:column renderFunction="custom-rendering#accountUrl" title="Acct No." name="number" filterable="true" property="number" sortable="false"/>
                                <datatables:column name="lastname" title="Last Name" property="customer.lastname" sortInitOrder="0" sortInitDirection="asc"/>
                                <datatables:column name="firstName" title="First Name" property="customer.firstName" sortInitOrder="1" sortInitDirection="asc"/>
                                <datatables:column name="gender" title="Gender" property="customer.gender" sortable="false"/>
                                <datatables:column name="balance" title="Balance" property="accountStandingBalance" renderFunction="custom-rendering#toPeso" sortable="false"/>
                                <datatables:column name="contact" title="Contact" property="customer.contactNumber" sortable="false"/>
                                <datatables:column name="address" title="Barangay" property="address.brgy" sortable="false"/>
                                <datatables:column name="zone" title="Zone" property="address.locationCode" sortable="false"/>
                                <datatables:column name="status" title="Status" property="status" sortable="false"/>
                                <datatables:column visible="false" property="id" />
                                <dandelion:bundle excludes="jquery"/>
                                <datatables:extraJs bundles="account" placeholder="before_end_document_ready" />
                                <datatables:extraJs bundles="session-timeout" placeholder="before_end_document_ready"/>
                            </datatables:table>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
        <script src="${WEB_JARS}jquery/2.0.3/jquery.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap-dialog.min.js"></script>
        <script src="${STATIC_URL}js/helpers/form-validation.js"></script>
        <script src="${STATIC_URL}js/global.js"></script>
        <script src="${STATIC_URL}js/helpers/reports-helper.js"></script>
        <script src="${STATIC_URL}js/accounts/list.js"></script>
        <script>
            $(document).ready(function(){
                $('#content-loader').hide()
                $('#main-content').show();
            });
        </script>
    </body>
</html>