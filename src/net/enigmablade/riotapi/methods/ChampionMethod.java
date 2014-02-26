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
 * 		<li>Get all free champions</li>
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
		super(api, "api/lol", "champion", "1.1", new Region[]{NA, EUW, EUNE, BR, LAN, LAS, OCE});
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
		
		//Convert JSON into Champion objects
		JsonObject csObj = (JsonObject)response.getValue();
		List<Champion> cs = new ArrayList<>(csObj.size());
		JsonArray csArray = csObj.getArray("champions");
		for(int n = 0; n < csArray.size(); n++)
		{
			//Convert Champion object
			JsonObject cObj = csArray.getObject(n);
			Champion c = new Champion(api, region, cObj.getString("name"), cObj.getLong("id"),
					cObj.getInt("attackRank"), cObj.getInt("magicRank"), cObj.getInt("defenseRank"), cObj.getInt("difficultyRank"),
					cObj.getBoolean("active"), cObj.getBoolean("freeToPlay"),
					cObj.getBoolean("botMmEnabled"), cObj.getBoolean("botEnabled"), cObj.getBoolean("rankedPlayEnabled"));
			cs.add(c);
		}
		return cs;
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
	 * @return A list of all free champions.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<Champion> getFreeChampions(Region region) throws RiotApiException
	{
		return getChampions(region, true);
	}
	
	/**
	 * Returns a list of all disabled champions and their accompanying information in the given region.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @return A list of all disabled champions.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<Champion> getDisabledChampions(Region region) throws RiotApiException
	{
		List<Champion> all = getAllChampions(region);
		List<Champion> disabled = new ArrayList<>();
		for(Champion c : all)
			if(!c.isActive())
				disabled.add(c);
		return disabled;
	}
	
	//Other methods
	
	/**
	 * Fills missing information in a champion if required, i.e., if the champion name or champion ID is missing.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param champion The champion to fill.
	 * @return <code>true</code> if information the champion was filled, otherwise <code>false</code>.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public boolean fillChampion(Champion champion, Region region) throws RiotApiException
	{
		List<Champion> champions = getAllChampions(region);
		Champion newChampion = null;
		for(Champion c : champions)
			if((champion.getId() >= 0 && c.getId() == champion.getId()) || c.getName().equals(champion.getName()))
			{
				newChampion = c;
				break;
			}
		
		//Fill if required
		if(newChampion != null)
		{
			champion.setName(newChampion.getName());
			champion.setId(newChampion.getId());
			champion.setAttackRank(newChampion.getAttackRank());
			champion.setMagicRank(newChampion.getMagicRank());
			champion.setDefenseRank(newChampion.getDefenseRank());
			champion.setDifficultyRank(newChampion.getDifficultyRank());
			champion.setActive(newChampion.isActive());
			champion.setFreeToPlay(newChampion.isFreeToPlay());
			champion.setBotMatchMadeEnabled(newChampion.isBotMatchMadeEnabled());
			champion.setBotCustomEnabled(newChampion.isBotCustomEnabled());
			champion.setRankedEnabled(newChampion.isRankedEnabled());
			return true;
		}
		return false;
	}
}
