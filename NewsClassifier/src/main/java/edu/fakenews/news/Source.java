package edu.fakenews.news;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import edu.fakenews.news.actors.GrabberActor;

public class Source<Actor extends GrabberActor> implements Serializable {
	private final String url;
	private final Class<Actor> grabber;
	private Date feedLastUpdate = null;
	
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

	public Date getFeedLastUpdate() {
		return feedLastUpdate;
	}

	public void setFeedLastUpdate(Date feedLastUpdate) {
		this.feedLastUpdate = feedLastUpdate;
	}
}
