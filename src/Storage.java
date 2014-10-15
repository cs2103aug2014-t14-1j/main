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
	public LinkedList<Entry> storageList = new LinkedList<Entry>();
	public String fileName = "ToDoManager.txt";
	public LinkedList<Entry> readFile(){
		
		try(BufferedReader reader = new BufferedReader(new FileReader(fileName)))
		{
			
			String sCurrentLine;
			int position;
			int detectSymbol[] = {0,0,0,0,0};
			Entry insert;
			
			storageList.clear();
			
			while((sCurrentLine = reader.readLine()) != null){
				insert = new Entry();
				position = 0;
				for(int positionIndex = 0; positionIndex < 5; positionIndex++){
					detectSymbol[positionIndex] = sCurrentLine.indexOf('|', position);
					position = detectSymbol[positionIndex]+1;
				}
				
				insert.setName(sCurrentLine.substring(6, detectSymbol[0]-1));
				insert.setStartingDate(sCurrentLine.substring(detectSymbol[0]+16, detectSymbol[1]-1));
				insert.setEndingDate(sCurrentLine.substring(detectSymbol[1]+14, detectSymbol[2]-1));
				insert.setStartingTime(sCurrentLine.substring(detectSymbol[2]+16, detectSymbol[3]-1));
				insert.setEndingTime(sCurrentLine.substring(detectSymbol[3]+14, detectSymbol[4]-1));
				if(sCurrentLine.length() - detectSymbol[4]+11 >6){
					insert.setDoneness(false);
				} else {
					insert.setDoneness(true);
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
		    
		    String insertTitle[] = {"Name: "," |Starting Date: ", " |Ending Date: ", " |Starting Time: ", " |Ending Time: ", " |Doneness: "};
		    String done;
		    String writeValue;
		    
		    BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
		    for(int i=0; i<storageList.size(); i++){
		    	writeValue = "";
		    	
		    	Entry temp = storageList.get(i);
		    	
		    	if(temp.getDoneness() == true)
		    		done = "Done";
		    	else
		    		done = "Not Yet Done";
		    	
		    	String insertItem[] = {temp.getName(), temp.getStartingDate(), temp.getEndingDate(), 
		    			               temp.getStartingTime(), temp.getEndingTime(), done};
		    	
		    	for(int j = 0; j < 6; j++){
		    		writeValue = writeValue.concat(insertTitle[j]);
		    		if (insertItem[j] != null) {
		    			writeValue = writeValue.concat(insertItem[j]);
		    		} else {
		    			writeValue = writeValue.concat("");
		    		}
		    		
		    	}
		    	 writeValue = writeValue.concat("\n");
		    	 writer.write(writeValue);
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