package net.enigmablade.riotapi.methods;

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
 * 		<li><i>Version</i>: 1.1</li>
 * 		<li><i>Regions</i>: NA, EUW, EUNE</li>
 * 	</ul>
 * </p>
 * <p>Operation information:
 * 	<ol>
 * 		<li>Get a summoner by name</li>
 * 		<li>Get a summoner by ID</li>
 * 		<li>Get multiple summoner names by ID</li>
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
		super(api, "api/lol", "summoner", "1.1", new Region[]{NA, EUW, EUNE});
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
		//Encode the summoner name for the URL
		summonerName = RiotApiUtil.encodeForUri(summonerName);
		
		//Send request
		Response response = getMethodResult(region,
				"by-name/{summonerName}",
				createArgMap("summonerName", summonerName));
		
		//Check errors
		if(response.getCode() == 404)
			throw new SummonerNotFoundException(region, summonerName);
		
		//Parse response
		return parseSummoner(region, (JsonObject)response.getValue());
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
		//Send request
		Response response = getMethodResult(region,
				"{summonerId}",
				createArgMap("summonerId", String.valueOf(summonerId)));
		
		//Check errors
		if(response.getCode() == 404)
			throw new SummonerNotFoundException(region, summonerId);
		
		//Parse response
		return parseSummoner(region, (JsonObject)response.getValue());
	}
	
	/**
	 * <p>Returns a list of summoners (id and name only) from the given list of summoner IDs. Maximum of 40 IDs.</p>
	 * <p><b>Note</b>: If one or more summoner IDs are valid, invalid summonerIDs will be ignored.<p>
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerIds The list of summoner IDs.
	 * @return The list of summoners.
	 * @throws SummonerNotFoundException If all of the summoner names were not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Summoner[] getSummonerNames(Region region, long... summonerIds) throws RiotApiException
	{
		if(summonerIds.length > 40)
			throw new IllegalArgumentException("Only 40 summoner IDs are allowed per request");
		
		//Create arg value
		StringBuffer idsStr = new StringBuffer();
		for(int n = 0; n < summonerIds.length; n++)
		{
			idsStr.append(summonerIds[n]);
			if(n < summonerIds.length-1)
				idsStr.append(',');
		}
		
		//Send request
		Response response = getMethodResult(region,
				"{summonerIds}/name",
				createArgMap("summonerIds", idsStr.toString()));
		
		//Check errors
		if(response.getCode() == 404)
			throw new SummonerNotFoundException(region);
		
		//Parse response
		try
		{
			JsonObject summonersObject = (JsonObject)response.getValue();
			
			//Convert list of summoner names
			JsonArray summonersArray = summonersObject.getArray("summoners");
			Summoner[] summoners = new Summoner[summonersArray.size()];
			for(int n = 0; n < summonersArray.size(); n++)
			{
				JsonObject summonerObject = summonersArray.getObject(n);
				summoners[n] = new Summoner(api, region, summonerObject.getLong("id"), summonerObject.getString("name"));
			}
			return summoners;
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
	 * Private helper to parse a summoner JSON object.
	 * @param region The region of the summoner.
	 * @param summonerObject The JSON object to parse.
	 * @return The summoner.
	 */
	private Summoner parseSummoner(Region region, JsonObject summonerObject)
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
	
	//TODO: mastery and rune pages
	
	//Other methods
	
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
		else if(summoner.getId() < 0)	//Missing ID, have name
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
}