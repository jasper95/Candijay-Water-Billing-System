<%@ include file="../postAuth/taglibs.jsp"%>
<div class="modal fade" id="expense-form-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3 class="modal-title"><span id="expense-action"></span> Expense</h3>
            </div>
            <div class="modal-body">
                <form:form modelAttribute="expenseForm" action="${pageContext.servletContext.contextPath}/admin/expenses/save" method="post" id="expense-form">
                    <fieldset>
                        <jsp:include page="../postAuth/form-alerts.jsp"/>
                        <div class="col-sm-6 form-group">
                            <cws:select id="exp-month" name="schedule.month" items="${monthOptions}" label="Month" icon="calendar" required="yrue" placeholder="Select month" size="12"/>
                        </div>
                        <div class="col-sm-6 form-group">
                            <cws:select id="exp-year" name="schedule.year" items="${yearOptions}" label="Year" icon="calendar" required="true" placeholder="Select year" size="12"/>
                        </div>
                        <div class="col-sm-6 form-group">
                            <cws:select id="exp-type" name="type" items="${typeOptions}" label="Type" icon="credit-card" required="true" placeholder="Select type" size="12"/>
                        </div>
                        <div class="col-sm-6 form-group">
                            <cws:input id="exp-amount" name="amount" label="Amount" icon="money" placeholder="Enter amount" required="true" size="12"/>
                        </div>
                        <input type="hidden" name="update" id="expId"/>
                        <form:hidden path="version" id="exp-version"/>
                        <p class="pm-audit audit-info">Created on <span id="cr-time-audit-pm"></span> by <span id="cr-user-audit-pm"></span></p>
                        <p class="pm-audit audit-info">Updated on <span id="up-time-audit-pm"></span> by <span id="up-user-audit-pm"></span></p>
                        <div class="col-sm-12 form-group">
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-sm-10">
                                    <div class="pull-right">
                                        <button type="submit" class="btn btn-primary">Submit</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>
                </form:form>
                <input id="expense-uri" type="hidden" value="${pageContext.servletContext.contextPath}/admin/expenses/"/>
            </div>
        </div>
    </div>
</div>
