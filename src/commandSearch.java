package todo_manager;

/*
@author: Cai Di
 */

import java.util.ArrayList;
import java.util.LinkedList;

public class commandSearch {
	Storage storage;
	private LinkedList<Entry> entryList;
	
	
	public commandSearch(LinkedList<Entry> list){
		this.entryList = list;
	}
	
	public LinkedList<Entry> search(String searchContent){
		searchContent = searchContent.trim();
		ValidationCheck validCheck = new ValidationCheck();
		boolean isDate = validCheck.isValidDate(searchContent);
		
		System.out.println("search content is date: "+isDate);
		
		LinkedList<Entry> searchResult = new LinkedList<Entry>();
		
		if(isDate){
			for(Entry entry: entryList){
				if(entry.getStartingDate() == searchContent ||entry.getEndingDate() == searchContent ){
					searchResult.add(entry);
				}
			}
		}
		else{
			searchContent = searchContent.toLowerCase();
			for(Entry entry: entryList){
				String temp = entry.getName().toLowerCase();
				if(temp.contains(searchContent)){
					searchResult.add(entry);
				}
			}
		}
		return searchResult;
	}
}
