package net.sqlitetutorial;
import java.sql.*;

/**
 *
 * @author sqlitetutorial.net
 */
public class Connect {
     /**
     * Connect to a sample database
     */
    public static void connect(){
        Connection conn = null;
        try {
            //db parameters
            // DriverManager.registerDriver(new oracle.jdbc.OracleDriver());	
            String url = "jdbc:sqlite:/Users/moshehaiem/Documents/CS174A/Project/Project/sqlite/db/chinook.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        connect();
    }
}


