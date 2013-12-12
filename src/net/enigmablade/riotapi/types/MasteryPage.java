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
	public static class Talent
	{
		private int id;
		private String name;
		private int rank;
		
		public Talent(int id, String name, int rank)
		{
			this.id = id;
			this.name = name;
			this.rank = rank;
		}
		
		public int getId()
		{
			return id;
		}
		
		public String getName()
		{
			return name;
		}
		
		public int getRank()
		{
			return rank;
		}
	}
	
	private String name;
	private List<Talent> talents;
	private boolean current;
	
	public MasteryPage(String name, List<Talent> talents, boolean current)
	{
		this.name = name;
		this.talents = talents;
		if(this.talents == null)
			this.talents = new ArrayList<>(0);
		this.current = current;
	}
	
	public String getName()
	{
		return name;
	}
	
	public List<Talent> getTalents()
	{
		return talents;
	}
	
	public boolean isCurrent()
	{
		return current;
	}
}
