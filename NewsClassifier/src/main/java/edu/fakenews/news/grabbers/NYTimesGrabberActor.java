package edu.fakenews.news.grabbers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.fakenews.news.actors.GrabberActor;
import edu.fakenews.news.article.Article;

public class NYTimesGrabberActor extends GrabberActor {
	private static final Logger logger = LoggerFactory.getLogger(NYTimesGrabberActor.class);
	
	@Override
	protected Article getArticle(Document document) {
		String heading = document.select("h1#headline").first().text();
		Elements pNodes = document.select("div.story-body").first().select("p");
		String articleText = "";
		for (Element p : pNodes) {
			articleText += p.text();
		}
		Article article = new Article(heading, articleText);
		return article;
	}

}
