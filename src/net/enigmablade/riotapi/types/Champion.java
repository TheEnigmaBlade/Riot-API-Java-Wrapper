package net.enigmablade.riotapi.types;

import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.exceptions.*;

/**
 * Information about a League of Legends champion.
 * 
 * @author Enigma
 */
public class Champion extends DynamicType
{
	private Region region;
	
	private String name;
	private long id;
	private int attackRank, magicRank, defenseRank, difficultyRank;
	
	//Dynamic data
	private boolean active, freeToPlay;
	private boolean botMatchMadeEnabled, botCustomEnabled, rankedEnabled;
	
	public Champion(RiotApi api, Region region, String name, long id)
	{
		this(api, region, name, id, -1, -1, -1, -1, false, false, false, false, false);
	}
	
	public Champion(RiotApi api, Region region, String name, long id, int attackRank, int magicRank, int defenseRank, int difficultyRank, boolean active, boolean freeToPlay, boolean botMatchMadeEnabled, boolean botCustomEnabled, boolean rankedEnabled)
	{
		super(api);
		
		this.region = region;
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
		
		if(attackRank >= 0)
			setTypeUpdated();
	}
	
	//Dynamic methods
	
	/**
	 * Verifies required information for dynamic information methods is available, such as champion ID.
	 * If not found, makes an API call to get dynamic champion information.
	 * @throws RiotApiException If there was an exception or an error from the server.
	 */
	private void verifyDynamicState() throws RiotApiException
	{
		if(!hasTypeUpdated())
		{
			setTypeUpdated();
			api.getChampionMethod().fillChampion(this, region);
		}
	}
	
	//Accessor methods
	
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
	
	//Custom object methods
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null || !(o instanceof Champion))
			return false;
		
		Champion c = (Champion)o;
		if(id >= 0  && id == c.id)
			return true;
		if(name != null && name.equals(c.name))
			return true;
		
		//Unlikely to ever get to this point, but there's nothing else we can compare
		return false;
	}
}
