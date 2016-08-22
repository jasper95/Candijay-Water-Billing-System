<%@ include file="fragments/postAuth/taglibs.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="icon" href="${STATIC_URL}img/cws.ico">
        <title>System Error</title>
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/font-awesome.min.css" rel="stylesheet">
        <sec:csrfMetaTags/>
    </head>
    <body>
        <jsp:include page="fragments/postAuth/header.jsp"/>
        <div id="wrapper">
            <jsp:include page="fragments/postAuth/sidebar.jsp"/>
            <div id="page-content-wrapper">
                <div class="page-content">
                    <div class="container-fluid">
                        <img class="img-responsive center-block" src="${STATIC_URL}/img/sad.png">
                        <div style="text-align: center; color: #800000; font-weight: 700">
                            <p style="font-size: 30px">Something went wrong....</p>
                            <p style="font-size: 24px">Error: ${type}</p>
                            <p style="font-size: 20px">Reason: ${message}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>