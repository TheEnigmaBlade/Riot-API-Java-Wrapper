package net.enigmablade.riotapi.constants;

/**
 * Constants for a league's tier and rank.
 * 
 * @author Enigma
 */
public enum LeagueTier
{
	TIER_CHALLENGER, TIER_DIAMOND, TIER_PLATINUM, TIER_GOLD, TIER_SILVER, TIER_BRONZE,
	RANK_V, RANK_IV, RANK_III, RANK_II, RANK_I, 
	UNKNOWN;
	
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
			
			case "v": return RANK_V;
			case "iv": return RANK_IV;
			case "iii": return RANK_III;
			case "ii": return RANK_II;
			case "i": return RANK_I;
			
			default: return UNKNOWN;
		}
	}
}
