package es.common.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import es.common.assembler.IAssembler;
import es.common.entity.AbstractCommonEntity;
import es.common.entity.IdentifiableObject;
import es.common.util.JoinEntityMap;
import es.common.util.MessageUtils;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;

/**
 * Abstract service that implements all the basic methods to interact with an entity that implements JPA repository
 * 
 * @param <R> the JPA repository of the entity
 * @param <E> the domain entity
 * @param <K> the identifier of the entity
 * @param <D> the DTO associated with the domain entity
 * @param <A> the assembler used to generate the DTOs
 */
@Data
@Service
@CommonsLog
public abstract class BasicService<R extends JpaRepository<E, K>, E, K extends Serializable, D, A extends IAssembler<E, D>>
		implements ICommonService<D, K> {

	protected final Class<E> entityClazz;
	protected final R repository;
	protected final A assembler;
	
	@Autowired
	protected PlatformTransactionManager transactionManager;
	
	protected TransactionTemplate transactionTemplate;
	
	/**
	 * Template to build a warning when the default descriptor is not found. <br>
	 * Should be used with String.format() or similar to be builded with the entity class name
	 */
	protected final String DEFAULT_DESCRIPTOR_TEMPLATE = "The 'DEFAULT_DESCRIPTOR' field was not found on the given entity %s";
	
	/**
	 * Default descriptor parameter name. Used to search the class descriptor
	 */
	protected final String DEFAULT_DESCTIPTOR = "DEFAULT_DESCRIPTION";
	
	
	public BasicService(Class<E> entityClazz, R repository, A assembler) {
		super();
		this.entityClazz = entityClazz;
		this.repository = repository;
		this.assembler = assembler;
	}
	
	@PostConstruct
	private void postConstruct() {
		transactionTemplate = new TransactionTemplate(transactionManager);
	}
	
	@Override
	public Collection<EntityModel<D>> findAll() {
		
		return transactionTemplate.execute(status -> {
			return assembler.buildDtosWithLinksFromEntities(repository.findAll());
		});
	}
	
	/**
	  * {@inheritDoc}
	  * @throws EntityNotFoundException it the entity is not found
	  */
	@Override
	public EntityModel<D> findById(K id) {
		
		return transactionTemplate.execute(status -> {
			return assembler.buildDtoWithLinksFromEntity(findByIdEntity(id));
		});
	}

	@Override
	public EntityModel<D> save(D dto) {
		
		var listDto = List.of(dto);
		
		createDataValidation(listDto);
		
		return transactionTemplate.execute(status -> { 
			
			Map<D, JoinEntityMap> relatedEntitiesMap = getRelatedEntities(listDto);
			
			JoinEntityMap relatedEntities = ( relatedEntitiesMap == null)
					? null
					: relatedEntitiesMap.get(dto);
			
			E entity = assembler.buildEntityFromDto(dto, relatedEntities);
			
			EntityModel<D> dtoWithLinks = assembler.buildDtoWithLinksFromEntity(repository.save(entity));
			
			return dtoWithLinks;
			
		});
		
	}
	
	@Override
	public EntityModel<D> update(D dto) {
		
		var listDto = List.of(dto);
		
		basicDataValidation(listDto);
		
		return transactionTemplate.execute(status -> {
			
			Map<D, JoinEntityMap> relatedEntitiesMap = getRelatedEntities(listDto);
			
			JoinEntityMap relatedEntities = ( relatedEntitiesMap == null)
					? null
					: relatedEntitiesMap.get(dto);
			
			E entity = assembler.buildEntityFromDto(dto, relatedEntities);
			
			copyPreviousDefaultProperties(entity);
			
			return assembler.buildDtoWithLinksFromEntity(
					repository.saveAndFlush(entity));
		});
		
	}


	@Override
	public Collection<EntityModel<D>> saveAll(Collection<D> dtos) {
		
		createDataValidation(dtos);
		
		return transactionTemplate.execute(status -> {
			
			Map<D, JoinEntityMap> relatedEntitiesMap = getRelatedEntities(dtos);
			
			Map<D, JoinEntityMap> relatedEntitiesMapNoNull = (relatedEntitiesMap == null)
					? new HashMap<>()
					: relatedEntitiesMap;
			
			Collection<E> entities = dtos.stream()
					.map(dto -> assembler.buildEntityFromDto(dto, relatedEntitiesMapNoNull.get(dto)))
					.collect(Collectors.toList());
	            
	    	return repository.saveAll(entities).stream()
					.map(entity -> assembler.buildDtoWithLinksFromEntity(entity))
					.collect(Collectors.toList());
	    	
		});
	}

	
	@Override
	public Collection<EntityModel<D>> updateAll(Collection<D> dtos) {
		
		basicDataValidation(dtos);
		
		return transactionTemplate.execute(status -> {
		
			Map<D, JoinEntityMap> relatedEntitiesMap = getRelatedEntities(dtos);
			
			Map<D, JoinEntityMap> relatedEntitiesMapNoNull = (relatedEntitiesMap == null)
					? new HashMap<>()
					: relatedEntitiesMap;
			
			Collection<E> entities = dtos.stream()
					.map(dto -> assembler.buildEntityFromDto(dto, relatedEntitiesMapNoNull.get(dto)))
					.collect(Collectors.toList());
			
			entities.forEach(entity -> {
				copyPreviousDefaultProperties(entity);
			});
			
			return repository.saveAllAndFlush(entities).stream()
					.map(entity -> assembler.buildDtoWithLinksFromEntity(entity))
					.collect(Collectors.toList());
		});
	}
	
	
	@Override
	public void deleteById(K id) {
		
		repository.deleteById(id);
	}

	
	@Override
	public void deleteByIds(Collection<K> ids) {
		
		repository.deleteAllById(ids);
		
	}
	
	
	private E findByIdEntity(K id) {
		
		return transactionTemplate.execute(status -> {
		
			Optional<E> optionEntity = repository.findById(id);
			
			// If entity was found, returns it
			if (optionEntity.isPresent()) {
			
				return optionEntity.get();
			
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
			
		});
	}
	

	/**
	 * Method that receives the post/put dto and from that, it will obtain all the related entities needed to 
	 * build the domain object 
	 * 
	 * @param dto data transfer object with all data to build a domain object
	 * @return Map with the entities obtained, the key must be the Class<?> of the entity
	 */
	public abstract Map<D, JoinEntityMap> getRelatedEntities(Collection<D> dtos);

	
	/**
	 * Method that receive the data and validates that the information is well formatted.
	 * As it validated the data, it will return the related entities asociated with the received DTO
	 * 
	 * @param dto
	 * 
	 */
	public abstract void basicDataValidation(Collection<D> dtos);
	
	/**
	 * Method that receive the data and validates that the information is well formatted.
	 * As it validated the data, it will return the related entities asociated with the received DTO
	 * 
	 * @param dto
	 * 
	 */
	public abstract void createDataValidation(Collection<D> dtos);
	
	
	protected void copyPreviousDefaultProperties(E entity) {
		
		Class<?> entityClazz = entity.getClass();
		
		if ( AbstractCommonEntity.class.isAssignableFrom(entityClazz) &&
				IdentifiableObject.class.isAssignableFrom(entityClazz)) {
			
			@SuppressWarnings("unchecked")
			IdentifiableObject<K> identifier = (IdentifiableObject<K>) entity;
			
			E prevEntity = findByIdEntity((K) identifier.getId());
			
			assembler.copyCommonEntityDefaultProperties((AbstractCommonEntity<?>) prevEntity, (AbstractCommonEntity<?>) entity);
			
		}
		
	}

}
