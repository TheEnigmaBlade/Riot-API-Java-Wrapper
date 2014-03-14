package net.enigmablade.riotapi.exceptions;

import net.enigmablade.riotapi.constants.*;

public class LeagueNotFoundException extends NotFoundException
{
	public LeagueNotFoundException(Region region, String participantId)
	{
		super("No leagues were found for "+participantId+" in "+region);
	}
}
