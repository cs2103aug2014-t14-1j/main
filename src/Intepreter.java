
public class Intepreter {
	
	public static ToDoManager.Executable parseCommand(String s){
		String[] words = s.split(" ");
		String cmdWord = words[0];
		ToDoManager.CommandType cmd;
//		ToDoManager.Executable exe = new ToDoManager.Executable()
		switch (cmdWord) {
			case "/add" :
				cmd = ToDoManager.CommandType.CMD_ADD;
				
			case "/delete" :
				cmd = ToDoManager.CommandType.CMD_DELETE;
				
			case "/clear" :
				cmd = ToDoManager.CommandType.CMD_CLEAR;
			
			case "/edit" :
				cmd = ToDoManager.CommandType.CMD_EDIT;
			
			case "/undo" :
				cmd = ToDoManager.CommandType.CMD_UNDO;
			
			case "/search" :
				cmd = ToDoManager.CommandType.CMD_SEARCH;
			
			case "/display" :
				cmd = ToDoManager.CommandType.CMD_DISPLAY;
			
			case "/mark" :
				cmd = ToDoManager.CommandType.CMD_DONE;
			
		}
		
		return null;
	}

}
