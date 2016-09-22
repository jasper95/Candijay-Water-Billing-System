<%@ include file="../postAuth/taglibs.jsp"%>
<div class="modal fade" id="finalize-payments-form-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Finalize Payments</h4>
            </div>
            <div class="modal-body">
                <form:form id="fn-pm-form" modelAttribute="addressForm" role="form">
                    <fieldset>
                        <jsp:include page="../postAuth/form-alerts.jsp"/>
                        <div class="col-sm-12 form-group">
                            <cws:select id="acc-bg" name="brgy" items2="${brgyOptions}" placeholder="Select brgy" label="Barangay" icon="home" required="true" size="12"/>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <div class="pull-right">
                                    <input type="submit" class="btn btn-primary" value="Submit">
                                </div>
                            </div>
                        </div>
                    </fieldset>
                </form:form>
            </div>
        </div>
    </div>
</div>