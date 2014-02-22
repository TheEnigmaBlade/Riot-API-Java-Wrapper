package net.enigmablade.riotapi.methods;

import java.util.*;
import net.enigmablade.jsonic.*;
import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.Requester.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.exceptions.*;
import net.enigmablade.riotapi.types.*;
import net.enigmablade.riotapi.util.*;
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
	public TeamMethod(RiotApi api)
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
	 * @throws TeamNotFoundException If the given summoner was not found, or if the summoner is not in any teams.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public List<Team> getSummonerTeams(Region region, long summonerId) throws RiotApiException
	{
		//Make request
		Response response = getMethodResult(region,
				"by-summoner/{summonerId}",
				createArgMap("summonerId", String.valueOf(summonerId)));
		
		//Check errors
		if(response.getCode() == 404)
			throw new TeamNotFoundException(region);
		
		//Parse response
		JsonArray teamsArray = (JsonArray)response.getValue();
		List<Team> teams = new ArrayList<>(teamsArray.size());
		for(int t = 0; t < teamsArray.size(); t++)
		{
			JsonObject teamObject = teamsArray.getObject(t);
			teams.add(convertTeam(teamObject, region));
		}
		
		return teams;
	}
	
	/**
	 * Returns a map of the teams corresponding to the given team IDs.
	 * @param region The game region (NA, EUW, EUNE, etc.)
	 * @param teamIds The team IDs.
	 * @return A map of team IDs to teams.
	 * @throws RegionNotSupportedException If the region is not supported by the method.
	 * @throws TeamNotFoundException If the given summoner was not found, or if the summoner is not in any teams.
	 * @throws RiotApiException If there was an exception or error from the server.
	 */
	public Map<String, Team> getTeams(Region region, String... teamIds) throws RiotApiException
	{
		String ids = IOUtil.createCommaDelimitedString(teamIds);
		
		//Make request
		Response response = getMethodResult(region,
				"{teamIds}",
				createArgMap("teamIds", ids));
		
		//Check errors
		if(response.getCode() == 404)
			throw new TeamNotFoundException(region);
		
		//Parse response
		JsonObject teamsObject = (JsonObject)response.getValue();
		Map<String, Team> teams = new HashMap<String, Team>();
		for(String teamId : teamsObject.keySet())
		{
			JsonObject teamObject = teamsObject.getObject(teamId);
			teams.put(teamId, convertTeam(teamObject, region));
		}
		return teams;
	}
	
	//Private converter methods
	
	/**
	 * Convert the JSON team object into a team object.
	 * @param teamObject The JSON object.
	 * @param region The game region.
	 * @return The converted team object.
	 */
	private Team convertTeam(JsonObject teamObject, Region region)
	{
		//Get team ID
		String id = teamObject.getString("fullId");
		//Convert stats
		Map<QueueType, Team.QueueStat> stats = convertStats(teamObject.getObject("teamStatSummary"));
		//Convert roster
		Team.Roster roster = convertRoster(teamObject.getObject("roster"), region);
		//Convert match history
		List<Team.Match> matchHistory = convertMatchHistory(teamObject.getArray("matchHistory"));
		//Convert MOTD
		Team.MessageOfTheDay motd = convertMotd(teamObject.getObject("messageOfDay"));
		
		long lastGame = teamObject.containsKey("lastGameDate") ? teamObject.getLong("lastGameDate") : -1;
		
		//Create league
		Team team = new Team(id, teamObject.getString("name"), teamObject.getString("tag"), teamObject.getString("status"),
				roster, matchHistory, stats, motd,
				teamObject.getLong("createDate"), teamObject.getLong("modifyDate"),
				lastGame, teamObject.getLong("lastJoinedRankedTeamQueueDate"),
				teamObject.getLong("lastJoinDate"), teamObject.getLong("secondLastJoinDate"), teamObject.getLong("thirdLastJoinDate"));
		return team;
	}
	
	/**
	 * Parse the queue stats portion of a team response.
	 * @param statSummaryObject
	 * @return A map of queue type and stat.
	 * @throws JsonException Parse error (shouldn't happen).
	 */
	private Map<QueueType, Team.QueueStat> convertStats(JsonObject statSummaryObject)
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
	private Team.Roster convertRoster(JsonObject rosterObject, Region region)
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
	private List<Team.Match> convertMatchHistory(JsonArray matchesArray)
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
	private Team.MessageOfTheDay convertMotd(JsonObject motdObject)
	{
		//May not exist in the response
		if(motdObject == null)
			return null;
		
		Team.MessageOfTheDay motd = new Team.MessageOfTheDay(motdObject.getString("message"),
				motdObject.getInt("version"), motdObject.getLong("createDate"));
		return motd;
	}
}
