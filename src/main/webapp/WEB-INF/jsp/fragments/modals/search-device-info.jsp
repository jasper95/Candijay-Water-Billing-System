<%--
  Created by IntelliJ IDEA.
  User: Bert
  Date: 2/22/2017
  Time: 8:12 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="../postAuth/taglibs.jsp"%>
<div class="modal fade" id="search-device-info-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" id="close-reading-form" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3 class="modal-title">Search Existing Meter</h3>
            </div>
            <div class="modal-body">
                <fieldset>
                    <div class="col-sm-4">
                        <h4 class="sub-header">Search Device Form</h4>
                        <div class="col-sm-12 form-group">
                            <div class="col-md-12 has-feedback ">
                                <label class="control-label" for="search-mc">Meter Code <span style="color:red">*</span></label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-tachometer fa-fw"></i></span>
                                    <input id="search-mc" placeholder="Meter code" class="form-control input-md " type="text" value="" autocomplete="off">
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-12 form-group">
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-sm-10">
                                    <div class="pull-right">
                                        <button id="submit-search-dv" class="btn btn-primary">Submit</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-8">
                        <a id="triggerSearch"></a>
                        <div id="dv-mc-filter" style="display: none"></div>
                        <datatables:table deferLoading="0" deferRender="true" cssClass="table table-striped" id="deviceTable" filterPlaceholder="none" filterSelector="#triggerSearch" serverSide="true" url="${spring:mvcUrl('datatables-api#devices').build()}" displayLength="3" dom="tpr" >
                            <datatables:column title="Meter Code" property="meterCode" filterable="true" selector="dv-mc-filter" sortable="false"/>
                            <datatables:column title="Last Reading" property="lastReading" sortable="false"/>
                            <datatables:column renderFunction="custom-rendering#accountUrl" title="Acct No." name="number" filterable="true" property="owner.number" sortable="false"/>
                            <datatables:column name="address" title="Barangay" property="owner.address.brgy" sortable="false"/>
                            <datatables:column name="balance" title="Balance" property="owner.accountStandingBalance" renderFunction="custom-rendering#toPeso" sortable="false"/>
                            <dandelion:bundle excludes="jquery"/>
                        </datatables:table>
                    </div>
                </fieldset>
            </div>
        </div>
    </div>
</div>