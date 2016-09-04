<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!-- Fixed navbar -->
<nav class="navbar navbar-cws navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${pageContext.servletContext.contextPath}/admin/">Candijay Water Billing System</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <sec:authorize access="hasAuthority('ACCOUNTS')">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle animate" data-toggle="dropdown">
                            <i class="fa fa-group fa-lg" ></i> Accounts <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu" role="menu">
                            <li>
                                <a href="${pageContext.servletContext.contextPath}/admin/customers/">
                                    <i class="fa fa-user fa-lg"></i> Manage Customer
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.servletContext.contextPath}/admin/accounts/">
                                    <i class="fa fa-user fa-lg"></i> Manage Accounts
                                </a>
                            </li>
                        </ul>
                    </li>
                </sec:authorize>
                <sec:authorize access="hasAuthority('READINGS')">
                    <li>
                        <a href="${pageContext.servletContext.contextPath}/admin/reading/">
                            <i class="fa fa-tachometer fa-lg"></i> Meter Reading
                        </a>

                    </li>
                </sec:authorize>
                <sec:authorize access="hasAuthority('TRANSACTIONS')">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle animate" data-toggle="dropdown">
                            <i class="fa fa-bank fa-lg"></i> Transactions <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <a href="${pageContext.servletContext.contextPath}/admin/payments/">
                                    <i class="fa fa-money fa-lg"></i> Payments
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.servletContext.contextPath}/admin/expenses/">
                                    <i class="fa fa-credit-card fa-lg"></i> Expenses
                                </a>
                            </li>
                        </ul>
                    </li>
                </sec:authorize>
                <sec:authorize access="hasAuthority('REPORTS')">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle animate" data-toggle="dropdown">
                            <i class="fa fa-folder-open-o fa-lg"></i> Reports <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <a href="${pageContext.servletContext.contextPath}/admin/bills/">
                                    <i class="fa fa-file-text-o fa-lg"></i> Bills
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.servletContext.contextPath}/admin/reports/">
                                    <i class="fa fa-bar-chart-o fa-lg"></i> Generate Reports
                                </a>
                            </li>
                        </ul>
                    </li>
                </sec:authorize>
                <sec:authorize access="hasAuthority('SYSTEM')">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle animate" data-toggle="dropdown">
                            <i class="fa fa-television fa-lg"></i> System <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <a href="${pageContext.servletContext.contextPath}/admin/settings/">
                                    <i class="fa fa-gear fa-lg"></i> Settings
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.servletContext.contextPath}/admin/system-users/">
                                    <i class="fa fa-users fa-lg"></i> Users
                                </a>
                            </li>
                        </ul>
                    </li>
                </sec:authorize>
                <li class="dropdown dropdown-right" class="dropdown-toggle animate" data-toggle="dropdown">
                    <a href="#" class="dropdown-toggle animate" data-toggle="dropdown">
                        <i class="fa fa-user fa-lg"></i>Profile<span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                            <a href="#" id="update-profile-link">
                                <i class="fa fa-edit fa-lg"></i> Update Profile
                            </a>
                        </li>
                        <li>
                            <a href="#" onclick="logout();">
                                <i class="fa fa-sign-out fa-lg"></i> Logout
                            </a>
                        </li>

                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>
<jsp:include page="../modals/update-profile-form.jsp"/>
<form id="logoutForm" action="${pageContext.servletContext.contextPath}/logout" method="post">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
<script>  
    function logout(){
        document.getElementById('logoutForm').submit();
    }
</script>