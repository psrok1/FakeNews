package edu.fakenews.news.actors;

import java.io.IOException;

import akka.actor.AbstractActor;
import edu.fakenews.news.article.Article;
import edu.fakenews.news.classifier.Classifier;

public class ClassifierActor extends AbstractActor {
	//private Classifier classifier = new Classifier();
	
	private void classify(Article article)
	{
		//this.classifier.classifyArticle(article);
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Article.class, this::classify)
				.build();
	}

}
