/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import beans.IncidentMessageBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adnan
 */
@WebServlet(name = "GetMessageServlet", urlPatterns = {"/GetMessageServlet"})
public class GetMessageServlet extends HttpServlet {

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
            out.println("<title>Servlet GetCommunityMessageServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GetCommunityMessageServlet at " + request.getContextPath() + "</h1>");
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
        //processRequest(request, response);
        HttpSession session = request.getSession(true);
        Connection conn = getMySqlConnection();  
        ArrayList<IncidentMessageBean> messages  = new ArrayList<IncidentMessageBean>();
        
        String msgType = (String)request.getParameter("msgType");
        System.out.println(msgType);
        String validFlag = (String)session.getAttribute("User");
        if(validFlag == null){
            session.setAttribute("Error", "page has Expired");
            RequestDispatcher dispatcher = request.getRequestDispatcher("LoginExample.jsp");
            dispatcher.forward( request, response); 
        }
        
        if (conn != null)
        {
          ResultSet rs = null;
          Statement stmt = null;
          if(msgType.equalsIgnoreCase("incidentMessage")){
                try
                {
                    String strQuery = "SELECT * FROM incident_msg WHERE reporting_time != '0000-00-00' ORDER BY reporting_time DESC";//"select * from incident_msg ORDER BY reporting_time ASC UNION SELECT * FROM  incident_msg WHERE DATE = '0000-00-00' ORDER BY msg_title ASC";
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery( strQuery); 

                    while(rs.next()){
                        IncidentMessageBean messageBean = new IncidentMessageBean();
                        int incidentId = rs.getInt("incident_id");

                        messageBean.setIncidentId(incidentId);
                        messageBean.setMessageDescription(rs.getString("msg_description"));
                        messageBean.setMessageTitle(rs.getString("msg_title"));
                        
                        
                        String latLong = rs.getString("latlong");
                        
                        
                        messageBean.setLatLong(latLong);
                        messages.add(messageBean);
                        
                        
                        
                        String strQuery1 = "select count(*) from incident_picture where incident_id = " + incidentId;
                               
                        ResultSet rs1 = null;
                        Statement stmt1 = null;
                        int noOfPics = 0;
                          
                        stmt1 = conn.createStatement();
                        rs1 = stmt1.executeQuery(strQuery1);
                       
                                
                        while(rs1.next()){
                            noOfPics=rs1.getInt(1);
                        }
                        messageBean.setNoOfImages(noOfPics);
                    }
                    request.setAttribute("incidentMessages", messages);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("messages.jsp");
                    dispatcher.forward( request, response); 

                }catch(SQLException se){
                    Logger.getLogger(GetMessageServlet.class.getName()).log(Level.SEVERE, null, se);
                    se.printStackTrace();
                }finally{
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(GetMessageServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
          }
          
         /*else if(msgType.equalsIgnoreCase("postMessage")){
              RequestDispatcher dispatcher = request.getRequestDispatcher("messagepost.jsp");
              dispatcher.forward( request, response);               
          }*/
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
        //processRequest(request, response);
    }
    
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
