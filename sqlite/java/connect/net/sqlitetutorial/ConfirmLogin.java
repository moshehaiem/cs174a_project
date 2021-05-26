package net.sqlitetutorial;
import java.io.*;
import java.sql.*;

public class ConfirmLogin {
  private Connect myC;

  public ConfirmLogin(String account, String id, String password, Connect con){
    myC = con;

    String correctUserPass = "";
    String correctUserID = "";
    String correctUserAccount = "";
    if (account == "customer") {
      try {
        correctUserPass = pullPassword(password);
        correctUserID = pullID(id);
        correctUserAccount = pullAccount(id);
      }
      catch (InvalidUsernameException iue) {
        System.out.println(iue.getError() + "Quitting Program...");
        System.exit(1);
      }
      catch (SQLException e) {
        System.out.println(e);
        System.out.println("Error getting customer account info. Exiting");
        System.exit(1);
      }
    }
    else if (account == "manager") {
      try {
        correctUserPass = pullPassword(id);
        correctUserID = pullID(id);
        correctUserAccount = pullAccount(id);
      }
      catch (SQLException e) {
        System.out.println("Error getting manager account info. Exiting");
        System.exit(1);
      }
      catch (InvalidUsernameException iue) {
        System.out.println(iue.getError() + "Quitting Program...");
        System.exit(1);
      }

    }

    if (correctUserPass.trim().equals(password) == false) {
        System.out.println("Incorrect password. Quitting Program...");
        System.exit(1);
    }

    if (correctUserID.trim().equals(id) == false) {
        System.out.println("Incorrect account id. Quitting Program...");
        System.exit(1);
    }


    if (correctUserAccount.trim().equals(account) == false) {
        System.out.println("Incorrect Account type. Quitting Program...");
        System.exit(1);
    }

  }

  private String pullPassword(String password) throws InvalidUsernameException, SQLException {
	
    String queryResult = "SELECT * FROM CUSTOMER c WHERE c._password = '" + password + "'";

    Statement stmt = myC.getConnection().createStatement();
    
    ResultSet rs = stmt.executeQuery(queryResult);
    
    String pulledPassword = "";

    while (rs.next())
    {
      pulledPassword = (rs.getString("_password"));
    }

    return pulledPassword;
  }

  private String pullID(String uname) throws InvalidUsernameException, SQLException{


    String queryResult = "SELECT * FROM MARKET_ACCOUNT m WHERE m.unique_id = '" + uname + "'";
        
    Statement stmt = myC.getConnection().createStatement();
        
    ResultSet rs = stmt.executeQuery(queryResult);
        
    String pulledID = "";

    while (rs.next())
    {
      pulledID = (rs.getString("unique_id"));
    }

    return pulledID;
  }

  private String pullAccount(String uname) throws InvalidUsernameException, SQLException {

    String a = "";

    String pulledAccount = "";
    
    String queryResult = "SELECT * FROM CUSTOMER c WHERE c.unique_ID = '" + uname + "'";
    
    Statement stmt = myC.getConnection().createStatement();
	
    ResultSet rs = stmt.executeQuery(queryResult);
    

    
    while (rs.next()){
      a = (rs.getString("isManager"));
    }
      if(a.trim().equals(1) == true){
        pulledAccount = "manager";
      }else{
        pulledAccount = "customer";
      }
    

    return pulledAccount;
  }
}
