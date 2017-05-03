package actors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import grabbers.Article;

public class NYTimesGrabberActor extends GrabberActor {

	@Override
	protected Article getArticle(Document document) {
		String heading = document.select("#headline").text();
		
		Elements pNodes = document.select(".story-body").first().select("p");
		String articleText = "";
		for (Element p : pNodes) {
			articleText += p.text();
		}
		Article article = new Article(heading, articleText);
		return article;
	}

}
