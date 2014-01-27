package net.enigmablade.riotapi.constants;

/**
 * Game mode constants as specified in the game constants page of the developer site.
 * @see <a href="https://developer.riotgames.com/docs/game-constants">Developer site</a>
 * 
 * @author Enigma
 */
public enum GameMode
{
	//Constant	Value			Name
	UNKNOWN		("UNKNOWN",		"Unknown"),
	CLASSIC		("CLASSIC",		"Classic"),
	DOMINION	("ODIN",		"Dominion"),
	ARAM		("ARAM",		"ARAM"),
	TUTORIAL	("TUTORIAL",	"Tutorial"),
	ALL_FOR_ONE	("ALLFORONE",	"All-for-one"),
	SHOWDOWN	("FIRSTBLOOD",	"Showdown");
	
	//---//
	
	//Data
	
	private String value;
	private String name;
	
	//Constructors
	
	private GameMode(String value, String name)
	{
		this.value = value;
		this.name = name;
	}
	
	//Accessor methods
	
	/**
	 * Returns the value of the constant for use in API calls.
	 * @return The game mode's API value.
	 */
	public String getValue()
	{
		return value;
	}
	
	/**
	 * Returns the nice and human-friendly name of the game mode.
	 * @return The name of the game mode.
	 */
	public String getName()
	{
		return name;
	}
	
	//Utility methods
	
	/**
	 * Returns the constant from the given API value.
	 * For example, the API value "ODIN" will return the constant DOMINION.
	 * @param value The API value.
	 * @return The constant.
	 */
	public static GameMode getFromValue(String value)
	{
		for(GameMode g : values())
			if(g.getValue().equals(value))
				return g;
		return UNKNOWN;
	}
}
