package net.enigmablade.riotapi.methods;

import static net.enigmablade.riotapi.constants.Region.*;
import java.util.*;
import net.enigmablade.jsonic.*;
import net.enigmablade.riotapi.Requester.Response;
import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.exceptions.*;
import net.enigmablade.riotapi.types.*;
import net.enigmablade.riotapi.util.*;

/**
 * <p>The league method and its supporting operations.<p>
 * <p>Method support information:
 * 	<ul>
 * 		<li><i>Version</i>: 2.4</li>
 * 		<li><i>Regions</i>: NA, EUW, EUNE, LAN, LAS, OCE, BR, TR, RU, KR</li>
 * 	</ul>
 * </p>
 * <p>Operation information:
 * 	<ol>
 * 		<li>Get all leagues and team leagues associated with a summoner ID</li>
 * 	</ol>
 * </p>
 * @see <a href="https://developer.riotgames.com/api/methods#!/369">Developer site</a>
 * 
 * @author Enigma
 */
public class LeagueMethod extends Method
{
	/**
	 * Create a new league method instance.
	 * @param api The API instance being used.
	 */
	public LeagueMethod(RiotApi api)
	{
		super(api, "api/lol", "league", "2.4", new Region[]{NA, EUW, EUNE, LAN, LAS, OCE, BR, TR, RU, KR});
	}
	
	//API-defined operation methods
	
	/**
	 * Returns a list of leagues for the given summoner.
	 * @param region The league region (NA, EUW, EUNE, etc.)
	 * @param summonerId The ID of the summoner.
	 * @return A list of leagues.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws LeagueNotFoundException If the given summoner is not in any leagues.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Map<String, List<League>> getLeagues(Region region, long... summonerIds) throws RiotApiException
	{
		return getLeaguesHelper(region, IOUtil.createCommaDelimitedString(summonerIds), "summoner");
	}
	
	/**
	 * Returns a list of leagues entries only for the given summoner.
	 * @param region The league region (NA, EUW, EUNE, etc.)
	 * @param summonerId The ID of the summoner.
	 * @return A list of league entries.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws LeagueNotFoundException If the given summoner is not in any leagues.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Map<String, List<League>> getLeagueEntries(Region region, long... summonerIds) throws RiotApiException
	{
		return getLeagueEntriesHelper(region, IOUtil.createCommaDelimitedString(summonerIds), "summoner");
	}
	
	/**
	 * Returns a list of leagues for the given team.
	 * @param region The league region (NA, EUW, EUNE, etc.)
	 * @param teamId The ID of the team.
	 * @return A list of leagues.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws LeagueNotFoundException If the given teamId is not in any leagues.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Map<String, List<League>> getLeagues(Region region, String... teamIds) throws RiotApiException
	{
		return getLeaguesHelper(region, IOUtil.createCommaDelimitedString(teamIds), "team");
	}
	
	/**
	 * Returns a list of leagues entries for the given team.
	 * @param region The league region (NA, EUW, EUNE, etc.)
	 * @param teamId The ID of the team.
	 * @return A list of league entries.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws LeagueNotFoundException If the given team is not in any leagues.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Map<String, List<League>> getLeagueEntries(Region region, String... teamIds) throws RiotApiException
	{
		return getLeagueEntriesHelper(region, IOUtil.createCommaDelimitedString(teamIds), "team");
	}
	
	/**
	 * Returns a the challenger league for the given region and queue.
	 * @param region The league region (NA, EUW, EUNE, etc.)
	 * @param queue The ranked queue.
	 * @return The challenger league.
	 * @throws IllegalArgumentException If the given ranked queue is not ranked.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws LeagueNotFoundException If the given summoner is not in any leagues.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public League getChallengerLeague(Region region, QueueType queue) throws RiotApiException
	{
		//Check arguments
		if(queue == null || !queue.isRanked())
			throw new IllegalArgumentException("Queue type must exist and be ranked.");
		
		//Make request
		Response response = getMethodResult(region,
				"challenger",
				null,
				createArgMap("type", queue.getLeagueValue()));
		
		//Parse response
		return convertLeague((JsonObject)response.getValue());
	}
	
	//Helper methods
	
	/**
	 * Returns a list of leagues for the given participant.
	 * @param region The league region (NA, EUW, EUNE, etc.)
	 * @param participantId The participant's ID.
	 * @param participantType The type of the participant ("summoner" or "team").
	 * @return A map of leagues.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws LeagueNotFoundException If the given team is not in any leagues.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	private Map<String, List<League>> getLeaguesHelper(Region region, String participantIds, String participantType) throws RiotApiException
	{
		//Make request
		Response response = getMethodResult(region,
				"by-"+participantType+"/{id}",
				createArgMap("id", participantIds));
		
		//Check errors
		checkLeagueErrors(response.getCode(), region, participantIds);
		
		//Parse response
		return convertLeagues((JsonObject)response.getValue());
	}
	
	/**
	 * Returns a list of league entries for the given participant.
	 * @param region The league region (NA, EUW, EUNE, etc.)
	 * @param participantId The participant's ID.
	 * @param participantType The type of the participant ("summoner" or "team").
	 * @return A list of league entries.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws LeagueNotFoundException If the given team is not in any leagues.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	private Map<String, List<League>> getLeagueEntriesHelper(Region region, String participantIds, String participantType) throws RiotApiException
	{
		//Make request
		Response response = getMethodResult(region,
				"by-"+participantType+"/{id}/entry",
				createArgMap("id", participantIds));
		
		//Check errors
		checkLeagueErrors(response.getCode(), region, participantIds);
		
		//Parse response
		return convertLeagues((JsonObject)response.getValue());
	}
	
	/**
	 * Check for response errors.
	 * @param responseCode The response code.
	 * @param region The region (not always used).
	 * @param summonerId The summoner ID (not always used).
	 * @throws RiotApiException If there was an error.
	 */
	private void checkLeagueErrors(int responseCode, Region region, String participantIds) throws RiotApiException
	{
		switch(responseCode)
		{
			case 404: throw new LeagueNotFoundException(region, participantIds);
		}
	}
	
	//Private converter methods
	
	/**
	 * Converts a JSON array of leagues to a list of league objects.
	 * @param leaguesObject The JSON array to be converted.
	 * @return The converted list of leagues.
	 */
	private Map<String, List<League>> convertLeagues(JsonObject leaguesObject)
	{
		Map<String, List<League>> leagues = new HashMap<>(leaguesObject.size());
		
		//Convert to list of leagues
		for(String participantId : leaguesObject.keySet())
		{
			JsonArray leaguesArray = leaguesObject.getArray(participantId);
			List<League> leaguesList = new ArrayList<>(leaguesArray.size());
			
			for(JsonIterator it = leaguesArray.iterator(); it.hasNext();)
				leaguesList.add(convertLeague(it.nextObject()));
			
			//Convert league object
			leagues.put(participantId, leaguesList);
		}
		
		return leagues;
	}
	
	/**
	 * Converts a JSON league to a league object.
	 * @param leagueObject The JSON league.
	 * @return The league.
	 */
	private League convertLeague(JsonObject leagueObject)
	{
		//Convert league entries list
		JsonArray entriesArray = leagueObject.getArray("entries");
		List<League.Entry> entries = new ArrayList<>(entriesArray.size());
		for(int l = 0; l < entriesArray.size(); l++)
		{
			JsonObject entryObject = entriesArray.getObject(l);
			League.Entry entry = convertLeagueEntry(entryObject);
			entries.add(entry);
		}
		
		//Create league
		League league = new League(leagueObject.getString("name"), leagueObject.getString("participantId"), leagueObject.getString("queue"), leagueObject.getString("tier"), entries);
		return league;
	}
	
	/**
	 * Converts a JSON object representing a league entry into a league entry object.
	 * @param entryObject The JSON object.
	 * @return The converted league entry.
	 */
	private League.Entry convertLeagueEntry(JsonObject entryObject)
	{
		//Convert league entry series if exists
		League.Entry.Series series = convertMiniSeries(entryObject.getObject("miniSeries"));
		
		//Create entry
		League.Entry entry = new League.Entry(entryObject.getString("division"),
				entryObject.getString("playerOrTeamId"), entryObject.getString("playerOrTeamName"),
				entryObject.getBoolean("isHotStreak"), entryObject.getBoolean("isFreshBlood"), entryObject.getBoolean("isVeteran"), entryObject.getBoolean("isInactive"),
				entryObject.getInt("wins"), entryObject.getInt("leaguePoints"),
				series);
		return entry;
	}
	
	/**
	 * Converts a JSON series object to a series object.
	 * @param seriesObject The JSON series object.
	 * @return The converted series object.
	 */
	private League.Entry.Series convertMiniSeries(JsonObject seriesObject)
	{
		League.Entry.Series series = null;
		if(seriesObject != null)
		{
			series = new League.Entry.Series(seriesObject.getInt("target"),
					seriesObject.getInt("wins"), seriesObject.getInt("losses"),
					seriesObject.getString("progress"));
		}
		return series;
	}
}
