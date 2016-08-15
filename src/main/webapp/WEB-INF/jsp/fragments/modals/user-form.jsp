<%@ include file="../postAuth/taglibs.jsp"%>
<div class="modal fade" id="user-form-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button id="close-dv-form" type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title"> <span id="user-action"></span> User</h4>
            </div>
            <div class="modal-body">
                <fieldset>
                    <form:form id="user-form" modelAttribute="user" role="form">
                        <div class="col-sm-12">
                            <div class="col-sm-12 alert alert-danger global-errors"></div>
                            <div class="col-sm-12 alert alert-success success-msg"></div>
                        </div>
                        <div class="col-sm-12 form-group create">
                            <cws:input id="fn" name="fullName" label="Fullname" icon="user" placeholder="Enter full name" required="true" size="6"/>
                            <cws:input id="un" name="username" label="Username" icon="user" placeholder="Enter user name" required="true" size="6"/>
                        </div>
                        <div class="col-sm-12 form-group create">
                            <div class="col-sm-6 has-feedback ">
                                <label class="control-label" for="pw">Password</label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-lock fa-fw"></i></span>
                                    <input id="pw" type="password" name="password" placeholder="Enter Password" class="form-control" required="true">
                                </div>
                            </div>
                            <div id="rt-container" class="col-sm-6 has-feedback ">
                                <label class="control-label" for="rt-pw">Retype Password</label>
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-lock fa-fw"></i></span>
                                    <input id="rt-pw" name="retype" placeholder="Retype password" class="form-control" required="true" type="password">
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-12 form-group">
                            <div class="col-sm-12">
                                <label class="control-label block" style="display: block;">Privileges</label>
                                <c:forEach var="role" items="${roles}">
                                    <div class="checkbox-items">
                                        <label class="checkbox-inline">
                                            <form:checkbox path="roles" value="${role.id}" data-toggle="toggle" data-width="120px" data-on="<i class='fa fa-check'></i> Allowed" data-off="<i class='fa fa-remove'></i> Not Allowed" /> <label>${role.description}</label>
                                        </label>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <div class="pull-right">
                                    <input id="create-submit" type="submit" class="btn btn-primary" value="Submit">
                                </div>
                            </div>
                        </div>
                    </form:form>
                    <form:form id="user-update-form" cssStyle="display: hidden;" modelAttribute="user" role="form">
                        <div class="col-sm-12">
                            <div class="col-sm-12 alert alert-danger global-errors"></div>
                            <div class="col-sm-12 alert alert-success success-msg"></div>
                        </div>
                        <div class="col-sm-12 form-group">
                            <div class="col-sm-12 checkbox-container">
                                <label class="control-label block" style="display: block;">Privileges</label>
                                <c:forEach var="role" items="${roles}">
                                    <div class="checkbox-items">
                                        <label class="checkbox-inline">
                                            <form:checkbox id="roles-item-${role.id}" path="roles" value="${role.id}" data-width="120px" data-toggle="toggle" data-on="<i class='fa fa-check'></i> Allowed" data-off="<i class='fa fa-remove'></i> Not Allowed" /> <label>${role.description}</label>
                                        </label>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="col-sm-12 form-group">
                            <div class="col-sm-6">
                                <label class="control-label" style="display:block">Status</label>
                                <div class="checkbox-items">
                                    <label class="checkbox-inline">
                                        <input id="status-toggle" type="checkbox" data-toggle="toggle" data-width="100px" data-on="<i class='fa fa-check'></i> Active" data-off="<i class='fa fa-remove'></i> Inactive">
                                    </label>
                                </div>
                            </div>
                        </div>
                        <form:hidden path="id" id="upd-id"/>
                        <form:hidden path="version" id="upd-vs"/>
                        <input type="hidden" name="updateStatus" id="upd-status" value="0"/>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <div class="pull-right">
                                    <input type="submit" class="btn btn-primary" value="Submit">
                                </div>
                            </div>
                        </div>
                    </form:form>
                </fieldset>
            </div>
        </div>
    </div>
</div>