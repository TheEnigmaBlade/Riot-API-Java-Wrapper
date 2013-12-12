package net.enigmablade.riotapi.util;

import java.util.*;

/**
 * A generic buffer pool using LRU replacement methods.
 * 
 * @author Enigma
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public class BufferPool<K, V>
{
	private ArrayDeque<K> keys;
	private ArrayDeque<V> values;
	private int capacity;
	
	/**
	 * Creates a new buffer pool with a capacity of 10.
	 */
	public BufferPool()
	{
		this(10);
	}
	
	/**
	 * Creates a new buffer pool with the specified capacity.
	 * @param capacity The capacity
	 */
	public BufferPool(int capacity)
	{
		keys = new ArrayDeque<K>(this.capacity = capacity);
		values = new ArrayDeque<V>(capacity);
	}
	
	//Function methods
	
	/**
	 * Adds a value to the buffer pool with the specified key.
	 * 
	 * @param key The new key
	 * @param value The new value
	 */
	public void add(K key, V value)
	{
		keys.addFirst(key);
		values.addFirst(value);
		trim();
	}
	
	/**
	 * Gets a value from the buffer pool and moves it to the top of the pool.
	 * 
	 * @param targetKey The key to get
	 * @return The value associated with the key
	 */
	public V get(K targetKey)
	{
		Iterator<K> keyIt = keys.iterator();
		Iterator<V> valueIt = values.iterator();
		
		//Iterate through the keys and values until the key is found
		while(keyIt.hasNext())
		{
			K key = keyIt.next();
			V value = valueIt.next();
			if(key.equals(targetKey))
			{
				//The key is found, move it to the top
				keyIt.remove();
				valueIt.remove();
				
				add(key, value);
				
				return value;
			}
		}
		
		return null;
	}
	
	/**
	 * Clears the buffer pool, removing all keys and values.
	 */
	public void clear()
	{
		keys.clear();
		values.clear();
	}
	
	//Helper methods
	
	/**
	 * Trims the buffer pool to the correct capacity.
	 */
	private void trim()
	{
		while(keys.size() > capacity)
		{
			keys.removeLast();
			values.removeLast();
		}
	}
	
	//General accessor methods
	
	/**
	 * Returns the capacity of the pool.
	 * @return The capacity
	 */
	public int getCapacity()
	{
		return capacity;
	}
	
	/**
	 * Sets the capacity of the pool.
	 * 
	 * @param capacity The new capacity
	 */
	public void setCapacity(int capacity)
	{
		this.capacity = capacity;
		trim();
	}
}
