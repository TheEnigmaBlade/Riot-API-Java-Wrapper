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
 * 		<li><i>Version</i>: 1.1</li>
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
		super(api, "api/lol", "game", "1.1", new Region[]{NA, EUW, EUNE});
	}
	
	//API-defined operation methods
	
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
			//Convert JSON into Champion objects
			JsonObject root = (JsonObject)response.getValue();
			
			//Check summoner IDs to make sure the server isn't crazy
			long rootSummonerId = root.getLong("summonerId");
			if(rootSummonerId != summonerId)
				throw new RiotApiException("Server returned invalid data: summoner ID mismatch");
			
			//Convert game list
			JsonArray gamesArray = root.getArray("games");
			List<Game> games = new ArrayList<>(gamesArray.size());
			for(int g = 0; g < gamesArray.size(); g++)
			{
				JsonObject gameObject = gamesArray.getObject(g);
				
				//Convert player list
				JsonArray playersArray = gameObject.getArray("fellowPlayers");
				List<Player> players = new ArrayList<>(playersArray.size());
				for(int p = 0; p < playersArray.size(); p++)
				{
					JsonObject playerObject = playersArray.getObject(p);
					Player player = new Player(playerObject.getLong("summonerId"), playerObject.getInt("championId"), (int)playerObject.getInt("teamId"));
					players.add(player);
				}
				
				//Convert statistic list
				JsonArray statsArray = gameObject.getArray("statistics");
				List<Game.Stat> stats = new ArrayList<>(statsArray.size());
				for(int p = 0; p < statsArray.size(); p++)
				{
					JsonObject statObject = statsArray.getObject(p);
					Game.Stat stat = new Game.Stat(statObject.getInt("id"), statObject.getString("name"), statObject.getInt("value"));
					stats.add(stat);
				}
				
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
}
