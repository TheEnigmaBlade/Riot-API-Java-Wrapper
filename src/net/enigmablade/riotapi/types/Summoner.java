package net.enigmablade.riotapi.types;

import java.util.*;
import net.enigmablade.riotapi.constants.*;

public class Summoner
{
	private long id;
	private String name;
	private Region region;
	private int profileIconId;
	private long summonerLevel;
	private Date revisionDate;
	
	public Summoner(Region region, String name)
	{
		this(region, -1, name);
	}

	public Summoner(Region region, long id)
	{
		this(region, id, null);
	}
	
	public Summoner(Region region, long id, String name)
	{
		this(region, id, name, -1, -1, 0);
	}
	
	public Summoner(Region region, long id, String name, int profileIconId, long summonerLevel, long revisionDate)
	{
		setRegion(region);
		setName(name);
		setId(id);
		setProfileIconId(profileIconId);
		setSummonerLevel(summonerLevel);
		setRevisionDate(revisionDate);
	}
	
	//Accessor and modifier methods
	
	public Region getRegion()
	{
		return region;
	}
	
	public void setRegion(Region region)
	{
		this.region = region;
	}
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getProfileIconId()
	{
		return profileIconId;
	}
	
	public void setProfileIconId(int profileIconId)
	{
		this.profileIconId = profileIconId;
	}
	
	public long getSummonerLevel()
	{
		return summonerLevel;
	}
	
	public void setSummonerLevel(long summonerLevel)
	{
		this.summonerLevel = summonerLevel;
	}
	
	public Date getRevisionDate()
	{
		return revisionDate;
	}
	
	public void setRevisionDate(long revisionDate)
	{
		this.revisionDate = new Date(revisionDate);
	}
}
