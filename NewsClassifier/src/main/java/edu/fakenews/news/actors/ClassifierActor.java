package edu.fakenews.news.actors;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import edu.fakenews.news.article.Article;
import edu.fakenews.news.classifier.Classifier;

public class ClassifierActor extends AbstractActor {
	private static final Logger logger = LoggerFactory.getLogger(ClassifierActor.class);
	//private Classifier classifier = new Classifier();
	
	private void classify(Article article)
	{
		logger.info("Article sent to classification");
		//this.classifier.classifyArticle(article);
		article.setClassificationTimestamp(new Date());
		article.setRating("passed");
		getContext().actorSelection("/user/storage").tell(article, ActorRef.noSender());
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Article.class, this::classify)
				.matchAny((o) -> {
					logger.warn("Got something unknown");
				})
				.build();
	}
}
