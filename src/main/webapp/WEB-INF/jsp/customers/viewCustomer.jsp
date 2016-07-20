<%-- 
    Document   : viewCustomer
    Created on : May 9, 2015, 9:20:49 PM
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
    <title>View Customer</title>
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
                        <div class="col-sm-10 ">
                            <h2>
                                <a type="button" href="${pageContext.servletContext.contextPath}/admin/customers">
                                    <img src="${STATIC_URL}img/back.png">
                                </a>View Customer
                            </h2>
                        </div>
                        <div class="col-sm-2  vertical-center">
                            <a type="button" class="btn btn-primary" href="${pageContext.servletContext.contextPath}/admin/customers/${customerForm.customer.id}/update">Update</a>
                        </div>
                    </div>
                <c:if test="${updateSuccess == 1}">
                    <div style="text-align: center" class="alert alert-success" style="border-radius:5px;"><br>
                        <h4>This customer is successfully updated!</h4>
                    </div>
                </c:if>
                    <form:form modelAttribute="customerForm" id="add-customer-form">
                        <div class="form-wrapper">
                            <h3 >Personal Information</h3>
                            <div class="col-sm-12 form-group">
                                <cws:input id="cust-ln" name="customer.lastname" label="Lastname" icon="user" placeholder="Enter Lastname" required="true" readOnly="true"/>
                                <cws:input id="cust-fn" name="customer.firstName" label="Firstname" icon="user" placeholder="Enter Firstname" required="true" readOnly="true"/>
                                <cws:input id="cust-mn" name="customer.middleName" label="Middle" icon="user" placeholder="Enter Middlename" required="true" readOnly="true"/>
                            </div>
                            <div class="col-sm-12 form-group">
                                <cws:select id="cust-gn" name="customer.gender"  items="${genderOptions}" label="Gender" placeholder="Select Gender" icon="mars-stroke" required="true" disabled="true"/>
                                <cws:input id="cust-bd" name="customer.birthDate" label="Birth Date" icon="birthday-cake" placeholder="Enter birth date" required="true" readOnly="true"/>
                                <cws:input id="cust-mc" name="customer.familyMembersCount" label="Members Count" placeholder="Enter members" icon="users" required="false" readOnly="true"/>
                            </div>
                            <div class="col-sm-12 form-group">
                                <cws:input id="cust-cn" name="customer.contactNumber" label="Contact Number" icon="mobile" placeholder="Enter 10-digit #" required="false" readOnly="true"/>
                                <cws:input id="cust-oc" name="customer.occupation" label="Occupation" icon="bank" placeholder="Enter occupation" required="false" readOnly="true"/>
                            </div>
                        </div>
                    </form:form>
                    <h3>Accounts</h3>
                    <div class="col-md-12 main">
                        <div class="table-responsive">
                            <datatables:table dom="t" cssClass="table table-striped" reloadSelector="#reload" id="accounts" url="${requestScope['javax.servlet.forward.request_uri']}/accounts">
                                <datatables:column name="id" property="id" visible="false" sortInitDirection="false"/>
                                <datatables:column renderFunction="custom-rendering#accountUrl" title="Acct No." name="number" property="number" sortable="false" sortInitDirection="false"/>
                                <datatables:column name="address" title="Barangay" property="address.brgy" sortable="false"/>
                                <datatables:column name="zone" title="Zone" property="address.locationCode" sortable="false"/>
                                <datatables:column name="status" title="Status" property="status" sortable="false"/>
                                <datatables:column title="Action" renderFunction="custom-rendering#activateAccount" sortable="false"/>
                                <dandelion:bundle excludes="jquery"/>
                            </datatables:table>
                        </div>
                    </div>
                    <input type="hidden" id="context-path" value="${pageContext.servletContext.contextPath}"/>
                    <button type="button" style="margin-top: 20px" class="btn btn-default pull-right" data-toggle="modal" data-target="#acct-form-modal"> Add Account </button>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="../fragments/modals/acct-form.jsp"/>
    <script src="${WEB_JARS}jquery/2.0.3/jquery.js"></script>
    <script src="${STATIC_URL}js/bootstrap.min.js"></script>
    <script src="${STATIC_URL}/js/form-validation.js"></script>
    <script src="${STATIC_URL}/js/customers/view.js"></script>
</body>
</html>