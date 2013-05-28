package PushNotif;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

/** Simple servlet for testing. Generates HTML instead of plain
 *  text as with the HelloWorld servlet.
 */

@WebServlet("/Register")
public class RegisterDevice extends HttpServlet {
	
	/**
	 * Auto-generated SerialID used for Serialization
	 */
	private static final long serialVersionUID = 229702844546070609L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {        
        String regID = request.getParameter("regID");
        if( regID == null){
        	System.out.println("Malformed register request:\nRegID: "+ regID);
        	return;
        }
        registerDevice(regID);
  	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return;	// We are currently using POST
	}
	
	private void registerDevice(String regID){
		String url = "jdbc:mysql://localhost:3306/campus_connect", user = "root", password = "adnan";
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
        
        /* Execute an INSERT statement to add regID and email into our database */
        try {
        	con = DriverManager.getConnection(url, user, password);
        	st = con.createStatement();
        	st.executeUpdate("INSERT INTO devices (regID) VALUES ('"+regID+"')");
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
