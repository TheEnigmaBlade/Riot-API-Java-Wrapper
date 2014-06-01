package net.enigmablade.riotapi.types;

import java.util.*;

/**
 * Information about a League of Legends rune page.
 * 
 * @author Enigma
 */
public class RunePage
{
	/**
	 * Information about a single slot in a rune page.
	 * 
	 * @author Enigma
	 */
	public static class Slot
	{
		private int id;
		private Rune rune;
		
		public Slot(int id, Rune rune)
		{
			this.id = id;
			this.rune = rune;
		}
		
		public int getId()
		{
			return id;
		}
		
		public Rune getRune()
		{
			return rune;
		}
	}
	
	/**
	 * Information about a rune that can fill a slot in a mastery page.
	 * 
	 * @author Enigma
	 */
	public static class Rune
	{
		private int id;
		
		public Rune(int id)
		{
			this.id = id;
		}
		
		public int getId()
		{
			return id;
		}
	}
	
	private long id;
	private String name;
	private List<Slot> slots;
	private boolean current;
	
	public RunePage(long id, String name, List<Slot> slots, boolean current)
	{
		this.id = id;
		this.name = name;
		this.slots = slots;
		if(this.slots == null)
			this.slots = new ArrayList<>(0);
		this.current = current;
	}
	
	public long getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public List<Slot> getRuneSlots()
	{
		return slots;
	}
	
	public boolean isCurrent()
	{
		return current;
	}
}
