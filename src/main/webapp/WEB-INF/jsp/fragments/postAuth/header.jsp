<%-- 
    Document   : header
    Created on : May 9, 2015, 12:14:21 AM
    Author     : Bert
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<spring:url value="/resources/img/admin_mini.png" var="adminIcon"/>
<%@taglib uri = "http://java.sun.com/jsp/jstl/core" prefix="c" %>
<spring:url value="/resources/img/dp.png" var="dp"/>
<!-- Fixed navbar -->
<div id="header" class="navbar-inverse navbar-fixed-top">
    <div class="container-fluid ">
        <a class="navbar-brand" href="#">Candijay Water Billing System</a>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a class="dropdown-toggle" role="button" data-toggle="dropdown" href="#"><img src="${adminIcon}"> <sec:authentication property="principal.username"/><span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="#" id="update-profile-link">Update Profile</a></li>
                        <li><a href="#" onclick="logout();">Logout</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
    <!-- /container -->
</div>
<jsp:include page="../modals/update-profile-form.jsp"/>
<form id="logoutForm" action="${pageContext.servletContext.contextPath}/logout.htm" method="post">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
<script>  
    function logout(){
        document.getElementById('logoutForm').submit();
    }
</script>