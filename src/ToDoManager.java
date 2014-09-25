import java.util.ArrayList;



public class ToDoManager {


	//commands received from user	
	enum CommandType {
		CMD_ADD, CMD_DELETE, CMD_CLEAR, CMD_EDIT, CMD_UNDO, CMD_SEARCH, CMD_DISPLAY, CMD_DONE
	}
	
	private static final String MESSAGE_WELCOME = null;
	
	public static void main(String[] args){
		UserInterface.showToUser(MESSAGE_WELCOME);
		//TODO
	}
	
	
	/**
	 * Date encapsulation object
	 * @param String time 		time given in 4-digit format, eg. "0030" , "2359", or "1400" 
	 * @param int day			day of the month, must be between 1 and 31, inclusive
	 * @param int month 		month of the year, must be between 1 and 12, inclusive
	 * @param int year 			Year. Must be greater than 0
	 * @throws InvalidDateException			throws this exception if date format given is not valid. Use try/catch when creating date object
	 * @author Khye An
	 *
	 */
	public static class DateTime{
		int _day, _month, _year;
		String _time;
		
		public DateTime(String time, int day, int month, int year) throws InvalidDateException{
			if (! validityCheck(time, day, month, year)){
				throw new InvalidDateException();
			}
			_time = time;
			_day = day;
			_month = month;
			_year = year;
		}
		
		private boolean validityCheck(String time, int day, int month, int year) {
			int num_time = Integer.parseInt(time);
			if (num_time < 0 || num_time > 2359) {
				return false;
			} else if (day <= 0 || day > 31) {
				return false;
			} else if (month <= 0 || month > 12) {
				return false;
			} else if (year <= 0) {
				return false;
			} else {
				return true;
			}
		}
		
		public String toString(){
			return null; //TODO
		}
	}
	/**
	 * 
	 * @author Khye An
	 *
	 */
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
		CommandType _command = null;
		String _info = null;
		String _startingDate = null;
		String _endingDate = null;
		String _startingTime = null;
		String _endingTime = null;
		Boolean _doneness = false;
		
		public Executable(CommandType command){
			_command = command;
		}
		
		public Executable(CommandType command, String info){
			_info = info;
			_command = command;
		}
		
		public Executable(CommandType command, String info, String start, String end){
			_info = info;
			_command = command;
			_startingTime = start;
			_endingTime = end;
		}
		
		public CommandType getCommand(){
			return _command;
		}
		
		public String getInfo(){
			return _info; 
		}
		
		public String getStartingDate(){
			return _startingDate; 
		}
		
		public String getEndingDate(){
			return _endingDate; 
		}
		
		public String getStartingTime(){
			return _startingTime;
		}
		
		public String getEndingTime(){
			return _endingTime;
		}
	}
	

	/**
	 * Object to represent an entry in the txt file while we are making changes to the entries
	 * @author Khye An
	 *
	 */
	
	/**
	 * Temporary storage of entries in .txt file. Call Storage.writeFile after modifying this
	 * @author Khye An
	 *
	 */
	public class EntryList extends ArrayList<Entry>{

		/**
		 *  No idea what this is for, but if i dont put this here eclipse complains. Anyone knows?
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
}
