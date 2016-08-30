<%@ include file="../postAuth/taglibs.jsp"%>
<div class="modal fade" id="reading-info-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
                            <div id="auditReading-id" style="display: none;"></div>
                            <a id="filterButtonAuditTable"></a>
                            <datatables:table deferLoading="0" deferRender="true" cssClass="table table-striped" id="auditReading" filterPlaceholder="none" filterSelector="#filterButtonAuditTable" serverSide="true" url="${pageContext.servletContext.contextPath}/admin/reading/modified/datatable-search" displayLength="3" dom="tp" >
                                <datatables:column property="reading.id" filterable="true" visible="false" selector="auditReading-id"/>
                                <datatables:column property="id" sortInitOrder="0" sortInitDirection="desc" visible="false"/>
                                <datatables:column title="Month" name="month" property="schedule.month" renderFunction="custom-rendering#month" sortable="true" cssCellClass="month"/>
                                <datatables:column title="Year" name="year" property="schedule.year" sortable="true" cssCellClass="year"/>
                                <datatables:column title="Consumption" name="consume" property="consumption" cssCellClass="consumption" sortable="false"/>
                                <datatables:column title="Reading" name="reading" property="readingValue" cssCellClass="reading" sortable="false"/>
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
