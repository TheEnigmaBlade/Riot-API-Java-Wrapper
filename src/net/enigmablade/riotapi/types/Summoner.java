package net.enigmablade.riotapi.types;

import java.util.*;

import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.exceptions.*;

public class Summoner
{
	private RiotApi api;
	
	private long id;
	private String name;
	private Region region;
	private int profileIconId;
	private long summonerLevel;
	private Date revisionDate;
	
	public Summoner(RiotApi api, Region region, long id, String name)
	{
		this(api, region, id, name, -1, -1, 0);
	}
	
	public Summoner(RiotApi api, Region region, long id, String name, int profileIconId, long summonerLevel, long revisionDate)
	{
		this.api = api;
		
		setRegion(region);
		setName(name);
		setId(id);
		setProfileIconId(profileIconId);
		setSummonerLevel(summonerLevel);
		setRevisionDate(revisionDate);
	}
	
	//Convenience methods
	
	public List<Game> getMatchHistory() throws RiotApiException
	{
		return api.getGameMethod().getRecentGames(region, id);
	}
	
	public Map<String, League> getLeagues() throws RiotApiException
	{
		return api.getLeagueMethod().getLeagues(region, id);
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
