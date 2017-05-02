package grabbers;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actors.GrabberActor;

public class LoggingGrabber extends GrabberActor {
	private static final Logger logger = LoggerFactory.getLogger(LoggingGrabber.class);
	
	@Override
	protected void getNewsFromUrl(URL newsUrl) {
		logger.info(String.format("Grabbing %s", newsUrl));
	}

}
