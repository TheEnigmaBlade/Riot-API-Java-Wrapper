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
	/**
	 * An aggregated stat for a queue.
	 * 
	 * @author Enigma
	 */
	public static class AggregatedStat
	{
		private int id;
		private String name;
		private int count;
		
		public AggregatedStat(int id, String name, int count)
		{
			this.id = id;
			this.name = name;
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
		
		public int getCount()
		{
			return count;
		}
	}
	
	private QueueType summaryType;
	private Date modifyDate;
	private int wins, losses;
	private List<AggregatedStat> stats;
	
	public PlayerStats(String summaryType, long modifyDate, int wins, int losses, List<AggregatedStat> stats)
	{
		this.summaryType = QueueType.getFromStatsValue(summaryType);
		this.modifyDate = new Date(modifyDate);
		this.wins = wins;
		this.losses = losses;
		this.stats = stats;
	}

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

	public List<AggregatedStat> getStats()
	{
		return stats;
	}
}
