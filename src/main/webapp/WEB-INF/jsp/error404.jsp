<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:url value="/resources/" var="STATIC_URL"/>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="${STATIC_URL}img/cws.ico">
        <title>System Error</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_gen.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="fragments/preAuth/header.jsp"/>
        <div style="margin-top: 100px" class="container">
            <img class="img-responsive center-block" src="${STATIC_URL}/img/sad2.png">
            <div style="text-align: center; color: #FFF; font-weight: 500; margin-top: 5px">
                <p style="font-size: 20px">The page does not exists or might have been deleted</p>
            </div>
        </div>
        <jsp:include page="fragments/footer.jsp"/>
    </body>
</html>
