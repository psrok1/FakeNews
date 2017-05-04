package edu.fakenews.news.actors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.AbstractActor;
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
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Article.class, this::storeArticle)
				.matchAny((o) -> {
					logger.warn("Got something unknown");
				})
				.build();
	}
}
