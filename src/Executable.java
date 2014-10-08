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

import todo_manager.ToDoManager.CommandType;

public class Executable {
	
	private CommandType command = null;
	
	private String info = null;
	private String startingDate = null;
	private String endingDate = null;
	private String startingTime = null;
	private String endingTime = null;
	private Boolean doneness = false;
	
	public Executable(CommandType commandInput){
		command = commandInput;
	}
	
	public Executable(CommandType commandInput, String infoInput){
		info = infoInput;
		command = commandInput;
	}
	
	public Executable(CommandType commandInput, String infoInput, String startInput, String endInput){
		info = infoInput;
		command = commandInput;
		startingTime = startInput;
		endingTime = endInput;
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
}
