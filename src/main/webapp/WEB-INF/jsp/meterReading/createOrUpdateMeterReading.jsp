<%-- 
    Document   : createOrUpdateMeterReading
    Created on : May 20, 2015, 6:39:33 PM
    Author     : Bert
--%>
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
        <title>${createOrUpdate} Reading</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_gen.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_dashboard.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_sticky-footer-navbar.css" rel="stylesheet">
        <script src="${WEB_JARS}jquery/2.0.3/jquery.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
    </head>
    <body class="cstm">
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <jsp:include page="../fragments/postAuth/sidebar.jsp"/>
        <div class="containerl-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h2 class="sub-header" style="margin-top:15px;"><a href="${pageContext.servletContext.contextPath}/admin/reading"><img src="<c:url value="/resources/img/back.png"/>" style="margin-bottom:5px;" class=""></a> ${createOrUpdate} Meter Reading</button>
            </h2>
            <div style="display: ${createOrUpdate == 'Create' ? 'anything' : 'none'}"/>
                <form:form modelAttribute="searchForm" id="fetchAccount" action="${pageContext.servletContext.contextPath}/admin/reading">
                    <form:hidden path="create" value="${createOrUpdate == 'Create' ? 'true' : 'false'}"/>
                    <div id="search-error" class="alert alert-danger" style="border-radius:5px;display:none">Account does not exists!</div>
                    <cws:inputField cssStyle="background-image: url('${STATIC_URL}img/un.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="accountNumber" label="Account Number"/><input type="submit" class="btn btn-ctm btn-primary" value="Find"/>    
                </form:form>
                <hr>
            </div>
            <div id="crt-mr-found">
                <p><img class="un-pw-img" src="${STATIC_URL}img/un.png" style="margin-top:-5px;margin-right:9px;"><strong> <span id="full-name" ></span></strong></p>
                <p><img class="un-pw-img" src="${STATIC_URL}img/address.PNG" style="margin-top:-5px; margin-right:10px;"><strong><span id="address"></span></strong></p>                
                <p><img class="un-pw-img" src="${STATIC_URL}img/current_reading.png" style="margin-top:-5px; margin-right:10px;"><strong><span id="last-reading"></span></strong></p>
                <p><img class="un-pw-img" src="${STATIC_URL}img/status.png" style="margin-top:-5px; margin-right:10px;"><span id="status" style="font-size:15px;"></span></p>
                <hr>
                <p><strong>Latest Reading:</strong></p>
                <div class="table-stripedesponsive">
                  <table class="table table-striped" id="recent-readings">
                    <thead>
                      <tr>
                        <th>Schedule</th>
                        <th>Consumption</th>
                        <th>Reading</th>
                        <th>Status</th>
                      </tr>
                    </thead>
                    <tbody>             
                    </tbody>
                  </table>
                </div>
                <hr>
                <p>
                    <strong>Add Reading</strong>
                </p>
                <form:form action="${requestScope['javax.servlet.forward.request_uri']}" modelAttribute="meterReadingForm" method="post" id="add-meterReading-form">
                    <div id="reading-error" class="alert alert-danger" style="border-radius:5px; display: none;"></div>
                    <div id="reading-success" class="alert alert-success" style="border-radius:5px; display: none;"></div>
                    <form:hidden path="accountId"/>
                    <div class="navbar-form">
                        <form:select id="reading-month" path="meterReading.schedule.month" cssClass="form-control frm-ent-accnt-nmb log-un" cssStyle="background-image: url('${STATIC_URL}img/month.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;">       
                            <form:option value="" label="Select Month"/>
                            <form:option value="1" label="Jan"/>
                            <form:option value="2" label="Feb"/>
                            <form:option value="3" label="Mar"/>
                            <form:option value="4" label="Apr"/>
                            <form:option value="5" label="May"/>
                            <form:option value="6" label="June"/>
                            <form:option value="7" label="Jul"/>
                            <form:option value="8" label="Aug"/>
                            <form:option value="9" label="Sept"/>
                            <form:option value="10" label="Oct"/>
                            <form:option value="11" label="Nov"/>
                            <form:option value="12" label="Dec"/>
                        </form:select>           
                    </div>
                    <div class="navbar-form">
                        <form:select id="reading-year" items="${yearOptions}" path="meterReading.schedule.year" cssClass="form-control frm-ent-accnt-nmb log-un" cssStyle="background-image: url('${STATIC_URL}img/year.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;"/>           
                    </div>
                    <div class="navbar-form">
                        <cws:inputField id="readingVal" cssStyle="background-image: url('${STATIC_URL}img/current_reading.png');background-size: contain; background-repeat:no-repeat; padding-left: 45px;" name="meterReading.readingValue" label="Current Reading"/>
                    </div>
                    <br>
                    <button type="submit" class="btn btn-ctm btn-primary" > Create Reading </button> 
                    <br><br><br><br><br><br>
                </form:form>
            </div>  
        </div>
        <c:choose>
            <c:when test="${createOrUpdate == 'Create'}">
                <script src="${STATIC_URL}js/meter-reading/create.js"></script>
            </c:when>
            <c:otherwise>
                <script src="${STATIC_URL}js/meter-reading/update.js"></script>
            </c:otherwise>
        </c:choose>
        <jsp:include page="../fragments/footer.jsp"/>
        <script src="${bootstrapJs}"> </script>
    </body>
</html>