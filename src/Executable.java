package todo_manager;

/**
 * Executable object that encapsulates everything that Logic needs to execute the command
 * Properties cannot be changed after instantiation
 * @param CommandType command		Enum type that is used to determine what is the command to run
 * @param String info 				String containing task description or other input that doesnt fit into DateTime or CommandType
 * @param DateTime dateTime 		Date object used to capture date and time of task
 * @param DateTime dateTimeEnd 		Date object representing date at which the task ends
 * @author Zeng Qingtao
 *
 */

import java.util.ArrayList;

import todo_manager.ToDoManager.CommandType;

public class Executable {
	
	private CommandType command = null;
	
	private String info = null;
	private String startingDate = null;
	private String endingDate = null;
	private String startingTime = null;
	private String endingTime = null;
	private Boolean doneness = null;
	private ArrayList<Integer> displayIndex = new ArrayList<Integer>();
	private String preStr;
	

	public Executable(CommandType command){
		this.command = command;
	}
	
	public Executable(CommandType command, String info){
		this.info = info;
		this.command = command;
	}
	
	public Executable(CommandType command, String info, String start, String end){
		this.info = info;
		this.command = command;
		this.startingTime = start;
		this.endingTime = end;
	}

	public CommandType getCommand() {
		return command;
	}

	public void setCommand(CommandType command) {
		this.command = command;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(String startingDate) {
		this.startingDate = startingDate;
	}

	public String getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(String endingDate) {
		this.endingDate = endingDate;
	}

	public String getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(String startingTime) {
		this.startingTime = startingTime;
	}

	public String getEndingTime() {
		return endingTime;
	}

	public void setEndingTime(String endingTime) {
		this.endingTime = endingTime;
	}

	public Boolean getDoneness() {
		return doneness;
	}

	public void setDoneness(Boolean doneness) {
		this.doneness = doneness;
	}
	
	public ArrayList<Integer> getDisplayIndex() {
		return displayIndex;
	}

	public void setDisplayIndex(ArrayList<Integer> displayIndex) {
		this.displayIndex = displayIndex;
	}
	
	public String getPreStr() {
		return preStr;
	}

	public void setPreStr(String preStr) {
		this.preStr = preStr;
	}
}
