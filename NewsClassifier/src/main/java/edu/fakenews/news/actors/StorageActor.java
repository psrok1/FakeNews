package edu.fakenews.news.actors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.AbstractActor;
import edu.fakenews.news.article.ArticleRepository;

@Component("storageActor")
@Scope("prototype")
public class StorageActor extends AbstractActor {
	@Autowired
	private ArticleRepository repository;
	
	@Override
	public Receive createReceive() {
		// TODO Auto-generated method stub
		return null;
	}
}
