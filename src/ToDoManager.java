package todo_manager;

import java.util.ArrayList;
import java.util.Scanner;



public class ToDoManager {


	//commands received from user	
	enum CommandType {
		CMD_ADD, CMD_DELETE, CMD_CLEAR, CMD_EDIT, CMD_UNDO, CMD_SEARCH, CMD_DISPLAY, CMD_DONE
	}
	
	private static final String MESSAGE_WELCOME = "Welcome to ToDo Manager!";
	private static final String MESSAGE_COMMAND_PROMPT = "command : ";
	
	

	public static void main(String[] args){
		String userInput;
		setup();
		UserInterface.showToUser(MESSAGE_WELCOME);
		while (true) {
			userInput = UserInterface.getNextCommand();
			////// dummy code start //////
			String s = userInput.substring(5);
			UserInterface.showToUser(s + " added.");
			////// dummy code end   //////
			Logic.actOnUserInput(userInput);
		}
	}
	
	public static class InvalidDateException extends Exception {

		/**
		 * No idea what this is for, but if i dont put this here eclipse complains. Anyone knows?
		 */
		private static final long serialVersionUID = -2426097127326357613L;

		public InvalidDateException() { 
			  super(); 
		}
		
		public InvalidDateException(String message) { 
			super(message); 
		}
		
		public InvalidDateException(String message, Throwable cause) { 
			super(message, cause); 
		}
		
		public InvalidDateException(Throwable cause) { 
			super(cause); 
		}
	}
	
	public static class EmptyInputException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8501096073450889230L;

		public EmptyInputException() { 
			  super(); 
		}
		
		public EmptyInputException(String message) { 
			super(message); 
		}
		
		public EmptyInputException(String message, Throwable cause) { 
			super(message, cause); 
		}
		
		public EmptyInputException(Throwable cause) { 
			super(cause); 
		}
	}
	
	public static class InvalidInputException extends Exception {


		/**
		 * 
		 */
		private static final long serialVersionUID = 4463352598274800204L;

		public InvalidInputException() { 
			  super(); 
		}
		
		public InvalidInputException(String message) { 
			super(message); 
		}
		
		public InvalidInputException(String message, Throwable cause) { 
			super(message, cause); 
		}
		
		public InvalidInputException(Throwable cause) { 
			super(cause); 
		}
	}
	
	/**
	 * Executable object that encapsulates everything that Logic needs to execute the command
	 * Properties cannot be changed after instantiation
	 * @param CommandType command		Enum type that is used to determine what is the command to run
	 * @param String info 				String containing task description or other input that doesnt fit into DateTime or CommandType
	 * @param DateTime dateTime 		Date object used to capture date and time of task
	 * @param DateTime dateTimeEnd 		Date object representing date at which the task ends
	 * @author Khye An
	 *
	 */
	public static class Executable{
		CommandType command = null;
		String info = null;
		String startingDate = null;
		String endingDate = null;
		String startingTime = null;
		String endingTime = null;
		Boolean doneness = false;
		
		public Executable(CommandType commandInput){
			command = commandInput;
		}
		
		public Executable(CommandType commandInput, String infoInput){
			info = infoInput;
			command = commandInput;
		}
		
		public Executable(CommandType commandInput, String infoInput, String startInput, String endInput){
			info = infoInput;
			command = commandInput;
			startingTime = startInput;
			endingTime = endInput;
		}
		
		public CommandType getCommand(){
			return command;
		}
		
		public String getInfo(){
			return info; 
		}
		
		public String getStartingDate(){
			return startingDate; 
		}
		
		public String getEndingDate(){
			return endingDate; 
		}
		
		public String getStartingTime(){
			return startingTime;
		}
		
		public String getEndingTime(){
			return endingTime;
		}
	}
	
	
	/**
	 * Temporary storage of entries in .txt file. Call Storage.writeFile after modifying this
	 * @author Khye An
	 *
	 */
	public class EntryList extends ArrayList<Entry>{

		/**
		 *  
		 */
		private static final long serialVersionUID = -1625638209526224271L;
		private Entry _head;
		public void EntryList(){
			_head = null;
			
		}
		
	}
	
	/**
	 * Storage of list of entries that is most recently displayed to the user
	 * Mainly used to delete after calling sort or search
	 * @author Khye An
	 *
	 */
	public class DisplayList extends ArrayList<Entry>{

		/**
		 *  No idea what this is for, but if i dont put this here eclipse complains. Anyone knows?
		 */
		private static final long serialVersionUID = -7851999266553632272L;

		
	}
	
	public static class UserInterface {
		
		static Scanner scannerInstance;
		
		/**
		 * Prints text for the user to read, and goes to the next line
		 * @param String s
		 */
		public static void showToUser(String s){
			System.out.println(s);
		}

		public static String getNextCommand() {
			System.out.print(MESSAGE_COMMAND_PROMPT);
			String userCommand = scannerInstance.nextLine();
			return userCommand;
		}

		public static void setup() {
			scannerInstance = new Scanner(System.in);
		}
	}
	
	
	//TODO	
	public static void setup(){
        UserInterface.setup();
//		Interpreter.setup();
//		Logic.setup();   //creation and filling out of linked lists
//		Storage.setup(); //initializing of reader / writer
//		

		
	}
}
