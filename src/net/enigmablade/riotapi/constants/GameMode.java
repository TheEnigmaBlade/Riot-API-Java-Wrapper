package net.enigmablade.riotapi.constants;

/**
 * Game mode constants as specified in the game constants page of the developer site.
 * @see <a href="https://developer.riotgames.com/docs/game-constants">Developer site</a>
 * 
 * @author Enigma
 */
public enum GameMode
{
	UNKNOWN		("UNKNOWN"),
	CLASSIC		("CLASSIC"),
	DOMINION	("ODIN"),
	ARAM		("ARAM"),
	TUTORIAL	("TUTORIAL");
	
	//---//
	
	private String value;
	
	private GameMode(String value)
	{
		this.value = value;
	}
	
	/**
	 * Return the value of the constant for use in API calls.
	 * @return
	 */
	public String getValue()
	{
		return value;
	}
	
	//Helpers
	
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
