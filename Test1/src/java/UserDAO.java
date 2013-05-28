

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import beans.UserBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Adnan
 */
public class UserDAO 
{ 
    static Connection currentCon = null;
    static ResultSet rs = null; 
    public static UserBean login(UserBean bean) { //preparing some objects for connection 
        Statement stmt = null; 
        String username = bean.getUsername(); 
        String password = bean.getPassword(); 
        String searchQuery = "select * from users where username='" + username + "' AND password='" + password + "'"; // "System.out.println" prints in the console; Normally used to trace the process 
        System.out.println("Your user name is " + username); 
        System.out.println("Your password is " + password); 
        System.out.println("Query: "+searchQuery); 
        try { //connect to DB 
            currentCon = getMySqlConnection(); 
            
            
            PreparedStatement pstmt = currentCon.prepareStatement("select * from users where username= ? "
                    + "AND pswd = ?");
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
             rs = pstmt.executeQuery();
            
            
            
            
            
            //stmt=currentCon.createStatement(); 
            //rs = stmt.executeQuery(searchQuery); 
            boolean more = rs.next(); // if user does not exist set the isValid variable to false 
            
            if (!more) { 
                System.out.println("Sorry, you are not a registered user! Please sign up first");
                bean.setValid(false); } //if user exists set the isValid variable to true 
            else if (more) {
                String userLogin = rs.getString("username");
                //String pswd = rs.getString("LastName"); 
                System.out.println("Welcome " + userLogin); 
                bean.setFirstName(userLogin); 
                bean.setLastName(userLogin); 
                bean.setValid(true); 
            }
        } catch (Exception ex) { 
            System.out.println("Log In failed: An Exception has occurred! " + ex);
        } //some exception handling 
        finally {
            if (rs != null) 
            { try { 
                rs.close(); 
               } catch (Exception e) {} 
               rs = null; 
            } 
            
            if (stmt != null) 
            { try { 
                stmt.close(); 
              } catch (Exception e) {} 
              stmt = null; 
            } 
            
            if (currentCon != null) 
            { try { 
                currentCon.close(); 
              } catch (Exception e) { } 
              currentCon = null; 
            } 
        } 
        return bean; 
    } 
    
    private static Connection getMySqlConnection() {
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