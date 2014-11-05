package todo_manager;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import todo_manager.ToDoManager.EmptyInputException;

//@author A0098735M
public class InterpreterTest {
	
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
		String name = "testname";
		Executable exe = Interpreter.parseCommand("/add "+name);
		assertEquals("Normal adding should return an executable with info stored", 
					  exe.getInfo(), name);
	}
	
	@Test(expected = EmptyInputException.class)
	public void emptyAddShouldThrowException() throws Exception{
		Executable exe = Interpreter.parseCommand("/add ");
	}
	
	//next 3 tests seem to belong to an equivalence partition, and it seems ineffective to test all 3,
	// but a bug was found where giving /from <date> only was accepted by the program (which it shouldnt)
	@Test(expected = IllegalArgumentException.class)
	public void incompleteAddFromShouldThrowException1() throws Exception{
		Executable exe = Interpreter.parseCommand("/add stuff /from");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void incompleteAddFromShouldThrowException2() throws Exception{
		Executable exe = Interpreter.parseCommand("/add stuff /from 21032012");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void incompleteAddFromShouldThrowException3() throws Exception{
		Executable exe = Interpreter.parseCommand("/add stuff /from 21032012 /to");
	}
	
	@Test
	public void completeAddFromShouldStoreNameAndDate() throws Exception{
		String name = "stuff and other", start = "21032012", end = "22031012";
		
		Executable exe = Interpreter.parseCommand("/add " + name + " /from " + start + " /to " + end);
		
		assertEquals("Name should be stored properly", exe.getInfo(), name);
		assertEquals("Start Date should be stored properly", exe.getStartingDate(), start);
		assertEquals("End Date should be stored properly", exe.getEndingDate(), end);
	}

}
