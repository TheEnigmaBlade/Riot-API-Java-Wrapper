package net.enigmablade.riotapi.types.staticdata;

import java.util.*;

public class RegionInfo
{
	private String cdnUrl;
	private String dragonMagic, dragonMagicCss;
	private String defaultLanguage;
	private String legacyMode;
	private int profileIconMax;
	private String store;
	
	private String realmVersion;
	private Map<String, String> typeVersions;
	
	public RegionInfo(String cdnUrl, String dragonMagic, String dragonMagicCss, String defaultLanguage, String legacyMode, int profileIconMax, String store, String realmVersion, Map<String, String> typeVersions)
	{
		this.cdnUrl = cdnUrl;
		this.dragonMagic = dragonMagic;
		this.dragonMagicCss = dragonMagicCss;
		this.defaultLanguage = defaultLanguage;
		this.legacyMode = legacyMode;
		this.profileIconMax = profileIconMax;
		this.store = store;
		this.realmVersion = realmVersion;
		this.typeVersions = typeVersions;
	}
	
	public String getCdnUrl()
	{
		return cdnUrl;
	}
	
	public String getDragonMagic()
	{
		return dragonMagic;
	}
	
	public String getDragonMagicCss()
	{
		return dragonMagicCss;
	}
	
	public String getDefaultLanguage()
	{
		return defaultLanguage;
	}
	
	public String getLegacyMode()
	{
		return legacyMode;
	}
	
	public int getProfileIconMax()
	{
		return profileIconMax;
	}
	
	public String getStore()
	{
		return store;
	}
	
	public String getRealmVersion()
	{
		return realmVersion;
	}
	
	public Map<String, String> getTypeVersions()
	{
		return typeVersions;
	}
}
