package net.enigmablade.riotapi;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.zip.*;
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
	public static final String GET_METHOD = "GET", POST_METHOD = "POST";
	public static final String GZIP_ENCODING = "gzip", DEFLATE_ENCODING = "deflate", GZIP_DEFLATE_ENCODING = GZIP_ENCODING+","+DEFLATE_ENCODING, DEFAULT_ENCODING = null;
	public static final Map<String, String> DEFAULT_HEADERS;
	
	static
	{
		DEFAULT_HEADERS = new HashMap<>();
		DEFAULT_HEADERS.put("Accept-Charset", "UTF-8");
	}
	
	//Options
	private String userAgent;
	private String protocol, method, encoding;
	private Map<String, String> headers;
	
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
		this(protocol, GET_METHOD, userAgent, limitPer10Seconds, limitPer10Minutes);
	}
	
	/**
	 * Create a new Requester with the given user agent and rate limits.
	 * @param protocol The HTTP protocol to use.
	 * @param method The HTTP method to use.
	 * @param userAgent The user agent for HTTP requests.
	 * @param limitPer10Seconds The limit for the number of requests per 10 seconds. Must be greater than 0.
	 * @param limitPer10Minutes The limit for the number of requests per 10 minutes. Must be greater than 0.
	 * @throws IllegalArgumentException If one of the arguments is not valid.
	 */
	public Requester(String protocol, String method, String userAgent, int limitPer10Seconds, int limitPer10Minutes)
	{
		if(!HTTP_PROTOCOL.equals(protocol) && !HTTPS_PROTOCOL.equals(protocol))
			throw new IllegalArgumentException("A valid protocol must be specified.");
		
		this.protocol = protocol;
		setMethod(method);
		setUserAgent(userAgent);
		setHeaders(DEFAULT_HEADERS);
		setEncoding(DEFAULT_ENCODING);
		
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
		return request(requestUrl, null);
	}
	
	/**
	 * Sends a request to the server at the given URL with an optional request body and returns the response.
	 * @param requestUrl The request URL.
	 * @param requestBody The optional request body.
	 * @return The response from the request.
	 * @throws IOException If there was an error when sending the request.
	 */
	public synchronized Response request(String requestUrl, String requestBody)
	{
		return requestHelper(requestUrl, requestBody);
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
	
	private Response requestHelper(String requestUrl, String requestBody)
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
		response = sendLimitedRequest(requestUrl, requestBody);
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
	
	private Response sendRequest(String requestUrl, String requestBody)
	{
		HttpURLConnection connection = null;
		try
		{
			//Create and connect
			URL url = new URL(requestUrl);
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(method);
			if(userAgent != null)
				connection.setRequestProperty("User-Agent", userAgent);
			for(String headerKey : headers.keySet())
				connection.setRequestProperty(headerKey, headers.get(headerKey));
			
			//Send body if present
			if(requestBody != null)
			{
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Length", String.valueOf(requestBody.length()));
				
				try(OutputStream out = connection.getOutputStream())
				{
					out.write(requestBody.getBytes(StandardCharsets.UTF_8));
				}
			}
			
			//Check response code and get appropriate input stream
			InputStream in;
			
			int responseCode = connection.getResponseCode();
			if(responseCode >= 300)
				in = connection.getErrorStream();
			else
				in = connection.getInputStream();
			
			//Check for optional encoding
			String responseEncoding = connection.getHeaderField("Content-Encoding");
			if(GZIP_ENCODING.equalsIgnoreCase(responseEncoding))
				in = new GZIPInputStream(in);
			else if(DEFLATE_ENCODING.equalsIgnoreCase(responseEncoding))
				in = new InflaterInputStream(in, new Inflater(true));
			else if(responseEncoding != null)
				System.err.println("Unknown HTTP encoding \""+responseEncoding+"\"");
			
			//Get response
			String responseText = IOUtil.readInputStreamFully(in);
			return new Response(responseText, responseCode);
		}
		catch(IOException e)
		{
			System.err.println("Failed to send request: IOException");
			e.printStackTrace();
			return new Response(null, -1);
		}
		finally
		{
			if(connection != null)
				connection.disconnect();
		}
	}
	
	private Response sendLimitedRequest(String requestUrl, String requestBody)
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
			Response response = sendRequest(requestUrl, requestBody);
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
		if(limitPer10Seconds < 0)
			throw new IllegalArgumentException("Rate limits must be greater than or equal to 0.");
		limitWait = limitPer10Seconds == 0 ? 0 : 10000/(this.limitPer10Seconds = limitPer10Seconds);
	}
	
	public synchronized int getLimitPer10Minutes()
	{
		return limitPer10Minutes;
	}
	
	public synchronized void setLimitPer10Minutes(int limitPer10Minutes)
	{
		if(limitPer10Minutes < 0)
			throw new IllegalArgumentException("Rate limits must be greater than or equal to 0.");
		
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
	
	public synchronized String getMethod()
	{
		return method;
	}
	
	public synchronized void setMethod(String method)
	{
		if(!GET_METHOD.equals(method) && !POST_METHOD.equals(method))
			throw new IllegalArgumentException("A valid method must be specified.");
		
		this.method = method;
	}
	
	public synchronized String getEncoding()
	{
		return encoding;
	}
	
	public synchronized void setEncoding(String encoding)
	{
		if(encoding != null && !GZIP_ENCODING.equals(encoding) && !DEFLATE_ENCODING.equals(encoding) && !GZIP_DEFLATE_ENCODING.equals(encoding))
			throw new IllegalArgumentException("A valid encoding must be specified (or null for no encoding).");
		
		this.encoding = encoding;
		
		if(encoding != null)
			headers.put("Accept-Encoding", encoding);
		else
			headers.remove("Accept-Encoding");
	}
	
	public synchronized Map<String, String> getHeaders()
	{
		return headers;
	}
	
	public synchronized void setHeaders(Map<String, String> headers)
	{
		this.headers = headers;
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
	
	/**
	 * Sets whether or not request responses should be cached.
	 * @param enabled Whether or not caching is enabled.
	 */
	public synchronized void setCacheEnabled(boolean enabled)
	{
		cacheEnabled = enabled;
	}
	
	/**
	 * Returns whether or not request responses are cached.
	 * @return <code>true</code> if requests responses are cached, otherwise <code>false</code>.
	 */
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
