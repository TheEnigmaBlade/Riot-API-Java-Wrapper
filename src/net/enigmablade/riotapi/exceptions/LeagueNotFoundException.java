package net.enigmablade.riotapi.exceptions;

import net.enigmablade.riotapi.constants.*;

public class LeagueNotFoundException extends RiotApiException
{
	public LeagueNotFoundException(Region region, long summonerId)
	{
		super("No leagues were found for summoner "+summonerId+" in "+region);
	}
}
