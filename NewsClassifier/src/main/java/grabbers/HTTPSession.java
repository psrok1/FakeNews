package grabbers;

import java.io.IOException;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPSession {
	private static final Logger logger = LoggerFactory.getLogger(HTTPSession.class);

	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36";
	
	private CookieStore cookieStore;
	private HttpContext httpContext;
	private HttpClient httpClient;
	
	public HTTPSession() {
		SSLConnectionSocketFactory sslsf = null;
		try {
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			sslsf = new SSLConnectionSocketFactory(
					builder.build(),
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		} catch(Exception e) {}
		
		httpClient = HttpClients
				.custom()
				.setSSLSocketFactory(sslsf)
		        .setDefaultRequestConfig(
		        		RequestConfig
		        		.custom()
		                .setCookieSpec(CookieSpecs.STANDARD).build())
		        .setRedirectStrategy(new LaxRedirectStrategy())
				.build();
		cookieStore = new BasicCookieStore();
		httpContext = new BasicHttpContext();
		httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
	}
	
	public String get(URL url) throws IOException
	{
		HttpUriRequest request = new HttpGet(url.toString());
		request.setHeader("User-Agent", USER_AGENT);
		HttpResponse response = httpClient.execute(request, httpContext);
		logger.info(String.format("GET %d %s",
				response.getStatusLine().getStatusCode(),
				url));
		if(response.getStatusLine().getStatusCode() == 200)
			return new BasicResponseHandler().handleResponse(response);
		else
			return null;
	}
}
