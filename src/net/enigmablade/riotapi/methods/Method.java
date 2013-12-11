package net.enigmablade.riotapi.methods;

import java.io.*;
import java.util.*;
import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.Requester.Response;

public abstract class Method
{
	private String header;
	private String version;
	private String method;
	
	private Requester requester;
	
	public Method(Requester requester, String header, String method, String version, Region[] supportedRegions)
	{
		this.requester = requester;
		this.header = header;
		this.method = method;
		this.version = version;
	}
	
	protected Response getMethodResult(Region region, Map<String, String> arguments) throws RiotApiException
	{
		return getMethodResult(region, null, arguments);
	}
	
	protected Response getMethodResult(Region region, String operation, Map<String, String> arguments) throws RiotApiException
	{
		//If not null, prep for the URL, otherwise it's empty
		if(operation != null)
			operation = "/"+operation;
		else
			operation = "";
		
		//Create string of arguments (&arg=argv&arg2=arg2v...)
		StringBuffer argumentStr = new StringBuffer();
		if(arguments != null)
		{
			for(String key : arguments.keySet())
				argumentStr.append('&').append(key).append('=').append(arguments.get(key));
		}
		
		//Create URL and send the request
		String url = "http://prod.api.pvp.net/"+header+"/"+region.getApiUsage()+"/v"+version+"/"+method+operation+"?api_key="+requester.getApiKey()+argumentStr;
		try
		{
			Response response = requester.request(url);
			if(response == null)	//null if parse exception
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
	
	protected static Map<String, String> createArgumentMap(String... args)
	{
		if(args.length % 2 > 0)
			throw new IllegalArgumentException("Arguments must be in pairs");
		
		Map<String, String> argMap = new HashMap<>();
		for(int n = 0; n < args.length; n++)
			argMap.put(args[n], String.valueOf(args[++n]));
		return argMap;
	}
}
