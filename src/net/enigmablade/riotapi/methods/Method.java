package net.enigmablade.riotapi.methods;

import java.util.*;
import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.Requester.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.exceptions.*;
import net.enigmablade.riotapi.util.*;

/**
 * <p>Abstract class to represent a method request that can be sent to the Riot API servers.</p>
 * <p>Executing a method requires a region and optionally an additional operation, operation path arguments, or query arguments.</p>
 * 
 * @author Enigma
 */
abstract class Method
{
	//Data
	protected RiotApi api;
	
	private String header;
	private String version;
	private String method;
	private Region[] supportedRegions;
	
	private String customEndpoint;
	private boolean useUnsecure;
	
	private int maxThings = 40;
	
	//Constructors
	
	/**
	 * Created a new method defined by the given information.
	 * @param api The API instance being used.
	 * @param header The header of the method. Usually "api" or "api/lol".
	 * @param method The name of the method. Ex. "champion" or "summoner"
	 * @param version The version of the method.
	 * @param supportedRegions The regions supported by the method.
	 */
	protected Method(RiotApi api, String header, String method, String version, Region[] supportedRegions)
	{
		this.api = api;
		
		this.header = header;
		this.method = method;
		this.version = version;
		this.supportedRegions = supportedRegions;
	}
	
	protected Method(RiotApi api, String customEndpoint, boolean useUnsecure, String header, String method, String version, Region[] supportedRegions)
	{
		this.api = api;
		
		this.header = header;
		this.method = method;
		this.version = version;
		this.supportedRegions = supportedRegions;
		
		this.customEndpoint = customEndpoint;
		this.useUnsecure = useUnsecure;
	}
	
	/**
	 * Execute a request and get the result. The response's value may be null if a method-specific error occurred.
	 * @return The response from the server.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	protected Response getMethodResult() throws RiotApiException
	{
		return getMethodResult(null, null, true);
	}
	
	/**
	 * Execute a request and get the result. The response's value may be null if a method-specific error occurred.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @return The response from the server.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	protected Response getMethodResult(Region region) throws RiotApiException
	{
		return getMethodResult(region, null, null, null);
	}
	
	/**
	 * Execute a request and get the result. The response's value may be null if a method-specific error occurred.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param queryArgs The optional query arguments. Appended to the end of a request (arg1=arg1v&arg2=arg2v&...)
	 * @return The response from the server.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	protected Response getMethodResult(Region region, Map<String, String> queryArgs) throws RiotApiException
	{
		return getMethodResult(region, null, null, queryArgs);
	}
	
	/**
	 * Execute a request and get the result. The response's value may be null if a method-specific error occurred.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param operation The optional operation to perform. Leave <code>null</code> to only execute the method.
	 * @return The response from the server.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	protected Response getMethodResult(Region region, String operation) throws RiotApiException
	{
		return getMethodResult(region, operation, null, null);
	}
	
	/**
	 * Execute a request and get the result. The response's value may be null if a method-specific error occurred.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param operation The optional operation to perform. Leave <code>null</code> to only execute the method.
	 * @param pathArgs The optional path arguments. Values in the operation surrounded by { } will be replaced.
	 * @return The response from the server.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	protected Response getMethodResult(Region region, String operation, Map<String, String> pathArgs) throws RiotApiException
	{
		return getMethodResult(region, operation, pathArgs, null);
	}
	
	protected Response getMethodResult(Region region, String operation, Map<String, String> pathArgs, Map<String, String> queryArgs) throws RiotApiException
	{
		return getMethodResult(region, operation, false, pathArgs, queryArgs);
	}
	
	protected Response getMethodResult(Region region, String operation, boolean isGlobal) throws RiotApiException
	{
		return getMethodResult(region, operation, isGlobal, null, null);
	}
	
	/**
	 * Execute a request and get the result. The response's value may be null if a method-specific error occurred.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param operation The optional operation to perform. Leave <code>null</code> to only execute the method.
	 * @param pathArgs The optional path arguments. Values in the operation surrounded by { } will be replaced.
	 * @param queryArgs The optional query arguments. Appended to the end of a request (arg1=arg1v&arg2=arg2v&...)
	 * @return The response from the server.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	protected Response getMethodResult(Region region, String operation, boolean isGlobal, Map<String, String> pathArgs, Map<String, String> queryArgs) throws RiotApiException
	{
		//Check to make sure the requested region is supported
		if(!isGlobal && !isRegionSupported(region))
			throw new RegionNotSupportedException(method, region, supportedRegions);
		
		//Create request URL
		String url = buildUrl(region, operation, pathArgs, queryArgs, isGlobal);
		
		//Send request
		Requester requester = api.getRequester();
		Response response = requester.request(url);
		if(response == null)	//null if parse exception, highly unlikely
			throw new RiotApiException("Uh oh, failed to parse response! That's bad!");
		
		//Everything is fine and dandy
		if(response.getCode() == 200)
			return response;
		
		//Errors common to all methods
		switch(response.getCode())
		{
			case 400: throw new RiotApiException("400: Bad request (theoretically shouldn't happen)");
			case 401: throw new RiotApiException("401: Unauthorized");
			case 429: throw new TooManyRequestsException("");
			
			case 500: throw new RiotApiException("500: Internal server error");
			case 503: throw new RiotApiException("503: Service unavailable (someone broke it)");
			
			default: return response;
				//throw new RiotApiException("Unknown error code "+response.getCode());
		}
	}
	
	//Helper methods
	
	/**
	 * Return whether or not a region is supported by this method.
	 * @param region The region being checked.
	 * @return <code>true</code> if the region is supported, otherwise <code>false</code>.
	 */
	public boolean isRegionSupported(Region region)
	{
		for(Region r : supportedRegions)
			if(region == r)
				return true;
		return false;
	}
	
	protected void checkAmountOfThings(String[] a, String thingType)
	{
		if(a == null || a.length > maxThings)
			throw new IllegalArgumentException("Too many "+thingType+"s, limit of "+maxThings);
	}
	
	protected void checkAmountOfThings(long[] a, String thingType)
	{
		if(a == null || a.length > maxThings)
			throw new IllegalArgumentException("Too many "+thingType+"s, limit of "+maxThings);
	}
	
	protected void setMaxThings(int maxThings)
	{
		this.maxThings = maxThings;
	}
	
	/**
	 * <p>Build a RESTful URL for the current method in the Riot API from the given information.<p>
	 * <p><b>Warning</b>: Some arguments are optional, and required arguments are not checked. See argument descriptions for more information.</p>
	 * @param region <i>Required</i> - The region in which the method is being called.
	 * @param operation <i>Optional</i> - The more specific operation of the method being called.
	 * @param pathArgs <i>Required if <code>region</code> contains path arguments</i> - A map of path arguments being replaced in the method.
	 * @param queryArgs <i>Optional</i> - A map of query arguments.
	 * @return The nicely formatted request URL.
	 */
	private String buildUrl(Region region, String operation, Map<String, String> pathArgs, Map<String, String> queryArgs, boolean useGlobal)
	{
		//Format path arguments within the operation
		if(operation != null && pathArgs != null)
			operation = IOUtil.replacePathArgs(operation, pathArgs);
		
		//Create string of query arguments (arg=argv&arg2=arg2v...)
		String queryArgsStr = IOUtil.genQueryArgs(queryArgs);
		
		//Create URL and send the request
		StringBuilder s = new StringBuilder(useUnsecure ? Requester.HTTP_PROTOCOL : Requester.HTTPS_PROTOCOL).append("://");
		if(customEndpoint != null)
			s.append(customEndpoint).append('/');
		else
			s.append((useGlobal ? Region.GLOBAL.getEndpoint() : region.getEndpoint())).append('/');			//Domain endpoint
		s.append(header);									//Header
		if(region != null)
			s.append('/').append(region.getValue());		//Region
		if(version != null)
			s.append("/v").append(version);					//Version
		if(method != null)
			s.append('/').append(method);					//Method
		if(operation != null)								//Operation (optional)
			s.append('/').append(operation);
		if(!useUnsecure)
		{
			s.append("?api_key=").append(api.getApiKey());		//API key
			s.append('&').append(queryArgsStr);					//Query args
		}
		return s.toString();
	}
	
	/**
	 * Return new mapping to be used for the path and query arguments.
	 * @param args The argument pairs. Place the key before the value.
	 * @return The argument map.
	 */
	protected static Map<String, String> createArgMap(String... args)
	{
		if(args.length % 2 > 0)
			throw new IllegalArgumentException("Arguments must be in pairs");
		
		Map<String, String> argMap = new HashMap<>();
		for(int n = 0; n < args.length; n++)
			argMap.put(args[n], String.valueOf(args[++n]));
		return argMap;
	}
}
