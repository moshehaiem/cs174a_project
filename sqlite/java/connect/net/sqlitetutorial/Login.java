package net.sqlitetutorial;
import java.io.*;
import java.sql.*;

public class Login {
  public static void main(String [] args) throws SQLException{
    Connect con = new Connect();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String login_create = "";
    String userType = "";
    String userID = "";
    String userPwd = "";
    String date_entered = "";
    String SKB = "";
    String SMD = "";
    String STC = "";
    
    ConfirmLogin L;

    System.out.println("Set today's date (format: MM/DD/YYYY");
    try {
      date_entered = br.readLine(); 
    } catch (IOException ioe) {
      System.out.println("Not an available date");
      System.exit(1);
    }


    
    String updateRow = "UPDATE CURRENT_DATE d set d._date = '" + date_entered + "'";

    Statement stmt = con.getConnection().createStatement();
    stmt.executeUpdate(updateRow);



    System.out.println("Set SKB price: ");
    try {
      SKB = br.readLine(); 
    } catch (IOException ioe) {
      System.out.println("Not an available price");
      System.exit(1);
    }
    
    updateRow = "UPDATE MOVIE_CONTRACT m set m.curr_price = '" + SKB + "'";

    stmt = con.getConnection().createStatement();
    stmt.executeUpdate(updateRow);



    System.out.println("Set SMD price: ");
    try {
      SMD = br.readLine(); 
    } catch (IOException ioe) {
      System.out.println("Not an available price");
      System.exit(1);
    }
    
    updateRow = "UPDATE MOVIE_CONTRACT m set m.curr_price = '" + SMD + "'";

    stmt = con.getConnection().createStatement();
    stmt.executeUpdate(updateRow);


    System.out.println("Set STC price: ");
    try {
      STC = br.readLine(); 
    } catch (IOException ioe) {
      System.out.println("Not an available price");
      System.exit(1);
    }
    
    updateRow = "UPDATE MOVIE_CONTRACT m set m.curr_price = '" + STC + "'";

    stmt = con.getConnection().createStatement();
    stmt.executeUpdate(updateRow);




    
    System.out.println("Are you logging in or creating an account? (Y = login, N = create account)");
    try {
      login_create = br.readLine(); 
    } catch (IOException ioe) {
      System.out.println("Not an available action");
      System.exit(1);
    }
    
    // login
    if (login_create.trim().equals("Y")) {
      System.out.println("Are you a manager or customer?");
      
      try {
        userType = br.readLine(); 
      } catch (IOException ioe) {
        System.out.println("Not an option for account type");
        System.exit(1);
      }
      
      System.out.println("Enter account username:");
      
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
          try {
            Customer C = new Customer(userID, con, getUniqueId(userID, con), getDate(con), userID);
          } catch (SQLException e) {
            System.out.println("Unique Id unable to be found");
            System.exit(1);
          }
          loggedIn = true;
        }
        else if (userType.equals("manager") == true){	
          L = new ConfirmLogin("manager", userID, userPwd, con);
          // Manager M = new Manager(userID, con);
          loggedIn = true;
        }
        else{
          System.out.println("Invalid input");
          break;
        }
      }
    }
    
    // create account
    String name = "";
    String username = "";
    String password = "";
    String address = "";
    String state = "";
    String phone = "";
    String email = "";
    String taxId = "";
    String ssn = "";
    String isManager = "";
    String unique_id = "";
    if (login_create.trim().equals("N")) {

      System.out.println("What is your name?");
      try {
        name = br.readLine(); 
      } catch (IOException ioe) {
        System.out.println("Not an option for name");
        System.exit(1);
      }

      boolean tryAgain = true;
      while (tryAgain) {
        System.out.println("What is your username");
        try {
          username = br.readLine(); 
          verifyUsername(username, con);
          tryAgain = false;
        } catch (IOException ioe) {
          System.out.println("Not an option for username");
          System.exit(1);
        } catch (InvalidUsernameException e) {
          System.out.println(e.getError());
        }
      }

      System.out.println("Choose a password");
      try {
        password = br.readLine(); 
      } catch (IOException ioe) {
        System.out.println("Not an option for password");
        System.exit(1);
      }

      System.out.println("What is your address?");
      try {
        address = br.readLine(); 
      } catch (IOException ioe) {
        System.out.println("Not an option for address");
        System.exit(1);
      }

      System.out.println("What state do you live in?");
      try {
        state = br.readLine(); 
      } catch (IOException ioe) {
        System.out.println("Not an option for state");
        System.exit(1);
      }

      System.out.println("What is your phone number (10 digit)?");
      try {
        phone = br.readLine(); 
      } catch (IOException ioe) {
        System.out.println("Not an option for phone number");
        System.exit(1);
      }

      System.out.println("What is your email address?");
      try {
        email = br.readLine(); 
      } catch (IOException ioe) {
        System.out.println("Not an option for email");
        System.exit(1);
      }

      System.out.println("What is your tax id?");
      try {
        taxId = br.readLine(); 
      } catch (IOException ioe) {
        System.out.println("Not an option for tax id");
        System.exit(1);
      }

      System.out.println("What is your social security number (9 digits not seperated by hyphen)?");
      try {
        ssn = br.readLine(); 
      } catch (IOException ioe) {
        System.out.println("Not an option for ssn");
        System.exit(1);
      }

      System.out.println("Are you a manager or customer (1 = manager, 0 = customer)?");
      try {
        isManager = br.readLine(); 
      } catch (IOException ioe) {
        System.out.println("Not an option");
        System.exit(1);
      }

      

      String insertData = "INSERT INTO CUSTOMER(_name, username, _password, _address, _state, phone_number, email_address, tax_id, SSN, isManager)" + 
							 " VALUES('" + name + "','" + username + "','" + password + "','" + address + "','" + state + "','" + phone + "','" + email + "','" + taxId + "','" + ssn + "','" + isManager + "')";
      
      Statement st = con.getConnection().createStatement();
            
			try {
				st.executeUpdate(insertData);
        System.out.println("Account succesfully created");
			} catch (Exception e) {
				System.out.println(e);
			}
      



      String queryResult = "SELECT * FROM ACCOUNT a WHERE a.unique_id = (SELECT max(unique_id) FROM ACCOUNT)";
        
      Statement stmt = con.getConnection().createStatement();
        
      ResultSet rs = stmt.executeQuery(queryResult);
        
      String newID = "";

      while (rs.next()){
      newID = (rs.getString("unique_id"));
      }
      int i=Integer.parseInt(newID);
      i += 1;
      newID = String.valueOf(i);
      


      insertData = "INSERT INTO ACCOUNT(unique_id, balance, tax_id)" + 
							 " VALUES('" + newID + "','1000','" + taxId + "')";
      
      st = con.getConnection().createStatement();
            
			try {
				st.executeUpdate(insertData);
        System.out.println("Market account succesfully created. 1000$ automatically deposited.");
			} catch (Exception e) {
				System.out.println(e);
			}



    }
  }  

  private static void verifyUsername(String uname, Connect con) throws InvalidUsernameException, SQLException{


    String queryResult = "SELECT * FROM CUSTOMER c WHERE c.username = '" + uname + "'";
        
    Statement stmt = con.getConnection().createStatement();
        
    ResultSet rs = stmt.executeQuery(queryResult);
        
    String pulledID = "";

    while (rs.next())
    {
      pulledID = (rs.getString("username"));
    }

    if (pulledID.trim().equals(uname)) throw new InvalidUsernameException("Username already taken");
  }



  private static String getUniqueId(String uname, Connect con) throws SQLException{


    String queryResult = "SELECT * FROM ACCOUNT a, CUSTOMER c WHERE c.tax_id = a.tax_id AND c.username = '" + uname + "'";
        
    Statement stmt = con.getConnection().createStatement();
        
    ResultSet rs = stmt.executeQuery(queryResult);
        
    String pulledUI = "";

    while (rs.next())
    {
      pulledUI = (rs.getString("unique_id"));
    }

    System.out.println(pulledUI);

    return pulledUI;
  }



  private static String getDate(Connect con) throws SQLException{


    String queryResult = "SELECT * FROM CURRENT_DATE";
        
    Statement stmt = con.getConnection().createStatement();
        
    ResultSet rs = stmt.executeQuery(queryResult);
        
    String pulledDate = "";

    while (rs.next())
    {
      pulledDate = (rs.getString("_date"));
    }


    return pulledDate;
  }

}
