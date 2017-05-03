package main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;

@Configuration
public class ApplicationConfiguration {
	@Bean
	public ActorSystem actorSystem()
	{
		return ActorSystem.create("fakenews", akkaConfiguration());
	}
	
	@Bean
	public Config akkaConfiguration()
	{
		return ConfigFactory.load();
	}
}
