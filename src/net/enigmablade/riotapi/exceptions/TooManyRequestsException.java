package net.enigmablade.riotapi.exceptions;

public class TooManyRequestsException extends RiotApiException
{
	public TooManyRequestsException(String message)
	{
		super(message);
	}
}
