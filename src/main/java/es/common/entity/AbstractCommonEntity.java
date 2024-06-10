package es.common.entity;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Abstract domain class with common properties: <br><br>
 *
 * <b>versionLock</b>, field to perform lock optimistic when multiple users are editing the same resource
 * <b>active</b>, to perform soft delete
 * <b>createdAt</b>, to save the datetime of creation
 * <b>ModifiedAt</b>, to save the datetime of the last modification
 * <b>ModifiedBy</b>, to save the identifier of the last user who modified the resource
 *
 * @param <K> with the main serializable class of the identifier field<br><br>
 *
 * @author diego cortavitarte
 * @version 202401
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractCommonEntity<K extends Serializable>
		implements IdentifiableObject<K> {
	
	// #############
	// # Constants #
	// #############
	
	/**
	 * Default version lock (for optimistic locking) for new entities 
	 */
	private static final Integer DEFAULT_VERSION_LOCK = 1;
	
	/**
	 * Default active property for new entities
	 */
	private static final Boolean DEFAULT_ACTIVE = Boolean.TRUE;
	
	/**
	 * Default modifiedBy property for new entities, if needed
	 */
	private static final Long DEFAULT_MODIFIED_BY = 0L;
	
	// ##############
	// # Properties #
	// ##############
	
	/**
	 * Entity database version, for controlling optimistic locking
	 */
	@Version
	@Column(name = "version_lock", nullable = false)
	private Integer versionLock;

	/**
	 * Indicates if the entity was logically deleted, or if it is active
	 */
	@Column(name = "active", nullable = false)
	private Boolean active;
	
	/**
	 * Date when entity was created (on UTC)
	 */
	@Column(name = "created_at", nullable = false)
	private ZonedDateTime createdAt;

	/**
	 * Date when entity was last updated (on UTC)
	 */
	@Column(name = "modified_at", nullable = false)
	private ZonedDateTime modifiedAt;

	/**
	 * User id which made the last update
	 */
	@Column(name = "modified_by", nullable = false)
	private Long modifiedBy;
	
	
	// ##################
	// # Public methods #
	// ##################
	
	/**
	 * Execute before persisting new element on database.
	 * Sets active and versionLock to default and created and modifiedAt are defined
	 * as current time on UTC
	 * 
	 * Checks if modifiedBy is present, otherwise the default value is used
	 */
	@PrePersist
    private void onPrePersist() {
        
		this.active = DEFAULT_ACTIVE;
		this.versionLock = DEFAULT_VERSION_LOCK;
		this.createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
		this.modifiedAt = this.createdAt;
		
		if (this.modifiedBy == null)
			this.modifiedBy = DEFAULT_MODIFIED_BY;
    }

	/**
	 * Execute before updating an element on database. 
	 * ModifiedAt is defined as current time on UTC
	 * 
	 * Checks if modifiedBy is present, otherwise the default value is used
	 */
    @PreUpdate
    private void onPreUpdate() {
        
    	this.modifiedAt = ZonedDateTime.now(ZoneId.of("UTC"));
    	
    	if (this.modifiedBy == null)
			this.modifiedBy = DEFAULT_MODIFIED_BY;
    }

}
