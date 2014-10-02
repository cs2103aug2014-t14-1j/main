package todo_manager;

public class Logic {
	
	//UI module will call this method 
	public static void actOnUserInput(String userInput){
		ToDoManager.Executable exe;
		try {
			exe = Interpreter.parseCommand(userInput);
			memoriseActionForUndo(exe);
			execute(exe);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void execute(ToDoManager.Executable task){
		//TODO
	}
	
	public static void executeAdd(ToDoManager.Executable task){
		//TODO
	}

	public static void executeDelete(ToDoManager.Executable task){
		//TODO
	}
	
	public static void executeClear(ToDoManager.Executable task){
		//TODO
	}
	
	public static void executeEdit(ToDoManager.Executable task){
		//TODO
	}
	
	public static void memoriseActionForUndo(ToDoManager.Executable task){
		//TODO
	}
	
	public static void executeUndo(ToDoManager.Executable task){
		//TODO
	}
	
	public static void executeSearch(ToDoManager.Executable task){
		//TODO
	}
	
	public static void executeDisplay(ToDoManager.Executable task){
		//TODO
	}
	
	public static void executeDone(ToDoManager.Executable task){
		//TODO
	}
}
