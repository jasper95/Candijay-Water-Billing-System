<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="id" required="false" rtexprvalue="true"
              description="Custom id"%>
<%@ attribute name="name" required="true" rtexprvalue="true"
              description="Name of corresponding property in bean object" %>
<%@ attribute name="label" required="true" rtexprvalue="true"
              description="Label appears in red color if input is considered as invalid after submission" %>
<%@ attribute name="cssStyle" required="false" rtexprvalue="true"
              description="" %>

<spring:bind path="${name}">
    <c:set var="cssGroup" value="form-control frm-ent-accnt-nmb log-un ${status.error ? 'error-form' : '' }"/>
    <form:input cssStyle="${cssStyle}" class="${cssGroup}" path="${name}" id="${id}" placeholder="${label}" required="true" autofocus="true"/>
</spring:bind>