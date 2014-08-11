package net.enigmablade.riotapi.constants;

/**
 * Constants for a league's tier and rank.
 * 
 * @author Enigma
 */
public enum LeagueTier
{
	TIER_CHALLENGER("Challenger"), TIER_DIAMOND("Diamond"), TIER_PLATINUM("Platinum"), TIER_GOLD("Gold"), TIER_SILVER("Silver"), TIER_BRONZE("Bronze"),
	DIVISION_V("V"), DIVISION_IV("IV"), DIVISION_III("III"), DIVISION_II("II"), DIVISION_I("I"),
	UNKNOWN("Unknown");
	
	/* --- */
	
	private String name;
	
	private LeagueTier(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	//Utility methods
	
	/**
	 * Returns the league constant representing the given value.
	 * For example, "diamond" would return TIER_DIAMOND and "iii" would return RANK_III.
	 * @param value The value.
	 * @return The corresponding league constant.
	 */
	public static LeagueTier stringToConstant(String value)
	{
		switch(value.toLowerCase())
		{
			case "challenger": return TIER_CHALLENGER;
			case "diamond": return TIER_DIAMOND;
			case "platinum": return TIER_PLATINUM;
			case "gold": return TIER_GOLD;
			case "silver": return TIER_SILVER;
			case "bronze": return TIER_BRONZE;
			
			case "v": return DIVISION_V;
			case "iv": return DIVISION_IV;
			case "iii": return DIVISION_III;
			case "ii": return DIVISION_II;
			case "i": return DIVISION_I;
			
			default: return UNKNOWN;
		}
	}
}
