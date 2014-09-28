import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Storage {
	public LinkedList<Entry> storageList = new LinkedList<Entry>();
	public String fileName = "toDoManager.txt";
	
	private ToDoManager.EntryList readFile(){
		
		try(BufferedReader reader = new BufferedReader(new FileReader("fileName")))
		{
			
			String sCurrentLine;
			int position;
			int detectSymbol[] = {0,0,0,0,0};
			Entry insert = new Entry();
			
			storageList.clear();
			
			while((sCurrentLine = reader.readLine()) != null){
				position = 0;
				for(int positionIndex = 0; positionIndex < 5; positionIndex++){
					detectSymbol[positionIndex] = sCurrentLine.indexOf('|', position);
					position = detectSymbol[positionIndex]+1;
				}
				
				insert._name = sCurrentLine.substring(6, detectSymbol[0]-1);
				insert._startingDate = sCurrentLine.substring(detectSymbol[0]+16, detectSymbol[1]-1);
				insert._endingDate = sCurrentLine.substring(detectSymbol[1]+14, detectSymbol[2]-1);
				insert._startingTime = sCurrentLine.substring(detectSymbol[2]+16, detectSymbol[3]-1);
				insert._endingTime = sCurrentLine.substring(detectSymbol[3]+14, detectSymbol[4]-1);
				if(sCurrentLine.length() - detectSymbol[4]+11 >6)
					insert._doneness = false;
				else
					insert._doneness = true;
				
				
				storageList.add(insert);
			}
			
		}
		catch(IOException e)
		{
			System.out.println("Cannot output the file");
			e.printStackTrace();
		}
		
		return null; //TODO
	}
	
	private void writeFile(ToDoManager.EntryList entryList) {
		try 
		{
		    File file = new File("fileName");
		    
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
		    	
		    	if(temp._doneness == true)
		    		done = "Done";
		    	else
		    		done = "Not Yet Done";
		    	
		    	String insertItem[] = {temp._name,temp._startingDate, temp._endingDate, temp._startingTime, temp._endingTime, done};
		    	
		    	for(int j = 0; j < 6; j++){
		    		writeValue = writeValue.concat(insertTitle[j]);
		    		writeValue = writeValue.concat(insertItem[j]);
		    	}
		    	
		    	 writer.write(writeValue);
		    	 writer.write("\n");
		    }
		    writer.close();
		    
		    System.out.println("List insert");
		    
		} 
		catch (IOException ex) 
		{
			System.out.println("Cannot output the file");
			ex.printStackTrace();
		} 
	}
}