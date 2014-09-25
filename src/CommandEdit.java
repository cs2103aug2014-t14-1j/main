package todo_manager;
/*
 * @author:Cai Di
 */

import java.util.ArrayList;
import java.util.Scanner;

public class CommandEdit {
	private ArrayList<Entry> storage;

	public CommandEdit(ArrayList<Entry> storage) {
		this.storage = storage;
	}

	public void edit(String editContent) {
		CommandSearch mySearch = new CommandSearch(storage);

		// call a display methode to display search results
		Display display = new Display();
		ArrayList<Entry> searchResult = mySearch.search(editContent);
		display.displaySearchRedult(searchResult);

		// let the user choose which one to edit
		Scanner sc = new Scanner(System.in);
		int userChoice = sc.nextInt();

		if (userChoice > searchResult.size()) {
			System.out.println("User entry is not valid");
		}
		else {
			// change entry attributes here
			storage.get(userChoice);
		}
	}
}
