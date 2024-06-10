package es.common.util;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatusCode;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import es.common.dto.ErrorResponseDto;

/**
 * Utility class for helping to work with the API HTTP errors 
 * 
 * @author soincon.es
 * @version 202105
 */
public final class ApiErrorUtil {


	// ###############
	// # Constructor #
	// ###############

	/**
	 * Default constructor
	 */
	private ApiErrorUtil() {

		throw new IllegalStateException("Utility class");
	}

	// ##################
	// # Public methods #
	// ##################
	
	/**
	 * Build error message with the given params
	 * 
	 * @param ex the exception throwed
	 * @param status the HTTP status to be applied and linked to the throwed exception
	 * @return the exception message built
	 */
	public static ErrorResponseDto buildErrorMsg(Exception ex, 
			HttpStatusCode httpStatus) {
		
		String errorMessage = ex.getMessage();
		Collection<String> validationErrors = null;
		
		if(ex instanceof BindException) {
			
			validationErrors = ((BindException)ex).getBindingResult()
					.getFieldErrors()
					.stream()
					.map((FieldError fe) -> fe.getObjectName() + " - " + fe.getField() + " " + fe.getDefaultMessage())
					.collect(Collectors.toList());
			
			errorMessage = null;
		}
		
		return new ErrorResponseDto(
				ZonedDateTime.now(),
				httpStatus.value(),
				getInternalErrorCode(ex, httpStatus),
				ex.getClass().getSimpleName(),
				errorMessage,
				validationErrors);
	}

	
	// ###################
	// # Private methods #
	// ###################
	
	/**
	 * Retrieves the internal error code linked to the exception throwed
	 * 
	 * @param ex the exception throwed
	 * @param status the HTTP status to be applied and linked to the throwed exception
	 * @return the internal error code
	 */
	private static String getInternalErrorCode(Exception ex, 
			HttpStatusCode httpStatus) {
		
		return null;
	}

}
