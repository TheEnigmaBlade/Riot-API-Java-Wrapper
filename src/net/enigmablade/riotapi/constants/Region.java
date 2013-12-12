package net.enigmablade.riotapi.constants;

/**
 * Regions available to be use in API calls.
 * 
 * @author Enigma
 */
public enum Region
{
	NA("na"),
	EUW("euw"),
	EUNE("eune"),
	BR("br"),
	TR("tr");
	
	//---//
	
	private String apiUse;
	
	private Region(String apiUse)
	{
		this.apiUse = apiUse;
	}
	
	public String getApiUsage()
	{
		return apiUse;
	}
}
