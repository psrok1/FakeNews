import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actors.ClassifierActor;
import actors.RSSFeedActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.BalancingPool;
import grabbers.DailyMailGrabberActor;
import grabbers.NYTimesGrabberActor;
import scala.concurrent.duration.Duration;

public class FakeNewsTracker {
	private static final Logger logger = LoggerFactory.getLogger(FakeNewsTracker.class);
	
	@SuppressWarnings("rawtypes")
	private static Source sources[] = {
			new Source<NYTimesGrabberActor>
				("http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml", 
			     NYTimesGrabberActor.class),
			new Source<DailyMailGrabberActor>
				("http://www.dailymail.co.uk/news/articles.rss",
				 DailyMailGrabberActor.class)
	};
	
	public static void main(String args[])
	{
		logger.info("Working Directory = " + System.getProperty("user.dir"));
				
		try {
			final ActorSystem system = ActorSystem.create("fakenews");
			
			final ActorRef classifierPool = system.actorOf(new BalancingPool(3).props(
					Props.create(ClassifierActor.class)), "classifier");
			
			for(Source source: sources)
			{
				final ActorRef rssFeed = system.actorOf(
						Props.create(RSSFeedActor.class, source.getGrabber()));
				rssFeed.tell(source.getUrl(), ActorRef.noSender());
				system.scheduler().schedule(
						Duration.Zero(), 
						Duration.create(1, TimeUnit.HOURS), 
						rssFeed,
						"update",
						system.dispatcher(),
						classifierPool); /* classifierPool as sender */
			}			
		} catch(MalformedURLException e)
		{
			logger.error("Malformed source URL!");
			e.printStackTrace();
		}
	}
}
