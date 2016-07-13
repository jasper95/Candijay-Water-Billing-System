<%-- 
    Document   : login
    Created on : Apr 30, 2015, 8:03:50 AM
    Author     : Bert
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:url value="/resources/" var="STATIC_URL"/>
<html lang="en">
    
    <jsp:include page="fragments/preAuth/headContents.jsp"/>
    <body>
        <jsp:include page="fragments/preAuth/header.jsp"/>
        <!-- Page Content -->
        <div class="container">
            <img class="cnt-img" src="${STATIC_URL}/img/center_piece.png">
            <div style="text-align: center">
            <c:if test="${not empty error}">
                <div class="alert-danger" style="width:25%">${error}</div>
            </c:if>
            <c:if test="${not empty msg}">
                <div class="alert-success" style="width:25%">${msg}</div>
            </c:if>
            <c:url value="/j_spring_security_check" var="loginUrl"/>
                <form id="loginForm"  action="${loginUrl}" method="POST">
                    <sec:csrfInput/>
                    <div class="form-group">
                        <input style="background-image: url('${STATIC_URL}img/un.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="j_username" type="text" class="form-control log-un" placeholder="Username" autocomplete="off" required autofocus/>
                    </div>
                    <div class="form-group">
                        <input style="background-image: url('${STATIC_URL}img/pw.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" type="password" name="j_password" class="form-control log-un" placeholder="Password" required autofocus/>
                    </div>
                    <div class="form-group">
                        <input type="submit" class="btn btn-sm btn-default btn-cstm log-cstm" value="LOG IN"/>
                    </div>
                </form>
            </div>
        </div>
        <jsp:include page="fragments/footer.jsp"/>
        <jsp:include page="fragments/preAuth/jsBlock.jsp"/>
    </body>
</html>