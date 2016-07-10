<%-- 
    Document   : errors
    Created on : May 28, 2015, 10:35:05 AM
    Author     : Bert
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
   <jsp:include page="fragments/postAuth/headContents.jsp"/>
    <body>
        <jsp:include page="fragments/postAuth/header.jsp"/>
        <jsp:include page="fragments/postAuth/sidebar.jsp"/>
        <div class="container">
            <center><img src="/CWS/resources/img/sad.png"></center>
            <div class="page-header">
                <h1 style="color:#fff">${type}</h1>
            </div>
            <div class="alert alert-danger fade in" style="border-radius:10px;">
                <h3>${message}</h3>
            </div>
        </div>        
        <jsp:include page="fragments/footer.jsp"/>
    </body>
</html>
