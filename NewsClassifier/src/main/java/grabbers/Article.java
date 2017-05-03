package grabbers;

import java.io.Serializable;

import org.json.JSONObject;

public class Article implements Serializable {
	private final String heading;
	private final String article;
	
	public Article(String heading, String article) {
		this.heading = heading;
		this.article = article;
	}
	
	public String getHeading() {
		return heading;
	}
	
	public String getArticle() {
		return article;
	}
	
	public String toJSON() {
		return new JSONObject()
				.put("heading", heading)
				.put("article", article)
				.toString();
	}
}
