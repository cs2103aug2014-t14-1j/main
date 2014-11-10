package todo_manager;

public class ToDoManager {
	
	//@author A0098735M
	//commands received from user	
	enum CommandType {
		CMD_ADD,    CMD_DELETE,  CMD_CLEAR, CMD_EDIT, CMD_UNDO, 
		CMD_SEARCH, CMD_DISPLAY, CMD_DONE,  CMD_SORT, CMD_EXIT,
		CMD_HELP, CMD_UNDONE, CMD_SEARCHFREEDAY
	}
	
	static final String MESSAGE_WELCOME = "Welcome to ToDo Manager!";
	static final String MESSAGE_ERROR_GENERIC = "An error has occurred.";
	static final String MESSAGE_ERROR_EMPTY_INPUT = "No command was given.";
	static final String MESSAGE_WRONG_INPUT_FORMAT = "Wrong input format given"; 
	static final String MESSAGE_INSUFFICIENT_ARGUMENT = "Insufficient arguments given."; 
	static final String MESSAGE_WRONG_DATE_FORMAT = "Wrong date format given."; 
	static final String MESSAGE_WRONG_TIME_FORMAT = "Wrong time format given."; 
	static final String MESSAGE_PAST_DATE = "Date given is in the past.";
	
	Logic logic;
	UserInterface userInterface;
	Storage storage;
	
	public static void main(String[] args){
		ToDoManager toDoManager = new ToDoManager();
		toDoManager.setup();
		
		String userInput;
		UserInterface.showToUser(MESSAGE_WELCOME);
		
		while (true) {
			
			userInput = UserInterface.getNextCommand();
			toDoManager.logic.actOnUserInput(userInput);
			
		}
	}
	
	
//TODO	
	public void setup(){
		 logic = Logic.getInstance();
		 userInterface = new UserInterface();
		 storage = new Storage();		
		
	     UserInterface.setup();
//			Interpreter.setup();
	     logic.setup(this);   //creation and filling out of linked lists
//			storage.setup(); //initializing of reader / writer
   }
	
	//@author generated
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
	
	//@author generated
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
		
}
