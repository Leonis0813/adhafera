package database.attribute;

public class Content implements Comparable<Content> {
	private String content;

	public Content() {
		this.content = "";
	}

	public Content(String content) {
		this.content = content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public String toString() {
		return content;
	}

	public int compareTo(Content content) {
		return this.content.compareTo(content.getContent());
	}
}
