<%--
  Created by IntelliJ IDEA.
  User: Bert
  Date: 12/22/2016
  Time: 7:22 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="../postAuth/taglibs.jsp"%>
<div class="modal fade" id="no-reading-info-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" id="close-reading-form" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3 class="modal-title">Search Accounts To Be Read</h3>
            </div>
            <div class="modal-body">
                <form:form modelAttribute="addressForm" method="post" id="md-no-reading-info">
                    <form:hidden id="isBrgy-param" path="printBrgy" value="1"/>
                    <fieldset>
                        <div class="col-sm-5">
                            <h4 class="sub-header">Search Form</h4>
                            <div class="col-sm-12 form-group barangay">
                                <cws:select id="brgy-param" name="barangay" items2="${brgyOptions}" placeholder="Select brgy" label="Barangay" icon="home" required="true" size="12"/>
                            </div>
                            <div class="col-sm-12 form-group zone" style="display:none;">
                                <cws:select id="zone-param" name="zone" label="Zone" icon="home" required="true" placeholder="Select zone" items2="${zoneOptions}" size="12"/>
                            </div>
                            <div class="col-sm-12 form-group">
                                <div class="col-sm-4">
                                    <label class="control-label" style="display:block">Search By</label>
                                    <input id="isBrgy-toggle" type="checkbox" checked data-width="100px" data-toggle="toggle" data-on="Barangay" data-off="Zone" data-onstyle="warning" data-offstyle="info">
                                </div>
                            </div>
                            <div class="col-sm-12 form-group">
                                <div class="form-group">
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <div class="pull-right">
                                            <button id="submit-search" class="btn btn-primary">Submit</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-7">
                            <a id="filter-nr"></a>
                            <div id="brgy-prox" class="hidden"></div>
                            <div id="zone-prox" class="hidden"></div>
                            <div id="isBrgy-prox" class="hidden"></div>
                            <datatables:table deferLoading="0" deferRender="true" cssClass="table table-striped" id="noReading" filterPlaceholder="none" filterSelector="#filter-nr" url="${spring:mvcUrl('datatables-api#accounts-tbr').build()}" serverSide="true" displayLength="5" dom="tpr">
                                <datatables:column title="Acct No." name="number" property="number" sortable="false" selector="brgy-prox" filterable="true"/>
                                <datatables:column name="lastname" title="Last Name" property="customer.lastname" sortInitOrder="0" sortInitDirection="asc" selector="zone-prox" filterable="true"/>
                                <datatables:column name="firstName" title="First Name" property="customer.firstName" sortInitOrder="1" sortInitDirection="asc" selector="isBrgy-prox" filterable="true"/>
                                <dandelion:bundle excludes="jquery"/>
                            </datatables:table>
                        </div>
                    </fieldset>
                </form:form>
            </div>
        </div>
    </div>
</div>