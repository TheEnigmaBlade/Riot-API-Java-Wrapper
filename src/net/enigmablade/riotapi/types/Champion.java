package net.enigmablade.riotapi.types;

import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.exceptions.*;

/**
 * Information about a League of Legends champion.
 * 
 * @author Enigma
 */
public class Champion extends ApiType
{
	private Region region;
	
	private String name;
	private long id;
	private int attackRank, magicRank, defenseRank, difficultyRank;
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
	}
	
	//Convenience methods
	
	/**
	 * Verifies required information for convenience methods is available, such as champion ID.
	 * If not found, makes an API call to get summoner information.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	private void verifyConvenienceState() throws RiotApiException
	{
		if(!hasApiUpdated())
		{
			setApiUpdated();
			api.getChampionMethod().fillChampion(this, region);
		}
	}
	
	//Accessor methods
	
	public String getName() throws RiotApiException
	{
		if(name == null)
			verifyConvenienceState();
		return name;
	}
	
	public long getId() throws RiotApiException
	{
		if(id < 0)
			verifyConvenienceState();
		return id;
	}
	
	public int getAttackRank() throws RiotApiException
	{
		verifyConvenienceState();
		return attackRank;
	}
	
	public int getMagicRank() throws RiotApiException
	{
		verifyConvenienceState();
		return magicRank;
	}
	
	public int getDefenseRank() throws RiotApiException
	{
		verifyConvenienceState();
		return defenseRank;
	}
	
	public int getDifficultyRank() throws RiotApiException
	{
		verifyConvenienceState();
		return difficultyRank;
	}
	
	public boolean isActive() throws RiotApiException
	{
		verifyConvenienceState();
		return active;
	}
	
	public boolean isFreeToPlay() throws RiotApiException
	{
		verifyConvenienceState();
		return freeToPlay;
	}
	
	public boolean isBotMatchMadeEnabled() throws RiotApiException
	{
		verifyConvenienceState();
		return botMatchMadeEnabled;
	}
	
	public boolean isBotCustomEnabled() throws RiotApiException
	{
		verifyConvenienceState();
		return botCustomEnabled;
	}
	
	public boolean isRankedEnabled() throws RiotApiException
	{
		verifyConvenienceState();
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
}
