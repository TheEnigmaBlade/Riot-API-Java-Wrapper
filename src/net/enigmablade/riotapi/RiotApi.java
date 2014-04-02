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
 * <p>Rate limiting per 10 seconds is strictly enforced. With the default of 10 requests per 10 seconds, subsequent requests will be forced to wait a maximum of 1 second.</a> 
 * 
 * @see <a href="https://developer.riotgames.com/">Developer site</a>
 * 
 * @author Enigma
 */
public class RiotApi
{
	public static final int DEFAULT_RATE_LIMIT_10_SEC = 10;
	public static final int DEFAULT_RATE_LIMIT_10_MIN = 500;
	
	private String apiKey;
	private Requester requester;
	
	private Locale defaultLocale;
	private Region defaultRegion;
	
	/**
	 * Creates a new instance to access the Riot API with the given API key and no user agent.
	 * Rate limiting is set the default of {@value RiotApi#DEFAULT_RATE_LIMIT_10_SEC} requests per 10 seconds and {@value RiotApi#DEFAULT_RATE_LIMIT_10_MIN} requests per 10 minutes.
	 * @param apiKey The API key to use.
	 */
	public RiotApi(String apiKey)
	{
		this(apiKey, null);
	}
	
	/**
	 * Creates a new instance to access the Riot API with the given API key and user agent.
	 * Rate limiting is set the default of {@value RiotApi#DEFAULT_RATE_LIMIT_10_SEC} requests per 10 seconds and {@value RiotApi#DEFAULT_RATE_LIMIT_10_MIN} requests per 10 minutes.
	 * @param apiKey The API key to use.
	 * @param userAgent The user agent to use.
	 */
	public RiotApi(String apiKey, String userAgent)
	{
		this(apiKey, userAgent, DEFAULT_RATE_LIMIT_10_SEC, DEFAULT_RATE_LIMIT_10_MIN);
	}
	
	/**
	 * Creates a new instance to access the Riot API with the given API key, user agent, and request rate limits.
	 * @param apiKey The API key to use.
	 * @param userAgent The user agent to use.
	 * @param limitPer10Seconds The limit for the number of requests per 10 seconds. Must be greater than 0.
	 * @param limitPer10Minutes The limit for the number of requests per 10 minutes. Must be greater than 0.
	 */
	public RiotApi(String apiKey, String userAgent, int limitPer10Seconds, int limitPer10Minutes)
	{
		this.apiKey = apiKey;
		
		requester = new Requester(userAgent, limitPer10Seconds, limitPer10Minutes);
		
		defaultLocale = null;
	}
	
	//Method management
	
	private ChampionMethod championMethod;
	private GameMethod gameMethod;
	private LeagueMethod leagueMethod;
	private SummonerMethod summonerMethod;
	private StatsMethod statsMethod;
	private TeamMethod teamMethod;
	private StaticDataMethod staticDataMethod;
	
	/**
	 * Returns the champion method to use for champion operations.
	 * @return The champion method
	 */
	public ChampionMethod getChampionApiMethod()
	{
		if(championMethod == null)
			championMethod = new ChampionMethod(this);
		return championMethod;
	}
	
	/**
	 * Returns the game method to use for game operations.
	 * @return The game method
	 */
	public GameMethod getGameApiMethod()
	{
		if(gameMethod == null)
			gameMethod = new GameMethod(this);
		return gameMethod;
	}
	
	/**
	 * Returns the league method to use for league operations.
	 * @return The league method
	 */
	public LeagueMethod getLeagueApiMethod()
	{
		if(leagueMethod == null)
			leagueMethod = new LeagueMethod(this);
		return leagueMethod;
	}
	
	/**
	 * Returns the summoner method to use for summoner operations.
	 * @return The summoner method
	 */
	public SummonerMethod getSummonerApiMethod()
	{
		if(summonerMethod == null)
			summonerMethod = new SummonerMethod(this);
		return summonerMethod;
	}
	
	/**
	 * Returns the stats method to use for stats operations.
	 * @return The stats method
	 */
	public StatsMethod getStatsApiMethod()
	{
		if(statsMethod == null)
			statsMethod = new StatsMethod(this);
		return statsMethod;
	}
	
	/**
	 * Returns the team method to use for team operations.
	 * @return The team method
	 */
	public TeamMethod getTeamApiMethod()
	{
		if(teamMethod == null)
			teamMethod = new TeamMethod(this);
		return teamMethod;
	}
	
	/**
	 * Returns the static data method to retrieve static LoL data.
	 * @return The static data method.
	 */
	public StaticDataMethod getStaticDataApiMethod()
	{
		if(staticDataMethod == null)
			staticDataMethod = new StaticDataMethod(this);
		return staticDataMethod;
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
		return getSummonerApiMethod().getSummonerByName(region, name);
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
		return getSummonerApiMethod().getSummonerById(region, id);
	}
	
	//Utility methods
	
	/**
	 * Sets whether or not the rate limit on requests is enforced.
	 * @param enabled Whether or not request limits are enforced.
	 */
	public void setRateLimitEnabled(boolean enabled)
	{
		requester.setRateLimitEnabled(enabled);
	}
	
	/**
	 * Returns whether or not the rate limit on requests is enforced.
	 * @return <code>true</code> if rate limits are enforced, otherwise <code>false</code>.
	 */
	public boolean isRateLimitEnabled()
	{
		return requester.isRateLimitEnabled();
	}
	
	/**
	 * Sets whether or not request caching is enabled.
	 * @param enabled Whether or not request caching is enabled.
	 */
	public void setCacheEnabled(boolean enabled)
	{
		requester.setCacheEnabled(enabled);
	}
	
	/**
	 * Returns whether or not request caching is enabled.
	 * @return <code>true</code> if request caching is enabled, otherwise <code>false</code>.
	 */
	public boolean isCacheEnabled()
	{
		return requester.isCacheEnabled();
	}
	
	/**
	 * Clears the request cache.
	 */
	public void clearApiCallCache()
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
	
	/**
	 * Returns the time (in milliseconds) until the oldest request is 10 minutes old.
	 * @return The millisecond time until the oldest request.
	 */
	public long getTimeUntilMoreApiCalls()
	{
		return System.currentTimeMillis() - (requester.getRequestsInPast10Seconds() > requester.getLimitPer10Seconds() ? requester.getOldestRequestTimeByAge(10000) : requester.getOldestRequestTime());
	}
	
	//Accessor methods
	
	/**
	 * Returns the Requester being used to send and limit API requests.
	 * @return The current Requester.
	 */
	public Requester getRequester()
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
	
	/**
	 * Sets the API key to use with API requests.
	 * @param apiKey The new API key.
	 */
	public void setApiKey(String apiKey)
	{
		this.apiKey = apiKey;
	}
	
	/**
	 * Returns the default region being used by requests.
	 * @return The default region.
	 */
	public Region getDefaultRegion()
	{
		return defaultRegion;
	}
	
	/**
	 * Sets the default region to be used by requests.
	 * @param defaultRegion The new default region.
	 */
	public void setDefaultRegion(Region defaultRegion)
	{
		this.defaultRegion = defaultRegion;
	}
	
	/**
	 * Returns the default locale being used by static data requests.
	 * @return The default locale, or <code>null</code> if using the API default.
	 */
	public Locale getDefaultLocale()
	{
		return defaultLocale;
	}
	
	/**
	 * Sets the default locale to be used by static data requests.
	 * Use <code>null</code> to use the API default.
	 * @param locale The new default locale.
	 */
	public void setDefaultLocale(Locale locale)
	{
		this.defaultLocale = locale;
	}
}
