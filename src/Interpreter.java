package todo_manager;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import todo_manager.ToDoManager.CommandType;
import todo_manager.ToDoManager.EmptyInputException;

/**
 * Summary of the internal state of the Executable object that is returned
 * (If variable not mentioned, defaults to default chosen in Executable class (current default is null))
 * 
 *  NO TIME GIVEN
 *  /add : info filled, 
 *  /add /start /by : info filled, startingDate is /start <date>, endingDate is /by <date>
 *  /add /by : info filled, endingDate is /by date
 *  /add /on : info filled, startingDate and endingDate are equal to /on date
 *  
 *  WITH TIME GIVEN 
 *  /add <info> /start <date> <optional_Startingtime> /by  <date> <optional_Endingtime>
 *  /add <info> /by <date> <optional_Endingtime> : endingTime filled, startingTime null;
 *  /add <info> /on <date> <optional_time> : startingTime = endingTime = <optional_time>
 *  /add <info> /on <date> <optional_Startingtime> <optional_Endingtime>
 * 
 *  /delete <int displayListIndex> : index given in exe.getdisplayIndex
 *  
 *  /undo : no extra info
 *  /clear : no extra info
 *  
 *  /edit <int displaylist index> <new task name> : new name stored under exe info
 *  /edit <int displaylist index> /start <new date> /by <new date2> : dates stored in startingDate and endingDate
 *  /edit <int displaylist index> /by <new date> : date stored in endingDate
 *  /edit <int displaylist index> /on <new date> : date stored in startingDate and endingDate 
 *  
 *  /display : command set to display, no other info will be given
 *  
 *  /search today : startingDate = endingDate = today's date
 *  /search tomorrow(or tmr) : startingDate = endingDate = trm's date
 *  /search this <week> (or <month>) : search the record of this week or month
 *  /search next <week> (or <month>) : search the record of next week or month
 *  /search <month> : on that month
 *  /search <date> : startingDate = endingDate = <date>
 *  /search <keyword> : info is the string of keyword or keywords as given by the user
 *  /search /start <date> : startingDate after <date> (sorted)
 *  /search /by <date> : endingDate after <date> (sorted)
 *  /search /start <date> /by <date> : startingDate and endingDate between those period
 *  /search done : doneness = true
 *  /search undone : doneness = false
 *  Note that Doneness will be set to null if doneness is not being searched for. False or true if it is being searched for.
 *  doneness defaults to false for all other operations, like add or delete.
 *  
 *  ////MARK DONE//// -> command is CMD_DONE
 *  /mark <displaylist index> : index(may have multiple) stored in displaylist index array
 *  /mark <keywords> : info is filled with String of all keywords
 *  
 *  ///MARK UNDONE/// -> command is CMD_UNDONE
 *  to mark undone, simply use the format for mark done as above, but include the 
 *  word "undone" anywhere after "/mark"
 *  
 *  help : displays list of help topics
 *  help </command> : displays help message for that command
 *
 *
 */


public class Interpreter {
	
	//change this to true to view contents of exe returned, for debugging
	private static final boolean DEBUG = false;
	
	private static final String EMPTY_STRING = "";
	
	//TODO : make one of this in ToDoManager and have all classes call it
	private static final String DATE_FORMAT = "ddMMyy"; 
	
	static DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	private static final Date today = new Date();
	
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
				exe = processSearch(words);
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
				
			case "help":
				exe = processHelp(words);
				break;
				
			default : 
				throw new IllegalArgumentException();
		}
		
		if (DEBUG) {
			// for debugging, to view the contents of executable
			printExe(exe); 
		}
		
		return exe;
	}

	private static Executable processHelp(String[] words) {
		Executable exe = new Executable(CommandType.CMD_HELP);
		
		//has keyword
		if ( ! doesNotHaveExtraText(words)) { 
			String extraWords = recombine(words, 1, words.length);
			exe.setInfo(extraWords);
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

	private static void processAddBy(Executable exe, String[] words, int i) throws ParseException {
		
		// i is where /by was found
		exe.setInfo(recombine(words, 1, i));
		
		//date
		if (words.length == i + 1){ // nothing after keyword
			throw new IllegalArgumentException();
		} else if (! isDate(words[i+1] ) ){ // wrong date format
			throw new IllegalArgumentException();
		} else if (! isGreaterToday(words[i+1])){
			throw new IllegalArgumentException();
		}
		
		String date = words[i+1];
		exe.setEndingDate(date);
		
		//time
		if (words.length == i + 3) { //time given
			if (! isTime(words[i+2]) ) { // wrong time format
				throw new IllegalArgumentException();
			}
			
			String time = words[i+2];
			exe.setEndingTime(time);
		}
		
	}

	private static void processAddOn(Executable exe, String[] words, int i) throws ParseException {
		exe.setInfo(recombine(words, 1, i));
		
		//date
		if (words.length <= i + 1){ // nothing after keyword
			throw new IllegalArgumentException();
		} else if (! isDate(words[i+1]) ){ // wrong date format
			throw new IllegalArgumentException();
		} else if (! isGreaterToday(words[i+1])){
			throw new IllegalArgumentException();
		}
		
		String date = words[i+1];
		exe.setStartingDate(date); 
		exe.setEndingDate(date);
		
		//time
		if (words.length <= i+2){
			// return if no time argument given
			return;
		}
		
		//first time argument
		String time;
		if (! isTime(words[i+2]) ) { // wrong time format
			throw new IllegalArgumentException();
		} else {
			time = words[i+2];
			exe.setStartingTime(time);
		}
		
		if (words.length <= i+3 ) {
			// 1 time given, event occurs at a point in time
			exe.setEndingTime(time);
			return;
		}
		
		// second time argument
		if (! isTime(words[i+3]) ) { // wrong time format
			throw new IllegalArgumentException();
		} else {
			time = words[i+3];
			exe.setEndingTime(time);
		}
	}

	private static void processAddFrom(Executable exe, String[] words, int i) throws ParseException {
		exe.setInfo(recombine(words, 1, i));
		
		int pointer = i + 1;
		int end = words.length;
		String time, date;
		
		//start date
		if (pointer == end) {
			throw new IllegalArgumentException();
		} else {  
			date = words[pointer];
			if ( isDate(date) && isGreaterToday(date) ){ // right date format
				exe.setStartingDate(date);
				pointer++;
			} else {
				throw new IllegalArgumentException();
			}
		}
		
		// start time
		if (pointer == end) {
			throw new IllegalArgumentException();
		} else if (! words[pointer].equals("/by") ) {
			time = words[pointer];
			if ( isTime(time) ){ // right date format
				exe.setStartingTime(time);
				pointer++;
			} else {
				throw new IllegalArgumentException();
			}
		}
		
		//  /by
		if (pointer == end || !words[pointer].equals("/by")) {
			throw new IllegalArgumentException();
		} else {
			pointer++;
		}
		
		//end date
		if (pointer == end) {
			throw new IllegalArgumentException();
		} else {  
			date = words[pointer];
			if ( isDate(date) && isGreaterToday(date) ){ // right date format
				exe.setEndingDate(date);
				pointer++;
			} else {
				throw new IllegalArgumentException();
			}
		}
		
		// end time
		if (pointer == end) {
			return;
		} else {
			time = words[pointer];
			if ( isTime(time) ){ // right date format
				exe.setEndingTime(time);
			} else {
				throw new IllegalArgumentException();
			}
		}
	}

	private static Executable processDelete(String[] words) throws IllegalArgumentException {
		Executable exe = new Executable(CommandType.CMD_DELETE);
		ArrayList<Integer> index = new ArrayList<Integer>();
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
			processEditBy(words, exe, 2);
			
		} else if (words[2].equals("/on")) { // edit date to "on" format
			processEditOn(words, exe, 2);
			
		} else if (words[2].equals("/start")) { // edit date to "start, by" format
			processEditStart(words, exe, 2);
			
		} else { // edit entry name
			String keywords = recombine(words, 2, words.length);
			exe.setInfo(keywords);
		}
		
		return exe;
	}

	private static void processEditStart(String[] words, Executable exe, int i) {
		// i is where /start is found
		int pointer = i + 1;
		int end = words.length;
		Boolean date = null;
		
		if (pointer == end) { // check for insufficient length
			throw new IllegalArgumentException();
		} else if ( isDate(words[pointer]) ) { // change date
			exe.setStartingDate(words[pointer]);
			date = true;
		} else if (isTime(words[pointer])) { //change time
			exe.setStartingTime(words[pointer]);
			date = false;
		} else { // unrecognised format
			throw new IllegalArgumentException();
		}
		
		pointer++;
		
		if (pointer == end) { // check for insufficient length
			return;
		} if ( isDate(words[pointer]) && date == false) { //check that werent given two dates
			exe.setStartingDate(words[pointer]);
		} else if (isTime(words[pointer]) && date == true) { //check that werent given two times
			exe.setStartingTime(words[pointer]);
		} else { // unrecognised format
			throw new IllegalArgumentException();
		}

		pointer++;
		
		if (pointer == end) {
			return;
		} else if (words[pointer] == "/by") {
			processEditBy(words, exe, pointer);
		}
		
	}

	private static void processEditOn(String[] words, Executable exe, int i) {
		// i is where /on is located in words
		
		int pointer = i + 1;
		int end = words.length;
		Boolean date = null;
		
		if (pointer == end) { // check for insufficient length
			throw new IllegalArgumentException();
		} else if ( isDate(words[pointer]) ) { // change date
			exe.setStartingDate(words[pointer]);
			exe.setEndingDate(words[pointer]);
			date = true;
		} else if (isTime(words[pointer])) { //change time
			exe.setStartingTime(words[pointer]);
			exe.setEndingTime(words[pointer]);
			date = false;
		} else { // unrecognised format
			throw new IllegalArgumentException();
		}
		
		pointer++;
		
		if (pointer == end) { // check for insufficient length
			return;
		} if ( isDate(words[pointer]) && date == false) { //check that werent given two dates
			exe.setStartingDate(words[pointer]);
			exe.setEndingDate(words[pointer]);
		} else if (isTime(words[pointer]) && date == true) { //check that werent given two times
			exe.setStartingTime(words[pointer]);
			exe.setEndingTime(words[pointer]);
		} else { // unrecognised format
			throw new IllegalArgumentException();
		}
		return;
	}

	private static void processEditBy(String[] words, Executable exe, int i) {
		// i is where /by is located in words
		
		int pointer = i + 1;
		int end = words.length;
		Boolean date = null;
		
		if (pointer == end) { // check for insufficient length
			throw new IllegalArgumentException();
		} else if ( isDate(words[pointer]) ) { // change date
			exe.setEndingDate(words[pointer]);
			date = true;
		} else if (isTime(words[pointer])) { //change time
			exe.setEndingTime(words[pointer]);
			date = false;
		} else { // unrecognised format
			throw new IllegalArgumentException();
		}
		
		pointer++;
		
		if (pointer == end) { // check for insufficient length
			return;
		} if ( isDate(words[pointer]) && date == false) { //check that werent given two dates
			exe.setEndingDate(words[pointer]);
		} else if (isTime(words[pointer]) && date == true) { //check that werent given two times
			exe.setEndingTime(words[pointer]);
		} else { // unrecognised format
			throw new IllegalArgumentException();
		}
		return;
	}

	private static Executable processUndo(String[] words) {
		return new Executable(CommandType.CMD_UNDO);
	}
	
	//@author A0128435E
	private static Executable processSearch(String[] words) throws IllegalArgumentException {
		
		
		Executable exe = new Executable(CommandType.CMD_SEARCH);
		
		//set doneness null so that can differentiate between 
		//search done (true), search undone (false), and others (null)
		exe.setDoneness(null);
		
		if (doesNotHaveExtraText(words)) { // no search keywords
			throw new IllegalArgumentException(); //TODO refine exception to be more informative
		
			//search Today
		} else if (words.length == 2 && words[1].equals("today")){  
			exe.setStartingDate(dateFormat.format(today));
			exe.setEndingDate(dateFormat.format(today));
		
			//search Tomorrow
		} else if (words.length == 2 && (words[1].equals("tomorrow") || words[1].equals("tmr"))){   
			Date tommorow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
			exe.setStartingDate(dateFormat.format(tommorow));
			exe.setEndingDate(dateFormat.format(tommorow));
			
		} else if (words.length == 3 && (words[1].equals("this") || words[1].equals("next")) 
				&& (words[2].equals("week") || words[2].equals("month"))){ 
			exe.setStartingDate(getStartDate(words[1], words[2]));
			exe.setEndingDate(getEndDate(words[1], words[2]));
			
			//search for one date		
		} else if (isDate(words[1])) { 
			exe.setStartingDate(words[1]);
			exe.setEndingDate(words[1]);
			
			//search by month name
		} else if (monthValue(words[1]) != null){
			exe.setEndingDate(monthValue(words[1]));
			
			//search by starting date
		} else if (words[1].equals("/start") && words.length == 3) { 
			//search for entries after a particular date
			if (words.length < 3 || !isDate(words[2])){ 
				// no date or invalid date
				throw new IllegalArgumentException();
			}
			exe.setStartingDate(words[2]);
			
			//search by starting and ending date
		} else if (words[1].equals("/start") && words[3].equals("/by")) { //search for entries after a particular date
			if (words.length != 5 || !isDate(words[2]) || !isDate(words[4])){ 
				// no date or invalid date
				throw new IllegalArgumentException();
			}
			exe.setStartingDate(words[2]);
			exe.setEndingDate(words[4]);
			
			//search by ending date
		} else if (words[1].equals("/by")) { //search for entries before a particular date
			if (words.length < 3 || !isDate(words[2])){ 
				// no date or invalid date
				throw new IllegalArgumentException();
			}
			exe.setEndingDate(words[2]);
			
			//search for entries that are marked done
		} else if (words[1].equals("done")) { 
			exe.setDoneness(true);
			
		} else if (words[1].equals("undone")) { 
			//search for entries that are marked undone
			exe.setDoneness(false);
			
		} else { //searching for some keywords
			String extraWords = recombine(words, 1, words.length);
			exe.setInfo(extraWords);
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
			ArrayList<Integer> index = new ArrayList<Integer>();
			String word;
			String info = "";
			for (int i = 1; i < words.length; i++) {
				word = words[i];
				if (isNumber(word)){ // its a display index
					index.add(Integer.parseInt(word));
				} else if (word.equals("undone") ) {
					exe.setCommand(CommandType.CMD_UNDONE);
				} else { // its a keyword
					info += word + " ";
				}
			}
			
			if (! index.isEmpty()){
				exe.setDisplayIndex(index);
			}
			if (info != "") {
				exe.setInfo(info);
			}
//			String extraWords = recombine(words, 1, words.length);
//			
//			//if extra info is a date
//			if (isDate(extraWords)) {
//				exe.setStartingDate(extraWords); 
//				exe.setEndingDate(extraWords);
//			} else { // else, extra info is keywords
//				exe.setInfo(extraWords);
//			}
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
	
	private static boolean isTime(String time) {
		return ValidationCheck.isValidTime(time);
	}
	
	private static boolean isNumber(String s){
		try{
			Integer.parseInt(s);
		} catch (NumberFormatException e){
			return false;
		}
		return true;
	}
	
	//@author A0128435E
	private static boolean isGreaterToday(String s) throws ParseException{
		return ValidationCheck.isGreater(s);
	}
	
	//helper method for debugging
	private static void printExe(Executable exe){
		String out = "---Executable Begin---\n";
		out += "Command : "      + exe.getCommand() + "\n";
		out += "Name : "         + exe.getInfo() + "\n" ;
		out += "StartingDate : " + exe.getStartingDate() + "\n";
		out += "EndingDate : "   + exe.getEndingDate() + "\n";
		out += "StartingTime : " + exe.getStartingTime() + "\n";
		out += "EndingTime : "   + exe.getEndingTime() + "\n";
		out += "Doneness : "     + exe.getDoneness() + "\n" ;
		out += "displayIndex : " + exe.getDisplayIndex() + "\n";
		out += "---Executable End---\n\n";
		
		System.out.println(out);
	}
	
	//@author A0128435E
	private static int getMonth(String date){
		int dateInt = Integer.parseInt(date);
		int month = (dateInt/100) % 100;
		return month;
	}
	
	//@author A0128435E
	private static String getStartDate(String status, String period){
		int thisMonth = getMonth(dateFormat.format(today));
		if	((status.equals("next") && period.equals("week"))){
			// count for the 7 days after today
			Date nextWeek = new Date(today.getTime() + 7 * (1000 * 60 * 60 * 24));
			return dateFormat.format(nextWeek);
		}
		else if(status.equals("this") && period.equals("week")){
			return dateFormat.format(today);
		}
		else if(status.equals("this") && period.equals("month")){
			return monthValue(Integer.toString(thisMonth));
		}
		else{
			int nextNextMonth = thisMonth + 1;
			return monthValue(Integer.toString(nextNextMonth));
		}
	}
	
	//@author A0128435E
	private static String getEndDate(String status, String period){
		int thisMonth = getMonth(dateFormat.format(today));
		
		if	((status.equals("next") && period.equals("week"))){
			// count for the 14 days after today
			Date nextNextWeek = new Date(today.getTime() + 14 * (1000 * 60 * 60 * 24));
			return dateFormat.format(nextNextWeek);
		}
		else if(status.equals("this") && period.equals("week")){
			// count for the 7 days after today
			Date nextWeek = new Date(today.getTime() + 7 * (1000 * 60 * 60 * 24));
			return dateFormat.format(nextWeek);
		}
		else if(status.equals("this") && period.equals("month")){
			return monthValue(Integer.toString(thisMonth));
		}
		else{
			int nextNextMonth = thisMonth + 1;
			return monthValue(Integer.toString(nextNextMonth));
		}
	}
	
	//@author A0128435E
	//return null value of if the input is not a month
	private static String monthValue(String word){
				
		String keyword = word.toLowerCase();
		
		switch (keyword) {
		case "1":
		case "january" :
			return "000100";
		
		case "2":
		case "february" :
			return "000200";
		
		case "3":
		case "march" :
			return "000300";
		
		case "4":
		case "april" :
			return "000400";
		
		case "5":
		case "may" :
			return "000500";
		
		case "6":
		case "june" :
			return "000600";
			
		case "7":
		case "july" :
			return "000700";
		
		case "8":
		case "august" :
			return "000800";
			
		case "9":
	    case "september" :
			return "000900";
			
	    case "10":
		case "october":
			return "001000";
			
		case "11":
		case "november":
			return "001100";
			
		case "12":
		case "december":
			return "001200";
			
			
		default : 
			return null;
		}
	}
	
	

}
