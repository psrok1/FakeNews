package edu.fakenews;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import edu.fakenews.news.article.Article;
import edu.fakenews.news.article.ArticleRepository;

@RestController
@Transactional
@RequestMapping("/api")
public class WebController {

	@Autowired
	private ActorSystem actorSystem;
	
	private ArticleRepository repository;
	
	@Autowired
	WebController(final ArticleRepository repository){
		this.repository = repository;
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
    public List<Article> getArticles() {
		return Lists.newArrayList(repository.findAll());
    }
	
	@RequestMapping(value="/push", method=RequestMethod.POST)
    public String addArticle(@RequestBody final Article article) {
		article.setOrigin("external");
		actorSystem.actorSelection("/user/classifier").tell(article, ActorRef.noSender());
		return "OK";
    }
}
