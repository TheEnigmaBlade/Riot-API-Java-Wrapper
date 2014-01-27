package net.enigmablade.riotapi.types;

import java.util.*;

import net.enigmablade.riotapi.constants.*;

/**
 * A set of stat summaries for a player.
 * 
 * @author Enigma
 */
public class PlayerStats
{
	private QueueType summaryType;
	private Date modifyDate;
	private int wins, losses;
	private Map<String, Integer> stats;
	
	public PlayerStats(String summaryType, long modifyDate, int wins, int losses, Map<String, Integer> stats)
	{
		this.summaryType = QueueType.getFromStatsValue(summaryType);
		this.modifyDate = new Date(modifyDate);
		this.wins = wins;
		this.losses = losses;
		this.stats = stats;
	}
	
	//Accessor methods
	
	public QueueType getSummaryType()
	{
		return summaryType;
	}

	public Date getModifyDate()
	{
		return modifyDate;
	}

	public int getWins()
	{
		return wins;
	}

	public int getLosses()
	{
		return losses;
	}

	public int getAggregatedStat(AggregatedStatType statType)
	{
		return stats.get(statType.getValue());
	}
}
