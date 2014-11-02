package todo_manager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

import todo_manager.ToDoManager.CommandType;
import todo_manager.ToDoManager.EmptyInputException;

public class Logic {
	
	/*
	 * Help-related messages
	 */
	private static final String HELP_NO_KEYWORD = 
			  "Type \"help <command>\" to get help for that particular topic.\n"
			+ "List of topics : \n"
			+ "/add      /display\n"
			+ "/delete   /clear\n"
			+ "/edit     /undo\n"
			+ "/mark     /search\n"
			+ "/sort     /exit\n"
			+ "date\n";
	
	private static final String HELP_INVALID_KEYWORD = "Invalid help topic given.\n";
	private static final String HELP_ADD = "Format of add command : /add <task name>\n" +
										   "The following can also be added after the basic add command : \n" +
										   "/on <date>\n" +
										   "/by <date>\n" +
										   "/start <date> /by <date>\n";
	private static final String HELP_DISPLAY = "The /display command will list all saved tasks on the screen.\n";
	private static final String HELP_DELETE = "Format of delete command : \n" +
											  "/delete <index no.> \n" +
											  "This deletes the entry with that index in the most recently shown list.\n" +
											  "Multiple indexes seperated by spaces also accepted, eg. : \n" +
											  "/delete 1 4 5\n";
	private static final String HELP_CLEAR = "\"/clear\" deletes all saved tasks.\n";
	private static final String HELP_EDIT = "Format for edit command : \n" +
											"/edit <index no.> <new task name>\n" + 
											"/edit <index no.> /on <new date>\n" + 
											"/edit <index no.> /by <new date>\n" + 
											"/edit <index no.> /start <new date> /by <new date2>\n" +
											"Note that <index no.> refers to the numbering in the most recently displayed list.\n";
	private static final String HELP_UNDO = "The /undo command reverses the most recent change made.\n";
	private static final String HELP_MARK = "This marks the item as done. Format of mark : \n" +
											"/mark <keyword>\n" +
											"/mark <index no.>\n" + 
											"Note that <index no.> refers to the numbering in the most recently displayed list.\n" +
											"<index no.> can be multiple numbers separated by spaces, to mark several items at one go.\n";
	private static final String HELP_SEARCH = "Format for search : \n" +
											  "/search today\n" +
											  "/search <done or undone>\n" +
											  "/search <date>\n" +
											  "/search <keyword>\n";
	private static final String HELP_SORT = "The /sort command arranges the tasks chronologically.\n";
	private static final String HELP_EXIT = "The /exit command shuts down ToDoManager.\n";

	Storage storage;
	
	private static LinkedList<LinkedList<Entry>> preList = new LinkedList<LinkedList<Entry>>(); // preState
	
	private static Logic instance = null;
	
	public static LinkedList<Entry> entryList = new LinkedList<Entry>();
	private static LinkedList<Entry> displayList = new LinkedList<Entry>();
	
	private static Logging logObj = Logging.getInstance();

	private Logic(){
	}
	
	public static Logic getInstance(){
		if(instance == null){
			instance = new Logic();
		}
		return instance;
	}
	
	public static LinkedList<Entry> getEntryList() {
		return entryList;
	}

	void setupGUI(ToDoManagerGUI toDoManagerGUI){
		storage = toDoManagerGUI.storage;
		entryList = readFromStorage();
	}
	
	void setup(ToDoManager toDoManager){
		storage = toDoManager.storage;
		entryList = readFromStorage();
	}
	
	//UI module will call this method 
	public Object actOnUserInput(String userInput){
		
		Executable exe;
		Object displayObj;
		try {
			
			exe = Interpreter.parseCommand(userInput);
			displayObj = execute(exe);
			
		} catch (EmptyInputException e) {
			return ToDoManager.MESSAGE_ERROR_EMPTY_INPUT;
		} catch (IllegalArgumentException e) {
			 return ToDoManager.MESSAGE_WRONG_INPUT_FORMAT;
		} catch (Exception e) {
			return ToDoManager.MESSAGE_ERROR_GENERIC;
		}
		
		return displayObj;
	}
	
	public Object execute(Executable task){
		

		CommandType command = task.getCommand();
		
		switch (command) {
		case CMD_ADD: 
			preList.add(new LinkedList<Entry>(entryList));
			executeAdd(task);
			executeDisplay(entryList);
			return entryList;
		case CMD_CLEAR: 
			preList.add(new LinkedList<Entry>(entryList));
			executeClear(task);
			executeDisplay(entryList);
			return entryList;
		case CMD_DELETE: 
			preList.add(new LinkedList<Entry>(entryList));
			executeDelete(task);
			executeDisplay(entryList);
			return entryList;
		case CMD_DISPLAY: 
			preList.add(new LinkedList<Entry>(entryList));
			executeDisplay(entryList);
			return entryList;
		case CMD_DONE: 
			preList.add(new LinkedList<Entry>(entryList));
			executeDone(task);
			executeDisplay(entryList);
			return entryList;
		case CMD_EDIT: 
			preList.add(new LinkedList<Entry>(entryList));
			executeEdit(task);
			executeDisplay(entryList);
			return entryList;
		case CMD_SEARCH: 
			preList.add(new LinkedList<Entry>(entryList));
			executeSearch(task);
			executeDisplay(displayList);
			return displayList;
		case CMD_UNDO: 
			executeUndo();
			executeDisplay(entryList);
			return entryList;
		case CMD_SORT: 
			preList.add(new LinkedList<Entry>(entryList));
			executeSort();
			executeDisplay(entryList);
			return entryList;
		case CMD_HELP:
			String out = executeHelp(task);
			return out;
		case CMD_EXIT:
			preList.clear();
			System.exit(0);
			//return "exit";
		default:
			return ToDoManager.MESSAGE_ERROR_GENERIC;
		}
			
	}
	
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
			case "/display":
				UserInterface.showToUser(HELP_DISPLAY);
				return HELP_DISPLAY;
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
			case "/search":
				UserInterface.showToUser(HELP_SEARCH);
				return HELP_SEARCH;
			case "/sort":
				UserInterface.showToUser(HELP_SORT);
				return HELP_SORT;
			case "/exit":
				UserInterface.showToUser(HELP_EXIT);
				return HELP_EXIT;
			case "date format" :
				//TODO
				return "";
			default : 
				UserInterface.showToUser(HELP_INVALID_KEYWORD);
				UserInterface.showToUser(HELP_NO_KEYWORD);
				return HELP_INVALID_KEYWORD+"\n"+HELP_NO_KEYWORD;
		}
	}

	private void executeUndo() {
		
		if(preList.isEmpty()){
			System.out.println("Nothing to Undo!");
		}
		else{
			entryList = new LinkedList<Entry>(preList.getLast());
			preList.removeLast();
		}
			
	}

	private void executeSort() {
		// To sort the task by Date line
		Collections.sort(entryList);
		writeToStorage();
	}

	private void executeAdd(Executable task){

		logObj.writeToLoggingFile("Trying to add");
		
		Entry entry = new Entry();
		
		if(task.getInfo()!=null){
			entry.setName(task.getInfo());
		}
		if(task.getStartingDate()!=null){
			entry.setStartingDate(task.getStartingDate());
		}
		if(task.getEndingDate()!= null){
			entry.setEndingDate(task.getEndingDate());
		}else{
			entry.setEndingDate("999999");
		}
		
		if(task.getStartingTime()!= null){
			entry.setStartingTime(task.getStartingTime());
		}
		if(task.getEndingTime()!= null){
			entry.setEndingTime(task.getEndingTime());
		}
		
		entryList.add(entry);
		writeToStorage();
		
		logObj.writeToLoggingFile("Done adding task");
		
	}

	private void executeDelete(Executable task){
		ArrayList<Integer> index = task.getDisplayIndex();
		int removeIndex;
		for(int i = 0 ; i < index.size(); i++){
			removeIndex = index.get(i) - 1;
			
			if(i > 0){
				for(int j = 0; j < i; j++){
					if(index.get(j) < index.get(i))
						removeIndex--;
				}
			}
			
			Entry removedEntry = displayList.remove(removeIndex);
			if (! displayList.equals(entryList)){ 
				//if they are the same then no need to remove again
				entryList.remove(removedEntry);
			}
		}
		writeToStorage();
	}
	
	private void executeClear(Executable task){
		
		entryList.clear();
		writeToStorage();
	}
	
	private void executeEdit(Executable task){
		
		try{
			int displayIndex = task.getDisplayIndex().get(0) - 1;
			Entry entryToEdit = displayList.get(displayIndex);
			String oldDetail = "";
			
			if (task.getInfo() != null){ //edit name
				oldDetail += "/name " + entryToEdit.getName();
				entryToEdit.setName(task.getInfo());
			} 
			
			if (task.getStartingDate() != null){ //edit startingDate
				oldDetail += "startingDate " + entryToEdit.getStartingDate() + " ";
				entryToEdit.setStartingDate(task.getStartingDate());
			}
			
			if (task.getEndingDate() != null){//edit endingDate
				oldDetail += "endingDate " + entryToEdit.getEndingDate() + " ";
				entryToEdit.setEndingDate(task.getEndingDate());
			}
			
	    	task.setPreStr(oldDetail); //memorise previous state for undo
			writeToStorage();
		} catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}


	public void executeSearch(Executable task){
		
		String searchContent, searchContent1;
		String[] searchKeyword, searchName;
		int pos;
		boolean doneness;
		
		LinkedList<Entry> searchResult = new LinkedList<Entry>();
		
		if(task.getInfo() != null){
			searchKeyword = task.getInfo().trim().toLowerCase().split(" ");
			System.out.println();
			for(Entry entry: entryList){
				pos = 0;
				searchName = entry.getName().toLowerCase().split(" ");
				for(int i = 0; i < searchName.length; i++){
					searchContent = searchName[i];
					if(searchContent.equals(searchKeyword[pos]))
						pos++;
					if(pos == searchKeyword.length){
						searchResult.add(entry);
						break;
					}
				}
			}
		}
		else if(task.getStartingDate() != null && task.getEndingDate() != null){
			searchContent = task.getStartingDate();
			searchContent1 = task.getEndingDate();
			String startDate, endDate;
			for(Entry entry: entryList){
				startDate = entry.getStartingDate();
				endDate = entry.getEndingDate();
				if(startDate.equals(searchContent) && endDate.equals(searchContent1)){
					searchResult.add(entry);
				}
			}
		}
		else if(task.getStartingDate() != null){
			searchContent = task.getStartingDate();
			for(Entry entry: entryList){
				if(entry.getStartingDate().equals(searchContent)){
					searchResult.add(entry);
				}
			}
		}
		else if(task.getEndingDate() != null){
			searchContent = task.getEndingDate();
			for(Entry entry: entryList){
				if(entry.getEndingDate().equals(searchContent)){
					searchResult.add(entry);
				}
			}
		}
		else if(task.getDoneness() != null){
			doneness = task.getDoneness();
			for(Entry entry: entryList){
				if(entry.getDoneness() == doneness){
					searchResult.add(entry);
				}
			}
		}
		
		displayList = searchResult;
		executeDisplay(displayList);
	}

	
	private void executeDisplay(LinkedList<Entry> list){

		displayList = list;
		int count = 1;
		String entryString;
		for (Entry e : list) {
			entryString = count+". "+e.getName();
			if (e.getStartingDate() != null && !e.getStartingDate().equals("")){
				entryString += " start: " + e.getStartingDate();
			}
			
			if (e.getEndingDate() != null && !e.getEndingDate().equals("") && !e.getEndingDate().equals("999999")){
				entryString += " end: " + e.getEndingDate();
			}
			
			if(e.getDoneness() == true){
				entryString += " Done";
			}
		    count++;
		    UserInterface.showToUser(entryString);
		}
		if(list.size() == 0){
			System.out.println("no entry found!");
		}
	}
	
	private void executeDone(Executable task){
		//Done something
		ArrayList<Integer> index = task.getDisplayIndex();
		
		for(int i = 0 ; i < index.size(); i++){
			entryList.get(index.get(i)-1).setDoneness(true);
		}
		writeToStorage();
	
	}
	
	private void writeToStorage(){
		storage.writeFile(entryList);
	}
	
	private LinkedList<Entry> readFromStorage(){
		return storage.readFile();
	}
	
	private boolean isGreaterDate(String newDateString, String compareDateString) throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
		Date newDate = dateFormat.parse(newDateString);
		Date compareDate = dateFormat.parse(compareDateString);
		
		if(newDate.after(compareDate)){
			return true;
		}
		
		return false;
	}
}
