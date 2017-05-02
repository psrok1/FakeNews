package actors;

import java.net.URL;

import akka.actor.AbstractActor;

public abstract class GrabberActor extends AbstractActor {
	abstract protected void getNewsFromUrl(URL newsUrl);
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(URL.class, this::getNewsFromUrl)
				.build();
	}

}
