package todo_manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logging {

	private static Logging instance = null;
	
	private Logging() {
	}
	
	public static Logging getInstance(){
		if(instance == null){
			instance = new Logging();
		}
		return instance;
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
