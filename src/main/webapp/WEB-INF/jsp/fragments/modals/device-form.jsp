<%@ include file="../postAuth/taglibs.jsp"%>
<div class="modal fade" id="device-form-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title"> <span id="device-action"></span> Device</h4>
            </div>
            <div class="modal-body">
                <form:form id="device-form" action="#" modelAttribute="deviceForm" role="form">
                    <fieldset>
                        <jsp:include page="../postAuth/form-alerts.jsp"/>
                        <div class="col-sm-12 form-group">
                            <cws:input id="acc-mb" name="brand" label="Meter Brand" icon="tachometer" placeholder="Enter meter brand" required="true" size="6"/>
                            <cws:input id="acc-lr" name="lastReading" label="Last Reading" icon="tachometer" placeholder="Enter last reading" required="true" size="6" moreClasses="is-number"/>
                        </div>
                        <div class="col-sm-12 form-group">
                            <cws:input id="acc-mc" name="meterCode" label="Meter Code" icon="tachometer" placeholder="Enter meter code" required="true" size="6"/>
                            <div id="dv-dup-toggle" class="col-sm-6">
                                <label class="control-label" style="display:block">Duplicate Meter Code:</label>
                                <input id="allow-dup-mc-toggle" data-width="120px" type="checkbox" data-toggle="toggle" data-on="<i class='fa fa-check'></i> Allowed" data-off="<i class='fa fa-remove'></i> Not Allowed">
                            </div>
                            <input type="hidden" name="duplicateMCToggle" id="allow-dup-mc" value="0">
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