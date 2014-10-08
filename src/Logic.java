package todo_manager;

import java.util.LinkedList;
import java.util.ListIterator;

import todo_manager.ToDoManager.CommandType;

public class Logic {
	Storage storage;
	
	private static LinkedList<Entry> entryList = new LinkedList<Entry>();
	private static LinkedList<Executable> exeList = new LinkedList<Executable>();
	
	
	void setup(ToDoManager toDoManager){
		storage = toDoManager.storage;
		entryList = readFromStorage();
	}
	
	//UI module will call this method 
	public void actOnUserInput(String userInput){
		
		Executable exe;
		try {
			Interpreter interpreter = new Interpreter();
			exe = interpreter.parseCommand(userInput);
			memoriseActionForUndo(exe);
			execute(exe);
		} catch (Exception e) {
//			e.printStackTrace();
			UserInterface.showToUser(ToDoManager.MESSAGE_GENERIC_ERROR);
		}
	}
	
	public void execute(Executable task){
		//TODO
		CommandType command = task.getCommand();
		
		switch (command) {
		case CMD_ADD: executeAdd(task);
			break;
		case CMD_CLEAR:
			break;
		case CMD_DELETE: executeDelete(task);
			break;
		case CMD_DISPLAY: executeDisplay(task);
			break;
		case CMD_DONE:
			break;
		case CMD_EDIT:
			break;
		case CMD_SEARCH:
			break;
		case CMD_UNDO:
			break;
		default:
			break;
		}
			
	}
	
	private void executeAdd(Executable task){
		
		Entry entry = new Entry();
		entry.setName(task.getInfo());
		entryList.add(entry);
		writeToStorage();
		
	}

	private void executeDelete(Executable task){
		
		String str = task.getInfo();
		int toDelete = -1;
		
		for (int i = 0; i < entryList.size(); i++) {
            if(entryList.get(i).getName().equals(str)){
            	toDelete = i;
            	break;
            }
        }
		
		if(toDelete != -1)
		{
			entryList.remove(toDelete);
			writeToStorage();
		}
	}
	
	private void executeClear(Executable task){
		//TODO
	}
	
	private void executeEdit(Executable task){
		//TODO
	}
	
	private void memoriseActionForUndo(Executable task){
		//TODO
		exeList.add(task);
	}
	
	private void executeUndo(Executable task){
		//TODO
	}
	
	private void executeSearch(Executable task){
		//TODO
	}
	
	private void executeDisplay(Executable task){
		//TODO
		//if(task.getInfo().equals("all")){
			
		//ListIterator<Entry> listIterator = entryList.listIterator();
		int count = 1;
		for (Entry e : entryList) {
			UserInterface.showToUser(count+". "+e.getName());
		    count++;
		}
	}
	
	private void executeDone(Executable task){
		//TODO
	}
	
	private void writeToStorage(){
		storage.writeFile(entryList);
	}
	
	private LinkedList<Entry> readFromStorage(){
		return storage.readFile();
	}
}
