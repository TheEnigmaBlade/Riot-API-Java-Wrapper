package net.enigmablade.riotapi.types;

import net.enigmablade.riotapi.*;

public abstract class ApiType
{
	protected RiotApi api;
	private boolean updated;
	
	public ApiType(RiotApi api)
	{
		this.api = api;
		
		updated = false;
	}
	
	protected boolean hasApiUpdated()
	{
		return updated;
	}
	
	protected void setApiUpdated()
	{
		updated = true;
	}
}
