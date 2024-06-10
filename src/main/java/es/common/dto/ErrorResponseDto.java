package es.common.dto;

import java.time.ZonedDateTime;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *  Representation of an error response
 *
 * @author diego cortavitarte
 * @version 202202
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

	/**
	 * Timestamp
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime timestamp;
	
	/**
	 *  HTTP status code
	 */
	private int code;

	/**
	 *  Internal status code
	 */
	private String internalCode;
	
    /**
     * Exception name, if exists
     */
	private String exception;

    /**
     * The error messages
     */
	private String message;

    /**
     * A collection with the validation errors, if exists
     */
	private Collection<String> validationErrors;

	@Override
	public String toString() {
		
		return super.toString();
	}

	
	
}
