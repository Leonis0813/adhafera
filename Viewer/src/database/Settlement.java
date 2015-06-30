package database;

public class Settlement {
	private String interval;
	private String day;
	private int year, month;
	private int price;

	public Settlement(String interval, int year, int month, String day, int price){
		this.interval = interval;
		this.year = year;
		this.month = month;
		this.day = day;
		this.price = price;
	}

	public String getDate(){
		if(interval.equals("year")){
			return Integer.toString(year) + "年";
		}else if(interval.equals("month")){
			if(month < 10){
				return Integer.toString(year) + "/0" + Integer.toString(month);
			}else{
				return Integer.toString(year) + "/" + Integer.toString(month);
			}
		}else{
			return day.replace("-", "/");
		}
	}

	public String getPrice(){
		return price > 0 ? "+"+Integer.toString(price) : Integer.toString(price);
	}
}
