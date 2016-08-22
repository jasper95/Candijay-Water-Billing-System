<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html lang="en">
    
    <jsp:include page="fragments/preAuth/headContents.jsp"/>
    <body class="cstm">
        <jsp:include page="fragments/preAuth/header.jsp"/>
        <!-- Page Content -->
        <div class="container">
          <div class="content">
            <div class="row" style="text-align: center;">
              <div class="span12">
                <div class="title">
                    <h2><strong>Contact Us</strong></h2>
                  <div class="hr hr-small hr-center"><span class="hr-inner"></span></div>
                  <p>We bring a personal and effective approach to every project we work on.</p>

                </div>
              </div>
              <div class="span8">
                <iframe width="100%" height="317" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src="https://maps.google.co.in/maps?f=q&amp;source=s_q&amp;hl=en&amp;geocode=&amp;q=mRova+Solutions,+Pune,+Maharashtra&amp;aq=0&amp;oq=mrova&amp;sll=18.815427,76.775144&amp;sspn=14.731137,19.577637&amp;ie=UTF8&amp;hq=mRova+Solutions,&amp;hnear=Pune,+Maharashtra&amp;t=m&amp;ll=18.526817,73.903141&amp;spn=0.073244,0.132008&amp;z=13&amp;iwloc=A&amp;output=embed"></iframe>
              </div>
              <div class="span4">
                <h4>Our Location</h4>
                <p>F 4, Vidhi Apartment, Jambulkar Chowk, Near Camelia, Wanowari</p>
                <hr class="grey"/>
                <h4>Email &amp; Phone</h4>
                <p>info@mrova.com</p>
                <p>020-60600340</p>
                <hr class="grey"/>
                <h4>Socialize With Us</h4>
                <div class="social">
                    <a target="_blank" title="find us on Facebook" href="http://www.facebook.com/PLACEHOLDER"><img alt="follow me on facebook" src="//login.create.net/images/icons/user/facebook_30x30.png" border=0></a> 
                    <a target="_blank" title="follow me on twitter" href="http://www.twitter.com/PLACEHOLDER"><img alt="follow me on twitter" src="//login.create.net/images/icons/user/twitter_30x30.png" border=0></a>
                </div>

              </div>
            </div>
          </div>
        </div> 

        <jsp:include page="fragments/footer.jsp"/>
        <jsp:include page="fragments/preAuth/jsBlock.jsp"/>
    </body>
</html>