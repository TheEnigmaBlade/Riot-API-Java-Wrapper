package net.enigmablade.riotapi.types.staticdata;

public class Passive
{
	private String name, description;
	private Image image;
	
	public Passive(String name, String description, Image image)
	{
		this.name = name;
		this.description = description;
		this.image = image;
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
}
