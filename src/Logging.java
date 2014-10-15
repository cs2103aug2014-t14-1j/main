package todo_manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logging {

	private Logging() {
	}
	
	public static Logging getInstance(){
		return new Logging();
	}
	
	public boolean writeToLoggingFile(String str){
		
		String fileName = "LoggingFile.txt";
		try 
		{
		    File file = new File(fileName);
		    
		    if(!file.exists()){
		    	file.createNewFile();
		    }
		    
		    FileWriter fw = new FileWriter(fileName,true); //the true will append the new data
		    fw.write(str+"\n");//appends the string to the file
		    fw.close();
		    
		} 
		catch (IOException ex) 
		{
			System.out.println("Cannot output the file");
			ex.printStackTrace();
		} 
		
		return true;
	}
}
