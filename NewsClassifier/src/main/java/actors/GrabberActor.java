package actors;

import java.net.URL;

import org.jsoup.nodes.Document;

import akka.actor.AbstractActor;

public abstract class GrabberActor extends AbstractActor {
	abstract protected void getArticle(Document document);
	
	private void getNewsFromUrl(URL newsUrl)
	{
		/**
		 * Fetch page from URL
		 * Create Document object using parse from Jsoup.
		 * Get heading and article from Document using getArticle
		 * Send object containing heading and article to classifier actor
		 */
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(URL.class, this::getNewsFromUrl)
				.build();
	}

}
