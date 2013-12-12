package net.enigmablade.riotapi.types;

import java.util.*;
import net.enigmablade.riotapi.constants.*;

/**
 * Information about a League of Legends league.
 * Can be a player-based league of a team-based league.
 * 
 * @author Enigma
 */
public class League
{
	/**
	 * A simple representation of a league entry.
	 *  
	 * @author Enigma
	 */
	public static class Entry
	{
		/**
		 * A simple representation of a league entry's series.
		 *  
		 * @author Enigma
		 */
		public static class Series
		{
			/**
			 * The state of a game in a series.
			 * 
			 * @author Enigma
			 */
			public static enum Progress
			{
				WON, LOST, NOT_PLAYED;
				
				public static Progress charToProgress(char c)
				{
					switch(c)
					{
						case 'W': return WON;
						case 'L': return LOST;
						default: return NOT_PLAYED;
					}
				}
			};
			
			private int targetWins;
			private int numWins, numLosses;
			private Progress[] progress;
			private long timeLeftToPlayMillis;
			
			public Series(int targetWins, int numWins, int numLosses, String progress, long timeLeftToPlayMillis)
			{
				this.targetWins = targetWins;
				this.numWins = numWins;
				this.numLosses = numLosses;
				this.timeLeftToPlayMillis = timeLeftToPlayMillis;
				
				this.progress = new Progress[progress.length()];
				for(int n = 0; n < progress.length(); n++)
					this.progress[n] = Progress.charToProgress(progress.charAt(n));
			}
			
			//Accessor methods
			
			public int getTargetWins()
			{
				return targetWins;
			}
			
			public int getNumWins()
			{
				return numWins;
			}
			
			public int getNumLosses()
			{
				return numLosses;
			}
			
			public Progress[] getProgress()
			{
				return progress;
			}
			
			public long getTimeLeftToPlayMillis()
			{
				return timeLeftToPlayMillis;
			}
		}
		
		private LeagueConstant tier, rank;
		private QueueType queueType;
		private String leagueName;
		
		private String playerOrTeamId, playerOrTeamName;
		private boolean isHotStreak, isFreshBlood, isVeteran, isInactive;
		private int wins, losses;
		private int leaguePoints;
		private Series series;
		private long lastPlayed, timeUntilDecay;
		
		public Entry(String tier, String rank, String queueType, String leagueName, String string, String playerOrTeamName, boolean isHotStreak, boolean isFreshBlood, boolean isVeteran, boolean isInactive, int wins, int losses, int leaguePoints, Series series, long lastPlayed, long timeUntilDecay)
		{
			this.tier = LeagueConstant.stringToConstant(tier);
			this.rank = LeagueConstant.stringToConstant(rank);
			this.queueType = QueueType.getFromValue(queueType);
			this.leagueName = leagueName;
			this.playerOrTeamId = string;
			this.playerOrTeamName = playerOrTeamName;
			this.isHotStreak = isHotStreak;
			this.isFreshBlood = isFreshBlood;
			this.isVeteran = isVeteran;
			this.isInactive = isInactive;
			this.wins = wins;
			this.losses = losses;
			this.leaguePoints = leaguePoints;
			this.series = series;
			this.lastPlayed = lastPlayed;
			this.timeUntilDecay = timeUntilDecay;
		}
		
		//Accessor methods
		
		public LeagueConstant getTier()
		{
			return tier;
		}
		
		public LeagueConstant getRank()
		{
			return rank;
		}
		
		public QueueType getQueueType()
		{
			return queueType;
		}
		
		public String getLeagueName()
		{
			return leagueName;
		}
		
		public String getPlayerOrTeamId()
		{
			return playerOrTeamId;
		}
		
		public String getPlayerOrTeamName()
		{
			return playerOrTeamName;
		}
		
		public boolean isHotStreak()
		{
			return isHotStreak;
		}
		
		public boolean isFreshBlood()
		{
			return isFreshBlood;
		}
		
		public boolean isVeteran()
		{
			return isVeteran;
		}
		
		public boolean isInactive()
		{
			return isInactive;
		}
		
		public int getWins()
		{
			return wins;
		}
			
		public int getLosses()
		{
			return losses;
		}
		
		public int getLeaguePoints()
		{
			return leaguePoints;
		}
		
		public Series getSeries()
		{
			return series;
		}
		
		public long getLastPlayed()
		{
			return lastPlayed;
		}
		
		public long getTimeUntilDecay()
		{
			return timeUntilDecay;
		}
	}
	
	private LeagueConstant tier;
	private String name;
	private QueueType queueType;
	private long timestamp;
	private List<Entry> entries;
	
	public League(String tier, String name, String queueType, long timestamp, List<Entry> entries)
	{
		this.tier = LeagueConstant.stringToConstant(tier);
		this.name = name;
		this.queueType = QueueType.getFromValue(queueType);
		this.timestamp = timestamp;
		this.entries = entries;
	}
	
	//Accessor methods
	
	public LeagueConstant getTier()
	{
		return tier;
	}
	
	public String getName()
	{
		return name;
	}
	
	public QueueType getQueueType()
	{
		return queueType;
	}
	
	public long getTimestamp()
	{
		return timestamp;
	}
	
	public List<Entry> getEntries()
	{
		return entries;
	}
}
