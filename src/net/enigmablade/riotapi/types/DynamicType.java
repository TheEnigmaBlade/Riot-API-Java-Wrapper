package net.enigmablade.riotapi.types;

import java.util.*;
import net.enigmablade.riotapi.*;

/**
 * A simple class to keep track of whether or not a dynamic type (a type that can update its stored information dynamically) has been updated.
 * Includes support tracking multiple parts of a type at once, such as a Champion that gets information from multiple sources.
 * 
 * @author Enigma
 */
public abstract class DynamicType
{
	protected RiotApi api;
	private Map<Object, Boolean> updated;
	
	private static final Object DEFAULT_KEY = new Object();
	
	protected DynamicType(RiotApi api)
	{
		this(api, 1);
	}
	
	protected DynamicType(RiotApi api, int num)
	{
		this.api = api;
		
		updated = new HashMap<>(num);
	}
	
	protected boolean hasTypeUpdated()
	{
		return updated.get(DEFAULT_KEY);
	}
	
	protected void setTypeUpdated()
	{
		updated.put(DEFAULT_KEY, true);
	}
	
	protected boolean hasTypeUpdated(Object key)
	{
		Boolean b = updated.get(key);
		return b != null ? b : false;
	}
	
	protected void setTypeUpdated(Object key)
	{
		updated.put(key, true);
	}
}
