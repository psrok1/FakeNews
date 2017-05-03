package actors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import grabbers.Article;

public class DailyMailGrabberActor extends GrabberActor {

	@Override
	protected Article getArticle(Document document) {
		String heading = document.select("#js-article-text").first().text();
		
		Elements pNodes = document.select("p[itemprop = articleBody]").select("p");
		String articleText = "";
		for (Element p : pNodes) {
			articleText += p.text();
		}
		Article article = new Article(heading, articleText);
		return article;
	}

}
