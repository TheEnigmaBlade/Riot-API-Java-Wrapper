package net.enigmablade.riotapi.methods;

import static net.enigmablade.riotapi.constants.Region.*;
import java.util.*;
import net.enigmablade.jsonic.*;
import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.Requester.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.constants.Locale;
import net.enigmablade.riotapi.exceptions.*;
import net.enigmablade.riotapi.types.lolstatus.*;
import net.enigmablade.riotapi.util.*;

public class LolStatusMethod extends UnlimitedMethod
{
	/**
	 * Create a new LoL status method instance.
	 * @param api The API instance being used.
	 */
	public LolStatusMethod(RiotApi api)
	{
		super(api, "status.leagueoflegends.com", true, "shards", null, null, new Region[]{NA, EUW, EUNE, LAN, LAS, OCE, BR, TR, RU, KR});
	}
	
	public List<Shard> getShards() throws RiotApiException
	{
		Response response = getMethodResult();
		return convertShards((JsonArray)response.getValue());
	}
	
	public Shard getShard(Region region) throws RiotApiException
	{
		Response response = getMethodResult(null,
				"{region}",
				true,
				createArgMap("region", region.getValue()), null);
		
		//Parse response
		JsonObject root = (JsonObject)response.getValue();
		return convertLongShard(root);
	}
	
	//Other methods
	
	public void fillShard(Shard shard) throws RiotApiException
	{
		Shard newShard = getShard(shard.getRegion());
		shard.setServices(newShard.getServices());
	}
	
	//Private conversion methods
	
	private List<Shard> convertShards(JsonArray shardsArray)
	{
		return ParseUtil.arrayToList(shardsArray, this::convertSimpleShard);
	}
	
	private Shard convertSimpleShard(JsonObject shardObj)
	{
		JsonArray localesArray = shardObj.getArray("locales");
		List<Locale> locales = new ArrayList<>(localesArray.size());
		for(JsonIterator it = localesArray.iterator(); it.hasNext();)
			locales.add(Locale.getFromValue(it.nextString()));
		
		return new Shard(api, shardObj.getString("name"), shardObj.getString("hostname"), shardObj.getString("region_tag"), locales, shardObj.getString("slug"));
	}
	
	private Shard convertLongShard(JsonObject shardObj)
	{
		Shard shard = convertSimpleShard(shardObj);
		shard.setServices(convertServices(shardObj.getArray("services")));
		return shard;
	}
	
	private List<Service> convertServices(JsonArray servicesArray)
	{
		return ParseUtil.arrayToList(servicesArray, serviceObj -> {
			return new Service(
					serviceObj.getString("name"),
					serviceObj.getString("slug"),
					serviceObj.getString("status"),
					convertIncidents(serviceObj.getArray("incidents")));
		});
	}
	
	private List<Incident> convertIncidents(JsonArray incidentsArray)
	{
		return ParseUtil.arrayToList(incidentsArray, incidentObj -> {
			return new Incident(
					incidentObj.getLong("id"),
					incidentObj.getString("created_at"),
					incidentObj.getBoolean("active"),
					convertIncidentMessages(incidentObj.getArray("updates")));
		});
	}
	
	private List<Incident.Message> convertIncidentMessages(JsonArray messagesArray)
	{
		return ParseUtil.arrayToList(messagesArray, messageObj -> {
			return new Incident.Message(
					messageObj.getLong("id"),
					messageObj.getString("severity"),
					messageObj.getString("author"),
					messageObj.getString("content"),
					convertTranslations(messageObj.getArray("translations")),
					messageObj.getString("created_at"),
					messageObj.getString("updated_at"));
		});
	}
	
	private Map<Locale, String> convertTranslations(JsonArray translationsArray)
	{
		Map<Locale, String> translations = new HashMap<>(translationsArray.size());
		for(JsonIterator it = translationsArray.iterator(); it.hasNext();)
		{
			JsonObject translationObj = it.nextObject();
			Locale locale = Locale.getFromValue(translationObj.getString("locale"));
			String content = translationObj.getString("content");
			translations.put(locale, content);
		}
		return translations;
	}
}
