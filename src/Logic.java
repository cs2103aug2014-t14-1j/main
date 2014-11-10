package todo_manager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

import todo_manager.ToDoManager.CommandType;
import todo_manager.ToDoManager.EmptyInputException;

public class Logic {
	
	/*
	 * Help-related messages
	 * HELP_NO_KEYWORD looks really out of alignment here, but looks fine on the GUI.
	 */
	//@author A0098735M
	public static final String HELP_NO_KEYWORD = 
			  "Type \"help <command>\" to get help for that particular topic.\n"
			+ "List of topics : \n"
			+ "  /add         /view\n"
			+ "  /delete     /clear\n"
			+ "  /edit         /undo\n"
			+ "  /mark       /sort\n"
			+ "  /freeday     /exit\n"
			+ "   date        time\n\n" 
			+ "  Press F3 for the list of HOTKEYS.";
	
	private static final String HELP_INVALID_KEYWORD = " Invalid help topic given.\n";
	private static final String HELP_ADD = " Format of add command : /add <task name>\n\n" +
										   " The following can also be added after the basic add command : \n" +
										   " /on <date> <start time> <end time>\n" +
										   " /on <date> <single time>\n" +
										   " /by <date> <end time>\n" +
										   " /start <date> <start time> /by <date> <end time>\n" +
										   " Note that each <time> is optional but each <date> is required.\n";
	private static final String HELP_DELETE = " Format of delete command : \n" +
											  " /delete <index no.> \n" +
											  " This deletes the entry with that index in the most recently shown list.\n" +
											  " Multiple indexes seperated by spaces also accepted, eg. : \n" +
											  " /delete 1 4 5\n";
	private static final String HELP_CLEAR = " \"/clear\" deletes all saved tasks.\n";
	private static final String HELP_EDIT = " Format for edit command : \n" +
											" /edit <index no.> <new task name>\n" + 
											" /edit <index no.> /on <new date>\n" + 
											" /edit <index no.> /by <new date>\n" + 
											" /edit <index no.> /start <new date> /by <new date2>\n" +
											" Note that <index no.> refers to the numbering in the most recently displayed list.\n" +
											" Time can replace or be added after dates (separated with a space), to edit the time of an event.";
	private static final String HELP_UNDO = " The /undo command reverses the most recent change made.\n" +
											" This extends to all actions taken since the app was started.\n";
	private static final String HELP_MARK = " This marks the item as done. Format of mark : \n" +
											" /mark <keyword>\n" +
											" /mark <index no.>\n" + 
											" Note that <index no.> refers to the numbering in the most recently displayed list.\n" +
											" <index no.> can be multiple numbers separated by spaces, to mark several items at one go.\n";
	private static final String HELP_VIEW = " Format for view : \n" +
											" /view         Shows all saved tasks\n" +
											" /view today\n" +
											" /view tmr or tomorrow\n" +
											" /view <month name> \n" +
											" /view <done or undone>\n" +
											" /view <date>\n" +
											" /view <keyword>\n";
	private static final String HELP_SORT = " The /sort command arranges the tasks chronologically.\n";
	private static final String HELP_EXIT = " The /exit command shuts down ToDoManager.\n";
	private static final String HELP_DATE = " Format for date is ddMMyy, eg. 210315 for 21st March 2015.\n";
	private static final String HELP_TIME = " Format for time is hhmm, eg. 1435 for 2.35pm.\n";
	private static final String HELP_FREEDAY = "Format for finding the next free day is /freeday";

	private static final String NoEndingDate = "999999";

	private static final String NoEndingTime = "0000";



	Storage storage;
	
	private static LinkedList<LinkedList<Entry>> preList = new LinkedList<LinkedList<Entry>>(); // preState
	
	private static Logic instance = null;
	
	public static LinkedList<Entry> entryList = new LinkedList<Entry>();
	private static LinkedList<Entry> displayList = new LinkedList<Entry>();
	
	private static Logging logObj = Logging.getInstance();
	private Result result;
	private static String DATE_FORMAT = Interpreter.DATE_FORMAT;

	//@author A0098924M
	private Logic() {
	}
	//@author A0098924M
	public static Logic getInstance() {
		if (instance == null) {
			instance = new Logic();
		}
		return instance;
	}
	//@author A0098924M
	public static LinkedList<Entry> getEntryList() {
		return entryList;
	}

	void setupGUI(ToDoManagerGUI toDoManagerGUI) {
		storage = toDoManagerGUI.storage;
		entryList = readFromStorage();
	}
	
	void setup(ToDoManager toDoManager) {
		storage = toDoManager.storage;
		entryList = readFromStorage();
	}
	//@author A0098924M
	//UI module will call this method 
	public Object actOnUserInput(String userInput) {
		
		Executable exe;
		Object displayObj;
		try {
			
			exe = Interpreter.parseCommand(userInput);
			displayObj = execute(exe);
			
		} catch (Exception e) {
			return printError(e);
		}
		
		return displayObj;
	}
	
	//@author A0098924M
	public Object execute(Executable task) throws Exception{
		
		result = new Result();
		CommandType command = task.getCommand();
		switch (command) {
		
			case CMD_ADD: 
				saveEntryListToPreList(); //save the pre-state of List
				executeAdd(task);
				executeDisplay(entryList);
				result.setCommandType(CommandType.CMD_ADD);
				result.setFeedback("A new task added successfully");
				result.setSuccess(true);
				result.setDisplayList(entryList);
				return result;
			
			case CMD_CLEAR: 
				saveEntryListToPreList();
				executeClear(task);
				executeDisplay(entryList);
				result.setCommandType(CommandType.CMD_CLEAR);
				result.setFeedback("All tasks cleared");
				result.setSuccess(true);
				return result;
			
			case CMD_DELETE: 
				saveEntryListToPreList();
				executeDelete(task);
				executeDisplay(entryList);
				result.setCommandType(CommandType.CMD_DELETE);
				result.setFeedback("Task deleted");
				result.setSuccess(true);
				result.setDisplayList(entryList);
				return result;
			
			case CMD_DISPLAY: 
				executeDisplay(entryList);
				result.setCommandType(CommandType.CMD_DISPLAY);
				result.setFeedback("Display tasks");
				result.setSuccess(true);
				result.setDisplayList(entryList);
				return result;
			
			case CMD_DONE: 
				saveEntryListToPreList();
				executeDone(task);
				executeDisplay(entryList);
				result.setCommandType(CommandType.CMD_DONE);
				result.setFeedback("Task marked done");
				result.setSuccess(true);
				result.setDisplayList(entryList);
				return result;
			
			case CMD_UNDONE: 
				saveEntryListToPreList();
				executeUndone(task);
				executeDisplay(entryList);
				result.setCommandType(CommandType.CMD_UNDO);
				result.setFeedback("Task marked undone");
				result.setSuccess(true);
				result.setDisplayList(entryList);
				return result;
				
			case CMD_HELP:
				String out = executeHelp(task);
				return "Help : \n" + out;
				
			case CMD_EDIT: 
				saveEntryListToPreList();
				executeEdit(task);
				executeDisplay(entryList);
				result.setCommandType(CommandType.CMD_EDIT);
				result.setSuccess(true);
				result.setFeedback("Task edited successfully");
				result.setDisplayList(entryList);
				return result;
			
			case CMD_SEARCH: 
				executeSearch(task);
				executeDisplay(displayList);
				result.setSuccess(true);
				return displayList;
			
			case CMD_UNDO: 
				result.setCommandType(CommandType.CMD_UNDO);
				Object outcome =  executeUndo();
				if (outcome instanceof String) {
					result.setFeedback((String) outcome);
				}
				result.setDisplayList(entryList);
				return result;
			
			case CMD_SORT: 
				executeSort();
				executeDisplay(entryList);
				return entryList;
				
			case CMD_SEARCHFREEDAY:
				
				String day = execute_searchFreeday();
				executeDisplay(entryList);
				result.setCommandType(CommandType.CMD_SEARCHFREEDAY);
				result.setDisplayList(entryList);
				result.setFeedback(day);
				return result;
		
				
			case CMD_EXIT:
				preList.clear();
				System.exit(0);
				//return "exit";
			default:
				return ToDoManager.MESSAGE_ERROR_GENERIC;
		}
			
	}
	
	//@author A0098924M
	private String execute_searchFreeday() throws ParseException {
		//search the next free day
		boolean freedayfound = false;
		
		DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
		Calendar cal = Calendar.getInstance();
		String day = dateFormat.format(cal.getTime()); //2014/08/06 16:00:22
		
		do{
			freedayfound = matchDay(day);
			if(freedayfound) break;
			cal.add(Calendar.DATE, 1);  // number of days to add
			day = dateFormat.format(cal.getTime());  // dt is now the new date
		}while(freedayfound != true);
		
		return day;
	}
	
	private boolean matchDay(String day) {
		
		for(Entry e : entryList){
			
			if(e.getDoneness()==false){
				
				if(e.getStartingDate().equals(day) || e.getEndingDate().equals(day)) 
					return false;
				
				//if(((e.getStartingDate().compareToIgnoreCase(day) == -1) && (e.getEndingDate().compareToIgnoreCase(day) == 1))){ //the day is with a period
					
				//}
			}
		}
		
		
		return true;
	}
	//@author A0098924M
	private void executeUndone(Executable task) {
	
		ArrayList<Integer> index = task.getDisplayIndex(); //Get the index to be mark Undone
		
		for (int i = 0 ; i < index.size(); i++) {
			entryList.get(index.get(i)-1).setDoneness(false);
		}
		writeToStorage();	
	}
	
	//@author A0098735M
	private String executeHelp(Executable task) {
		
		String topic = task.getInfo();
		if (topic == null) {
			UserInterface.showToUser(HELP_NO_KEYWORD);
			return HELP_NO_KEYWORD;
		}
		
		switch (topic) {
			case "/add":
				UserInterface.showToUser(HELP_ADD);
				return HELP_ADD;
			case "/delete":
				UserInterface.showToUser(HELP_DELETE);
				return HELP_DELETE;
			case "/clear":
				UserInterface.showToUser(HELP_CLEAR);
				return HELP_CLEAR;
			case "/edit":
				UserInterface.showToUser(HELP_EDIT);
				return HELP_EDIT;
			case "/undo":
				UserInterface.showToUser(HELP_UNDO);
				return HELP_UNDO;
			case "/mark":
				UserInterface.showToUser(HELP_MARK);
				return HELP_MARK;
			case "/view":
				UserInterface.showToUser(HELP_VIEW);
				return HELP_VIEW;
			case "/sort":
				UserInterface.showToUser(HELP_SORT);
				return HELP_SORT;
			case "/exit":
				UserInterface.showToUser(HELP_EXIT);
				return HELP_EXIT;
			case "date" :
				UserInterface.showToUser(HELP_DATE);
				return HELP_DATE;
			case "time" :
				UserInterface.showToUser(HELP_TIME);
				return HELP_TIME;
			case "/freeday":
				UserInterface.showToUser(HELP_FREEDAY);
				return HELP_FREEDAY;
			default : 
				UserInterface.showToUser(HELP_INVALID_KEYWORD);
				UserInterface.showToUser(HELP_NO_KEYWORD);
				return HELP_INVALID_KEYWORD+"\n"+HELP_NO_KEYWORD;
		}
	}

	//@author A0098924M
	private Object executeUndo() {
		
		if (preList.isEmpty()) {
			System.out.println("Nothing to Undo!");
			return "Nothing to Undo!";
		} else {
			entryList = new LinkedList<Entry>(preList.getLast());
			preList.removeLast();
			return entryList;
		}	
	}

	//@author A0098924M
	private void executeSort() {
		// To sort the task by Ending Date and Time
		Collections.sort(entryList);
		writeToStorage();
	}

	//@author A0098924M
	private void executeAdd(Executable task) throws Exception {

		logObj.writeToLoggingFile("Trying to add");
		
		Entry entry = new Entry();
		
		if (task.getInfo()!=null) {
			entry.setName(task.getInfo());
		}
		if (task.getStartingDate()!=null) {
			entry.setStartingDate(task.getStartingDate());
		}
		if (task.getEndingDate()!= null) {
			entry.setEndingDate(task.getEndingDate());
		} else {
			entry.setEndingDate(NoEndingDate);
		}
		
		if (task.getStartingTime()!= null) {
			entry.setStartingTime(task.getStartingTime());
		} if (task.getEndingTime()!= null) {
			entry.setEndingTime(task.getEndingTime());
		} else {
			entry.setEndingTime(NoEndingTime);
		}
		
		ValidationCheck.checkStartEndDate(entry.getStartingDate(), 
									      entry.getEndingDate());
		
		if (entry.getStartingDate() != null && 
			entry.getEndingDate() != null &&
			entry.getStartingDate().equals(entry.getEndingDate())){
			ValidationCheck.checkStartEndTime(entry.getStartingTime(), 
				                 	 	  entry.getEndingTime());
		}
		
		ValidationCheck.checkIllegalChars(entry.getName());
		
		entryList.add(entry);
		writeToStorage();
		
		logObj.writeToLoggingFile("Done adding task");
		
	}
	
	//@author A0128435E
	private void executeDelete(Executable task) throws Exception{
		if (displayList.isEmpty()) {
			throw new Exception("Nothing to delete!");
		}
		
		ArrayList<Integer> index = task.getDisplayIndex();
		int removeIndex;
		for (int i = 0 ; i < index.size(); i++) {
			removeIndex = index.get(i) - 1;
			
			if (i > 0) {
				for (int j = 0; j < i; j++) {
					if (index.get(j) < index.get(i))
						removeIndex--;
				}
			}
			
			Entry removedEntry = displayList.remove(removeIndex);
			if (! displayList.equals(entryList)) { 
				//if they are the same then no need to remove again
				entryList.remove(removedEntry);
			}
		}
		writeToStorage();
	}
	
	//@author A0098924M
	private void executeClear(Executable task) {
		
		entryList.clear();
		writeToStorage();
	}
	
	//@author A0098735M
	private void executeEdit(Executable task) throws Exception {
		
		int displayIndex = task.getDisplayIndex().get(0) - 1;
		Entry entryToEdit = displayList.get(displayIndex);
		
		if (task.getInfo() != null) { //edit name
			entryToEdit.setName(task.getInfo());
		} 
		
		if (task.getStartingDate() != null) { //edit startingDate
			entryToEdit.setStartingDate(task.getStartingDate());
		}
		
		if (task.getEndingDate() != null) {//edit endingDate
			entryToEdit.setEndingDate(task.getEndingDate());
		}
		
		if (task.getStartingTime() != null) { //edit startingTime
			entryToEdit.setStartingTime(task.getStartingTime());
		}
		
		if (task.getEndingTime() != null) {//edit endingTime
			entryToEdit.setEndingTime(task.getEndingTime());
		}
		
		ValidationCheck.checkStartEndDate(entryToEdit.getStartingDate(), 
				                          entryToEdit.getEndingDate());
		
		if (entryToEdit.getStartingDate() != null && 
			entryToEdit.getEndingDate()   != null &&
			entryToEdit.getStartingDate().equals(entryToEdit.getEndingDate())){
				ValidationCheck.checkStartEndTime(entryToEdit.getStartingTime(), 
						entryToEdit.getEndingTime());
		}
		
		ValidationCheck.checkIllegalChars(entryToEdit.getName());

		writeToStorage();	
	}

	//@author A0128435E
	public LinkedList<Entry> executeSearch(Executable task) throws ParseException{
		
		String searchContent, searchContent1, startDate, endDate;
		boolean doneness;

		LinkedList<Entry> searchResult = new LinkedList<Entry>();
		
		if (task.getInfo() != null) { // search by keyword
			ArrayList<Integer> match = noOfMatchWord(task.getInfo());
			int maxMatch = Collections.max(match);
			//display the records from the highest matched value to the lowest one
			for (int i = maxMatch; i > 0; i--) {
				for (int j = 0; j < match.size(); j++) {
					if (match.get(j)==i) {
						searchResult.add(entryList.get(j));
					}
				}
			}
			
		} else if (task.getStartingDate() != null && task.getEndingDate() != null) { //search by starting date and ending date
			searchContent = task.getStartingDate();
			searchContent1 = task.getEndingDate();
			int month = getMonth(searchContent);
			
			for (Entry entry: entryList) {
				startDate = entry.getStartingDate();
				endDate = entry.getEndingDate();
				// select the record with matched month name or within startDate and endDate
				if ((isMonthValue(searchContent) && isEqualMonth(startDate, endDate, month))
						|| isBetween(searchContent, searchContent1, startDate, endDate)) {
					searchResult.add(entry);
				} else {
					continue;
				}
			}
			
		} else if (task.getStartingDate() != null) {
			searchContent = task.getStartingDate();
			for (Entry entry: entryList) {
				startDate = entry.getStartingDate();
				if (isGreater(startDate, searchContent)) {
					searchResult.add(entry);
				}
			}
			
		} else if (task.getEndingDate() != null) {
			searchContent = task.getEndingDate();
			int month = getMonth(searchContent);
			
			for (Entry entry: entryList) {
				endDate = entry.getEndingDate();
				// insert the record with matched month name or within startDate and endDate
				if ((isMonthValue(searchContent) && (getMonth(endDate)== month)) || 
					!isMonthValue(searchContent) && isGreater(endDate, searchContent)) {
						searchResult.add(entry);
				} else {
					continue;
				}
			}
			
		} else if (task.getDoneness() != null) {
			doneness = task.getDoneness();
			for (Entry entry: entryList) {
				if (entry.getDoneness() == doneness) {
					searchResult.add(entry);
				}
			}
		} else {
			throw new IllegalArgumentException("Incorrect record !");
		}
		
		displayList = searchResult;
		return displayList;
	}

	
	private void executeDisplay(LinkedList<Entry> list) {

		displayList = list;
		int count = 1;
		String entryString;
		for (Entry e : list) {
			entryString = count+". "+e.getName();
			if (e.getStartingDate() != null && !e.getStartingDate().equals("")) {
				entryString += " start: " + e.getStartingDate();
			}
			
			if (e.getEndingDate() != null && !e.getEndingDate().equals("") && !e.getEndingDate().equals(NoEndingDate)) {
				entryString += " end: " + e.getEndingDate();
			}
			
			if (e.getDoneness() == true) {
				entryString += " Done";
			}
		    count++;
		    UserInterface.showToUser(entryString);
		}
		if (list.size() == 0) {
			System.out.println("no entry found!");
		}
	}
	//@author A0098924M
	private void executeDone(Executable task) {
		//Mark Done something
		ArrayList<Integer> index = task.getDisplayIndex();
		
		for (int i = 0 ; i < index.size(); i++) {
			entryList.get(index.get(i)-1).setDoneness(true);
		}
		writeToStorage();
	
	}
	
	private void writeToStorage() {
		storage.writeFile(entryList);
	}
	
	private LinkedList<Entry> readFromStorage() {
		return storage.readFile();
	}
	
	//@author A0098735M
	private void saveEntryListToPreList() {
		LinkedList<Entry> newList = new LinkedList<Entry>();
		for (Entry e : entryList) {
			newList.add(e.copy());
		}
		preList.add(newList);
	}
	
	//@author A0128435E
	private ArrayList<Integer> noOfMatchWord(String keyword) {
		ArrayList<Integer> noOfMatch = new ArrayList<Integer>();
		String[] searchKeyword = keyword.trim().toLowerCase().split(" ");
		String[] recordName;
		int matchNo = 0;
		for (Entry entry : entryList) {
			recordName = entry.getName().toLowerCase().split(" ");
			matchNo = 0;
			for (int i = 0; i < recordName.length; i++) {
				for (int j = 0; j < searchKeyword.length; j++) {
					if (searchKeyword[j].length() < 2) {
						continue;
					}
					if (recordName[i].contains(searchKeyword[j])) {
						matchNo++;
					}
				}
			}
			noOfMatch.add(matchNo);
		}
		return noOfMatch;
	}
	
	//@author A0128435E
	private boolean isEqualMonth(String startDate, String endDate, int compareMonth) throws ParseException{
		if (getMonth(startDate) == compareMonth && getMonth(endDate)== compareMonth){
			return true;
		}
		return false;
	}
	
	//@author A0128435E
	private boolean isBetween(String searchStart, String searchEnd, String startDate, String endDate) throws ParseException{
		if (!isMonthValue(searchStart) && isGreater(startDate, searchStart) && isSmaller(endDate, searchEnd)) {
			return true;
		}
		return false;			
	}
	
	//@author A0128435E
	private boolean isGreater(String newDateString, String compareDateString) throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		Date newDate = dateFormat.parse(newDateString);
		Date compareDate = dateFormat.parse(compareDateString);
		
		if (!newDate.before(compareDate)) {
			return true;
		}
		
		return false;
	}
	
	//@author A0128435E
	private boolean isSmaller(String newDateString, String compareDateString) throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		Date newDate = dateFormat.parse(newDateString);
		Date compareDate = dateFormat.parse(compareDateString);
		
		if (!newDate.after(compareDate)) {
			return true;
		}
		
		return false;
	}
	
	//@author A0128435E
	private boolean isMonthValue(String date) {
		int amountOfZero = 0;
		for (int i = 0; i < date.length(); i++) {
			if (date.charAt(i) == '0') {
				amountOfZero++;
			}
		}
		if (amountOfZero >= 4) {
			return true;
		} else {
			return false;
		}
	}
	
	//@author A0128435E
	private int getMonth(String date) {
		int dateInt = Integer.parseInt(date);
		int month = (dateInt/100) % 100;
		return month;
	}
	
	//@author A0098735M
	private String printError(Exception e) {
		
		if (e.getMessage() != null) {
			return e.getMessage();
		}
		
		if (e instanceof IllegalArgumentException) {
			return "Incorrect argument given.";
		} else if (e instanceof EmptyInputException) {
			return ToDoManager.MESSAGE_ERROR_EMPTY_INPUT;
		} else {
			return ToDoManager.MESSAGE_ERROR_GENERIC;
		}
	}
	
} 