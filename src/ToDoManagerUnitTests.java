package todo_manager;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import todo_manager.ToDoManager.EmptyInputException;

@SuiteClasses({ ToDoManagerUnitTests.testLogic.class, ToDoManagerUnitTests.testStorage.class, 
				ToDoManagerUnitTests.testInterpreter.class, ToDoManagerUnitTests.tesUserinterface.class})
public class ToDoManagerUnitTests {

	public static class testLogic {
		//TODO
	}
	
	public static class testInterpreter {
		
		@Test(expected = EmptyInputException.class)  
		public void EmptyInputShouldThrowException() throws Exception {
			Executable exe = Interpreter.parseCommand("");
		}
		
		@Test(expected = IllegalArgumentException.class)
		public void WrongInputShouldThrowException() throws Exception {
			Executable exe = Interpreter.parseCommand("lollolol");
		}
		
		@Test
		public void basicAddShouldStoreName() throws Exception{
			String name = "/add testname";
			Executable exe = Interpreter.parseCommand(name);
			assertEquals("Normal adding should return an executable with info stored", 
						  exe.getInfo(), name);
		}
		
	}
	
	public static class testStorage {
		//TODO
	}
	
	public static class tesUserinterface {
		//TODO
		// not sure if this module can be tested
	}
	

}
