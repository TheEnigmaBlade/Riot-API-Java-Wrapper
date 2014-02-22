package net.enigmablade.riotapi.exceptions;

import net.enigmablade.riotapi.constants.*;

public class SummonerNotFoundException extends NotFoundException
{
	public SummonerNotFoundException(Region region)
	{
		super("The summoners were not found in "+region);
	}
	
	public SummonerNotFoundException(Region region, long summonerId)
	{
		super("The summoner "+summonerId+" was not found in "+region);
	}
}
