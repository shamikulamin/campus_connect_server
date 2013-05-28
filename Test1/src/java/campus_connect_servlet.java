
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//import javax.sql.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author adnan
 */
@WebServlet(name = "campus_connect_servlet", urlPatterns = {"/campus_connect_servlet"})
public class campus_connect_servlet extends HttpServlet {
// This is just a comment for testing. 
    //static int iUserID = 0;
    Logger vLog = Logger.getLogger("com.campus");
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String sParamVal;

        sParamVal = request.getParameter("get");
        if(sParamVal != null)
        {
            if (sParamVal.equals("getCommunityMsg")) {
                // return all trap locations for now. We will modify it later.
                PrintWriter out = response.getWriter();
                String msg = "";
                try {
                    //response.
                    out.println(getCommunityMsg().toString());
                } finally {
                    out.close();
                }
            }
        } 
         if(sParamVal != null)
        {
            if (sParamVal.equals("getCommunityMsgForMap")) {
                // return all trap locations for now. We will modify it later.
                PrintWriter out = response.getWriter();
                String msg = "";
                try {
                    //response.
                    out.println(getCommunityMsgForMap().toString());
                } finally {
                    out.close();
                }
            }
        }  
        
        
        sParamVal = request.getParameter("getCommMsgDesc");
        
        if(sParamVal != null)
        {
            int ID = Integer.parseInt(sParamVal);
            
            PrintWriter out = response.getWriter();
                String msg = "";
                try {
                    //response.
                    out.println(getCommunityMsgDesc(ID));
                } finally {
                    out.close();
                }
        }
    }   

    private String getCommunityMsgDesc(int ID)
    {
        String sRet="";
        
        Connection con = getMySqlConnection();
        try
        {
            PreparedStatement pstmt = con.prepareStatement("SELECT msg_description FROM "
                    + "community_msg WHERE comm_msg_id = ?");
            pstmt.setInt(1, ID);
            ResultSet vResult = pstmt.executeQuery();
            
            while(vResult.next())
            {
                sRet = vResult.getString("msg_description");
                
            }

            
        } catch (SQLException s) {
            
        }finally{
            try{
            con.close();
            }catch(SQLException e){}
        }
        
        
        return sRet;
    }
    
    private JSONArray getCommunityMsgForMap()
    {   
        JSONArray vReturnObjects = null;
        try
        {
        vReturnObjects = new JSONArray();
      
        Connection con = getMySqlConnection();
        try {
            
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM community_msg where latlong <> 'none' and CURRENT_TIMESTAMP() < expiry_time order by reporting_time DESC");
            
            while (res.next()) 
            {
                JSONObject object = new JSONObject();
                try {
                    object.put("comm_msg_id", res.getInt("comm_msg_id"));
                    object.put("msg_title", res.getString("msg_title"));
                    object.put("msg_description", res.getString("msg_description"));
                    object.put("reporting_time", res.getString("reporting_time"));
                    object.put("latlong", res.getString("latlong"));
                    object.put("msg_type",res.getString("msg_type"));
                    object.put("expiry_time",res.getString("expiry_time"));
                } 
                catch (Exception e) 
                {
                    vLog.error(e.getStackTrace());
                }
                vReturnObjects.put(object);
                
            }
            
        } 
        catch (SQLException s) 
        {
            vLog.error(s.getMessage());
        }
        finally 
        {
            try
            {
                con.close();
            }catch(SQLException e)
            {
                vLog.error(e.getMessage());
            }
        }
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
            vLog.error(e.getMessage());
        }
        return vReturnObjects;    
    }


    private JSONArray getCommunityMsg()
    {   
        JSONArray vReturnObjects = null;
        try
        {
        vReturnObjects = new JSONArray();
      
        Connection con = getMySqlConnection();
        try {
            
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM community_msg where CURRENT_TIMESTAMP() < expiry_time order by reporting_time DESC");
            //ResultSet res = st.executeQuery("SELECT * FROM community_msg where latlong <> 'none' and CURRENT_TIMESTAMP() < expiry_time order by reporting_time DESC");
            
            while (res.next()) 
            {
                JSONObject object = new JSONObject();
                try {
                    object.put("comm_msg_id", res.getInt("comm_msg_id"));
                    object.put("msg_title", res.getString("msg_title"));
                    object.put("msg_description", res.getString("msg_description"));
                    object.put("reporting_time", res.getString("reporting_time"));
                    object.put("latlong", res.getString("latlong"));
                    object.put("msg_type",res.getString("msg_type"));
                    object.put("expiry_time",res.getString("expiry_time"));
                } 
                catch (Exception e) 
                {
                    vLog.error(e.getStackTrace());
                }
                vReturnObjects.put(object);
                
            }
            
        } 
        catch (SQLException s) 
        {
            vLog.error(s.getMessage());
        }
        finally 
        {
            try
            {
                con.close();
            }catch(SQLException e)
            {
                vLog.error(e.getMessage());
            }
        }
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
            vLog.error(e.getMessage());
        }
        return vReturnObjects;    
    }
    
    
    private String getTrapLocationString() {
        String responseBody = "";
        Connection con = getMySqlConnection();
        try {
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT latlong FROM traplocations");
            while (res.next()) {
                responseBody = responseBody + res.getString("latlong") + " ";
            }
            con.close();
        } catch (SQLException s) {
            responseBody = s.getMessage();
        }

        return responseBody;
    }

     private String getUserLocationString() {
        String responseBody = "";
        Connection con = getMySqlConnection();
        try {
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT userpos FROM userlocations");
            while (res.next()) {
                responseBody = responseBody + res.getString("userpos") + " ";
            }
            con.close();
        } catch (SQLException s) {
            responseBody = s.getMessage();
        }

        return responseBody;
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
    
    private int getNextIncidentId() {
        int maxIncidentId = 0;
        Connection con = getMySqlConnection();
        try {
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT MAX(incident_id) from incident_msg");
            while (res.next()) {
                maxIncidentId = res.getInt(1);
            }
            con.close();
        } catch (SQLException s) {
            s.printStackTrace();
        }finally{
            try {
                con.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(campus_connect_servlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return maxIncidentId;
    }
    
    private void insertIncidentMsg(String msg_title,String msg_description,String time,String latLong,ArrayList<String> imagePaths){
        Connection con = getMySqlConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("INSERT into incident_msg(msg_title,msg_description,reporting_time,latlong) VALUES(?,?,?,?)");
            pstmt.setString(1, msg_title);
            pstmt.setString(2, msg_description);
            pstmt.setTimestamp(3, getCurrentDate(Long.parseLong(time)));
            pstmt.setString(4, latLong);
            
            pstmt.executeUpdate();
            
            PreparedStatement pstmtPic = con.prepareStatement("INSERT into incident_picture(incident_id,picture) Values(?,?)");
            
            int incidentId = getNextIncidentId();
            
            
            Iterator it = imagePaths.iterator();
            while(it.hasNext()){
                String path = (String)it.next();
                pstmtPic.setInt(1, incidentId);
                pstmtPic.setString(2, path);
                
                pstmtPic.executeUpdate();
            }
            
            
            
            
        }catch (SQLException s) {
            s.printStackTrace();
        }finally{
            try {
               // pstmt.close();
                con.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(campus_connect_servlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ;
        
    }
    
    private static java.sql.Timestamp getCurrentDate(long time) {
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(time);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    
    

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException
   {
       

// get next incident id
       int maxIncidentID = getNextIncidentId() + 1;
       String msg_title= "";
       String msg_description= "";
       String time= "";
       String latLong = "";
       String userID="";
       String password="";
       ArrayList<String> imagePaths = new ArrayList<String>();
       
       try
       {
           String sImagePath = "C:/incidentImages/"+maxIncidentID+"/";
           boolean success = ( new File(sImagePath)).mkdirs();
           if (success) {
                System.out.println("Directory: " + sImagePath + " created");
           }  
  
           File vDir = new File(sImagePath);
           List<FileItem> items = new ServletFileUpload(new
                        DiskFileItemFactory()).parseRequest(request);
           for (FileItem item : items)
           {
               if (item.getFieldName().equals("image"))
               {
                   String fileName = item.getName();
                   //String fileContentType = item.getContentType();
                   InputStream fileContent = item.getInputStream();

                   BufferedImage bImageFromConvert = ImageIO.read(fileContent);
                   imagePaths.add(sImagePath+fileName);
                   
                   File vImageFile = new File(sImagePath+fileName);

                   ImageIO.write(bImageFromConvert, "jpg", vImageFile);
                   //Image img = ImageIO.read(new File(sImagePath+fileName)).getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);
                   
                    BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
                    img.createGraphics().drawImage(ImageIO.read(vImageFile).getScaledInstance(100, 100, Image.SCALE_SMOOTH),0,0,null);
                    ImageIO.write(img, "jpg", new File(sImagePath+fileName+"_thumb.jpg"));

               }
               if(item.getFieldName().equals("message"))
               {
//                   File dataFile = new
//                    File(sImagePath+System.currentTimeMillis());
                   msg_description = item.getString();
                   /*FileWriter v = new FileWriter(dataFile);
                   v.append(sVal);
                   v.close();*/
               }
               
                if(item.getFieldName().equals("latitude"))
                {
                   /*File dataFile = new File(sImagePath+"gps_"+System.currentTimeMillis() +".txt");
                   String sVal = item.getString();
                   FileWriter v = new FileWriter(dataFile);
                   v.append(sVal);
                   v.close();*/
                   latLong = item.getString();
                }
                if(item.getFieldName().equals("longitude"))
                {                   
                   latLong = latLong + "," + item.getString();                   
                }
                
                if(item.getFieldName().equals("message_title"))
                {                   
                   msg_title = item.getString();
                }
                
                if(item.getFieldName().equals("reporting_time"))
                {                   
                   time = item.getString();
                }
                
                if(item.getFieldName().equals("uid"))
                {
                    userID=item.getString();
                }
                
                if(item.getFieldName().equals("pass"))
                {
                    password=item.getString();
                }
                
                
           }
       } catch (Exception e) {
           throw new ServletException("Cannot parse multipart request.", e);
       }
       // run a query to insert a row with the incident id (already received) and latlong and message details and image paths.
       insertIncidentMsg(msg_title,msg_description,time,latLong,imagePaths);
       processRequest(request, response);

   }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

        
