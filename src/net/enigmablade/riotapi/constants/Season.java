package net.enigmablade.riotapi.constants;

public enum Season
{
	SEASON_3("SEASON3"),
	SEASON_4("SEASON4");
	
	//---//
	
	private String value;
	
	private Season(String value)
	{
		this.value = value;
	}
	
	public String getValue()
	{
		return value;
	}
}
