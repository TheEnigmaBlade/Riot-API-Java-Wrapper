package net.enigmablade.riotapi.exceptions;

import net.enigmablade.riotapi.constants.*;

public class SummonerNotFoundException extends RiotApiException
{
	public SummonerNotFoundException(Region region)
	{
		super("The summoners were not found in "+region);
	}
	
	public SummonerNotFoundException(Region region, String summoner)
	{
		super("Summoner \""+summoner+"\" was not found in "+region);
	}
	
	public SummonerNotFoundException(Region region, long summoner)
	{
		this(region, summoner+"");
	}
}
