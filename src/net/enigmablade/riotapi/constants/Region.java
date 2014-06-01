package net.enigmablade.riotapi.constants;

/**
 * Regions available to be use in API calls.
 * 
 * @author Enigma
 */
public enum Region
{
	NA	("na",		"na.api.pvp.net"),
	EUW	("euw",		"euw.api.pvp.net"),
	EUNE("eune",	"eune.api.pvp.net"),
	LAN	("lan",		"lan.api.pvp.net"),
	LAS	("las",		"las.api.pvp.net"),
	OCE	("oce",		"oce.api.pvp.net"),
	BR	("br",		"br.api.pvp.net"),
	TR	("tr",		"tr.api.pvp.net"),
	RU	("ru",		"ru.api.pvp.net"),
	KR	("kr",		"kr.api.pvp.net");
	
	//---//
	
	//Data
	
	private String value;
	private String endpoint;
	
	//Constructors
	
	/**
	 * Creates a new region with the given API value.
	 * @param value The API value.
	 */
	private Region(String value, String endpoint)
	{
		this.value = value;
		this.endpoint = endpoint;
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
	
	/**
	 * Returns the endpoint for the region.
	 * @return The endpoint.
	 */
	public String getEndpoint()
	{
		return endpoint;
	}
}
