package todo_manager;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import org.junit.Test;

//@author A0128435E
public class StorageTest {
	
	public final String insertTitle[] = {"Name:","|Starting Date:","|Ending Date:","|Starting Time:","|Ending Time:", "|Doneness:"};
	public final int noOfItem = 6;
	//@author A0128435E
	public void test() {
		String fileName = "ToDoManager.txt";
		LinkedList<Entry> testList = new LinkedList<Entry>();
	
	// test the writing file function by inserting an entry
		try 
		{
		    File file = new File(fileName);
		    
		    if(!file.exists()){
		    	file.createNewFile();
		    }
		    
		    
		    String done = "Not Yet Done";
		    String writeItem = "";
		    Entry testRecord = new Entry();
		    
		    // set up a test record with a task name "Test Name"
		    testRecord.setName("Test Name");
		    
		    BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
		    	
		    	String insertItem[] = {testRecord.getName(), testRecord.getStartingDate(), testRecord.getEndingDate(), 
		    			testRecord.getStartingTime(), testRecord.getEndingTime(), done};
		    	
		    	for(int j = 0; j < noOfItem; j++){
		    		writeItem = writeItem.concat(insertTitle[j]);
		    		if (insertItem[j] != null) {
		    			writeItem = writeItem.concat(insertItem[j]);
		    		} 
		    		else {
		    			writeItem = writeItem.concat("");
		    		}
		    		
		    	}
		    	
		    	writeItem = writeItem.concat("|\n");
		    	writer.write(writeItem);
		    
		    writer.close();
		    
		    
		} 
		catch (IOException ex) 
		{
			System.out.println("Cannot output the file");
			ex.printStackTrace();
		} 
		
	// test the reading file function of the inserting entry
		
		try(BufferedReader reader = new BufferedReader(new FileReader(fileName)))
		{
			
			String readLine;
	
			int pos;
			int start[] = {0,0,0,0,0,0};
			int end[] = {0,0,0,0,0,0};
			String recordContent[] = {null, null ,null, null ,null, null};
			Entry insert;
			
			testList.clear();
			
			while((readLine = reader.readLine()) != null){
				insert = new Entry();
				
				//get the starting index of each extracting string
				pos = 0;
				for(int i = 0; i < noOfItem; i++){
					start[i] = readLine.indexOf(':', pos) + 1;
					pos = start[i];
				}
				
				//get the ending index of each extracting string
				pos = 0;
				for(int i = 0; i < noOfItem; i++){
					end[i] = readLine.indexOf('|', pos);
					pos = end[i]+1;
				}
				
				//get the content of record
				for(int i = 0; i < noOfItem; i++){
					recordContent[i] = readLine.substring(start[i], end[i]);
				}
				
				//set the value of each record
				insert.setName(recordContent[0]);
				insert.setStartingDate(recordContent[1]);
				insert.setEndingDate(recordContent[2]);
				insert.setStartingTime(recordContent[3]);
				insert.setEndingTime(recordContent[4]);
				
				//set the doneness of each record
				if(recordContent[5].equals("Done") ){
					insert.setDoneness(true);
				} else {
					insert.setDoneness(false);
				}
					
				testList.add(insert);
			}
			assertEquals("The first item name is ", "Test Name", testList.getLast().getName());
			
		}
		catch(IOException e)
		{
			fail("Not yet find floder.");
			e.printStackTrace();
		}
	}

}
