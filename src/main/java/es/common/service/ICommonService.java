package es.common.service;

import java.util.Collection;

import org.springframework.hateoas.EntityModel;

/**
 * Contract to be used by those services that are being used by the controller layer
 * 
 * @param <D> The data transfer object representation of the domain
 * @param <K> The domain entity
 */
public interface ICommonService<D, K> {
	
	/**
	 * Returns all entities transformed to its DTO
	 * 
	 * @return all domain entities
	 */
	Collection<EntityModel<D>> findAll();
	
	/**
	 * Return the DTO with links of the domain entity by the given identifier
	 * 
	 * @return DTO found
	 */
	EntityModel<D> findById(K id);
	
	/**
	 * Saves the domain entity associated with the given DTO
	 * 
	 * @return the DTO with links of the saved entity
	 */
	EntityModel<D> save(D id);
	
	/**
	 * Saves the domain entity associated with the given DTO
	 * 
	 * @return the DTO with links of the saved entity
	 */
	EntityModel<D> update(D id);
		
	/**
	 * Saves the domain entities associated with the given DTOs
	 * 
	 * @return the DTOs of the saved entities
	 */
	Collection<EntityModel<D>> saveAll(Collection<D> dtos);
	
	/**
	 * Saves the domain entities associated with the given DTOs
	 * 
	 * @return the DTOs of the saved entities
	 */
	Collection<EntityModel<D>> updateAll(Collection<D> dtos);
	
	
	void deleteById(K id);
	
	
	void deleteByIds(Collection<K> ids);
	
}
