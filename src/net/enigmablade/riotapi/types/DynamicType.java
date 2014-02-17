package net.enigmablade.riotapi.types;

import java.util.*;
import net.enigmablade.riotapi.*;

/**
 * A simple class to keep track of whether or not a dynamic type (a type that can update its stored information dynamically) has been updated.
 * Includes support tracking multiple parts of a type at once, such as a Champion that gets information from multiple sources.
 * 
 * @author Enigma
 */
abstract class DynamicType
{
	protected RiotApi api;
	private boolean[] updated;
	
	protected DynamicType(RiotApi api)
	{
		this(api, 1);
	}
	
	protected DynamicType(RiotApi api, int num)
	{
		this.api = api;
		
		updated = new boolean[num];
		Arrays.fill(updated, false);
	}
	
	protected boolean hasTypeUpdated()
	{
		return updated[0];
	}
	
	protected void setTypeUpdated()
	{
		updated[0] = true;
	}
	
	protected boolean hasTypeUpdated(int index)
	{
		if(index >= 0 && index < updated.length)
			return updated[0];
		return false;
	}
	
	protected void setTypeUpdated(int index)
	{
		if(index >= 0 && index < updated.length)
			updated[0] = true;
	}
}
