package net.enigmablade.riotapi;

public class RiotApiException extends Exception
{
	public RiotApiException(String message)
	{
		super(message);
	}
	
	public RiotApiException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
