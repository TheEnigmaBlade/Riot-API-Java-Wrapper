package net.enigmablade.riotapi.constants;

/**
 * Regions available to be use in API calls.
 * 
 * @author Enigma
 */
public enum Region
{
	NA	("na",		"na1",	"na.api.pvp.net"),
	EUW	("euw",		"eu",	"euw.api.pvp.net"),
	EUNE("eune",	"eun1",	"eune.api.pvp.net"),
	LAN	("lan",		"la1",	"lan.api.pvp.net"),
	LAS	("las",		"la2",	"las.api.pvp.net"),
	OCE	("oce",		"oc1",	"oce.api.pvp.net"),
	BR	("br",		"",	"br.api.pvp.net"),
	TR	("tr",		"",	"tr.api.pvp.net"),
	RU	("ru",		"",	"ru.api.pvp.net"),
	KR	("kr",		"",	"kr.api.pvp.net"),
	PBE	("pbe",		"",	"global.api.pvp.net"),
	GLOBAL("global", "",	"global.api.pvp.net");
	
	//---//
	
	//Data
	
	private String value, tag;
	private String endpoint;
	
	//Constructors
	
	/**
	 * Creates a new region with the given API value.
	 * @param value The API value.
	 */
	private Region(String value, String tag, String endpoint)
	{
		this.value = value;
		this.tag = tag;
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
	
	public String getTag()
	{
		return tag;
	}
	
	/**
	 * Returns the endpoint for the region.
	 * @return The endpoint.
	 */
	public String getEndpoint()
	{
		return endpoint;
	}
	
	//Static conversions
	
	public static Region getFromTag(String tag, String slug)
	{
		for(Region v : values())
			if(v.getTag().equals(tag) || v.getValue().equals(slug))
				return v;
		return GLOBAL;
	}
	
	public static Region getFromSlug(String slug)
	{
		for(Region v : values())
			if(v.getValue().equals(slug))
				return v;
		return GLOBAL;
	}
}
