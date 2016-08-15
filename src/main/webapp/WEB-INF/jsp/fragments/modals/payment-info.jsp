<%@ include file="../postAuth/taglibs.jsp"%>
<div class="modal fade" id="payment-info-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" id="close-reading-form" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3 class="modal-title">Payment Changes</h3>
            </div>
            <div class="modal-body">
                <fieldset>
                    <div class="col-md-12">
                        <div class="table-responsive">
                            <div id="auditPayment-id" style="display: none;"></div>
                            <a id="filterButtonAuditTable"></a>
                            <datatables:table cssClass="table table-striped" id="auditPayment" filterPlaceholder="none" filterSelector="#filterButtonAuditTable" serverSide="true" url="${pageContext.servletContext.contextPath}/admin/payments/modified/datatable-search" displayLength="3" dom="tp" >
                                <datatables:column property="payment.id" filterable="true" visible="false" selector="auditPayment-id"/>
                                <datatables:column property="id" sortInitOrder="0" sortInitDirection="desc" visible="false"/>
                                <datatables:column title="OR number" property="receiptNumber" sortable="false" cssCellClass="or-number"/>
                                <datatables:column title="Discount" name="discount" property="discount" sortable="false" renderFunction="custom-rendering#toPeso" cssCellClass="payment-discount"/>
                                <datatables:column title="Amount Paid" name="amount-paid" property="amountPaid" sortable="false" renderFunction="custom-rendering#toPeso" cssCellClass="payment-amount"/>
                                <datatables:column title="Payment Date" name="date" property="date" sortable="false" renderFunction="custom-rendering#paidDate" cssCellClass="payment-date"/>
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