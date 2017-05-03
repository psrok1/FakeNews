package classifier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import grabbers.Article;

public class Classifier {
	private static final Logger logger = LoggerFactory.getLogger(FakeNewsTracker.class);
	private final String MAIN_SCRIPT = "classifier/run.py";
	
	private Process procClassifier = null;
	
	private BufferedReader pipeInput = null;
	private BufferedWriter pipeOutput = null;
	
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
					"python",
					MAIN_SCRIPT
			});
			
			this.pipeInput = new BufferedReader(
					new InputStreamReader(procClassifier.getInputStream()));
			this.pipeOutput = new BufferedWriter(
					new OutputStreamWriter(procClassifier.getOutputStream()));
		} catch(IOException e)
		{
			logger.error("Classifier spawn failed");
			e.printStackTrace();
		}
	}
	
	private String request(Article article) throws IOException {
		String result;
		
		if(this.procClassifier == null || !this.procClassifier.isAlive())
			throw new IOException();
		
		this.pipeOutput.write(article.toJSON());
		this.pipeOutput.newLine();
		
		result = this.pipeInput.readLine();
		
		if(result == null)
			throw new IOException();
		
		return result;
	}
	
	public String classifyArticle(Article article)
	{
		try {
			return this.request(article);
		} catch(IOException e)
		{
			logger.error("Something was wrong with classifier. Trying to respawn.");
			this.spawn();
		}
		return null;
	}
	
}
