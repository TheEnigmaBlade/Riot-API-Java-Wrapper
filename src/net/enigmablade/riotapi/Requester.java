package net.enigmablade.riotapi;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.*;
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
	
	private int limitShort, limitLong;
	private long limitShortInterval, limitLongInterval;
	private DelayQueue<RequestLock> requestQueueShort, requestQueueLong;
	private Lock rateLock;
	
	//Caching
	private boolean cacheEnabled = true;
	
	private BufferPool<String, Response> cache;
	private static final int CACHE_AGE_LIMIT = 600000;	//10 minutes
	
	/**
	 * Create a new HTTPS Requester with the given user agent and rate limits.
	 * @param userAgent The user agent for HTTP requests.
	 * @param shortLimit The limit for the number of requests per 10 seconds. Must be greater than 0.
	 * @param longLimit The limit for the number of requests per 10 minutes. Must be greater than 0.
	 */
	public Requester(String userAgent, int shortLimit, long shortInterval, TimeUnit shortIntervalUnit, int longLimit, long longInterval, TimeUnit longIntervalUnit)
	{
		this(HTTPS_PROTOCOL, userAgent, shortLimit, shortInterval, shortIntervalUnit, longLimit, longInterval, longIntervalUnit);
	}
	
	/**
	 * Create a new Requester with the given user agent and rate limits.
	 * @param protocol The HTTP protocol to use.
	 * @param userAgent The user agent for HTTP requests.
	 * @param limitPer10Seconds The limit for the number of requests per 10 seconds. Must be greater than 0.
	 * @param limitPer10Minutes The limit for the number of requests per 10 minutes. Must be greater than 0.
	 * @throws IllegalArgumentException If one of the arguments is not valid.
	 */
	public Requester(String protocol, String userAgent, int shortLimit, long shortInterval, TimeUnit shortIntervalUnit, int longLimit, long longInterval, TimeUnit longIntervalUnit)
	{
		this(protocol, GET_METHOD, userAgent, shortLimit, shortInterval, shortIntervalUnit, longLimit, longInterval, longIntervalUnit);
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
	public Requester(String protocol, String method, String userAgent, int shortLimit, long shortInterval, TimeUnit shortIntervalUnit, int longLimit, long longInterval, TimeUnit longIntervalUnit)
	{
		if(!HTTP_PROTOCOL.equals(protocol) && !HTTPS_PROTOCOL.equals(protocol))
			throw new IllegalArgumentException("A valid protocol must be specified.");
		
		this.protocol = protocol;
		setMethod(method);
		setUserAgent(userAgent);
		setHeaders(DEFAULT_HEADERS);
		setEncoding(DEFAULT_ENCODING);
		
		setShortLimit(shortLimit, shortInterval, shortIntervalUnit);
		setLongLimit(longLimit, longInterval, longIntervalUnit);
		
		rateLock = new ReentrantLock(true);
		requestQueueShort = new DelayQueue<>();
		requestQueueLong = new DelayQueue<>();
		
		cache = new BufferPool<>(longLimit);
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
	 * Checks if a request response is already in the cache, otherwise sends a new request.
	 * @param requestUrl The requests's URL
	 * @param requestBody The requests's body
	 * @return The response to the request (possibly from the cache)
	 */
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
	
	/**
	 * Sends a request to the server.
	 * @param requestUrl The requests's URL
	 * @param requestBody The requests's body
	 * @return The response to the request
	 */
	private Response sendRequest(String requestUrl, String requestBody)
	{
		HttpURLConnection connection = null;
		try
		{
			//Create and connect
			URL url = new URL(requestUrl);
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(method);
			////Set user agent
			if(userAgent != null)
				connection.setRequestProperty("User-Agent", userAgent);
			////Set encoding
			if(encoding != null)
				connection.setRequestProperty("Accept-Encoding", encoding);
			////Other request properties
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
			
			String responseText = null;
			
			if(in != null)
			{
				//Check for optional encoding
				String responseEncoding = connection.getHeaderField("Content-Encoding");
				if(GZIP_ENCODING.equalsIgnoreCase(responseEncoding))
					in = new GZIPInputStream(in);
				else if(DEFLATE_ENCODING.equalsIgnoreCase(responseEncoding))
					in = new InflaterInputStream(in, new Inflater(true));
				else if(responseEncoding != null)
					System.err.println("Unknown HTTP encoding \""+responseEncoding+"\"");
				
				//Get response
				responseText = IOUtil.readInputStreamFully(in);
			}
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
	
	/**
	 * Sends a request to the server, enforcing rate limits if enabled.
	 * @param requestUrl The requests's URL
	 * @param requestBody The requests's body
	 * @return The response to the request
	 */
	private Response sendLimitedRequest(String requestUrl, String requestBody)
	{
		//Lock to prevent multiple requests from executing at once
		rateLock.lock();
		
		try
		{
			//Wait (if required) for the request time limit
			if(limiterEnabled)
			{
				try
				{
					if(requestQueueShort.size() == limitShort)
						requestQueueShort.take();
					if(requestQueueLong.size() == limitLong)
						requestQueueLong.take();
				}
				catch(InterruptedException e)
				{
					// Dunno lol
				}
			}
			
			//Send request
			Response response = sendRequest(requestUrl, requestBody);
			
			//Add a request locks
			requestQueueShort.add(new RequestLock(limitShortInterval));
			requestQueueLong.add(new RequestLock(limitLongInterval));
			
			return response;
		}
		finally
		{
			//Unlock to let the next request through
			rateLock.unlock();
		}
	}
	
	/**
	 * Drain expired requests from the request queues.
	 */
	private void drainRequestQueues()
	{
		rateLock.lock();
		
		Collection<RequestLock> drain = new LinkedList<>();
		if(requestQueueShort.size() >= limitShort)
			requestQueueShort.drainTo(drain);
		if(requestQueueLong.size() >= limitLong)
			requestQueueLong.drainTo(drain);
		
		rateLock.unlock();
	}
	
	//Accessors and modifiers
	
	/**
	 * Returns the rate limit for the short time interval.
	 * @return The short interval rate limit
	 */
	public synchronized int getShortLimit()
	{
		return limitShort;
	}
	
	/**
	 * Returns short time interval for rate limiting.
	 * @return The short interval
	 */
	public synchronized long getShortInterval()
	{
		return limitShortInterval;
	}
	
	/**
	 * Sets the rate limiting information for the short interval.
	 * @param limit The rate limit
	 * @param interval The time interval
	 * @param unit The time interval unit
	 */
	public synchronized void setShortLimit(int limit, long interval, TimeUnit unit)
	{
		if(limit < 0 || interval < 0)
			throw new IllegalArgumentException("Rate limits must be greater than or equal to 0.");
		this.limitShort = limit;
		this.limitShortInterval = TimeUnit.NANOSECONDS.convert(interval, unit);
	}
	
	/**
	 * Returns the rate limit for the long time interval.
	 * @return The long interval rate limit
	 */
	public synchronized int getLongLimit()
	{
		return limitLong;
	}
	
	/**
	 * Returns long time interval for rate limiting.
	 * @return The long interval
	 */
	public synchronized long getLongInterval()
	{
		return limitLongInterval;
	}
	
	/**
	 * Sets the rate limiting information for the long interval.
	 * @param limit The rate limit
	 * @param interval The time interval
	 * @param unit The time interval unit
	 */
	public synchronized void setLongLimit(int limit, long interval, TimeUnit unit)
	{
		if(limit < 0 || interval < 0)
			throw new IllegalArgumentException("Rate limits must be greater than or equal to 0.");
		this.limitLong = limit;
		this.limitLongInterval = TimeUnit.NANOSECONDS.convert(interval, unit);
	}
	
	/**
	 * Returns the user agent being sent with requests.
	 * @return The requester user agent
	 */
	public synchronized String getUserAgent()
	{
		return userAgent;
	}
	
	/**
	 * Sets the user agent to be sent with requests.
	 * @param userAgent
	 */
	public synchronized void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}
	
	/**
	 * Returns the protocol being used by the requester (HTTP or HTTPS).
	 * @return The request protocol
	 * @see #HTTP_PROTOCOL
	 * @see #HTTPS_PROTOCOL
	 */
	public synchronized String getProtocol()
	{
		return protocol;
	}
	
	/**
	 * Sets the protocol being used by the requester.
	 * @return The new request protocol
	 * @see #HTTP_PROTOCOL
	 * @see #HTTPS_PROTOCOL
	 */
	public synchronized void setProtocol(String method)
	{
		if(!HTTP_PROTOCOL.equals(method) && !HTTPS_PROTOCOL.equals(method))
			throw new IllegalArgumentException("A valid protocol must be specified.");
		
		this.method = method;
	}
	
	/**
	 * Returns the method being used to the requester.
	 * @return The requester method
	 * @see #GET_METHOD
	 * @see #POST_METHOD
	 */
	public synchronized String getMethod()
	{
		return method;
	}
	
	/**
	 * Sets the method being used by the requester.
	 * @param method The new requester method
	 * @see #GET_METHOD
	 * @see #POST_METHOD
	 */
	public synchronized void setMethod(String method)
	{
		if(!GET_METHOD.equals(method) && !POST_METHOD.equals(method))
			throw new IllegalArgumentException("A valid method must be specified.");
		
		this.method = method;
	}
	
	/**
	 * Returns the encoding being used by the requester.
	 * @return The requester encoding
	 * @see #GZIP_ENCODING
	 * @see #DEFLATE_ENCODING
	 * @see #GZIP_DEFLATE_ENCODING
	 * @see #DEFAULT_ENCODING
	 */
	public synchronized String getEncoding()
	{
		return encoding;
	}
	
	/**
	 * Returns the encoding being used by the requester.
	 * @param encoding The new requester encoding
	 * @see #GZIP_ENCODING
	 * @see #DEFLATE_ENCODING
	 * @see #GZIP_DEFLATE_ENCODING
	 * @see #DEFAULT_ENCODING
	 */
	public synchronized void setEncoding(String encoding)
	{
		if(encoding != null && !GZIP_ENCODING.equals(encoding) && !DEFLATE_ENCODING.equals(encoding) && !GZIP_DEFLATE_ENCODING.equals(encoding))
			throw new IllegalArgumentException("A valid encoding must be specified (or null for no encoding).");
		
		this.encoding = encoding;
	}
	
	/**
	 * Returns the headers being sent with a request.
	 * @return The request headers
	 */
	public synchronized Map<String, String> getHeaders()
	{
		return headers;
	}
	
	/**
	 * Overwrites <i>all</i> request headers being sent with a request.
	 * @param headers The new request headers
	 */
	public synchronized void setHeaders(Map<String, String> headers)
	{
		this.headers = headers;
	}
	
	/**
	 * Returns the number of requests that have been sent within the past short interval.
	 * @return The number of short interval requests
	 */
	public synchronized int getRequestsInPastShortInterval()
	{
		drainRequestQueues();
		return requestQueueShort.size();
	}
	
	/**
	 * Returns the number of requests that have been sent within the past long interval.
	 * @return The number of long interval requests
	 */
	public synchronized int getRequestsInPastLongInterval()
	{
		drainRequestQueues();
		return requestQueueLong.size();
	}
	
	/**
	 * Sets whether or not rate limiting is enforced.
	 * @param enabled Whether or not rate limiting is enforced
	 */
	public synchronized void setRateLimitEnabled(boolean enabled)
	{
		limiterEnabled = enabled;
	}
	
	/**
	 * Returns whether or not rate limiting is enforced.
	 * @return <code>true</code> if rate limiting is enforced, otherwise <code>false</code>
	 */
	public synchronized boolean isRateLimitEnabled()
	{
		return limiterEnabled;
	}
	
	/**
	 * Resets rate limits.
	 */
	public synchronized void clearRateLimit()
	{
		requestQueueShort.clear();
		requestQueueLong.clear();
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
	
	/**
	 * Delayed Lock used for request limiting.
	 * 
	 * @author Enigma
	 */
	private class RequestLock implements Delayed
	{
		private long delay, start;
		
		/**
		 * Create a new request lock with a delay in nanoseconds.
		 * @param delay Lock delay in nanoseconds
		 */
		public RequestLock(long delay)
		{
			this.delay = delay;
			start = System.nanoTime();
		}
		
		@Override
		public int compareTo(Delayed o)
		{
			if(o == null)
				return 1;
			return Long.compare(getDelay(TimeUnit.NANOSECONDS), o.getDelay(TimeUnit.NANOSECONDS));
		}
		
		@Override
		public long getDelay(TimeUnit unit)
		{
			return unit.convert(delay - (System.nanoTime() - start), TimeUnit.NANOSECONDS);
		}
	}
}
