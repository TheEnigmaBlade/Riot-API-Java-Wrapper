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
					switch(Character.toLowerCase(c))
					{
						case 'w': return WON;
						case 'l': return LOST;
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
		
		private LeagueTier tier, rank;
		private QueueType queueType;
		private String leagueName;
		
		private String playerOrTeamId, playerOrTeamName;
		private boolean isHotStreak, isFreshBlood, isVeteran, isInactive;
		private int wins;
		private int leaguePoints;
		private Series series;
		private Date lastPlayed;
		
		public Entry(String tier, String rank, String queueType, String leagueName, String string, String playerOrTeamName, boolean isHotStreak, boolean isFreshBlood, boolean isVeteran, boolean isInactive, int wins, int leaguePoints, Series series, long lastPlayed)
		{
			this.tier = LeagueTier.stringToConstant(tier);
			this.rank = LeagueTier.stringToConstant(rank);
			this.queueType = QueueType.getFromGameValue(queueType);
			this.leagueName = leagueName;
			this.playerOrTeamId = string;
			this.playerOrTeamName = playerOrTeamName;
			this.isHotStreak = isHotStreak;
			this.isFreshBlood = isFreshBlood;
			this.isVeteran = isVeteran;
			this.isInactive = isInactive;
			this.wins = wins;
			this.leaguePoints = leaguePoints;
			this.series = series;
			this.lastPlayed = new Date(lastPlayed);
		}
		
		//Accessor methods
		
		/**
		 * Returns the league's tier:
		 * TIER_CHALLENGER, TIER_DIAMOND, TIER_PLATINUM, TIER_GOLD, TIER_SILVER, or TIER_BRONZE.
		 * @return The league's tier.
		 */
		public LeagueTier getTier()
		{
			return tier;
		}
		
		public LeagueTier getRank()
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
			
		public int getLeaguePoints()
		{
			return leaguePoints;
		}
		
		public Series getSeries()
		{
			return series;
		}
		
		public Date getLastPlayed()
		{
			return lastPlayed;
		}
	}
	
	private String name;
	private String participantId;
	private QueueType queueType;
	private LeagueTier tier;
	private List<Entry> entries;
	
	public League(String name, String participantId,  String queueType, String tier, List<Entry> entries)
	{
		this.name = name;
		this.participantId = participantId;
		this.queueType = QueueType.getFromGameValue(queueType);
		this.tier = LeagueTier.stringToConstant(tier);
		this.entries = entries;
	}
	
	//Accessor methods
	
	/**
	 * Returns the name of the league.
	 * @return The league's name.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns the ID of the participant in the league.
	 * For solo queue leagues, it's a summoner ID as a string.
	 * For team leagues, it's a team ID. 
	 * @return The participant ID. 
	 */
	public String getParticipantId()
	{
		return participantId;
	}
	
	/**
	 * Returns the queue 
	 * @return
	 */
	public QueueType getQueueType()
	{
		return queueType;
	}
	
	public LeagueTier getTier()
	{
		return tier;
	}
	
	public List<Entry> getEntries()
	{
		return entries;
	}
}
