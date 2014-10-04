package todo_manager;

public class ToDoManager {

	//commands received from user	
	enum CommandType {
		CMD_ADD, CMD_DELETE, CMD_CLEAR, CMD_EDIT, CMD_UNDO, CMD_SEARCH, CMD_DISPLAY, CMD_DONE
	}
	
	private static final String MESSAGE_WELCOME = "Welcome to ToDo Manager!";
	
	public static void main(String[] args){
		
		String userInput;
		setup();
		UserInterface.showToUser(MESSAGE_WELCOME);
		
		while (true) {
			
			userInput = UserInterface.getNextCommand();
			Logic logic = new Logic();
			logic.actOnUserInput(userInput);
			
		}
	}
	
	
//TODO	
	public static void setup(){
	        UserInterface.setup();
//			Interpreter.setup();
//			Logic.setup();   //creation and filling out of linked lists
//			Storage.setup(); //initializing of reader / writer
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
		
}
