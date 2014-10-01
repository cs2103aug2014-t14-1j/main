package todo_manager;

public class Entry{
    public String name;
    public String startingDate;
    public String endingDate;
    public String startingTime;
    public String endingTime;
    public Boolean doneness;
    
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
    
    public void setDone(boolean doneness){
        this.doneness = doneness;
    }
    
    public boolean getDone(){
        return doneness;
    }
    
    public void setInfo(String name){
        this.name = name;
    }
    
    public String getInfo(){
        return name;
    }
    
    public void setStartingDate(String date){
        startingDate = date;
    }
    
    public String getStartingDate(){
        return startingDate;
    }
    
    public void setEndingDate(String date){
        endingDate = date;
    }
    
    public String getEndingDate(){
        return endingDate;
    }
    
    public void setStartingTime(String time){
        startingTime = time;
    }
    
    public String getStartingTime(){
        return startingTime;
    }
    
    public void setEndingTime(String time){
        endingTime = time;
    }
    
    public String getEndingTime(){
        return endingTime;
    }
    
    //TODO implement comparator
    //TODO implement containsWord(String Word)
}