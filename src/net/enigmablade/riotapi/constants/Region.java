package net.enigmablade.riotapi.constants;

/**
 * Regions available to be use in API calls.
 * 
 * @author Enigma
 */
public enum Region
{
	NA	("na"),
	EUW	("euw"),
	EUNE("eune"),
	BR	("br"),
	TR	("tr"),
	OCE	("oce");
	
	//---//
	
	//Data
	
	private String value;
	
	//Constructors
	
	/**
	 * Creates a new region with the given API value.
	 * @param value The API value.
	 */
	private Region(String value)
	{
		this.value = value;
	}
	
	//Accessor methods
	
	/**
	 * Returns the value of the region for use with the API.
	 * @return The API value.
	 */
	public String getValue()
	{
		return value;
	}
}
