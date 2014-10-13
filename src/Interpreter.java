package todo_manager;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import todo_manager.ToDoManager.CommandType;
import todo_manager.ToDoManager.EmptyInputException;
import todo_manager.ToDoManager.InvalidInputException;

/**
 * Summary of the internal state of the Executable object that is returned
 * (If variable not mentioned, defaults to default chosen in Executable class (current default is null))
 * 
 *  /add : info filled, 
 *  /add /from /to : info filled, startingDate is /from, endingDate is /to
 *  /add /by : info filled, endingDate is /by date
 *  /add /on : info filled, startingDate and endingDate are equal to /on date
 * 
 *  /delete <date> : startingDate and endingDate are equal to given date
 *  /delete <keywords> : info is filled with String of all keywords
 *  
 *  /undo : no extra info
 *  /clear : no extra info
 *  
 *  /edit <date> : startingDate and endingDate are equal to given date
 *  /edit <keywords> : info is filled with String of all keywords
 *  
 *  /search <date> : startingDate and endingDate are equal to given date
 *  /search <keywords> : info is filled with String of all keywords
 *  
 *  /display  : defaults to display all, info set to "all"
 *  /display all : info set to "all"
 *  /display today : startingDate and endingDate set to today's date
 *  /display <date> : startingDate and endingDate are equal to given date
 *  /display <keywords> : info is filled with String of all keywords 
 *  
 *  /mark <date> : startingDate and endingDate are equal to given date
 *  /mark <keywords> : info is filled with String of all keywords 
 *  
 * @author Qingtao
 *
 */


public class Interpreter {
	
	private static final String EMPTY_STRING = "";
	
	//TODO : make one of this in ToDoManager and have all classes call it
	private static final String DATE_FORMAT = "ddMMyy"; 
	
	public Interpreter() {
	}

	public Executable parseCommand(String s) throws Exception{
		
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
				exe = processSearch(words);
				break;
				
			case "/display" :
				//exe = processDisplay(words);
				exe = new Executable(CommandType.CMD_DISPLAY);
				break;
				
			case "/mark" :
				exe = processMark(words);
				break;
				
			case "/sort" :
				exe = new Executable(CommandType.CMD_SORT);
				break;
			default : 
				throw new InvalidInputException();			
		}
		
		return exe;
	}

	private Executable processAdd(String[] words) throws Exception {
		
		Executable exe = new Executable(CommandType.CMD_ADD);
	
		String word;
		boolean addBasic = true;
		
		if (doesNotHaveExtraText(words)){
			throw new EmptyInputException();
		}
		
		//linearly read the words, stop when you find a keyword
		for (int i = 1; i < words.length; i++) {
			word = words[i];
			if (word.equals("/from")) {
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

	private void processAddBasic(Executable exe, String[] words) {
		exe.setInfo(recombine(words, 1, words.length));
	}

	private static void processAddBy(Executable exe, String[] words, 
			                         int i) throws InvalidInputException {
		exe.setInfo(recombine(words, 1, i));
		
		if (words.length == i + 1){ // nothing after keyword
			throw new InvalidInputException();
		} 
		
		String date = recombine(words, i+1, words.length);
		//TODO valid check on date
		exe.setEndingDate(date);
	}

	private static void processAddOn(Executable exe, String[] words,
			                         int i) throws InvalidInputException {
		exe.setInfo(recombine(words, 1, i));
		
		if (words.length == i + 1){ // nothing after keyword
			throw new InvalidInputException();
		} 
		
		String date = recombine(words, i+1, words.length);
		//TODO valid check on date
		exe.setStartingDate(date); 
		exe.setEndingDate(date);
	}

	private static void processAddFrom(Executable exe, String[] words, 
			                           int i) throws InvalidInputException {
		exe.setInfo(recombine(words, 1, i));
		
		if (words.length == i + 1){ // nothing after keyword /for
			throw new InvalidInputException();
		} 
		
		int j;
		String word;
		for (j = i + 1; j < words.length; j++) { //look for keyword /to
			word = words[j];
			if (word.equals("/to")){
				break;
			}
		}
		
		if (words.length == j + 1){ // nothing after /to
			throw new InvalidInputException();
		} else{
			exe.setStartingDate(recombine(words, i + 1, j));
			exe.setEndingDate(recombine(words, j + 1, words.length));
		}
	}

	private static Executable processDelete(String[] words) throws InvalidInputException {
		Executable exe = new Executable(CommandType.CMD_DELETE);
		if (doesNotHaveExtraText(words)){ // no identifiers on what to delete
			throw new InvalidInputException();
		} else { //has more words
			String extraWords = recombine(words, 1, words.length);
			
			//if extra info is a date
			if (ValidationCheck.isValidDate(extraWords)) {
				
				exe.setStartingDate(extraWords); 
				exe.setEndingDate(extraWords);
			
			} else { // else, extra info is keywords
				exe.setInfo(extraWords);
			}
		}
		return exe;
	}

	private static Executable processClear(String[] words) {
		return new Executable(CommandType.CMD_CLEAR);
	}
	
	//TODO figure out what the follow up command to edit will look like
	private static Executable processEdit(String[] words) {
		Executable exe = new Executable(CommandType.CMD_EDIT);
		if (! doesNotHaveExtraText(words)){ //has more words
			String extraWords = recombine(words, 1, words.length);
			
			//if extra info is a date
			if (ValidationCheck.isValidDate(extraWords)) {
				exe.setStartingDate(extraWords); 
				exe.setEndingDate(extraWords);
			} else { // else, extra info is keywords
				exe.setInfo(extraWords);
			}
		}
		return exe;
	}

	private static Executable processUndo(String[] words) {
		return new Executable(CommandType.CMD_UNDO);
	}

	private static Executable processSearch(String[] words) throws InvalidInputException {
		Executable exe = new Executable(CommandType.CMD_SEARCH);
		if (! doesNotHaveExtraText(words)) { // no search keywords
			throw new InvalidInputException();
		} else { //processing of search keywords
			String extraWords = recombine(words, 1, words.length);
			
			//if extra info is a date
			if (ValidationCheck.isValidDate(extraWords)) {
				exe.setStartingDate(extraWords); 
				exe.setEndingDate(extraWords);
			} else { // else, extra info is keywords
				exe.setInfo(extraWords);
			}
		}
		return exe;
	}
	
	private static Executable processDisplay(String[] words) throws InvalidInputException {
		Executable exe = new Executable(CommandType.CMD_SEARCH);
		if (! doesNotHaveExtraText(words)) { // no search keywords, defaults to display all
			exe.setInfo("all");
		} else if (words[1].equals("today")){ //display today
			DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			Date date = new Date();
			System.out.println(dateFormat.format(date));
		} else { // display date or keywords
			String extraWords = recombine(words, 1, words.length);
			
			//if extra info is a date
			if (ValidationCheck.isValidDate(extraWords)) {
				exe.setStartingDate(extraWords); 
				exe.setEndingDate(extraWords);
			} else { // else, extra info is keywords
				exe.setInfo(extraWords);
			}
		}
		return exe;
	}

	private static Executable processMark(String[] words) throws InvalidInputException {
		Executable exe = new Executable(CommandType.CMD_DONE);
		if (doesNotHaveExtraText(words)){ // no identifiers
			throw new InvalidInputException();
		} else {  //has more words
			String extraWords = recombine(words, 1, words.length);
			
			//if extra info is a date
			if (ValidationCheck.isValidDate(extraWords)) {
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

}
