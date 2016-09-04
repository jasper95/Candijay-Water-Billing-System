<%@ include file="../postAuth/taglibs.jsp"%>
<div class="modal fade" id="acct-form-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">${accountForm.account == null ? 'Add' : 'Update'} Account</h4>
            </div>
            <div class="modal-body">
                <form:form id="acct-form" action="#" modelAttribute="accountForm" role="form">
                    <fieldset>
                        <jsp:include page="../postAuth/form-alerts.jsp"/>
                    <c:if test="${accountForm.account.id == null}">
                        <div class="col-sm-12 form-group">
                            <cws:input id="acc-mc" name="device.meterCode" label="Meter Code" icon="tachometer" placeholder="Enter meter code" required="true" size="6"/>
                            <cws:input id="acc-mb" name="device.brand" label="Meter Brand" icon="tachometer" placeholder="Enter meter brand" required="true" size="6"/>
                        </div>
                    </c:if>
                        <div class="col-sm-12 form-group">
                            <cws:select id="acc-bg" name="address.brgy" items2="${brgyOptions}" placeholder="Select brgy" label="Barangay" icon="home" required="true" size="6"/>
                            <cws:select id="ac-lc" name="address.locationCode" items2="${zoneOptions}" placeholder="Select Zone" label="Zone" icon="home" required="true" size="6"/>
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