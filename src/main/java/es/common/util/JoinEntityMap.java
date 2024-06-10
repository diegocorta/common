package es.common.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.springframework.data.util.Pair;

import jakarta.validation.constraints.NotNull;

/**
 *	Wrapper class to be used as a generic map. Inside the map, all type of objects can be stored,
 *  with the condition that every one of them must be returned with its given clazz type for autocasting. 
 */
public class JoinEntityMap {
	
	private final HashMap<String, Object> genericMap = new HashMap<>();

	private JoinEntityMap() {
	}

	public static JoinEntityMap from(@NotNull String key, @NotNull Object entity) {
    	
    	JoinEntityMap genericMap = new JoinEntityMap();
    	genericMap.put(key, entity);
    	
    	return genericMap;
    	
    }
    
    public static JoinEntityMap from(Collection<Pair<@NotNull String, @NotNull Object>> pairs) {
    	
    	JoinEntityMap genericMap = new JoinEntityMap();
    	
    	for(Pair<String, Object> pair : pairs)
    		genericMap.put(pair.getFirst(), pair.getSecond());
    	
    	return genericMap;
    	
    }
	
	/**
	 * Associates the specified value with the specified key in this map.
	 * If the map previously contained a mapping for the key, the old value is replaced.
	 * 
	 * @param <T> value class type
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 *
	 */
    public <E> void put(@NotNull String key, @NotNull E entity) {
    	
    	genericMap.put(key, entity);
 
    }

    /**
     * Returns the value to which the specified key is mapped.
     * 
     * @param <T> value expected class type
     * @param key the key whose associated value is to be returned 
     * @param clazz the expected class type
     * 
     * @return the value to which the specified key is mapped, or null if this map contains no mapping for the key
     * 
     * @throws NoSuchElementException when the object is not found with the given key
     * @throws IllegalStaleException when the clazz type received is not compatible with the object stores with the given key
     */
    public <E> E get(String key, Class<E> clazz) {
    	
    	Object value = genericMap.get(key);
	      
	  	if (value == null) {
	          throw new NoSuchElementException("Not an object found with the key: " + key);
	      }
	
	      if (!clazz.isInstance(value)) {
	          throw new IllegalStateException("The class type received: '" +clazz.getCanonicalName() + 
	          		"' does not match the type of the object found: '" +value.getClass().getCanonicalName() +
	          		"' with the given key: '"+ key +"'");
	      }
	
	      return clazz.cast(value);
    	
    }
    
}
