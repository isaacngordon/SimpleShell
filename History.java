import java.util.ArrayList;
import java.util.List;

public class History {
	
	int count;																//keep track of the number of records in history
	List<String> history;													//keep track of record contents in history
	
	public History(){
		count = 0;
		history = new ArrayList<String>();
	}//constructor
	
	/**
	 * add a new string to the history
	 * @param command
	 */
	public void add(String command){
		count++;															//increment count
		history.add(command);												//add command string to history
		
	}//add


	/**
	 * Print out the complete contents of history.
	 */
	public void print() {
		try{																//try to 
			for(int i = 0; i < history.size(); i++) 						//for every item in History
				System.out.println(i + " " + history.get(i));				//print out its index then string
		} catch(IndexOutOfBoundsException iobe){							//if there is nothing, an exception will be thrown
			System.out.println("History is empty.");						//deal with it by printing an error message
		}//Catch
	}//print
	
	/**
	 * Get the size of the history.
	 * @return
	 */
	public int size(){
		return count;
	}//size

	/**
	 * Get the ith history record in history. 
	 * If it does not exist then throw IndexOutOfBoundsException
	 * @param i
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public String get(int i) throws IndexOutOfBoundsException{
		if(i < 0 || i >= count) throw new IndexOutOfBoundsException();		//if the index is too small or big throw an exception
		String element = history.get(i);									//set element to the contents of history at index i
		return element;														//return that element
	}//get
	
	/**
	 * if the user entered "history" command, execute the proper history command
	 * Case 1: "History" --> print history
	 * Case 2: "!!" execute last entered command from history
	 * Case 3: "!<integer i>" execute the ith command from history
	 * @param command
	 * @return new command
	 */
	public String commandChecker(String command){
		String commandLine = command;
		
		if(commandLine.equalsIgnoreCase("history")){									//commandLine is correct command "history"
	    	  print();																	//print history
	    	  add(commandLine); 														//add input  to the history record
	    	  return "-1";																//begin loop again
	      }//if
	      
	      //if command starts with "!" it might be valid history command, otherwise move on
	      if(commandLine.charAt(0) == '!'){
	    	  System.out.println("line is " + commandLine);
	    	  if(commandLine.equals("!!")){														//IF the command is "!!"
	    		  try{																			//try
	    			  commandLine = this.get(this.size() - 1);									//setting the commandLine = to the last command in history
	    		  }catch(IndexOutOfBoundsException ie){											//if the index is out of bounds
	    			  System.out.println("There is no commands in the history.");				//that means there is no commands in history yet
	    			  return "-1";
	    		  }//tryCatch
	    	  }//if
	    	  else {
	    		  Integer i;																			//int i to store the index of history item the user wants
	    		  try{																					//try
	    			  String sub = commandLine.substring(1);											//create substring after "!"
	    			  i = Integer.parseInt(sub);														//attempt to parse the substring as an INteger
	    		  } catch(Exception e){																	//if the substring is not an integer, or doise not exist
	    			  System.out.println("Correct use of history commands is either \"!!\" "			//print error message
	    			  		+ "to run last used command, \nor \"!<i>\" wher i is an integer "
	    			  		+ "between 0 and the length of the history, which executes \n"
	    			  		+ "the ith command in the history.");
	    			 return "-1";																		//then restart loop
	    		  }//catch 
	    		  
	    		  if(i > this.size() - 1){																//if i is greater than the history recrd size
	    			  System.out.println("There are only " + history.size() + " commands in history. "	//print error
	    			  		+ "Please use i such that 0<=i<" + history.size() + "." );
	    			  return "-1";
	    		  }//if
	    		  
	    		  commandLine = this.get(i); 														//otherwise, set the command line to whatever history record the user asked for
	    	  }//else
	      }//if
	      if(commandLine.charAt(0) == '!' || commandLine.equalsIgnoreCase("history"))				//check if the new commandLine is still a history command
	    	  return commandChecker(commandLine);													//if so then recursively run the method 
	      
	      return commandLine;																		//otherwise return the result
		
	}//historyCommandChecker

}
