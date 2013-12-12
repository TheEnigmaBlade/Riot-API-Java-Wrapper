package net.enigmablade.riotapi.util;

import java.io.*;
import java.net.*;

public class RiotApiUtil
{
	/**
	 * Encodes a piece of text for a URI by removing all spaces and encoding non-US-ASCII characters.
	 * @param text The text to encode.
	 * @return The encoded text.
	 */
	public static String encodeForUri(String text)
	{
		text = text.replace(" ", "");
		try
		{
			text = URLEncoder.encode(text, "UTF-8");
		}
		catch(UnsupportedEncodingException e) { /* Theoretically shouldn't happen since we're told to use UTF-8 */ }
		
		return text;
	}
}
