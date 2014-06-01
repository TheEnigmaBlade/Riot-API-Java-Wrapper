package net.enigmablade.riotapi.methods;

import java.util.*;
import net.enigmablade.jsonic.*;
import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.Requester.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.exceptions.*;
import net.enigmablade.riotapi.types.*;
import net.enigmablade.riotapi.util.*;
import static net.enigmablade.riotapi.constants.Region.*;

/**
 * <p>The summoner method and its supporting operations.<p>
 * <p>Method support information:
 * 	<ul>
 * 		<li><i>Version</i>: 1.4</li>
 * 		<li><i>Regions</i>: NA, EUW, EUNE</li>
 * 	</ul>
 * </p>
 * <p>Operation information:
 * 	<ol>
 * 		<li>Get summoners by name</li>
 * 		<li>Get summoners by ID</li>
 * 		<li>Get summoner names by ID</li>
 * 		<li>Get rune pages by summoner ID</li>
 * 		<li>Get mastery pages by summoner ID</li>
 * 	</ol>
 * </p>
 * @see <a href="https://developer.riotgames.com/api/methods#!/292">Developer site</a>
 * 
 * @author Enigma
 */
public class SummonerMethod extends Method
{
	/**
	 * Create a new summoner method instance.
	 * @param api The API instance being used.
	 */
	public SummonerMethod(RiotApi api)
	{
		super(api, "api/lol", "summoner", "1.4", new Region[]{NA, EUW, EUNE, BR, LAN, LAS, OCE, KR});
	}
	
	//API-defined operation methods
	
	/**
	 * Returns a summoner with the specified summoner name.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerName The name of the summoner.
	 * @return The summoner with the given name.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Summoner getSummonerByName(Region region, String summonerName) throws RiotApiException
	{
		return getSummonersByName(region, summonerName).get(ApiUtil.standardizeSummonerName(summonerName));
	}
	
	/**
	 * <p>Returns the summoners with the specified summoner names (case and whitespace insensitive). Maximum of 40 names.</p>
	 * <p>The keys in the returned map are standardized summoner names, which are lower-case summoner names without whitespace.
	 * See ApiUtil for summoner name conversion utilities.</p>
	 * 
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerNames The names of the summoners.
	 * @return A map of summoner name to summoner.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 * @see ApiUtil#standardizeSummonerName(String)
	 */
	public Map<String, Summoner> getSummonersByName(Region region, String... summonerNames) throws RiotApiException
	{
		//Check argument conditions
		if(summonerNames == null || summonerNames.length > 40)
			throw new IllegalArgumentException("Only 40 summoner names are allowed per request");
		
		//Encode summoner names
		for(int n = 0; n < summonerNames.length; n++)
			summonerNames[n] = IOUtil.encodeForUri(ApiUtil.standardizeSummonerName(summonerNames[n]));
		
		//Create arg value
		String namesStr = IOUtil.createCommaDelimitedString(summonerNames);
		
		//Send request
		Response response = getMethodResult(region,
				"by-name/{summonerNames}",
				createArgMap("summonerNames", namesStr));
		
		//Check errors
		if(response.getCode() == 404)
			throw new SummonerNotFoundException(region);
		
		//Parse response
		JsonObject summonersObject = (JsonObject)response.getValue();
		Map<String, Summoner> summoners = new HashMap<>();
		for(String summonerName : summonersObject.keySet())
			summoners.put(summonerName, convertSummoner(region, (JsonObject)summonersObject.get(summonerName)));
		return summoners;
	}
	
	/**
	 * Returns a summoner with the specified summoner ID.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerId The ID of the summoner.
	 * @return The summoner with the given ID.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Summoner getSummonerById(Region region, long summonerId) throws RiotApiException
	{
		return getSummonersById(region, summonerId).get(summonerId);
	}
	
	/**
	 * Returns the summoners with the specified summoner IDs. Maximum of 40 IDs.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerIds The IDs of the summoners.
	 * @return A map from summoner ID to summoner.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Map<Long, Summoner> getSummonersById(Region region, long... summonerIds) throws RiotApiException
	{
		//Check argument conditions
		if(summonerIds == null || summonerIds.length > 40)
			throw new IllegalArgumentException("Only 40 summoner IDs are allowed per request");
		
		//Create arg value
		String idsStr = IOUtil.createCommaDelimitedString(summonerIds);
				
		//Send request
		Response response = getMethodResult(region,
				"{summonerIds}",
				createArgMap("summonerIds", String.valueOf(idsStr)));
		
		//Check errors
		if(response.getCode() == 404)
			throw new SummonerNotFoundException(region);
		
		//Parse response
		JsonObject summonersObject = (JsonObject)response.getValue();
		Map<Long, Summoner> summoners = new HashMap<>();
		for(String summonerID : summonersObject.keySet())
			summoners.put(Long.parseLong(summonerID), convertSummoner(region, (JsonObject)summonersObject.get(summonerID)));
		return summoners;
	}
	
	/**
	 * <p>Returns the summoner names of the given summoner ID.</p>
	 * <p><b>Note</b>: If one or more summoner IDs are valid, invalid summonerIDs will be ignored.<p>
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerId The summoner ID.
	 * @return The summoner name of the given summoner ID.
	 * @throws SummonerNotFoundException If all of the summoner names were not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public String getSummonerName(Region region, long summonerId) throws RiotApiException
	{
		return getSummonerNames(region, summonerId).get(summonerId);
	}
	
	/**
	 * <p>Returns a mapping of summoner IDs to summoner names from the given list of summoner IDs. Maximum of 40 IDs.</p>
	 * <p><b>Note</b>: If one or more summoner IDs are valid, invalid summonerIDs will be ignored.<p>
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerIds The list of summoner IDs.
	 * @return A map of summoner ID to summoner name.
	 * @throws SummonerNotFoundException If all of the summoner names were not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Map<Long, String> getSummonerNames(Region region, long... summonerIds) throws RiotApiException
	{
		//Check argument conditions
		if(summonerIds == null || summonerIds.length > 40)
			throw new IllegalArgumentException("Only 40 summoner IDs are allowed per request");
		
		//Create arg value
		String idsStr = IOUtil.createCommaDelimitedString(summonerIds);
		
		//Send request
		Response response = getMethodResult(region,
				"{summonerIds}/name",
				createArgMap("summonerIds", idsStr));
		
		//Check errors
		if(response.getCode() == 404)
			throw new SummonerNotFoundException(region);
		
		//Parse response
		JsonObject summonersObject = (JsonObject)response.getValue();
		Map<Long, String> summoners = new HashMap<>();
		for(String summonerId : summonersObject.keySet())
		{
			//Convert mastery page
			long id = Long.parseLong(summonerId);
			summoners.put(id, summonersObject.getString(summonerId));
		}
		return summoners;
	}
	
	/**
	 * Returns a list of the summoner's mastery pages.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summoner The summoner.
	 * @return The list of mastery pages.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<MasteryPage> getSummonerMasteryPages(Region region, Summoner summoner) throws RiotApiException
	{
		return getSummonersMasteryPages(region, summoner.getId()).get(summoner.getId());
	}
	
	/**
	 * Returns a list of the summoner's mastery pages.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerId The summoner ID.
	 * @return The list of mastery pages.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<MasteryPage> getSummonerMasteryPages(Region region, long summonerId) throws RiotApiException
	{
		return getSummonersMasteryPages(region, summonerId).get(summonerId);
	}
	
	/**
	 * Returns a map of the given summoners' IDs to their lists of mastery pages.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summoners The summoners.
	 * @return The list of mastery pages.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Map<Long, List<MasteryPage>> getSummonersMasteryPages(Region region, Summoner... summoners) throws RiotApiException
	{
		//Convert summoners to IDs
		long[] ids = new long[summoners.length];
		for(int n = 0; n < summoners.length; n++)
			ids[n] = summoners[n].getId();
		
		return getSummonersMasteryPages(region, ids);
	}
	
	/**
	 * Returns a map of the given summoner IDs to their lists of mastery pages.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerIds The IDs of the summoners.
	 * @return The list of mastery pages.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Map<Long, List<MasteryPage>> getSummonersMasteryPages(Region region, long... summonerIds) throws RiotApiException
	{
		//Check argument conditions
		if(summonerIds == null || summonerIds.length > 40)
			throw new IllegalArgumentException("Only 40 summoner IDs are allowed per request");
		
		//Create arg value
		String idsStr = IOUtil.createCommaDelimitedString(summonerIds);
		
		//Send request
		Response response = getMethodResult(region,
				"{summonerIds}/masteries",
				createArgMap("summonerIds", String.valueOf(idsStr)));
		
		//Check errors
		if(response.getCode() == 404)
			throw new SummonerNotFoundException(region);
		
		//Parse response
		JsonObject summonersObject = (JsonObject)response.getValue();
		Map<Long, List<MasteryPage>> summoners = new HashMap<>();
		
		for(String summonerId : summonersObject.keySet())
		{
			JsonObject masteryPageObject = summonersObject.getObject(summonerId);
			
			//Convert mastery page list
			List<MasteryPage> pages = new ArrayList<>(masteryPageObject.size());
			JsonArray pagesArray = masteryPageObject.getArray("pages");
			for(int p = 0; p < pagesArray.size(); p++)
				pages.add(convertMasteryPage(pagesArray.getObject(p)));
			
			summoners.put(masteryPageObject.getLong("summonerId"), pages);
		}
		return summoners;
	}
	
	/**
	 * Returns a list of the summoner's rune pages.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summoner The summoner.
	 * @return The list of rune pages.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<RunePage> getSummonerRunePages(Region region, Summoner summoner) throws RiotApiException
	{
		return getSummonersRunePages(region, summoner.getId()).get(summoner.getId());
	}
	
	/**
	 * Returns a list of the summoner's rune pages.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerId The ID of the summoner.
	 * @return The list of rune pages.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<RunePage> getSummonerRunePages(Region region, long summonerId) throws RiotApiException
	{
		return getSummonersRunePages(region, summonerId).get(summonerId);
	}
	
	/**
	 * Returns a map of the given summoners' IDs to their lists of rune pages.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summoners The summoners.
	 * @return The list of rune pages.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Map<Long, List<RunePage>> getSummonersRunePages(Region region, Summoner... summoners) throws RiotApiException
	{
		//Convert summoners to IDs
		long[] ids = new long[summoners.length];
		for(int n = 0; n < summoners.length; n++)
			ids[n] = summoners[n].getId();
		
		return getSummonersRunePages(region, ids);
	}
	
	/**
	 * Returns a map of the given summoners IDs to their lists of rune pages.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerIds The IDs of the summoners.
	 * @return The list of rune pages.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Map<Long, List<RunePage>> getSummonersRunePages(Region region, long... summonerIds) throws RiotApiException
	{
		//Check argument conditions
		if(summonerIds == null || summonerIds.length > 40)
			throw new IllegalArgumentException("Only 40 summoner IDs are allowed per request");
		
		//Create arg value
		String idsStr = IOUtil.createCommaDelimitedString(summonerIds);
		
		//Send request
		Response response = getMethodResult(region,
				"{summonerIds}/runes",
				createArgMap("summonerIds", String.valueOf(idsStr)));
		
		//Check errors
		if(response.getCode() == 404)
			throw new SummonerNotFoundException(region);
		
		//Parse response
		JsonObject summonersObject = (JsonObject)response.getValue();
		Map<Long, List<RunePage>> summoners = new HashMap<>();
		
		for(String summonerId : summonersObject.keySet())
		{
			JsonObject runePageObject = summonersObject.getObject(summonerId);
			
			//Convert rune page list
			JsonArray pagesArray = runePageObject.getArray("pages");
			List<RunePage> pages = new ArrayList<>(pagesArray.size());
			for(int p = 0; p < pagesArray.size(); p++)
			{
				JsonObject pageObject = pagesArray.getObject(p);
				pages.add(convertRunePage(pageObject));
			}
			
			summoners.put(runePageObject.getLong("summonerId"), pages);
		}
		return summoners;
	}
	
	//Other operation methods
	
	/**
	 * Fills missing information in a Summoner if required, i.e., if the summoner name or summoner ID is missing.
	 * @param summoner The summoner to fill.
	 * @return <code>true</code> if information the summoner was filled, otherwise <code>false</code>.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public boolean fillSummoner(Summoner summoner) throws RiotApiException
	{
		Summoner newSummoner = null;
		if(summoner.getName() == null)	//Missing name, have ID
			newSummoner = getSummonerById(summoner.getRegion(), summoner.getId());
		else							//Missing ID, have name
			newSummoner = getSummonerByName(summoner.getRegion(), summoner.getName());
		
		//Fill if required
		if(newSummoner != null)
		{
			summoner.setId(newSummoner.getId());
			summoner.setName(newSummoner.getName());
			summoner.setProfileIconId(newSummoner.getProfileIconId());
			summoner.setSummonerLevel(newSummoner.getSummonerLevel());
			summoner.setRevisionDate(newSummoner.getRevisionDate().getTime());
			return true;
		}
		return false;
	}
	
	//Private parsing methods
	
	/**
	 * Private helper to parse a summoner JSON object.
	 * @param region The region of the summoner.
	 * @param summonerObject The JSON object to parse.
	 * @return The summoner.
	 */
	private Summoner convertSummoner(Region region, JsonObject summonerObject)
	{
		try
		{
			Summoner summoner = new Summoner(api, region,
					summonerObject.getLong("id"), summonerObject.getString("name"),
					summonerObject.getInt("profileIconId"), summonerObject.getLong("summonerLevel"),
					summonerObject.getLong("revisionDate"));
			return summoner;
		}
		catch(JsonException e)
		{
			//Shouldn't happen since the JSON is already parsed
			System.err.println("JSON parse error");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Private helper to parse a mastery page object.
	 * @param pageObject The page's JSON object (duh).
	 * @return The converted mastery page.
	 */
	private MasteryPage convertMasteryPage(JsonObject pageObject)
	{
		//Convert talent list
		JsonArray talentsArray = pageObject.getArray("talents");
		List<MasteryPage.Mastery> talents = null;
		if(talentsArray != null)					//Can be null if no masteries are set in the page
		{
			talents = new ArrayList<>(talentsArray.size());
			for(int t = 0; t < talentsArray.size(); t++)
			{
				JsonObject talentObject = talentsArray.getObject(t);
				
				//Create talent object
				MasteryPage.Mastery talent = new MasteryPage.Mastery(talentObject.getInt("id"), talentObject.getInt("rank"));
				talents.add(talent);
			}
		}
		else
		{
			talents = new ArrayList<>(0);
		}
		
		//Create mastery page object
		MasteryPage page = new MasteryPage(pageObject.getLong("id"), pageObject.getString("name"), talents, pageObject.getBoolean("current"));
		return page;
	}
	
	/**
	 * Private helper to parse a rune page object.
	 * @param pageObject The page's JSON object (duh).
	 * @return The converted rune page.
	 */
	private RunePage convertRunePage(JsonObject pageObject)
	{
		//Convert rune slot list
		JsonArray slotsArray = pageObject.getArray("slots");
		List<RunePage.Slot> slots = new ArrayList<>();
		if(slotsArray != null)				//Can be null if no runes are set in the page
		{
			for(int t = 0; t < slotsArray.size(); t++)
			{
				JsonObject slotObject = slotsArray.getObject(t);
				
				//Create rune object
				RunePage.Rune rune = new RunePage.Rune(slotObject.getInt("runeId"));
				
				//Create slot object
				RunePage.Slot slot = new RunePage.Slot(slotObject.getInt("runeSlotId"), rune);
				slots.add(slot);
			}
		}
		
		//Create mastery page object
		RunePage page = new RunePage(pageObject.getLong("id"), pageObject.getString("name"), slots, pageObject.getBoolean("current"));
		return page;
	}
}
