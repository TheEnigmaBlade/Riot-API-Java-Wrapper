package net.enigmablade.riotapi.constants;

/**
 * Queue type constants as specified in the game constants page of the developer site.
 * @see <a href="https://developer.riotgames.com/docs/game-constants">Developer site</a>
 * 
 * @author Enigma
 */
public enum QueueType
{
	UNKNOWN				(-1, "Unknown"),
	NORMAL_5V5_BLIND	(2,	 "Normal 5v5 Blind Pick"),
	NORMAL_5V5_DRAFT	(14, "Normal 5v5 Draft Pick"),
	NORMAL_5V5_COOP		(7,	 "Normal Coop vs AI"),
	RANKED_5V5_SOLO		(4,	 "Ranked 5v5 Solo"),
	NORMAL_3V3_BLIND	(8,	 "Normal 3v3 Blind"),
	NORMAL_3V3_COOP		(52, "Normal 3v3 Coop vs AI"),
	DOMINION_5V5_BLIND	(16, "Dominion 5v5 Blind Pick"),
	DOMINION_5V5_DRAFT	(17, "Dominion 5v5 Draft Pick"),
	DOMINION_5V5_COOP	(25, "Dominion Coop vs AI"),
	RANKED_TEAM_3V3		(41, "Ranked Team 3v3"),
	RANKED_TEAM_5V5		(42, "Ranked Team 5v5"),
	ARAM_5V5			(65, "ARAM"),
	ARAM_5V5_COOP		(67, "ARAM Coop vs AI");
	
	//---//
	
	private int id;
	private String name;
	
	private QueueType(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Returns the queue ID to be used in API calls.
	 * @return
	 */
	public int getId()
	{
		return id;
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
		switch(id)
		{
			case 2: return NORMAL_5V5_BLIND;
			case 14: return NORMAL_5V5_DRAFT;
			case 7: return NORMAL_5V5_COOP;
			case 4: return RANKED_5V5_SOLO;
			case 8: return NORMAL_3V3_BLIND;
			case 52: return NORMAL_3V3_COOP;
			case 16: return DOMINION_5V5_BLIND;
			case 17: return DOMINION_5V5_DRAFT;
			case 25: return DOMINION_5V5_COOP;
			case 41: return RANKED_TEAM_3V3;
			case 42: return RANKED_TEAM_5V5;
			case 65: return ARAM_5V5;
			case 67: return ARAM_5V5_COOP;
			default: return UNKNOWN;
		}
	}
}
