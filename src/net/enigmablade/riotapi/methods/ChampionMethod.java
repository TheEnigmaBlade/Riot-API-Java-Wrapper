package net.enigmablade.riotapi.methods;

import java.util.*;
import net.enigmablade.jsonic.*;
import net.enigmablade.riotapi.*;
import static net.enigmablade.riotapi.Region.*;
import net.enigmablade.riotapi.Requester.*;
import net.enigmablade.riotapi.types.*;

public class ChampionMethod extends Method
{
	public ChampionMethod(Requester requester)
	{
		super(requester, "api/lol", "champion", "1.1", new Region[]{NA, EUW, EUNE});
	}
	
	public List<Champion> getChampions(Region region, boolean free) throws RiotApiException
	{
		Map<String, String> args = createArgumentMap("freeToPlay", String.valueOf(free));
		Response response = getMethodResult(region, args);
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
						cObj.getLong("attackRank").intValue(), cObj.getLong("magicRank").intValue(), cObj.getLong("defenseRank").intValue(), cObj.getLong("difficultyRank").intValue(),
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
	
	public List<Champion> getAllChampions(Region region) throws RiotApiException
	{
		return getChampions(region, false);
	}
	
	public List<Champion> getFreeChampions(Region region) throws RiotApiException
	{
		return getChampions(region, true);
	}
}
