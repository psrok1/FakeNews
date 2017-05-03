import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actors.RSSFeedActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import grabbers.LoggingGrabber;
import scala.concurrent.duration.Duration;

public class FakeNewsTracker {
	private static final Logger logger = LoggerFactory.getLogger(FakeNewsTracker.class);
	
	public static void main(String args[])
	{
		logger.info("Working Directory = " + System.getProperty("user.dir"));
		
		try {
			final ActorSystem system = ActorSystem.create("fakenews");
			
			final ActorRef rssFeed = system.actorOf(Props.create(RSSFeedActor.class, LoggingGrabber.class));
			
			rssFeed.tell(new URL("http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml"), ActorRef.noSender());
			system.scheduler().schedule(
					Duration.Zero(), 
					Duration.create(1, TimeUnit.HOURS), 
					rssFeed,
					"update",
					system.dispatcher(),
					ActorRef.noSender());
		} catch(MalformedURLException e)
		{
			
		}
	}
}
