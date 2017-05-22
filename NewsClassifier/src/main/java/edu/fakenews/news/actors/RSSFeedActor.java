package edu.fakenews.news.actors;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import edu.fakenews.news.Source;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

public class RSSFeedActor extends AbstractActor {
	private static final Logger logger = LoggerFactory.getLogger(RSSFeedActor.class);
	
	ActorRef grabberActor;
	URL feedUrl;
	Date feedLastUpdate;
		
	private void setFeedSource(Source feedSource)
	{
		try {
			this.grabberActor = getContext().actorOf(Props.create(feedSource.getGrabber()), "grabber");
			this.feedUrl = feedSource.getUrl();
			this.grabberActor.tell(this.feedUrl, ActorRef.noSender());
			this.feedLastUpdate = feedSource.getFeedLastUpdate();

			logger.info(String.format("Set feed URL to %s", feedUrl));
		} catch (MalformedURLException e) {
			logger.error("Critical error during feed setup");
			e.printStackTrace();
		}
	}
	
	private void updateFeed()
	{
		logger.info(String.format("Triggered update for URL %s", feedUrl));
		try {
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));
			
			if(feedLastUpdate == null)
			{
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				c.add(Calendar.DATE, -1);
				feedLastUpdate = c.getTime();
			}
			
			/**
			 * Fetch all fresh news
			 */
			
			feed.getEntries().stream()
				.filter(e -> e.getPublishedDate().after(feedLastUpdate))
				.forEach(e -> {
					logger.info(String.format("Found some good news: %s", e.getLink()));
					/* send entry to grabberActor */
					grabberActor.tell(e, getSender());
				});
			
			/**
			 * Update earliest feed pub timestamp
			 */
			feed.getEntries().forEach(e -> {
				if(e.getPublishedDate().after(feedLastUpdate))
					feedLastUpdate = e.getPublishedDate(); 
			});
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Source.class, this::setFeedSource)
				.match(String.class, cmd -> {
					if(cmd.equals("update"))
						this.updateFeed();
					else
					{
						logger.error("Unknown command");
					}
				})
				.build();
	}
}
