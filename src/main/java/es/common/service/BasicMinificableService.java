package es.common.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import es.common.assembler.IAssemblerMinificable;
import es.common.entity.AbstractCommonEntity;
import es.common.util.JoinEntityMap;
import es.common.util.MessageUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.apachecommons.CommonsLog;

/**
 * Abstract service that implements all the basic methods to interact with an entity that implements JPA repository
 * 
 * @param <R> the JPA repository of the entity
 * @param <E> the domain entity
 * @param <K> the identifier of the entity
 * @param <D> the DTO associated with the domain entity
 * @param <A> the assembler used to generate the DTOs
 * @param <M> the minified DTO associated with the domain entity
 */
@Service
@CommonsLog
public abstract class BasicMinificableService<R extends JpaRepository<E, K>, E extends AbstractCommonEntity<?>, K extends Serializable, D, M, A extends IAssemblerMinificable<E, D, M>>
		extends BasicService<R, E, K, D, A>
		implements ICommonMinifiedService<D, K, M> {

	protected final Class<M> minifiedClazz;
	
	public BasicMinificableService(Class<E> entityClazz, Class<M> minifiedClazz, R repository, A assembler) {
		super(entityClazz, repository, assembler);
		this.minifiedClazz = minifiedClazz;
	}


	@Override
	public Collection<EntityModel<M>> findAllMinified() {
		
		return repository.findAll().stream()
			.map(entity -> assembler.buildMinDtoWithLinksFromEntity(entity, minifiedClazz))
			.collect(Collectors.toList());
		
	}

	
	@Override
	public EntityModel<M> findByIdMinified(K id) {
		
		Optional<E> optionEntity = repository.findById(id);
		
		// If entity was found, returns it
		if (optionEntity.isPresent()) {
		
			return assembler.buildMinDtoWithLinksFromEntity(
					optionEntity.get(), minifiedClazz);
		
		// Otherwise, throw an EntityNotFoundException
		} else {
			
			String entityDescription;
				
			try {
				
				entityDescription = (String) entityClazz.getField(DEFAULT_DESCTIPTOR).get(null);
			} catch (Exception e) {
				
				log.warn(String.format(DEFAULT_DESCRIPTOR_TEMPLATE, entityClazz.getCanonicalName()), e);
				entityDescription = entityClazz.getCanonicalName();
			}
			
			throw new EntityNotFoundException(
					MessageUtils.entityNotFoundExceptionMessage(entityDescription));
		}
		
	}

	@Override
	public EntityModel<M> saveMinified(D dto) {
		
		var listDto = List.of(dto);
		
		createDataValidation(listDto);
		
		return transactionTemplate.execute(status -> { 
		
			Map<D, JoinEntityMap> relatedEntitiesMap = getRelatedEntities(listDto);
			
			JoinEntityMap relatedEntities = ( relatedEntitiesMap == null)
					? null
					: relatedEntitiesMap.get(dto);
			
			E entity = assembler.buildEntityFromDto(dto, relatedEntities);
			
			entity = repository.save(entity);
			
			return assembler.buildMinDtoWithLinksFromEntity(entity, minifiedClazz);
			
		});
	}

	@Override
	public EntityModel<M> updateMinified(D dto) {
		
		var listDto = List.of(dto);
		
		basicDataValidation(listDto);
		
		return transactionTemplate.execute(status -> { 
		
			Map<D, JoinEntityMap> relatedEntitiesMap = getRelatedEntities(listDto);
			
			JoinEntityMap relatedEntities = ( relatedEntitiesMap == null)
					? null
					: relatedEntitiesMap.get(dto);
			
			E entity = assembler.buildEntityFromDto(dto, relatedEntities);
			
			copyPreviousDefaultProperties(entity);
			
			entity = repository.saveAndFlush(entity);
					
			return assembler.buildMinDtoWithLinksFromEntity(entity, minifiedClazz);
			
		});
	}

	@Override
	public Collection<EntityModel<M>> saveAllMinified(Collection<D> dtos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<EntityModel<M>> updateAllMinified(Collection<D> dtos) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
