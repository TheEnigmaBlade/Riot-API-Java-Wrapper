package net.enigmablade.riotapi.types.lolstatus;

import java.util.*;

public class Service
{
	private String name;
	private String slug;
	private String status;	// Online, Alert, Offline, Deploying
	private List<Incident> incidents;
	
	public Service(String name, String slug, String status, List<Incident> incidents)
	{
		this.name = name;
		this.slug = slug;
		this.status = status;
		this.incidents = incidents;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getSlug()
	{
		return slug;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public List<Incident> getIncidents()
	{
		return Collections.unmodifiableList(incidents);
	}
}
