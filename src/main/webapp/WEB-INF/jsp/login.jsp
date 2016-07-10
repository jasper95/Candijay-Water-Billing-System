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
            <center>
                <c:url value="/resources/img/center_piece.png" var="centerPiece"/>
                <img class="cnt-img" src="${centerPiece}">
                <c:if test="${not empty error}">
			<div class="alert-danger" style="width:25%">${error}</div>
		</c:if>
		<c:if test="${not empty msg}">
                    <div class="alert-success" style="width:25%">${msg}</div>
		</c:if>
            </center>
            <c:url value="/j_spring_security_check" var="loginUrl"/>
            <form id="loginForm" action="${loginUrl}" method="POST">
                <sec:csrfInput/>
                <center>
                    <!--p class="error-msg">INVALID USERNAME AND PASSWORD</p-->
                    <div class="navbar-form">
                        <input style="background-image: url('${STATIC_URL}img/un.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="j_username" type="text" class="form-control frm-ent-accnt-nmb log-un" placeholder="User name" required autofocus/>    
                    </div>
                    <div class="navbar-form">
                        <input style="background-image: url('${STATIC_URL}img/pw.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" type="password" name="j_password" class="form-control frm-ent-accnt-nmb log-pw" placeholder="Password" required autofocus/>    
                    </div>
                </center>
                <center>
                    <input type="submit" class="btn btn-sm btn-default btn-cstm log-cstm" value="LOG IN"/>
                </center>
            </form>
            <center><a class="fgt-log" href="">Forgot username or password?</a></center> 
        </div>
        <jsp:include page="fragments/footer.jsp"/>
        <jsp:include page="fragments/preAuth/jsBlock.jsp"/>
    </body>
</html>