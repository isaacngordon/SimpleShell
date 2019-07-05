import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SSInputParser {
	
	String commandLine;					//commandLine string to be parsed
	List<String> commands;				//list of commands from the commandLine String
	boolean isWindows;					//whether or not a Windows OS is being used
	
	

	public SSInputParser(String commandLine, boolean isWindows) {
		this.commandLine = commandLine;
		this.isWindows = isWindows;
		commands = generateCommandsList();
	}//constructor

	/**
	 * Returns an ArrayList of commands generated from the commandLine, 
	 * including relevant Windows commands in index 0 and 1, if isWindows = true.
	 * @return
	 */
	private List<String> generateCommandsList() {
		List<String> list = new ArrayList<String>();
		
		//if isWindows is true, add "cmd.exe" and "/c" to the list
		if(isWindows){
			list.add("cmd.exe");
			list.add("/c");
		}//if

		//Take apart the commandLind string and add its various contents to the list		 
		StringTokenizer st = new StringTokenizer(commandLine," ");
		while(st.hasMoreTokens()) 
			list.add(st.nextToken());
		
		return list;
	}//generateCommandsList

	/**
	 * Return the list of commands parsed from the commandLine string. 
	 * @return
	 */
	public List<String> getList() {
		return commands;
	}//getList
	
	

}
