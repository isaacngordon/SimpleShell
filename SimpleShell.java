import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class SimpleShell {

	
	  public static void main(String[] args) throws java.io.IOException {
		  
		String osName = System.getProperty("os.name");										//Retrieves OS name
		boolean isWindows = osName.contains("window") || osName.contains("Window");			//checks if the OS is a windows OS
		String pathDeli;																		//delimeter in path strings
		
		//define the delimiter within path strings
		if(isWindows) pathDeli = "\\";
		else pathDeli = "/";
		
		History history = new History();													//create new history to log commands
		File chosenDir = new File(System.getProperty("user.dir"));							//keep track of directory, start in user.dir
	    
		String commandLine;																	//to hold commanLine arguments
	    BufferedReader console = new BufferedReader(new InputStreamReader(System.in));		//to read input from the console
	    List<String> parsedCommands; 														//will contain list of arguments from commandLine

	    
	    // we break out with <control><C>
	    while (true){
	      // read what the user entered
	      System.out.print("jsh>");
	      commandLine = console.readLine();
	      commandLine = commandLine.trim();
	      
	      // if the user entered a return, just loop again
	      if(commandLine.equals("")) continue;
	      
	      /*
	       * Check if the entered command is a history command.
	       *  If it is, then commandLine will be set to "-1" and then we know to hop to end of loop.
	       *  Otherwise we will continue as normals
	       */
	      commandLine = history.commandChecker(commandLine);						
	      if(commandLine == "-1") continue;		
	      
	      /**
	       * (1) parse the input to obtain the command and any parameters
	       */
	      history.add(commandLine); 												//add input  to the history record
	      SSInputParser ssip = new SSInputParser(commandLine, isWindows);			//new input parser to parse commandLine
	      parsedCommands = ssip.getList();											//gets the parsed list from the ssip
	      
	      /**
	       * (2) create a ProcessBuilder object
	       * (3) start the process
	       */
	      ProcessBuilder pb;
    	  Process process;
	      
    	  /**
    	   * If the user inputs an invalid command, then the Process.start() will throw an IOException.
    	   * Handle that by catching it and having the user enter a new prompt. 
    	   */
	      try{
	    	  pb = new ProcessBuilder(parsedCommands);												//create new ProcessBuilder based off commands
	    	  pb.directory(chosenDir); 																//set directory to our current chosen directory
	    	  process = pb.start();	    	  														//start procwss
	      } catch (IOException io){
	    	  System.out.println("Valid commands you know not. Try again you must.");				//tell the user they screwed up
	    	  continue;																				//go to top of loop
	      };//tryCatch
	      
    	  /**
    	   * Check for cd command with valid arguments, if it exists:
    	   * Case 1: Command and arguments are valid, implement normally
    	   * Case 2: Directory argument is bad argument, throw 
    	   */
    	  if(parsedCommands.contains("cd")){
    		  File newDir;																				//new directory file
    		  
    		  if(parsedCommands.indexOf("cd") == parsedCommands.size() - 1){							//check if "cd" is the final command, 
    			 newDir = new File(System.getProperty("user.dir")); 									//if so then return to the user's directory
    		  }//if
    		  else{																						//else, check the following index of parsedCommands 
    			  String path = parsedCommands.get(parsedCommands.indexOf("cd")+1);						//for the input path
    			  if(path.equals("..")){																//if the input string is",,"
    				  newDir = pb.directory().getParentFile();											//set to parent directory
    			  }//if
    			  else{																					//otherwise, 
    				  if(path.contains(pathDeli))															//check for "/"
    					  newDir = new File(path);														// and set File to the entered path
    				  else																				//and if there is no"/"
    					  newDir = new File(pb.directory().getAbsolutePath() + pathDeli + path);				//append path name to end of current path
    			  }//else  
    		  }//else
    		  if(!newDir.exists()) System.out.println("Invalid filepath. ");							//If the new directory doesn't exist, tell the user
    		  else chosenDir = newDir;																	//otherwise, set the working directory to the user's specified directory
    	  }//if

	      /**
	       * (4) obtain the output stream
	       * Note: Process.getInputStream() returns a stream inputing data into
	       * our program FROM the Process. It is therefore output of the Process 
	       * and input for out program.
	       */
	      InputStream processOutput = process.getInputStream();								//get inputstream of process
	      InputStreamReader por = new InputStreamReader(processOutput);						//create inputstreamreader
	      BufferedReader brout = new BufferedReader(por);									//create buffereedreader for inputstreamreader
	      
	      /**
	       * (5) output the contents returned by the command
	       */
	      String processOutputText;															//to store current lin in brout
	      while((processOutputText = brout.readLine()) != null) 							//while there still exists output line
	    	  System.out.println(processOutputText);										//output each line to the console
	      
	      brout.close();																	//close bufferedReader
	      } //while
	    //console.close();
	    }//main
}//class
