
/*
@author: Cai Di
 */

import java.util.ArrayList;

public class CommandSearch {
	private ArrayList<Entry> storage;
	
	public CommandSearch(ArrayList<Entry> storage){
		this.storage = storage;
	}
	
	public ArrayList<Entry> search(String searchContent){
		ValidationCheck validCheck = new ValidationCheck();
		boolean isDate = validCheck.isValidDate(searchContent);
		
		ArrayList<Entry> searchResult = new ArrayList<Entry>();
		
		if(isDate){
			for(Entry entry: storage){
				if(entry.startingDate == searchContent ||entry.endingDate == searchContent ){
					searchResult.add(entry);
				}
			}
		}
		else{
			searchContent = searchContent.toLowerCase();
			for(Entry entry: storage){
				String temp = entry.name.toLowerCase();
				if(temp.contains(searchContent)){
					searchResult.add(entry);
				}
			}
		}
		return searchResult;
	}
}
