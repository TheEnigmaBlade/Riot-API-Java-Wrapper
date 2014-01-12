package net.enigmablade.riotapi.methods;

import java.util.*;
import net.enigmablade.jsonic.*;
import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.Requester.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.exceptions.*;
import net.enigmablade.riotapi.types.*;
import static net.enigmablade.riotapi.constants.Region.*;

/**
 * <p>The team method and its supporting operations.<p>
 * <p>Method support information:
 * 	<ul>
 * 		<li><i>Version</i>: 2.2</li>
 * 		<li><i>Regions</i>: NA, EUW, EUNE, BR, TR</li>
 * 	</ul>
 * </p>
 * <p>Operation information:
 * 	<ol>
 * 		<li>Get teams associated with a summoner ID</li>
 * 	</ol>
 * </p>
 * @see <a href="http://developer.riotgames.com/api/methods#!/256">Developer site</a>
 * 
 * @author Enigma
 */
public class TeamMethod extends Method
{
	/**
	 * Create a new league method instance.
	 * @param api The API instance being used.
	 */
	public TeamMethod(RiotAPI api)
	{
		super(api, "api/lol", "team", "2.2", new Region[]{NA, EUW, EUNE, BR, TR});
	}
	
	//API-defined operation methods
	
	/**
	 * Returns a list of teams the given summoner is in.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param summonerId The ID of the summoner.
	 * @return A list of recent games (max 10).
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws SummonerNotFoundException If the given summoner was not found, or if the summoner is not in any teams.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<Team> getTeams(Region region, long summonerId) throws RiotApiException
	{
		Response response = getMethodResult(region,
				"by-summoner/{summonerId}",
				createArgMap("summonerId", String.valueOf(summonerId)));
		
		//Check errors
		switch(response.getCode())
		{
			case 401: throw new RiotApiException("401: Unauthorized");
			case 404: throw new SummonerNotFoundException(region);
		}
		
		//Parse response
		try
		{
			//Convert to a list of teams
			JsonArray teamsArray = (JsonArray)response.getValue();
			List<Team> teams = new ArrayList<>(teamsArray.size());
			for(int t = 0; t < teamsArray.size(); t++)
			{
				JsonObject teamObject = teamsArray.getObject(t);
				
				//Get team ID
				String id = teamObject.getString("fullId");
				//Convert stats
				Map<QueueType, Team.QueueStat> stats = parseStats(teamObject.getObject("teamStatSummary"));
				//Convert roster
				Team.Roster roster = parseRoster(teamObject.getObject("roster"), region);
				//Convert match history
				List<Team.Match> matchHistory = parseMatchHistory(teamObject.getArray("matchHistory"));
				//Convert MOTD
				Team.MessageOfTheDay motd = parseMotd(teamObject.getObject("messageOfDay"));
				
				//Create league
				Team team = new Team(id, teamObject.getString("name"), teamObject.getString("tag"), teamObject.getString("status"),
						roster, matchHistory, stats, motd,
						teamObject.getLong("createDate"), teamObject.getLong("modifyDate"),
						teamObject.getLong("lastGameDate"), teamObject.getLong("lastJoinedRankedTeamQueueDate"),
						teamObject.getLong("lastJoinDate"), teamObject.getLong("secondLastJoinDate"), teamObject.getLong("thirdLastJoinDate"));
				teams.add(team);
			}
			
			return teams;
		}
		catch(JsonException e)
		{
			//Shouldn't happen since the JSON is already parsed
			System.err.println("JSON parse error");
			e.printStackTrace();
			return null;
		}
	}
	
	//Private converter methods
	
	/**
	 * Parse the queue stats portion of a team response.
	 * @param statSummaryObject
	 * @return A map of queue type and stat.
	 * @throws JsonException Parse error (shouldn't happen).
	 */
	private Map<QueueType, Team.QueueStat> parseStats(JsonObject statSummaryObject) throws JsonException
	{
		JsonArray statsArray = statSummaryObject.getArray("teamStatDetails");
		
		//Might not exist in the response if no games have been played (not sure)
		if(statsArray == null)
			return new TreeMap<>();
		
		Map<QueueType, Team.QueueStat> stats = new TreeMap<>();
		for(int s = 0; s < statsArray.size(); s++)
		{
			JsonObject statObject = statsArray.getObject(s);
			QueueType queue = QueueType.getFromGameValue(statObject.getString("teamStatType"));
			Team.QueueStat stat = new Team.QueueStat(statObject.getInt("wins"), statObject.getInt("losses"), statObject.getInt("averageGamesPlayed"),
					statObject.containsKey("rating") ? statObject.getInt("rating") : -1,
					statObject.containsKey("maxRating") ? statObject.getInt("maxRating") : -1,
					statObject.containsKey("seedRating") ? statObject.getInt("seedRating") : -1);
			stats.put(queue, stat);
		}
		return stats;
	}
	
	/**
	 * Parse the roster portion of a team response.
	 * @param rosterObject
	 * @param region
	 * @return The team roster.
	 * @throws JsonException Parse error (shouldn't happen).
	 */
	private Team.Roster parseRoster(JsonObject rosterObject, Region region) throws JsonException
	{
		//Create list of members
		JsonArray membersArray = rosterObject.getArray("memberList");
		List<Team.Roster.Member> members = new ArrayList<>(membersArray.size());
		for(int m = 0; m < membersArray.size(); m++)
		{
			JsonObject memberObject = membersArray.getObject(m);
			Summoner summoner = new Summoner(api, region, memberObject.getLong("playerId"), null);
			Team.Roster.Member member = new Team.Roster.Member(summoner, memberObject.getString("status"),
					memberObject.getLong("inviteDate"), memberObject.getLong("joinDate"));
			members.add(member);
		}
		return null;
	}
	
	/**
	 * Parse the match history portion of a team response.
	 * @param matchesArray
	 * @return A list of matches.
	 * @throws JsonException Parse error (shouldn't happen).
	 */
	private List<Team.Match> parseMatchHistory(JsonArray matchesArray) throws JsonException
	{
		//Might not exist in the response if no games have been played (not sure)
		if(matchesArray == null)
			return new ArrayList<>(0);
		
		List<Team.Match> matches = new ArrayList<>(matchesArray.size());
		for(int m = 0; m < matchesArray.size(); m++)
		{
			JsonObject matchObject = matchesArray.getObject(m);
			
			//Create match object
			Team.Match match = new Team.Match(matchObject.getLong("gameId"),
					matchObject.getString("gameMode"), matchObject.getInt("mapId"),
					matchObject.getInt("kills"), matchObject.getInt("deaths"), matchObject.getInt("assists"),
					matchObject.getBoolean("win"), matchObject.getBoolean("invalid"),
					matchObject.getString("opposingTeamName"), matchObject.getInt("opposingTeamKills"));
			matches.add(match);
		}
		return matches;
	}
	
	/**
	 * Parse the MOTD portion of a team response.
	 * @param motdObject
	 * @return The MOTD.
	 * @throws JsonException Parse error (shouldn't happen).
	 */
	private Team.MessageOfTheDay parseMotd(JsonObject motdObject) throws JsonException
	{
		//May not exist in the response
		if(motdObject == null)
			return null;
		
		Team.MessageOfTheDay motd = new Team.MessageOfTheDay(motdObject.getString("message"),
				motdObject.getInt("version"), motdObject.getLong("createDate"));
		return motd;
	}
}
