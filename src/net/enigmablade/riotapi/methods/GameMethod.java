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
 * <p>The game method and its supporting operations.<p>
 * <p>Method support information:
 * 	<ul>
 * 		<li><i>Version</i>: 1.3</li>
 * 		<li><i>Regions</i>: NA, EUW, EUNE</li>
 * 	</ul>
 * </p>
 * <p>Operation information:
 * 	<ol>
 * 		<li>Get recent games by summoner ID (max 10)</li>
 * 	</ol>
 * </p>
 * @see <a href="https://developer.riotgames.com/api/methods#!/292">Developer site</a>
 * 
 * @author Enigma
 */
public class GameMethod extends Method
{
	/**
	 * Create a new game method instance.
	 * @param api The API instance being used.
	 */
	public GameMethod(RiotApi api)
	{
		super(api, "api/lol", "game", "1.3", new Region[]{NA, EUW, EUNE, BR, LAN, LAS, OCE, KR});
	}
	
	//API-defined operation methods
	
	/**
	 * Returns a list of recent games for the given summoner.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summoner The the summoner.
	 * @return A list of recent games (max 10).
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<Game> getRecentGames(Region region, Summoner summoner) throws RiotApiException
	{
		return getRecentGames(region, summoner.getId());
	}
	
	/**
	 * Returns a list of recent games for the given summoner.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerId The ID of the summoner.
	 * @return A list of recent games (max 10).
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<Game> getRecentGames(Region region, long summonerId) throws RiotApiException
	{
		Response response = getMethodResult(region,
				"by-summoner/{summonerId}/recent",
				createArgMap("summonerId", String.valueOf(summonerId)));
		
		//Check errors
		if(response.getCode() == 404)
			throw new GameDataNotFoundException(region, summonerId);
		
		//Parse response
		JsonObject root = (JsonObject)response.getValue();
		return convertRecentGames(root, region, summonerId);
	}
	
	//Private converter methods
	
	private List<Game> convertRecentGames(JsonObject recentGamesObject, Region region, long summonerId) throws RiotApiException
	{
		//Check summoner IDs to make sure the server isn't crazy
		long rootSummonerId = recentGamesObject.getLong("summonerId");
		if(rootSummonerId != summonerId)
			throw new RiotApiException("Server returned invalid data: summoner ID mismatch");
		
		//Convert game list
		JsonArray gamesArray = recentGamesObject.getArray("games");
		if(gamesArray == null)								//Might be null if no games have been played
			return new ArrayList<>(0);
		
		List<Game> games = new ArrayList<>(gamesArray.size());
		for(int g = 0; g < gamesArray.size(); g++)
		{
			JsonObject gameObject = gamesArray.getObject(g);
			
			//Convert player list
			List<Player> players = convertPlayerList(gameObject.getArray("fellowPlayers"), region);
			
			//Convert statistic list
			Map<String, Object> stats = convertGameStats(gameObject.getObject("stats"));
			
			//Create game object
			Game game = new Game(api, region,
					gameObject.getInt("championId"), gameObject.getInt("level"), gameObject.getInt("spell1"), gameObject.getInt("spell2"),
					gameObject.getLong("createDate"), gameObject.getBoolean("invalid"),
					gameObject.getLong("gameId"), gameObject.getString("gameMode"), gameObject.getString("gameType"), gameObject.getString("subType"), gameObject.getInt("mapId"),
					gameObject.getInt("teamId"), players, stats);
			games.add(game);
		}
		return games;
	}
	
	/**
	 * Private helper to convert a list of players in a game.
	 * @param playersArray The original list of players.
	 * @param region The region the players are in.
	 * @return The new converted list of players.
	 * @throws JsonException Shouldn't ever happen.
	 */
	private List<Player> convertPlayerList(JsonArray playersArray, Region region) throws JsonException
	{
		List<Player> players = null;
		if(playersArray != null)
		{
			players = new ArrayList<>(playersArray.size());
			for(int p = 0; p < playersArray.size(); p++)
			{
				JsonObject playerObject = playersArray.getObject(p);
				Player player = new Player(api, region, playerObject.getLong("summonerId"), playerObject.getInt("championId"), playerObject.getInt("teamId"));
				players.add(player);
			}
		}
		else
		{
			players = new ArrayList<>(0);
		}
		return players;
	}
	
	/**
	 * Private helper to convert a list of stats in a game.
	 * @param statsArray The original list of players.
	 * @return The new converted list of stats.
	 * @throws JsonException Shouldn't ever happen.
	 */
	private Map<String, Object> convertGameStats(JsonObject statsObject) throws JsonException
	{
		Map<String, Object> stats = new TreeMap<>();
		if(statsObject != null)
		{
			for(String key : statsObject.keySet())
			{
				Object value = statsObject.get(key);
				stats.put(key, value);
			}
		}
		return stats;
	}
}
