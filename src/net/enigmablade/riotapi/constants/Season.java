package net.enigmablade.riotapi.constants;

/**
 * Constants representing seasons available for access from the API.
 * 
 * @author Enigma
 */
public enum Season
{
	SEASON_3("SEASON3"),
	SEASON_4("SEASON4");
	
	//---//
	
	//Data
	
	private String value;
	
	//Constructors
	
	/**
	 * Creates a new season with the given API value.
	 * @param apiValue The API value.
	 */
	private Season(String apiValue)
	{
		this.value = apiValue;
	}
	
	//Accessor methods
	
	/**
	 * Returns the value for use with the API.
	 * @return The API value.
	 */
	public String getValue()
	{
		return value;
	}
}
