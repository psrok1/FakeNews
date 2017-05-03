package edu.fakenews.news.article;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.json.JSONObject;

@Entity
public class Article implements Serializable {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	private String origin = null;
	private String heading = null;
	private String article = null;
	
	protected Article() {}
	
	public Article(String heading, String article) {
		this.heading = heading;
		this.article = article;
	}
	
	public String getOrigin() {
		return this.origin;
	}
	
	public void setOrigin(String origin)
	{
		this.origin = origin;
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
