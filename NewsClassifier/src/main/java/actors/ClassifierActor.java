package actors;

import java.io.IOException;

import akka.actor.AbstractActor;
import classifier.Classifier;
import grabbers.Article;

public class ClassifierActor extends AbstractActor {
	private Classifier classifier = null;
	
	public ClassifierActor()
	{
		this.spawn();
	}
	
	private void spawn() {
		try {
			this.classifier = new Classifier();
			this.classifier.init();
		} catch(IOException e)
		{
			this.classifier = null;
		}

	}
	
	private void classify(Article article)
	{
		try {
			this.classifier.classifyArticle(article);
			// do something with result
		} catch(IOException e)
		{
			// Respawn if classifier is dead
			this.spawn();
		}
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Article.class, this::classify)
				.build();
	}

}
