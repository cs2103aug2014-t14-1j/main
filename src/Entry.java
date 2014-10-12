package todo_manager;

public class Entry implements Comparable<Entry>{
	
    private String name;
    private String startingDate;
    private String endingDate;
    private String startingTime;
    private String endingTime;
    private Boolean doneness;
    
    public Entry(String info){
        name = info;
        startingDate = null;
        endingDate = null;
        startingTime = null;
        endingTime = null;
        doneness = false;
    }
    
    public Entry(){
        name = null;
        startingDate = null;
        endingDate = null;
        startingTime = null;
        endingTime = null;
        doneness = false;
    }
    
    public Entry(String info, String startingDate, String endingDate, String startingTime, String endingTime ){
        name = info;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.doneness = false;
    }

	
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(String startingDate) {
		this.startingDate = startingDate;
	}

	public String getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(String endingDate) {
		this.endingDate = endingDate;
	}

	public String getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(String startingTime) {
		this.startingTime = startingTime;
	}

	public String getEndingTime() {
		return endingTime;
	}

	public void setEndingTime(String endingTime) {
		this.endingTime = endingTime;
	}

	public Boolean getDoneness() {
		return doneness;
	}

	public void setDoneness(Boolean doneness) {
		this.doneness = doneness;
	}
	
	private int changeEndingDate(){
		int endingDate = Integer.parseInt(this.endingDate);
		int year = endingDate % 1000;
		int month = ((int)(endingDate/100)) % 100;
		int day = endingDate /1000;
		return year*100 + month*10 + day;
	}
    
	public int compareTo(Entry compareEntry) {
		 
		int compareDate = ((Entry) compareEntry).changeEndingDate(); 
 
		//ascending order
		return this.changeEndingDate() - compareDate;
 
		//descending order
		//return compareQuantity - this.quantity;
 
	}	
    //TODO implement comparator
    //TODO implement containsWord(String Word)
}