<%-- 
    Document   : report
    Created on : Dec 29, 2015, 7:06:00 PM
    Author     : Lesterrific17
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<spring:url value="/resources/" var="STATIC_URL"/>
<spring:url value="/webjars/" var="WEB_JARS"/>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="${STATIC_URL}img/cws.ico">
        <title>Candijay Water Billing System 2.0</title>
        <sec:csrfMetaTags/>
        <link href="${STATIC_URL}css/stylesheet_reports.css" rel="stylesheet">
        <link href="${STATIC_URL}css/bootstrap.min.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_gen.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_admin.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_dashboard.css" rel="stylesheet">
        <link href="${STATIC_URL}css/stylesheet_sticky-footer-navbar.css" rel="stylesheet">
        <script src="${WEB_JARS}jquery/2.0.3/jquery.js"></script>
        <script src="${STATIC_URL}js/bootstrap.min.js"></script>
    </head>
    
    <body class="cstm" id="report-body">
        <jsp:include page="../fragments/postAuth/header.jsp"/>
        <jsp:include page="../fragments/postAuth/sidebar.jsp"/>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                <div class="page-content-center" id="report-form-content">
                    <div id="report-form-header" class="theme-color">Generate Report</div>
                    <form:form id="reportForm" action="${requestScope['javax.servlet.forward.request_uri']}" modelAttribute="reportForm">
                        <div id="report-form-container">
                            <div id="not-for-chart">
                                <div class="field-holder">
                                    <form:select  items="${yearOptions}"  path="year" cssClass="animate2s" id="year">
                                    </form:select>
                                </div>
                                <div class="field-holder">
                                    <form:select cssClass="animate2s" id="month" path="month">
                                        <form:option label="Select Month" value=""/>
                                        <form:option label="January" value="1"/>
                                        <form:option label="Febuary" value="2"/>
                                        <form:option label="March" value="3"/>
                                        <form:option label="April" value="4"/>
                                        <form:option label="May" value="5"/>
                                        <form:option label="June" value="6"/>
                                        <form:option label="July" value="7"/>
                                        <form:option label="August" value="8"/>
                                        <form:option label="September" value="9"/>
                                        <form:option label="October" value="10"/>
                                        <form:option label="November" value="11"/>
                                        <form:option label="December" value="12"/>
                                    </form:select>
                                </div>          
                                <div class="field-holder visible" id="barangay-fieldh">
                                    <form:select placeholder="Barangay" cssClass="animate2s" id="barangay" path="barangay">
                                        <form:option label="Select Barangay" value=""/>
                                        <form:option label="Tugas" value="Tugas"/>
                                        <form:option label="Poblacion" value="Poblacion"/>
                                    </form:select>
                                </div>
                                <form:hidden value="0" id="summary" path="summary"/>
                                <div class="field-holder">
                                    <form:select class="animate2s" path="type">
                                        <form:option label="Select type" value=""/>
                                        <form:option label="Collectibles" value="collectibles"/>
                                        <form:option label="Collection" value="collection"/>
                                        <form:option label="Bills" value="bills"/>
                                    </form:select>
                                </div>
                                <div class="field-holder">
                                    <div class="toogle-label">Summary</div>
                                    <div id="summary-toggle" class="toggle-off">
                                        <div class="toggle-switch animate2s"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form:form>
                    <form:form modelAttribute="chartForm" action="${requestScope['javax.servlet.forward.request_uri']}">
                        <div id="report-form-container">
                            <div id="for-chart" style="display:none">
                                <div class="field-holder">
                                    <form:select items="${yearOptions}" path="year" cssClass="animate2s">
                                    </form:select>
                                </div>
                                <div class="field-holder">
                                    <form:select class="animate2s" path="type">
                                        <form:option label="Select type" value=""/>
                                        <form:option label="Water Consumption" value="1"/>
                                        <form:option label="Collection and Collectibles" value="2"/>
                                    </form:select>
                                </div>                        
                            </div>
                            <div class="field-holder">
                                <div class="toogle-label">Chart</div>
                                <div id="chart-toggle" class="toggle-off">
                                    <div class="toggle-switch animate2s"></div>
                                </div>
                            </div>
                            <button id="submit">Generate</button>   
                        </div>
                    </form:form>    
                    <input type="hidden" name="chart" value="0" id="chart" />
                </div>
            </div>
        </div>    
        <script src="${STATIC_URL}js/reports.js"></script>
    </body>
</html>
