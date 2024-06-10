package es.common.service;

import java.util.Collection;

import org.springframework.hateoas.EntityModel;

/**
 * Contract to be used by those services that are being used by the controller layer
 * 
 * It implements the request to obtain the DTOs and its minified version with the basic CRUD
 * 
 * @param <D> The data transfer object representation of the domain
 * @param <K> The domain entity
 * @param <M> The minified data transfer object
 */
public interface ICommonMinifiedService<D, K, M> 
		extends ICommonService<D, K> {
	
	/**
	 * Returns all entities transformed to its minified DTO
	 * 
	 * @return all domain entities
	 */
	Collection<EntityModel<M>> findAllMinified();
	
	/**
	 * Return the minified DTO of the domain entity by the given identifier
	 * 
	 * @return minified DTO found
	 */
	EntityModel<M> findByIdMinified(K id);
	
	/**
	 * Saves the domain entity associated with the given DTO
	 * 
	 * @return the minified DTO of the saved entity
	 */
	EntityModel<M> saveMinified(D dto);
	
	/**
	 * Saves the domain entity associated with the given DTO
	 * 
	 * @return the minified DTO of the saved entity
	 */
	EntityModel<M> updateMinified(D dto);
		
	/**
	 * Saves the domain entities associated with the given DTOs
	 * 
	 * @return the minified DTOs of the saved entities
	 */
	Collection<EntityModel<M>> saveAllMinified(Collection<D> dtos);
	
	/**
	 * Saves the domain entities associated with the given DTOs
	 * 
	 * @return the minified DTOs of the saved entities
	 */
	Collection<EntityModel<M>> updateAllMinified(Collection<D> dtos);
	
}
