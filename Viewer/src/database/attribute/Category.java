package database.attribute;

public class Category implements Comparable<Category> {
	private String category;

	public Category() {
		this.category = "";
	}

	public Category(String category) {
		this.category = category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public String toString() {
		return category;
	}

	public int compareTo(Category category) {
		return this.category.compareTo(category.getCategory());
	}
}
