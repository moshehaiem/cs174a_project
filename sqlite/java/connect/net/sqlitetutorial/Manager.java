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
    
    
    
    
  public Manager(String username, Connect conn, String uniqueID){
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
        try {
          addInterest();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in adding interest. Exiting");
          System.exit(1);
        }
        break;
        case "generate monthly statement":
        try {
          generateMonthlyStatement();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in generating monthly statement. Exiting");
          System.exit(1);
        }
        break;
        case "list active customers":
        try {
          listActiveCustomers();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in listing active customers. Exiting");
          System.exit(1);
        }
        break;
        case "generate DTER":
        try {
          generateDTER();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in generating DTER. Exiting");
          System.exit(1);
        }
        break;
        case "get customer report":
        try {
          getCusomterReport();
        } catch (SQLException e) {
          System.out.println(e);
          System.out.println("Error in getting customer report. Exiting");
          System.exit(1);
        }
        break;
        case "delete transactions":
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
