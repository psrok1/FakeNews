package main;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.BalancingPool;
import news.Source;
import news.actors.ClassifierActor;
import news.actors.RSSFeedActor;
import news.grabbers.DailyMailGrabberActor;
import news.grabbers.NYTimesGrabberActor;
import scala.concurrent.duration.Duration;

@Component
public class NewsRunner implements ApplicationRunner {	
	private static final Logger logger = LoggerFactory.getLogger(NewsRunner.class);
	
	@SuppressWarnings("rawtypes")
	private static Source sources[] = {
			new Source<NYTimesGrabberActor>
				("http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml", 
			     NYTimesGrabberActor.class),
			new Source<DailyMailGrabberActor>
				("http://www.dailymail.co.uk/news/articles.rss",
				 DailyMailGrabberActor.class)
	};

	@Autowired
	private ActorSystem actorSystem;
		
	@Override
	public void run(ApplicationArguments args) throws Exception {
		logger.info("Hello world");
		
		final ActorRef classifierPool = actorSystem.actorOf(
				new BalancingPool(3).props(
						Props.create(ClassifierActor.class)
				), "classifier");
		
		for(Source<?> source: sources)
		{
			try {
				final ActorRef rssFeed = actorSystem.actorOf(
						Props.create(RSSFeedActor.class, source.getGrabber()));
				rssFeed.tell(source.getUrl(), ActorRef.noSender());
				actorSystem.scheduler().schedule(
						Duration.Zero(), 
						Duration.create(1, TimeUnit.HOURS), 
						rssFeed,
						"update",
						actorSystem.dispatcher(),
						classifierPool); /* classifierPool as sender */
			} catch(MalformedURLException e)
			{
				e.printStackTrace();
			}
		}
	}
}
