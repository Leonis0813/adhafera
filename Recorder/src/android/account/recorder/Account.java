package android.account.recorder;

public class Account {
	public static final int TYPE = 0;
	public static final int DATE = 1;
	public static final int CONTENT = 2;
	public static final int CATEGORY = 3;
	public static final int PRICE = 4;
	
	private String type;
	private String date;
	private String content;
	private String category;
	private int price;

	public Account(String type, String date, String content, String category, int price) {
		this.type = type;
		this.date = date;
		this.content = content;
		this.category = category;
		this.price = price;
	}
	
	public Account(String[] infos) {
		this.type = infos[TYPE];
		this.date = infos[DATE];
		this.content = infos[CONTENT];
		this.category = infos[CATEGORY];
		this.price = Integer.parseInt(infos[PRICE]);
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getDate() {
		return date;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

	public String toString() {
		return type + ", " + date + ", " + content + ", " + category + ", " + price;
	}

	public String[] toArray() {
		String[] array = {type, date, content, category, Integer.toString(price)};
		return array;
	}
}
