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
	 * @param limitPer10Seconds 
	 * @param limitPer10Minutes 
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
	
	/**
	 * Returns the champion method to use for champion calls.
	 * @return The champion method
	 */
	public ChampionMethod getChampionMethod()
	{
		if(championMethod == null)
			championMethod = new ChampionMethod(this);
		return championMethod;
	}
	
	/**
	 * Returns the game method to use for game calls.
	 * @return The game method
	 */
	public GameMethod getGameMethod()
	{
		if(gameMethod == null)
			gameMethod = new GameMethod(this);
		return gameMethod;
	}
	
	/**
	 * Returns the league method to use for game calls.
	 * @return The league method
	 */
	public LeagueMethod getLeagueMethod()
	{
		if(leagueMethod == null)
			leagueMethod = new LeagueMethod(this);
		return leagueMethod;
	}
	
	/**
	 * Returns the summoner method to use for game calls.
	 * @return The summoner method
	 */
	public SummonerMethod getSummonerMethod()
	{
		if(summonerMethod == null)
			summonerMethod = new SummonerMethod(this);
		return summonerMethod;
	}
	
	//Convenience methods
	
	public Summoner getSummoner(Region region, String name) throws RiotApiException
	{
		return getSummonerMethod().getSummonerByName(region, name);
	}
	
	public Summoner getSummoner(Region region, long id) throws RiotApiException
	{
		return getSummonerMethod().getSummonerById(region, id);
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
	
	protected Requester getRequester()
	{
		return requester;
	}
	
	public String getApiKey()
	{
		return apiKey;
	}
}
