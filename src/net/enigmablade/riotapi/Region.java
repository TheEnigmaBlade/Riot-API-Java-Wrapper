package net.enigmablade.riotapi;

public enum Region
{
	NA("na"), EUW("euw"), EUNE("eune");
	
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
