package net.enigmablade.riotapi.exceptions;

import net.enigmablade.riotapi.constants.*;

public class TeamNotFoundException extends NotFoundException
{
	public TeamNotFoundException(Region region)
	{
		super("The teams were not found in "+region);
	}
}
