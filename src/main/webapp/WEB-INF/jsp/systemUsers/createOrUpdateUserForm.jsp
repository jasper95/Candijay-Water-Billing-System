<%-- 
    Document   : createOrUpdateUserForm
    Created on : Jan 21, 2016, 6:07:56 AM
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
        <title>${createOrUpdate} User</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_gen.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_dashboard.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_sticky-footer-navbar.css" rel="stylesheet">
        <sec:csrfMetaTags/>
        <spring:url value="/webjars/jquery/2.0.3/jquery.js" var="jQuery"/>
        <script src="${jQuery}"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
    </head>
    <body class="cstm">
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <jsp:include page="../fragments/postAuth/sidebar.jsp"/>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h2 class="sub-header" style="margin-top:15px;"><a type="button" href="#"><img src="${STATIC_URL}img/back.png"  style="margin-bottom:5px;" class=""></a>${createOrUpdate} User</h2>
            <form:form method="post" action="${requestScope['javax.servlet.forward.request_uri']}" modelAttribute="user">
                <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.user'].allErrors}">
                   <div class="alert alert-danger" style="border-radius:5px;"><br>
                       <h4>You got an error!</h4><hr>
                       <form:errors path="*" cssClass="error" />
                   </div>
                </c:if>
                <c:if test="${createOrUpdate == 'Create'}">
                    <!--form:errors path="fullName" element="div"/-->
                    <div class="navbar-form">
                        <cws:inputField cssStyle="background-image: url('${STATIC_URL}img/un.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="fullName" label="Full Name"/>
                        <cws:inputField cssStyle="background-image: url('${STATIC_URL}img/un.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="username" label="Username"/>
                    </div>
                    <div class="navbar-form">
                        <form:password class="form-control frm-ent-accnt-nmb log-un" cssStyle="background-image: url('${STATIC_URL}img/pw.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" path="password" placeholder="Password" id="password"/>
                        <input type="password" id="re-type" placeholder="Retype Password" class="form-control frm-ent-accnt-nmb log-un" style="background-image: url('${STATIC_URL}img/pw.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;"/>
                    </div>            
                </c:if>    
                Roles : <form:checkboxes path="roles" element="div" items="${roles}" itemValue="id" itemLabel="description"/>
                <input type="submit" value="Submit" onclick="return passwordCheck()"/>
            </form:form>
        </div>
        <jsp:include page="../fragments/footer.jsp"/>
        <script>
            function passwordCheck(){
                if(document.getElementById("password").value === document.getElementById("re-type").value)
                    return true;
                else alert("Password check does not match");
                return false;
            }
        </script>
    </body>
</html>
