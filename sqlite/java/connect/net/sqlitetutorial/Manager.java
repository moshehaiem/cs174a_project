package net.sqlitetutorial;
import java.io.*;
import java.sql.*;

public class Manager {
  private int managerID;
  private Connect custConn;

  public void addInterest(){

  }

  public void generateMonthlyStatement(){

  }

  public void listActiveCustomers(){
    
  }

  public void generateDTER(){

  }

  public void getCusomterReport(){

  }

  public void deleteTransactions(){

  }
    
    
    
    
  public Manager(String username, Connect conn){
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
        case "add monthly interest":
        addInterest();
        break;
        case "generate monthly statement":
        generateMonthlyStatement();
        break;
        case "list active customers":
        listActiveCustomers();
        break;
        case "generate DTER":
        generateDTER();
        break;
        case "get customer report":
        getCusomterReport();
        break;
        case "delete transactions":
        deleteTransactions();
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
