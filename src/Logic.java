package todo_manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import todo_manager.ToDoManager.CommandType;
import todo_manager.ToDoManager.EmptyInputException;

public class Logic {
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
	public LinkedList<Entry> actOnUserInput(String userInput){
		
		Executable exe;
		try {
			
			exe = Interpreter.parseCommand(userInput);
			execute(exe);
			
		} catch (EmptyInputException e) {
			UserInterface.showToUser(ToDoManager.MESSAGE_ERROR_EMPTY_INPUT);
		} catch (IllegalArgumentException e) {
			UserInterface.showToUser(ToDoManager.MESSAGE_WRONG_INPUT_FORMAT);
		} catch (Exception e) {
			UserInterface.showToUser(ToDoManager.MESSAGE_ERROR_GENERIC);
		}
		
		return displayList;
	}
	
	public void execute(Executable task){
		

		CommandType command = task.getCommand();
		
		switch (command) {
		case CMD_ADD: 
			preList.add(new LinkedList<Entry>(entryList));
			executeAdd(task);
			executeDisplay(entryList);
			break;
		case CMD_CLEAR: 
			preList.add(new LinkedList<Entry>(entryList));
			executeClear(task);
			executeDisplay(entryList);
			break;
		case CMD_DELETE: 
			preList.add(new LinkedList<Entry>(entryList));
			executeDelete(task);
			executeDisplay(entryList);
			break;
		case CMD_DISPLAY: 
			preList.add(new LinkedList<Entry>(entryList));
			executeDisplay(entryList);
			break;
		case CMD_DONE: 
			preList.add(new LinkedList<Entry>(entryList));
			executeDone(task);
			executeDisplay(entryList);
			break;
		case CMD_EDIT: 
			preList.add(new LinkedList<Entry>(entryList));
			executeEdit(task);
			executeDisplay(entryList);
			break;
		case CMD_SEARCH: 
			preList.add(new LinkedList<Entry>(entryList));
			executeSearch(task);
			break;
		case CMD_UNDO: 
			executeUndo();
			executeDisplay(entryList);
			break;
		case CMD_SORT: 
			preList.add(new LinkedList<Entry>(entryList));
			executeSort();
			executeDisplay(entryList);
			
		case CMD_EXIT:
			preList.clear();
			System.exit(0);
		default:
			break;
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


	public LinkedList<Entry> executeSearch(Executable task){
		/*
		String keyword;
		boolean doneness;
		displayList.clear;
		
		if(task.getInfo() != null){
			keyword = task.getInfo().toLowerCase();
			for (int i = 0; i < entryList.size(); i++) {
	            if(entryList.get(i).getName().toLowerCase().contains(keyword)){
	            	displayList.add(entryList.get(i));
	            }
	        }
		}
		else if(task.getStartingDate() != null){
			keyword = task.getStartingDate();
			for (int i = 0; i < entryList.size(); i++) {
	            if(entryList.get(i).getStartingDate().contains(keyword)){
	            	displayList.add(entryList.get(i));
	            }
	        }
		}
		else if(task.getEndingDate() != null){
			keyword = task.getEndingDate();
			for (int i = 0; i < entryList.size(); i++) {
	            if(entryList.get(i).getEndingDate().contains(keyword)){
	            	displayList.add(entryList.get(i));
	            }
	        }
		}
		else if(task.getStartingTime() != null){
			keyword = task.getStartingTime();
			for (int i = 0; i < entryList.size(); i++) {
	            if(entryList.get(i).getStartingTime().contains(keyword)){
	            	displayList.add(entryList.get(i));
	            }
	        }
		}
		else if(task.getEndingTime() != null){
			keyword = task.getEndingTime();
			for (int i = 0; i < entryList.size(); i++) {
	            if(entryList.get(i).getEndingTime().contains(keyword)){
	            	displayList.add(entryList.get(i));
	            }
	        }
		}
		else{
			doneness = task.getDoneness();
			for (int i = 0; i < entryList.size(); i++) {
	            if(entryList.get(i).getDoneness() == doneness){
	            	displayList.add(entryList.get(i));
	            }
	        }
		}
		*/
		String searchContent = task.getInfo().trim();
		ValidationCheck validCheck = new ValidationCheck();
		boolean isDate = validCheck.isValidDate(searchContent);
		
		
		LinkedList<Entry> searchResult = new LinkedList<Entry>();
		
		if(isDate){
			for(Entry entry: entryList){
				if(entry.getStartingDate() == searchContent ||entry.getEndingDate() == searchContent ){
					searchResult.add(entry);
				}
			}
		}
		else{
			searchContent = searchContent.toLowerCase();
			for(Entry entry: entryList){
				String temp = entry.getName().toLowerCase();
				if(temp.contains(searchContent)){
					searchResult.add(entry);
				}
			}
		}
		
		displayList = searchResult;
		executeDisplay(displayList);
		return searchResult;
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
			
			if (e.getEndingDate() != null && !e.getEndingDate().equals("")){
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
		String str = task.getInfo();
		for (int i = 0; i < entryList.size(); i++) {
            if(entryList.get(i).getName().equals(str)){
            	entryList.get(i).setDoneness(true);
            	break;
            }
        }
	}
	
	private void writeToStorage(){
		storage.writeFile(entryList);
	}
	
	private LinkedList<Entry> readFromStorage(){
		return storage.readFile();
	}
}
