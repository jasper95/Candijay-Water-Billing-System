<%-- 
    Document   : admin
    Created on : May 15, 2015, 10:34:30 PM
    Author     : Bert
--%>
<%@taglib uri = "http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="datatables" uri="http://github.com/dandelion/datatables" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<spring:url value="/resources/" var="STATIC_URL"/>
<!DOCTYPE html>
<html>
    <jsp:include page="fragments/postAuth/headContents.jsp"/>
    <body>
        <jsp:include page="fragments/postAuth/header.jsp"/>
        <jsp:include page="fragments/postAuth/sidebar.jsp"/>
        <!-- Page Content -->
        <div class="container">
            <img class="cnt-img" src="${STATIC_URL}img/center_piece.png">
            <p class="welcome-txt">Welcome to the Candijay Water Billing System Admin Page</p>
            <p class="welcome-sub">Thank you for using the system.</p>
        </div>
        </div>  
        <jsp:include page="fragments/footer.jsp"/>
    </body>
</html>
