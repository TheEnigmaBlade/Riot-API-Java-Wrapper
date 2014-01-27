package net.enigmablade.riotapi.types;

import java.util.*;
import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.exceptions.*;

/**
 * Information about a League of Legends summoner (player profile).
 * 
 * @author Enigma
 */
public class Summoner extends DynamicType
{
	private long id;
	private String name;
	private Region region;
	private int profileIconId;
	private long summonerLevel;
	private Date revisionDate;
	
	/**
	 * <p>Create a new summoner in the given region. One or both of ID and name must be given.</p>
	 * <p><b>Note</b>: Internal use only (unless you know what you're doing).</p>
	 * @param api The current API instance.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param id The summoner ID.
	 * @param name The summoner name.
	 */
	public Summoner(RiotApi api, Region region, long id, String name)
	{
		this(api, region, id, name, -1, -1, 0);
	}
	
	/**
	 * <p>Create a new summoner with the given information. All information must be provided.</p>
	 * <p><b>Note</b>: Internal use only (unless you know what you're doing).</p>
	 * @param api The current API instance.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param id The summoner ID.
	 * @param name The summoner name.
	 * @param profileIconId The summoner profile icon ID.
	 * @param summonerLevel The summoner level.
	 * @param revisionDate The revision date of the summoner.
	 */
	public Summoner(RiotApi api, Region region, long id, String name, int profileIconId, long summonerLevel, long revisionDate)
	{
		super(api);
		setRegion(region);
		setName(name);
		setId(id);
		setProfileIconId(profileIconId);
		setSummonerLevel(summonerLevel);
		setRevisionDate(revisionDate);
		
		if(id >= 0 && name != null)
			setTypeUpdated();
	}
	
	//Convenience methods
	
	/**
	 * Verifies required information for convenience methods is available, such as summoner ID.
	 * If not found, makes an API call to get summoner information.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	private void verifyConvenienceState() throws RiotApiException
	{
		if(!hasTypeUpdated())
		{
			setTypeUpdated();
			api.getSummonerMethod().fillSummoner(this);
		}
	}
	
	/**
	 * Returns the match history of the current summoner.
	 * @return The list of recent games.
	 * @throws RegionNotSupportedException If the summoner's region is not supported.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<Game> getMatchHistory() throws RiotApiException
	{
		verifyConvenienceState();
		return api.getGameMethod().getRecentGames(region, id);
	}
	
	/**
	 * Returns the list of leagues with which the current summoner is associated.
	 * The list includes solo leagues (mapped to the summoner ID) and team leagues (mapped to the team ID).
	 * @return The list of leagues.
	 * @throws RegionNotSupportedException If the summoner's region is not supported.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<League> getLeagues() throws RiotApiException
	{
		verifyConvenienceState();
		return api.getLeagueMethod().getLeagues(region, id);
	}
	
	/**
	 * Returns the league the current summoner is in for the given queue type, or null if the summoner is not in a league.
	 * @param queue The queue type
	 * @return The league for the given queue type.
	 * @throws RegionNotSupportedException If the summoner's region is not supported.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public League getLeague(QueueType queue) throws RiotApiException
	{
		List<League> leagues = getLeagues();
		for(League league : leagues)
			if(league.getQueueType() == queue)
				return league;
		return null;
	}
	
	/**
	 * Returns the list of the current summoner's mastery pages.
	 * @return The list of mastery pages.
	 * @throws RegionNotSupportedException If the summoner's region is not supported.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<MasteryPage> getMasteryPages() throws RiotApiException
	{
		verifyConvenienceState();
		return api.getSummonerMethod().getSummonerMasteryPages(region, id);
	}
	
	/**
	 * Returns the current summoner's current mastery page (the page in use or last used).
	 * @return The current mastery page.
	 * @throws RegionNotSupportedException If the summoner's region is not supported.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public MasteryPage getCurrentMasteryPage() throws RiotApiException
	{
		List<MasteryPage> pages = getMasteryPages();
		for(MasteryPage page : pages)
			if(page.isCurrent())
				return page;
		return null;
	}
	
	/**
	 * Returns the list of the current summoner's rune pages.
	 * @return The list of rune pages.
	 * @throws RegionNotSupportedException If the summoner's region is not supported.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<RunePage> getRunePages() throws RiotApiException
	{
		verifyConvenienceState();
		return api.getSummonerMethod().getSummonerRunePages(region, id);
	}
	
	/**
	 * Returns the current summoner's current rune page (the page in use or last used).
	 * @return The current rune page.
	 * @throws RegionNotSupportedException If the summoner's region is not supported.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public RunePage getCurrentRunePage() throws RiotApiException
	{
		List<RunePage> pages = getRunePages();
		for(RunePage page : pages)
			if(page.isCurrent())
				return page;
		return null;
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
	
	public long getId() throws RiotApiException
	{
		if(id < 0)
			verifyConvenienceState();
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public String getName() throws RiotApiException
	{
		if(name == null)
			verifyConvenienceState();
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getProfileIconId() throws RiotApiException
	{
		verifyConvenienceState();
		return profileIconId;
	}
	
	public void setProfileIconId(int profileIconId)
	{
		this.profileIconId = profileIconId;
	}
	
	public long getSummonerLevel() throws RiotApiException
	{
		verifyConvenienceState();
		return summonerLevel;
	}
	
	public void setSummonerLevel(long summonerLevel)
	{
		this.summonerLevel = summonerLevel;
	}
	
	public Date getRevisionDate() throws RiotApiException
	{
		verifyConvenienceState();
		return revisionDate;
	}
	
	public void setRevisionDate(long revisionDate)
	{
		this.revisionDate = new Date(revisionDate);
	}
}
