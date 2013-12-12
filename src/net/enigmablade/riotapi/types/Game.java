package net.enigmablade.riotapi.types;

import java.util.*;

import net.enigmablade.riotapi.constants.*;

/**
 * Information about a League of Legends game.
 * 
 * @author Enigma
 */
public class Game
{
	/**
	 * A simple representation of a game statistic.
	 * 
	 * @author Enigma
	 */
	public static class Stat
	{
		private int id, value;
		private String name;
		
		public Stat(int id, String name, int value)
		{
			this.id = id;
			this.name = name;
			this.value = value;
		}
		
		public int getId()
		{
			return id;
		}
		
		public int getValue()
		{
			return value;
		}
		
		public String getName()
		{
			return name;
		}
	}
	
	private int championId;			//championId
	private int level;				//level
	private int spell1, spell2;		//spell1, spell2
	
	private Date createDate;		//createDate
	private boolean invalid;		//invalid
	
	private long gameId;			//gameId
	private GameMode gameMode;		//gameMode
	private GameType gameType;		//gameType
	private String gameSubType;		//subType
	private MapType map;			//mapId
	
	private int teamId;				//teamId
	private List<Player> players;	//fellowPlayers
	private List<Stat> stats;		//statistics
	
	public Game(int championId, int level, int spell1, int spell2, long createDate, boolean invalid, long gameId, String gameMode, String gameType, String gameSubType, int mapId, int teamId, List<Player> players, List<Stat> stats)
	{
		this.championId = championId;
		this.level = level;
		this.spell1 = spell1;
		this.spell2 = spell2;
		this.createDate = new Date(createDate);
		this.invalid = invalid;
		this.gameId = gameId;
		this.gameMode = GameMode.getFromValue(gameMode);
		this.gameType = GameType.getFromValue(gameType);
		this.gameSubType = gameSubType;
		this.map = MapType.getFromId(mapId);
		this.teamId = teamId;
		this.players = players;
		this.stats = stats;
	}
	
	//Accessor methods
	
	public int getChampionId()
	{
		return championId;
	}
	
	public Date getCreateDate()
	{
		return createDate;
	}
	
	public List<Player> getPlayers()
	{
		return players;
	}
	
	public long getGameId()
	{
		return gameId;
	}
	
	public GameMode getGameMode()
	{
		return gameMode;
	}
	
	public GameType getGameType()
	{
		return gameType;
	}
	
	public String getGameSubType()
	{
		return gameSubType;
	}
	
	public MapType getMapType()
	{
		return map;
	}
	
	public boolean isInvalid()
	{
		return invalid;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public int getSpell1()
	{
		return spell1;
	}
	
	public int getSpell2()
	{
		return spell2;
	}
	
	public int getTeamId()
	{
		return teamId;
	}
	
	public List<Stat> getStats()
	{
		return stats;
	}
}
