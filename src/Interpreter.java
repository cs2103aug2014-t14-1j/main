package todo_manager;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import todo_manager.ToDoManager.CommandType;
import todo_manager.ToDoManager.EmptyInputException;

/**
 * Summary of the internal state of the Executable object that is returned
 * (If variable not mentioned, defaults to default chosen in Executable class (current default is null))
 * 
 *  /add : info filled, 
 *  /add /start /by : info filled, startingDate is /start <date>, endingDate is /by <date>
 *  /add /by : info filled, endingDate is /by date
 *  /add /on : info filled, startingDate and endingDate are equal to /on date
 * 
 *  /delete <int displayListIndex> : index given in exe.getdisplayIndex
 *  
 *  /undo : no extra info
 *  /clear : no extra info
 *  
 *  /edit <int displaylist index> <new task name> : new name stored under exe info
 *  /edit <int displaylist index> /from <new date> /to <new date2> : dates stored in startingDate and endingDate
 *  /edit <int displaylist index> /by <new date> : date stored in endingDate
 *  /edit <int displaylist index> /on <new date> : date stored in startingDate and endingDate 
 *  
 *  /display : command set to display, no other info will be given
 *  
 *  /search today : startingDate = endingDate = today's date
 *  /search <date> : startingDate = endingDate = <date>
 *  /search /from <date> : startingDate = <date>
 *  /search /by <date> : endingDate = <date>
 *  /search <keyword> : info is the string of keyword or keywords as given by the user
 *  /search done : doneness = true
 *  /search undone : doneness = false
 *  Note that Doneness will be set to null if doneness is not being searched for. False or true if it is being searched for.
 *  doneness defaults to false for all other operations, like add or delete.
 *  
 *  /mark <date> : startingDate and endingDate are equal to given date
 *  /mark <keywords> : info is filled with String of all keywords 
 *  
 *
 */


public class Interpreter {
	
	private static final boolean DEBUG = false;
	
	private static final String EMPTY_STRING = "";
	
	//TODO : make one of this in ToDoManager and have all classes call it
	private static final String DATE_FORMAT = "ddMMyy"; 
	
	private static ArrayList<Integer> index = new ArrayList<Integer>();
	
	public Interpreter() {
	}

	public static Executable parseCommand(String s) throws Exception{
		
		s = s.trim();
		if (s.equals(EMPTY_STRING)){
			throw new EmptyInputException();
		}
		
		String[] words = s.split(" ");
		String cmdWord = words[0].toLowerCase();
		
		Executable exe;
		
		switch (cmdWord) {
			case "/add" :
				exe = processAdd(words);
				break;
				
			case "/delete" :
				exe = processDelete(words);
				break;
				
			case "/clear" :
				exe = processClear(words);
				break;
				
			case "/edit" :
				exe = processEdit(words);
				break;
				
			case "/undo" :
				exe = processUndo(words);
				break;
				
			case "/search" :
				exe = processSearch(s);
				break;
				
			case "/display" :
				exe = processDisplay(words);
				break;
				
			case "/mark" :
				exe = processMark(words);
				break;
				
			case "/sort" :
				exe = new Executable(CommandType.CMD_SORT);
				break;
			case "/exit":
				exe = new Executable(CommandType.CMD_EXIT);
				break;
			default : 
				throw new IllegalArgumentException();
		}
		
		if (DEBUG) {
			printExe(exe); // for debugging, to view the contents of executable
		}
		
		return exe;
	}

	private static Executable processAdd(String[] words) throws Exception {
		
		Executable exe = new Executable(CommandType.CMD_ADD);
	
		String word;
		boolean addBasic = true;
		
		if (doesNotHaveExtraText(words)){
			throw new EmptyInputException();
		}
		
		//linearly read the words, stop when you find a keyword
		for (int i = 1; i < words.length; i++) {
			word = words[i];
			if (word.equals("/start")) {
				processAddFrom(exe, words, i);
				addBasic = false;
				break;
			} else if (word.equals("/on")) {
				processAddOn(exe, words, i);
				addBasic = false;
				break;
			} else if (word.equals("/by")) {
				processAddBy(exe, words, i);
				addBasic = false;
				break;
			}
		}
		
		if (addBasic) {
			processAddBasic(exe, words);
		}
		
		return exe;
	}

	private static void processAddBasic(Executable exe, String[] words) {
		exe.setInfo(recombine(words, 1, words.length));
	}

	private static void processAddBy(Executable exe, String[] words, 
			                         int i) throws IllegalArgumentException {
		exe.setInfo(recombine(words, 1, i));
		
		if (words.length == i + 1){ // nothing after keyword
			throw new IllegalArgumentException();
		} 
		
		String date = recombine(words, i+1, words.length);
		//TODO valid check on date
		exe.setEndingDate(date);
	}

	private static void processAddOn(Executable exe, String[] words,
			                         int i) throws IllegalArgumentException {
		exe.setInfo(recombine(words, 1, i));
		
		if (words.length == i + 1){ // nothing after keyword
			throw new IllegalArgumentException();
		} 
		
		String date = recombine(words, i+1, words.length);
		//TODO valid check on date
		exe.setStartingDate(date); 
		exe.setEndingDate(date);
	}

	private static void processAddFrom(Executable exe, String[] words, 
			                           int i) throws IllegalArgumentException {
		exe.setInfo(recombine(words, 1, i));
		
		if (words.length <= i + 3){ // nothing after keyword /from
			throw new IllegalArgumentException();
		} 
		
		int j;
		String word;
		for (j = i + 1; j < words.length; j++) { //look for keyword /to
			word = words[j];
			if (word.equals("/by")){
				break;
			}
		}
		
		if (words.length == j + 1){ // nothing after /by
			throw new IllegalArgumentException();
		} else{
			exe.setStartingDate(recombine(words, i + 1, j));
			exe.setEndingDate(recombine(words, j + 1, words.length));
		}
	}

	private static Executable processDelete(String[] words) throws IllegalArgumentException {
		Executable exe = new Executable(CommandType.CMD_DELETE);
		index = new ArrayList<Integer>();
		if (doesNotHaveExtraText(words)) { // no identifiers on what to delete
			throw new IllegalArgumentException();
		}
		for(int i = 1; i < words.length; i++){
			index.add(Integer.parseInt( words[i] ));
		}
		exe.setDisplayIndex(index);
		return exe;
	}

	private static Executable processClear(String[] words) {
		return new Executable(CommandType.CMD_CLEAR);
	}
	
	private static Executable processEdit(String[] words) throws IllegalArgumentException {
		Executable exe = new Executable(CommandType.CMD_EDIT);
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		if (words.length <= 2){ // not enough info to edit
			throw new IllegalArgumentException();
		}
		
		index.add(Integer.parseInt(words[1]));
		
		exe.setDisplayIndex(index);
		
		if (words[2].equals("/by")) { // edit date to "by" format
			exe.setEndingDate(words[3]);
			
		} else if (words[2].equals("/on")) { // edit date to "on" format
			exe.setStartingDate(words[3]);
			exe.setEndingDate(words[3]);
			
		} else if (words[2].equals("/from") && words[4].equals("/to")) { // edit date to "from-to" format
			if (words.length < 6 || ! words[4].equals("/to")){ //incorrect or insufficient commands from user
				throw new IllegalArgumentException();
			}
			exe.setStartingDate(words[3]);
			exe.setEndingDate(words[5]);
			
		} else { // edit entry name
			String keywords = recombine(words, 2, words.length);
			exe.setInfo(keywords);
		}
		
		return exe;
	}

	private static Executable processUndo(String[] words) {
		return new Executable(CommandType.CMD_UNDO);
	}

	private static Executable processSearch(String userInput) throws IllegalArgumentException {
		/*
		
		Executable exe = new Executable(CommandType.CMD_SEARCH);
		
		//set doneness null so that can differentiate between 
		//search done (true), search undone (false), and others (null)
		exe.setDoneness(null);
		
		if (doesNotHaveExtraText(words)) { // no search keywords
			throw new IllegalArgumentException(); //TODO refine exception to be more informative
			
		} else if (words[1].equals("today")){   //search today
			DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			Date date = new Date();
			exe.setStartingDate(dateFormat.format(date));
			exe.setEndingDate(dateFormat.format(date));
			
		} else if (isDate(words[1])) { //search for one date
			exe.setStartingDate(words[1]);
			exe.setEndingDate(words[1]);
			
		} else if (words[1].equals("/from")) { //search for entries after a particular date
			if (words.length < 3 || !isDate(words[2])){ 
				// no date or invalid date
				throw new IllegalArgumentException();
			}
			exe.setStartingDate(words[2]);
		} else if (words[1].equals("/by")) { //search for entries before a particular date
			if (words.length < 3 || !isDate(words[2])){ 
				// no date or invalid date
				throw new IllegalArgumentException();
			}
			exe.setEndingDate(words[2]);
			
		} else if (words[1].equals("done")) { 
			//search for entries that are marked done
			exe.setDoneness(true);
			
		} else if (words[1].equals("undone")) { 
			//search for entries that are marked undone
			exe.setDoneness(false);
			
		} else { //searching for some keywords
			String extraWords = recombine(words, 1, words.length);
			exe.setInfo(extraWords);
		}
		
		return exe;
		*/
		
		
		Executable exe = new Executable((CommandType.CMD_SEARCH));
		userInput = userInput.trim();
		String searchContent = userInput.substring(7).trim();
		if(searchContent.compareTo("") == 0){
			throw new IllegalArgumentException();
		}
		else{
			exe.setInfo(searchContent);
		}
		return exe;
	}
	
	private static Executable processDisplay(String[] words){
		Executable exe = new Executable(CommandType.CMD_DISPLAY);
		return exe;
	}

	private static Executable processMark(String[] words) throws IllegalArgumentException {
		Executable exe = new Executable(CommandType.CMD_DONE);
		if (doesNotHaveExtraText(words)){ // no identifiers
			throw new IllegalArgumentException();
		} else {  //has more words
			String extraWords = recombine(words, 1, words.length);
			
			//if extra info is a date
			if (isDate(extraWords)) {
				exe.setStartingDate(extraWords); 
				exe.setEndingDate(extraWords);
			} else { // else, extra info is keywords
				exe.setInfo(extraWords);
			}
		}
		return exe;
	}
	
	private static boolean doesNotHaveExtraText(String[] words){
		return (words.length <= 1);
	}
	
	/**
	 * Combines a string array back into a readable sentence, with SPACES
	 * Output INCLUDES start and EXCLUDES end
	 * @param String[] words 
	 * @param int startIndex	index from which to start, INCLUSIVE
	 * @param int endIndex		index to end at, EXCLUSIVE
	 * @return String sentence
	 */
	private static String recombine(String[] words, int startIndex, int endIndex){
		String line = "";
		for (int i = startIndex; i < endIndex; i++) {
			line += words[i]+" ";
		}
		return line.trim();
	}
	
	private static boolean isDate(String date) {
		return ValidationCheck.isValidDate(date);
	}
	
	private static void printExe(Executable exe){
		String out = "---Executable Begin---\n" + "Command : ";
		out += exe.getCommand() + "\n" + "Name : ";
		out += exe.getInfo() + "\n" + "StartingDate : ";
		out += exe.getStartingDate() + "\n" + "EndingDate : ";
		out += exe.getEndingDate() + "\n" + "Doneness : ";
		out += exe.getDoneness() + "\n" + "displayIndex : ";
		out += exe.getDisplayIndex() + "\n";
		out += "---Executable End---\n\n";
		
		System.out.println(out);
	}

}
