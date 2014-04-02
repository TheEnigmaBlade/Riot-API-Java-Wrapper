package net.enigmablade.riotapi.types.staticdata.masteries;

import java.util.*;
import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.constants.staticdata.*;
import net.enigmablade.riotapi.exceptions.*;
import net.enigmablade.riotapi.types.*;

public class Masteries extends DynamicType
{
	private String version;
	private Map<String, Mastery> masteries;
	private List<String> defenseTree, offenseTree, utilityTree;
	
	//Constructors
	
	public Masteries(RiotApi api, String version, Map<String, Mastery> masteries, List<String> offenseTree, List<String> defenseTree, List<String> utilityTree)
	{
		super(api, 0);
		
		this.version = version;
		this.masteries = masteries;
		this.defenseTree = defenseTree;
		this.offenseTree = offenseTree;
		this.utilityTree = utilityTree;
	}
	
	//State methods
	
	/**
	 * Verifies required information for static information methods is available.
	 * If not found, makes an API call to get dynamic champion information.
	 * @param type The type of static data being checked.
	 * @throws RiotApiException If there was an exception or an error from the server.
	 */
	private void verifyStaticState(ChampionDataType type) throws RiotApiException
	{
		if(!hasTypeUpdated(type))
		{
			setTypeUpdated(type);
			//api.getStaticDataApiMethod().fillMasteries(this, region, type);
		}
	}
	
	//Accessor methods
	
	public String getVersion()
	{
		return version;
	}
	
	public Map<String, Mastery> getMasteries()
	{
		return masteries;
	}
	
	public Mastery getMastery(String masteryId)
	{
		return masteries.get(masteryId);
	}
	
	public List<String> getDefenseTree()
	{
		return defenseTree;
	}
	
	public List<String> getOffenseTree()
	{
		return offenseTree;
	}
	
	public List<String> getUtilityTree()
	{
		return utilityTree;
	}
}
