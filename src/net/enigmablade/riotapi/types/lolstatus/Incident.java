package net.enigmablade.riotapi.types.lolstatus;

import java.util.*;
import net.enigmablade.riotapi.constants.Locale;

public class Incident
{
	public static class Message
	{
		private long id;
		private String severity;	// info, alert, error
		private String author;
		private String content;
		private Map<Locale, String> translations;
		private String createdAt, updatedAt;
		
		public Message(long id, String severity, String author, String content, Map<Locale, String> translations, String createdAt, String updatedAt)
		{
			this.id = id;
			this.severity = severity;
			this.author = author;
			this.content = content;
			this.translations = translations;
			this.createdAt = createdAt;
			this.updatedAt = updatedAt;
		}
		
		public long getId()
		{
			return id;
		}
		
		public String getSeverity()
		{
			return severity;
		}
		
		public String getAuthor()
		{
			return author;
		}
		
		public String getContent()
		{
			return content;
		}
		
		public Map<Locale, String> getTranslations()
		{
			return Collections.unmodifiableMap(translations);
		}
		
		public String getCreatedAt()
		{
			return createdAt;
		}
		
		public String getUpdatedAt()
		{
			return updatedAt;
		}
		
		//Overrides
		
		@Override
		public String toString()
		{
			return "#"+id+"["+author+"]";
		}
	}
	
	private long id;
	private String createdAt;
	private boolean active;
	private List<Message> messages;
	
	public Incident(long id, String createdAt, boolean active, List<Message> messages)
	{
		this.id = id;
		this.createdAt = createdAt;
		this.active = active;
		this.messages = messages;
	}

	public long getId()
	{
		return id;
	}
	
	public String getCreatedAt()
	{
		return createdAt;
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public void setActive(boolean active)
	{
		this.active = active;
	}
	
	public List<Message> getMessages()
	{
		return Collections.unmodifiableList(messages);
	}
	
	//Overrides
	
	@Override
	public String toString()
	{
		return "#"+id+"[active="+active+", messages="+messages.size()+"]";
	}
	
	@Override
	public int hashCode()
	{
		return new Long(id).hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null || !(obj instanceof Incident))
			return false;
		return id == ((Incident)obj).id;
	}
}
