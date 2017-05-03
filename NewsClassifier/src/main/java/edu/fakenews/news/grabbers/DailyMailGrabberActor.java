package edu.fakenews.news.grabbers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.fakenews.news.actors.GrabberActor;
import edu.fakenews.news.article.Article;

public class DailyMailGrabberActor extends GrabberActor {

	@Override
	protected Article getArticle(Document document) {
		String heading = document.select("#js-article-text h1").first().text();
		
		Elements pNodes = document.select("div[itemprop = articleBody]").select("p");
		String articleText = "";
		for (Element p : pNodes) {
			articleText += p.text();
		}
		Article article = new Article(heading, articleText);
		return article;
	}

}