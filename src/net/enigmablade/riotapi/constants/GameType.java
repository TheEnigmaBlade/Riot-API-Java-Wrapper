package net.enigmablade.riotapi.constants;

/**
 * Game type constants as specified in the game constants page of the developer site.
 * @see <a href="https://developer.riotgames.com/docs/game-constants">Developer site</a>
 * 
 * @author Enigma
 */
public enum GameType
{
	//Constant	Value
	UNKNOWN		("UNKNOWN"),
	CUSTOM		("CUSTOM_GAME"),
	MATCHED		("MATCHED_GAME"),
	CO_OP_VS_AI	("CO_OP_VS_AI_GAME"),
	TUTORIAL	("TUTORIAL_GAME");
	
	//---//
	
	//Data
	
	private String value;
	
	//Constructors
	
	/**
	 * Create a new game type with the given value.
	 * @param value The value.
	 */
	private GameType(String value)
	{
		this.value = value;
	}
	
	//Accessor methods
	
	/**
	 * Return the value of the constant for use in API calls.
	 * @return The game type's API value.
	 */
	public String getValue()
	{
		return value;
	}
	
	//Utility methods
	
	/**
	 * Returns the constant from the given API value.
	 * For example, the API value "MATCHED_GAME" will return the constant MATCHED.
	 * @param value The API value.
	 * @return The constant.
	 */
	public static GameType getFromValue(String value)
	{
		for(GameType g : values())
			if(g.getValue().equals(value))
				return g;
		return UNKNOWN;
	}
}
