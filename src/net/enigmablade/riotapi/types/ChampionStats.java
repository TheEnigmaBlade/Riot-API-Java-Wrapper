package net.enigmablade.riotapi.types;

import java.util.*;

import net.enigmablade.riotapi.constants.*;

/**
 * A set of champion stats for a ranked season.
 * 
 * @author Enigma
 */
public class ChampionStats
{
	private int championId;
	private String championName;
	private Map<String, Integer> stats;
	
	public ChampionStats(int championId, String championName, Map<String, Integer> stats)
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
	
	public int getAggregatedStat(AggregatedStatType statType)
	{
		return stats.get(statType.getValue());
	}	
}
