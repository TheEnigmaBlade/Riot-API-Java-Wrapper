package net.enigmablade.riotapi.util;

/**
 * General utilities for usage of the API.
 * 
 * @author Enigma
 */
public class ApiUtil
{
	/**
	 * Converts the given summoner name to the standardized summoner name form, which is a lower-case summoner name without whitespace.
	 * @param summonerName The summoner name to standardize.
	 * @return The standardized summoner name.
	 */
	public static String standardizeSummonerName(String summonerName)
	{
		return summonerName.replace(" ", "").toLowerCase();
	}
}
