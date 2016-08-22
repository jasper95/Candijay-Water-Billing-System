<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <jsp:include page="fragments/preAuth/headContents.jsp"/>
    <body class="cstm">
        <jsp:include page="fragments/preAuth/header.jsp"/>
        
        <div class="container">
          <h1><strong>The Staff</strong>
           <br>
           <small>We do everything with our core values of <strong>honesty</strong>, hard <strong>work</strong>, and <strong>trust</strong>.</small>
           <hr>
          </h1>
          <!-- Example row of columns -->
          <div class="row">
            <div class="col-md-4">
              <spring:url value="/resources/img/staff.png" var="staffImg"/>
              <img src="${staffImg}" class="staff-img">
              <h4><strong>Dominique de Loyola</strong></h4>
              <p class="staff-ds">Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
            </div>
            <div class="col-md-4">
              <img src="${staffImg}" class="staff-img">
              <h4><strong>Dominique de Loyola</strong></h4>
              <p class="staff-ds">Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
            </div>
            <div class="col-md-4">
              <img src="${staffImg}" class="staff-img">
              <h4><strong>Dominique de Loyola</strong></h4>
              <p class="staff-ds">Donec sed odio dui. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Vestibulum id ligula porta felis euismod semper. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus.</p>
            </div>
          </div>
          <div class="row">
            <div class="col-md-4">
              <spring:url value="/resources/img/staff.png" var="staffImg"/>
              <img src="${staffImg}" class="staff-img">
              <h4><strong>Dominique de Loyola</strong></h4>
              <p class="staff-ds">Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
            </div>
            <div class="col-md-4">
              <img src="${staffImg}" class="staff-img">
              <h4><strong>Dominique de Loyola</strong></h4>
              <p class="staff-ds">Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
            </div>
            <div class="col-md-4">
              <img src="${staffImg}" class="staff-img">
              <h4><strong>Dominique de Loyola</strong></h4>
              <p class="staff-ds">Donec sed odio dui. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Vestibulum id ligula porta felis euismod semper. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus.</p>
            </div>
          </div>
        </div> 

    <jsp:include page="fragments/footer.jsp"/>
    </body>
</html>
