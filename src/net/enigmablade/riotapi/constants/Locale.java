package net.enigmablade.riotapi.constants;

public enum Locale
{
	ENGLISH_US("en_US");
	
	/* --- */
	
	private String value;
	
	private Locale(String value)
	{
		this.value = value;
	}
	
	public String getValue()
	{
		return value;
	}
}
