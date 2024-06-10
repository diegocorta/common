package es.common.entity;

import java.io.Serializable;

/**
 * Contract to be used by those objects with identifier
 * 
 * Implies that the primary key must be treated as id, or at least, the setter and getter
 * 
 * @author diego cortavitarte
 * @version 202401
 */
public interface IdentifiableObject<K extends Serializable> {

	/**
	 * Returns the identifier of the entity
	 * 
	 * @return the identifier of the entity
	 */
	K getId();
	
	/**
	 * Setters the identifier of the entity
	 * 
	 * @param id the identifier of the entity
	 */
	void setId(K id);
}
