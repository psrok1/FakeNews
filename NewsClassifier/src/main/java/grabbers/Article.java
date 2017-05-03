package grabbers;

public class Article {
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

}
