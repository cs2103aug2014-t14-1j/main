package todo_manager;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

public class TestDriver {
	private static Logic logic;
	private static ToDoManagerGUI toDoManagerGUI;
	private static Object resultObj;
	private static Result result;
	private static String resultStr;
	private static LinkedList<Entry> displayList;
	private static Entry entry;
	
	@BeforeClass 
	public static void Initialise() {
		toDoManagerGUI = new ToDoManagerGUI();
		toDoManagerGUI.setup();
		logic = Logic.getInstance();
	}
	
	@Before
	@After
	public void ClearStorage() {
		logic.actOnUserInput("/clear");
	}
	
	@Test
	public void testClear() {
		logic.actOnUserInput("/add test case 1");
		//try and clear
		resultObj = logic.actOnUserInput("/clear");
		result = castToResult(resultObj);
		assertEquals("Clear should return success message.", "All tasks cleared", result.getFeedback());
		
		//check that there is no more entry
		resultObj = logic.actOnUserInput("/display");
		result = castToResult(resultObj);
		assertEquals("Should have no entries left.", 0, result.getDisplayList().size());
	}
	
	@Test
	public void testAddBasicAndDisplay(){
		//try and add
		resultObj = logic.actOnUserInput("/add test case 2");
		result = castToResult(resultObj);
		assertEquals("Add should return success message.", "A new task added successfully", result.getFeedback());
		
		//check that there is an Entry
		resultObj = logic.actOnUserInput("/display");
		result = castToResult(resultObj);
		displayList = result.getDisplayList();
		assertEquals("Should have the added Entry.", "test case 2", displayList.get(0).getName());
		assertEquals("Should have no other Entries.", 1, displayList.size());
	}
	
	@Test
	public void testAddOn() {
		//try and add with one time
		resultObj = logic.actOnUserInput("/add test case 3 /on 231215 1432");
		result = castToResult(resultObj);
		assertEquals("Add should return success message.", "A new task added successfully", result.getFeedback());
		
		//check that the Entry is correct
		resultObj = logic.actOnUserInput("/display");
		result = castToResult(resultObj);
		entry = result.getDisplayList().get(0);
		assertEquals("Should have the added Entry.",   "test case 3", entry.getName());
		assertEquals("Start date should be correct.",  "231215", entry.getStartingDate());
		assertEquals("End date should be correct.",    "231215", entry.getEndingDate());
		assertEquals("Start time should be correct.",  "1432", entry.getStartingTime());
		assertEquals("End time should be correct.",    "1432", entry.getEndingTime());
		
		//clear the previous entry
		logic.actOnUserInput("/clear");
		
		//try and add with multiple time
		resultObj = logic.actOnUserInput("/add test case 3 /on 231215 1432 1955");
		result = castToResult(resultObj);
		assertEquals("Add should return success message.", "A new task added successfully", result.getFeedback());
		
		//check that the Entry is correct
		resultObj = logic.actOnUserInput("/display");
		result = castToResult(resultObj);
		entry = result.getDisplayList().get(0);
		assertEquals("Start time should be correct.", "1432", entry.getStartingTime());
		assertEquals("End time should be correct.", "1955", entry.getEndingTime());
	}
	
	@Test
	public void testAddBy() {
		//try and add
		resultObj = logic.actOnUserInput("/add test case 4 /by 010315 1234");
		result = castToResult(resultObj);
		assertEquals("Add should return success message.", "A new task added successfully", result.getFeedback());
		
		//check that the Entry is correct
		resultObj = logic.actOnUserInput("/display");
		result = castToResult(resultObj);
		entry = result.getDisplayList().get(0);
		assertEquals("Should have the added Entry.", "test case 4", entry.getName());
		assertNull("Should have no starting date.", entry.getStartingDate());
		assertNull("Should have no starting time.", entry.getStartingTime());
		assertEquals("End date should be correct.", "010315", entry.getEndingDate());
		assertEquals("End time should be correct.", "1234", entry.getEndingTime());
	}
	
	@Test
	public void testAddStartBy() {
		//try and add
		resultObj = logic.actOnUserInput("/add test case 5 /start 030215 1345 /by 010315 1234");
		result = castToResult(resultObj);
		assertEquals("Add should return success message.", "A new task added successfully", result.getFeedback());
		
		//check that the Entry is correct
		resultObj = logic.actOnUserInput("/display");
		result = castToResult(resultObj);
		entry = result.getDisplayList().get(0);
		assertEquals("Should have the added Entry.",  "test case 5", entry.getName());
		assertEquals("Start date should be correct.", "030215", entry.getStartingDate());
		assertEquals("Start time should be correct.", "1345", entry.getStartingTime());
		assertEquals("End date should be correct.",   "010315", entry.getEndingDate());
		assertEquals("End time should be correct.",   "1234", entry.getEndingTime());
	}
	
	@Test
	public void testDelete() {
		//add an entry first
		logic.actOnUserInput("/add test case 6.1");
		
		//try to delete it
		resultObj = logic.actOnUserInput("/delete 1");
		result = castToResult(resultObj);
		assertEquals("Delete should return success message.", "Task deleted", result.getFeedback());
		
		//check that there is no more entry
		resultObj = logic.actOnUserInput("/display");
		result = castToResult(resultObj);
		assertEquals("Should have no entries left.", 0, result.getDisplayList().size());
		
		//add multiple entries
		logic.actOnUserInput("/add test case 6.1");
		logic.actOnUserInput("/add test case 6.2");
		logic.actOnUserInput("/add test case 6.3");
		logic.actOnUserInput("/add test case 6.4");
		logic.actOnUserInput("/add test case 6.5");
		
		//try to delete multiple entries
		resultObj = logic.actOnUserInput("/delete 2 4");
		result = castToResult(resultObj);
		assertEquals("Multiple delete should return success message.", "Task deleted", result.getFeedback());
		
		//check that the correct entries remain
		resultObj = logic.actOnUserInput("/display");
		result = castToResult(resultObj);
		displayList = result.getDisplayList();
		assertEquals("Should have no entries left.", 3, displayList.size());
		assertEquals("First entry after multiple deleting should be 6.1", "test case 6.1", displayList.get(0).getName());
		assertEquals("First entry after multiple deleting should be 6.1", "test case 6.3", displayList.get(1).getName());
		assertEquals("First entry after multiple deleting should be 6.1", "test case 6.5", displayList.get(2).getName());
	}
	
	@Test
	public void testEdit() {
		//add some entries first
		logic.actOnUserInput("/add test case 7.1 /on 121214 1234");
		logic.actOnUserInput("/add test case 7.2 /on 130115 1414");
		logic.actOnUserInput("/add test case 7.3 /on 140315 1759");
		
		//test edit name
		resultObj = logic.actOnUserInput("/edit 2 new name");
		result = castToResult(resultObj);
		assertEquals("Edit should return success message.", "Task edited successfully", result.getFeedback());
		entry = result.getDisplayList().get(1);
		assertEquals("Edit name should change the name.", "new name", entry.getName());
		
		//test edit starting time and date
		resultObj = logic.actOnUserInput("/edit 2 /start 311214 1010");
		result = castToResult(resultObj);
		assertEquals("Edit should return success message.", "Task edited successfully", result.getFeedback());
		entry = result.getDisplayList().get(1);
		assertEquals("Edit starting date should change the date.", "311214", entry.getStartingDate());
		assertEquals("Edit starting time should change the time.", "1010", entry.getStartingTime());
		
		//test edit ending time and date
		resultObj = logic.actOnUserInput("/edit 2 /by 290115 2350");
		result = castToResult(resultObj);
		assertEquals("Edit should return success message.", "Task edited successfully", result.getFeedback());
		entry = result.getDisplayList().get(1);
		assertEquals("Edit ending date should change the date.", "290115", entry.getEndingDate());
		assertEquals("Edit ending time should change the time.", "2350", entry.getEndingTime());
		
		//check that the others werent edited
		resultObj = logic.actOnUserInput("/display");
		result = castToResult(resultObj);
		displayList = result.getDisplayList();
		entry = displayList.get(0);
		assertEquals("Edit should not alter other entries.", "test case 7.1", entry.getName());
		assertEquals("Edit should not alter other entries.", "121214", entry.getStartingDate());
		assertEquals("Edit should not alter other entries.", "121214", entry.getEndingDate());
		assertEquals("Edit should not alter other entries.", "1234", entry.getStartingTime());
		assertEquals("Edit should not alter other entries.", "1234", entry.getEndingTime());
		
		entry = displayList.get(2);
		assertEquals("Edit should not alter other entries.", "test case 7.3", entry.getName());
		assertEquals("Edit should not alter other entries.", "140315", entry.getStartingDate());
		assertEquals("Edit should not alter other entries.", "140315", entry.getEndingDate());
		assertEquals("Edit should not alter other entries.", "1759", entry.getStartingTime());
		assertEquals("Edit should not alter other entries.", "1759", entry.getEndingTime());
	}
	
	@Test
	public void testMark() {
		//add some entries first
		logic.actOnUserInput("/add test case 8.1 /by 121214 1234");
		logic.actOnUserInput("/add test case 8.2 /on 130115 1414");
		logic.actOnUserInput("/add test case 8.3 /by 140315 1759");
		
		//mark 1  entry done
		resultObj = logic.actOnUserInput("/mark 2");
		result = castToResult(resultObj);
		assertEquals("Mark should return success message.", "Task marked done", result.getFeedback());
		displayList = result.getDisplayList();
		entry = displayList.get(0);
		assertEquals("Mark should not change other entries.", false, entry.getDoneness());
		entry = displayList.get(1);
		assertEquals("Mark should mark the right entry.", true, entry.getDoneness());
		entry = displayList.get(2);
		assertEquals("Mark should not change other entries.", false, entry.getDoneness());
		
		//mark multiple entries done
		resultObj = logic.actOnUserInput("/mark 1 3");
		result = castToResult(resultObj);
		assertEquals("Mark should return success message.", "Task marked done", result.getFeedback());
		displayList = result.getDisplayList();
		entry = displayList.get(0);
		assertEquals("Mark should not change other entries.", true, entry.getDoneness());
		entry = displayList.get(1);
		assertEquals("Mark should mark the right entry.", true, entry.getDoneness());
		entry = displayList.get(2);
		assertEquals("Mark should not change other entries.", true, entry.getDoneness());
		
		//mark 1 entry undone
		resultObj = logic.actOnUserInput("/mark 3 undone");
		result = castToResult(resultObj);
		assertEquals("Mark should return success message.", "Task marked undone", result.getFeedback());
		displayList = result.getDisplayList();
		entry = displayList.get(0);
		assertEquals("Mark should not change other entries.", true, entry.getDoneness());
		entry = displayList.get(1);
		assertEquals("Mark should mark the right entry.", true, entry.getDoneness());
		entry = displayList.get(2);
		assertEquals("Mark should not change other entries.", false, entry.getDoneness());
		
		//mark multiple entries undone
		resultObj = logic.actOnUserInput("/mark 1 2 undone");
		result = castToResult(resultObj);
		assertEquals("Mark should return success message.", "Task marked undone", result.getFeedback());
		displayList = result.getDisplayList();
		entry = displayList.get(0);
		assertEquals("Mark should not change other entries.", false, entry.getDoneness());
		entry = displayList.get(1);
		assertEquals("Mark should mark the right entry.", false, entry.getDoneness());
		entry = displayList.get(2);
		assertEquals("Mark should not change other entries.", false, entry.getDoneness());
	}

	private static Result castToResult(Object input){
		assertTrue("Returned object should be a Result", input instanceof Result);
		Result output = (Result) resultObj;
		return output;
	}
	
	private static String castToString(Object input){
		assertTrue("Returned object should be a String", input instanceof String);
		String output = (String) resultObj;
		return output;
	}
	
}
