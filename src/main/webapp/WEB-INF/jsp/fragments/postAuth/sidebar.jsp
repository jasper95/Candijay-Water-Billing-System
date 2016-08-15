<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:url value="/resources/" var="STATIC_URL"/>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="nav-side-menu">
    <div class="brand">
        <span class="sidebar-brand">Candijay Water Billing System</span>
    </div>
    <i class="fa fa-bars fa-2x toggle-btn" data-toggle="collapse" data-target="#menu-content"></i>
    <div class="menu-list">
        <ul id="menu-content" class="menu-content collapse out">
            <li >
                <a href="${pageContext.servletContext.contextPath}/admin">
                    <i class="fa fa-home fa-lg"></i> Home
                </a>
            </li>
        <sec:authorize access="hasRole('CUSTOMER_ACCOUNTS')">
            <li data-toggle="collapse" data-target="#manage-cus-acct" class="collapsed">
                <a href="#">
                    <i class="fa fa-group fa-lg"></i> Accounts <span class="arrow"></span>
                </a>
            </li>
        </sec:authorize>
            <ul class="sub-menu collapse" id="manage-cus-acct">
                <li>
                    <a href="${pageContext.servletContext.contextPath}/admin/customers">
                        <i class="fa fa-user fa-lg"></i> Manage Customer
                    </a>
                </li>
                <li>
                    <a href="${pageContext.servletContext.contextPath}/admin/accounts">
                        <i class="fa fa-user fa-lg"></i> Manage Accounts
                    </a>
                </li>
            </ul>
        <sec:authorize access="hasRole('METER_READING')">
            <li>
                <a href="${pageContext.servletContext.contextPath}/admin/reading">
                    <div>
                        <i class="fa fa-tachometer fa-lg"></i> Meter Reading
                    </div>
                </a>
            </li>
        </sec:authorize>
        <sec:authorize access="hasRole('PAYMENTS')">
            <li data-toggle="collapse" data-target="#transactions"  class="collapsed">
                <a href="#">
                    <i class="fa fa-bank fa-lg"></i> Transactions <span class="arrow"></span>
                </a>
            </li>
        </sec:authorize>
            <ul class="sub-menu collapse" id="transactions">
                <li>
                    <a style="display: block" href="${pageContext.servletContext.contextPath}/admin/payments">
                        <i class="fa fa-money fa-lg"></i> Payments
                    </a>
                </li>
                <li>
                    <a style="display: block" href="${pageContext.servletContext.contextPath}/admin/expenses">
                        <i class="fa fa-credit-card fa-lg"></i> Expenses
                    </a>
                </li>
            </ul>
        <sec:authorize access="hasRole('BILLS_REPORTS')">
            <li data-toggle="collapse" data-target="#manage-rpt"  class="collapsed">
                <a href="#">
                    <i class="fa fa-folder-open-o fa-lg"></i> Reports <span class="arrow"></span>
                </a>
            </li>
        </sec:authorize>
            <ul class="sub-menu collapse" id="manage-rpt">
                <li>
                    <a href="${pageContext.servletContext.contextPath}/admin/bills">
                        <i class="fa fa-file-text-o fa-lg"></i> Bills
                    </a>
                </li>
                <li>
                    <a href="${pageContext.servletContext.contextPath}/admin/reports">
                        <i class="fa fa-bar-chart-o fa-lg"></i> Generate Reports
                    </a>
                </li>
            </ul>
        <sec:authorize access="hasRole('SYSTEM_USERS')">
            <li data-toggle="collapse" class="collapsed" data-target="#system-action">
                <a href="#">
                    <i class="fa fa-television fa-lg"></i> System <span class="arrow"></span>
                </a>
            </li>
        </sec:authorize>
            <ul class="sub-menu collapse" id="system-action">
                <li>
                    <a href="${pageContext.servletContext.contextPath}/admin/settings">
                        <i class="fa fa-gear fa-lg"></i> Settings
                    </a>
                </li>
                <li>
                    <a href="${pageContext.servletContext.contextPath}/admin/system-users">
                        <i class="fa fa-users fa-lg"></i> Users
                    </a>
                </li>
            </ul>
            <li class="prof-action-menu" data-target="#user-prof-action" data-toggle="collapse" class="collapsed">
                <a href="#">
                    <i class="fa fa-user fa-lg"></i> Profile <span class="arrow"></span>
                </a>
            </li>
            <ul id="user-prof-action" class="sub-menu collapse">
                <li class="prof-action-menu">
                    <a href="#" id="update-profile-link2">
                        <i class="fa fa-edit fa-lg"></i> Update Profile
                    </a>
                </li>
                <li class="prof-action-menu">
                    <a href="#" onclick="logout();">
                        <i class="fa fa-sign-out fa-lg"></i> Logout
                    </a>
                </li>
            </ul>
        </ul>
    </div>
</div>