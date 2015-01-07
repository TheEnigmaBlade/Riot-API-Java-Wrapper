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
	//Constant			Game/League value		Stat value			Match value					Ranked	Team	Name
	NONE				("NONE",				"None",				"NONE",						false, 	false,	"None"),
	NORMAL_5V5_BLIND	("NORMAL",				"Unranked",			"NORMAL_5x5_BLIND",			false,	false,	"Normal 5v5 Blind"),
	NORMAL_5V5_DRAFT	("NORMAL",				"Unranked",			"NORMAL_5x5_DRAFT",			false,	false,	"Normal 5v5 Draft"),
	NORMAL_5V5_BOTS		("BOT",					"CoopVsAI",			"BOT_5x5",					false,	false,	"Normal Coop vs AI"),
	RANKED_5V5_SOLO		("RANKED_SOLO_5x5",		"RankedSolo5x5",	"RANKED_SOLO_5x5",			true,	false,	"Ranked 5v5 Solo"),
	RANKED_5V5_PREMADE	("RANKED_PREMADE_5x5",	"RankedPremade5x5",	"RANKED_PREMADE_5x5",		true,	false,	"Ranked 5v5 Premade"),
	RANKED_5V5_TEAM		("RANKED_TEAM_5x5",		"RankedTeam5x5",	"RANKED_TEAM_5x5",			true,	true,	"Ranked 5v5 Team"),
	NORMAL_3V3_BLIND	("NORMAL_3x3",			"Unranked3x3",		"NORMAL_3x3",				false,	false,	"Normal 3v3 Blind"),
	NORMAL_3V3_BOTS		("BOT_3x3", 			"CoopVsAI3x3",		"BOT_TT_3x3",				false,	false,	"Normal 3v3 Coop vs AI"),
	RANKED_3V3_PREMADE	("RANKED_PREMADE_3X3",	"RankedPremade3x3",	"RANKED_TEAM_3x3",			true,	false,	"Ranked Premade 3v3"),
	RANKED_3V3_TEAM		("RANKED_TEAM_3x3",		"RankedTeam3x3",	"RANKED_PREMADE_3x3",		true,	true,	"Ranked Team 3v3"),
	DOMINION_5V5_BLIND	("ODIN_UNRANKED", 		"OdinUnranked",		"ODIN_5x5_BLIND",			false,	false,	"Dominion 5v5 Blind"),
	DOMINION_5V5_DRAFT	("ODIN_UNRANKED", 		"OdinUnranked",		"ODIN_5x5_DRAFT",			false,	false,	"Dominion 5v5 Draft"),
	DOMINION_5V5_BOTS	("ODIN_UNRANKED",		"CoopVsAI",			"BOT_ODIN_5x5",				false,	false,	"Dominion Coop vs AI"),
	ARAM_5V5			("ARAM_UNRANKED_5x5",	"AramUnranked5x5",	"ARAM_5x5",					false,	false,	"ARAM"),
	ARAM_5V5_BOTS		("ARAM_UNRANKED_5x5",	"AramUnranked5x5",	"ARAM_5x5",					false,	false,	"ARAM Coop vs AI"),
	ONE_FOR_ALL_5V5		("ONEFORALL_5x5",		"OneForAll5x5",		"ONEFORALL_5x5",			false,	false,	"One-for-all 5v5"),
	SHOWDOWN_1V1		("FIRSTBLOOD_1x1",		"FirstBlood1x1",	"FIRSTBLOOD_1x1",			false,	false,	"Snowdown Showdown 1v1"),
	SHOWDOWN_2V2 		("FIRSTBLOOD_2x2",		"FirstBlood2x2",	"FIRSTBLOOD_2x2",			false,	false,	"Snowdown Showdown 2v2"),
	HEXAKILL_SR			("SR_6x6",				"SummonersRift6x6",	"SR_6x6",					false,	false,	"Summoner's Rift Hexakill"),
	HEXAKILL_TT			("HEXAKILL",			"Hexakill",			"HEXAKILL",					false,	false,	"Twisted Treeling Hexakill"),
	TEAMBUILDER_5V5		("CAP_5x5",				"CAP5x5",			"GROUP_FINDER_5x5",			false,	false,	"Team Builder 5v5"),
	URF_5V5				("URF",					"URF",				"URF_5x5",					false,	false,	"URF 5v5"),
	URF_5V5_BOTS		("URF_BOT",				"URFBots",			"BOT_URF_5x5",				false,	false,	"URF Coop vs AI"),
	NIGHTMARE_BOTS_R1	("NIGHTMARE_BOT",		"NightmareBot",		"NIGHTMARE_BOT_5x5_RANK1",	false,	false,	"Nightmare Bots, rank 1"),
	NIGHTMARE_BOTS_R2	("NIGHTMARE_BOT",		"NightmareBot",		"NIGHTMARE_BOT_5x5_RANK2",	false,	false,	"Nightmare Bots, rank 2"),
	NIGHTMARE_BOTS_R5	("NIGHTMARE_BOT",		"NightmareBot",		"NIGHTMARE_BOT_5x5_RANK5",	false,	false,	"Nightmare Bots, rank 5"),
	ASCENSION_5x5		("ASCENSION", 			"Ascension",		"ASCENSION",				false, 	false,	"Ascension"),
	KING_PORO_5x5		("KING_PORO",			"",					"KING_PORO_5x5",			false,	false,	"King Poro");
	
	//---//
	
	//Data
	
	private String leagueValue, statsValue, matchValue;
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
	private QueueType(String leagueValue, String statValue, String matchValue, boolean ranked, boolean team, String name)
	{
		this.leagueValue = leagueValue;
		this.statsValue = statValue;
		this.matchValue = matchValue;
		this.name = name;
		this.ranked = ranked;
		this.team = team;
	}
	
	//Accessor methods
	
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
	 * Returns the queue value to be used in match API calls.
	 * @return The queue match value.
	 */
	public String getMatchValue()
	{
		return matchValue;
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
	
	/**
	 * Returns the constant from the given API match value.
	 * For example, the queue value 'RANKED_SOLO_5v5' will return the constant RANKED_SOLO_5v5.
	 * @param value The API match value.
	 * @return The constant.
	 */
	public static QueueType getFromMatchValue(String value)
	{
		for(QueueType q : values())
			if(q.getMatchValue().equals(value))
				return q;
		return NONE;
	}
}
