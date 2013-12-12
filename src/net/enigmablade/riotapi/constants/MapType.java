package net.enigmablade.riotapi.constants;

/**
 * Map type constants as specified in the game constants page of the developer site.
 * @see <a href="https://developer.riotgames.com/docs/game-constants">Developer site</a>
 * 
 * @author Enigma
 */
public enum MapType
{
	UNKNOWN					(-1, "Unknown"),
	SUMMONERS_RIFT			(1,	 "Summoner's Rift"),
	SUMMONERS_RIFT_AUTUMN	(2,	 "Summoner's Rift Autumn"),
	TWISTED_TREELINE		(10, "Twisted Treeline"),
	TWISTED_TREELINE_OLD	(4,	 "Original Twisted Treeline"),
	CRYSTAL_SCAR			(8,	 "Crystal Scar"),
	HOWLING_ABYSS			(12, "Howling Abyss"),
	PROVING_GROUNDS			(3,	 "Proving Grounds");
	
	//---//
	
	private int id;
	private String name;
	
	private MapType(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Returns the map ID to be used in API calls.
	 * @return
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Returns the human-friendly map name of the constant.
	 * @return The map name.
	 */
	public String getName()
	{
		return name;
	}
	
	//Helpers
	
	/**
	 * Returns the constant from the given API map ID.
	 * For example, the map ID '1' will return the constant SUMMONERS_RIFT.
	 * @param id The API map ID.
	 * @return The constant.
	 */
	public static MapType getFromId(int id)
	{
		for(MapType m : values())
			if(m.getId() == id)
				return m;
		return UNKNOWN;
	}
}