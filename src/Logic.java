package todo_manager;

import java.util.LinkedList;
import java.util.ListIterator;

import todo_manager.ToDoManager.CommandType;

public class Logic {
	
	private static LinkedList<Entry> entryList = new LinkedList<Entry>();
	private static LinkedList<Executable> exeList = new LinkedList<Executable>();
	
	public Logic() {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void execute(Executable task){
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
	
	public static void executeAdd(Executable task){
		
		Entry entry = new Entry();
		entry.setName(task.getInfo());
		entryList.add(entry);
		
	}

	public static void executeDelete(Executable task){
		
		String str = task.getInfo();
		int toDelete = -1;
		
		for (int i = 0; i < entryList.size(); i++) {
            if(entryList.get(i).getName().equals(str)){
            	toDelete = i;
            }
        }
		
		if(toDelete != -1)
		{
			entryList.remove(toDelete);
		}
	}
	
	public static void executeClear(Executable task){
		//TODO
	}
	
	public static void executeEdit(Executable task){
		//TODO
	}
	
	public static void memoriseActionForUndo(Executable task){
		//TODO
		exeList.add(task);
	}
	
	public static void executeUndo(Executable task){
		//TODO
	}
	
	public static void executeSearch(Executable task){
		//TODO
	}
	
	public static void executeDisplay(Executable task){
		//TODO
		//if(task.getInfo().equals("all")){
			
		 ListIterator<Entry> listIterator = entryList.listIterator();
	        while (listIterator.hasNext()) {
	            System.out.println(listIterator.next().getName());
	        }
	        
	//	}
	}
	
	public static void executeDone(Executable task){
		//TODO
	}
}
