package net.enigmablade.riotapi;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.*;
import net.enigmablade.jsonic.*;

public class Requester
{
	private String apiKey;
	
	private int limitPer10Seconds;
	private long limitWait, lastCall;
	private Lock rateLock;
	
	private int limitPer10Minutes;
	private Queue<Long> requestQueue;
	private static final long REQUEST_QUEUE_TIME_LIMIT = 60000;	//10 minutes
	
	private String userAgent;
	
	public Requester(String apiKey, String userAgent)
	{
		this(5, 50, apiKey, userAgent);
	}
	
	public Requester(int limitPer10Seconds, int limitPer10Minutes, String apiKey, String userAgent)
	{
		setLimitPer10Seconds(limitPer10Seconds);
		setLimitPer10Minutes(limitPer10Minutes);
		setApiKey(apiKey);
		setUserAgent(userAgent);
		
		rateLock = new ReentrantLock(true);
		requestQueue = new LinkedList<>();
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
	
	public Response request(String requestUrl, boolean speedy) throws IOException
	{
		System.out.println("Sending request: "+requestUrl);
		
		Response response = sendLimitedRequest(requestUrl);
		if(response.getValue() == null)
			return response;
		
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
		//URLEncoder.encode(requestUrl, "UTF-8")
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
			catch(InterruptedException e) { /* I don't even know when this is ever thrown... */	}
		
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
		limitWait = limitPer10Seconds/10*1000;
	}
	
	public int getLimitPer10Minutes()
	{
		return limitPer10Minutes;
	}
	
	public void setLimitPer10Minutes(int limitPer10Minutes)
	{
		this.limitPer10Minutes = limitPer10Minutes;
	}
	
	public String getApiKey()
	{
		return apiKey;
	}
	
	public void setApiKey(String apiKey)
	{
		this.apiKey = apiKey;
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
