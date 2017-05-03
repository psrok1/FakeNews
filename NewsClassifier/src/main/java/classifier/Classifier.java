package classifier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import grabbers.Article;

public class Classifier {
	private final String MAIN_SCRIPT = "";
	
	private Process procClassifier = null;
	
	private BufferedReader pipeInput = null;
	private BufferedWriter pipeOutput = null;
	
	public void init() throws IOException
	{
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
	}
	
	public String classifyArticle(Article article) throws IOException {
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
}
