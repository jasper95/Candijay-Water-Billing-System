<%@ include file="../postAuth/taglibs.jsp"%>
<div class="modal fade" id="recent-payments-info-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" id="close-reading-form" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3 class="modal-title">Payments History</h3>
            </div>
            <div class="modal-body">
                <fieldset>
                    <div class="col-md-12">
                        <div class="table-responsive">
                            <div id="recent-payments-account-id" style="display: none;"></div>
                            <a id="filterRecentPayments"></a>
                            <datatables:table deferLoading="0" deferRender="true" cssClass="table table-striped" id="recentPaymentsTable" filterPlaceholder="none" filterSelector="#filterRecentPayments" serverSide="true" url="${spring:mvcUrl('datatables-api#payments').build()}" displayLength="10" dom="tpr" >
                                <datatables:column property="account.id" filterable="true" visible="false" selector="recent-payments-account-id"/>
                                <datatables:column property="id" sortInitOrder="0" sortInitDirection="desc" visible="false"/>
                                <datatables:column title="Date" property="date" sortable="false"/>
                                <datatables:column title="OR #" property="receiptNumber" sortable="false"/>
                                <datatables:column title="Paid" property="amountPaid" sortable="false" renderFunction="custom-rendering#toPeso"/>
                                <datatables:column title="Type" property="type" sortable="false"/>
                                <datatables:column title="Cu.M" property="invoice.reading.consumption" sortable="false" default="---"/>
                                <datatables:column title="Penalty" property="invoice.penalty" sortable="false" renderFunction="custom-rendering#toPeso"/>
                                <datatables:column title="Discount" property="invoice.discount" sortable="false" renderFunction="custom-rendering#toPeso"/>
                                <datatables:column title="Due" property="invoiceTotal" sortable="false" renderFunction="custom-rendering#toPeso"/>
                                <datatables:column title="Balance" property="balance" sortable="false" renderFunction="custom-rendering#toPeso"/>
                                <dandelion:bundle excludes="jquery"/>
                            </datatables:table>
                            <button type="button" id="payment-print-preview-btn" class="btn btn-default">Print</button>
                        </div>
                    </div>
                </fieldset>
            </div>
        </div>
    </div>
</div>
