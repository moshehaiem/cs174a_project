package net.sqlitetutorial;
import java.sql.*;

public class Connect {
    
    private Connection conn;

    Connect() throws SQLException {
        try {
            //db parameters
            // DriverManager.registerDriver(new oracle.jdbc.OracleDriver());	
            String url = "jdbc:sqlite:/Users/patrickkuang/cs174a_project/sqlite/db/chinook.db";
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

    public Connection getConnection()
	{
		return conn;
	}
}


