package application.model;

import database.attribute.Category;
import database.attribute.Content;
import database.attribute.Date;
import database.attribute.Price;


public class Expense extends Account {

	public Expense(Date date, Content content, Category category, Price price) {
		super(date, content, category, price);
	}

	public Expense() {
		super();
	}

}
