package net.enigmablade.riotapi;

import java.io.*;
import java.util.*;

import net.enigmablade.riotapi.Requester.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.exceptions.*;

/**
 * <p>Abstract class to represent a method request that can be sent to the Riot API servers.</p>
 * <p>Executing a method requires a region and optionally an additional operation, operation path arguments, or query arguments.</p>
 * 
 * @author Enigma
 */
public abstract class Method
{
	private RiotApi api;
	private boolean skipCache;
	
	private String header;
	private String version;
	private String method;
	private Region[] supportedRegions;
	
	/**
	 * Created a new method defined by the given information.
	 * @param api The API instance being used.
	 * @param apiKey The API key to use for requests.
	 * @param header The header of the method. Usually "api" or "api/lol".
	 * @param method The name of the method. Ex. "champion" or "summoner"
	 * @param version The version of the method.
	 * @param supportedRegions The regions supported by the method.
	 */
	public Method(RiotApi api, String header, String method, String version, Region[] supportedRegions)
	{
		this.api = api;
		skipCache = false;
		
		this.header = header;
		this.method = method;
		this.version = version;
		this.supportedRegions = supportedRegions;
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
	 * @param pathArgs The optional path arguments. Values in the operation surrounded by { } will be replaced.
	 * @return The response from the server.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	protected Response getMethodResult(Region region, String operation, Map<String, String> pathArgs) throws RiotApiException
	{
		return getMethodResult(region, operation, pathArgs, null);
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
	protected Response getMethodResult(Region region, String operation, Map<String, String> pathArgs, Map<String, String> queryArgs) throws RiotApiException
	{
		//Check to make sure the requested region is supported
		if(!isRegionSupported(region))
			throw new RegionNotSupportedException(method, region, supportedRegions);
		
		//If not null, prepare for the URL, otherwise it's empty
		if(operation != null)
			operation = "/"+operation;
		else
			operation = "";
		
		//Format path arguments within the operation
		int start, end;
		while((start = operation.indexOf('{')) >= 0)
		{
			if((end = operation.indexOf('}')) > 0)	//Sanity check
			{
				String part1 = operation.substring(0, start);
				String key = operation.substring(start+1, end);
				String part2 = operation.substring(end+1, operation.length());
				
				StringBuffer buf = new StringBuffer(part1);
				buf.append(pathArgs.get(key));
				buf.append(part2);
				operation = buf.toString();
			}
		}
		
		//Create string of query arguments (&arg=argv&arg2=arg2v...)
		StringBuffer queryArgsStr = new StringBuffer();
		if(queryArgs != null)
		{
			for(String key : queryArgs.keySet())
				queryArgsStr.append('&').append(key).append('=').append(queryArgs.get(key));
		}
		
		//Create URL and send the request
		String url = "http://prod.api.pvp.net/"+header+"/"+region.getApiUsage()+"/v"+version+"/"+method+operation+"?api_key="+api.getApiKey()+queryArgsStr.toString();
		try
		{
			Response response = api.getRequester().request(url, skipCache);
			if(response == null)	//null if parse exception, highly unlikely
				throw new RiotApiException("Failed to parse response");
			
			//Errors common to all methods
			if(response.getCode() == 400)
				throw new RiotApiException("400: Bad request");
			else if(response.getCode() == 500)
				throw new RiotApiException("500: Internal server error");
			
			return response;
		}
		catch(IOException e)
		{
			throw new RiotApiException("", e);
		}
	}
	
	//Accessor and modifier methods
	
	public boolean isSkippingCache()
	{
		return skipCache;
	}
	
	public void setSkipCache(boolean skipCache)
	{
		this.skipCache = skipCache;
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
