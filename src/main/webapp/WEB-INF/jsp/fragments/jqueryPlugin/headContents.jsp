<%-- 
    Document   : headContents
    Created on : May 9, 2015, 11:24:24 AM
    Author     : Bert
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- jquery-ui.js file is really big so we only load what we need instead of loading everything -->
<spring:url value="/webjars/jquery-ui/1.10.3/ui/jquery.ui.core.js" var="jQueryUiCore"/>
<script src="${jQueryUiCore}"></script>

<spring:url value="/webjars/jquery-ui/1.10.3/ui/jquery.ui.datepicker.js" var="jQueryUiDatePicker"/>
<script src="${jQueryUiDatePicker}"></script>

<!-- jquery-ui.css file is not that big so we can afford to load it -->
<spring:url value="/webjars/jquery-ui/1.10.3/themes/base/jquery-ui.css" var="jQueryUiCss"/>
<link href="${jQueryUiCss}" rel="stylesheet"/>
