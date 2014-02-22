package net.enigmablade.riotapi;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.*;
import javax.net.ssl.*;
import net.enigmablade.jsonic.*;
import net.enigmablade.riotapi.util.*;

/**
 * A utility class to send HTTP get requests. Requests are limited on a per-second basis.
 * 
 * @author Enigma
 */
public class Requester
{
	public static final String HTTP_PROTOCOL = "http", HTTPS_PROTOCOL = "https";
	
	//Options
	private String userAgent;
	private String protocol;
	
	//Rate limiting
	private boolean limiterEnabled = true;
	private int limitPer10Seconds;
	private long limitWait, lastCall;
	private Lock rateLock;
	
	private int limitPer10Minutes;
	private LinkedList<Long> requestQueue;
	private static final long REQUEST_QUEUE_TIME_LIMIT = 600000;	//10 minutes
	
	//Caching
	private boolean cacheEnabled = true;
	
	private BufferPool<String, Response> cache;
	private static final int CACHE_AGE_LIMIT = 600000;	//10 minutes
	
	/**
	 * Create a new HTTPS Requester with the given user agent and rate limits.
	 * @param userAgent The user agent for HTTP requests.
	 * @param limitPer10Seconds The limit for the number of requests per 10 seconds. Must be greater than 0.
	 * @param limitPer10Minutes The limit for the number of requests per 10 minutes. Must be greater than 0.
	 */
	public Requester(String userAgent, int limitPer10Seconds, int limitPer10Minutes)
	{
		this(HTTPS_PROTOCOL, userAgent, limitPer10Seconds, limitPer10Minutes);
	}
	
	/**
	 * Create a new Requester with the given user agent and rate limits.
	 * @param protocol The HTTP protocol to use.
	 * @param userAgent The user agent for HTTP requests.
	 * @param limitPer10Seconds The limit for the number of requests per 10 seconds. Must be greater than 0.
	 * @param limitPer10Minutes The limit for the number of requests per 10 minutes. Must be greater than 0.
	 * @throws IllegalArgumentException If one of the arguments is not valid.
	 */
	public Requester(String protocol, String userAgent, int limitPer10Seconds, int limitPer10Minutes)
	{
		if(!HTTP_PROTOCOL.equals(protocol) && !HTTPS_PROTOCOL.equals(protocol))
			throw new IllegalArgumentException("A valid protocol must be specified.");
		if(limitPer10Seconds <= 0 || limitPer10Minutes <= 0)
			throw new IllegalArgumentException("Rate limits must be greater than 0.");
		
		this.protocol = protocol;
		setUserAgent(userAgent);
		setLimitPer10Seconds(limitPer10Seconds);
		setLimitPer10Minutes(limitPer10Minutes);
		
		rateLock = new ReentrantLock(true);
		requestQueue = new LinkedList<>();
		
		cache = new BufferPool<>(limitPer10Minutes);
	}
	
	//Functionality
	
	/**
	 * <p>A response from a request. Comprised of the response body, response code, and request time.</p>
	 * <p>If the request resulted in an error (code >= 300), the value will be <code>null</code>.</p>
	 * 
	 * @author Enigma
	 */
	public class Response
	{
		private Object value;
		private int code;
		private long made;
		
		/**
		 * Create a new value with the given value and response code.
		 * @param value The value.
		 * @param code The response code.
		 */
		protected Response(Object value, int code)
		{
			this.value = value;
			this.code = code;
			
			made = System.currentTimeMillis();
		}
		
		/**
		 * Returns the value of the response. Will return <code>null</code> if the response resulted from an error (code >= 300).
		 * @return The value, or <code>null</code> if for a response error.
		 */
		public Object getValue()
		{
			return value;
		}
		
		/**
		 * Returns the response code. Errors are values >= 300.
		 * @return The response code.
		 */
		public int getCode()
		{
			return code;
		}
		
		/**
		 * Returns the time (in milliseconds) the request result was received.
		 * @return The time the request was made.
		 */
		public long getTimeReceived()
		{
			return made;
		}
	}
	
	/**
	 * Sends a request to the server at the given URL and returns the response.
	 * @param requestUrl The request URL.
	 * @return The response from the request.
	 * @throws IOException If there was an error when sending the request.
	 */
	public synchronized Response request(String requestUrl)
	{
		//return request(requestUrl, null);
		return requestHelper(requestUrl);
	}
	
	/**
	 * Sends a request to the server at the given URL and returns the response, skipping the cache if specified.
	 * @param requestUrl The request URL.
	 * @param listener The request listener to receive the result. Leave <code>null</code> to return the value.
	 * @return The response from the request, or <code>null</code> if a listener was given.
	 * @throws IOException
	 */
	/*public Response request(final String requestUrl, final RequestListener listener)
	{
		//Send request to separate thread and return null
		if(listener != null)
		{
			//I can't wait until this is misused and someone ends up making 50,000 threads with queued requests
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					listener.requestFulfilled(requestHelper(requestUrl));
				}
			}).start();
			return null;
		}
		//Send request directly and return the result
		else
		{
			return requestHelper(requestUrl);
		}
	}*/
	
	private Response requestHelper(String requestUrl)
	{
		Response response;
		
		//Check if it's in the cache
		if(cacheEnabled)
		{
			response = cache.get(requestUrl);
			//Ignore if not in cache or too old
			if(response != null && System.currentTimeMillis()-response.getTimeReceived() < CACHE_AGE_LIMIT)
				return response;
		}
		
		//Otherwise send the request
		response = sendLimitedRequest(requestUrl);
		if(response.getValue() != null)
		{
			//Parse the request
			try
			{
				response.value = JsonParser.parse((String)response.getValue());
				cache.add(requestUrl, response);
			}
			catch(JsonParseException e)
			{
				//This shouldn't happen if we assume they're always sending correctly-formatted JSON
				e.printStackTrace();
				response.value = null;
				response.code = -1;
			}
		}
		
		return response;
	}
	
	//Private utilities
	
	private Response sendRequest(String requestUrl)
	{
		try
		{
			//Create and connect
			URL url = new URL(requestUrl);
			HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			if(userAgent != null)
				connection.setRequestProperty("User-Agent", userAgent);
			connection.connect();
			
			//Check response code and return if error
			int responseCode = connection.getResponseCode();
			if(responseCode >= 300)
				return new Response(null, responseCode);
			
			//Get response
			String responseText;
			try(InputStream in = connection.getInputStream())
			{
				responseText = IOUtil.readInputStreamFully(in);
			}
			Response response = new Response(responseText, responseCode);
			
			connection.disconnect();
			
			return response;
		}
		catch(IOException e)
		{
			System.err.println("Failed to send request: IOException");
			e.printStackTrace();
			return new Response(null, -1);
		}
	}
	
	private Response sendLimitedRequest(String requestUrl)
	{
		//Lock to prevent multiple requests from executing at once
		rateLock.lock();
		
		try
		{
			//Wait (if required) for the request time limit
			long timeSinceLast = System.currentTimeMillis()-lastCall;
			if(limiterEnabled && timeSinceLast < limitWait)
			{
				try
				{
					Thread.sleep(limitWait-timeSinceLast);
				}
				catch(InterruptedException e) { /* I don't even know when this is ever thrown... */ }
			}
			
			//Send request
			Response response = sendRequest(requestUrl);
			lastCall = System.currentTimeMillis();
			
			//Manage request queue
			trimRequestQueue();
			requestQueue.offerLast(lastCall);
			
			return response;
		}
		finally
		{
			//Unlock to let the next request through
			rateLock.unlock();
		}
	}
	
	private void trimRequestQueue()
	{
		for(Iterator<Long> it = requestQueue.descendingIterator(); it.hasNext();)
		{
			if(lastCall-it.next() > REQUEST_QUEUE_TIME_LIMIT)
				it.remove();
			else
				break;
		}
	}
	
	//Accessors and modifiers
	
	public synchronized int getLimitPer10Seconds()
	{
		return limitPer10Seconds;
	}
	
	public synchronized void setLimitPer10Seconds(int limitPer10Seconds)
	{
		limitWait = 10000/(this.limitPer10Seconds = limitPer10Seconds);
	}
	
	public synchronized int getLimitPer10Minutes()
	{
		return limitPer10Minutes;
	}
	
	public synchronized void setLimitPer10Minutes(int limitPer10Minutes)
	{
		this.limitPer10Minutes = limitPer10Minutes;
	}
	
	public synchronized String getUserAgent()
	{
		return userAgent;
	}
	
	public synchronized void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}
	
	public String getProtocol()
	{
		return protocol;
	}
	
	public synchronized int getRequestsInPast10Seconds()
	{
		int count = 0;
		for(Long time : requestQueue)
		{
			if(time <= 10000)
				count++;
			else
				break;
		}
		return count;
	}
	
	public synchronized int getRequestsInPast10Minutes()
	{
		trimRequestQueue();
		return requestQueue.size();
	}
	
	public synchronized long getOldestRequestTimeByAge(int age)
	{
		for(Long time : requestQueue)
			if(time >= age)
				return time;
		return requestQueue.peekLast();
	}
	
	public synchronized long getOldestRequestTime()
	{
		return requestQueue.isEmpty() ? -1 : requestQueue.peekLast();
	}
	
	public synchronized void setRateLimitEnabled(boolean enabled)
	{
		limiterEnabled = enabled;
	}
	
	public synchronized boolean isRateLimitEnabled()
	{
		return limiterEnabled;
	}
	
	/**
	 * Resets rate limits.
	 */
	public synchronized void clearRateLimit()
	{
		requestQueue.clear();
		lastCall = 0;
	}
	
	public synchronized void setCacheEnabled(boolean enabled)
	{
		cacheEnabled = enabled;
	}
	
	public synchronized boolean isCacheEnabled()
	{
		return cacheEnabled;
	}
	
	/**
	 * Clears the request cache.
	 */
	public synchronized void clearCache()
	{
		cache.clear();
	}
}
