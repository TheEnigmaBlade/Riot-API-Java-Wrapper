package net.enigmablade.riotapi.types;

import java.util.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.exceptions.*;

/**
 * Information about a team.
 * 
 * @author Enigma
 */
public class Team
{
	/**
	 * Information about a team's set of statistics for a queue.
	 * 
	 * @author Enigma
	 */
	public static class QueueStat
	{
		private int wins, losses, averageGamesPlayed;
		private int rating, maxRating, seedRating;
		
		public QueueStat(int wins, int losses, int averageGamesPlayed, int rating, int maxRating, int seedRating)
		{
			this.wins = wins;
			this.losses = losses;
			this.averageGamesPlayed = averageGamesPlayed;
			this.rating = rating;
			this.maxRating = maxRating;
			this.seedRating = seedRating;
		}
		
		//Accessor methods
		
		public int getWins()
		{
			return wins;
		}
		
		public int getLosses()
		{
			return losses;
		}
		
		public int getAverageGamesPlayed()
		{
			return averageGamesPlayed;
		}
		
		public int getRating()
		{
			return rating;
		}
		
		public int getMaxRating()
		{
			return maxRating;
		}
		
		public int getSeedRating()
		{
			return seedRating;
		}
	}
	
	/**
	 * Information about a team roster.
	 * 
	 * @author Enigma
	 */
	public static class Roster
	{
		public static class Member
		{
			private Summoner summoner;
			private Date inviteDate, joinDate;
			private String status;				//TODO: figure out possible statuses
			
			public Member(Summoner summoner, String status, long inviteDate, long joinDate)
			{
				this.summoner = summoner;
				this.status = status;
				this.inviteDate = new Date(inviteDate);
				this.joinDate = new Date(joinDate);
			}
			
			public Summoner getSummoner()
			{
				return summoner;
			}
			
			public Date getInviteDate()
			{
				return inviteDate;
			}
			
			public Date getJoinDate()
			{
				return joinDate;
			}
			
			public String getStatus()
			{
				return status;
			}
		}
		
		private Summoner owner;
		private List<Member> members;
		
		public Roster(List<Member> members, long owner) throws RiotApiException
		{
			this.members = members;
			for(Member member : members)
				if(member.getSummoner().getId() == owner)
				{
					this.owner = member.getSummoner();
					break;
				}
		}
		
		public Summoner getOwner()
		{
			return owner;
		}
		
		public List<Member> getMembers()
		{
			return members;
		}
	}
	
	/**
	 * <p>Information about a League of Legends game/match.</p>
	 * 
	 * <p><b>Note</b>: may be merged with Game in the future once the API is expanded.</p>
	 * 
	 * @author Enigma
	 */
	public static class Match
	{
		private long id;
		private GameMode gameMode;
		private MapType map;
		
		private int kills, deaths, assists;
		private boolean win;
		private boolean invalid;
		
		private String opposingTeamName;
		private int opposingTeamKills;
		
		public Match(long id, String gameMode, int map, int kills, int deaths, int assists, boolean win, boolean invalid, String opposingTeamName, int opposingTeamKills)
		{
			this.id = id;
			this.gameMode = GameMode.getFromValue(gameMode);
			this.map = MapType.getFromId(map);
			this.kills = kills;
			this.deaths = deaths;
			this.assists = assists;
			this.win = win;
			this.invalid = invalid;
			this.opposingTeamName = opposingTeamName;
			this.opposingTeamKills = opposingTeamKills;
		}

		public long getId()
		{
			return id;
		}

		public GameMode getGameMode()
		{
			return gameMode;
		}

		public MapType getMap()
		{
			return map;
		}

		public int getKills()
		{
			return kills;
		}

		public int getDeaths()
		{
			return deaths;
		}

		public int getAssists()
		{
			return assists;
		}

		public boolean isWin()
		{
			return win;
		}

		public boolean isInvalid()
		{
			return invalid;
		}

		public String getOpposingTeamName()
		{
			return opposingTeamName;
		}

		public int getOpposingTeamKills()
		{
			return opposingTeamKills;
		}
	}
	
	/**
	 * Information about a message of the day.
	 * 
	 * @author Enigma
	 */
	public static class MessageOfTheDay
	{
		private String message;
		private int version;
		private Date createDate;
		
		public MessageOfTheDay(String message, int version, long createDate)
		{
			this.message = message;
			this.version = version;
			this.createDate = new Date(createDate);
		}
		
		//Accessor methods
		
		public String getMessage()
		{
			return message;
		}
		
		public int getVersion()
		{
			return version;
		}
		
		public Date getCreateDate()
		{
			return createDate;
		}
	}
	
	private String id;
	private String name;
	private String tag;
	private String status;			//TODO: figure out possible statuses
	
	private Roster roster;
	private List<Match> matchHistory;
	private Map<QueueType, Team.QueueStat> stats;
	private MessageOfTheDay messageOfTheDay;
	
	private Date createDate, modifyDate;
	private Date lastGameDate, lastJoinedRankedTeamQueueDate;
	private Date lastJoinDate, secondLastJoinDate, thirdLastJoinDate;
	
	public Team(String id, String name, String tag, String status, Roster roster, List<Match> matchHistory, Map<QueueType, Team.QueueStat> stats, MessageOfTheDay messageOfTheDay, long createDate, long modifyDate, long lastGameDate, long lastJoinedRankedTeamQueueDate, long lastJoinDate, long secondLastJoinDate, long thirdLastJoinDate)
	{
		this.id = id;
		this.name = name;
		this.tag = tag;
		this.status = status;
		this.roster = roster;
		this.matchHistory = matchHistory;
		this.stats = stats;
		this.messageOfTheDay = messageOfTheDay;
		this.createDate = new Date(createDate);
		this.modifyDate = new Date(modifyDate);
		this.lastGameDate = new Date(lastGameDate);
		this.lastJoinedRankedTeamQueueDate = new Date(lastJoinedRankedTeamQueueDate);
		this.lastJoinDate = new Date(lastJoinDate);
		this.secondLastJoinDate = new Date(secondLastJoinDate);
		this.thirdLastJoinDate = new Date(thirdLastJoinDate);
	}
	
	//Accessor methods
	
	public String getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getTag()
	{
		return tag;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public Roster getRoster()
	{
		return roster;
	}
	
	public List<Match> getMatchHistory()
	{
		return matchHistory;
	}
	
	public Map<QueueType, QueueStat> getStats()
	{
		return stats;
	}
	
	/**
	 * Returns the message of the day, or <code>null</code> if one has not been set.
	 * @return The message of the day.
	 */
	public MessageOfTheDay getMessageOfTheDay()
	{
		return messageOfTheDay;
	}
	
	public Date getCreateDate()
	{
		return createDate;
	}
	
	public Date getModifyDate()
	{
		return modifyDate;
	}
	
	public Date getLastGameDate()
	{
		return lastGameDate;
	}
	
	public Date getLastJoinedRankedTeamQueueDate()
	{
		return lastJoinedRankedTeamQueueDate;
	}
	
	public Date getLastJoinDate()
	{
		return lastJoinDate;
	}
	
	public Date getSecondLastJoinDate()
	{
		return secondLastJoinDate;
	}
	
	public Date getThirdLastJoinDate()
	{
		return thirdLastJoinDate;
	}
}
