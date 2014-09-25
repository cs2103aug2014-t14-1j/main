import java.util.Date;

public class Entry{
		String _name;
		String _startingDate, _endingDate;
		String _startingTime, _endingTime;
		Boolean _doneness;
		
		public Entry(String info){
			_name = info;
			_startingDate = null;
			_endingDate = null;
			_startingTime = null;
			_endingTime = null;
			_doneness = false;
		}
		
		public Entry(String info, String startingDate, String endingDate, String startingTime, String endingTime ){
			_name = info;
			_startingDate = startingDate;
			_endingDate = endingDate;
			_startingTime = startingTime;
			_endingTime = endingTime;
			_doneness = false;
		}
		
		public void setDone(boolean doneness){
			_doneness = doneness;
		}
		
		public boolean getDone(){
			return _doneness;
		}
		
		public void setInfo(String name){
			_name = name;
		}
		
		public String getInfo(){
			return _name;
		}
		
		public void setStartingDate(String date){
			_startingDate = date;
		}
		
		public String getStartingDate(){
			return _startingDate;
		}
		
		public void setEndingDate(String date){
			_endingDate = date;
		}
		
		public String getEndingDate(){
			return _endingDate;
		}
		
		public void setStartingTime(String time){
			_startingTime = time;
		}
		
		public String getStartingTime(){
			return _startingTime;
		}
		
		public void setEndingTime(String time){
			_endingTime = time;
		}
		
		public String getEndingTime(){
			return _endingTime;
		}
		
		//TODO implement comparator
		//TODO implement containsWord(String Word)
	}