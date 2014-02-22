package net.enigmablade.riotapi.exceptions;

public abstract class NotFoundException extends RiotApiException
{
	public NotFoundException(String message)
	{
		super(message);
	}
}
