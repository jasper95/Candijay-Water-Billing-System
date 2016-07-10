<%-- 
    Document   : navbar
    Created on : Apr 30, 2015, 8:21:05 AM
    Author     : Bert
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:url value="/resources/" var="STATIC_URL"/>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div class="col-sm-3 col-md-2 sidebar">
    <ul class="nav-cstm nav-sidebar">
        <li><a href="${pageContext.servletContext.contextPath}/admin" class="blue"><img src="${STATIC_URL}img/home.png" class="side-bar-ico"></a></li>
        <sec:authorize access="hasRole('CUSTOMER_ACCOUNTS')">
            <li><a href="${pageContext.servletContext.contextPath}/admin/customers"><img src="${STATIC_URL}img/customer_admin.png" class="side-bar-ico"></a></li>
            <li><a href="${pageContext.servletContext.contextPath}/admin/accounts"><img src="${STATIC_URL}img/accounts_admin.png" class="side-bar-ico"></a></li>
        </sec:authorize>
        <sec:authorize access="hasRole('METER_READING')">
            <li><a href="${pageContext.servletContext.contextPath}/admin/reading"><img src="${STATIC_URL}img/meter_reading_admin.png" class="side-bar-ico"></a></li>
        </sec:authorize>
        <sec:authorize access="hasRole('BILLS_REPORTS')">
            <li><a href="${pageContext.servletContext.contextPath}/admin/bills"><img src="${STATIC_URL}img/bills_admin.png" class="side-bar-ico"></a></li>
        </sec:authorize>
        <sec:authorize access="hasRole('PAYMENTS')">
            <li><a href="${pageContext.servletContext.contextPath}/admin/payments"><img src="${STATIC_URL}img/payment_admin.png" class="side-bar-ico"></a></li>
        </sec:authorize>
    </ul>
</div>
