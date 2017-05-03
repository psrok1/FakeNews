package edu.fakenews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import edu.fakenews.news.SpringExtension;

@Configuration
public class ApplicationConfiguration {
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private SpringExtension springExtension;
	
	@Bean
	public ActorSystem actorSystem()
	{
		ActorSystem system = ActorSystem.create("fakenews", akkaConfiguration());
		springExtension.initialize(applicationContext);
		return system;
	}
	
	@Bean
	public Config akkaConfiguration()
	{
		return ConfigFactory.load();
	}
}
