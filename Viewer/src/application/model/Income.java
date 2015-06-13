package application.model;

import database.attribute.Category;
import database.attribute.Content;
import database.attribute.Date;
import database.attribute.Price;


public class Income extends Account {

	public Income(Date date, Content content, Category category, Price price) {
		super(date, content, category, price);
	}

	public Income() {
		super();
	}

}
