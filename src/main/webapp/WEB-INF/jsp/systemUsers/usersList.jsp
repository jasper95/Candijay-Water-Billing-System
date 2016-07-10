<%@ include file="../fragments/postAuth/taglibs.jsp"%>
<!DOCTYPE html>
<html>
    <jsp:include page="../fragments/postAuth/headContents.jsp"/>
    <body class="cstm">
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <jsp:include page="../fragments/postAuth/sidebar.jsp"/>
            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                <h2 class="sub-header">
                    Users
                    <a type="button" class="btn btn-ctm btn-default" style="float:right;" href="system-users/new"> 
                        Create User 
                    </a>
                </h2>              
                <div class="table-stripedesponsive">                   
                    <datatables:table dom="t" cssClass="table table-striped" id="users" url="/admin/system-users/get-all">
                        <datatables:column title="Username" property="username" sortable="false" sortInitDirection="none" renderFunction="custom-rendering#userListUsername"/>
                        <datatables:column title="Type" property="type" sortable="false"/>
                        <datatables:column title="Status" property="status" sortable="false"/>
                        <datatables:column title="Actions" sortable="false" renderFunction="custom-rendering#updateUser"/>
                        <dandelion:bundle excludes="jquery"/>
                    </datatables:table>
                </div>
            </div>
            <input id="request-uri" type="hidden" value="${requestScope['javax.servlet.forward.request_uri']}"/>
            <input id="current-user" type="hidden" value="<sec:authentication property="name"/>"/>
        <jsp:include page="../fragments/footer.jsp"/>
    </body>
</html>