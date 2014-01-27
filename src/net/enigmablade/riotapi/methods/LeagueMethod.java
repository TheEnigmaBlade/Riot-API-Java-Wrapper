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
	 * Returns a list of recent games for the given summoner.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerId The ID of the summoner.
	 * @return A list of recent games (max 10).
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws LeagueNotFoundException If the given summoner is not in any leagues.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<League> getLeagues(Region region, long summonerId) throws RiotApiException
	{
		Response response = getMethodResult(region,
				"by-summoner/{summonerId}",
				createArgMap("summonerId", String.valueOf(summonerId)));
		
		//Check errors
		switch(response.getCode())
		{
			case 404: throw new LeagueNotFoundException(region, summonerId);
		}
		
		//Parse response
		return convertLeagues((JsonArray)response.getValue());
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
			
			//Convert league entries list
			JsonArray entriesArray = leagueObject.getArray("entries");
			List<League.Entry> entries = new ArrayList<>(entriesArray.size());
			for(int l = 0; l < entriesArray.size(); l++)
			{
				JsonObject entryObject = entriesArray.getObject(l);
				
				//Convert league entry series if exists
				League.Entry.Series series = convertMiniSeries(entryObject.getObject("miniSeries"));
				
				//Create entry
				League.Entry entry = new League.Entry(entryObject.getString("tier"), entryObject.getString("rank"),	entryObject.getString("queueType"), entryObject.getString("leagueName"),
						entryObject.getString("playerOrTeamId"), entryObject.getString("playerOrTeamName"),
						entryObject.getBoolean("isHotStreak"), entryObject.getBoolean("isFreshBlood"), entryObject.getBoolean("isVeteran"), entryObject.getBoolean("isInactive"),
						entryObject.getInt("wins"), entryObject.getInt("leaguePoints"), series,
						entryObject.getLong("lastPlayed"));
				entries.add(entry);
			}
			
			//Create league
			League league = new League(leagueObject.getString("name"), leagueObject.getString("participantId"), leagueObject.getString("queue"), leagueObject.getString("tier"), entries);
			leagues.add(league);
		}
		
		return leagues;
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
