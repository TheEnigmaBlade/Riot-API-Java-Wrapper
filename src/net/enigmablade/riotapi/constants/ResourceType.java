package net.enigmablade.riotapi.constants;

public enum ResourceType
{
	MANA		("mana"),
	ENERGY		("energy"),
	RAGE		("rage"),
	FEROCITY	("ferocity"),
	SHIELD		("shield"),
	FURY		("fury"),
	BATTLEFURY	("battlefury"),
	DRAGONFURY	("dragonfury"),
	HEAT		("heat"),
	BLOODWELL	("bloodwell"),
	WIND		("wind"),
	NONE		("none", "nocost");
	
	/* --- */
	
	private String[] values;
	
	private ResourceType(String... values)
	{
		this.values = values;
	}
	
	//Static helper methods
	
	public static ResourceType getResourceType(String value)
	{
		for(ResourceType type : values())
			for(String t : type.values)
				if(t.equalsIgnoreCase(value))
					return type;
		return NONE;
	}
}
