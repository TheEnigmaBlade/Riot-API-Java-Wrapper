package net.enigmablade.riotapi.constants;

/**
 * All available League of Legends languages and their locale codes.
 * 
 * @author Enigma
 */
public enum Locale
{
	ENGLISH_US("en_US"),
	ENGLISH_GB("en_GB"),
	ENGLISH_AU("en_AU"),
	SPANISH("es_ES"),
	SPANISH_MX("es_MX"),
	SPANISH_AR("es_AR"),
	FRENCH("fr_FR"),
	GERMAN("de_DE"),
	ITALIAN("it_IT"),
	POLISH("pl_PL"),
	ENGLISH_PL("en_PL"),
	GREEK("el_GR"),
	ROMANIAN("ro_RO"),
	CZECH("cs_CZ"),
	HUNGARIAN("hu_HU"),
	PORTUGUESE_BRAZIL("pt_BR"),
	TURKISH("tr_TR"),
	THAI("th_TH"),
	VIETNAMESE("vn_VN"),
	INDONESIAN("id_ID"),
	RUSSIAN("ru_RU"),
	KOREAN("ko_KR"),
	CHINESE_CHINA("zh_CN"),
	CHINESE_TAIWAN("zh_TW");
	
	/* --- */
	
	private String value;
	
	private Locale(String value)
	{
		this.value = value;
	}
	
	/**
	 * Get the locale's locale code.
	 * @return
	 */
	public String getValue()
	{
		return value;
	}
	
	public static Locale getFromValue(String value)
	{
		if(value != null)
		{
			for(Locale v : values())
				if(v.getValue().equals(value))
					return v;
		}
		return null;
	}
}
