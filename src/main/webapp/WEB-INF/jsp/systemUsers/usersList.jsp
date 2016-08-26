<%@ include file="../fragments/postAuth/taglibs.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="${STATIC_URL}img/cws.ico">
        <title>System Users</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/bootstrap-toggle.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_sticky-footer-navbar.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <div id="content-loader" class="loader"></div>
        <div id="main-content" style="display:none" class="container-fluid">
            <div class="row">
                <div class="col-sm-10">
                    <h2>System Users</h2>
                </div>
                <div class="col-sm-2 vertical-center">
                    <button id="add-user" type="button" class="btn btn-default">Create User</button>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 main">
                    <div class="table-responsive">
                        <a id="reload-table" class="hidden"></a>
                        <datatables:table dom="t" reloadSelector="#reload-table" cssClass="table table-striped" id="users" url="/admin/system-users/get-all">
                            <datatables:column title="Username" property="username" sortable="false" sortInitDirection="none" renderFunction="custom-rendering#userListUsername"/>
                            <datatables:column title="Type" property="type" cssCellClass="type" sortable="false"/>
                            <datatables:column title="Status" property="status" cssCellClass="status
                            " sortable="false"/>
                            <datatables:column title="Edit" sortable="false" renderFunction="custom-rendering#updateUser"/>
                            <dandelion:bundle excludes="jquery"/>
                        </datatables:table>
                        <input id="request-uri" type="hidden" value="${requestScope['javax.servlet.forward.request_uri']}"/>
                        <input id="current-user" type="hidden" value="<sec:authentication property="name"/>"/>
                        <input type="hidden" id="row-num"/>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="../fragments/modals/user-form.jsp"/>
        <script src="${WEB_JARS}jquery/2.0.3/jquery.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${STATIC_URL}js/helpers/form-validation.js"></script>
        <script src="${STATIC_URL}js/global.js"></script>
        <script src="${STATIC_URL}js/bootstrap-toggle.min.js"></script>
        <script src="${STATIC_URL}js/sys-user/list.js"></script>
        <script>
            $(document).ready(function(){
                $('#content-loader').hide()
                $('#main-content').show();
            });
        </script>
    </body>
</html>