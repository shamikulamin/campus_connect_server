<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="beans.CommunityMessageBean"%>
<jsp:useBean id="messageBean" class="beans.CommunityMessageBean" scope="request" />
<jsp:useBean id="communityMessages" type="ArrayList<beans.CommunityMessageBean>" scope="request" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Message Page</title>

<link rel="stylesheet" type="text/css" href="js/DataTables-1.9.2/media/css/jquery.dataTables.css"/>
<style type="text/css" title="currentStyle">
			@import "js/DataTables-1.9.2/media/css/demo_page.css";
			@import "js/DataTables-1.9.2/media/css/demo_table_jui.css";
			@import "js/DataTables-1.9.2/examples/examples_support/themes/smoothness/jquery-ui-1.8.4.custom.css";
</style>

<script type="text/javascript" charset="utf-8" src="js/DataTables-1.9.2/media/js/jquery.js">
</script>
<script type="text/javascript" charset="utf-8" src="js/DataTables-1.9.2/media/js/jquery.dataTables.js">
</script>
<script type="text/javascript">
    
    $(document).ready(function() {
				oTable = $('#messageTable').dataTable({
					"bJQueryUI": true,
					"sPaginationType": "full_numbers"
				});
    } );
</script>

</head>
    
    <body>
        
        <div class="demo_jui">
        <table id ="messageTable" >
            <thead>
                 
                  <tr>
                        <th>No</th>
                        <th>Title</th>
                        <th>Description</th>
                        <th>Location</th>
                        

                  </tr>
            </thead>
            <tbody>
                <%for(int i=0; i < communityMessages.size(); i++)
                {
                CommunityMessageBean comMsg = new CommunityMessageBean();
                comMsg = (CommunityMessageBean) communityMessages.get(i);
                %>
                <tr>
                <td><%= comMsg.getMsgId() %></td>
                <td><%= comMsg.getMsgTitle() %></td>
                <td><%= comMsg.getMsg_Description() %></td>
                <td><a href="showLocationInMap.jsp?latLong=<%=comMsg.getLatLong()%>">Location</a></td>
               
                </tr>
                <%}%>
            </tbody>
            <li><a alighn="right" href="http://129.107.116.135:8084/Test1/LogoutServlet">Logout</a></li>
        </table>
        </div>
    </body>
</html>
