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
        <title>Customers</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/bootstrap-dialog.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <div id="content-loader" class="loader"></div>
        <div id="main-content" style="display:none" class="container">
            <div class="row">
                <div class="col-sm-9">
                    <h2>Customers</h2>
                </div>
                <div class="col-sm-3 vertical-center text-right">
                    <a type="button" class="btn btn-ctm btn-default" href="${pageContext.servletContext.contextPath}/admin/customers/new/">Create Customer</a>
                </div>
            </div>
            <div id="search-filters" class="alert alert-info form-wrapper">
                <div class="col-sm-12">
                    <div class="col-md-3" id="last-name">
                        <label>Lastname</label>
                    </div>
                    <div class="col-md-3" id="first-name">
                        <label>Firstname</label>
                    </div>
                    <div class="col-md-3 pull-align-right vertical-center filter-btn-wrapper">
                        <a id="filterClearButton" type="button" class="btn btn-danger list-filter-btn"><i class="fa fa-remove fa-fw"></i> Reset </a>
                        <a id="filterButton" type="button" class="btn btn-primary list-filter-btn"><i class="fa fa-search fa-fw"></i> Search </a>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 main">
                    <div class="table-responsive">
                        <datatables:table deferLoading="0" deferRender="true" id="customer" cssClass="table table-striped" dom="ltipr" url="${spring:mvcUrl('datatables-api#customers').build()}" serverSide="true" filterPlaceholder="none" filterSelector="#filterButton" filterClearSelector="#filterClearButton">
                            <datatables:column visible="false" filterable="true" property="lastname" sortInitDirection="asc" sortInitOrder="0"/>
                            <datatables:column visible="false" property="firstName" sortInitDirection="asc" sortInitOrder="1"/>
                            <datatables:column title="Name"  sortable="false" property="name" renderFunction="custom-rendering#customerUrl"/>
                            <datatables:column title="Gender" sortable="false" property="gender"/>
                            <datatables:column title="Occupation" sortable="false" property="occupation"/>
                            <datatables:column title="Contact" sortable="false" property="contactNumber"/>
                            <datatables:column title="Members" sortable="false" property="familyMembersCount"/>
                            <datatables:column title="Edit" renderFunction="custom-rendering#customerListActions" sortable="false"/>
                            <datatables:extraJs bundles="customer" placeholder="before_end_document_ready" />
                            <datatables:extraJs bundles="session-timeout" placeholder="before_end_document_ready"/>
                            <dandelion:bundle excludes="jquery"/>
                        </datatables:table>
                    </div>
                </div>
            </div>
        </div>
        <script src="${WEB_JARS}jquery/2.0.3/jquery.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap-dialog.min.js"></script>
        <script src="${STATIC_URL}js/helpers/form-validation.js"></script>
        <script src="${STATIC_URL}js/global.js"></script>
        <script>
            $(document).ready(function(){
                $('#content-loader').hide()
                $('#main-content').show();
            });
        </script>
    </body>
</html>