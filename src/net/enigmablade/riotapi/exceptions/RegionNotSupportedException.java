package net.enigmablade.riotapi.exceptions;

import java.util.*;

import net.enigmablade.riotapi.constants.*;

/**
 * An exception for when a region isn't supported by a method!
 * 
 * @author Enigma
 */
public class RegionNotSupportedException extends RiotApiException
{
	public RegionNotSupportedException(String method, Region region, Region[] supportedRegions)
	{
		super("Region \""+region+"\" is not supported by method \""+method+"\", supported: "+Arrays.toString(supportedRegions));
	}
}
