package PushNotif;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * Simple servlet for unregistering devices from  push notifications 
 * */

@WebServlet("/Unregister")
public class UnregisterDevice extends HttpServlet {
	
	/**
	 * Auto-generated SerialID used for Serialization
	 */
	private static final long serialVersionUID = -4277772430114541913L;
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String regID = request.getParameter("regID");
        if( regID == null ){
        	System.out.println("Malformed unregister request");
        	return;
        }
        unregisterDevice(regID);
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return;	// We are currently using get
	}
	
	private void unregisterDevice(String regID) {
		Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String url = "jdbc:mysql://localhost:3306/campus_connect", user = "root", password = "adnan";
        
        /* Check if we can load the MySQL Driver */
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver not found!");
			e.printStackTrace();
		}
        
		/* Execute a DELETE statement to remove regID from our database */
        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            st.executeUpdate("DELETE FROM devices WHERE regID='"+regID+"'");
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
	}
}
