import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import actors.RSSFeedActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import grabbers.LoggingGrabber;
import scala.concurrent.duration.Duration;

public class FakeNewsTracker {
	
	public static void main(String args[])
	{
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
