package net.enigmablade.riotapi.types.staticdata;

import java.util.*;

public class RecommendedItems
{
	public static class Block
	{
		public static class Item
		{
			private String id;
			private int count;
			
			public Item(String id, int count)
			{
				this.id = id;
				this.count = count;
			}
			
			public String getId()
			{
				return id;
			}
			
			public int getCount()
			{
				return count;
			}
		}
		
		private String type;
		private List<Item> items;
		
		public Block(String type, List<Item> items)
		{
			this.type = type;
			this.items = items;
		}
		
		public String getType()
		{
			return type;
		}
		
		public List<Item> getItems()
		{
			return items;
		}
	}
	
	private String champion, map, mode, type;
	private String title;
	private boolean priority;
	private List<Block> blocks;
	
	public RecommendedItems(String champion, String map, String mode, String type, String title, boolean priority, List<Block> blocks)
	{
		this.champion = champion;
		this.map = map;
		this.mode = mode;
		this.type = type;
		this.title = title;
		this.priority = priority;
		this.blocks = blocks;
	}
	
	public String getChampion()
	{
		return champion;
	}
	
	public String getMap()
	{
		return map;
	}
	
	public String getMode()
	{
		return mode;
	}
	
	public String getType()
	{
		return type;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public boolean isPriority()
	{
		return priority;
	}
	
	public List<Block> getBlocks()
	{
		return blocks;
	}
}
