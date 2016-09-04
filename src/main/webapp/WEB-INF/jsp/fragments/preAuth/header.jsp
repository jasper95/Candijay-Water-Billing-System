<%-- 
    Document   : header
    Created on : Apr 30, 2015, 12:50:19 AM
    Author     : Bert
--%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!-- Fixed navbar -->
<nav id="header1" class="navbar navbar-default navbar-fixed-top" >
  <div class="container">
    <div class="navbar-header">
        <a class="navbar-brand" href="${pageContext.servletContext.contextPath}/login" style="color: #fff;">CANDIJAY WATER BILLING SYSTEM</a>
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
    </div>
<%--    <div id="navbar" class="collapse collapse-nav navbar-collapse">
        <ul class="nav navbar-nav navbar-right">
            <li class="menu ${pageContext.request.requestURI eq '/WEB-INF/jsp/login.jsp' ? ' active' : ''}">
                <sec:authorize access="isAnonymous()">
                    <a href="${pageContext.servletContext.contextPath}/login" style="color:#fff;"><span style="color:white">LOGIN</span></a>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">
                    <a href="${pageContext.servletContext.contextPath}/admin" style="color:#fff;"><span style="color:white">ADMIN</span></a>
                </sec:authorize>
            </li> 
        </ul>
      </div>--%>
  </div>
</nav>