package net.sqlitetutorial;
class InvalidUsernameException extends Exception
{
private String usernameError;

public InvalidUsernameException(String e){
     super();
     usernameError = e;
   }

   public String getError()
   {
    return usernameError;
   }
}