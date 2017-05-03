package main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import akka.actor.ActorSystem;
import news.Article;

@RestController
@RequestMapping("/api")
public class WebController {

	@Autowired
	private ActorSystem actorSystem;
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
    public String getArticles() {
		return actorSystem.name();
    }
	
	@RequestMapping(value="/push", method=RequestMethod.POST)
    public String addArticle(@RequestBody final Article article) {
		return actorSystem.name();
    }
}
