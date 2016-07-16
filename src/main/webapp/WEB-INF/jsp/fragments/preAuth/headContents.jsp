<%-- 
    Document   : preAuthStaticFiles
    Created on : Apr 30, 2015, 7:43:12 AM
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
    <spring:url value="/resources/img/cws.ico" var="cwsIcon"/>
    <link rel="icon" href="${cwsIcon}">

    <title>Candijay Water Billing System 2.0</title>

    <!-- Bootstrap core CSS -->
    <spring:url value="/resources/css/bootstrap.min.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet">

    <!-- Custom styles for this template -->
    <spring:url value="/resources/css/stylesheet_gen.css" var="stylesheetCss"/>
    <link href="${stylesheetCss}" rel="stylesheet">
    <spring:url value="/resources/css/stylesheet_sticky-footer-navbar.css" var="stickyFooterNavbarCss"/>
    <link href="${stickyFooterNavbarCss}" rel="stylesheet">
    <spring:url value="/resources/" var="STATIC_URL"/>
    <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
    <sec:csrfMetaTags/>
</head>