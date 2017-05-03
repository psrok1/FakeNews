package news.actors;

import java.io.IOException;

import akka.actor.AbstractActor;
import news.Article;
import news.classifier.Classifier;

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
