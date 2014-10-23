/*
@author: Cheuk Ting
 */


package todo_manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Storage {
	public final int noOfItem = 6;
	public final String insertTitle[] = {"Name:","|Starting Date:","|Ending Date:","|Starting Time:","|Ending Time:", "|Doneness:"};
	public LinkedList<Entry> storageList = new LinkedList<Entry>();
	public String fileName = "ToDoManager.txt";
	
	public LinkedList<Entry> readFile(){
		
		try(BufferedReader reader = new BufferedReader(new FileReader(fileName)))
		{
			
			String readLine;
			int pos;
			int start[] = {0,0,0,0,0,0};
			int end[] = {0,0,0,0,0,0};
			String recordContent[] = {null, null ,null, null ,null, null};
			Entry insert;
			
			storageList.clear();
			
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
				
				//set the value of each record (will change the magic numbers later)
				//0: Name				1: Starting Date		2: Ending Date		
				//3: Starting Time		4: Ending Time			5: Doneness
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
				
				storageList.add(insert);
			}
			
		}
		catch(FileNotFoundException e) 
		{
			File file = new File(fileName);
		    try {
				file.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch(IOException e)
		{
			System.out.println("Cannot output the file");
			e.printStackTrace();
		}
		
		return storageList; //TODO
	}
	
	public void writeFile(LinkedList<Entry> entryList) {
		try 
		{
		    File file = new File(fileName);
		    
		    if(!file.exists()){
		    	file.createNewFile();
		    }
		    
		    
		    String done;
		    String writeItem;
		    
		    BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
		    for(int i=0; i<storageList.size(); i++){
		    	
		    	// Store the writing item
		    	writeItem = "";
		    	Entry writeRecord = storageList.get(i);
		    	
		    	// Set up the doneness
		    	if(writeRecord.getDoneness() == true)
		    		done = "Done";
		    	else
		    		done = "Not Yet Done";
		    	
		    	String insertItem[] = {writeRecord.getName(), writeRecord.getStartingDate(), writeRecord.getEndingDate(), 
		    						   writeRecord.getStartingTime(), writeRecord.getEndingTime(), done};
		    	
		    	for(int j = 0; j < noOfItem; j++){
		    		writeItem = writeItem.concat(insertTitle[j]);
		    		if (insertItem[j] != null) {
		    			writeItem = writeItem.concat(insertItem[j]);
		    		} 
		    		else {
		    			writeItem = writeItem.concat("");
		    		}
		    		
		    	}
		    	
		    	// insert the separator to separate each item
		    	writeItem = writeItem.concat("|\n");
		    	writer.write(writeItem);
		    }
		    writer.close();
		    
		    
		} 
		catch (IOException ex) 
		{
			System.out.println("Cannot output the file");
			ex.printStackTrace();
		} 
	}
}