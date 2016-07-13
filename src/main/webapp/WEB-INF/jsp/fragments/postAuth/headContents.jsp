<%-- 
    Document   : headContents
    Created on : Apr 30, 2015, 8:18:29 AM
    Author     : Bert
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <spring:url value="/resources/" var="STATIC_URL"/>
    <link rel="icon" href="${STATIC_URL}img/cws.ico">

    <title>Candijay Water Billing System 2.0</title>

    <!-- Bootstrap core CSS -->
    <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <!--link href="${STATIC_URL}css/stylesheet_gen.css" rel="stylesheet">
    <link href="${STATIC_URL}css/stylesheet_admin.css" rel="stylesheet">
    <link href="${STATIC_URL}css/stylesheet_dashboard.css" rel="stylesheet"-->
    <link href="${STATIC_URL}css/stylesheet_sticky-footer-navbar.css" rel="stylesheet">
    <spring:url value="/webjars/jquery/2.0.3/jquery.js" var="jQuery"/>
    <script src="${jQuery}"></script>
    <script src="${STATIC_URL}js/bootstrap.min.js"></script>
    <sec:csrfMetaTags/>
</head>