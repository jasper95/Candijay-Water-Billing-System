<%@ include file="fragments/postAuth/taglibs.jsp"%>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="icon" href="${STATIC_URL}img/cws.ico">
        <title>System Error</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
        <sec:csrfMetaTags/>
    </head>
    <body>
    <%--<div class="container">
        <h1><strong  style="color:#fff; font-size:100px;">ERROR 404</strong><br><small>The page your are trying to access doesn't exist.</small></h1>
        <sec:authorize access="isAuthenticated()">
            <a href="${pageContext.servletContext.contextPath}/admin" type="button" class="btn btn-default"> Return to Home Page</a>
        </sec:authorize>
        <sec:authorize access="isAnonymous()">
            <a href="${pageContext.servletContext.contextPath}/" type="button" class="btn btn-default"> Return to Home Page</a>
        </sec:authorize>
    </div>--%>
    </body>
</html>
