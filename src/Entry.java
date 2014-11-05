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
		
		int day = Integer.parseInt(this.endingDate.substring(0, 2));
		int month = Integer.parseInt(this.endingDate.substring(2, 4));
		int year = Integer.parseInt(this.endingDate.substring(4, 6));
		
		int time = Integer.parseInt(this.endingTime);
		
		return year*100+month*10+day+time;
	}
    
	public int compareTo(Entry compareEntry) {
		 
		int compareDate = ((Entry) compareEntry).changeEndingDate(); 
 
		//ascending order
		return this.changeEndingDate() - compareDate;
 
		//descending order
		//return compareQuantity - this.quantity;
 
	}	
    
	public Entry copy(){
		Entry exe = new Entry();
		exe.setName(this.name);
		exe.setStartingDate(this.startingDate);
		exe.setEndingDate(this.endingDate);
		exe.setStartingTime(this.startingTime);
		exe.setEndingTime(this.endingTime);
		exe.setDoneness(this.doneness);
		return exe;
	}
}