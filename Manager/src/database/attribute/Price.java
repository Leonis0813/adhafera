package database.attribute;

public class Price implements Comparable<Price> {
	private static final int EMPTY = -1;

	private int price;

	public Price() {
		this.price = EMPTY;
	}

 	public Price(int price) {
		this.price = price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

	public String toPriceString() {
		if(price==EMPTY){
			return "";
		}else{
			return Integer.toString(price);
		}
	}

	public String toString() {
		return toPriceString();
	}

	public int compareTo(Price price) {
		if(this.price == price.getPrice()){
			return 0;
		}else if(this.price > price.getPrice()){
			return 1;
		}else{
			return -1;
		}
	}
}
