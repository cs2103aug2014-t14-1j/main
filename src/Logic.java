package todo_manager;

import java.util.Collections;
import java.util.LinkedList;

import todo_manager.ToDoManager.CommandType;
import todo_manager.ToDoManager.EmptyInputException;

public class Logic {
	Storage storage;
	
	public static LinkedList<Entry> entryList = new LinkedList<Entry>();
	private static LinkedList<Entry> preventryList;
	private static LinkedList<Entry> displayList = new LinkedList<Entry>();
	private static LinkedList<Executable> exeList = new LinkedList<Executable>();
	
	private static Logging logObj = Logging.getInstance();

	

	
	void setup(ToDoManager toDoManager){
		storage = toDoManager.storage;
		entryList = readFromStorage();
	}
	
	//UI module will call this method 
	public void actOnUserInput(String userInput){
		
		Executable exe;
		try {
			exe = Interpreter.parseCommand(userInput);
			execute(exe);
			memoriseActionForUndo(exe);
		} catch (EmptyInputException e) {
			UserInterface.showToUser(ToDoManager.MESSAGE_ERROR_EMPTY_INPUT);
		} catch (IllegalArgumentException e) {
			UserInterface.showToUser(ToDoManager.MESSAGE_WRONG_INPUT_FORMAT);
		} catch (Exception e) {
			UserInterface.showToUser(ToDoManager.MESSAGE_ERROR_GENERIC);
		}
	}
	
	public void execute(Executable task){
		

		CommandType command = task.getCommand();
		
		switch (command) {
		case CMD_ADD: 
			executeAdd(task);
			//executeDisplay(entryList);
			break;
		case CMD_CLEAR: 
			executeClear(task);
			//executeDisplay(entryList);
			break;
		case CMD_DELETE: 
			executeDelete(task);
			//executeDisplay(entryList);
			break;
		case CMD_DISPLAY: 
			executeDisplay(entryList);
			break;
		case CMD_DONE: 
			executeDone(task);
			//executeDisplay(entryList);
			break;
		case CMD_EDIT: 
			executeEdit(task);
			executeDisplay(entryList);
			break;
		case CMD_SEARCH: 
			executeSearch(task);
			break;
		case CMD_UNDO: 
			executeUndo(task);
			//executeDisplay(entryList);
			break;
		case CMD_SORT: 
			executeSort(task);
			executeDisplay(displayList);
		default:
			break;
		}
			
	}
	
	private void executeSort(Executable task) {
		// To sort the task by Date line
		Collections.sort(entryList);
	}

	private void executeAdd(Executable task){

		logObj.writeToLoggingFile("Trying to add");
		Entry entry = new Entry();
		entry.setName(task.getInfo());
		entry.setStartingDate(task.getStartingDate());
		entry.setEndingDate(task.getEndingDate());
		entryList.add(entry);
		writeToStorage();
		logObj.writeToLoggingFile("Done adding task");
		
	}

	private void executeDelete(Executable task){
		int index = task.getDisplayIndex() - 1;
		Entry removedEntry = displayList.remove(index);
		if (! displayList.equals(entryList)){ 
			//if they are the same then no need to remove again
			entryList.remove(removedEntry);
		}
		writeToStorage();
	}
	
	private void executeClear(Executable task){
		//TODO
		preventryList = new LinkedList<Entry>(entryList);
		entryList.clear();
	}
	
	private void executeEdit(Executable task){
//		//edit the key work
//		
//		
//		String str = task.getInfo();
//		int num = Integer.parseInt(task.getInfo().substring(0,1)); // cant assume that the number is single digit
//		
//		String preStr = entryList.get(num-1).getName();
//		
//		task.setPreStr(preStr);
//		
//		entryList.get(num-1).setName(str.substring(1));
		
		try{
			int displayIndex = task.getDisplayIndex() - 1;
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
	
	private void memoriseActionForUndo(Executable task){
		//TODO
		exeList.add(task);
	}
	
	private void executeUndo(Executable task){
		//Undo last operation
		//exeList.removeLast();
		CommandType command = exeList.getLast().getCommand();
		Executable undotask = exeList.getLast();
		exeList.removeLast();
		
		switch (command) {
		case CMD_ADD: executeDelete(undotask);
					 
					  break;
		case CMD_CLEAR: entryList = preventryList;
						
						break;
		case CMD_DELETE: executeAdd(undotask);
						 
						 break;
		case CMD_DONE: executeUndone(undotask);
					   
					   break;
		case CMD_EDIT: executeUnEdit(undotask);
			break;
		case CMD_SORT: 
			break;
		default:
			System.out.println("cannot be undo");
			break;
		}
	}
	
	private void executeUnEdit(Executable undotask) {
		
		int num = Integer.parseInt(undotask.getInfo().substring(0,1));
		
		entryList.get(num-1).setName(undotask.getPreStr());
		
		
	}

	private void executeUndone(Executable task) {
		//un done 
		String str = task.getInfo();
		for (int i = 0; i < entryList.size(); i++) {
            if(entryList.get(i).getName().equals(str)){
            	entryList.get(i).setDoneness(false);
            	break;
            }
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
		executeDisplay(searchResult);
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
