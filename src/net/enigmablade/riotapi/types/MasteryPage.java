package net.enigmablade.riotapi.types;

import java.util.*;

/**
 * Information about a League of Legends mastery page.
 * 
 * @author Enigma
 */
public class MasteryPage
{
	/**
	 * Information about an individual talent in a mastery page.
	 * 
	 * @author Enigma
	 */
	public static class Mastery
	{
		private int id;
		private int rank;
		
		public Mastery(int id, int rank)
		{
			this.id = id;
			this.rank = rank;
		}
		
		public int getId()
		{
			return id;
		}
		
		public int getRank()
		{
			return rank;
		}
	}
	
	private long id;
	private String name;
	private List<Mastery> talents;
	private boolean current;
	
	public MasteryPage(long id, String name, List<Mastery> talents, boolean current)
	{
		this.name = name;
		this.talents = talents;
		if(this.talents == null)
			this.talents = new ArrayList<>(0);
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
	
	public List<Mastery> getTalents()
	{
		return talents;
	}
	
	public boolean isCurrent()
	{
		return current;
	}
}
