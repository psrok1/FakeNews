package grabbers;

import java.net.URL;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actors.GrabberActor;

public class LoggingGrabber extends GrabberActor {
	private static final Logger logger = LoggerFactory.getLogger(LoggingGrabber.class);

	@Override
	protected Article getArticle(Document document) {
		logger.info(document.baseUri());
		return null;
	}
}
