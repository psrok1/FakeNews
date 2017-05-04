package edu.fakenews.news.article;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
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
	
	@Column(columnDefinition = "CLOB")
	private String heading = null;
	
	@Column(columnDefinition = "CLOB")
	private String article = null;

	private String origin = null;
	private Date pubTimestamp;
	
	private Date classificationTimestamp;
	private String rating = null;
	
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

	public Date getClassificationTimestamp() {
		return classificationTimestamp;
	}

	public void setClassificationTimestamp(Date classificationTimestamp) {
		this.classificationTimestamp = classificationTimestamp;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public Date getPubTimestamp() {
		return pubTimestamp;
	}

	public void setPubTimestamp(Date pubTimestamp) {
		this.pubTimestamp = pubTimestamp;
	}
}
