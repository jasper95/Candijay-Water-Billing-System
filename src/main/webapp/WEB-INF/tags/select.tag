<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="id" required="true" rtexprvalue="true" %>
<%@ attribute name="name" required="true" rtexprvalue="true"%>
<%@ attribute name="label" required="true" rtexprvalue="true" %>
<%@ attribute name="icon" required="true" rtexprvalue="true" %>
<%@ attribute name="required" required="true" rtexprvalue="true" %>
<%@ attribute name="placeholder" required="true" rtexprvalue="true" %>
<%@ attribute name="items" required="false" rtexprvalue="true" type="java.util.HashMap"%>
<%@ attribute name="items2" required="false" rtexprvalue="true" type="java.util.Set"%>
<%@ attribute name="disabled" required="false" rtexprvalue="true" %>
<%@ attribute name="size" required="false" rtexprvalue="true" %>

<spring:bind path="${name}">
    <div class="col-md-${not empty size ? size : '4'} has-feedback ${status.error ? 'has-error' : '' }">
        <label class="control-label" for="${id}">${label} <span style="color:red">${required eq 'true' ? '&#42;' :''}</span></label>
        <div class="input-group">
            <span class="input-group-addon"><i class="fa fa-${icon} fa-fw"></i></span>
            <form:select id="${id}" path="${name}" class="form-control input-md"  autocomplete="off" disabled="${disabled}">
                <form:option value="" label="--${placeholder}--"/>
                <c:choose>
                    <c:when test="${not empty items}">
                        <form:options items="${items}"/>
                    </c:when>
                    <c:otherwise>
                        <form:options items="${items2}"/>
                    </c:otherwise>
                </c:choose>
            </form:select>
            <span class="glyphicon ${status.error ? 'glyphicon-remove' : '' } form-control-feedback" aria-hidden="true"></span>
        </div>
        <c:if test="${status.error}">
            <c:set var="firstError" value="${status.errorMessages[0]}"/>
            <span class="field-error">${firstError}</span>
        </c:if>
    </div>
</spring:bind>