package net.enigmablade.riotapi.methods;

import java.util.*;
import net.enigmablade.jsonic.*;
import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.Requester.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.constants.Locale;
import net.enigmablade.riotapi.exceptions.*;
import net.enigmablade.riotapi.types.*;
import static net.enigmablade.riotapi.constants.Region.*;

/**
 * <p>The static data method and its supporting operations.<p>
 * <p>Method support information:
 * 	<ul>
 * 		<li><i>Version</i>: 1.0</li>
 * 		<li><i>Regions</i>: NA, EUW, EUNE, LAN, LAS, OCE, BR, TR, RU, KR</li>
 * 	</ul>
 * </p>
 * <p>Operation information:
 * 	<ol>
 * 		<li></li>
 * 	</ol>
 * </p>
 * @see <a href="https://developer.riotgames.com/api/methods#!/378">Developer site</a>
 * 
 * @author Enigma
 */
public class StaticDataMethod extends Method
{
	/**
	 * Create a new champion method instance.
	 * @param api The API instance being used.
	 */
	public StaticDataMethod(RiotApi api)
	{
		super(api, "api/lol/static-data", null, "1", new Region[]{NA, EUW, EUNE, LAN, LAS, OCE, BR, TR, RU, KR});
	}
	
	//API-defined operation methods
	
	/**
	 * Returns basic static data for all champions.
	 * @param region The region (<i>required</i>).
	 * @param locale The language locale (<i>required</i>).
	 * @return A map of champion IDs to champions.
	 * @throws RegionNotSupportedException If the region is not supported.
	 * @throws IllegalArgumentException If any required arguments are invalid.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Map<String, Champion> getChampions(Region region, Locale locale) throws RiotApiException
	{
		return getChampions(region, locale, null);
	}
	
	/**
	 * Returns the specified static data for all champions.
	 * @param region The region (<i>required</i>).
	 * @param locale The language locale (<i>required</i>).
	 * @param championData The type of data to return. Defaults to basic data if <code>null</code>.
	 * @return A map of champion IDs to champions.
	 * @throws RegionNotSupportedException If the region is not supported.
	 * @throws IllegalArgumentException If any required arguments are invalid.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Map<String, Champion> getChampions(Region region, Locale locale, ChampionDataType championData) throws RiotApiException
	{
		//Check arguments
		if(locale == null)
			throw new IllegalArgumentException("A local must be given.");
		
		//Create argument maps
		Map<String, String> queryArgs = createArgMap("locale", locale.getValue());
		if(championData != null)
			queryArgs.put("champData", championData.toString().toLowerCase());
		
		//Send request
		Response response = staticGetMethodResult(region,
				"champion",
				null, queryArgs);
		
		//Parse response
		return convertChampionList((JsonObject)response.getValue(), championData, region, locale);
	}
	
	/**
	 * Returns basic static data for the champion with the given ID.
	 * @param region The region (<i>required</i>).
	 * @param locale The language locale (<i>required</i>).
	 * @param championId The champion ID.
	 * @return The champion.
	 * @throws RegionNotSupportedException If the region is not supported.
	 * @throws IllegalArgumentException If any required arguments are invalid.
	 * @throws StaticDataNotFoundException If no champion was found with the given champion ID.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Champion getChampion(Region region, Locale locale, String championId) throws RiotApiException
	{
		return getChampion(region, locale, championId, null);
	}
	
	/**
	 * Returns the specified static data for the champion with the given ID.
	 * @param region The region (<i>required</i>).
	 * @param locale The language locale (<i>required</i>).
	 * @param championId The champion ID.
	 * @param championData The type of data to return. Defaults to basic data if <code>null</code>.
	 * @return The champion.
	 * @throws RegionNotSupportedException If the region is not supported.
	 * @throws IllegalArgumentException If any required arguments are invalid.
	 * @throws StaticDataNotFoundException If no champion was found with the given champion ID.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Champion getChampion(Region region, Locale locale, String championId, ChampionDataType championData) throws RiotApiException
	{
		//Check arguments
		if(locale == null)
			throw new IllegalArgumentException("A local must be given.");
		if(championId == null)
			throw new IllegalArgumentException("A champion ID must be given.");
		
		//Create argument maps
		Map<String, String> pathArgs = createArgMap(
				"id", championId);
		
		Map<String, String> queryArgs = createArgMap(
				"locale", locale.getValue());
		if(championData != null)
			queryArgs.put("champData", championData.toString().toLowerCase());
		
		//Send request
		Response response = staticGetMethodResult(region,
				"champion/{id}",
				pathArgs, queryArgs);
		
		//Check errors
		if(response.getCode() == 404)
			throw new StaticDataNotFoundException(region, championId);
		
		//Parse response
		return convertChampion((JsonObject)response.getValue(), championData, region, locale);
	}
	
	//Private converter methods
	
	private Map<String, Champion> convertChampionList(JsonObject rootObject, ChampionDataType loadedData, Region region, Locale locale)
	{
		System.out.println(rootObject.toString());
		System.out.println("Format:  "+rootObject.getString("format"));
		System.out.println("Type:    "+rootObject.getString("type"));
		System.out.println("Version: "+rootObject.getString("version"));
		System.out.println("Keys:    "+rootObject.getObject("keys"));
		System.out.println();
		
		//Convert the list of champions
		JsonObject championsObject = rootObject.getObject("data");
		Map<String, Champion> champions = new HashMap<>(championsObject.size());
		for(String championKey : championsObject.keySet())
		{
			//Convert a champion
			JsonObject championObject = championsObject.getObject(championKey);
			Champion champion = convertChampion(championObject, loadedData, region, locale);
			champions.put(championKey, champion);
		}
		return champions;
	}
	
	private Champion convertChampion(JsonObject championObject, ChampionDataType loadedData, Region region, Locale locale)
	{
		String name = championObject.getString("name");
		long id = Long.parseLong(championObject.getString("key"));
		String key = championObject.getString("id");
		String title = championObject.getString("title");
		
		Champion c = new Champion(api, region, locale, name, id, key, title);
		
		if(loadedData != null)
		{
			boolean notAll = loadedData != ChampionDataType.ALL;
			
			switch(loadedData)
			{
				//Basic data
				case PARTYPE:
					String partype = championObject.getString("partype");
					ChampionResourceType resourceType = ChampionResourceType.getResourceType(partype);
					c.setResourceType(resourceType);
					if(notAll) break;
				
				case BLURB:
					String blurb = championObject.getString("blurb");
					c.setBlurb(blurb);
					if(notAll) break;
				
				case LORE:
					String lore = championObject.getString("lore");
					c.setLore(lore);
					if(notAll) break;
				
				//Lists
				case ALLYTIPS:
					JsonArray allyTipsArray = championObject.getArray("allytips");
					List<String> allyTips = convertTips(allyTipsArray);
					c.setAllyTips(allyTips);
					if(notAll) break;
				
				case ENEMYTIPS:
					JsonArray enemyTipsArray = championObject.getArray("enemytips");
					List<String> enemyTips = convertTips(enemyTipsArray);
					c.setEnemyTips(enemyTips);
					if(notAll) break;
				
				case RECOMMENDED:
					JsonArray recommendedArray = championObject.getArray("recommended");
					
					if(notAll) break;
				
				case SKINS:
					JsonArray skinsArray = championObject.getArray("skins");
					
					if(notAll) break;
				
				case SPELLS:
					JsonArray spellsArray = championObject.getArray("spells");
					
					if(notAll) break;
				
				case TAGS:
					JsonArray tagsArray = championObject.getArray("tags");
					
					if(notAll) break;
				
				//Objects
				case IMAGE:
					JsonObject imageObject = championObject.getObject("image");
					
					if(notAll) break;
				
				case INFO:
					JsonObject infoObject = championObject.getObject("info");
					
					if(notAll) break;
				
				case PASSIVE:
					JsonObject passiveObject = championObject.getObject("passive");
					
					if(notAll) break;
				
				case STATS:
					JsonObject statsObject = championObject.getObject("stats");
					
					if(notAll) break;
			}
		}
		
		return c;
	}
	
	private List<String> convertTips(JsonArray tipsArray)
	{
		List<String> tips = new ArrayList<>(tipsArray.size());
		for(JsonIterator it = tipsArray.iterator(); it.hasNext();)
			tips.add(it.nextString());
		return tips;
	}
	
	//Other methods
	
	public void fillChampion(Champion champion, Region region) throws RiotApiException
	{
		fillChampion(champion, region, null);
	}
	
	public void fillChampion(Champion champion, Region region, ChampionDataType dataType) throws RiotApiException
	{
		//TODO
	}
	
	//Helper methods
	
	private Response staticGetMethodResult(Region region, String operation, Map<String, String> pathArgs, Map<String, String> queryArgs) throws RiotApiException
	{
		boolean save = api.isRateLimitEnabled();
		api.setRateLimitEnabled(false);
		Response r = getMethodResult(region, operation, pathArgs, queryArgs);
		api.setRateLimitEnabled(save);
		return r;
	}
}
