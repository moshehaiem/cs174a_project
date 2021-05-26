package net.sqlitetutorial;
import java.io.*;
import java.sql.*;

public class Login {
  public static void main(String [] args) throws SQLException{
    Connect con = new Connect();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String userType = "";
    String userID = "";
    String userPwd = "";
    
    ConfirmLogin L;

    System.out.println("Are you a manager or customer?");
    
    try {
      userType = br.readLine(); 
    } catch (IOException ioe) {
      System.out.println("Not an option for account type");
      System.exit(1);
    }

    System.out.println("Enter account id:");

    try {
      userID = br.readLine();
    } catch (IOException ioe) {
      System.out.println("Not an option for account id");
      System.exit(1);
    }

    System.out.println("Enter Password:");

    try {
      userPwd = br.readLine();
    } catch (IOException ioe) {
      System.out.println("Not an option for account password");
      System.exit(1);
    }

    boolean loggedIn = false;

    while (loggedIn == false){

      if (userType.equals("customer") == true){
			  L = new ConfirmLogin("customer", userID, userPwd, con);
			  Customer C = new Customer(userID, con);
			  loggedIn = true;
			}
		  else if (userType.equals("manager") == true){	
			  L = new ConfirmLogin("manager", userID, userPwd, con);
			  Manager M = new Manager(userID, con);
			  loggedIn = true;
			}
		    else{
			    System.out.println("Invalid input");
			    break;
			}

    }
  }  
}
