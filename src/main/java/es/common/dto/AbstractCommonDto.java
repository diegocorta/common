package es.common.dto;

import java.io.Serializable;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Abstract representation of a domain entity for data exchange
 * 
 * @author diego cortavitarte
 * @version 202202
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractCommonDto implements Serializable {

	private static final long serialVersionUID = 5042929607486799190L;

	// ##############
	// # Properties #
	// ##############
	public static final String SELF_REF = "self";
	
	/**
	 * Linked domain entity database version, for controlling optimistic locking
	 */
	@PositiveOrZero
	private Integer versionLock;

	/**
	 * Indicates if it was logically deleted, or if it is active
	 */
	private boolean active;

	/**
	 * Date when entity was created (on UTC)
	 */
	@DateTimeFormat
	private String createdAt;

	/**
	 * Date when entity was last updated (on UTC)
	 */
	@DateTimeFormat
	private String modifiedAt;	

	/**
	 * User identifier which made the last update
	 */
	@PositiveOrZero
	private Long modifiedBy;

}
