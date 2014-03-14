package net.enigmablade.riotapi.methods;

import java.util.*;
import net.enigmablade.jsonic.*;
import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.Requester.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.constants.Locale;
import net.enigmablade.riotapi.exceptions.*;
import net.enigmablade.riotapi.types.*;
import net.enigmablade.riotapi.types.staticdata.*;
import net.enigmablade.riotapi.util.*;
import static net.enigmablade.riotapi.constants.Region.*;

/**
 * <p>The static data method and its supporting operations.<p>
 * <p>Method support information:
 * 	<ul>
 * 		<li><i>Version</i>: 1.0</li>
 * 		<li><i>Regions</i>: NA, EUW, EUNE, LAN, LAS, OCE, BR, TR, RU, KR</li>
 * 	</ul>
 * </p>
 * <p>Operation information:
 * 	<ol>
 * 		<li></li>
 * 	</ol>
 * </p>
 * @see <a href="https://developer.riotgames.com/api/methods#!/378">Developer site</a>
 * 
 * @author Enigma
 */
public class StaticDataMethod extends Method
{
	/**
	 * Create a new champion method instance.
	 * @param api The API instance being used.
	 */
	public StaticDataMethod(RiotApi api)
	{
		super(api, "api/lol/static-data", null, "1", new Region[]{NA, EUW, EUNE, LAN, LAS, OCE, BR, TR, RU, KR});
	}
	
	//API-defined operation methods
	
	public Map<String, Champion> getChampions(Region region) throws RiotApiException
	{
		return getChampions(region, null);
	}
	
	/**
	 * Returns basic static data for all champions.
	 * @param region The region (<i>required</i>).
	 * @param locale The language locale (<i>required</i>).
	 * @return A map of champion IDs to champions.
	 * @throws RegionNotSupportedException If the region is not supported.
	 * @throws IllegalArgumentException If any required arguments are invalid.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Map<String, Champion> getChampions(Region region, Locale locale) throws RiotApiException
	{
		return getChampions(region, locale, null);
	}
	
	/**
	 * Returns the specified static data for all champions.
	 * @param region The region (<i>required</i>).
	 * @param locale The language locale (<i>required</i>).
	 * @param championData The type of data to return. Defaults to basic data if <code>null</code>.
	 * @return A map of champion IDs to champions.
	 * @throws RegionNotSupportedException If the region is not supported.
	 * @throws IllegalArgumentException If any required arguments are invalid.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Map<String, Champion> getChampions(Region region, Locale locale, ChampionDataType championData) throws RiotApiException
	{
		//Create argument maps
		Map<String, String> queryArgs = createArgMap();
		if(locale != null)
			queryArgs.put("locale", locale.getValue());
		if(championData != null && championData != ChampionDataType.BASIC)
			queryArgs.put("champData", championData.toString().toLowerCase());
		
		//Send request
		Response response = staticGetMethodResult(region,
				"champion",
				null, queryArgs);
		
		//Parse response
		return convertChampionList((JsonObject)response.getValue(), championData, region, locale);
	}
	
	/**
	 * Returns basic static data for the champion with the given ID.
	 * @param region The region (<i>required</i>).
	 * @param championId The champion ID.
	 * @return The champion.
	 * @throws RegionNotSupportedException If the region is not supported.
	 * @throws IllegalArgumentException If any required arguments are invalid.
	 * @throws StaticDataNotFoundException If no champion was found with the given champion ID.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Champion getChampion(Region region, long championId) throws RiotApiException
	{
		return getChampion(region, null, championId);
	}
	
	/**
	 * Returns the specified static data for the champion with the given ID.
	 * @param region The region (<i>required</i>).
	 * @param championId The champion ID.
	 * @param championData The type of data to return. Defaults to basic data if <code>null</code>.
	 * @return The champion.
	 * @throws RegionNotSupportedException If the region is not supported.
	 * @throws IllegalArgumentException If any required arguments are invalid.
	 * @throws StaticDataNotFoundException If no champion was found with the given champion ID.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Champion getChampion(Region region, long championId, ChampionDataType championData) throws RiotApiException
	{
		return getChampion(region, null, championId, championData);
	}
	
	/**
	 * Returns basic static data for the champion with the given ID.
	 * @param region The region (<i>required</i>).
	 * @param locale The language locale (<i>required</i>).
	 * @param championId The champion ID.
	 * @return The champion.
	 * @throws RegionNotSupportedException If the region is not supported.
	 * @throws IllegalArgumentException If any required arguments are invalid.
	 * @throws StaticDataNotFoundException If no champion was found with the given champion ID.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Champion getChampion(Region region, Locale locale, long championId) throws RiotApiException
	{
		return getChampion(region, locale, championId, null);
	}
	
	/**
	 * Returns the specified static data for the champion with the given ID.
	 * @param region The region (<i>required</i>).
	 * @param locale The language locale (<i>required</i>).
	 * @param championId The champion ID.
	 * @param championData The type of data to return. Defaults to basic data if <code>null</code>.
	 * @return The champion.
	 * @throws RegionNotSupportedException If the region is not supported.
	 * @throws IllegalArgumentException If any required arguments are invalid.
	 * @throws StaticDataNotFoundException If no champion was found with the given champion ID.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Champion getChampion(Region region, Locale locale, long championId, ChampionDataType championData) throws RiotApiException
	{
		//Create argument maps
		Map<String, String> pathArgs = createArgMap("id", String.valueOf(championId));
		
		Map<String, String> queryArgs = createArgMap();
		if(locale == null)
			locale = api.getLocale();
		queryArgs.put("locale", locale.getValue());
		if(championData != null && championData != ChampionDataType.BASIC)
			queryArgs.put("champData", championData.toString().toLowerCase());
		
		//Send request
		Response response = staticGetMethodResult(region,
				"champion/{id}",
				pathArgs, queryArgs);
		
		//Check errors
		if(response.getCode() == 404)
			throw new StaticDataNotFoundException(region, String.valueOf(championId));
		
		//Parse response
		return convertChampion((JsonObject)response.getValue(), championData, region, locale);
	}
	
	//Converter methods
	
	////Champion
	
	private Map<String, Champion> convertChampionList(JsonObject rootObject, ChampionDataType loadedData, Region region, Locale locale)
	{
		//Convert the list of champions
		JsonObject championsObject = rootObject.getObject("data");
		Map<String, Champion> champions = new HashMap<>(championsObject.size());
		for(String championKey : championsObject.keySet())
		{
			//Convert a champion
			JsonObject championObject = championsObject.getObject(championKey);
			Champion champion = convertChampion(championObject, loadedData, region, locale);
			champions.put(championKey, champion);
		}
		return champions;
	}
	
	private Champion convertChampion(JsonObject championObject, ChampionDataType loadedData, Region region, Locale locale)
	{
		String name = championObject.getString("name");
		long id = Long.parseLong(championObject.getString("key"));
		String key = championObject.getString("id");
		String title = championObject.getString("title");
		
		Champion c = new Champion(api, region, locale, name, id, key, title);
		
		if(loadedData != null)
		{
			boolean notAll = loadedData != ChampionDataType.ALL;
			
			switch(loadedData)
			{
				//Basic data
				case PARTYPE:
					String partype = championObject.getString("partype");
					ResourceType resourceType = ResourceType.getResourceType(partype);
					c.setResourceType(resourceType);
					if(notAll) break;
				
				case BLURB:
					String blurb = championObject.getString("blurb");
					c.setBlurb(blurb);
					if(notAll) break;
				
				case LORE:
					String lore = championObject.getString("lore");
					c.setLore(lore);
					if(notAll) break;
				
				//Lists
				case ALLYTIPS:
					JsonArray allyTipsArray = championObject.getArray("allytips");
					List<String> allyTips = convertChampionTips(allyTipsArray);
					c.setAllyTips(allyTips);
					if(notAll) break;
				
				case ENEMYTIPS:
					JsonArray enemyTipsArray = championObject.getArray("enemytips");
					List<String> enemyTips = convertChampionTips(enemyTipsArray);
					c.setEnemyTips(enemyTips);
					if(notAll) break;
				
				case RECOMMENDED:
					JsonArray recommendedArray = championObject.getArray("recommended");
					List<RecommendedItems> recommended = convertChampionRecommended(recommendedArray);
					c.setRecommendedItems(recommended);
					if(notAll) break;
				
				case SKINS:
					JsonArray skinsArray = championObject.getArray("skins");
					List<Skin> skins = convertChampionSkins(skinsArray);
					c.setSkins(skins);
					if(notAll) break;
				
				case SPELLS:
					JsonArray spellsArray = championObject.getArray("spells");
					List<Spell> spells = convertSpells(spellsArray);
					c.setSpells(spells);
					if(notAll) break;
				
				case TAGS:
					JsonArray tagsArray = championObject.getArray("tags");
					List<String> tags = convertChampionTags(tagsArray);
					c.setTags(tags);
					if(notAll) break;
				
				//Objects
				case IMAGE:
					JsonObject imageObject = championObject.getObject("image");
					Image image = convertImage(imageObject);
					c.setImage(image);
					if(notAll) break;
				
				case PASSIVE:
					JsonObject passiveObject = championObject.getObject("passive");
					Passive passive = convertChampionPassive(passiveObject);
					c.setPassive(passive);
					if(notAll) break;
				
				case STATS:
					JsonObject statsObject = championObject.getObject("stats");
					Champion.Stats stats = convertChampionStats(statsObject);
					c.setStats(stats);
					if(notAll) break;
			}
		}
		
		return c;
	}
	
	private List<String> convertChampionTips(JsonArray tipsArray)
	{
		List<String> tips = new ArrayList<>(tipsArray.size());
		for(JsonIterator it = tipsArray.iterator(); it.hasNext();)
			tips.add(it.nextString());
		return tips;
	}
	
	private List<String> convertChampionTags(JsonArray tagsArray)
	{
		List<String> tags = new ArrayList<>(tagsArray.size());
		for(JsonIterator it = tagsArray.iterator(); it.hasNext();)
			tags.add(it.nextString());
		return tags;
	}
	
	private List<RecommendedItems> convertChampionRecommended(JsonArray recommendedArray)
	{
		List<RecommendedItems> recommendedItems = new ArrayList<>(recommendedArray.size());
		for(JsonIterator it = recommendedArray.iterator(); it.hasNext();)
		{
			JsonObject recommendedObject = it.nextObject();
			
			//Convert blocks
			JsonArray blocksArray = recommendedObject.getArray("blocks");
			List<RecommendedItems.Block> blocks = new ArrayList<>(blocksArray.size());
			for(JsonIterator it2 = blocksArray.iterator(); it2.hasNext();)
			{
				JsonObject blockObject = it2.nextObject();
				
				//Convert items
				JsonArray itemsArray = blockObject.getArray("items");
				if(itemsArray != null)
				{
					List<RecommendedItems.Block.Item> items = new ArrayList<>(itemsArray.size());
					for(JsonIterator it3 = itemsArray.iterator(); it3.hasNext();)
					{
						JsonObject itemObject = it3.nextObject();
						
						//Convert item
						RecommendedItems.Block.Item item = new RecommendedItems.Block.Item(itemObject.getString("id"), itemObject.getInt("count"));
						items.add(item);
					}
					
					//Convert block
					RecommendedItems.Block block = new RecommendedItems.Block(blockObject.getString("type"), items);
					blocks.add(block);
				}
			}
			
			//Convert recommended set
			RecommendedItems recommended = new RecommendedItems(recommendedObject.getString("champion"),
					recommendedObject.getString("map"), recommendedObject.getString("mode"), recommendedObject.getString("type"),
					recommendedObject.getString("title"), recommendedObject.getBoolean("priority"),
					blocks);
			recommendedItems.add(recommended);
		}
		return recommendedItems;
	}
	
	private List<Skin> convertChampionSkins(JsonArray skinsArray)
	{
		List<Skin> skins = new ArrayList<>(skinsArray.size());
		for(JsonIterator it = skinsArray.iterator(); it.hasNext();)
		{
			JsonObject skinObject = it.nextObject();
			
			//Convert skin
			Skin skin = new Skin(
					skinObject.getString("id"),
					skinObject.getString("name"),
					skinObject.getInt("num"));
			skins.add(skin);
		}
		return skins;
	}
	
	private Champion.Stats convertChampionStats(JsonObject statsObject)
	{
		Champion.Stats stats = new Champion.Stats(
				statsObject.getDouble("attackdamage"), statsObject.getDouble("attackdamageperlevel"), statsObject.getDouble("attackrange"),
				statsObject.getDouble("attackspeedoffset"), statsObject.getDouble("attackspeedperlevel"), statsObject.getDouble("crit"), statsObject.getDouble("critperlevel"),
				statsObject.getDouble("hp"), statsObject.getDouble("hpperlevel"), statsObject.getDouble("hpregen"), statsObject.getDouble("hpregenperlevel"),
				statsObject.getDouble("mp"), statsObject.getDouble("mpperlevel"), statsObject.getDouble("mpregen"), statsObject.getDouble("mpregenperlevel"),
				statsObject.getDouble("armor"), statsObject.getDouble("armorperlevel"), statsObject.getDouble("spellblock"), statsObject.getDouble("spellblockperlevel"),
				statsObject.getDouble("movespeed"));
		return stats;
	}
	
	private Passive convertChampionPassive(JsonObject passiveObject)
	{
		Image image = convertImage(passiveObject.getObject("image"));
		Passive passive = new Passive(passiveObject.getString("name"), passiveObject.getString("description"), image);
		return passive;
	}
	
	private List<Spell> convertSpells(JsonArray spellArray)
	{
		List<Spell> spells = new ArrayList<>(spellArray.size());
		for(JsonIterator it = spellArray.iterator(); it.hasNext();)
		{
			JsonObject spellObject = it.nextObject();
			
			//Convert spell
			Image image = convertImage(spellObject.getObject("image"));
			Spell spell = new Spell(spellObject.getString("name"), spellObject.getString("id"), spellObject.getString("description"), image);
			spells.add(spell);
			
			//Convert general
			JsonObject levelTipObject = spellObject.getObject("leveltip");
			List<String> levelTipLabels = ApiUtil.convertStringArray(levelTipObject.getArray("label"));
			List<String> levelTipEffects = ApiUtil.convertStringArray(levelTipObject.getArray("effect"));
			
			JsonArray varsArray = spellObject.getArray("vars");
			List<Spell.SpellVar> vars = convertSpellVars(varsArray, spell);
			
			spell.initGeneral(spellObject.getString("tooltip"), levelTipLabels, levelTipEffects, spellObject.getInt("maxrank"), vars);
			
			//Convert resource
			ResourceType resourceType = ResourceType.getResourceType(spellObject.getString("costType"));
			List<Integer> costs = ApiUtil.convertIntArray(spellObject.getArray("cost"));
			
			spell.initResource(resourceType, spellObject.getString("resource"), costs, spellObject.getString("costBurn"));
			
			//Convert info
			List<Integer> cooldowns = ApiUtil.convertIntArray(spellObject.getArray("cooldown"));
			
			Object rangeObj = spellObject.get("range");
			List<Integer> ranges = null;
			if(ValueUtil.isJsonArray(rangeObj))	//Leave the range null if not an array, means it's "self"
				ranges = ApiUtil.convertIntArray(spellObject.getArray("range"));
			
			spell.initInfo(cooldowns, spellObject.getString("cooldownBurn"), ranges, spellObject.getString("rangeBurn"));
			
			//Convert effects
			JsonArray effectsArray = spellObject.getArray("effect");
			List<List<Integer>> effects = new ArrayList<List<Integer>>();
			if(effectsArray != null)
			{
				for(JsonIterator it2 = effectsArray.iterator(); it2.hasNext();)
					effects.add(ApiUtil.convertIntArray(it2.nextArray()));
			}
			
			List<String> effectBurns = ApiUtil.convertStringArray(spellObject.getArray("effectBurn"));
			
			spell.initEffects(effects, effectBurns);
		}
		return spells;
	}
	
	private List<Spell.SpellVar> convertSpellVars(JsonArray spellVarsArray, Spell spell)
	{
		if(spellVarsArray == null)
			return new ArrayList<>(0);
		
		List<Spell.SpellVar> vars = new ArrayList<>(spellVarsArray.size());
		for(JsonIterator it = spellVarsArray.iterator(); it.hasNext();)
		{
			JsonObject varObject = it.nextObject();
			String key = varObject.getString("key");
			String link = varObject.getString("link");
			boolean dynamic = "+".equals(varObject.getString("dyn"));
			
			Spell.SpellVar var;
			/*if(dynamic)
				var = spell.new DynamicSpellVar(key, link, convertFloatArray(varObject.getArray("coeff")));
			else
				var = spell.new StaticSpellVar(key, link, varObject.getFloat("coeff"));*/
			Object coeff = varObject.get("coeff");
			if(coeff instanceof JsonArray)
				coeff = ApiUtil.convertArray((JsonArray)coeff);
			var = spell.new SpellVar(key, link, coeff, dynamic);
			vars.add(var);
		}
		return vars;
	}
	
	//Common converter methods
	
	private Image convertImage(JsonObject imageObject)
	{
		Image image = new Image(
				imageObject.getString("full"),
				imageObject.getString("group"),
				imageObject.getString("sprite"),
				imageObject.getInt("x"), imageObject.getInt("y"),
				imageObject.getInt("w"), imageObject.getInt("h"));
		return image;
	}
	
	//Other methods
	
	public void fillChampion(Champion champion, Region region) throws RiotApiException
	{
		fillChampion(champion, region, null);
	}
	
	public void fillChampion(Champion champion, Region region, ChampionDataType dataType) throws RiotApiException
	{
		Champion newChampion = getChampion(region, champion.getLocale(), champion.getId(), dataType);
		
		//Data in every response
		champion.setName(newChampion.getName());
		champion.setId(newChampion.getId());
		champion.setKey(newChampion.getKey());
		champion.setTitle(newChampion.getTitle());
		
		//Specific data
		boolean notAll = dataType != ChampionDataType.ALL;
		switch(dataType)
		{
			//Basic data
			case PARTYPE:
				champion.setResourceType(newChampion.getResourceType());
				if(notAll) break;
			
			case BLURB:
				champion.setBlurb(newChampion.getBlurb());
				if(notAll) break;
			
			case LORE:
				champion.setLore(newChampion.getLore());
				if(notAll) break;
			
			//Lists
			case ALLYTIPS:
				champion.setAllyTips(newChampion.getAllyTips());
				if(notAll) break;
			
			case ENEMYTIPS:
				champion.setEnemyTips(newChampion.getEnemyTips());
				if(notAll) break;
			
			case RECOMMENDED:
				champion.setRecommendedItems(newChampion.getRecommendedItems());
				if(notAll) break;
			
			case SKINS:
				champion.setSkins(newChampion.getSkins());
				if(notAll) break;
			
			case SPELLS:
				champion.setSpells(newChampion.getSpells());
				if(notAll) break;
			
			case TAGS:
				champion.setTags(newChampion.getTags());
				if(notAll) break;
			
			//Objects
			case IMAGE:
				champion.setImage(newChampion.getImage());
				if(notAll) break;
			
			case PASSIVE:
				champion.setPassive(newChampion.getPassive());
				if(notAll) break;
			
			case STATS:
				champion.setStats(newChampion.getStats());
				if(notAll) break;
		}
	}
	
	//Helper methods
	
	private Response staticGetMethodResult(Region region, String operation, Map<String, String> pathArgs, Map<String, String> queryArgs) throws RiotApiException
	{
		boolean save = api.isRateLimitEnabled();
		api.setRateLimitEnabled(false);
		Response r = getMethodResult(region, operation, pathArgs, queryArgs);
		api.setRateLimitEnabled(save);
		return r;
	}
}
