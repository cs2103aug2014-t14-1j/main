package todo_manager;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

public class searchTest {
	@Test
	public void test(){	
		Logic myLogic = Logic.getInstance();
		LinkedList<Entry> myEntryList = new LinkedList<Entry>();
		Entry myEntry = new Entry();
		Entry myEntry2 = new Entry();
		
		myEntry.setName("I have an exam soon");
		myEntry2.setName("V0.2 is coming soon");
		myLogic.entryList.add(myEntry);
		myLogic.entryList.add(myEntry2);
		
		try {
			assertEquals("search result found", "I have an exam soon", myLogic.executeSearch(Interpreter.parseCommand("/search exam")).getFirst().getName());
			assertEquals("search result found", "I have an exam soon", myLogic.executeSearch(Interpreter.parseCommand("/search I")).getFirst().getName());
			assertEquals("search result found", "V0.2 is coming soon", myLogic.executeSearch(Interpreter.parseCommand("/search co")).getFirst().getName());
			assertEquals("search result found", "V0.2 is coming soon", myLogic.executeSearch(Interpreter.parseCommand("/search soon")).get(1).getName());
			assertEquals("search result found", 2, myLogic.executeSearch(Interpreter.parseCommand("/search soon")).size());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
