package net.enigmablade.riotapi.constants;

/**
 * All available League of Legends languages and their locale codes.
 * 
 * @author Enigma
 */
public enum Locale
{
	ENGLISH_US("en_US"),
	SPANISH("es_ES"),
	FRENCH("fr_FR"),
	GERMAN("de_DE"),
	ITALIAN("it_IT"),
	POLISH("pl_PL"),
	GREEK("el_GR"),
	ROMANIAN("ro_RO"),
	PORTUGUESE_BRAZIL("pt_BR"),
	TURKISH("tr_TR"),
	THAI("th_TH"),
	VIETNAMESE("vn_VN"),
	INDONESIAN("id_ID"),
	RUSSIAN("ru_RU"),
	KOREAN("ko_KR"),
	CHINESE_CHINA("zh_CN"),
	CHINESE_TAIWAN("zh_TW"),;
	
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
}
