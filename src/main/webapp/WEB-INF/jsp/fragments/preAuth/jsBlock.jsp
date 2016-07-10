<%-- 
    Document   : preAuthJSBlock
    Created on : Apr 30, 2015, 7:44:17 AM
    Author     : Bert
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url value="/webjars/jquery/2.1.4/jquery.min.js" var="jquery"/>
<script src="${jquery}"></script>
<spring:url value="/resources/js/bootstrap.min.js" var="bootstrapJs"/>
<script src="${bootstrapJs}"></script>