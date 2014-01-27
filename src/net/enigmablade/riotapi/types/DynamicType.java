package net.enigmablade.riotapi.types;

import net.enigmablade.riotapi.*;

abstract class DynamicType
{
	protected RiotApi api;
	private boolean updated;
	
	protected DynamicType(RiotApi api)
	{
		this.api = api;
		
		updated = false;
	}
	
	protected boolean hasTypeUpdated()
	{
		return updated;
	}
	
	protected void setTypeUpdated()
	{
		updated = true;
	}
}
