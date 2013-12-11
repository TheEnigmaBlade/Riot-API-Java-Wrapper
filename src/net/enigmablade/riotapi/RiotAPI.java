package net.enigmablade.riotapi;

import net.enigmablade.riotapi.methods.*;

public class RiotApi
{
	private Requester requester;
	
	public RiotApi(String apiKey, String userAgent)
	{
		this.requester = new Requester(apiKey, userAgent);
	}
	
	//Methods
	
	public ChampionMethod getChampionMethod()
	{
		return new ChampionMethod(requester);
	}
}
