package net.enigmablade.riotapi;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.*;
import net.enigmablade.jsonic.*;
import net.enigmablade.riotapi.util.*;

/**
 * A utility class to send HTTP get requests. Requests are limited on a per-second basis.
 * 
 * @author Enigma
 */
public class Requester
{
	private String userAgent;
	
	//Rate limiting
	private boolean limiterEnabled = true;
	private int limitPer10Seconds;
	private long limitWait, lastCall;
	private Lock rateLock;
	
	private int limitPer10Minutes;
	private Queue<Long> requestQueue;
	private static final long REQUEST_QUEUE_TIME_LIMIT = 60000;	//10 minutes
	
	//Caching
	private BufferPool<String, Response> cache;
	private static final int CACHE_AGE_LIMIT = 60000;	//10 minutes
	
	/**
	 * Create a new Requester with the given user agent and rate limits.
	 * @param userAgent The user agent for HTTP requests.
	 * @param limitPer10Seconds The limit for the number of requests per 10 seconds. Must be greater than 0.
	 * @param limitPer10Minutes The limit for the number of requests per 10 minutes. Must be greater than 0.
	 */
	public Requester(String userAgent, int limitPer10Seconds, int limitPer10Minutes)
	{
		if(limitPer10Seconds <= 0 || limitPer10Minutes <= 0)
			throw new IllegalArgumentException("Rate limits must be greater than 0.");
		
		setUserAgent(userAgent);
		setLimitPer10Seconds(limitPer10Seconds);
		setLimitPer10Minutes(limitPer10Minutes);
		
		rateLock = new ReentrantLock(true);
		requestQueue = new LinkedList<>();
		
		cache = new BufferPool<>(limitPer10Minutes);
	}
	
	//Functionality methods
	
	public class Response
	{
		private Object value;
		private int code;
		private long made;
		
		protected Response(Object value, int code)
		{
			this.value = value;
			this.code = code;
			
			made = System.currentTimeMillis();
		}
		
		public Object getValue()
		{
			return value;
		}
		
		public int getCode()
		{
			return code;
		}
		
		public long getMadeTime()
		{
			return made;
		}
	}
	
	public Response request(String requestUrl) throws IOException
	{
		return request(requestUrl, false);
	}
	
	public Response request(String requestUrl, boolean skipCache) throws IOException
	{
		return request(requestUrl, skipCache, false);
	}
	
	public Response request(String requestUrl, boolean skipCache, boolean speedy) throws IOException
	{
		Response response;
		
		//Check if it's in the cache
		if(!skipCache)
		{
			response = cache.get(requestUrl);
			//Ignore if not in cache or too old
			if(response != null && System.currentTimeMillis()-response.getMadeTime() < CACHE_AGE_LIMIT)
				return response;
		}
		
		//Otherwise send the request
		response = sendLimitedRequest(requestUrl);
		if(response.getValue() == null)
			return response;
		
		//Parse the request
		try
		{
			response.value = JsonParser.parse((String)response.getValue(), speedy);
			return response;
		}
		catch(JsonParseException e)
		{
			//This shouldn't happen if we assume they're always sending correctly-formatted JSON
			e.printStackTrace();
			return null;
		}
	}
	
	//Private utilities
	
	private Response sendRequest(String requestUrl) throws IOException
	{
		//Create and connect
		URL url = new URL(requestUrl);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
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
		InputStream in = connection.getInputStream();
		String response = IOUtil.readInputStreamFully(in);
		return new Response(response, responseCode);
	}
	
	private Response sendLimitedRequest(String requestUrl) throws IOException
	{
		//Lock to prevent multiple requests from executing at once
		rateLock.lock();
		
		//Wait (if required) for the request time limit
		long timeSinceLast = System.currentTimeMillis()-lastCall;
		if(limiterEnabled && timeSinceLast < limitWait)
			try
			{
				Thread.sleep(limitWait-timeSinceLast);
			}
			catch(InterruptedException e) { /* I don't even know when this is ever thrown... */ }
		
		//Send request
		Response response = sendRequest(requestUrl);
		lastCall = System.currentTimeMillis();
		
		//Manage request queue
		trimRequestQueue();
		requestQueue.add(lastCall);
		
		//Unlock to let the next request through
		rateLock.unlock();
		
		return response;
	}
	
	private void trimRequestQueue()
	{
		for(Iterator<Long> it = requestQueue.iterator(); it.hasNext();)
		{
			if(lastCall-it.next() > REQUEST_QUEUE_TIME_LIMIT)
				it.remove();
			else
				break;
		}
	}
	
	//Accessors and modifiers
	
	public int getLimitPer10Seconds()
	{
		return limitPer10Seconds;
	}
	
	public void setLimitPer10Seconds(int limitPer10Seconds)
	{
		this.limitPer10Seconds = limitPer10Seconds;
		limitWait = (int)(10.0/limitPer10Seconds*1000);
	}
	
	public int getLimitPer10Minutes()
	{
		return limitPer10Minutes;
	}
	
	public void setLimitPer10Minutes(int limitPer10Minutes)
	{
		this.limitPer10Minutes = limitPer10Minutes;
	}
	
	public String getUserAgent()
	{
		return userAgent;
	}
	
	public void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}
	
	public int getRequestsInPast10Minutes()
	{
		trimRequestQueue();
		return requestQueue.size();
	}
	
	public long getOldestRequestTime()
	{
		return requestQueue.peek();
	}
	
	public void setRateLimiterEnabled(boolean enabled)
	{
		limiterEnabled = enabled;
	}
	
	public void clearCache()
	{
		cache.clear();
	}
}
