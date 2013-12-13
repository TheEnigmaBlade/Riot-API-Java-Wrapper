package net.enigmablade.riotapi.types;

import java.util.*;

/**
 * A set of champion stats for a ranked season.
 * 
 * @author Enigma
 */
public class ChampionStats
{
	/**
	 * A single champion stat.
	 * 
	 * @author Enigma
	 */
	public static class Stat
	{
		private int id;
		private String name;
		private int value;
		private int count;
		
		public Stat(int id, String name, int value, int count)
		{
			this.id = id;
			this.name = name;
			this.value = value;
			this.count = count;
		}
		
		public int getId()
		{
			return id;
		}
		
		public String getName()
		{
			return name;
		}
		
		public int getValue()
		{
			return value;
		}
		
		public int getCount()
		{
			return count;
		}
	}
	
	private int championId;
	private String championName;
	private List<Stat> stats;
	
	public ChampionStats(int championId, String championName, List<Stat> stats)
	{
		this.championId = championId;
		this.championName = championName;
		this.stats = stats;
	}
	
	public int getChampionId()
	{
		return championId;
	}
	
	public String getChampionName()
	{
		return championName;
	}
	
	public List<Stat> getStats()
	{
		return stats;
	}	
}
