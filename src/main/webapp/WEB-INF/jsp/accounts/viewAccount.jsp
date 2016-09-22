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
        <title>View Account</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/bootstrap-dialog.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
        <sec:csrfMetaTags/>
    </head>
    <body>
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <div class="container">
            <div class="row">
                <div class="col-sm-10 ">
                    <h2>View Account</h2>
                </div>
                <div class="col-sm-2  vertical-center">
                    <button type="button" class="btn btn-primary" id="update-acct-btn"><i class="fa fa-edit fa-fw"></i>Update</button>
                </div>
            </div>
        <c:if test="${updateSuccess == 1}">
            <div style="text-align: center" class="alert alert-success" style="border-radius:5px;"><br>
                <h4>This Account is successfully updated!</h4>
            </div>
        </c:if>
            <form:form modelAttribute="accountForm">
                <div class="form-wrapper">
                    <h3> <a href="${pageContext.servletContext.contextPath}/admin/customers/${accountForm.customerId}/"> Customer Information </a> </h3>
                    <div class="col-sm-12 form-group">
                        <cws:input id="cust-ln" name="account.customer.lastname" label="Lastname" icon="user" placeholder="Enter Lastname" required="false" readOnly="true"/>
                        <cws:input id="cust-fn" name="account.customer.firstName" label="Firstname" icon="user" placeholder="Enter Firstname" required="false" readOnly="true"/>
                        <cws:input id="cust-mn" name="account.customer.middleName" label="Middle" icon="user" placeholder="Enter Middlename" required="false" readOnly="true"/>
                    </div>
                </div>
                <div class="form-wrapper">
                    <h3>Account Information</h3>
                    <div class="col-sm-12 form-group">
                        <cws:input id="acct-no" name="account.number" label="Account number" icon="user" placeholder="" required="false" readOnly="true"/>
                        <cws:input id="acct-sb" name="account.accountStandingBalance" label="Standing Balance" icon="money" placeholder="" required="false" readOnly="true"/>
                        <cws:input id="acct-st" name="account.status" label="Status" icon="info" placeholder="" required="false" readOnly="true"/>
                    </div>
                    <div class="col-sm-12 form-group">
                        <cws:select id="acct-bg" name="address.brgy" items2="${brgyOptions}" placeholder="Select brgy" label="Barangay" icon="home" required="true" disabled="true"/>
                        <cws:select id="acct-lc" name="account.purok" items="${purokOptions}" placeholder="Select Purok" label="Purok" icon="home" required="true" disabled="true"/>
                    </div>
                </div>
            </form:form>
            <a id="reload"></a>
            <h3>Devices</h3>
            <div class="col-md-12 main">
                <div class="table-responsive">
                    <datatables:table dom="t" reloadSelector="#reload" cssClass="table table-striped" id="devices" url="${pageContext.servletContext.contextPath}/admin/accounts/${accountForm.account.number}/devices">
                        <datatables:column title="Meter Code" property="meterCode" sortable="false" sortInitDirection="false"/>
                        <datatables:column title="Meter Brand" property="brand" sortable="false" sortInitDirection="false"/>
                        <datatables:column title="Active" property="active" renderFunction="custom-rendering#trueToYes" sortable="false"/>
                        <datatables:column title="Last Reading" property="lastReading" sortable="false"/>
                        <datatables:column title="Activate" renderFunction="custom-rendering#activateDevice" sortable="false"/>
                        <datatables:column title="Edit" renderFunction="custom-rendering#editDevice" sortable="false"/>
                        <dandelion:bundle excludes="jquery"/>
                    </datatables:table>
                </div>
            </div>
            <input id="acct-base-url" type="hidden" value="${pageContext.servletContext.contextPath}/admin/accounts/${accountForm.account.number}"/>
            <input id="all-acct-base-url" type="hidden" value="${pageContext.servletContext.contextPath}/admin/accounts"/>
            <input id="device-action-url" type="hidden"/>
            <button type="button" id="btn-add-dv" style="margin-top: 20px"  class="btn btn-default pull-right"> Add Device </button>
        </div>
        <jsp:include page="../fragments/modals/device-form.jsp"/>
        <jsp:include page="../fragments/modals/acct-form.jsp"/>
        <script src="${WEB_JARS}jquery/2.0.3/jquery.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap-dialog.min.js"></script>
        <script src="${STATIC_URL}js/helpers/form-validation.js"></script>
        <script src="${STATIC_URL}js/global.js"></script>
        <script src="${STATIC_URL}js/accounts/view.js"></script>
    </body>
</html>
