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
        <title>Settings</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_sticky-footer-navbar.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-10 ">
                    <h2>Update Settings</h2>
                </div>
            </div>
            <c:if test="${updateSuccess == 1}">
                <div style="text-align: center" class="alert alert-success" style="border-radius:5px;"><br>
                    <h4>Settings successfully updated!</h4>
                </div>
            </c:if>
            <form:form modelAttribute="settings" id="settingsForm" method="POST">
                <div class="form-wrapper">
                    <div class="col-sm-12 form-group">
                        <cws:input id="s-penalty" name="penalty" label="Penalty" icon="money" placeholder="Enter penalty" required="true"/>
                        <cws:input id="s-basic" name="basic" label="Basic" icon="money" placeholder="Enter basic" required="true"/>
                        <cws:input id="s-pes" name="pes" label="PES" icon="money" placeholder="Enter pes" required="true"/>
                    </div>
                    <div class="col-sm-12 form-group">
                        <cws:input id="s-depFund" name="depreciationFund" label="Depreciation Fund" icon="money" placeholder="Enter depfund" required="true"/>
                        <cws:input id="s-sysloss" name="systemLoss" label="System Loss" icon="money" placeholder="Enter sysloss" required="true"/>
                        <cws:input id="s-debtsOK" name="debtsAllowed" label="Debts Allowed" icon="money" placeholder="Enter debts allowed" required="true"/>
                    </div>
                    <div class="col-sm-12 form-group">
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <div class="pull-right">
                                    <button type="submit" class="btn btn-primary">Save</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
        </div>
        <script src="${WEB_JARS}jquery/2.0.3/jquery.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
        <script src="${STATIC_URL}js/helpers/form-validation.js"></script>
        <script src="${STATIC_URL}js/global.js"></script>
    </body>
</html>