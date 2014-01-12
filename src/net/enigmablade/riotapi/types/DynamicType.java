package net.enigmablade.riotapi.types;

import net.enigmablade.riotapi.*;

public abstract class DynamicType
{
	protected RiotApi api;
	private boolean updated;
	
	public DynamicType(RiotApi api)
	{
		this.api = api;
		
		updated = false;
	}
	
	protected boolean hasDynamicUpdated()
	{
		return updated;
	}
	
	protected void setDynamicUpdated()
	{
		updated = true;
	}
}
