package net.enigmablade.riotapi.exceptions;

import net.enigmablade.riotapi.constants.*;

public class StaticDataNotFoundException extends NotFoundException
{
	public StaticDataNotFoundException(Region region, String dataId)
	{
		super("Static data with ID \""+dataId+"\" was not found in "+region);
	}
}
