package net.enigmablade.riotapi.types;

import java.util.*;
import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.constants.*;

/**
 * <p>Information about a League of Legends game/match.</p>
 * 
 * <p><b>Note: may be merged with Team.Stat in the future once the API is expanded.</b></p>
 * 
 * @author Enigma
 */
public class Game implements Comparable<Game>
{
	private RiotApi api;
	private Region region;
	
	private int championId;				//championId
	private int level;					//level
	private int spell1, spell2;			//spell1, spell2
	
	private Date playedDate;			//createDate
	private boolean invalid;			//invalid
	
	private long gameId;				//gameId
	private GameMode gameMode;			//gameMode
	private GameType gameType;			//gameType
	private QueueType gameSubType;		//subType
	private MapType map;				//mapId
	
	private int teamId;					//teamId
	private List<Player> players;		//fellowPlayers
	private Map<String, Object> stats;	//statistics
	
	public Game(RiotApi api, Region region, int championId, int level, int spell1, int spell2, long playedDate, boolean invalid, long gameId, String gameMode, String gameType, String gameSubType, int mapId, int teamId, List<Player> players, Map<String, Object> stats)
	{
		this.api = api;
		this.region = region;
		
		this.championId = championId;
		this.level = level;
		this.spell1 = spell1;
		this.spell2 = spell2;
		this.playedDate = new Date(playedDate);
		this.invalid = invalid;
		this.gameId = gameId;
		this.gameMode = GameMode.getFromValue(gameMode);
		this.gameType = GameType.getFromValue(gameType);
		this.gameSubType = QueueType.getFromGameValue(gameSubType);
		this.map = MapType.getFromId(mapId);
		this.teamId = teamId;
		this.players = players;
		this.stats = stats;
	}
	
	//Accessor methods
	
	public Region getRegion()
	{
		return region;
	}
	
	/**
	 * Returns the champion played by the target summoner of the request.
	 * @return The played champion ID.
	 */
	public Champion getChampion()
	{
		return new Champion(api, region, championId);
	}
	
	/**
	 * Returns the level of the target summoner of the request.
	 * @return The summoner level.
	 */
	public int getSummonerLevel()
	{
		return level;
	}
	
	/**
	 * Returns the first summoner spell used by the target summoner of the request.
	 * @return The first summoner spell.
	 */
	public int getSpell1()
	{
		return spell1;
	}
	
	/**
	 * Returns the second summoner spell used by the target summoner of the request.
	 * @return The second summoner spell.
	 */
	public int getSpell2()
	{
		return spell2;
	}
	
	/**
	 * Returns the ID of the team on which the target summoner of the request played.
	 * @return The summoner's team ID.
	 */
	public int getTeamId()
	{
		return teamId;
	}
	
	/**
	 * Returns the date on which the game was played.
	 * @return The played date.
	 */
	public Date getPlayedDate()
	{
		return playedDate;
	}
	
	/**
	 * Returns a list of players who participated in the game.
	 * @return A list of players.
	 */
	public List<Player> getPlayers()
	{
		return players;
	}
	
	/**
	 * Returns the game's ID.
	 * @return The game's ID.
	 */
	public long getGameId()
	{
		return gameId;
	}
	
	/**
	 * Returns the game's mode.
	 * @return The game's mode.
	 */
	public GameMode getGameMode()
	{
		return gameMode;
	}
	
	/**
	 * Returns the game's type.
	 * @return The game's type.
	 */
	public GameType getGameType()
	{
		return gameType;
	}
	
	/**
	 * Returns the game's sub-type, which is normally represented as a queue type.
	 * @return The game's sub-type.
	 */
	public QueueType getGameSubType()
	{
		return gameSubType;
	}
	
	/**
	 * Returns the map on which the game was played.
	 * @return The game's map.
	 */
	public MapType getMapType()
	{
		return map;
	}
	
	/**
	 * Returns whether or not the game is invalid (not sure what it means).
	 * @return <code>true</code> if the game is invalid, otherwise <code>false</code>.
	 */
	public boolean isInvalid()
	{
		return invalid;
	}
	
	//Stat accessor methods
	
	/**
	 * Returns a boolean represented by the given key.
	 * If the value doesn't exist, returns <code>false</code>.
	 * If the value exists, assumes the value is a valid boolean.
	 * @param key The stat key.
	 * @return The boolean value.
	 */
	private boolean getBooleanStat(String key)
	{
		if(stats.containsKey(key))
			return (Boolean)stats.get(key);
		return false;
	}
	
	/**
	 * Returns an integer represented by the given key.
	 * If the value doesn't exist, returns <code>0</code>.
	 * If the value exists, assumes the value is a valid integer.
	 * @param key The stat key.
	 * @return The integer value.
	 */
	private int getIntStat(String key)
	{
		if(stats.containsKey(key))
			return ((Long)stats.get(key)).intValue();
		return 0;
	}
	
	public int getLevel()
	{
		return getIntStat("level");
	}
	
	public int getAssists()
	{
		return getIntStat("assists");
	}
	
	public int getBarracksKilled()
	{
		return getIntStat("barracksKilled");
	}
	
	public int getChampionsKilled()
	{
		return getIntStat("championsKilled");
	}
	
	public int getCombatPlayerScore()
	{
		return getIntStat("combatPlayerScore");
	}
	
	public int getConsumablesPurchased()
	{
		return getIntStat("consumablesPurchased");
	}
	
	public int getDamageDealt()
	{
		return getIntStat("damageDealtPlayer");
	}
	
	public int getDoubleKills()
	{
		return getIntStat("doubleKills");
	}
	
	public boolean getGotFirstBlood()
	{
		return getIntStat("firstBlood") > 0;
	}
	
	public int getGoldLeft()
	{
		return getIntStat("gold");
	}
	
	public int getGoldEarned()
	{
		return getIntStat("goldEarned");
	}
	
	public int getGoldSpent()
	{
		return getIntStat("goldSpent");
	}
	
	public List<Integer> getItems()
	{
		List<Integer> items = new ArrayList<>(7);
		int item;
		for(int n = 0; n < 7; n++)
		{
			if((item = getIntStat("item"+n)) > 0)
				items.add(item);
		}
		return items;
	}
	
	public int getNumItemsPurchased()
	{
		return getIntStat("itemsPurchased");
	}
	
	public int getNumKillingSprees()
	{
		return getIntStat("killingSprees");
	}
	
	public int getLargestCriticalStrike()
	{
		return getIntStat("largestCriticalStrike");
	}
	
	public int getLargestKillingSpree()
	{
		return getIntStat("largestKillingSpree");
	}
	
	public int getLargestMultiKill()
	{
		return getIntStat("largestMultiKill");
	}
	
	public int getNumLegendaryItemsPurchased()
	{
		return getIntStat("legendaryItemsCreated");
	}
	
	public int getTotalMagicDamageDealt()
	{
		return getIntStat("magicDamageDealtPlayer");
	}
	
	public int getMagicDamageDealtToChampions()
	{
		return getIntStat("magicDamageDealtToChampions");
	}
	
	public int getMagicDamageDealtToMinions()
	{
		return getTotalMagicDamageDealt()-getMagicDamageDealtToChampions();
	}
	
	public int getMagicDamageTaken()
	{
		return getIntStat("magicDamageTaken");
	}
	
	@Deprecated
	public int getNumMinionsDenied()
	{
		return getIntStat("minionsDenied");
	}
	
	public int getTotalMinionsKilled()
	{
		return getIntStat("minionsKilled");
	}
	
	public int getEnemyMinionsKilled()
	{
		return getTotalMinionsKilled()-getNeutralMinionsKilled();
	}
	
	public int getNeutralMinionsKilled()
	{
		return getIntStat("neutralMinionsKilled");
	}
	
	public int getNeutralMinionsKilledInEnemyJungle()
	{
		return getIntStat("neutralMinionsKilledEnemyJungle");
	}
	
	public int getNeutralMinionsKilledInAlliedJungle()
	{
		return getIntStat("neutralMinionsKilledYourJungle");
	}
	
	public boolean killedNexus()
	{
		return getBooleanStat("nexusKilled");
	}
	
	public int getDominionPointCaptures()
	{
		return getIntStat("nodeCapture");
	}
	
	public int getDominionPointCaptureAssists()
	{
		return getIntStat("nodeCaptureAssist");
	}
	
	public int getDominionPointNeutralized()
	{
		return getIntStat("nodeNeutralize");
	}
	
	public int getDominionPointNeutralizeAssists()
	{
		return getIntStat("nodeNeutralizeAssist");
	}
	
	public int getDeaths()
	{
		return getIntStat("numDeaths");
	}
	
	public int getNumItemsBought()
	{
		return getIntStat("numItemsBought");
	}
	
	public int getObjectivePlayerScore()
	{
		return getIntStat("objectivePlayerScore");
	}
	
	public int getPentaKills()
	{
		return getIntStat("pentaKills");
	}
	
	public int getTotalPhysicalDamageDealth()
	{
		return getIntStat("physicalDamageDealtPlayer");
	}
	
	public int getPhysicalDamageDealtToChampions()
	{
		return getIntStat("physicalDamageDealtToChampions");
	}
	
	public int getPhysicalDamageDealtToOther()
	{
		return getTotalPhysicalDamageDealth()-getPhysicalDamageDealtToChampions();
	}
	
	public int getPhysicalDamageTaken()
	{
		return getIntStat("physicalDamageTaken");
	}
	
	public int getQuadraKills()
	{
		return getIntStat("quadraKills");
	}
	
	public int getNumSightWardsBought()
	{
		return getIntStat("sightWardsBought");
	}
	
	public int getNumTimesSpell1Cast()
	{
		return getIntStat("spell1Cast");
	}
	
	public int getNumTimesSpell2Cast()
	{
		return getIntStat("spell2Cast");
	}
	
	public int getNumTimesSpell3Cast()
	{
		return getIntStat("spell3Cast");
	}
	
	public int getNumTimesSpell4Cast()
	{
		return getIntStat("spell4Cast");
	}
	
	public int getTotalNumTimesSpellsCast()
	{
		return getNumTimesSpell1Cast()+getNumTimesSpell2Cast()+getNumTimesSpell3Cast()+getNumTimesSpell4Cast();
	}
	
	public int getNumTimesSummonerSpell1Cast()
	{
		return getIntStat("summonSpell1Cast");
	}
	
	public int getNumTimesSummonerSpell2Cast()
	{
		return getIntStat("summonSpell2Cast");
	}
	
	public int getTotalTimesSummonerSpellsCast()
	{
		return getNumTimesSummonerSpell1Cast()+getNumTimesSummonerSpell2Cast();
	}
	
	public int getSuperMinionsKilled()
	{
		return getIntStat("superMonsterKilled");
	}
	
	public int getTeam()
	{
		return getIntStat("team");
	}
	
	public int getTeamObjective()
	{
		return getIntStat("teamObjective");
	}
	
	public int getLength()
	{
		return getIntStat("timePlayed");
	}
	
	public int getTotalDamageDealt()
	{
		return getIntStat("totalDamageDealt");
	}
	
	public int getTotalDamageDealtToChampions()
	{
		return getIntStat("totalDamageDealtToChampions");
	}
	
	public int getTotalDamageDealtToOther()
	{
		return getTotalDamageDealt()-getTotalDamageDealtToChampions();
	}
	
	public int getTotalDamageTaken()
	{
		return getIntStat("totalDamageTaken");
	}
	
	public int getTotalHealed()
	{
		return getIntStat("totalHeal");
	}
	
	public int getTotalPlayerScore()
	{
		return getIntStat("totalPlayerScore");
	}
	
	public int getScoreRank()
	{
		return getIntStat("totalScoreRank");
	}
	
	public int getTotalCrowdControlTimeDealt()
	{
		return getIntStat("totalTimeCrowdControlDealt");
	}
	
	public int getNumUnitsHealed()
	{
		return getIntStat("totalUnitsHealed");
	}
	
	public int getTripleKills()
	{
		return getIntStat("tripleKills");
	}
	
	public int getTotalTrueDamageDealt()
	{
		return getIntStat("trueDamageDealtPlayer");
	}
	
	public int getTrueDamageDealtToChampions()
	{
		return getIntStat("trueDamageDealtToChampions");
	}
	
	public int getTrueDamageDealtToOther()
	{
		return getTotalTrueDamageDealt()-getTrueDamageDealtToChampions();
	}
	
	public int getTrueDamageTaken()
	{
		return getIntStat("trueDamageTaken");
	}
	
	public int getTurretsKilled()
	{
		return getIntStat("turretsKilled");
	}
	
	public int getUnrealKills()
	{
		return getIntStat("unrealKills");
	}
	
	public int getTotalVictoryPoints()
	{
		return getIntStat("victoryPointTotal");
	}
	
	public int getVisionWardsBought()
	{
		return getIntStat("visionWardsBought");
	}
	
	public int getWardsKilled()
	{
		return getIntStat("wardKilled");
	}
	
	public int getWardsPlaced()
	{
		return getIntStat("wardPlaced");
	}
	
	public boolean isWin()
	{
		return getBooleanStat("win");
	}
	
	//Other methods
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null || !(o instanceof Game))
			return false;
		return gameId == ((Game)o).gameId;
	}
	
	@Override
	public int hashCode()
	{
		return (int)gameId;
	}
	
	@Override
	public int compareTo(Game o)
	{
		if(equals(o))
			return 0;
		return playedDate.compareTo(o.playedDate);
	}
	
	@Override
	public String toString()
	{
		return "Game "+gameId+" ("+playedDate+")";
	}
}
