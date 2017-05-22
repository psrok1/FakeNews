package edu.fakenews.news.actors;

import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import edu.fakenews.news.Source;
import edu.fakenews.news.article.Article;
import edu.fakenews.news.article.ArticleRepository;

@Component("storageActor")
@Scope("prototype")
public class StorageActor extends AbstractActor {
	private static final Logger logger = LoggerFactory.getLogger(StorageActor.class);
	
	@Autowired
	private ArticleRepository repository;

	private void storeArticle(Article article)
	{
		logger.info("Storing article "+article.getHeading());
		repository.save(article);
	}
	
	private void getSourceLastUpdate(Source source)
	{
		try {
			Article lastArticle = repository.findBySourceOrderByPubTimestampDesc(source.getUrl().toString()).get(0);
			source.setFeedLastUpdate(lastArticle.getPubTimestamp());
		} catch(IndexOutOfBoundsException e)
		{
			logger.info("Article related with source not found");
		} catch(MalformedURLException e)
		{
			e.printStackTrace();
		}
		getSender().tell(source, ActorRef.noSender());
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Article.class, this::storeArticle)
				.match(Source.class, this::getSourceLastUpdate)
				.matchAny((o) -> {
					logger.warn("Got something unknown");
				})
				.build();
	}
}
