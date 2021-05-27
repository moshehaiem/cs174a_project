package net.sqlitetutorial;
import java.sql.*;

public class Connect {
    
    private Connection conn;

    Connect() throws SQLException {
        try {
            //db parameters
            String url = "jdbc:sqlite:/Users/moshehaiem/CS174A/Project/Project/cs174a_project/sqlite/db/chinook.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection()
	{
		return conn;
	}
}


