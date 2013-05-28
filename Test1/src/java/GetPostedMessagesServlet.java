/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import beans.CommunityMessageBean;
import beans.IncidentMessageBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Adnan
 */
@WebServlet(name = "GetPostedMessagesServlet", urlPatterns = {"/GetPostedMessagesServlet"})
public class GetPostedMessagesServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GetPostedMessagesServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GetPostedMessagesServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       HttpSession session = request.getSession(true);
       String validFlag = (String)session.getAttribute("User");
        if(validFlag == null){
            session.setAttribute("Error", "page has Expired");
            RequestDispatcher dispatcher = request.getRequestDispatcher("LoginExample.jsp");
            dispatcher.forward( request, response); 
        }
        
        Connection conn = getMySqlConnection();
        
         if (conn != null)
        {
            try {
                ResultSet rs = null;
                Statement stmt = null;
                String strQuery = "select * from community_msg";
                stmt = conn.createStatement();
                rs = stmt.executeQuery( strQuery);
                ArrayList<CommunityMessageBean> messages  = new ArrayList<CommunityMessageBean>();
                
                while(rs.next()){
                     CommunityMessageBean comMsg = new CommunityMessageBean();
                     comMsg.setMsgId(rs.getInt("comm_msg_id"));
                     comMsg.setMsgTitle(rs.getString("msg_title"));
                     comMsg.setMsg_Description(rs.getString("msg_description"));
                     comMsg.setReportingTime(rs.getString("reporting_time"));
                     String latLong = rs.getString("latlong");
                     if(latLong != null){
                        comMsg.setLatLong(latLong);
                     }
                     
                     messages.add(comMsg);
                }
                request.setAttribute("communityMessages", messages);
                RequestDispatcher dispatcher = request.getRequestDispatcher("PostedMessages.jsp");
                dispatcher.forward( request, response);
            } catch (SQLException ex) {
                Logger.getLogger(GetPostedMessagesServlet.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(GetMessageServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
           } 
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    private Connection getMySqlConnection() {
        Connection con = null;
        String url = "jdbc:mysql://localhost:3306/";
        String db = "campus_connect";
        String driver = "com.mysql.jdbc.Driver";
        String user = "root";
        String pass = "adnan";
        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url + db, user, pass);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return con;
    }
}
