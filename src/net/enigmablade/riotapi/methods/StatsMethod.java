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
 * <p>The stats method and its supporting operations.<p>
 * <p>Method support information:
 * 	<ul>
 * 		<li><i>Version</i>: 1.1</li>
 * 		<li><i>Regions</i>: NA, EUW, EUNE</li>
 * 	</ul>
 * </p>
 * <p>Operation information:
 * 	<ol>
 * 		<li>Get a summoner stat summaries for each queue</li>
 * 		<li>Get a summoner ranked stats</li>
 * 	</ol>
 * </p>
 * @see <a href="https://developer.riotgames.com/api/methods#!/294">Developer site</a>
 * 
 * @author Enigma
 */
public class StatsMethod extends Method
{
	/**
	 * Create a new stats method instance.
	 * @param api The API instance being used.
	 */
	public StatsMethod(RiotApi api)
	{
		super(api, "api/lol", "stats", "1.1", new Region[]{NA, EUW, EUNE});
	}
	
	//API-defined operation methods
	
	/**
	 * Returns a list of stat summaries for a summoner during the current season.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerId The ID of the summoner.
	 * @return The list of stat summaries.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<PlayerStats> getCurrentSeasonStatSummaries(Region region, long summonerId) throws RiotApiException
	{
		return getStatSummaries(region, summonerId, null);
	}
	
	/**
	 * Returns a list of stat summaries for a summoner during the given season.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerId The ID of the summoner.
	 * @param season The season of stats to retrieve.
	 * @return The list of stat summaries.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<PlayerStats> getStatSummaries(Region region, long summonerId, Season season) throws RiotApiException
	{
		//Send request
		Response response = getMethodResult(region,
				"by-summoner/{summonerId}/summary",
				createArgMap("summonerId", String.valueOf(summonerId)),
				season != null ? createArgMap("season", season.getValue()) : null);
		
		//Check errors
		if(response.getCode() == 404)
			throw new SummonerNotFoundException(region, summonerId);
		
		//Parse response
		try
		{
			JsonObject root = (JsonObject)response.getValue();
			
			//Check summoner IDs to make sure the server isn't crazy
			long rootSummonerId = root.getLong("summonerId");
			if(rootSummonerId != summonerId)
				throw new RiotApiException("Server returned invalid data: summoner ID mismatch");
			
			//Convert stat summaries list
			JsonArray sumsArray = root.getArray("playerStatSummaries");
			List<PlayerStats> sums = new ArrayList<>(sumsArray.size());
			for(int s = 0; s < sumsArray.size(); s++)
			{
				JsonObject sumObject = sumsArray.getObject(s);
				
				//Convert stats list
				JsonArray statsArray = sumObject.getArray("aggregatedStats");
				List<PlayerStats.AggregatedStat> stats = new ArrayList<>(statsArray.size());
				for(int a = 0; a < statsArray.size(); a++)
				{
					JsonObject statObject = statsArray.getObject(a);
					
					//Create stat object
					PlayerStats.AggregatedStat stat = new PlayerStats.AggregatedStat(statObject.getInt("id"), statObject.getString("name"),
							statObject.getInt("count"));
					stats.add(stat);
				}
				
				//Create stat summary object
				PlayerStats sum = new PlayerStats(sumObject.getString("playerStatSummaryType"), sumObject.getLong("modifyDate"),
						sumObject.getInt("wins"), sumObject.getInt("losses"), stats);
				sums.add(sum);
			}
			return sums;
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
	 * Returns a list of ranked stats for a summoner during the current season.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerId The ID of the summoner.
	 * @return The list of stat summaries.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<ChampionStats> getCurrentSeasonRankedChampionStats(Region region, long summonerId) throws RiotApiException
	{
		return getRankedChampionStats(region, summonerId, null);
	}
	
	/**
	 * Returns a list of ranked stats for a summoner during the given season.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerId The ID of the summoner.
	 * @param season The season of stats to retrieve.
	 * @return The list of stat summaries.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<ChampionStats> getRankedChampionStats(Region region, long summonerId, Season season) throws RiotApiException
	{
		//Send request
		Response response = getMethodResult(region,
				"by-summoner/{summonerId}/ranked",
				createArgMap("summonerId", String.valueOf(summonerId)),
				season != null ? createArgMap("season", season.getValue()) : null);
		
		//Check errors
		if(response.getCode() == 404)
			throw new SummonerNotFoundException(region, summonerId);
		
		//Parse response
		try
		{
			JsonObject root = (JsonObject)response.getValue();
			
			//Check summoner IDs to make sure the server isn't crazy
			long rootSummonerId = root.getLong("summonerId");
			if(rootSummonerId != summonerId)
				throw new RiotApiException("Server returned invalid data: summoner ID mismatch");
			
			//Convert stat summaries list
			JsonArray championsArray = root.getArray("champions");
			List<ChampionStats> champions = new ArrayList<>(championsArray.size());
			for(int c = 0; c < championsArray.size(); c++)
			{
				JsonObject championObject = championsArray.getObject(c);
				
				//Convert stats list
				JsonArray statsArray = championObject.getArray("stats");
				List<ChampionStats.Stat> stats = new ArrayList<>(statsArray.size());
				for(int a = 0; a < statsArray.size(); a++)
				{
					JsonObject statObject = statsArray.getObject(a);
					
					//Create stat object
					ChampionStats.Stat stat = new ChampionStats.Stat(statObject.getInt("id"), statObject.getString("name"),
							statObject.getInt("value"), statObject.getInt("c" /* Docs say "count", so API is wrong :( */ ));
					stats.add(stat);
				}
				
				//Create stat summary object
				ChampionStats sum = new ChampionStats(championObject.getInt("id"), championObject.getString("name"), stats);
				champions.add(sum);
			}
			return champions;
		}
		catch(JsonException e)
		{
			//Shouldn't happen since the JSON is already parsed
			System.err.println("JSON parse error");
			e.printStackTrace();
			return null;
		}
	}
}
