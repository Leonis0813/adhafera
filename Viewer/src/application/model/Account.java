package application.model;

import database.attribute.Category;
import database.attribute.Content;
import database.attribute.Date;
import database.attribute.Price;


public abstract class Account {
	private Date date;
	private Content content;
	private Category category;
	private Price price;

	public Account(Date date, Content content, Category category, Price price){
		this.date = date;
		this.content = content;
		this.category = category;
		this.price = price;
	}

	public Account(){
		this.date = new Date();
		this.content = new Content();
		this.category = new Category();
		this.price = new Price();
	}

	public void setDate(Date date){
		this.date = date;
	}

	public Date getDate(){
		return date;
	}

	public String getDateString(){
		return date.toDateString();
	}

	public void setContent(Content content){
		this.content = content;
	}

	public Content getContent(){
		return content;
	}

	public void setCategory(Category category){
		this.category = category;
	}

	public Category getCategory(){
		return category;
	}

	public void setPrice(Price price){
		this.price = price;
	}

	public Price getPrice(){
		return price;
	}

	public String getPriceString(){
		return price.toPriceString();
	}
}
