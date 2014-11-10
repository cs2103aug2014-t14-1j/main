package todo_manager;

/**
 * UserInterface will show welcome message. 
 * Waiting user to key in, (getNextCommand) return the user input as a string to be process 
 * 
 *
 */

import java.util.Scanner;
//@author A0098924M-unused
//We switched to a GUI
public class UserInterface {

	static Scanner scannerInstance;
	
	private static final String MESSAGE_COMMAND_PROMPT = "command : ";
	
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
