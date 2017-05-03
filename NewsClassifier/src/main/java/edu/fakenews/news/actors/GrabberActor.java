package edu.fakenews.news.actors;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import edu.fakenews.news.HTTPSession;
import edu.fakenews.news.article.Article;

public abstract class GrabberActor extends AbstractActor {
	private static final Logger logger = LoggerFactory.getLogger(GrabberActor.class);
	private HTTPSession http = new HTTPSession();
	
	abstract protected Article getArticle(Document document);
		
	private void getNewsFromUrl(URL newsUrl)
	{
		try {
			logger.info("Getting news from "+newsUrl.toString());
			String html = http.get(newsUrl);
			if(html == null)
			{
				logger.error("Got empty response");
				return;
			}
			Document doc = Jsoup.parse(html);
			logger.info("Parsing news");
			Article article = getArticle(doc);
			article.setOrigin(newsUrl.toString());
			logger.info(String.format("Fetched article %s", article.getHeading()));
			getSender().tell(article, ActorRef.noSender());
		} catch(Exception e)
		{
			logger.error("Good news for people who loves bad news");
			e.printStackTrace();
		}
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(URL.class, this::getNewsFromUrl)
				.build();
	}

}