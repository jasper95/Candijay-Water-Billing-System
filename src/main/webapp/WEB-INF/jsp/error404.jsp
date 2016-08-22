<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!DOCTYPE html>
<html lang="en">
  <jsp:include page="fragments/postAuth/headContents.jsp"/>
  <body class="error_404">
    <!-- Page Content -->
    <div class="container">
        <h1><strong  style="color:#fff; font-size:100px;">ERROR 404</strong><br><small>The page your are trying to access doesn't exist.</small></h1>
        <sec:authorize access="isAuthenticated()"> 
            <a href="${pageContext.servletContext.contextPath}/admin" type="button" class="btn btn-default"> Return to Home Page</a>
        </sec:authorize>
        <sec:authorize access="isAnonymous()"> 
            <a href="${pageContext.servletContext.contextPath}/" type="button" class="btn btn-default"> Return to Home Page</a>
        </sec:authorize>
    </div>  
    <jsp:include page="fragments/footer.jsp"/>
  </body>
</html>
