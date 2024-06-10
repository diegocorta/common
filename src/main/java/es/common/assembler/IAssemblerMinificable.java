package es.common.assembler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.EntityModel;

public interface IAssemblerMinificable<E, D, M> extends IAssembler<E, D> {
	
	/**
	 * Converts a dto to its minified class. The minified class must be passed as 
	 * parameter.<br><br> 
	 * 
	 * The method use BeanUtils to automatically copy the properties from one entity
	 * to the other.
	 * 
	 * @param dto the data to be minified
	 * @param clazz the minified class
	 * @return the minified information
	 */
	default M buildMinDtoFromDto(D dto, Class<M> clazz) {
		
		try {
			
			M minified = clazz.getDeclaredConstructor().newInstance();
			
			BeanUtils.copyProperties(dto, minified);
			
			return minified;
			
		} catch (Exception e) {
			
			String dtoClassName = dto.getClass().getCanonicalName();
			
			throw new RuntimeException("Error obtaining minified version of "+dtoClassName+" dto representation", e);
		}
		
	};
	
	/**
	 * Converts a domain object to its minified transfer object information. The minified class must be passed as 
	 * parameter.<br><br> 
	 * 
	 * @param entity the domain object to be minified
	 * @param clazz the minified class
	 * @return the minified information
	 */
	default M buildDtoMinFromEntity(E entity, Class<M> clazz) {
		
		try {
			
			D dto = buildDtoFromEntity(entity);
			
			M minified = clazz.getDeclaredConstructor().newInstance();
			
			BeanUtils.copyProperties(dto, minified);
			
			return minified;
			
		} catch (Exception e) {
			
			String entityClassName = entity.getClass().getCanonicalName();
			
			throw new RuntimeException("Error obtaining minified version of "+entityClassName+" entity representation", e);
		}
		
	};
	
	/**
	 * Converts a domain object to its minified transfer object information. The minified class must be passed as 
	 * parameter.<br><br> 
	 * 
	 * @param entity the domain object to be minified
	 * @param clazz the minified class
	 * @return the minified information
	 */
	default EntityModel<M> buildMinDtoWithLinksFromEntity(E entity, Class<M> clazz) {
		
		try {
			
			// Generates the links of the DTO
			EntityModel<D> dtoWithLinks = buildDtoWithLinksFromEntity(entity);
			
			// Create an instance of the minified class
			M minified = clazz.getDeclaredConstructor().newInstance();
			// Copy links from the original DTO to the minified class
			BeanUtils.copyProperties(dtoWithLinks.getContent(), minified);
			
			// Generates the entityModel of the minified class and assign the links
			EntityModel<M> dtoMinWithLinks = EntityModel.of(minified);
			dtoMinWithLinks.add(dtoWithLinks.getLinks());
			
			return dtoMinWithLinks;
			
		} catch (Exception e) {
			
			String entityClassName = entity.getClass().getCanonicalName();
			
			throw new RuntimeException("Error obtaining minified version of "+entityClassName+" entity representation", e);
		}
		
	};
	
	/**
	 * Converts a domain object to its minified transfer object information. The minified class must be passed as 
	 * parameter.<br><br> 
	 * 
	 * @param entity the domain object to be minified
	 * @param clazz the minified class
	 * @return the minified information
	 */
	default Collection<EntityModel<M>> buildMinDtosWithLinksFromEntities(Collection<E> entities, Class<M> clazz) {
		
		List<EntityModel<M>> list = new ArrayList<>();
		
		for (var entity : entities) {
			list.add(buildMinDtoWithLinksFromEntity(entity, clazz));
		}
		
		return list;
		
	};

}
