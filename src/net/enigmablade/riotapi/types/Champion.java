package net.enigmablade.riotapi.types;

import java.util.*;
import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.constants.Locale;
import net.enigmablade.riotapi.exceptions.*;
import net.enigmablade.riotapi.types.staticdata.*;

/**
 * Information about a League of Legends champion.
 * 
 * @author Enigma
 */
public class Champion extends DynamicType
{
	public static class Stats
	{
		public double armor, armorPerLevel;
		public double attackDamage, attackDamagePerLevel, attackRange;
		public double attackSpeedOffset, attackSpeedPerLevel;
		public double crit, critPerLevel;
		public double hp, hpPerLevel, hpRegen, hpRegenPerLevel;
		public double movespeed;
		public double mp, mpPerLevel, mpRegen, mpRegenPerLevel;
		public double magicResist, magicResistPerLevel;
		
		public Stats(double attackDamage, double attackDamagePerLevel, double attackRange,
			double attackSpeedOffset, double attackSpeedPerLevel, double crit, double critPerLevel,
			double hp, double hpPerLevel, double hpRegen, double hpRegenPerLevel,
			double mp, double mpPerLevel, double mpRegen, double mpRegenPerLevel,
			double armor, double armorPerLevel, double magicResist, double magicResistPerLevel, double movespeed)
		{
			this.armor = armor;
			this.armorPerLevel = armorPerLevel;
			this.attackDamage = attackDamage;
			this.attackDamagePerLevel = attackDamagePerLevel;
			this.attackRange = attackRange;
			this.attackSpeedOffset = attackSpeedOffset;
			this.attackSpeedPerLevel = attackSpeedPerLevel;
			this.crit = crit;
			this.critPerLevel = critPerLevel;
			this.hp = hp;
			this.hpPerLevel = hpPerLevel;
			this.hpRegen = hpRegen;
			this.hpRegenPerLevel = hpRegenPerLevel;
			this.movespeed = movespeed;
			this.mp = mp;
			this.mpPerLevel = mpPerLevel;
			this.mpRegen = mpRegen;
			this.mpRegenPerLevel = mpRegenPerLevel;
			this.magicResist = magicResist;
			this.magicResistPerLevel = magicResistPerLevel;
		}
	}
	
	private Region region;
	private Locale locale;
	
	//Identification data
	private String name;
	private long id;
	
	//Dynamic data
	private boolean active, freeToPlay;
	private boolean botMatchMadeEnabled, botCustomEnabled, rankedEnabled;
	private int attackRank, magicRank, defenseRank, difficultyRank;
	
	//Static data
	private String key;
	private String title;
	private ResourceType resourceType;
	private String blurb, lore;
	
	private List<String> allyTips, enemyTips;
	private List<String> tags;
	private Image image;
	private List<Skin> skins;
	private List<RecommendedItems> recommended;
	private Passive passive;
	private List<Spell> spells;
	private Stats stats;
	
	//Constructors
	
	////Dynamic construction
	
	public Champion(RiotApi api, Region region, String name, long id)
	{
		this(api, region, name, id, -1, -1, -1, -1, false, false, false, false, false);
	}
	
	public Champion(RiotApi api, Region region, String name, long id, int attackRank, int magicRank, int defenseRank, int difficultyRank, boolean active, boolean freeToPlay, boolean botMatchMadeEnabled, boolean botCustomEnabled, boolean rankedEnabled)
	{
		super(api, 2);
		
		this.region = region;
		this.locale = api.getLocale();
		
		this.name = name;
		this.id = id;
		this.attackRank = attackRank;
		this.magicRank = magicRank;
		this.defenseRank = defenseRank;
		this.difficultyRank = difficultyRank;
		this.active = active;
		this.freeToPlay = freeToPlay;
		this.botMatchMadeEnabled = botMatchMadeEnabled;
		this.botCustomEnabled = botCustomEnabled;
		this.rankedEnabled = rankedEnabled;
		
		if(attackRank >= 0 && magicRank >= 0 && defenseRank >= 0 && difficultyRank >= 0)
			setTypeUpdated();
		
		key = null;
		title = null;
		resourceType = null;
		blurb = null;
		lore = null;
		allyTips = null;
		enemyTips = null;
		tags = null;
		image = null;
		skins = null;
		recommended = null;
		passive = null;
		spells = null;
		stats = null;
	}
	
	////Static construction
	
	public Champion(RiotApi api, Region region, Locale locale, String name, long id, String key, String title)
	{
		this(api, region, name, id);
		
		this.key = key;
		this.title = title;
		this.locale = locale;
	}
	
	//State methods
	
	/**
	 * Verifies required information for dynamic information methods is available, such as champion ID.
	 * If not found, makes an API call to get dynamic champion information.
	 * @throws RiotApiException If there was an exception or an error from the server.
	 */
	private void verifyDynamicState() throws RiotApiException
	{
		if(!hasTypeUpdated(0))
		{
			setTypeUpdated(0);
			api.getChampionMethod().fillChampion(this, region);
		}
	}
	
	/**
	 * Verifies required information for static information methods is available, such as champion ID.
	 * If not found, makes an API call to get dynamic champion information.
	 * @throws RiotApiException If there was an exception or an error from the server.
	 */
	private void verifyStaticState() throws RiotApiException
	{
		if(!hasTypeUpdated(1))
		{
			setTypeUpdated(1);
			api.getStaticDataMethod().fillChampion(this, region);
		}
	}
	
	private void verifyStaticState(ChampionDataType type) throws RiotApiException
	{
		api.getStaticDataMethod().fillChampion(this, region, type);
	}
	
	//Accessor methods
	
	public Region getRegion()
	{
		return region;
	}
	
	public Locale getLocale()
	{
		return locale;
	}
	
	public String getName() throws RiotApiException
	{
		if(name == null)
			verifyDynamicState();
		return name;
	}
	
	public long getId() throws RiotApiException
	{
		if(id < 0)
			verifyDynamicState();
		return id;
	}
	
	////Dynamic accessor methods
	
	public int getAttackRank() throws RiotApiException
	{
		verifyDynamicState();
		return attackRank;
	}
	
	public int getMagicRank() throws RiotApiException
	{
		verifyDynamicState();
		return magicRank;
	}
	
	public int getDefenseRank() throws RiotApiException
	{
		verifyDynamicState();
		return defenseRank;
	}
	
	public int getDifficultyRank() throws RiotApiException
	{
		verifyDynamicState();
		return difficultyRank;
	}
	
	public boolean isActive() throws RiotApiException
	{
		verifyDynamicState();
		return active;
	}
	
	public boolean isFreeToPlay() throws RiotApiException
	{
		verifyDynamicState();
		return freeToPlay;
	}
	
	public boolean isBotMatchMadeEnabled() throws RiotApiException
	{
		verifyDynamicState();
		return botMatchMadeEnabled;
	}
	
	public boolean isBotCustomEnabled() throws RiotApiException
	{
		verifyDynamicState();
		return botCustomEnabled;
	}
	
	public boolean isRankedEnabled() throws RiotApiException
	{
		verifyDynamicState();
		return rankedEnabled;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public void setAttackRank(int attackRank)
	{
		this.attackRank = attackRank;
	}

	public void setMagicRank(int magicRank)
	{
		this.magicRank = magicRank;
	}

	public void setDefenseRank(int defenseRank)
	{
		this.defenseRank = defenseRank;
	}

	public void setDifficultyRank(int difficultyRank)
	{
		this.difficultyRank = difficultyRank;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public void setFreeToPlay(boolean freeToPlay)
	{
		this.freeToPlay = freeToPlay;
	}

	public void setBotMatchMadeEnabled(boolean botMatchMadeEnabled)
	{
		this.botMatchMadeEnabled = botMatchMadeEnabled;
	}

	public void setBotCustomEnabled(boolean botCustomEnabled)
	{
		this.botCustomEnabled = botCustomEnabled;
	}

	public void setRankedEnabled(boolean rankedEnabled)
	{
		this.rankedEnabled = rankedEnabled;
	}
	
	////Static accessor methods
	
	public String getKey() throws RiotApiException
	{
		if(key == null)
			verifyStaticState();
		return key;
	}
	
	public String getTitle() throws RiotApiException
	{
		if(key == null)
			verifyStaticState();
		return title;
	}
	
	public ResourceType getResourceType() throws RiotApiException
	{
		if(resourceType == null)
			verifyStaticState(ChampionDataType.PARTYPE);
		return resourceType;
	}
	
	public String getBlurb() throws RiotApiException
	{
		if(blurb == null)
			verifyStaticState(ChampionDataType.BLURB);
		return blurb;
	}
	
	public String getLore() throws RiotApiException
	{
		if(lore == null)
			verifyStaticState(ChampionDataType.LORE);
		return lore;
	}
	
	public List<String> getAllyTips() throws RiotApiException
	{
		if(allyTips == null)
			verifyStaticState(ChampionDataType.ALLYTIPS);
		return allyTips;
	}
	
	public List<String> getEnemyTips() throws RiotApiException
	{
		if(enemyTips == null)
			verifyStaticState(ChampionDataType.ENEMYTIPS);
		return enemyTips;
	}
	
	public List<String> getTags() throws RiotApiException
	{
		if(tags == null)
			verifyStaticState(ChampionDataType.TAGS);
		return tags;
	}
	
	public Image getImage() throws RiotApiException
	{
		if(image == null)
			verifyStaticState(ChampionDataType.IMAGE);
		return image;
	}
	
	public List<Skin> getSkins() throws RiotApiException
	{
		if(skins == null)
			verifyStaticState(ChampionDataType.SKINS);
		return skins;
	}
	
	public List<RecommendedItems> getRecommendedItems() throws RiotApiException
	{
		if(recommended == null)
			verifyStaticState(ChampionDataType.RECOMMENDED);
		return recommended;
	}
	
	public Passive getPassive() throws RiotApiException
	{
		if(passive == null)
			verifyStaticState(ChampionDataType.PASSIVE);
		return passive;
	}
	
	public List<Spell> getSpells() throws RiotApiException
	{
		if(spells == null)
			verifyStaticState(ChampionDataType.SPELLS);
		return spells;
	}
	
	public Stats getStats() throws RiotApiException
	{
		if(stats == null)
			verifyStaticState(ChampionDataType.STATS);
		return stats;
	}
	
	public double getArmor() throws RiotApiException
	{
		return getStats().armor;
	}
	
	public double getArmorPerLevel() throws RiotApiException
	{
		return getStats().armorPerLevel;
	}
	
	public double getAttackDamage() throws RiotApiException
	{
		return getStats().attackDamage;
	}
	
	public double getAttackDamagePerLevel() throws RiotApiException
	{
		return getStats().attackDamagePerLevel;
	}
	
	public double getAttackRange() throws RiotApiException
	{
		return getStats().attackRange;
	}
	
	public double getAttackSpeedOffset() throws RiotApiException
	{
		return getStats().attackSpeedOffset;
	}
	
	public double getAttackSpeedPerLevel() throws RiotApiException
	{
		return getStats().attackSpeedPerLevel;
	}
	
	public double getCrit() throws RiotApiException
	{
		return getStats().crit;
	}
	
	public double getCritPerLevel() throws RiotApiException
	{
		return getStats().critPerLevel;
	}
	
	public double getHp() throws RiotApiException
	{
		return getStats().hp;
	}
	
	public double getHpPerLevel() throws RiotApiException
	{
		return getStats().hpPerLevel;
	}
	
	public double getHpRegen() throws RiotApiException
	{
		return getStats().hpRegen;
	}
	
	public double getHpRegenPerLevel() throws RiotApiException
	{
		return getStats().hpRegenPerLevel;
	}
	
	public double getMovespeed() throws RiotApiException
	{
		return getStats().movespeed;
	}
	
	public double getMana() throws RiotApiException
	{
		return getStats().mp;
	}
	
	public double getManaPerLevel() throws RiotApiException
	{
		return getStats().mpPerLevel;
	}
	
	public double getManaRegen() throws RiotApiException
	{
		return getStats().mpRegen;
	}
	
	public double getManaRegenPerLevel() throws RiotApiException
	{
		return getStats().mpRegenPerLevel;
	}
	
	public double getMagicResist() throws RiotApiException
	{
		return getStats().magicResist;
	}
	
	public double getMagicResistPerLevel() throws RiotApiException
	{
		return getStats().magicResistPerLevel;
	}
	
	public void setKey(String key)
	{
		this.key = key;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void setResourceType(ResourceType resourceType)
	{
		this.resourceType = resourceType;
	}
	
	public void setBlurb(String blurb)
	{
		this.blurb = blurb;
	}
	
	public void setLore(String lore)
	{
		this.lore = lore;
	}
	
	public void setAllyTips(List<String> allyTips)
	{
		this.allyTips = allyTips;
	}
	
	public void setEnemyTips(List<String> enemyTips)
	{
		this.enemyTips = enemyTips;
	}
	
	public void setTags(List<String> tags)
	{
		this.tags = tags;
	}
	
	public void setImage(Image image)
	{
		this.image = image;
	}
	
	public void setSkins(List<Skin> skins)
	{
		this.skins = skins;
	}
	
	public void setRecommendedItems(List<RecommendedItems> recommended)
	{
		this.recommended = recommended;
	}
	
	public void setPassive(Passive passive)
	{
		this.passive = passive;
	}
	
	public void setSpells(List<Spell> spells)
	{
		this.spells = spells;
	}
	
	public void setStats(Stats stats)
	{
		this.stats = stats;
	}
	
	//Custom object methods
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null || !(o instanceof Champion))
			return false;
		
		Champion c = (Champion)o;
		
		if(!locale.equals(c.locale))
			return false;
		
		if(id >= 0  && id == c.id)
			return true;
		if(name != null && name.equals(c.name))
			return true;
		
		//Unlikely to ever get to this point, but there's nothing else we can compare
		return false;
	}
}
