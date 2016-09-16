<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="id" required="true" rtexprvalue="true" %>
<%@ attribute name="name" required="true" rtexprvalue="true"%>
<%@ attribute name="label" required="true" rtexprvalue="true" %>
<%@ attribute name="icon" required="true" rtexprvalue="true" %>
<%@ attribute name="placeholder" required="true" rtexprvalue="true" %>
<%@ attribute name="required" required="true" rtexprvalue="true" %>
<%@ attribute name="readOnly" required="false" rtexprvalue="true" %>
<%@ attribute name="size" required="false" rtexprvalue="true" %>

<spring:bind path="${name}">
    <div class="col-sm-${not empty size ? size : '4'} has-feedback ${status.error ? 'has-error' : '' }">
        <label class="control-label" for="${id}">${label}</label>
        <div class="input-group">
            <span class="input-group-addon"><i class="fa fa-${icon} fa-fw"></i></span>
            <input id="${id}" name="${name}" class="form-control input-sm" placeholder="${placeholder}" autocomplete="off" ${required eq 'true' ? 'required': ''} ${readOnly eq 'true' ? 'readOnly' : ''} />
            <c:if test="${status.error}">
                <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
            </c:if>
        </div>
        <c:if test="${status.error}">
            <c:set var="firstError" value="${status.errorMessages[0]}"/>
            <span class="field-error">${firstError}</span>
        </c:if>
    </div>
</spring:bind>