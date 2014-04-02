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
		
		/**
		 * Returns the league's rank:
		 * RANK_V, RANK_IV, RANK_III, RANK_II, or RANK_I.
		 * @return The league's rank.
		 */
		public LeagueTier getRank()
		{
			return rank;
		}
		
		/**
		 * Returns the league's queue type.
		 * @return The queue type.
		 */
		public QueueType getQueueType()
		{
			return queueType;
		}
		
		/**
		 * Returns the name of the league.
		 * @return The league's name.
		 */
		public String getLeagueName()
		{
			return leagueName;
		}
		
		/**
		 * Returns the ID of the entry: a team ID if the league is comprised of teams or a summoner ID if the league is comprised of summoners.
		 * @return The team ID or summoner ID.
		 * @see QueueType#isTeamBased()
		 */
		public String getPlayerOrTeamId()
		{
			return playerOrTeamId;
		}
		
		/**
		 * Returns the name of the entry: a team name if the league is comprised of teams or a summoner name if the league is comprised of summoners.
		 * @return The team name or summoner name.
		 * @see QueueType#isTeamBased()
		 */
		public String getPlayerOrTeamName()
		{
			return playerOrTeamName;
		}
		
		/**
		 * Returns whether or not the entry is on a hot streak.
		 * @return <code>true</code> if the entry is on a hot streak, otherwise <code>false</code>.
		 */
		public boolean isHotStreak()
		{
			return isHotStreak;
		}
		
		/**
		 * Returns whether or not the entry is new to the league.
		 * @return <code>true</code> if the entry is new to the league, otherwise <code>false</code>.
		 */
		public boolean isFreshBlood()
		{
			return isFreshBlood;
		}
		
		/**
		 * Returns whether or not the entry is a veteran of the league.
		 * @return <code>true</code> if the entry is a veteran, otherwise <code>false</code>.
		 */
		public boolean isVeteran()
		{
			return isVeteran;
		}
		
		/**
		 * Returns whether or not the entry is inactive in the league.
		 * @return <code>true</code> if the entry is inactive, otherwise <code>false</code>.
		 */
		public boolean isInactive()
		{
			return isInactive;
		}
		
		/**
		 * Returns the number of wins the entry has within the league.
		 * @return The entry's wins.
		 */
		public int getWins()
		{
			return wins;
		}
		
		/**
		 * Returns the number of league points the entry has within the league.
		 * @return The entry's league points.
		 */
		public int getLeaguePoints()
		{
			return leaguePoints;
		}
		
		/**
		 * Returns information on the entry's series if it is in one.
		 * @return The entry's series information if it is in one, otherwise <code>null</code>.
		 */
		public Series getSeries()
		{
			return series;
		}
		
		/**
		 * Returns the date on which the entry last played in the league.
		 * @return The last played date.
		 */
		public Date getLastPlayed()
		{
			return lastPlayed;
		}
	}
	
	//Data
	
	private String name;
	private String participantId;
	private QueueType queueType;
	private LeagueTier tier;
	private Map<String, Entry> entries;
	
	//Constructors
	
	public League(String name, String participantId,  String queueType, String tier, Map<String, Entry> entries)
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
	 * Returns the league's queue type.
	 * @return The queue type.
	 */
	public QueueType getQueueType()
	{
		return queueType;
	}
	
	/**
	 * Returns the league's tier (bronze, silver, gold, etc.)
	 * @return The league's tier.
	 */
	public LeagueTier getTier()
	{
		return tier;
	}
	
	/**
	 * Returns the entries in the league. A league can consist of exclusively teams or summoners.
	 * @return The league's entries.
	 */
	public Map<String, Entry> getEntries()
	{
		return entries;
	}
	
	public Entry getEntry(String entryId)
	{
		return getEntries().get(entryId);
	}
}
