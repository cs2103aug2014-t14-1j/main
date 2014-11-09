package todo_manager;
/*
 @author: A0085159W
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidationCheck {
	private static final String DATE_FORMAT = null;


	public static boolean isValidDate(String input) {
		boolean isValid = validateDate(input);
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
	
	public static boolean isValidStatus(String status) {
		if (status.compareTo("done") == 0 || status.compareTo("undone") == 0) {
			return true;
		} else {
			return false;
		}
	}


	public static boolean isGreater(String date) throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
		Date newDate = dateFormat.parse(date);
		Date today = new Date();
		// minus the date by 1 day
		Date correctDate = new Date(today.getTime() - (1000 * 60 * 60 * 24));
		
		if(!newDate.before(correctDate)){
			return true;
		}
		
		return false;
	}
	
	public static void checkStartEndTime(String startStr, String endStr){
		int startInt, endInt;
		if (startStr == null || endStr == null) {
			return;
		}
		try {
			startInt = Integer.parseInt(startStr);
			endInt = Integer.parseInt(endStr);
		} catch (Exception e) {
			throw new IllegalArgumentException(ToDoManager.MESSAGE_WRONG_TIME_FORMAT);
		}
		if (startInt > endInt){
			throw new IllegalArgumentException("Start time is greater than end time");
		}
	}
	
}
