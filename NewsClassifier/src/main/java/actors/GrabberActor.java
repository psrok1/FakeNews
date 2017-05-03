package actors;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import akka.actor.AbstractActor;
import grabbers.Article;

public abstract class GrabberActor extends AbstractActor {
	abstract protected Article getArticle(Document document);
	
	private void getNewsFromUrl(URL newsUrl)
	{
		/**
		 * Fetch page from URL
		 * Create Document object using parse from Jsoup.
		 * Get heading and article from Document using getArticle
		 * Send object containing heading and article to classifier actor
		 */
		try {
			Document doc = Jsoup.connect(newsUrl.toString()).get();
			Article article = getArticle(doc);
			// TODO
		} catch(Exception e)
		{
			/**
			 * TODO log
			 */
		}
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(URL.class, this::getNewsFromUrl)
				.build();
	}

}
