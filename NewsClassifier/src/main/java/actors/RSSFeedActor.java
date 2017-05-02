package actors;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import grabbers.LoggingGrabber;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

public class RSSFeedActor extends AbstractActor {
	private static final Logger logger = LoggerFactory.getLogger(RSSFeedActor.class);
	
	ActorRef grabberActor;
	URL feedUrl;
	Date feedLastDate;
	
	public RSSFeedActor(Class<GrabberActor> grabberClass) {
		grabberActor = getContext().actorOf(Props.create(grabberClass), "grabber");
	}
	
	private void setFeedUrl(URL feedUrl)
	{
		logger.info(String.format("Set feed URL to %s", feedUrl));
		this.feedUrl = feedUrl;
		this.feedLastDate = null;
	}
	
	private void updateFeed()
	{
		logger.info(String.format("Triggered update for URL %s", feedUrl));
		try {
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));
			
			if(feedLastDate == null)
			{
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				c.add(Calendar.DATE, -1);
				feedLastDate = c.getTime();
			}
			
			/**
			 * Fetch all fresh news
			 */
			
			feed.getEntries().stream()
				.filter(e -> e.getPublishedDate().after(feedLastDate))
				.forEach(e -> {
					/* send to grabberActor */
					try {
						grabberActor.tell(new URL(e.getUri()), getSelf());
					} catch (MalformedURLException exc) {
						exc.printStackTrace();
					}
				});
			
			/**
			 * Update earliest feed pub timestamp
			 */
			feed.getEntries().forEach(e -> {
				if(e.getPublishedDate().after(feedLastDate))
					feedLastDate = e.getPublishedDate(); 
			});
		} catch(Exception e)
		{
			
		}
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(URL.class, this::setFeedUrl)
				.match(String.class, cmd -> {
					if(cmd.equals("update"))
						this.updateFeed();
					else
					{
						/**
						 * @todo: log an error
						 */
					}
				})
				.build();
	}
}
