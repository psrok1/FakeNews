package edu.fakenews;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import akka.routing.BalancingPool;
import edu.fakenews.news.Source;
import edu.fakenews.news.SpringExtension;
import edu.fakenews.news.actors.ClassifierActor;
import edu.fakenews.news.actors.RSSFeedActor;
import edu.fakenews.news.article.ArticleRepository;
import edu.fakenews.news.grabbers.DailyMailGrabberActor;
import edu.fakenews.news.grabbers.NYTimesGrabberActor;
import scala.concurrent.duration.Duration;

@Component
public class NewsRunner implements ApplicationRunner {	
	private static final Logger logger = LoggerFactory.getLogger(NewsRunner.class);
	
	@SuppressWarnings("rawtypes")
	private static Source sources[] = {
			new Source<NYTimesGrabberActor>
				("http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml", 
			     NYTimesGrabberActor.class)/*,
			new Source<DailyMailGrabberActor>
				("http://www.dailymail.co.uk/news/articles.rss",
				 DailyMailGrabberActor.class)*/
	};

	@Autowired
	private ActorSystem actorSystem;
	
	@Autowired
	private SpringExtension springExtension;
			
	@Override
	public void run(ApplicationArguments args) throws Exception {
		logger.info("Hello world");
		
		final ActorRef classifierPool = actorSystem.actorOf(
				new BalancingPool(3).props(
						Props.create(ClassifierActor.class)
				), "classifier");
		
		// Create storage actor managed by Spring
		final ActorRef storageActor = actorSystem.actorOf(springExtension.props("storageActor"), "storage");
		
		final Inbox actorSystemInbox = Inbox.create(actorSystem);

		logger.info("Storage actor path is: "+storageActor.path().toString());
		
		for(Source<?> source: sources)
		{
			actorSystemInbox.send(storageActor, source);
		}
				
		for(int i = 0; i < sources.length; i++)
		{
			try {
				Source source = (Source)actorSystemInbox.receive(Duration.create(15, TimeUnit.SECONDS));
				final ActorRef rssFeed = actorSystem.actorOf(Props.create(RSSFeedActor.class));
				rssFeed.tell(source, ActorRef.noSender());
				actorSystem.scheduler().schedule(
						Duration.Zero(), 
						Duration.create(1, TimeUnit.HOURS), 
						rssFeed,
						"update",
						actorSystem.dispatcher(),
						classifierPool); /* classifierPool as sender */
			} catch(TimeoutException e)
			{
				logger.error("Timeout during creation of feed actor");
				e.printStackTrace();
			}
		}
	}
}
