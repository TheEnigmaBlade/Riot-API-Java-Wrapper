package net.enigmablade.riotapi.constants;

/**
 * Queue type constants as specified in the game constants page of the developer site.
 * @see <a href="https://developer.riotgames.com/docs/game-constants">Developer site</a>
 * 
 * @author Enigma
 */
public enum QueueType
{
	UNKNOWN				(-1, "UNKNOWN",			"Unknown"),
	NORMAL_5V5_BLIND	(2,	 "",				"Normal 5v5 Blind Pick"),
	NORMAL_5V5_DRAFT	(14, "",				"Normal 5v5 Draft Pick"),
	NORMAL_5V5_COOP		(7,	 "",				"Normal Coop vs AI"),
	RANKED_5V5_SOLO		(4,	 "RANKED_SOLO_5x5",	"Ranked 5v5 Solo"),
	RANKED_5V5_TEAM		(42, "RANKED_TEAM_5x5",	"Ranked Team 5v5"),
	NORMAL_3V3_BLIND	(8,	 "", 				"Normal 3v3 Blind"),
	NORMAL_3V3_COOP		(52, "", 				"Normal 3v3 Coop vs AI"),
	RANKED_3V3_TEAM		(41, "RANKED_TEAM_3x3",	"Ranked Team 3v3"),
	DOMINION_5V5_BLIND	(16, "", 				"Dominion 5v5 Blind Pick"),
	DOMINION_5V5_DRAFT	(17, "", 				"Dominion 5v5 Draft Pick"),
	DOMINION_5V5_COOP	(25, "", 				"Dominion Coop vs AI"),
	ARAM_5V5			(65, "", 				"ARAM"),
	ARAM_5V5_COOP		(67, "", 				"ARAM Coop vs AI");
	
	//---//
	
	private int id;
	private String value;
	private String name;
	
	private QueueType(int id, String value, String name)
	{
		this.id = id;
		this.value = value;
		this.name = name;
	}
	
	/**
	 * Returns the queue ID to be used in API calls.
	 * @return The queue ID.
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Returns the queue value to be used in API calls.
	 * @return The queue value.
	 */
	public String getValue()
	{
		return value;
	}
	
	/**
	 * Returns the human-friendly queue name of the constant.
	 * @return The map name.
	 */
	public String getName()
	{
		return name;
	}
	
	//Helpers
	
	/**
	 * Returns the constant from the given API queue ID.
	 * For example, the queue ID '2' will return the constant NORMAL_5V5_BLIND.
	 * @param id The API queue ID.
	 * @return The constant.
	 */
	public static QueueType getFromId(int id)
	{
		for(QueueType q : values())
			if(q.getId() == id)
				return q;
		return UNKNOWN;
	}
	
	/**
	 * Returns the constant from the given API queue value.
	 * For example, the queue value 'RANKED_5V5_SOLO' will return the constant RANKED_5V5_SOLO.
	 * @param value The API queue value.
	 * @return The constant.
	 */
	public static QueueType getFromValue(String value)
	{
		for(QueueType q : values())
			if(q.getValue().equals(value))
				return q;
		return UNKNOWN;
	}
}
