package net.enigmablade.riotapi.types;

public class Player
{
	private long summonerId;
	private int championId;
	private int teamId;
	
	public Player(long summonerId, int championId, int teamId)
	{
		this.summonerId = summonerId;
		this.championId = championId;
		this.teamId = teamId;
	}
	
	//Accessor methods
	
	public long getSummonerId()
	{
		return summonerId;
	}

	public int getChampionId()
	{
		return championId;
	}

	public int getTeamId()
	{
		return teamId;
	}
}