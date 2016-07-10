<%-- 
    Document   : createOrUpdateCustomerForm
    Created on : Apr 29, 2015, 8:22:54 AM
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
        <title>${customerForm.customer.id == null ? 'Create' : 'Update' } Customer</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_gen.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_dashboard.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_sticky-footer-navbar.css" rel="stylesheet">
        <script src="${WEB_JARS}jquery/2.0.3/jquery.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${WEB_JARS}jquery-ui/1.10.3/ui/jquery.ui.core.js"></script>
        <script src="${WEB_JARS}jquery-ui/1.10.3/ui/jquery.ui.datepicker.js"></script>
        <link href="${WEB_JARS}jquery-ui/1.10.3/themes/base/jquery-ui.css" rel="stylesheet"/>
    </head>
    <body class="cstm">
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <jsp:include page="../fragments/postAuth/sidebar.jsp"/>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <c:set var="AddOrUpdate" value="${customerForm.customer.id == null ? 'Create' : 'Update' }"/>
            <c:choose >
                <c:when test="${customerForm.customer.id == null}">
                    <c:set var="AddOrUpdate" value="Create"/>
                    <c:url var="backUrl" value="/admin/customers"/>                    
                </c:when>
                <c:otherwise>
                    <c:set var="AddOrUpdate" value="Update"/>
                    <spring:url var="backUrl" value="/admin/customers/{customerId}">
                        <spring:param name="customerId" value="${customerForm.customer.id}"/>
                    </spring:url>
                </c:otherwise>
            </c:choose>
            <h2 class="sub-header" style="margin-top:15px;"><a type="button" href="${backUrl}"><img src="${STATIC_URL}img/back.png" style="margin-bottom:5px;" class=""></a> ${AddOrUpdate} Customer</h2>
            <form:form modelAttribute="customerForm" method="post" id="add-customer-form">
                <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.customerForm'].allErrors}">
                    <div class="alert alert-danger" style="border-radius:5px;"><br>
                        <h4>Oh snap! You got an error!</h4><hr>
                        <form:errors path="*" cssClass="error" />
                    </div>
                </c:if>
                <div class="table-stripedesponsive">
                    <div class="navbar-form" >
                        <cws:inputField cssStyle="background-image: url('${STATIC_URL}img/un.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="customer.lastname" label="Last Name"/>
                        <cws:inputField name="customer.firstName" label="First Name"/>
                        <cws:inputField name="customer.middleName" label="Middle Name"/>
                        <form:radiobutton path="customer.gender" value="M"/> Male
                        <form:radiobutton path="customer.gender" value="F"/> Female
                        </div>      
                    </div>
                    <div class="navbar-form" > 
                        <cws:inputField cssStyle="background-image: url('${STATIC_URL}img/birthdate.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="customer.birthDate" id="birthDate" label="Birthdate"/>            
                        <cws:inputField name="customer.familyMembersCount" label="Household count"/>
                    </div>
                    <div class="navbar-form" >
                        <cws:inputField cssStyle="background-image: url('${STATIC_URL}img/mobile.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="customer.contactNumber" label="Contact Number"/>
                        <cws:inputField cssStyle="background-image: url('${STATIC_URL}img/occupation.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="customer.occupation" label="Occupation"/>
                    </div>
                    <button type="submit" class="btn btn-primary">Submit</button>
                </div>
            </form:form>
        </div>
 
        <script>
            $(function () {
                $("#birthDate").datepicker({ dateFormat: 'yy/mm/dd'});
            });
        </script>
        <jsp:include page="../fragments/footer.jsp"/>
    </body>
</html>