//         ~ Copyright Tyler "Enigma" Haines, 2013 ~
// Distributed under the Boost Software License, Version 1.0
//             See accompanying file LICENSE.txt
//         or at http://www.boost.org/LICENSE_1_0.txt
// -----------------------------------------------------------
// This statement also applies to the rest of the source files
// in the net.enigmablade.riotapi package.  I'm just too lazy.

package net.enigmablade.riotapi;

import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.exceptions.*;
import net.enigmablade.riotapi.methods.*;
import net.enigmablade.riotapi.types.*;

/**
 * <p>Provides access to the Riot API though a number of methods as outlined in <a href="https://developer.riotgames.com/api/methods">the developer documentation</a>.</p>
 * <p>Rate limiting per 10 seconds is strictly enforced. With the default of 5 requests per 10 seconds, subsequent requests will be forced to wait a maximum of 2 seconds.</a> 
 * 
 * @see <a href="https://developer.riotgames.com/">Developer site</a>
 * 
 * @author Enigma
 */
public class RiotApi
{
	private String apiKey;
	private Requester requester;
	
	/**
	 * Creates a new instance to access the Riot API with the given API key and user agent.
	 * Rate limiting is set the default of 5 requests per 10 seconds and 50 requests per 10 minutes.
	 * @param apiKey The API key to use.
	 * @param userAgent The user agent to use.
	 */
	public RiotApi(String apiKey, String userAgent)
	{
		this.apiKey = apiKey;
		
		requester = new Requester(userAgent);
	}
	
	/**
	 * Creates a new instance to access the Riot API with the given API key, user agent, and request rate limits.
	 * @param apiKey The API key to use.
	 * @param userAgent The user agent to use.
	 * @param limitPer10Seconds The limit for the number of requests per 10 seconds.
	 * @param limitPer10Minutes The limit for the number of requests per 10 minutes.
	 */
	public RiotApi(String apiKey, String userAgent, int limitPer10Seconds, int limitPer10Minutes)
	{
		this.apiKey = apiKey;
		
		requester = new Requester(userAgent, limitPer10Seconds, limitPer10Minutes);
	}
	
	//Method management
	
	private ChampionMethod championMethod;
	private GameMethod gameMethod;
	private LeagueMethod leagueMethod;
	private SummonerMethod summonerMethod;
	private StatsMethod statsMethod;
	private TeamMethod teamMethod;
	
	/**
	 * Returns the champion method to use for champion operations.
	 * @return The champion method
	 */
	public ChampionMethod getChampionMethod()
	{
		if(championMethod == null)
			championMethod = new ChampionMethod(this);
		return championMethod;
	}
	
	/**
	 * Returns the game method to use for game operations.
	 * @return The game method
	 */
	public GameMethod getGameMethod()
	{
		if(gameMethod == null)
			gameMethod = new GameMethod(this);
		return gameMethod;
	}
	
	/**
	 * Returns the league method to use for league operations.
	 * @return The league method
	 */
	public LeagueMethod getLeagueMethod()
	{
		if(leagueMethod == null)
			leagueMethod = new LeagueMethod(this);
		return leagueMethod;
	}
	
	/**
	 * Returns the summoner method to use for summoner operations.
	 * @return The summoner method
	 */
	public SummonerMethod getSummonerMethod()
	{
		if(summonerMethod == null)
			summonerMethod = new SummonerMethod(this);
		return summonerMethod;
	}
	
	/**
	 * Returns the stats method to use for stats operations.
	 * @return The stats method
	 */
	public StatsMethod getStatsMethod()
	{
		if(statsMethod == null)
			statsMethod = new StatsMethod(this);
		return statsMethod;
	}
	
	/**
	 * Returns the team method to use for team operations.
	 * @return The team method
	 */
	public TeamMethod getTeamMethod()
	{
		if(teamMethod == null)
			teamMethod = new TeamMethod(this);
		return teamMethod;
	}
	
	//Convenience methods
	
	/**
	 * Returns the summoner with the given summoner name.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param name The summoner name.
	 * @return The summoner.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Summoner getSummoner(Region region, String name) throws RiotApiException
	{
		return getSummonerMethod().getSummonerByName(region, name);
	}
	
	/**
	 * Returns the summoner with the given summoner ID.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param id The summoner id.
	 * @return The summoner.
	 * @throws SummonerNotFoundException If the summoner was not found.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Summoner getSummoner(Region region, long id) throws RiotApiException
	{
		return getSummonerMethod().getSummonerById(region, id);
	}
	
	//Utility methods
	
	/**
	 * Clears the request cache.
	 */
	public void clearCache()
	{
		requester.clearCache();
	}
	
	//Data methods
	
	/**
	 * Returns the number of remaining API calls before hitting the 10 minute limit.
	 * @return The number of remaining API calls.
	 */
	public int getRemainingApiCalls()
	{
		return requester.getLimitPer10Minutes()-requester.getRequestsInPast10Minutes();
	}
	
	//Accessor methods
	
	/**
	 * Returns the Requester being used to send and limit API requests.
	 * @return The current Requester.
	 */
	protected Requester getRequester()
	{
		return requester;
	}
	
	/**
	 * Returns the API key being used for API requests.
	 * @return The current API key.
	 */
	public String getApiKey()
	{
		return apiKey;
	}
}
