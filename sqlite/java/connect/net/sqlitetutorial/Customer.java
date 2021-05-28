package net.sqlitetutorial;
import java.io.*;
import java.sql.*;

public class Customer {
  private String customerID;
  private Connect myC;
  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

  public void buyStock() throws SQLException{
    String stock_type="";
    System.out.println("What stock would you like to buy (SKB, SMD, STC)");
      
    try {
      stock_type = br.readLine();
    } catch (IOException ioe) {
      System.out.println("Not an option for stock");
      System.exit(1);
    }

    String amount="";
    System.out.println("How many shares");
      
    try {
      amount = br.readLine();
    } catch (IOException ioe) {
      System.out.println("Not an option for amount");
      System.exit(1);
    }


    String queryResult = "SELECT * FROM MOVIE_CONTRACT m WHERE m.symbol= '" + stock_type + "'";
        
    Statement stmt = myC.getConnection().createStatement();
        
    ResultSet rs = stmt.executeQuery(queryResult);
        
    String priceforstock = "";

    while (rs.next()){
      priceforstock = (rs.getString("curr_price"));
    }

    int p=Integer.parseInt(priceforstock);

    int i=Integer.parseInt(amount);
    i *= p;
    amount = String.valueOf(i);


		String updateRow = "UPDATE ACCOUNT a set a.balance = a.balance - " + amount + " WHERE a.unique_id = '" + customerID + "'";
		stmt.executeUpdate(updateRow);


    // queryResult = "SELECT * FROM MOVIE_CONTRACT m WHERE m.symbol= '" + stock_type + "'";
        
    // Statement stmt = myC.getConnection().createStatement();
        
    // ResultSet rs = stmt.executeQuery(queryResult);
        
    // String priceforstock = "";

    // while (rs.next()){
    //   priceforstock = (rs.getString("curr_price"));
    // }



    // updateRow = "UPDATE ACCOUNT a set a.balance = a.balance - " + amount + " WHERE a.unique_id = '" + customerID + "'";
		// rs = st.executeQuery(updateRow);


  }

  public void sellStock(){
    
  }

  public void depositMoney(){

    String moneyAmount="";
    System.out.println("How much money to deposit?");
      
    try {
      moneyAmount = br.readLine();
    } catch (IOException ioe) {
      System.out.println("Not an option for depositing");
      System.exit(1);
    }

    String updateRow = "UPDATE ACCOUNT a set a.balance = a.balance + " + moneyAmount + " WHERE a.unique_id = '" + customerID + "'";
		stmt.executeQuery(updateRow);

  }

  public void withdrawMoney(){
    String moneyAmount="";
    System.out.println("How much money to withdraw?");
      
    try {
      moneyAmount = br.readLine();
    } catch (IOException ioe) {
      System.out.println("Not an option for withdrawal");
      System.exit(1);
    }

    String queryResult = "SELECT * FROM ACCOUNT a WHERE a.unique_id= '" + customerID + "'";
        
    Statement stmt = myC.getConnection().createStatement();
        
    ResultSet rs = stmt.executeQuery(queryResult);
        
    String currBalance = "";

    while (rs.next()){
      currBalance = (rs.getString("balance"));
    }

    int p=Integer.parseInt(currBalance);
    int i=Integer.parseInt(moneyAmount);


    if(i<p){
      System.out.println("You don't have enough in your balance");
      System.exit(1);
    }
    moneyAmount = String.valueOf(i);

    String updateRow = "UPDATE ACCOUNT a set a.balance = a.balance - " + moneyAmount + " WHERE a.unique_id = '" + customerID + "'";
		stmt.executeUpdate(updateRow);

  }



  public void showBalance(){
    String queryResult = "SELECT * FROM ACCOUNT a WHERE a.unique_id= '" + customerID + "'";
        
    Statement stmt = myC.getConnection().createStatement();
        
    ResultSet rs = stmt.executeQuery(queryResult);
        
    String currBalance = "";

    while (rs.next()){
      currBalance = (rs.getString("balance"));
    }
    System.out.println("Current Balance:");
    System.out.println("$" + currBalance);

  }

  public void showTransactions(){

  }

  public void listCurrentPrice(){

  }

  public void listMovieInformation(){

  }

    
    
    
    
  public Customer(String username, Connect conn, String uniqueID){
    myC = conn;
    System.out.println("Logged in");
    customerID = uniqueID;
    
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
        case "buy stock":
        try {
          buyStock();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in buying stock. Exiting");
          System.exit(1);
        }
        break;
        case "sell stock":
        sellStock();
        break;
        case "deposit money":
        depositMoney();
        break;
        case "withdraw money":
        withdrawMoney();
        break;
        case "show balance":
        showBalance();
        break;
        case "show transactions":
        showTransactions();
        break;
        case "show price":
        listCurrentPrice();
        break;
        case "movie information":
        listMovieInformation();
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

