package net.enigmablade.riotapi.types.staticdata;

import java.util.*;
import net.enigmablade.riotapi.constants.*;

public class Spell
{
	public class SpellVar
	{
		public String link, key;
		public Object coeff;
		private boolean dynamic;
		
		public SpellVar(String key, String link, Object coeff, boolean dynamic)
		{
			this.key = key;
			this.link = link;
			this.coeff = coeff;
			this.dynamic = dynamic;
		}
		
		public boolean isDynamic()
		{
			return dynamic;
		}
		
		//public abstract float getCoeff(int rank);
		
		public Object getCoeff()
		{
			return coeff;
		}
	}
	
	/*public class StaticSpellVar extends SpellVar
	{
		private float coeff;
		
		public StaticSpellVar(String key, String link, float coeff)
		{
			super(key, link, false);
			this.coeff = coeff;
		}
		
		@Override
		public float getCoeff(int rank)
		{
			return coeff;
		}
	}
	
	public class DynamicSpellVar extends SpellVar
	{
		private List<Float> coeff;
		
		public DynamicSpellVar(String key, String link, List<Float> coeff)
		{
			super(key, link, true);
			this.coeff = coeff;
		}
		
		@Override
		public float getCoeff(int rank)
		{
			checkRank(rank);
			return coeff.get(rank);
		}
	}*/
	
	private String id, name, description;
	private Image image;
	
	private String tooltip;
	private List<String> levelTipLabels, levelTipEffects;
	private int maxRank;
	private List<SpellVar> vars;
	
	private ResourceType resourceType;
	private String resourceUsage;
	private List<Integer> costs;
	private String costBurn;
	
	private List<Integer> cooldowns;
	private String cooldownBurn;
	
	private List<Integer> ranges;
	private String rangeBurn;
	
	private List<List<Integer>> effects;
	private List<String> effectBurns;
	
	//Initialization
	
	public Spell(String name, String id, String description, Image image)
	{
		this.name = name;
		this.id = id;
		this.description = description;
		this.image = image;
	}
	
	public void initGeneral(String tooltip, List<String> levelTipLabels, List<String> levelTipEffects, int maxRank, List<SpellVar> vars)
	{
		this.tooltip = tooltip;
		this.levelTipLabels = levelTipLabels;
		this.levelTipEffects = levelTipEffects;
		this.maxRank = maxRank;
		this.vars = vars;
	}
	
	public void initResource(ResourceType resourceType, String resourceUsage, List<Integer> cost, String costBurn)
	{
		this.resourceType = resourceType;
		this.resourceUsage = resourceUsage;
		this.costs = cost;
		this.costBurn = costBurn;
	}
	
	public void initInfo(List<Integer> cooldowns, String cooldownBurn, List<Integer> ranges, String rangeBurn)
	{
		this.cooldowns = cooldowns;
		this.cooldownBurn = cooldownBurn;
		this.ranges = ranges;
		this.rangeBurn = rangeBurn;
	}
	
	public void initEffects(List<List<Integer>> effects, List<String> effectBurns)
	{
		this.effects = effects;
		this.effectBurns = effectBurns;
	}
	
	//Accessor methods
	
	public String getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public Image getImage()
	{
		return image;
	}
	
	public String getTooltip()
	{
		return tooltip;
	}
	
	public String getTooltip(int rank)
	{
		checkRank(rank);
		//TODO: implement conversion
		return null;
	}
	
	public List<String> getLevelTipLabels()
	{
		return new ArrayList<>(levelTipLabels);
	}
	
	public List<String> getLevelTipEffects()
	{
		return new ArrayList<>(levelTipEffects);
	}
	
	public int getMaxRank()
	{
		return maxRank;
	}
	
	public ResourceType getResourceType()
	{
		return resourceType;
	}
	
	public String getResourceUsage()
	{
		return resourceUsage;
	}
	
	public String getResourceUsage(int rank)
	{
		checkRank(rank);
		//TODO: implement conversion
		return null;
	}
	
	public List<Integer> getCosts()
	{
		return new ArrayList<Integer>(costs);
	}
	
	public String getCost()
	{
		return costBurn;
	}
	
	public int getCost(int rank)
	{
		checkRank(rank);
		return costs.get(rank-1);
	}
	
	public List<Integer> getCooldowns()
	{
		return new ArrayList<Integer>(cooldowns);
	}
	
	public String getCooldown()
	{
		return cooldownBurn;
	}
	
	public int getCooldown(int rank)
	{
		checkRank(rank);
		return cooldowns.get(rank-1);
	}
	
	public List<Integer> getRanges()
	{
		return new ArrayList<Integer>(ranges);
	}
	
	public String getRange()
	{
		return rangeBurn;
	}
	
	public int getRange(int rank)
	{
		checkRank(rank);
		return ranges.get(rank-1);
	}
	
	//Helper methods
	
	private void checkRank(int rank) throws RuntimeException
	{
		if(rank < 1)
			throw new IllegalArgumentException("Rank must be >= 1");
		if(rank > maxRank)
			throw new IllegalArgumentException("Rank must be <= "+maxRank);
	}
}
