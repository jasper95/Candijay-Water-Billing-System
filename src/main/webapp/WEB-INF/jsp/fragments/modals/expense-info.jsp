<%@ include file="../postAuth/taglibs.jsp"%>
<div class="modal fade" id="expense-info-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" id="close-reading-form" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3 class="modal-title">Expense Changes</h3>
            </div>
            <div class="modal-body">
                <fieldset>
                    <div class="col-md-12">
                        <div class="table-responsive">
                            <div id="auditExpense-id" style="display: none;"></div>
                            <a id="filterButtonAuditTable"></a>
                            <datatables:table cssClass="table table-striped" id="auditExpense" filterPlaceholder="none" filterSelector="#filterButtonAuditTable" serverSide="true" url="${pageContext.servletContext.contextPath}/admin/expenses/modified/datatable-search" displayLength="3" dom="tp" >
                                <datatables:column property="expense.id" filterable="true" visible="false" selector="auditExpense-id"/>
                                <datatables:column property="id" sortInitOrder="0" sortInitDirection="desc" visible="false"/>
                                <datatables:column title="Month" name="month" property="schedule.month" cssCellClass="month" renderFunction="custom-rendering#month" sortable="false"/>
                                <datatables:column title="Year" name ="year" property="schedule.year" cssCellClass="year" sortable="false"/>
                                <datatables:column title="Amount" name="amount" property="amount" sortable="false" cssCellClass="amount" renderFunction="custom-rendering#toPeso"/>
                                <datatables:column title="Type" name="type" property="type" sortable="false" cssCellClass="type" renderFunction="custom-rendering#expenseType"/>
                                <datatables:column title="Modified on" name="creationTime" property="creationTime"/>
                                <datatables:column title="Modified by" name="createdByUser" property="createdByUser" sortable="false"/>
                                <dandelion:bundle excludes="jquery"/>
                            </datatables:table>
                        </div>
                    </div>
                </fieldset>
            </div>
        </div>
    </div>
</div>