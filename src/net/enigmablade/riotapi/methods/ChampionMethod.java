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
 * <p>The champion method and its supporting operations.<p>
 * <p>Method support information:
 * 	<ul>
 * 		<li><i>Version</i>: 1.1</li>
 * 		<li><i>Regions</i>: NA, EUW, EUNE</li>
 * 	</ul>
 * </p>
 * <p>Operation information:
 * 	<ol>
 * 		<li>Get all champions</li>
 * 		<li>Get all free champions</i>
 * 	</ol>
 * </p>
 * @see <a href="http://developer.riotgames.com/api/methods#!/291">Developer site</a>
 * 
 * @author Enigma
 */
public class ChampionMethod extends Method
{
	/**
	 * Create a new champion method instance.
	 * @param api The API instance being used.
	 */
	public ChampionMethod(RiotApi api)
	{
		super(api, "api/lol", "champion", "1.1", new Region[]{NA, EUW, EUNE});
	}
	
	//API-defined operation methods
	
	/**
	 * Returns a list of all available or free champions and their accompanying information in the given region.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param free Whether or not to return the free champions only.
	 * @return A list of all available or free champions.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<Champion> getChampions(Region region, boolean free) throws RiotApiException
	{
		Response response = getMethodResult(region, createArgMap("freeToPlay", String.valueOf(free)));
		
		//Parse response
		try
		{
			//Convert JSON into Champion objects
			JsonObject csObj = (JsonObject)response.getValue();
			List<Champion> cs = new ArrayList<>(csObj.size());
			JsonArray csArray = csObj.getArray("champions");
			for(int n = 0; n < csArray.size(); n++)
			{
				JsonObject cObj = csArray.getObject(n);
				Champion c = new Champion(cObj.getString("name"), cObj.getLong("id"),
						cObj.getInt("attackRank"), cObj.getInt("magicRank"), cObj.getInt("defenseRank"), cObj.getInt("difficultyRank"),
						cObj.getBoolean("active"), cObj.getBoolean("freeToPlay"),
						cObj.getBoolean("botMmEnabled"), cObj.getBoolean("botEnabled"), cObj.getBoolean("rankedPlayEnabled"));
				cs.add(c);
			}
			return cs;
		}
		catch(JsonException e)
		{
			//Shouldn't happen since the JSON is already parsed
			System.err.println("JSON parse error");
			e.printStackTrace();
			return null;
		}
	}
	
	//Other methods
	
	/**
	 * Returns a list of all available champions and their accompanying information in the given region.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @return A list of all available champions.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<Champion> getAllChampions(Region region) throws RiotApiException
	{
		return getChampions(region, false);
	}
	
	/**
	 * Returns a list of all free champions (10 total) and their accompanying information in the given region.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @return A list of all available champions.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<Champion> getFreeChampions(Region region) throws RiotApiException
	{
		return getChampions(region, true);
	}
}
