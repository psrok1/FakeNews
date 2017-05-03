package edu.fakenews.news.grabbers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.fakenews.news.actors.GrabberActor;
import edu.fakenews.news.article.Article;

public class CallTheCopsGrabberActor extends GrabberActor {

	@Override
	protected Article getArticle(Document document) {
		String heading = document.select("h1.post-title").first().text();
		Elements pNodes = document.select("div.entry").select("p");
		String articleText = "";
		for (Element p : pNodes) {
			articleText += p.text();
		}
		Article article = new Article(heading, articleText);
		return article;
	}

}