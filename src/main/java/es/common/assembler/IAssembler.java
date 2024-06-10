package es.common.assembler;

import java.util.Collection;

import org.springframework.hateoas.EntityModel;

import es.common.entity.AbstractCommonEntity;
import es.common.util.JoinEntityMap;
import jakarta.validation.constraints.NotNull;

public interface IAssembler<E, D> {

	/**
	 * The method receives a data transfer object. It also receives the related entities.
	 * Whit that information, the domain entity can be properly formed
	 * 
	 * @param dto the data transfer object to be converted to a domain object
	 * @param relatedEntities the related domain entities
	 * @return the domain object
	 */
	E buildEntityFromDto(D dto, JoinEntityMap relatedEntities);
	
	/**
	 * The method receives a domain object to be converted to its data transfer object
	 * 
	 * @param entity the domain object
	 * @return the data transfer object
	 */
	D buildDtoFromEntity(E entity);
	
	/**
	 * Converts the specified entity domain to the corresponding HATEOAS representation of information
	 *
	 * @param dto complete representation to which we are going to add the HATEOAS links
	 * @return the representation of information created from the specified domain entity
	 */
	EntityModel<D> buildDtoWithLinksFromEntity( @NotNull E dto);
	
	/**
	 * Converts the specified entity domain to the corresponding HATEOAS representation of information
	 *
	 * @param dto complete representation to which we are going to add the HATEOAS links
	 * @return the representation of information created from the specified domain entity
	 */
	Collection<EntityModel<D>> buildDtosWithLinksFromEntities( @NotNull Collection<E> entities);

	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	default AbstractCommonEntity<?> copyCommonEntityDefaultProperties(AbstractCommonEntity<?> source, AbstractCommonEntity<?> target) {
		
		target.setActive(source.getActive());
		target.setCreatedAt(source.getCreatedAt());
		target.setModifiedAt(source.getModifiedAt());
		
		return target;
	}
	
}
