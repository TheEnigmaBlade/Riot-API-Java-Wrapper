package net.enigmablade.riotapi.methods;

import java.util.*;
import net.enigmablade.jsonic.*;
import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.Requester.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.exceptions.*;
import net.enigmablade.riotapi.types.*;
import static net.enigmablade.riotapi.constants.Region.*;

/**
 * <p>The league method and its supporting operations.<p>
 * <p>Method support information:
 * 	<ul>
 * 		<li><i>Version</i>: 2.3</li>
 * 		<li><i>Regions</i>: NA, EUW, EUNE, BR, TR</li>
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
		super(api, "api/lol", "league", "2.3", new Region[]{NA, EUW, EUNE, BR, TR});
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
	public List<League> getLeagues(Region region, long summonerId) throws RiotApiException
	{
		//Make request
		Response response = getMethodResult(region,
				"by-summoner/{summonerId}",
				createArgMap("summonerId", String.valueOf(summonerId)));
		
		//Check errors
		checkLeagueErrors(response.getCode(), region, summonerId);
		
		//Parse response
		return convertLeagues((JsonArray)response.getValue());
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
	public List<League.Entry> getLeagueEntries(Region region, long summonerId) throws RiotApiException
	{
		//Make request
		Response response = getMethodResult(region,
				"by-summoner/{summonerId}/entry",
				createArgMap("summonerId", String.valueOf(summonerId)));
		
		//Check errors
		checkLeagueErrors(response.getCode(), region, summonerId);
		
		//Parse response
		JsonArray leagueEntriesArray = (JsonArray)response.getValue();
		List<League.Entry> leagueEntries = new ArrayList<>(leagueEntriesArray.size());
		for(int n = 0; n < leagueEntriesArray.size(); n++)
		{
			//Convert a league entry
			JsonObject leagueEntryObject = leagueEntriesArray.getObject(n);
			leagueEntries.add(convertLeagueEntry(leagueEntryObject));
		}
		return leagueEntries;
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
	 * Check for response errors.
	 * @param responseCode The response code.
	 * @param region The region (not always used).
	 * @param summonerId The summoner ID (not always used).
	 * @throws RiotApiException If there was an error.
	 */
	private void checkLeagueErrors(int responseCode, Region region, long summonerId) throws RiotApiException
	{
		switch(responseCode)
		{
			case 404: throw new LeagueNotFoundException(region, summonerId);
		}
	}
	
	//Private converter methods
	
	/**
	 * Converts a JSON array of leagues to a list of league objects.
	 * @param leaguesArray The JSON array to be converted.
	 * @return The converted list of leagues.
	 */
	private List<League> convertLeagues(JsonArray leaguesArray)
	{
		List<League> leagues = new ArrayList<>(leaguesArray.size());
		
		//Convert to list of leagues
		for(int n = 0; n < leaguesArray.size(); n++)
		{
			JsonObject leagueObject = leaguesArray.getObject(n);
			
			//Convert league object
			leagues.add(convertLeague(leagueObject));
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
			entries.add(convertLeagueEntry(entryObject));
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
		League.Entry entry = new League.Entry(entryObject.getString("tier"), entryObject.getString("rank"),	entryObject.getString("queueType"), entryObject.getString("leagueName"),
				entryObject.getString("playerOrTeamId"), entryObject.getString("playerOrTeamName"),
				entryObject.getBoolean("isHotStreak"), entryObject.getBoolean("isFreshBlood"), entryObject.getBoolean("isVeteran"), entryObject.getBoolean("isInactive"),
				entryObject.getInt("wins"), entryObject.getInt("leaguePoints"), series,
				entryObject.getLong("lastPlayed"));
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
					seriesObject.getString("progress"),
					seriesObject.getLong("timeLeftToPlayMillis"));
		}
		return series;
	}
}
