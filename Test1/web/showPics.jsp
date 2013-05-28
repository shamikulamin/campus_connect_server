<%-- 
    Document   : showPics
    Created on : Jul 11, 2012, 12:13:17 PM
    Author     : Adnan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
         
        <style type="text/css" title="currentStyle">
		 @import "js/pics/css/zoom.css";		
        </style>

        <script type="text/javascript" charset="utf-8" src="js/DataTables-1.9.2/media/js/jquery.js">
        </script>
        
        <script type="text/javascript" src="js/pics/js/zoom.js"></script>
        
        <script type="text/javascript">
               
             $(function(){
                 //alert("adnan");
                 $.zoom();
             });   
            
        </script>
    </head>
    <body>
         
        <div class="container">
	<h3>Images</h3>
        <ul class="gallery">
         <% 
            String noOfImages = (String)request.getParameter("noOfPics");
            
            String incidentIdentifier = (String)request.getParameter("incidentId");
           
            int imageCount = Integer.parseInt(noOfImages);
            int incidentId =  Integer.parseInt(incidentIdentifier);
            //int incidentId = Integer.parseInt(picdetails.split("|")[1]);
            for(int i=0; i < imageCount; i++)
            {
            
            %>
            
            <li><a href="/incidentImages/<%= incidentId%>/<%= i %>.jpg" title="PIC"><img 
                        src="/incidentImages/<%= incidentId%>/<%= i%>.jpg_thumb.jpg" title="PIC"/></a></li>
            
            
            <%}%>
        </ul>
        
        </div>
         
         
    </body>
</html>
