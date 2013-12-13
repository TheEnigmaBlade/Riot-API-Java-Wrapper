package net.enigmablade.riotapi.types;

import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.constants.*;

/**
 * Information about a player that participated in a game or match.
 * 
 * @author Enigma
 */
public class Player extends Summoner
{
	private int championId;
	private int teamId;
	
	public Player(RiotApi api, Region region, long summonerId, int championId, int teamId)
	{
		super(api, region, summonerId, null);
		this.championId = championId;
		this.teamId = teamId;
	}
	
	//Accessor methods
	
	public int getChampionId()
	{
		return championId;
	}

	public int getTeamId()
	{
		return teamId;
	}
}
