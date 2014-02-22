package net.enigmablade.riotapi.constants;

/**
 * Queue type constants as specified in the game constants page of the developer site.
 * 
 * Why so many different values for usage? I dunno.
 * 
 * @see <a href="https://developer.riotgames.com/docs/game-constants">Developer site</a>
 * 
 * @author Enigma
 */
public enum QueueType
{
	//Constant			ID		Game/League value		Stat value			Ranked	Team	Name
	NONE				(-1,	"NONE",					"None",				false, 	false,	"None"),
	NORMAL_5V5_BLIND	(2,		"NORMAL",				"Unranked",			false,	false,	"Normal 5v5 Blind"),
	NORMAL_5V5_DRAFT	(14,	"NORMAL",				"Unranked",			false,	false,	"Normal 5v5 Draft"),
	NORMAL_5V5_BOTS		(7,		"BOT",					"CoopVsAI",			false,	false,	"Normal Coop vs AI"),
	RANKED_5V5_SOLO		(4,		"RANKED_SOLO_5x5",		"RankedSolo5x5",	true,	false,	"Ranked 5v5 Solo"),
	RANKED_5V5_TEAM		(42,	"RANKED_TEAM_5x5",		"RankedTeam5x5",	true,	true,	"Ranked 5v5 Team"),
	NORMAL_3V3_BLIND	(8,		"NORMAL_3x3",			"Unranked3x3",		false,	false,	"Normal 3v3 Blind"),
	NORMAL_3V3_BOTS		(52,	"BOT_3x3", 				"CoopVsAI3x3",		false,	false,	"Normal 3v3 Coop vs AI"),
	RANKED_3V3_TEAM		(41,	"RANKED_TEAM_3x3",		"RankedTeam3x3",	true,	true,	"Ranked Team 3v3"),
	DOMINION_5V5_BLIND	(16,	"ODIN_UNRANKED", 		"OdinUnranked",		false,	false,	"Dominion 5v5 Blind"),
	DOMINION_5V5_DRAFT	(17,	"ODIN_UNRANKED", 		"OdinUnranked",		false,	false,	"Dominion 5v5 Draft"),
	DOMINION_5V5_BOTS	(25,	"ODIN_UNRANKED",		"CoopVsAI",			false,	false,	"Dominion Coop vs AI"),
	ARAM_5V5			(65,	"ARAM_UNRANKED_5x5",	"AramUnranked5x5",	false,	false,	"ARAM"),
	ARAM_5V5_BOTS		(67,	"ARAM_UNRANKED_5x5",	"AramUnranked5x5",	false,	false,	"ARAM Coop vs AI"),
	ONE_FOR_ALL_5V5		(70,	"ONEFORALL_5x5",		"OneForAll5x5",		false,	false,	"One-for-all 5v5"),
	SHOWDOWN_1V1		(72,	"FIRSTBLOOD_1x1",		"FirstBlood1x1",	false,	false,	"Snowdown Showdown 1v1"),
	SHOWDOWN_2V2 		(73,	"FIRSTBLOOD_2x2",		"FirstBlood2x2",	false,	false,	"Snowdown Showdown 2v2"),
	HEXAKILL_6V6		(-1,	"SR_6x6",				"SummonersRift6x6",	false,	false,	"Hexakill 6v6");
	
	//---//
	
	//Data
	
	private int id;
	private String leagueValue, statsValue;
	private boolean ranked, team;
	private String name;
	
	//Constructors
	
	/**
	 * Create a new queue type constant with the given information. 
	 * @param id The queue ID.
	 * @param leagueValue The value used by game and league methods.
	 * @param statValue The value used by stat methods.
	 * @param ranked Whether or not the queue is ranked.
	 * @param team Whether or not the queue is team-based.
	 * @param name The queue name.
	 */
	private QueueType(int id, String leagueValue, String statValue, boolean ranked, boolean team, String name)
	{
		this.id = id;
		this.leagueValue = leagueValue;
		this.statsValue = statValue;
		this.name = name;
		this.ranked = ranked;
		this.team = team;
	}
	
	//Accessor methods
	
	/**
	 * Returns the queue ID to be used in API calls.
	 * @return The queue ID.
	 */
	/*public int getId()
	{
		return id;
	}*/
	
	/**
	 * Returns the queue value to be used in league API calls.
	 * @return The queue league value.
	 */
	public String getLeagueValue()
	{
		return leagueValue;
	}
	
	/**
	 * Returns the queue value to be used in stats API calls.
	 * @return The queue stats value.
	 */
	public String getStatsValue()
	{
		return statsValue;
	}
	
	/**
	 * Returns whether or not the queue type is a ranked queue.
	 * @return <code>true</code> if the queue is ranked, otherwise <code>false</code>.
	 */
	public boolean isRanked()
	{
		return ranked;
	}
	
	/**
	 * Returns whether or not the queue type is team-based, i.e. you queue as a team rather than a group of players.
	 * Examples include RANKED_TEAM_5x5 and RANKED_TEAM_3x3.
	 * @return Returns <code>true</code> if the queue is team-based, otherwise <code>false</code>.
	 */
	public boolean isTeamBased()
	{
		return team;
	}
	
	/**
	 * Returns the human-friendly queue name of the constant.
	 * @return The map name.
	 */
	public String getName()
	{
		return name;
	}
	
	//Utility methods
	
	/**
	 * Returns the constant from the given API queue ID.
	 * For example, the queue ID '2' will return the constant NORMAL_5V5_BLIND.
	 * @param id The API queue ID.
	 * @return The constant.
	 */
	/*public static QueueType getFromId(int id)
	{
		for(QueueType q : values())
			if(q.getId() == id)
				return q;
		return NONE;
	}*/
	
	/**
	 * Returns the constant from the given API queue value.
	 * For example, the queue value 'RANKED_5V5_SOLO' will return the constant RANKED_5V5_SOLO.
	 * @param value The API queue value.
	 * @return The constant.
	 */
	public static QueueType getFromGameValue(String value)
	{
		for(QueueType q : values())
			if(q.getLeagueValue().equals(value))
				return q;
		return NONE;
	}
	
	/**
	 * Returns the constant from the given API queue value.
	 * For example, the queue value 'RANKED_5V5_SOLO' will return the constant RANKED_5V5_SOLO.
	 * @param value The API queue value.
	 * @return The constant.
	 */
	public static QueueType getFromStatsValue(String value)
	{
		for(QueueType q : values())
			if(q.getStatsValue().equals(value))
				return q;
		return NONE;
	}
}
