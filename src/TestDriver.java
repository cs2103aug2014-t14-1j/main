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
		assertEquals("Delete should return success message.", "Task deleted", result.getFeedback());
		
		//check that there is no more entry
		resultObj = logic.actOnUserInput("/display");
		result = castToResult(resultObj);
		displayList = result.getDisplayList();
		assertEquals("Should have no entries left.", 3, displayList.size());
		assertEquals("First entry after deleting should be 6.1", "test case 6.1", displayList.get(0).getName());
		assertEquals("First entry after deleting should be 6.1", "test case 6.3", displayList.get(1).getName());
		assertEquals("First entry after deleting should be 6.1", "test case 6.5", displayList.get(2).getName());
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
