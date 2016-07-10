<%-- 
    Document   : resetProfile
    Created on : Jan 24, 2016, 9:41:53 PM
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
        <title>Edit Profile</title>
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
            <h2 class="sub-header" style="margin-top:15px;">Update Profile</h2>
            <div>
                <form:form id="name-edit" action="${pageContext.servletContext.contextPath}/admin/update-name"  modelAttribute="user">
                    Full name<form:input path="fullName" required="true" />
                    <input type="submit" value="Edit"/>
                </form:form>
            </div>
            <div>
                <form:form action="${pageContext.servletContext.contextPath}/admin/update-password" id="password-edit">
                    Current:<input type="password" name="current" required="true"/>
                    New:<input type="password" name="new" id="new-pass"/>
                    Re-type new:<input type="password" name="retype-new" id="pass-check"/>
                    <input type="submit" value="Edit" disabled="true" id="pass-submit"/>
                </form:form>
            </div>
        </div>
        <script>
            $(document).ready(function(){
                $('#password-edit').on('submit', function(e){
                    e.preventDefault();
                    $.post($(this).attr('action'), $(this).serialize(), function(response){
                        console.log(response)
                    });
                });
                $('#name-edit').on('submit', function(e){
                    e.preventDefault();
                    $.post($(this).attr('action'), $(this).serialize(), function(response){
                        console.log(response)
                    });  
                });
                $('#pass-check').keyup(function(e){
                    passFormCheck();     
                });
                $('#new-pass').keyup(function(e){
                    passFormCheck();
                });
                function passFormCheck(){
                    if($('#pass-check').val() != '' && $('#pass-check').val() === $('#new-pass').val())
                        $('#pass-submit').prop('disabled', false);
                    else $('#pass-submit').prop('disabled', true);
                }
            });
        </script>
        <jsp:include page="../fragments/footer.jsp"/>  
    </body>
</html>
