package net.sqlitetutorial;
import java.io.*;
import java.sql.*;
import java.time.YearMonth;
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
    String q3 = "SELECT *, MIN(t._date) FROM TRANSACTIONS t, CUSTOMER c WHERE t.username = c.username AND c.username = '" + username + "' AND strftime('%m', t._date) = strftime('%m', '"+curr_date+"') AND strftime('%Y', t._date) = strftime('%Y', '"+curr_date+"')";
    String q4 = "SELECT *, MAX(t._date) FROM TRANSACTIONS t, CUSTOMER c WHERE t.username = c.username AND c.username = '" + username + "' AND strftime('%m', t._date) = strftime('%m', '"+curr_date+"') AND strftime('%Y', t._date) = strftime('%Y', '"+curr_date+"')";

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
        System.out.println(String.format("%.3f", Double.parseDouble((rs2.getString("shares")))));
  
        System.out.print("Money transacted: \n$");
        System.out.println(String.format("%.2f", Double.parseDouble((rs2.getString("balance")))));
  
        System.out.print("Overall balance: \n$");
        System.out.println(String.format("%.2f", Double.parseDouble((rs2.getString("overall_balance")))));
  
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
        System.out.println(String.format("%.3f", Double.parseDouble((rs3.getString("shares")))));
  
        System.out.print("Money transacted: \n$");
        System.out.println(String.format("%.2f", Double.parseDouble((rs3.getString("balance")))));
  
        System.out.print("Overall balance: \n$");
        System.out.println(String.format("%.2f", Double.parseDouble((rs3.getString("overall_balance")))));
  
        System.out.println("________________________________");
        System.out.println();
      }
    }

    ResultSet rs4 = stmt.executeQuery(q3);
    Double beginning_bal = 0.0;
    while (rs4.next()){
      String beginning_ovBal = rs4.getString("overall_balance");
      String beginning_balance = rs4.getString("balance");
      beginning_bal = Double.parseDouble(beginning_ovBal) - Double.parseDouble(beginning_balance);
    }

    ResultSet rs5 = stmt.executeQuery(q4);
    String ending_bal = "";
    while (rs5.next()){
      ending_bal = rs5.getString("overall_balance");
    }

    double total_change = Double.parseDouble(ending_bal) - beginning_bal;
    System.out.println("\n\n");
    System.out.println("Initial Balance: $" + String.format("%.2f", beginning_bal));
    System.out.println("Final Balance: $" + String.format("%.2f", Double.parseDouble(ending_bal)));
    System.out.println("Total earned: $" + String.format("%.2f", total_change));
    System.out.println("Total commision payed: $" + String.valueOf(count*20));
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
    String queryResult = "SELECT * FROM TRANSACTIONS t, CUSTOMER c WHERE t._date < '"+curr_date+"' AND t._date > DATE('"+curr_date+"','-1 month') AND (t.username = c.username) GROUP BY c._name HAVING SUM(t.balance) >= 10000";
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
    String username="";
    System.out.println("username:");
      
    try {
      username = br.readLine();
    } catch (IOException ioe) {
      System.out.println("Not an option for username");
      System.exit(1);
    }

    String queryResult = "SELECT * FROM ACCOUNT a, CUSTOMER C WHERE a.tax_id = c.tax_id AND c.username = '" + username + "'";
    String queryResult2 = "SELECT * FROM STOCK_ACCOUNT s WHERE s.username = '" + username + "'";

    Statement stmt = myC.getConnection().createStatement();
    ResultSet rs = stmt.executeQuery(queryResult);
    
    System.out.println();
    while (rs.next()){
      String market_active = rs.getString("unique_id");
      System.out.println("Current balance: $" + String.format("%.2f", Double.parseDouble((rs.getString("balance")))));
      
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


    
    String _month = curr_date.substring(5, 7);
    String _year = curr_date.substring(0, 4);
    YearMonth yearMonthObject = YearMonth.of(Integer.parseInt(_year), Integer.parseInt(_month));
    int daysOfMonth = yearMonthObject.lengthOfMonth();
    
    if(Integer.parseInt(curr_date.substring(8)) != daysOfMonth){
      System.out.println("Not the end of the month, cannot accrue interest");
      return;
    }else{
      Statement stmt = myC.getConnection().createStatement();
      String q1 = "SELECT * FROM MARKET_ACCOUNT";
      ResultSet rs1 = stmt.executeQuery(q1);

      ArrayList<String> usernames = new ArrayList<String>( );
      ArrayList<String> uid = new ArrayList<String>( );
      while (rs1.next()) {
        usernames.add(rs1.getString("username"));
        uid.add(rs1.getString("unique_id"));
      }
      
      ResultSet rs;
      for (int i=0; i<usernames.size(); i++) {
        String username = usernames.get(i);
        String un_id = uid.get(i);
        String q2 = "SELECT * FROM TRANSACTIONS t WHERE t.username='" + username +"'";
        rs = myC.getConnection().createStatement().executeQuery(q2);
        double avg_balance = 0;
        int prev_day = 0;
        Double ov_bal = 0.0, bal = 0.0;
        int dayOfTransaction = 0;
        while(rs.next()){
          ov_bal = rs.getDouble("overall_balance");
          bal = rs.getDouble("balance");
          String temp_date = rs.getString("_date");
          System.out.println(temp_date);
          System.out.println(bal);
          System.out.println(ov_bal);
          dayOfTransaction = Integer.parseInt(temp_date.substring(8));

          avg_balance+=(ov_bal-bal)*(dayOfTransaction-prev_day);
          System.out.println(avg_balance);
          prev_day=dayOfTransaction;
        }
        avg_balance+=ov_bal*(daysOfMonth-dayOfTransaction);
        avg_balance/=daysOfMonth;
        
        Double interest = .02 * avg_balance;

        System.out.println(interest);

        String updateRow = "UPDATE ACCOUNT SET balance = balance + '"+interest+"' WHERE unique_id = '" + un_id + "'";
        myC.getConnection().createStatement().executeUpdate(updateRow);



        String insertData = "INSERT INTO TRANSACTIONS(username, _date, trans_type, shares, balance, overall_balance)" + " VALUES('" + username + "','" + curr_date + "','accrue', 0, "+interest+", "+ String.valueOf(ov_bal+interest) +")";
        myC.getConnection().createStatement().executeUpdate(insertData);

      }
    }

    

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
