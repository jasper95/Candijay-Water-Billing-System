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
<div id="top-nav" class="navbar navbar-inverse navbar-static-top" style="z-index:1500; background-color:#000915; position:fixed; width: 100%">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <a class="navbar-brand" href="" style="margin-left:50px;">Candijay Water Billing System</a> 
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right" style="margin-right:10px;">
                <li class="dropdown">
                    <a class="dropdown-toggle" role="button" data-toggle="dropdown" href="#"><img src="${adminIcon}"> <sec:authentication property="principal.type"/> <span class="caret"></span></a>
                    <ul id="g-account-menu" class="dropdown-menu" role="menu">
                        <li>
                          <center><img src="${dp}"></center>
                          <center><p><strong><sec:authentication property="principal.username"/></strong></p></center>
                          <a href="#" onclick="logout();"><center>Logout</center></a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
    <!-- /container -->
</div>
<form id="logoutForm" action="${pageContext.servletContext.contextPath}/logout.htm" method="post">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
<script>  
    function logout(){
        document.getElementById('logoutForm').submit();
    }
</script>