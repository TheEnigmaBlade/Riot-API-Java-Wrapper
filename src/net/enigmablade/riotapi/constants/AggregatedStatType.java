package net.enigmablade.riotapi.constants;

import java.util.*;

import static net.enigmablade.riotapi.constants.GameMode.*;

/**
 * Constants used to retrieve information on aggregated stats.
 * 
 * @author Enigma
 */
public enum AggregatedStatType
{
	AVERAGE_ASSISTS					("averageAssists",				DOMINION),
	AVERAGE_CHAMPIONS_KILLED		("averageChampionsKilled",		DOMINION),
	AVERAGE_COMBAT_PLAYER_SCORE		("averageCombatPlayerScore",	DOMINION),
	AVERAGE_NODE_CAPTURE			("averageNodeCapture",			DOMINION),
	AVERAGE_NODE_CAPTURE_ASSIST		("averageNodeCaptureAssist",	DOMINION),
	AVERAGE_NODE_NEUTRALIZE			("averageNodeNeutralize",		DOMINION),
	AVERAGE_NODE_NEUTRALIZE_ASSIST	("averageNodeNeutralizeAssist",	DOMINION),
	AVERAGE_NUM_DEATHS				("averageNumDeaths",			DOMINION),
	AVERAGE_OBJECTIVE_PLAYER_SCORE	("averageObjectivePlayerScore",	DOMINION),
	AVERAGE_TEAM_OBJECTIVE			("averageTeamObjective",		DOMINION),
	AVERAGE_TOTAL_PLAYER_SCORE		("averageTotalPlayerScore",		DOMINION),
	BOT_GAMES_PLAYED				("botGamesPlayed",				CLASSIC, DOMINION, ARAM, TUTORIAL),
	KILLING_SPREE					("killingSpree",				CLASSIC, DOMINION, ARAM, TUTORIAL),
	MAX_ASSISTS						("maxAssists",					DOMINION),
	MAX_CHAMPIONS_KILLED			("maxChampionsKilled",			CLASSIC, DOMINION, ARAM, TUTORIAL),
	MAX_COMBAT_PLAYER_SCORE			("maxCombatPlayerScore",		DOMINION),
	LARGEST_CRITICAL_STRIKE			("maxLargestCriticalStrike",	CLASSIC, DOMINION, ARAM, TUTORIAL),
	LARGEST_KILLING_SPREE			("maxLargestKillingSpree",		CLASSIC, DOMINION, ARAM, TUTORIAL),
	MOST_NODES_CAPTURED				("maxNodeCapture",				DOMINION),
	MOST_NODE_CAPTURE_ASSISTS		("maxNodeCaptureAssist",		DOMINION),
	MOST_NODES_NEUTRALIZED			("maxNodeNeutralize",			DOMINION),
	MOST_NODE_NEUTRALIZE_ASSISTS	("maxNodeNeutralizeAssist",		DOMINION),
	MOST_OBJECTIVE_PLAYER_SCORE		("maxObjectivePlayerScore",		DOMINION),
	MOST_TEAM_OBJECTIVES			("maxTeamObjective",			DOMINION),
	MOST_TIME_PLAYED				("maxTimePlayed",				CLASSIC, DOMINION, ARAM, TUTORIAL),
	MOST_TIME_SPENT_LIVING			("maxTimeSpentLiving",			CLASSIC, DOMINION, ARAM, TUTORIAL),
	MOST_TOTAL_PLAYER_SCORE			("maxTotalPlayerScore",			DOMINION),
	MOST_CHAMPION_KILLS				("mostChampionKillsPerSession",	CLASSIC, DOMINION, ARAM, TUTORIAL),
	MOST_SPELLS_CAST				("mostSpellsCast",				CLASSIC, DOMINION, ARAM, TUTORIAL),
	NORMAL_GAMES_PLAYED				("normalGamesPlayed",			CLASSIC, DOMINION, ARAM, TUTORIAL),
	RANKED_PREMADE_GAMES_PLAYED		("rankedPremadeGamesPlayed",	CLASSIC, DOMINION, ARAM, TUTORIAL),
	RANKED_SOLO_GAMES_PLAYED		("rankedSoloGamesPlayed",		CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_ASSISTS					("totalAssists",				CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_CHAMPION_KILLS			("totalChampionKills",			CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_DAMAGE_DEALT				("totalDamageDealt",			CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_DAMAGE_TAKEN				("totalDamageTaken",			CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_DOUBLE_KILLS				("totalDoubleKills",			CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_FIRST_BLOODS				("totalFirstBlood",				CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_GOLD_EARNED				("totalGoldEarned",				CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_HEALING_DONE				("totalHeal",					CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_MAGIC_DAMAGE_DEALT		("totalMagicDamageDealt",		CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_MINION_KILLS				("totalMinionKills",			CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_NEUTRAL_MINIONS_KILLED	("totalNeutralMinionsKilled",	CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_NODES_CAPTURED			("totalNodeCapture",			DOMINION),
	TOTAL_NODES_NEUTRALIZED			("totalNodeNeutralize",			DOMINION),
	TOTAL_PENTA_KILLS				("totalPentaKills",				CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_PHYSICAL_DAMAGE_DEALT		("totalPhysicalDamageDealt",	CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_QUADRA_KILLS				("totalQuadraKills",			CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_GAMES_LOST				("totalSessionsLost",			CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_GAMES_PLAYED				("totalSessionsPlayed",			CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_GAMES_WON					("totalSessionsWon",			CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_TRIPLE_KILLS				("totalTripleKills",			CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_TURRETS_KILLED			("totalTurretsKilled",			CLASSIC, DOMINION, ARAM, TUTORIAL),
	TOTAL_UNREAL_KILLS				("totalUnrealKills",			CLASSIC, DOMINION, ARAM, TUTORIAL);
	
	//---//
	
	//Data
	
	private String value;
	private List<GameMode> supportedGameModes;
	
	//Constructors
	
	/**
	 * Create a new aggregated stat type.
	 * @param value The stat type's value for use with the API.
	 * @param supportedGameModes A list of supported game modes.
	 */
	private AggregatedStatType(String value, GameMode... supportedGameModes)
	{
		this.value = value;
		this.supportedGameModes = Arrays.asList(supportedGameModes);
	}
	
	//Accessor methods
	
	/**
	 * Returns the string value of the stat type for use with the API.
	 * @return The type value.
	 */
	public String getValue()
	{
		return value;
	}
	
	/**
	 * Returns whether or not the aggregated stat type is supported by the given game mode.
	 * @param mode The game mode.
	 * @return <code>true</code> if the game mode is supported, otherwise <code>false</code>.
	 */
	public boolean isGameModeSupported(GameMode mode)
	{
		return supportedGameModes.contains(mode);
	}
	
	//Utility methods
	
	/**
	 * Returns a list of aggregated stat types that are supported by the given game mode.
	 * @param mode The game mode.
	 * @return A list of stat types.
	 */
	public static List<AggregatedStatType> getSupportedTypes(GameMode mode)
	{
		List<AggregatedStatType> types = new ArrayList<>();
		for(AggregatedStatType type : values())
			if(type.isGameModeSupported(mode))
				types.add(type);
		return types;
	}
}
