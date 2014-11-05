package todo_manager;

import java.util.LinkedList;

import todo_manager.ToDoManager.CommandType;

public class Result {
	private boolean success;
	private String feedback;
	private String helpMessage;
	private CommandType commandType;
	private LinkedList<Entry> displayList;
	
	public Result(){
	}
	
	public void setSuccess(boolean isSuccess){
		success = isSuccess;
	}
	
	public void setFeedback(String feedbackInfo){
		feedback = feedbackInfo;
	}
	
	public void setDisplayList(LinkedList<Entry> list){
		displayList = list;
	}
	
	public void setHelpMessage(String help){
		helpMessage = help;
	}
	
	public void setCommandType(CommandType command){
		commandType = command;
	}
	
	public boolean getSuccess(){
		return success;
	}
	
	public String getFeedback(){
		return feedback;
	}
	
	public CommandType getCommandType(){
		return commandType;
	}
	
	public LinkedList<Entry> getDisplayList(){
		return displayList;
	}
	
	public String getHelpMessage(){
		return helpMessage;
	}
}
