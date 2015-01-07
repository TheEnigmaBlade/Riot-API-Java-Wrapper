package net.enigmablade.riotapi.methods;

import java.util.*;
import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.Requester.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.exceptions.*;

public class UnlimitedMethod extends Method
{
	protected UnlimitedMethod(RiotApi api, String header, String method, String version, Region[] supportedRegions)
	{
		super(api, header, method, version, supportedRegions);
	}
	
	protected UnlimitedMethod(RiotApi api, String customEndpoint, boolean useUnsecure, String header, String method, String version, Region[] supportedRegions)
	{
		super(api, customEndpoint, useUnsecure, header, method, version, supportedRegions);
	}
	
	@Override
	protected Response getMethodResult(Region region, String operation, boolean isGlobal, Map<String, String> pathArgs, Map<String, String> queryArgs) throws RiotApiException
	{
		boolean save = api.isRateLimitEnabled();
		api.setRateLimitEnabled(false);
		Response response = super.getMethodResult(region, operation, isGlobal, pathArgs, queryArgs);
		api.setRateLimitEnabled(save);
		return response;
	}
}
