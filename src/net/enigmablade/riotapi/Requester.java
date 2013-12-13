package net.enigmablade.riotapi;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.*;
import net.enigmablade.jsonic.*;

import net.enigmablade.riotapi.util.*;

/**
 * A utility class to send HTTP get requests. Requests are limited on a per-second basis.
 * @author Enigma
 *
 */
public class Requester
{
	private String userAgent;
	
	private int limitPer10Seconds;
	private long limitWait, lastCall;
	private Lock rateLock;
	
	private int limitPer10Minutes;
	private Queue<Long> requestQueue;
	private static final long REQUEST_QUEUE_TIME_LIMIT = 60000;	//10 minutes
	
	private BufferPool<String, Response> cache;
	
	public Requester(String userAgent)
	{
		this(userAgent, 5, 50);
	}
	
	public Requester(String userAgent, int limitPer10Seconds, int limitPer10Minutes)
	{
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
		
		protected Response(Object value, int code)
		{
			this.value = value;
			this.code = code;
		}
		
		public Object getValue()
		{
			return value;
		}
		
		public int getCode()
		{
			return code;
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
			if(response != null)
				return response;
		}
		
		//Otherwise send the request
		response = sendLimitedRequest(requestUrl);
		if(response.getValue() == null)
			return response;
		
		//Parse the request
		try
		{
			JsonObject json = JsonParser.parseObject((String)response.getValue(), speedy);
			response.value = json;
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
		connection.setRequestProperty("User-Agent", userAgent);
		connection.setRequestProperty("Accept-Charset", "UTF-8");
		connection.connect();
		
		//Check response code and return if error
		int responseCode = connection.getResponseCode();
		if(responseCode >= 300)
			return new Response(null, responseCode);
		
		//Get response
		InputStream in = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer buffer = new StringBuffer();
		String line;
		while((line = reader.readLine()) != null)
			buffer.append(line);
		return new Response(buffer.toString(), responseCode);
	}
	
	private Response sendLimitedRequest(String requestUrl) throws IOException
	{
		//Lock to prevent multiple requests from executing at once
		rateLock.lock();
		
		//Wait (if required) for the request time limit
		long timeSinceLast = System.currentTimeMillis()-lastCall;
		if(timeSinceLast < limitWait)
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
}
