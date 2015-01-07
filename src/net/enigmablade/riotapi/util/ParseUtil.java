package net.enigmablade.riotapi.util;

import java.util.*;
import java.util.function.*;
import net.enigmablade.jsonic.*;

public class ParseUtil
{
	public static <T> List<T> arrayToList(JsonArray array, Function<JsonObject, T> conversion)
	{
		List<T>	list = new ArrayList<>(array.size());
		for(JsonIterator it = array.iterator(); it.hasNext();)
		{
			JsonObject obj = it.nextObject();
			T result = conversion.apply(obj);
			list.add(result);
		}
		return list;
	}
}
