package net.sqlitetutorial;
import java.io.*;
import java.sql.*;

public class Customer {
  private int customerID;
  private Connect custConn;

  public void buyStock(){
    
  }

  public void sellStock(){
    
  }

  public void depositMoney(){

  }

  public void withdrawMoney(){

  }

  public void showBalance(){

  }

  public void showTransactions(){

  }

  public void listCurrentPrice(){

  }

  public void listMovieInformation(){

  }

    
    
    
    
  public Customer(String username, Connect conn){
    custConn = conn;
    
    System.out.println("Logged in");
    
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
        buyStock();
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

