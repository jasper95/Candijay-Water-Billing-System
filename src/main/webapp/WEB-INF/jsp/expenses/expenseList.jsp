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
        <title>Expenses</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/bootstrap-dialog.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
        <sec:csrfMetaTags/>
    </head>
    <body>
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <div id="content-loader" class="loader"></div>
        <div id="main-content" style="display:none" class="container">
            <div class="row">
                <div class="col-sm-10">
                    <h2>Expenses</h2>
                </div>
                <div class="col-sm-2 vertical-center">
                    <button id="create-expense" type="button" class="btn btn-ctm btn-default">Create Expenses</button>
                </div>
            </div>
            <div class="alert alert-info form-wrapper">
                <div class="col-sm-12 form-group">
                    <div class="col-md-3" id="expense-type">
                        <label>Type</label>
                    </div>
                    <div class="col-md-3" id="expense-month">
                        <label>Month</label>
                    </div>
                    <div class="col-md-3" id="expense-year">
                        <label>Year</label>
                    </div>
                    <div class="pull-align-right col-md-3 vertical-center filter-btn-wrapper">
                        <a id="filterClearButton" type="button" class="btn btn-danger list-filter-btn"><i class="fa fa-remove fa-fw"></i> Reset </a>
                        <a id="filterButton" type="button" class="btn btn-primary list-filter-btn"><i class="fa fa-search fa-fw"></i> Search </a>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 main">
                    <div class="table-responsive">
                        <datatables:table deferLoading="0" deferRender="true" dom="ltipr" id="expense" cssClass="table table-striped" url="/admin/expenses/datatable-search" serverSide="true" filterPlaceholder="none" filterSelector="#filterButton" filterClearSelector="#filterClearButton">
                            <datatables:column name="id" property="id" filterable="true" visible="false" sortInitOrder="0" sortInitDirection="desc"/>
                            <datatables:column title="Month" name="month" property="schedule.month" cssCellClass="month" renderFunction="custom-rendering#month" />
                            <datatables:column title="Year" name ="year" property="schedule.year" cssCellClass="year"/>
                            <datatables:column title="Amount" name="amount" property="amount" sortable="false" cssCellClass="amount" renderFunction="custom-rendering#toPeso"/>
                            <datatables:column title="Type" name="type" property="type" sortable="false" cssCellClass="type" renderFunction="custom-rendering#expenseType"/>
                            <datatables:column title="Edit" sortable="false" renderFunction="custom-rendering#readingActions"/>
                            <datatables:column title="Audit" sortable="false" renderFunction="custom-rendering#audit"/>
                            <dandelion:bundle excludes="jquery"/>
                            <datatables:extraJs bundles="expense" placeholder="before_end_document_ready" />
                            <datatables:extraJs bundles="session-timeout" placeholder="before_end_document_ready"/>
                            <datatables:extraJs bundles="months" placeholder="after_all"/>
                        </datatables:table>
                        <input type="hidden" id="row-num">
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="../fragments/modals/expense-form.jsp"/>
        <jsp:include page="../fragments/modals/expense-info.jsp"/>
        <script src="${WEB_JARS}jquery/2.0.3/jquery.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${STATIC_URL}js/bootstrap-dialog.min.js"></script>
        <script src="${STATIC_URL}js/helpers/form-validation.js"></script>
        <script src="${STATIC_URL}js/global.js"></script>
        <script src="${STATIC_URL}js/expenses/list.js"></script>
        <script>
            $(document).ready(function(){
                $('#content-loader').hide()
                $('#main-content').show();
            });
        </script>
    </body>
</html>
