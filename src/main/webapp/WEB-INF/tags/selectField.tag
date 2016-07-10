<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="name" required="true" rtexprvalue="true"
              description="Name of corresponding property in bean object" %>
<%@ attribute name="id" required="false" rtexprvalue="true"
              description="Custom id"%>
<%@ attribute name="names" required="true" rtexprvalue="true" type="java.util.List"
              description="Names in the list" %>
<%@ attribute name="cssStyle" required="false" rtexprvalue="true"
              description="Style of Select" %>
<%@ attribute name="blankLabel" required="true" rtexprvalue="true"
              description="Style of Select" %>
<spring:bind path="${name}">
    <c:set var="cssGroup" value="form-control frm-ent-accnt-nmb log-un ${status.error ? 'error-form' : '' }"/>
    <form:select cssStyle="${cssStyle}" class="${cssGroup}" path="${name}" id="${id}">
        <form:option value="" label="${blankLabel}"/>
        <c:forEach var="item" items="${names}">
            <form:option value="${item.id}" label="${item.toString()}"/>
        </c:forEach> 
    </form:select>
</spring:bind>