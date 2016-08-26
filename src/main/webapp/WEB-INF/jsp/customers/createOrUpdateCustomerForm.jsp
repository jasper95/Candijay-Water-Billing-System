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
        <title>${customerForm.customer.id == null ? 'Create' : 'Update' } Customer</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_sticky-footer-navbar.css" rel="stylesheet">
        <link href="${WEB_JARS}jquery-ui/1.10.3/themes/base/jquery-ui.css" rel="stylesheet"/>
    </head>
    <body>
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-8 col-md-10">
                    <h2> ${customerForm.customer.id == null ? 'Create' : 'Update' } Customer
                    </h2>
                </div>
            </div>
            <form:form modelAttribute="customerForm" method="post" id="add-customer-form">
                <div class="form-wrapper">
                    <h3 >Personal Information</h3>
                    <div class="col-sm-12 form-group">
                        <cws:input id="cust-ln" name="customer.lastname" label="Lastname" icon="user" placeholder="Enter Lastname" required="true"/>
                        <cws:input id="cust-fn" name="customer.firstName" label="Firstname" icon="user" placeholder="Enter Firstname" required="true"/>
                        <cws:input id="cust-mn" name="customer.middleName" label="Middlename" icon="user" placeholder="Enter Middlename" required="true"/>
                    </div>
                    <div class="col-sm-12 form-group">
                        <cws:select id="cust-gn" name="customer.gender"  items="${genderOptions}" label="Gender" placeholder="Select Gender" icon="mars-stroke"  required="true"/>
                        <cws:input id="cust-bd" name="customer.birthDate" label="Birth Date" icon="birthday-cake" placeholder="Enter birth date" readOnly="true" required="true"/>
                        <cws:input id="cust-mc" name="customer.familyMembersCount" label="Household Members Count" placeholder="Enter members" icon="users" required="false"/>
                    </div>
                    <div class="col-sm-12 form-group">
                        <cws:input id="cust-cn" name="customer.contactNumber" label="Contact Number" icon="mobile" placeholder="Enter 10-digit #" required="false"/>
                        <cws:input id="cust-oc" name="customer.occupation" label="Occupation" icon="bank" placeholder="Enter occupation" required="false"/>
                    </div>
                </div>
            <c:if test="${customerForm.customer.id == null}">
                <div class="form-wrapper">
                    <h3>Account Information</h3>
                    <div class="col-sm-12 form-group">
                        <cws:input id="acc-mc" name="device.meterCode" label="Meter Code" icon="tachometer" placeholder="Enter meter code" required="true"/>
                        <cws:input id="acc-mb" name="device.brand" label="Meter Brand" icon="tachometer" placeholder="Enter meter brand" required="true"/>
                        <cws:select id="acc-bg" name="address.brgy" items2="${brgyOptions}" placeholder="Select brgy" label="Barangay" icon="home" required="true"/>
                    </div>
                    <div class="col-sm-12 form-group">
                        <cws:select id="ac-lc" name="address.locationCode" items2="${zoneOptions}" placeholder="Select Zone" label="Zone" icon="home" required="true"/>
                    </div>
                </div>
            </c:if>
                <button style="margin-right: 30px;" class="btn btn-primary btn-lg pull-right" type="submit">  Save  </button>
            </form:form>
        </div>
        <script src="${WEB_JARS}jquery/2.0.3/jquery.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${WEB_JARS}jquery-ui/1.10.3/ui/jquery.ui.core.js"></script>
        <script src="${WEB_JARS}jquery-ui/1.10.3/ui/jquery.ui.datepicker.js"></script>
        <script src="${STATIC_URL}js/helpers/form-validation.js"></script>
        <script src="${STATIC_URL}js/global.js"></script>
        <script>
            $(function () {
                $("#cust-bd").datepicker({ changeMonth: true, changeYear: true, yearRange : '-90:+0', dateFormat: 'yy/mm/dd'});
            });
        </script>
    </body>
</html>