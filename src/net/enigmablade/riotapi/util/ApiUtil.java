package net.enigmablade.riotapi.util;

import java.util.*;
import net.enigmablade.jsonic.*;

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
	
	//Conversion utilities
	
	public static List<Object> convertArray(JsonArray array)
	{
		if(array == null)
			return new ArrayList<>(0);
		
		List<Object> objs = new ArrayList<>();
		for(JsonIterator it = array.iterator(); it.hasNext();)
			objs.add(it.next());
		return objs;
	}
	
	public static List<String> convertStringArray(JsonArray array)
	{
		if(array == null)
			return new ArrayList<>(0);
		
		List<String> strings = new ArrayList<>();
		for(JsonIterator it = array.iterator(); it.hasNext();)
			strings.add(it.nextString());
		return strings;
	}
	
	public static List<Integer> convertIntArray(JsonArray array)
	{
		if(array == null)
			return new ArrayList<>(0);
		
		List<Integer> ints = new ArrayList<>();
		for(JsonIterator it = array.iterator(); it.hasNext();)
			ints.add(it.nextInt());
		return ints;
	}
	
	public static List<Float> convertFloatArray(JsonArray array)
	{
		if(array == null)
			return new ArrayList<>(0);
		
		List<Float> ints = new ArrayList<>();
		for(JsonIterator it = array.iterator(); it.hasNext();)
			ints.add(it.nextFloat());
		return ints;
	}
}
