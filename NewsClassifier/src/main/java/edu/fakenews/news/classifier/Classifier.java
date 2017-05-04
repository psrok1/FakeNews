package edu.fakenews.news.classifier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.fakenews.news.article.Article;

public class Classifier {
	private static final Logger logger = LoggerFactory.getLogger(Classifier.class);
	private final String MAIN_SCRIPT = "classifier/classify.py";
	
	private Process procClassifier = null;
	
	private BufferedReader pipeInput = null;
	private BufferedWriter pipeOutput = null;
	private BufferedReader pipeError = null;
	
	public Classifier()
	{
		this.spawn();
	}
	
	private void spawn()
	{
		try {
			if(procClassifier != null && procClassifier.isAlive())
				procClassifier.destroy();
			
			this.procClassifier = Runtime.getRuntime().exec(new String[]{
					"python3",
					MAIN_SCRIPT
			});
			
			this.pipeInput = new BufferedReader(
					new InputStreamReader(procClassifier.getInputStream()));
			this.pipeOutput = new BufferedWriter(
					new OutputStreamWriter(procClassifier.getOutputStream()));
			this.pipeError = new BufferedReader(
					new InputStreamReader(procClassifier.getErrorStream()));
		} catch(IOException e)
		{
			logger.error("Classifier spawn failed");
			e.printStackTrace();
		}
	}
	
	private String request(Article article) throws IOException {
		String result;
		
		if(this.procClassifier == null || !this.procClassifier.isAlive())
			throw new IOException("Classifier is not running");
		
		this.pipeOutput.write(article.toJSON());
		this.pipeOutput.newLine();
		
		result = this.pipeInput.readLine();
		
		if(result == null)
			throw new IOException("Got EOF from classifier");
		
		while(this.pipeError.ready())
			logger.error(this.pipeError.readLine());
		
		return new JSONObject(result).get("result").toString();
	}
	
	public String classifyArticle(Article article)
	{
		try {
			return this.request(article);
		} catch(IOException e)
		{
			logger.error("Something was wrong with classifier. Trying to respawn.");
			e.printStackTrace();
			this.spawn();
		}
		return null;
	}
	
}
