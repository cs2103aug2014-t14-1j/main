package todo_manager;
// @A0128435E
import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

public class searchTest {
	@Test
	public void test(){	
		Logic myLogic = Logic.getInstance();
		LinkedList<Entry> myEntryList = new LinkedList<Entry>();

		// *********search test 1 (key word test)************
		Entry searchEntry1 = new Entry();
		Entry searchEntry2 = new Entry();
		
		// set the test name with the certain keyword
		searchEntry1.setName("I have an exam soon");
		searchEntry2.setName("V0.5 is coming soon");
		
		// insert the entries to the list
		myLogic.entryList.add(searchEntry1);
		myLogic.entryList.add(searchEntry2);
		
		try {
			// normal case
			assertEquals("search result found", "I have an exam soon", myLogic.executeSearch(Interpreter.parseCommand("/view exam")).getFirst().getName());
			// boundary case
			assertEquals("search result found", "I have an exam soon", myLogic.executeSearch(Interpreter.parseCommand("/view have exam")).getFirst().getName());
			// normal case
			assertEquals("search result found", "V0.5 is coming soon", myLogic.executeSearch(Interpreter.parseCommand("/view coming soon")).getFirst().getName());
			// boundary case
			assertEquals("search result found", "V0.5 is coming soon", myLogic.executeSearch(Interpreter.parseCommand("/view V0.5")).get(1).getName());
			// test the number of records return
			assertEquals("search result found", 2, myLogic.executeSearch(Interpreter.parseCommand("/view soon")).size());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// After testing, clear all the element of the list
		myLogic.entryList.clear();
		
		// ********** search test 2 (date test)************
		
		// not be detected case
		searchEntry1.setName("should not be dectected");
		searchEntry1.setStartingDate("100914");
		searchEntry1.setEndingDate("111114");
		
		
		// not be detected case
		searchEntry2.setName("should not be dectected");
		searchEntry2.setStartingDate("101014");
		searchEntry2.setEndingDate("111014");
		
		// normal case
		// A task from 10/11/2014 to 11/11/2014
		Entry searchEntry3 = new Entry();
		searchEntry3.setName("I test the first date");
		searchEntry3.setStartingDate("101114");
		searchEntry3.setEndingDate("111114");
		
		// boundary case
		// A task from 01/11/2014 to 30/11/2014
		Entry searchEntry4 = new Entry();
		searchEntry4.setName("I test the second date");
		searchEntry4.setStartingDate("011114");
		searchEntry4.setEndingDate("301114");
		
		// normal case
		// A task from 10/11/2014 to 11/12/2014
		Entry searchEntry5 = new Entry();
		searchEntry5.setName("I test the third date");
		searchEntry5.setStartingDate("101114");
		searchEntry5.setEndingDate("111214");
		
		// normal case
		// A task from 10/11/2014 to 11/12/2014
		Entry searchEntry6 = new Entry();
		searchEntry6.setName("I test the third date");
		searchEntry6.setStartingDate("101214");
		searchEntry6.setEndingDate("131214");
		
		// normal case
		// A task from 10/11/2014 to 11/12/2014
		Entry searchEntry7 = new Entry();
		searchEntry7.setName("I test the third date");
		searchEntry7.setStartingDate("101214");
		searchEntry7.setEndingDate("131214");
				
		
		// normal case
		Entry searchEntry8 = new Entry();
		searchEntry8.setName("I test the forth date");
		searchEntry8.setStartingDate("100115");
		searchEntry8.setEndingDate("120115");
		
		// normal case
		Entry searchEntry9 = new Entry();
		searchEntry9.setName("I test the forth date");
		searchEntry9.setStartingDate("110115");
		searchEntry9.setEndingDate("130115");
		
		// insert the record to the entryList
		myLogic.entryList.add(searchEntry1);
		myLogic.entryList.add(searchEntry2);
		myLogic.entryList.add(searchEntry3);
		myLogic.entryList.add(searchEntry4);
		myLogic.entryList.add(searchEntry5);
		myLogic.entryList.add(searchEntry6);
		myLogic.entryList.add(searchEntry7);
		myLogic.entryList.add(searchEntry8);
		myLogic.entryList.add(searchEntry9);
		
		try {
			
			// normal test (test /start)
			// the number of records should only be 3 after 10-11-2014.
			assertEquals("search result found", 6, myLogic.executeSearch(Interpreter.parseCommand("/view /start 101114")).size());
			
			// boundary case (test /start)
			// the record date which closer to 10-11-2014 should be the first record
			assertEquals("search result found", "I test the first date", myLogic.executeSearch(Interpreter.parseCommand("/view /start 101114")).getFirst().getName());
			
			// boundary case (test month name)
			// The result should be 3 as there are only 4 records end in November (including the first date and the last day in November)
			assertEquals("search result found", 3, myLogic.executeSearch(Interpreter.parseCommand("/view November")).size());
				
			// normal test (test alternative search method)
			assertEquals("search result found", 3, myLogic.executeSearch(Interpreter.parseCommand("/view 11")).size());
			
			// normal case
			// test alternative search method (with 2 records)
			assertEquals("search result found", "I test the third date", myLogic.executeSearch(Interpreter.parseCommand("/view /by 101214")).getFirst().getName());
			assertEquals("search result found", 5, myLogic.executeSearch(Interpreter.parseCommand("/view /by 101214")).size());
			
			// boundary case
			// test alternative between the case
			assertEquals("search result found", "I test the forth date", myLogic.executeSearch(Interpreter.parseCommand("/view /start 090115 /by 110115")).get(1).getName());
			assertEquals("search result found", 1, myLogic.executeSearch(Interpreter.parseCommand("/view /start 090115 /by 110115")).size());
			
			// normal case
			// test the search this month
			assertEquals("search result found", 2, myLogic.executeSearch(Interpreter.parseCommand("/view this month")).size());
			
			// normal case
			// test the search next month
			assertEquals("search result found", 2, myLogic.executeSearch(Interpreter.parseCommand("/view next month")).size());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
