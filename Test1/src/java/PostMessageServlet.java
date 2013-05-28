/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import PushNotif.*;
import com.google.android.gcm.server.*;
import java.sql.*;

/**
 *
 * @author Adnan
 */
@WebServlet(name = "PostMessageServlet", urlPatterns = {"/PostMessageServlet"})
public class PostMessageServlet extends HttpServlet {

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
    private static final String API_KEY = "AIzaSyCVZ_FxaGmbvCjbJ_vShvdL3g-86pJIZR0";
    private static final String url = "jdbc:mysql://localhost:3306/campus_connect", user = "root", password = "adnan";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String name = (String)request.getParameter("name");
            System.out.println(name); 
        } finally {            
            out.close();
        }
    }
    
    private void sendpushnotif(String title, String text,String locationList, String msgType)
    {
        /*HttpClient httpClient=new DefaultHttpClient();
        HttpPost post=new HttpPost("http://localhost:8084/PushNotifServer/Push");
        try {
      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
      nameValuePairs.add(new BasicNameValuePair("title",title));
      nameValuePairs.add(new BasicNameValuePair("text",text));
      nameValuePairs.add(new BasicNameValuePair("location",locationList));
      nameValuePairs.add(new BasicNameValuePair("messagetype",msgType));
      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 
      HttpResponse response = httpClient.execute(post);

    } catch (IOException e) {
      e.printStackTrace();
    }*/
        sendPush(title,text,locationList,msgType);
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
       // processRequest(request, response);
        /*HttpSession session = request.getSession(true);
        Connection conn = getMySqlConnection(); 
        
        String msgType = (String)request.getParameter("messagetype");
        System.out.println(msgType);
        
        String message = (String)request.getParameter("message");
        System.out.println(message);*/
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
        HttpSession session = request.getSession(true);
        Connection conn = getMySqlConnection(); 
        PrintWriter out = response.getWriter();
        
        String msgType = (String)request.getParameter("messagetype");
        System.out.println(msgType);
        
        
        String message = (String)request.getParameter("message");
        System.out.println(message);
        
        String messageTitle = (String)request.getParameter("msgTitle");
        System.out.println(messageTitle);
        
        String expiryHours = (String)request.getParameter("expiryhours");
        System.out.println(expiryHours);
        
        String expiryDays = (String)request.getParameter("expirydays");
        System.out.println(expiryDays);
        
        String pushCheck = (String)request.getParameter("push");
        
        
        
        long delay = Long.parseLong(expiryHours) * 60 * 60 * 1000 + Long.parseLong(expiryDays) * 24 * 60 * 60 * 1000;
        if(delay == 0)
            delay = 24 * 60 * 60 * 1000;
        
        String locationList = (String)request.getParameter("location_list");
        System.out.println(locationList);
        String validFlag = (String)session.getAttribute("User");
        if(validFlag == null){
            session.setAttribute("Error", "page has Expired");
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward( request, response); 
        }
        
        
        
        String query = "insert into community_msg(msg_title, msg_description,reporting_time,latlong,msg_type,expiry_time)VALUES(?, ?, ?, ?, ?,?)";
        PreparedStatement pstmt = null;
        try{
            int index = 1;
            pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); // create a statement
            
            pstmt.setString(index++, messageTitle); // set input parameter 2
            pstmt.setString(index++, message); // set input parameter 3
            long curTime = System.currentTimeMillis();
            pstmt.setTimestamp(index++, getCurrentDate(curTime));
           

            if(locationList == null || locationList.equals(""))
                 pstmt.setString(index++, "none");
            else
                pstmt.setString(index++, formatLatLong(locationList));
            pstmt.setString(index++, msgType);
            
            
            
            pstmt.setTimestamp(index++, getExpiryDate(curTime, delay));
             
            
            pstmt.executeUpdate(); // execute insert statement
            ResultSet vGeneratedID =  pstmt.getGeneratedKeys();
            int iMsgID = 0;
            
            if (vGeneratedID != null && vGeneratedID.next())
                iMsgID = vGeneratedID.getInt(1);
             
            
            if (pushCheck!=null){
                if(locationList.equals("")){
                    sendpushnotif(messageTitle,message,locationList,msgType);
                } else {
                    sendpushnotif(messageTitle,message,formatLatLong(locationList),msgType);
                }
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("userLogged.jsp");
            dispatcher.forward( request, response); 
            
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(PostMessageServlet.class.getName()).log(Level.SEVERE, null, ex);
            }          
        }
        
        
        
    }
    
    private String formatLatLong(String latLongList){
        String formattedLatLong = "";
        StringBuilder locationListModified = new StringBuilder();
        for(int i = 0 ; i < latLongList.length();++i){
            if(latLongList.charAt(i) == '(')
                continue;
            if(latLongList.charAt(i) == ')'){
                locationListModified.append('|');
                i+=1;
            } else{
                locationListModified.append(latLongList.charAt(i));
            }
        }
        formattedLatLong = locationListModified.toString();
        formattedLatLong = formattedLatLong.substring(0,formattedLatLong.length()-1);
        return formattedLatLong;
    }
    
    private static java.sql.Timestamp getCurrentDate(long curTime) {
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(curTime);
    }
    
     private static java.sql.Timestamp getExpiryDate(long curTime,long delay) {
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(curTime + delay);
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
    private ArrayList<String> getDeviceList() {
		Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        ArrayList<String> devices = new ArrayList<String>();

        /* Check if we can load the MySQL Driver */
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver not found!");
			e.printStackTrace();
		}

		/* Execute a SELECT statement get all devices in database then add them to an ArrayList */
		try {
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM devices");

			/* Add all devices to an ArrayList */
			while (rs.next()) 
				devices.add(rs.getString(1));	// Only add Registration ID's to list
    
			
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(UnregisterDevice.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}
				
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(UnregisterDevice.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		return devices;
	}
        
        public void sendPush(String alertTitle, String text, String location, String msgType) {
             ArrayList<String> devices = getDeviceList();
            sendPushNotification(devices,alertTitle,text,location,msgType);
        }
	
	private void sendPushNotification(ArrayList<String> devices, String alertTitle, String text,String location,String msgType) {
		Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        
        /* Check if we can load the MySQL Driver */
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver not found!");
			e.printStackTrace();
		}

		/* Send Notifications and update our database if some error occurs */
		try {
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM devices");

			/* Send Push Notifications to all devices in list */
			Sender sender = new Sender(API_KEY);
			Message message = new Message.Builder().addData("title", alertTitle).addData("text",text).addData("location",location).addData("msgType",msgType).build();
			MulticastResult result = sender.send(message, devices, 5);
			System.out.println( result.getSuccess()+" notifications successfully sent");
			/*
			for( Result r : result.getResults() ) {
				if (r.getMessageId() != null) {
					String canonicalRegId = r.getCanonicalRegistrationId();
					if (canonicalRegId != null) {
						// same device has more than on registration ID: update database
						rs = st.executeQuery("UPDATE devices SET regID='"+canonicalRegId+"'");
					}
				} else {
					String error = r.getErrorCodeName();
					if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
						// application has been removed from device - unregister database
						rs = st.executeQuery("DELETE FROM devices WHERE regID='"+r.getCanonicalRegistrationId()+"'");
					}
				}
			}
			*/
			
			for (int i = 0; i < result.getTotal(); i++) {
                Result r = result.getResults().get(i);

                if (r.getMessageId() != null) {
                    String canonicalRegId = r.getCanonicalRegistrationId();
                    if (canonicalRegId != null) {
                        // devices.get(i) has more than one registration ID: update database
                    	st.executeUpdate("UPDATE devices SET regID='"+canonicalRegId+"' WHERE regID='"+devices.get(i)+"'");
                    }
                } else {
                    String error = r.getErrorCodeName();
                    if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                        // application has been removed from devices.get(i) - unregister database
                    	st.executeUpdate("DELETE FROM devices WHERE regID='"+devices.get(i)+"'");
                    }
                }
            }
		}
		catch (SQLException ex) {
			Logger lgr = Logger.getLogger(UnregisterDevice.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}
				
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(UnregisterDevice.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}
}
