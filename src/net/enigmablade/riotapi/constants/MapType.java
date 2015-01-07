package net.enigmablade.riotapi.constants;

/**
 * Map type constants as specified in the game constants page of the developer site.
 * @see <a href="https://developer.riotgames.com/docs/game-constants">Developer site</a>
 * 
 * @author Enigma
 */
public enum MapType
{
	//Constant					ID		Name
	UNKNOWN						(-1,	"Unknown"),
	SUMMONERS_RIFT_OLD			(1,		"Old Summoner's Rift"),
	SUMMONERS_RIFT_AUTUMN_OLD	(2,		"Old Summoner's Rift Autumn"),
	SUMMONERS_RIFT				(11,	"Summoner's Rift"),
	TWISTED_TREELINE_OLD		(4,		"Old Twisted Treeline"),
	TWISTED_TREELINE			(10,	"Twisted Treeline"),
	CRYSTAL_SCAR				(8,		"Crystal Scar"),
	HOWLING_ABYSS				(12,	"Howling Abyss"),
	PROVING_GROUNDS				(3,		"Proving Grounds");
	
	//---//
	
	//Data
	
	private int id;
	private String name;
	
	//Constructors
	
	/**
	 * Creates a new map type constant with the given map ID and map name.
	 * @param id The map ID.
	 * @param name The map name.
	 */
	private MapType(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	//Accessor methods
	
	/**
	 * Returns the map ID to be used in API calls.
	 * @return The map's API ID.
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
	
	//Utility methods
	
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
