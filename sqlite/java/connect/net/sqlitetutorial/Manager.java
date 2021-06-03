package net.sqlitetutorial;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class Manager {
  private String managerID;
  private String curr_date;
  private Connect myC;
  private String username;
  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

  public void generateMonthlyStatement() throws SQLException{
    //find transactions with the same month as current month
    //in those transactions, list name and email of customer
    //find the latest date of the month and earliest date of month, and in those dates, find the balance associated
    //total commision payed = amount of transactions that are buy and sell * 20 

    String username = "";
    System.out.println("customer username:");
      
    try {
      username = br.readLine();
    } catch (IOException ioe) {
      System.out.println("Not an option for id");
      System.exit(1);
    }

    String q1 = "SELECT * FROM CUSTOMER c WHERE c.username = '" + username + "'";
    String q2 = "SELECT * FROM TRANSACTIONS t, CUSTOMER c WHERE t.username = c.username AND c.username = '" + username + "' AND strftime('%m', t._date) = strftime('%m', '"+curr_date+"') AND strftime('%Y', t._date) = strftime('%Y', '"+curr_date+"')";
    String q3 = "SELECT *, MIN(t._date) FROM TRANSACTIONS t WHERE strftime('%m', t._date) = strftime('%m', '"+curr_date+"') AND strftime('%Y', t._date) = strftime('%Y', '"+curr_date+"')";
    String q4 = "SELECT *, MAX(t._date) FROM TRANSACTIONS t WHERE strftime('%m', t._date) = strftime('%m', '"+curr_date+"') AND strftime('%Y', t._date) = strftime('%Y', '"+curr_date+"')";

    Statement stmt = myC.getConnection().createStatement();
    ResultSet rs = stmt.executeQuery(q1);

    String name = "";
    String email = "";

    while (rs.next()){
      name = rs.getString("_name");
      email = rs.getString("email_address");
    }

    System.out.println("\nName: " + name + "\nEmail: " + email + "\n\nMarket Account Statement:\n");

    ResultSet rs2 = stmt.executeQuery(q2);

    while (rs2.next()){
      if (!rs2.getString("trans_type").contains("buy") && !rs2.getString("trans_type").contains("sell")) {
        System.out.println("Date of transaction: ");
        System.out.println((rs2.getString("_date")));
  
        System.out.println("Transaction type: ");
        System.out.println((rs2.getString("trans_type")));
  
        System.out.println("Overall shares bought and sold: ");
        System.out.println((rs2.getString("shares")));
  
        System.out.print("Money transacted: \n$");
        System.out.println((rs2.getString("balance")));
  
        System.out.print("Overall balance: \n$");
        System.out.println((rs2.getString("overall_balance")));
  
        System.out.println("________________________________");
        System.out.println();
      }
    }

    ResultSet rs3 = stmt.executeQuery(q2);
    System.out.println("\n\n");
    System.out.println("Stock Account Statement: \n");
    int count = 0;
    while (rs3.next()){
      if (rs2.getString("trans_type").contains("buy") || rs2.getString("trans_type").contains("sell")) {
        count++;
        System.out.println("Date of transaction: ");
        System.out.println((rs3.getString("_date")));
  
        System.out.println("Transaction type: ");
        System.out.println((rs3.getString("trans_type")));
  
        System.out.println("Overall shares bought and sold: ");
        System.out.println((rs3.getString("shares")));
  
        System.out.print("Money transacted: \n$");
        System.out.println((rs3.getString("balance")));
  
        System.out.print("Overall balance: \n$");
        System.out.println((rs3.getString("overall_balance")));
  
        System.out.println("________________________________");
        System.out.println();
      }
    }

    ResultSet rs4 = stmt.executeQuery(q3);
    String beginning_bal = "";
    while (rs4.next()){
      beginning_bal = rs4.getString("overall_balance");
    }

    ResultSet rs5 = stmt.executeQuery(q4);
    String ending_bal = "";
    while (rs5.next()){
      ending_bal = rs5.getString("overall_balance");
    }

    double total_change = Double.parseDouble(ending_bal) - Double.parseDouble(beginning_bal);
    System.out.println("\n\n");
    System.out.println("Initial Balance: $" + beginning_bal);
    System.out.println("Final Balance: $" + ending_bal);
    System.out.println("Total earned: $" + total_change);
    System.out.println("Total commision payed: $" + String.valueOf(count * 20));
    System.out.println();

  }



  public void listActiveCustomers() throws SQLException{
    String queryResult = "SELECT * FROM TRANSACTIONS t, CUSTOMER c  WHERE (strftime('%m', t._date) = strftime('%m', '"+curr_date+"')) AND (strftime('%Y', t._date) = strftime('%Y', '"+curr_date+"')) AND (t.trans_type = 'buy' OR t.trans_type = 'sell') AND c.username = t.username GROUP BY c._name HAVING SUM(t.shares) >= 1000;";

    Statement stmt = myC.getConnection().createStatement();
    ResultSet rs = stmt.executeQuery(queryResult);
    System.out.println("\n\n");
    int count = 1;
    while (rs.next()){
      System.out.println(count + ") "+ rs.getString("_name"));
      count++;
    }
  }



  public void generateDTER() throws SQLException{
    String queryResult = "SELECT * FROM TRANSACTIONS t, CUSTOMER c WHERE t.date < '"+curr_date+"' AND t.date > DATE('"+curr_date+"','-1 month') AND (t.username = c.username) GROUP BY c._name HAVING SUM(t.balance) >= 10000";
    // String queryResult = "SELECT * FROM TRANSACTIONS t, CUSTOMER c WHERE (strftime('%m', t._date) = strftime('%m', '"+curr_date+"')) AND (strftime('%Y', t._date) = strftime('%Y', '"+curr_date+"')) AND (t.username = c.username) GROUP BY c._name HAVING SUM(t.balance) >= 10000";
    Statement stmt = myC.getConnection().createStatement();
    ResultSet rs = stmt.executeQuery(queryResult);

    System.out.println("\n\n");
    while (rs.next()){
      System.out.println("Name: "+ rs.getString("_name"));
      System.out.println("State: "+ rs.getString("_state"));
      System.out.println();
      System.out.println("______________");
    }
    System.out.println();
  }



  public void getCusomterReport() throws SQLException{
    String cust_id="";
    System.out.println("Customer id:");
      
    try {
      cust_id = br.readLine();
    } catch (IOException ioe) {
      System.out.println("Not an option for id");
      System.exit(1);
    }

    String queryResult = "SELECT * FROM ACCOUNT a WHERE a.unique_id = '"+cust_id+"'";
    String queryResult2 = "SELECT * FROM STOCK_ACCOUNT s WHERE s.unique_id = '"+cust_id+"'";

    Statement stmt = myC.getConnection().createStatement();
    ResultSet rs = stmt.executeQuery(queryResult);
    
    System.out.println();
    while (rs.next()){
      String market_active = rs.getString("unique_id");
      System.out.println("Current balance: $" + rs.getString("balance"));
      
      if(market_active.trim().equals("")){
        System.out.println("User does not have active market account");
      }else{
        System.out.println("User has active market account");
      }
      
    }
    System.out.println();
    
    ResultSet rs2 = stmt.executeQuery(queryResult2);
    while (rs2.next()){
      String stock_active = rs2.getString("unique_id");
      if(stock_active.trim().equals("")){
        System.out.println("User does not have active stock account");
      }else{  
        System.out.println("User has active stock account for: " + rs2.getString("symbol"));
      }
      
    }
    
    System.out.println();


  }



  public void deleteTransactions() throws SQLException{
    String queryResult = "DELETE FROM TRANSACTIONS";

    Statement stmt = myC.getConnection().createStatement();
    stmt.executeUpdate(queryResult);
    System.out.println("Transactions deleted!");
    System.out.println();

  }


  public void addInterest() throws SQLException{
    String queryResult = "SELECT * FROM TRANSACTIONS t, CUSTOMER c WHERE t.username=c.username AND strftime('%m', t._date) = strftime('%m', '"+curr_date+"') AND strftime('%Y', t._date) = strftime('%Y', '"+curr_date+"')";
    Statement stmt = myC.getConnection().createStatement();
    ResultSet rs = stmt.executeQuery(queryResult);
    while(rs.next()){

    }
    
    //for each customer, find days of transactions in the current month and year (for ex, 2021-05-01, 2021-05-09, 2021-05-22). 
    //Then find  balance from 01, 09, and 22. So this means that 01-09 is balance from 01, 09-22 is balance from 09, and 22-EOM is balance from 22
    //Then add to their current balance  (((9-1)*balance from 01 + (22-9)*balance from 09 + (EOM-22)*balance from 22) / DOM ) * .02




    System.out.println("Interest Added!");
    
  }
    
    
    
  public Manager(String _username, Connect conn, String uniqueID, String c_date){
    myC = conn;
    System.out.println("Logged in");
    managerID = uniqueID;
    curr_date = c_date;
    username = _username;
    
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String command = null;
    
    boolean continueAccess = true;
    
    while (continueAccess == true){
      System.out.println();
      System.out.println("Next action:");
      try{
        command = br.readLine();
      }
      catch (IOException ioe){
        System.out.println("Not a valid action");
      }
      
      switch( command ){
        case "interest":
        try {
          addInterest();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in adding interest. Exiting");
          System.exit(1);
        }
        break;
        case "statement":
        try {
          generateMonthlyStatement();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in generating monthly statement. Exiting");
          System.exit(1);
        }
        break;
        case "customers":
        try {
          listActiveCustomers();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in listing active customers. Exiting");
          System.exit(1);
        }
        break;
        case "DTER":
        try {
          generateDTER();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in generating DTER. Exiting");
          System.exit(1);
        }
        break;
        case "report":
        try {
          getCusomterReport();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in getting customer report. Exiting");
          System.exit(1);
        }
        break;
        case "delete":
        try {
          deleteTransactions();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in deleting transactions. Exiting");
          System.exit(1);
        }
        break;
        case "quit":
        continueAccess = false;
        break;
        default:
        System.out.println("Not a valid action");
      }
    }
	
  }
}
