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
 * 		<li><i>Version</i>: 1.2</li>
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
		super(api, "api/lol", "game", "1.2", new Region[]{NA, EUW, EUNE});
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
		
		//Parse response
		try
		{
			JsonObject root = (JsonObject)response.getValue();
			
			//Check summoner IDs to make sure the server isn't crazy
			long rootSummonerId = root.getLong("summonerId");
			if(rootSummonerId != summonerId)
				throw new RiotApiException("Server returned invalid data: summoner ID mismatch");
			
			//Convert game list
			JsonArray gamesArray = root.getArray("games");
			if(gamesArray == null)								//Might be null if no games have been played
				return new ArrayList<>(0);
			
			List<Game> games = new ArrayList<>(gamesArray.size());
			for(int g = 0; g < gamesArray.size(); g++)
			{
				JsonObject gameObject = gamesArray.getObject(g);
				
				//Convert player list
				List<Player> players = convertPlayerList(gameObject.getArray("fellowPlayers"), region);
				
				//Convert statistic list
				Map<String, Game.Stat> stats = convertGameStats(gameObject.getArray("statistics"));
				
				//Create game object
				Game game = new Game(gameObject.getInt("championId"), gameObject.getInt("level"), gameObject.getInt("spell1"), gameObject.getInt("spell2"),
						gameObject.getLong("createDate"), gameObject.getBoolean("invalid"),
						gameObject.getLong("gameId"), gameObject.getString("gameMode"), gameObject.getString("gameType"), gameObject.getString("subType"), gameObject.getInt("mapId"),
						gameObject.getInt("teamId"), players, stats);
				games.add(game);
			}
			return games;
		}
		catch(JsonException e)
		{
			//Shouldn't happen since the JSON is already parsed
			System.err.println("JSON parse error");
			e.printStackTrace();
			return null;
		}
	}
	
	//Private converter methods
	
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
				Player player = new Player(api, region, playerObject.getLong("summonerId"), playerObject.getInt("championId"), (int)playerObject.getInt("teamId"));
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
	private Map<String, Game.Stat> convertGameStats(JsonArray statsArray) throws JsonException
	{
		Map<String, Game.Stat> stats = new TreeMap<>();
		if(statsArray != null)
		{
			for(int p = 0; p < statsArray.size(); p++)
			{
				JsonObject statObject = statsArray.getObject(p);
				String name = statObject.getString("name");
				Game.Stat stat = new Game.Stat(statObject.getInt("id"), name, statObject.getInt("value"));
				stats.put(name, stat);
			}
		}
		return stats;
	}
}
