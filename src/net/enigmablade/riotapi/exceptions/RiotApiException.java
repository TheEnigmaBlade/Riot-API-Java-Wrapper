package net.enigmablade.riotapi.exceptions;

/**
 * An exception thing.
 * 
 * @author Enigma
 */
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
