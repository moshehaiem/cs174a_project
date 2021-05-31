package net.sqlitetutorial;
import java.io.*;
import java.sql.*;

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

    String queryResult = "SELECT * FROM TRANSACTIONS t, CUSTOMER c WHERE t.username = c.username AND EXTRACT(MONTH FROM t.date) = EXTRACT(MONTH FROM "+curr_date+")";

    String queryResult2 = "SELECT * FROM TRANSACTIONS t, CUSTOMER c WHERE t.username = c.username AND EXTRACT(MONTH FROM t.date) = EXTRACT(MONTH FROM "+curr_date+") AND EXTRACT(DAY FROM t.date) = (SELECT * FROM TRANSACTIONS t WHERE t.date)";

    String queryResult3 = "SELECT * FROM TRANSACTIONS t, CUSTOMER c WHERE t.username = c.username AND EXTRACT(MONTH FROM t.date) = EXTRACT(MONTH FROM "+curr_date+") AND EXTRACT(DAY FROM t.date) = (SELECT * FROM TRANSACTIONS t WHERE t.date)";

    String queryResult4 = "SELECT * FROM TRANSACTIONS t, CUSTOMER c WHERE t.username = c.username AND EXTRACT(MONTH FROM t.date) = EXTRACT(MONTH FROM "+curr_date+") AND (t.trans_type = " + "'" + "buy" + "'" + "OR t.trans_type = "+ "'" + "sell"+ "'" + ")";
    
    Statement stmt = myC.getConnection().createStatement();
    ResultSet rs = stmt.executeQuery(queryResult);
    ResultSet rs2 = stmt.executeQuery(queryResult2);
    ResultSet rs3 = stmt.executeQuery(queryResult3);
    ResultSet rs4 = stmt.executeQuery(queryResult4);
        
    while (rs.next()){
      System.out.println("Name: "+ rs.getString("_name"));
      System.out.println("Email: "+ rs.getString("email_address"));

      String initb = rs2.getString("overall_balance");
      String finalb = rs3.getString("overall_balance");

      System.out.println("Initial balance: $"+ initb);
      System.out.println("Final balance: $"+ finalb);

      double initB = Double.parseDouble(initb);
      double finalB = Double.parseDouble(finalb);
      finalB -= initB;

      System.out.println("Total earnings: $"+ String.valueOf(finalB));


      int count = 0;
      while(rs4.next()){
        count++;
      }
      System.out.println("Commision payed: $"+ String.valueOf(count*20));
      
    }

  }



  public void listActiveCustomers() throws SQLException{
    String queryResult = "SELECT * FROM TRANSACTIONS t, CUSTOMER c WHERE t.username = c.username AND (t.trans_type = 'buy' OR t.trans_type = 'sell') AND SUM(t.shares) >= 1000 AND EXTRACT(MONTH FROM t.date) = EXTRACT(MONTH FROM "+curr_date+") AND EXTRACT(YEAR FROM t.date) = EXTRACT(YEAR FROM "+curr_date+")";

    Statement stmt = myC.getConnection().createStatement();
    ResultSet rs = stmt.executeQuery(queryResult);

    int count = 1;
    while (rs.next()){
      System.out.println(count + ") "+ rs.getString("_name"));
      count++;
    }
  }



  public void generateDTER() throws SQLException{
    String queryResult = "SELECT * FROM TRANSACTIONS t, CUSTOMER c WHERE t.username = c.username AND SUM(t.balance) >= 10000 AND EXTRACT(MONTH FROM t.date) = EXTRACT(MONTH FROM "+curr_date+") AND EXTRACT(YEAR FROM t.date) = EXTRACT(YEAR FROM "+curr_date+")";

    Statement stmt = myC.getConnection().createStatement();
    ResultSet rs = stmt.executeQuery(queryResult);


    while (rs.next()){
      System.out.println("Name: "+ rs.getString("_name"));
      System.out.println("State: "+ rs.getString("_state"));
    }
  }



  public void getCusomterReport() throws SQLException{
    String cust_id="";
    System.out.println("customer id:");
      
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
    ResultSet rs2 = stmt.executeQuery(queryResult2);


    while (rs.next()){
      String market_active = rs.getString("unique_id");
      System.out.println("current balance: $" + rs.getString("balance"));

      if(market_active.trim().equals("")){
        System.out.println("User does not have active market account");
      }else{
        System.out.println("User has active market account");
      }

    }

    while (rs2.next()){
      String stock_active = rs2.getString("unique_id");
      if(stock_active.trim().equals("")){
        System.out.println("User does not have active stock account");
      }else{  
        System.out.println("User has active stock account");
      }

    }



  }



  public void deleteTransactions() throws SQLException{
    String queryResult = "DELETE FROM TRANSACTIONS";

    Statement stmt = myC.getConnection().createStatement();
    stmt.executeQuery(queryResult);

  }


  public void addInterest() throws SQLException{

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
