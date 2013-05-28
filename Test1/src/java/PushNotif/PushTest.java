package PushNotif;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

@WebServlet("/Push")
public class PushTest extends HttpServlet {
	
	/**
	 * Auto-generated SerialID used for Serialization
	 */
	private static final long serialVersionUID = 4228630363034911862L;
	private static final String API_KEY = "AIzaSyCVZ_FxaGmbvCjbJ_vShvdL3g-86pJIZR0";	// API key is assigned by Google
    private static final String url = "jdbc:mysql://localhost:3306/campus_connect", user = "root", password = "adnan";
    
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String alertTitle = request.getParameter("title");
            String text = request.getParameter("text");
            String location= request.getParameter("location");
            String msgType=request.getParameter("messagetype");
            if( alertTitle == null || text == null ){
                    System.out.println("Malformed push request");
                    return;
            }

            ArrayList<String> devices = getDeviceList();
            sendPushNotification(devices,alertTitle,text,location,msgType);

            /* Redirect user back to push notification page */
                    //response.sendRedirect("http://localhost:8084/Test1/userLogged.jsp");
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