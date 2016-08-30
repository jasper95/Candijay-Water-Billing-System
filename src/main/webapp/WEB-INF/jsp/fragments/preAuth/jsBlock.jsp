<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url value="/webjars/jquery/2.0.3/jquery.min.js" var="jquery"/>
<script src="${jquery}"></script>
<spring:url value="/resources/js/bootstrap.min.js" var="bootstrapJs"/>
<script src="${bootstrapJs}"></script>