package net.enigmablade.riotapi.types;

public class Champion
{
	private String name;
	private long id;
	private int attackRank, magicRank, defenseRank, difficultyRank;
	private boolean active, freeToPlay;
	private boolean botMatchMadeEnabled, botCustomEnabled, rankedEnabled;
	
	public Champion(String name, long id, int attackRank, int magicRank, int defenseRank, int difficultyRank, boolean active, boolean freeToPlay, boolean botMatchMadeEnabled, boolean botCustomEnabled, boolean rankedEnabled)
	{
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
	
	//Accessor methods

	public String getName()
	{
		return name;
	}

	public long getId()
	{
		return id;
	}

	public int getAttackRank()
	{
		return attackRank;
	}

	public int getMagicRank()
	{
		return magicRank;
	}

	public int getDefenseRank()
	{
		return defenseRank;
	}

	public int getDifficultyRank()
	{
		return difficultyRank;
	}

	public boolean isActive()
	{
		return active;
	}

	public boolean isFreeToPlay()
	{
		return freeToPlay;
	}

	public boolean isBotMatchMadeEnabled()
	{
		return botMatchMadeEnabled;
	}

	public boolean isBotCustomEnabled()
	{
		return botCustomEnabled;
	}

	public boolean isRankedEnabled()
	{
		return rankedEnabled;
	}
}
