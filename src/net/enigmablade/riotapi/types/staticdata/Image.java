package net.enigmablade.riotapi.types.staticdata;

public class Image
{
	private String full, group, sprite;
	private int x, y, w, h;
	
	public Image(String full, String group, String sprite, int x, int y, int w, int h)
	{
		this.full = full;
		this.group = group;
		this.sprite = sprite;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public String getFull()
	{
		return full;
	}
	
	public String getGroup()
	{
		return group;
	}
	
	public String getSprite()
	{
		return sprite;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getW()
	{
		return w;
	}
	
	public int getH()
	{
		return h;
	}
}
