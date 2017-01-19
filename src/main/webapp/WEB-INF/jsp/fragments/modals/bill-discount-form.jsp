<%@ include file="../postAuth/taglibs.jsp"%>
<div class="modal fade" id="bill-discount-form-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Update Discount</h4>
            </div>
            <div class="modal-body">
                <fieldset>
                    <div class="col-sm-6">
                        <h4 class="sub-header">Account Details</h4>
                        <div class="info-wrapper">
                            <img class="info-img" src="${STATIC_URL}img/un.png">
                            <div class="info-text">
                                <span id="bill-dc-full-name"></span>
                            </div>
                        </div>
                        <div class="info-wrapper">
                            <img class="info-img" src="${STATIC_URL}img/address.PNG">
                            <div class="info-text">
                                <span id="bill-dc-address"></span>
                            </div>
                        </div>
                        <div class="info-wrapper">
                            <img class="info-img" src="${STATIC_URL}img/standing_balance.png">
                            <div class="info-text">
                                <span id="bill-dc-due"></span>
                            </div>
                        </div>
                        <div class="info-wrapper">
                            <img class="info-img" src="${STATIC_URL}img/date.png">
                            <div class="info-text">
                                <span id="bill-dc-date"></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <h4 class="sub-header">Discount Form</h4>
                        <form:form id="discount-form" action="#" modelAttribute="billDiscountForm" role="form">
                            <jsp:include page="../postAuth/form-alerts.jsp"/>
                            <form:hidden id="dc-id" path="id"/>
                            <div class="col-sm-12 form-group">
                                <cws:input id="dc-val" name="discount" label="Discount" icon="money" placeholder="Enter discount" required="true" size="12" moreClasses="is-number"/>
                            </div>
                            <div class="col-sm-12 form-group">
                                <div class="form-group">
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <div class="pull-right">
                                            <button type="submit" class="btn btn-primary">Submit</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form:form>
                        <input type="hidden" id="bill-due-reference"/>
                    </div>
                </fieldset>
            </div>
        </div>
    </div>
</div>
