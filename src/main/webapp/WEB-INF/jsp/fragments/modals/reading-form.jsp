<%--
  Created by IntelliJ IDEA.
  User: Bert
  Date: 7/19/2016
  Time: 7:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="../postAuth/taglibs.jsp"%>
<div class="modal fade" id="reading-form-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" id="close-reading-form" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3 class="modal-title">Update Reading</h3>
            </div>
            <div class="modal-body">
                <form:form modelAttribute="meterReadingForm" method="post" id="md-update-form">
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
                                <img class="info-img" src="${STATIC_URL}img/current_reading.png">
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
                            <h4 class="sub-header">Reading Form</h4>
                            <div class="col-sm-12 form-group">
                                <cws:select id="rd-mn" name="meterReading.schedule.month" label="Reading Month" icon="calendar" required="true" placeholder="Select month" items="${monthOptions}" size="12"/>
                            </div>
                            <div class="col-sm-12 form-group">
                                <cws:select id="rd-yr" items="${yearOptions}" name="meterReading.schedule.year" label="Reading Year" icon="calendar" required="true" placeholder="Select year" size="12"/>
                            </div>
                            <div class="col-sm-12 form-group">
                                <cws:input id="reading-val" name="meterReading.readingValue" label="Reading value" icon="tachometer" placeholder="Enter reading" required="true" size="12" moreClasses="is-number"/>
                            </div>
                            <form:hidden path="meterReading.version" id="rd-vs"/>
                            <input type="hidden" name="readingId" id="rd-id"/>
                            <p class="audit-info">Created on <span id="cr-time-audit-mr"></span> by <span id="cr-user-audit-mr"></span></p>
                            <p class="audit-info">Updated on <span id="up-time-audit-mr"></span> by <span id="up-user-audit-mr"></span></p>
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
                <input id="reading-uri" type="hidden" value="${pageContext.servletContext.contextPath}/admin/reading/"/>
            </div>
        </div>
    </div>
</div>