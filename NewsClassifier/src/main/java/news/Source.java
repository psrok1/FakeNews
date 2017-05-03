package news;

import java.net.MalformedURLException;
import java.net.URL;

import news.actors.GrabberActor;

public class Source<Actor extends GrabberActor> {
	private final String url;
	private final Class<Actor> grabber;
	
	public Source(String url, Class<Actor> grabber) {
		this.url = url;
		this.grabber = grabber;
	}

	public URL getUrl() throws MalformedURLException {
		return new URL(url);
	}

	@SuppressWarnings("unchecked")
	public Class<GrabberActor> getGrabber() {
		return (Class<GrabberActor>)grabber;
	}
}
