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
            <img class="img-responsive center-block" src="${STATIC_URL}/img/center_piece.png">
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-msg center-block">${error} <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a></div>
            </c:if>
            <c:if test="${not empty msg}">
                <div class="alert alert-success alert-msg center-block">${msg} <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a></div>
            </c:if>
            <form id="loginForm"  action="${pageContext.servletContext.contextPath}/login" method="POST">
                <sec:csrfInput/>
                <div class="log-fields-wrapper center-block">
                    <div class="input-group">
                        <span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
                        <input name="username" type="text" class="form-control" placeholder="Username" autocomplete="off" required autofocus/>
                    </div>
                </div>
                <div class="log-fields-wrapper center-block">
                    <div class="input-group">
                        <span class="input-group-addon"><i class="fa fa-lock fa-fw"></i></span>
                        <input type="password" name="password" class="form-control" placeholder="Password" required autofocus/>
                    </div>
                </div>
                <div class="form-group">
                    <input type="submit" class="center-block btn btn-md btn-default btn-cstm log-cstm" value="LOG IN"/>
                </div>
            </form>
        </div>
        <jsp:include page="fragments/footer.jsp"/>
        <jsp:include page="fragments/preAuth/jsBlock.jsp"/>
        <script> history.pushState("", "", "${pageContext.servletContext.contextPath}/login")</script>
    </body>
</html>