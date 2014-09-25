package todo_manager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ValidationCheck {
	public static boolean isValidDate(String input) {
		boolean isValid = validateDate(input);
		return isValid;
	}
	
	public static boolean isValidDuration(String from, String to) {
		boolean isValid = validateDuration(from, to);
		return isValid;
	}
	
	public static boolean isValidTime(String input) {
		boolean isValid = validateTime(input);
		return isValid;
	}
	

	public static boolean isValidOperation(String operation) {
		boolean isValid = validateOperation(operation);
		return isValid;
	}
	

	private static boolean validateOperation(String operation) {
		operation = operation.toLowerCase();
		switch (operation) {
			case "add" :
			case "delete" :
			case "display" :
			case "edit" :
			case "undo" :
			case "clear" :
			case "search" :
			case "exit" :
				//all valid operations should fall through to return true
				return true;
			default :
				return false;
			}
	}
	
	private static boolean validateDate(String date) 
	{
		String DATE_FORMAT = "ddMMyy";
        try {
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            dateFormat.setLenient(false);
            dateFormat.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
	}
	
	private static boolean validateTime(String input) {
		
		try {
			if (input.length() != 4) {
				return false;
			}
			int time = Integer.parseInt(input);
			int mins = time % 100;
			int hours = time / 100;
			
			if (mins > 59 || hours > 23 || mins < 0 || hours < 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	private static boolean validateDuration(String from, String to) {
		try {
			int start = Integer.parseInt(from);
			int end = Integer.parseInt(to);
			
			return ( ((end - start) > 0) && validateTime(from) && validateTime(to) );
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
