<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
    
    <jsp:include page="fragments/preAuth/headContents.jsp"/>
    <body>
        <jsp:include page="fragments/preAuth/header.jsp"/>
        <!-- Page Content -->
        <div class="container">
            <c:url value="/resources/img/center_piece.png" var="centerPiece"/>
            <img class="cnt-img" src="${centerPiece}">
            <center>
                <form class="navbar-form">
                    <input type="text" class="form-control frm-ent-accnt-nmb" placeholder="enter account number" style="width:400px;">
                </form>
            </center>
            <center ><button type="button" class="btn btn-sm btn-default btn-cstm">VIEW BILL</button></center>
            <center >
              <div class="opt-hm">
                <label style="float:left;">
                  <input type="checkbox"> <span class="rem">Remember account no.</span>
                </label>
                <a class="fgt-cnt" href="">Forgot account no.?</a>
              </div>
            </center>
        </div>
            <jsp:include page="fragments/footer.jsp"/>
            <jsp:include page="fragments/preAuth/jsBlock.jsp"/>
    </body>
</html>
