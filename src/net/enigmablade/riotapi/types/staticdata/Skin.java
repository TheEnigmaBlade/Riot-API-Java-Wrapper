package net.enigmablade.riotapi.types.staticdata;

public class Skin
{
	private String id, name;
	private int num;
	
	public Skin(String id, String name, int num)
	{
		super();
		this.id = id;
		this.name = name;
		this.num = num;
	}
	
	public String getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getNum()
	{
		return num;
	}
}
