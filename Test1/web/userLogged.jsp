<%@ page language="java" 
contentType="text/html; 
charset=windows-1256" 
pageEncoding="windows-1256" 
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html> 
    <head> 
        <meta http-equiv="Content-Type" 
              content="text/html; charset=windows-1256"> 
              <title> User Logged Successfully </title> 
              <style type="text/css">
                body {background-color:#b0c4de;} 
              </style>
    </head> 
    <body> 
        <%
        String userName = (String) session.getAttribute("User");
        String error = (String) session.getAttribute("Error");
        if (userName == null ) {
            request.setAttribute("Error", "Session has ended.  Please login.");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
           
        } else { %>
        
       
       <table height="100%" width="100%">
           
        <td alighn="center"> You have logged in successfully. </td> 
        <tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
        <tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
        <tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
          
           
         
        <tr>
         <td align="center"><a valign="middle" href="http://129.107.116.135:8084/Test1/GetMessageServlet?msgType=incidentMessage">Check Messages</a></td>
        </tr>
        <tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
        <tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
        
        <tr>
          <td align="center"><a valighn="middle" href="http://129.107.116.135:8084/Test1/post.jsp">Post Messages</a></td>
        </tr>
        <tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
        <tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
        
        <tr>
         <td align="center"><a valighn="middle" href="http://129.107.116.135:8084/Test1/GetPostedMessagesServlet">Check Posted Messages</a></td>
        </tr>
        <tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
        <tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>
        
        <tr>
         <td align="center"><a valighn="middle" href="http://129.107.116.135:8084/Test1/LogoutServlet">Logout</a></td>
        </tr>
        
        <% } %>
        </table>
    </body>
     
</html>