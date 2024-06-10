package es.common.util;

public class MessageUtils {

	/**
	 * Method that returns the message to be build when an entity is not found
	 * 
	 * @param entityDescriptor the descriptor of the entity
	 * @return the message to be shown or logged
	 */
	public static String entityNotFoundExceptionMessage(String entityDescriptor) {
		return String.format("%s entity was not found", entityDescriptor);
	}
	
	/**
	 * Method that returns the message to be build when an entity is not found
	 * 
	 * @param entityDescriptor the descriptor of the entity
	 * @return the message to be shown or logged
	 */
	public static String entityAlrreadyExistsExceptionMessage(String entityDescriptor) {
		return String.format("%s entity alrready exists", entityDescriptor);
	}
	
	/**
	 * Method that returns the message to be build when an entity is not found
	 * 
	 * @param entityDescriptor the descriptor of the entity
	 * @return the message to be shown or logged
	 */
	public static String identifierMustBeNull(String entityDescriptor) {
		return String.format("the identifier of the %s entity must be null", entityDescriptor);
	}
	
	/**
	 * Method that returns the message to be build when an entity is not found
	 * 
	 * @param entityDescriptor the descriptor of the entity
	 * @return the message to be shown or logged
	 */
	public static String identifierMustNotBeNull(String entityDescriptor) {
		return String.format("the identifier of the %s entity must not be null", entityDescriptor);
	}
}
