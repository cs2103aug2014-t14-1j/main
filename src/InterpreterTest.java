package todo_manager;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import todo_manager.ToDoManager.EmptyInputException;

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
		String name = "/add testname";
		Executable exe = Interpreter.parseCommand(name);
		assertEquals("Normal adding should return an executable with info stored", 
					  exe.getInfo(), name);
	}

}
