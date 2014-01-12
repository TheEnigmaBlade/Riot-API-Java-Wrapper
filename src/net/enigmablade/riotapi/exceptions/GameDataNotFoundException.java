package net.enigmablade.riotapi.exceptions;

import net.enigmablade.riotapi.constants.*;

public class GameDataNotFoundException extends RiotApiException
{
	public GameDataNotFoundException(Region region, long summonerId)
	{
		super("Game data for summoner ID \""+summonerId+"\" was not found in "+region);
	}
}
