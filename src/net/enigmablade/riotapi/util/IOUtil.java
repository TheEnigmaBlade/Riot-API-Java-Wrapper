package net.enigmablade.riotapi.util;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;

public class IOUtil
{
	/**
	 * Creates a comma delimited string from an array of strings. For example:
	 * ["a", "b", "c", "w"] -> "a,b,c,w"
	 * @param things The particularly pompously provided array of strings.
	 * @return The boring new string.
	 */
	public static String createCommaDelimitedString(String... things)
	{
		StringBuffer s = new StringBuffer();
		for(int n = 0; n < things.length; n++)
		{
			s.append(things[n].toString());
			if(n < things.length-1)
				s.append(',');
		}
		return s.toString();
	}
	
	/**
	 * Creates a comma delimited string from an array of longs. For example:
	 * [1, 2, 3, 4] -> "1,2,3,4"
	 * @param things The gratifyingly glorious given array of longs.
	 * @return The dull new string.
	 */
	public static String createCommaDelimitedString(long... things)
	{
		StringBuffer s = new StringBuffer();
		for(int n = 0; n < things.length; n++)
		{
			s.append(things[n]);
			if(n < things.length-1)
				s.append(',');
		}
		return s.toString();
	}
	
	/**
	 * <p>Replace arguments in a given string with the given mappings. If an argument mapping is not found, it is ignored.</p>
	 * <p>The substring <code>{ARG}</code> denotes an argument with the key "<code>ARG</code>".</p>
	 * <p><b>Example</b>:<br/>
	 * Given string: "<code>/api/lol/{region}/v1.3/summoner/by-name/{summonerNames}</code>"<br/>
	 * Given arg map: <code>{"region"="na", "summonerNames"="TheEnigmaBlade"}</code><br/>
	 * Result: "<code>/api/lol/na/v1.3/summoner/by-name/TheEnigmaBlade</code>"</p>
	 * @param s The string.
	 * @param args The argument mappings.
	 * @return A new string with arguments replaced.
	 */
	public static String replacePathArgs(String s, Map<String, String> args)
	{
		StringBuffer buf = new StringBuffer();
		
		int start, end = 0;
		String part2 = s;
		while((start = s.indexOf('{', end)) >= 0)				//Find each arg
		{
			if((end = s.indexOf('}')) > 0)						//Find the end of the arg
			{
				String key = s.substring(start+1, end);			//Get the arg's key
				if(args.containsKey(key))						//Ignore if there's no replacement
				{
					String part1 = s.substring(0, start);		//Get everything before the arg
					s = part2 = s.substring(end+1, s.length());	//Get everything after the arg, continues to search in this part
					end = 0;									//Reset search position as well
					
					buf.append(part1).append(args.get(key));
				}
			}
		}
		buf.append(part2);										//Add any remaining bits
		
		return buf.toString();
	}
	
	public static String genQueryArgs(Map<String, String> queryArgs)
	{
		StringBuffer queryArgsStr = new StringBuffer();
		if(queryArgs != null)
		{
			int i = 0;
			for(String key : queryArgs.keySet())
			{
				queryArgsStr.append(key).append('=').append(queryArgs.get(key));
				if(i++ < queryArgs.size()-1)
					queryArgsStr.append('&');
			}
		}
		return queryArgsStr.toString();
	}
	
	/**
	 * Encodes a piece of text for a URI by removing all spaces and encoding non-US-ASCII-alphanumeric characters.
	 * @param text The text to encode.
	 * @return The encoded text.
	 */
	public static String encodeForUri(String text)
	{
		if(text == null)
			return null;
		
		//Remove invalid characters
		//If you don't know how2regex, this line removes all characters that aren't unicode alphabetic or
		//digit numbers (½ is a non-digit number character)
		text = text.replaceAll("[^\\p{L}\\p{Nd}]", "");
		
		//Encode UTF-8 in US-ASCII (À -> %C3%80)
		try
		{
			text = URLEncoder.encode(text, "UTF-8");
		}
		catch(UnsupportedEncodingException e) { /* Theoretically shouldn't happen since we're told to use UTF-8 */ }
		
		return text;
	}
	
	/**
	 * Read a UTF-8 formatted text input stream until a line-termination character. 
	 * @param in The input stream from which to read.
	 * @return The String representation of the input stream.
	 */
	public static String readInputStreamFully(InputStream in)
	{
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8"))))
		{
			StringBuffer buffer = new StringBuffer();
			String line;
			while((line = reader.readLine()) != null)
				buffer.append(line);
			return buffer.toString();
		}
		catch(IOException e)
		{
			return null;
		}
	}
}
