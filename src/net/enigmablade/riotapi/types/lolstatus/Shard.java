package net.enigmablade.riotapi.types.lolstatus;

import java.util.*;
import net.enigmablade.riotapi.*;
import net.enigmablade.riotapi.constants.*;
import net.enigmablade.riotapi.constants.Locale;
import net.enigmablade.riotapi.exceptions.*;
import net.enigmablade.riotapi.types.*;

public class Shard extends DynamicType
{
	private String name;
	private String hostname;
	private Region region;
	private List<Locale> locales;
	private String slug;
	private List<Service> services;
	
	public Shard(RiotApi api, String name, String hostname, String regionTag, List<Locale> locales, String slug)
	{
		super(api);
		this.name = name;
		this.hostname = hostname;
		this.region = Region.getFromTag(regionTag, slug);
		this.locales = locales;
		this.slug = slug;
	}
	
	//Helper methods
	
	private void verifyState() throws RiotApiException
	{
		if(!hasTypeUpdated())
		{
			api.getLolStatusApiMethod().fillShard(this);
		}
	}
	
	//Accessor methods
	
	public String getName()
	{
		return name;
	}

	public String getHostname()
	{
		return hostname;
	}

	public Region getRegion()
	{
		return region;
	}

	public List<Locale> getLocales()
	{
		return Collections.unmodifiableList(locales);
	}

	public String getSlug()
	{
		return slug;
	}
	
	public List<Service> getServices() throws RiotApiException
	{
		verifyState();
		return services;
	}
	
	public void setServices(List<Service> services)
	{
		setTypeUpdated();
		this.services = services;
	}

	@Override
	public String toString()
	{
		return region.toString()+"["+name+"/"+slug+"]";
	}
}
