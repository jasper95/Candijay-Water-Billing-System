<%-- 
    Document   : header
    Created on : Apr 30, 2015, 12:50:19 AM
    Author     : Bert
--%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!-- Fixed navbar -->
<nav class="navbar navbar-default navbar-fixed-top" >
  <div class="container">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#" style="color: #fff;">CANDIJAY WATER BILLING SYSTEM</a>
    </div>
    <div class="collapse-nav navbar-collapse">
        <ul id="" class="nav navbar-nav">
            <li class="menu${pageContext.request.requestURI eq '/CWS/WEB-INF/jsp/index.jsp' ? ' active' : ''}"><a href="index.htm" style="color:#fff;">HOME</a></li>
            <li class="menu${pageContext.request.requestURI eq '/CWS/WEB-INF/jsp/about.jsp' ? ' active' : ''}"><a href="about.htm" style="color:#fff;">ABOUT</a></li>
            <li class="menu${pageContext.request.requestURI eq '/CWS/WEB-INF/jsp/staff.jsp' ? ' active' : ''}"><a href="staff.htm" style="color:#fff;">STAFF</a></li>
            <li class="menu${pageContext.request.requestURI eq '/CWS/WEB-INF/jsp/contact.jsp' ? ' active' : ''}"><a href="contact.htm" style="color:#fff;">CONTACT</a></li>
            <li class="menu${pageContext.request.requestURI eq '/CWS/WEB-INF/jsp/faq.jsp' ? ' active' : ''}"><a href="faq.htm" style="color:#fff;">FAQ</a>
            <li class="menu ${pageContext.request.requestURI eq '/CWS/WEB-INF/jsp/login.jsp' ? ' active' : ''}"> 
                <sec:authorize access="isAnonymous()">
                    <a href="login.htm" style="color:#fff;">LOGIN</a>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">
                    <a href="admin.htm" style="color:#fff;">ADMIN</a>
                </sec:authorize>
            </li> 
        </ul>
      </div><!--/.nav-collapse -->
  </div>
</nav>