<%@ include file="../postAuth/taglibs.jsp"%>
<div class="modal fade" id="payment-form-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" id="close-reading-form" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3 class="modal-title">Update Payment</h3>
            </div>
            <div class="modal-body">
                <form:form modelAttribute="paymentForm" method="post" id="md-update-form">
                    <form:hidden path="payment.version" id="pm-version"/>
                    <form:hidden id="ac-id" path="accountId"/>
                    <fieldset>
                        <div class="col-sm-6">
                            <h4 class="sub-header">Account Details</h4>
                            <div class="info-wrapper">
                                <img class="info-img" src="${STATIC_URL}img/un.png">
                                <div class="info-text">
                                    <span id="md-full-name"></span>
                                </div>
                            </div>
                            <div class="info-wrapper">
                                <img class="info-img" src="${STATIC_URL}img/address.PNG">
                                <div class="info-text">
                                    <span id="md-address"></span>
                                </div>
                            </div>
                            <div class="info-wrapper">
                                <img class="info-img" src="${STATIC_URL}img/standing_balance.png">
                                <div class="info-text">
                                    <span id="md-last-reading"></span>
                                </div>
                            </div>
                            <div class="info-wrapper">
                                <img class="info-img" src="${STATIC_URL}img/status.png">
                                <div class="info-text">
                                    <span id="md-status"></span>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <jsp:include page="../postAuth/form-alerts.jsp"/>
                            <h4 class="sub-header">Payment Form</h4>
                            <div class="col-sm-12 form-group">
                                <cws:input id="pm-or" name="payment.receiptNumber" label="OR Number" icon="money" placeholder="Enter or no." required="true" size="12"/>
                            </div>
                            <div class="col-sm-12 form-group">
                                <cws:input id="pm-paid" name="payment.amountPaid" label="Amount" icon="money" placeholder="Enter amount" required="true" size="12"/>
                            </div>
                            <div class="col-sm-12 form-group">
                                <cws:input id="pm-discount" name="payment.discount" label="Discount" icon="money" placeholder="Enter discount" required="true" size="12"/>
                            </div>
                            <div class="col-sm-12 form-group">
                                <cws:input id="pm-date" name="payment.date" label="Date" icon="calendar" placeholder="Choose date" required="true" readOnly="true" size="12"/>
                            </div>
                            <input type="hidden" name="update" id="pid"/>
                            <p class="audit-info">Created on <span id="cr-time-audit"></span> by <span id="cr-user-audit"></span></p>
                            <p class="audit-info">Updated on <span id="up-time-audit"></span> by <span id="up-user-audit"></span></p>
                            <div class="col-sm-12 form-group">
                                <div class="form-group">
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <div class="pull-right">
                                            <button type="submit" class="btn btn-primary">Submit</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>
                </form:form>
                <input id="payments-uri" type="hidden" value="${pageContext.servletContext.contextPath}/admin/payments/"/>
            </div>
        </div>
    </div>
</div>