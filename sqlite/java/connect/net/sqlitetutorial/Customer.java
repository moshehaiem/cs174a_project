package net.sqlitetutorial;
import java.io.*;
import java.sql.*;

public class Customer {
  private String customerID;
  private String curr_date;
  private Connect myC;
  private String username;
  
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

    double price = Double.parseDouble(priceforstock);

    int shares =Integer.parseInt(amount);

    double total = price * shares;

    //confirm balance is high enough
    queryResult = "SELECT * FROM ACCOUNT a WHERE a.unique_id= '" + customerID + "'";
        
    rs = stmt.executeQuery(queryResult);
        
    String currBalance = "";

    while (rs.next()){
      currBalance = (rs.getString("balance"));
    }

    double adujusted_balance = Double.parseDouble(currBalance) - 20;


    if(total > adujusted_balance){
      System.out.println("You don't have enough in your balance");
      System.exit(1);
    }


		String updateRow = "UPDATE ACCOUNT SET balance = balance - 20 - " + total + " WHERE unique_id = '" + customerID + "'";
		stmt.executeUpdate(updateRow);

    adujusted_balance -= total;

    double balance = -(total+20);

    //add to transaction table
    String insertData = "INSERT INTO TRANSACTIONS(username, _date, trans_type, shares, balance, overall_balance)" + " VALUES('" + username + "','" + curr_date + "','buy','" + String.valueOf(shares) + "'," + String.valueOf(balance) + ", "+ String.valueOf(adujusted_balance) +")";
		stmt.executeUpdate(insertData);



    //add stock to account

    //check if user_id is under stock info
    //if it isn't, insert the new data

    //if it is, then check if the bought stock id exists under that tax_id
    //if it does, then update the amount of stock 
    //if it doesn't, then insert the new data

    queryResult = "SELECT * FROM STOCK_ACCOUNT s WHERE s.unique_id= '" + customerID + "'";
        
    rs = stmt.executeQuery(queryResult);
        
    String user = "";

    while (rs.next()){
      user = (rs.getString("unique_id"));
    }


    if(user.trim().equals("")){
      insertData = "INSERT INTO STOCK_ACCOUNT(username, unique_id, shares, symbol)" + " VALUES('" + username + "','"+customerID +"'," + String.valueOf(shares) + ",'" +stock_type +"')";
		  stmt.executeUpdate(insertData);
      System.exit(1);
    }


    queryResult = "SELECT * FROM STOCK_ACCOUNT s WHERE s.unique_id= '" + customerID + "' AND s.symbol = '"+ stock_type +"'";
        
    rs = stmt.executeQuery(queryResult);
        
    String sym_exists = "";

    while (rs.next()){
      sym_exists = (rs.getString("unique_id"));
    }

    if(sym_exists.trim().equals("")){
      insertData = "INSERT INTO STOCK_ACCOUNT(username, unique_id, shares, symbol)" + " VALUES('" + username + "','"+customerID +"'," + String.valueOf(shares) + ",'" +stock_type +"')";
		  stmt.executeUpdate(insertData);
      System.exit(1);
    }


    updateRow = "UPDATE STOCK_ACCOUNT set shares = shares + " + String.valueOf(shares) + " WHERE unique_id = '" + customerID + "'";
		stmt.executeUpdate(updateRow);

    System.out.println("Buy stock transaction completed");
  }


  public void sellStock() throws SQLException{
    //add to transaction
    String stock_type="";
    System.out.println("What stock would you like to sell (SKB, SMD, STC)");
      
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

    double price = Double.parseDouble(priceforstock);

    int shares =Integer.parseInt(amount);

    double total = price * shares;



    queryResult = "SELECT * FROM STOCK_ACCOUNT s WHERE s.unique_id= '" + customerID + "'";
    rs = stmt.executeQuery(queryResult);
        
    String availableshare = "";

    while (rs.next()){
      availableshare = (rs.getString("shares"));
    }

    double availableShares = Double.parseDouble(availableshare);

    if(availableShares < shares){
      System.out.println("You don't have enough shares");
      System.exit(1);
    }

    //update amount of shares (minus availableshare)
    String updateRow = "UPDATE STOCK_ACCOUNT SET shares = shares - " + String.valueOf(shares) + " WHERE unique_id = '" + customerID + "'";
		stmt.executeUpdate(updateRow);

    //update, add money to balance (plus amount)
    updateRow = "UPDATE ACCOUNT set balance = balance + " + String.valueOf(total-20) + " WHERE unique_id = '" + customerID + "'";
		stmt.executeUpdate(updateRow);



    queryResult = "SELECT * FROM ACCOUNT a WHERE a.unique_id= '" + customerID + "'";
    rs = stmt.executeQuery(queryResult);
        
    String ovbalance = "";

    while (rs.next()){
      ovbalance = (rs.getString("balance"));
    }



    //insert to transaction
    String insertData = "INSERT INTO TRANSACTIONS(username, _date, trans_type, shares, balance, overall_balance)" + " VALUES('" + username + "','" + curr_date + "','sell','" + String.valueOf(shares) + "'," + String.valueOf(total) + ", " + ovbalance+")";
		stmt.executeUpdate(insertData);
      
    System.out.println("Sell stock transaction completed");

  }


  public void depositMoney() throws SQLException{
    String moneyAmount="";
    System.out.println("How much money to deposit?");
      
    try {
      moneyAmount = br.readLine();
    } catch (IOException ioe) {
      System.out.println("Not an option for depositing");
      System.exit(1);
    }
    
    String updateRow = "UPDATE ACCOUNT SET balance = balance + " + moneyAmount + " WHERE unique_id = '" + customerID + "'";

    Statement stmt = myC.getConnection().createStatement();
    stmt.executeUpdate(updateRow);


    
    String queryResult = "SELECT * FROM ACCOUNT a WHERE a.unique_id= '" + customerID + "'";
    ResultSet rs = stmt.executeQuery(queryResult);
        
    String ovbalance = "";

    while (rs.next()){
      ovbalance = (rs.getString("balance"));
    }


    //add to transaction
    String insertData = "INSERT INTO TRANSACTIONS(username, _date, trans_type, shares, balance, overall_balance)" + " VALUES('" + username + "','" + curr_date + "','deposit',0," + moneyAmount+ ", " + ovbalance +")";
      
		
		stmt.executeUpdate(insertData);

    System.out.println("Deposit transaction completed");
  }



  public void withdrawMoney() throws SQLException{
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

    double balance = Double.parseDouble(currBalance);
    double withdrawAmount = Double.parseDouble(moneyAmount);


    if(withdrawAmount>balance){
      System.out.println("You don't have enough in your balance");
      System.exit(1);
    }

    String updateRow = "UPDATE ACCOUNT set balance = balance - " + String.valueOf(withdrawAmount) + " WHERE unique_id = '" + customerID + "'";
		stmt.executeUpdate(updateRow);




    queryResult = "SELECT * FROM ACCOUNT a WHERE a.unique_id= '" + customerID + "'";
    rs = stmt.executeQuery(queryResult);
        
    String ovbalance = "";

    while (rs.next()){
      ovbalance = (rs.getString("balance"));
    }
    //add to transaction

    String insertData = "INSERT INTO TRANSACTIONS(username, _date, trans_type, shares, balance, overall_balance)" + " VALUES('" + username + "','" + curr_date + "','withdrawal',0,-" + String.valueOf(withdrawAmount)+ ", "+ ovbalance+")";
      
		
		stmt.executeUpdate(insertData);

    System.out.println("Withdraw transaction completed");
  }



  public void showBalance() throws SQLException{
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

  public void showTransactions() throws SQLException{
    String queryResult = "SELECT * FROM TRANSACTIONS t WHERE t.username= '" + username + "'";
        
    Statement stmt = myC.getConnection().createStatement();
        
    ResultSet rs = stmt.executeQuery(queryResult);
        

    while (rs.next()){
      System.out.println("Date of transaction: ");
      System.out.println((rs.getString("_date")));

      System.out.println("Transaction type: ");
      System.out.println((rs.getString("trans_type")));

      System.out.println("Overall shares bought and sold: ");
      System.out.println((rs.getString("shares")));

      System.out.print("Money transacted: \n$");
      System.out.println((rs.getString("balance")));

      System.out.print("Overall balance: \n$");
      System.out.println((rs.getString("overall_balance")));

      System.out.println("________________________________");
      System.out.println();
    }
  }

  public void listCurrentPrice() throws SQLException{
    String name="";
    String stock_type="";
    String dob="";
    String movie_title="";
    String role="";
    String year="";
    String total_value="";
    String price="";

    System.out.println("Which stock?");
    try {
      stock_type = br.readLine();
    } catch (IOException ioe) {
      System.out.println("Not an option for stock");
      System.exit(1);
    }


    String queryResult = "SELECT * FROM MOVIE_CONTRACT m WHERE m.symbol= '" + stock_type + "'";
        
    Statement stmt = myC.getConnection().createStatement();
        
    ResultSet rs = stmt.executeQuery(queryResult);

    while (rs.next()){
      name = (rs.getString("_name"));
      dob = (rs.getString("dob"));
      movie_title=(rs.getString("movie_title"));
      role=(rs.getString("_role"));
      year=(rs.getString("_year"));
      total_value=(rs.getString("contract"));
      price=(rs.getString("curr_price"));
    }

    System.out.println("\nCurrent Price: $"+ price);
    System.out.println("Actor/director name: "+ name);
    System.out.println("Stock symbol: "+ stock_type);
    System.out.println("Actor/director dob: "+ dob);
    System.out.println("Movie title: "+ movie_title);
    System.out.println("Role: "+ role);
    System.out.println("Year: "+ year);
    System.out.println("Contract: $"+ total_value);
    System.out.println("");
  }

  public void listMovieInformation() throws SQLException{
    String year1 = "";
    String year2 = "";
    String choice = "";
    String movie_option = "";

    System.out.println("Would you like information on a specific movie or search through years movie was created? ");
    try {
      choice = br.readLine();
    } catch (IOException ioe) {
      System.out.println("Not an option for choice");
      System.exit(1);
    }
    if (choice.trim().equals("specific")){
      System.out.println("Which movie? ");
      try {
        movie_option = br.readLine();
      } catch (IOException ioe) {
        System.out.println("Not an option for movie");
        System.exit(1);
      }

      String queryResult = "SELECT * FROM MOVIE_CONTRACT m WHERE movie_title = '" +movie_option +"'";
          
      Statement stmt = myC.getConnection().createStatement();
          
      ResultSet rs = stmt.executeQuery(queryResult);

      System.out.println();
      //display movie info in those years
      while (rs.next()){
        System.out.println("Movie Title: "+ movie_option);
        System.out.println("Year: "+ (rs.getString("_year")));
        System.out.println("Genre: "+ (rs.getString("genre")));
        System.out.println("Ratings: "+ (rs.getString("ratings")));
        System.out.println();
        System.out.println("_______________________________________________");
        System.out.println();
      }

    }else if (choice.trim().equals("range")){
      System.out.println("Year range 1: ");
      try {
        year1 = br.readLine();
      } catch (IOException ioe) {
        System.out.println("Not an option for year");
        System.exit(1);
      }
      System.out.println("Year range 2: ");
      try {
        year2 = br.readLine();
      } catch (IOException ioe) {
        System.out.println("Not an option for year");
        System.exit(1);
      } 

      //strftime('%y', m.date)    
      //find all movies in between the two years

      String queryResult = "SELECT * FROM MOVIE_CONTRACT m WHERE _year<= '" + year2 + "' AND _year >= '" +year1 +"'";
          
      Statement stmt = myC.getConnection().createStatement();
          
      ResultSet rs = stmt.executeQuery(queryResult);

      System.out.println();
      //display movie info in those years
      while (rs.next()){
        System.out.println("Movie Title: "+ (rs.getString("movie_title")));
        System.out.println("Year: "+ (rs.getString("_year")));
        System.out.println("Genre: "+ (rs.getString("genre")));
        System.out.println("Ratings: "+ (rs.getString("ratings")));
        System.out.println();
        System.out.println("_______________________________________________");
        System.out.println();
      }
    } else {
      System.out.println("Not an option");
      System.exit(1);
    }
  }

    
    
  public Customer(String _username, Connect conn, String uniqueID, String c_date){
    myC = conn;
    System.out.println("Logged in");
    customerID = uniqueID;
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
        case "buy":
        try {
          buyStock();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in buying stock. Exiting");
          System.exit(1);
        }
        break;
        case "sell":
        try {
          sellStock();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in selling stock. Exiting");
          System.exit(1);
        }
        break;
        case "deposit":
        try {
          depositMoney();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in Depositing Money. Exiting");
          System.exit(1);
        }
        break;
        case "withdraw":
        try {
          withdrawMoney();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in withdrawing Money. Exiting");
          System.exit(1);
        }
        break;
        case "balance":
        try {
          showBalance();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in showing balance. Exiting");
          System.exit(1);
        }
        break;
        case "transactions":
        try {
          showTransactions();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in showing transactions. Exiting");
          System.exit(1);
        }
        break;
        case "price":
        try {
          listCurrentPrice();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in listing current price. Exiting");
          System.exit(1);
        }
        break;
        case "movies":
        try {
          listMovieInformation();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in listing movie information. Exiting");
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

