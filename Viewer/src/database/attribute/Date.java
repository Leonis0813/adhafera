package database.attribute;

public class Date implements Comparable<Date> {
	private static final int EMPTY = -1;

	private int year;
	private int month;
	private int day;

	public Date(){
		this.year = EMPTY;
		this.month = EMPTY;
		this.day = EMPTY;
	}

	public Date(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public Date(String date) {
		String[] tkn = date.split("-");
		year = Integer.parseInt(tkn[0]);
		month = Integer.parseInt(tkn[1]);
		day = Integer.parseInt(tkn[2]);
	}
	
	public Date(java.sql.Date date) {
		String[] tkn = date.toString().split("-");
		try{
			this.year = Integer.parseInt(tkn[0]);
			this.month = Integer.parseInt(tkn[1]);
			this.day = Integer.parseInt(tkn[2]);
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getYear() {
		return year;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getMonth() {
		return month;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getDay() {
		return day;
	}

	public String toDateString() {
		if(year==EMPTY && month==EMPTY && day==EMPTY){
			return "";
		}else{
			return year + "-" + month + "-" + day;
		}
	}

	public String toString() {
		return toDateString();
	}

	public int compareTo(Date date) {
		int year = date.getYear();
		int month = date.getMonth();
		int day = date.getDay();
		
		if(this.year > year){
			return 1;
		}else if(this.year < year){
			return -1;
		}else{
			if(this.month > month){
				return 1;
			}else if(this.month < month){
				return -1;
			}else{
				if(this.day > day){
					return 1;
				}else if(this.day < day){
					return -1;
				}else{
					return 0;
				}
			}
		}
	}
}