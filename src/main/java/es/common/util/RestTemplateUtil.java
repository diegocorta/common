package es.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.github.javaparser.quality.Preconditions;

import jakarta.annotation.Nullable;

/**
 * Assembler to be used for making HTTP calls using Rest Template utility
 *
 * @author SOINCON S.L.
 * @version 202205
 */
public final class RestTemplateUtil {

	// #############
	// # Constants #
	// #############

	/**
	 * Default logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateUtil.class);

	/**
	 * Check messages
	 */
	private static final String CHECK_URL_MESSAGE = "URL must not be empty";
	private static final String CHECK_BODY_MESSAGE = "Body must not be null";

	// ################
	// # Constructors #
	// ################

	/**
	 * Default private constructor
	 */
	private RestTemplateUtil() {

		throw new IllegalStateException("Utility class");

	}

	// ##################
	// # Public methods #
	// ##################

	/**
	 * Sends a GET HTTP request to the specified URL and returns the response
	 * 
	 * @param <T>          ResponseEntity type
	 * @param url          URL to send the request
	 * @param responseType ResponseEntity type
	 * @param headers      headers for the request
	 * @return ResponseEntity<T>
	 * @throws Exception
	 */
	public static <T> ResponseEntity<T> get(RestTemplate restTemplate, String url,
			ParameterizedTypeReference<T> type,
			HttpHeaders headers) {

		// Preconditions
		Preconditions.checkArgument(url != null && !url.isEmpty(), CHECK_URL_MESSAGE);

		ResponseEntity<T> response = null;

		HttpEntity<?> request = new HttpEntity<>(headers);

		try {

			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

			response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, type);

		} catch (HttpClientErrorException e) {

			LOGGER.info("Client error on GET: {}. Error code: {}", url, e.getStatusCode().value());
			throw e;

		} catch (HttpServerErrorException e) {

			LOGGER.info("Server error on GET: {}. Error code: {}" + "\nWith error: {}", url, e.getStatusCode().value(),
					e.getMessage());
			throw e;
			
		} catch (Exception e) {

			LOGGER.error("Error found executing GET petition", e);
			throw new RuntimeException(e);		
		}

		return response;

	}

	/**
	 * Sends a POST HTTP request to the specified URL and returns the response
	 * 
	 * @param <T>          ResponseEntity type
	 * @param url          URL to send the request
	 * @param body         body for the POST request
	 * @param responseType ResponseEntity type
	 * @param headers      headers for the request
	 * @return ResponseEntity<T>
	 */
	public static <T> ResponseEntity<T> post(RestTemplate restTemplate, String url,
			Object body, 
			ParameterizedTypeReference<T> type,
			HttpHeaders headers) {

		// Preconditions
		Preconditions.checkArgument(url != null && !url.isEmpty(), CHECK_URL_MESSAGE);
		Preconditions.checkNotNull(body, CHECK_BODY_MESSAGE);

		ResponseEntity<T> response = null;

		HttpEntity<?> request = new HttpEntity<>(body, headers);

		try {

			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

			response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, request, type);

			
		} catch (HttpClientErrorException e) {

			LOGGER.info("Client error on POST: {}. Error code: {}", url, e.getStatusCode().value());
			LOGGER.info("BODY: {}", e.getMessage());
			throw e;

		} catch (HttpServerErrorException e) {

			LOGGER.info("Server error on POST: {}. Error code: {}" + "\nWith error: {}", url, e.getStatusCode().value(),
					e.getMessage());
			throw e;
			
		} catch (Exception e) {

			LOGGER.error("Error found executing POST petition", e);
			throw new RuntimeException(e);
			
		}

		return response;

	}

	/**
	 * Sends a PUT HTTP request to the specified URL and returns the response
	 * 
	 * @param <T>          ResponseEntity type
	 * @param url          URL to send the request
	 * @param body         body for the PUT request
	 * @param responseType ResponseEntity type
	 * @param headers      headers for the request
	 * @return ResponseEntity<T>
	 */
	public static <T> ResponseEntity<T> put(RestTemplate restTemplate, String url,
			Object body, ParameterizedTypeReference<T> type,
			HttpHeaders headers) {

		// Preconditions
		Preconditions.checkArgument(url != null && !url.isEmpty(), CHECK_URL_MESSAGE);
		Preconditions.checkNotNull(body, CHECK_BODY_MESSAGE);

		ResponseEntity<T> response = null;

		HttpEntity<?> request = new HttpEntity<>(body, headers);

		try {

			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

			response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, request, type);

		} catch (HttpClientErrorException e) {

			LOGGER.info("Client error on PUT: {}. Error code: {}", url, e.getStatusCode().value());
			throw e;
			
		} catch (HttpServerErrorException e) {

			LOGGER.info("Server error on PUT: {}. Error code: {}" + "\nWith error: {}", url, e.getStatusCode().value(),
					e.getMessage());
			throw e;
			
		} catch (Exception e) {

			LOGGER.error("Error found executing PUT petition", e);
			throw new RuntimeException(e);	
			
		}

		return response;

	}

	/**
	 * Sends a DELETE HTTP request to the specified URL and returns the response
	 * 
	 * @param <T>          ResponseEntity type
	 * @param url          URL to send the request
	 * @param responseType ResponseEntity type
	 * @param headers      headers for the request
	 * @return ResponseEntity<T>
	 */
	public static <T> ResponseEntity<T> delete(RestTemplate restTemplate, String url,
			@Nullable Object body,
			ParameterizedTypeReference<T> type,
			HttpHeaders headers) {

		// Preconditions
		Preconditions.checkArgument(url != null && !url.isEmpty(), CHECK_URL_MESSAGE);

		ResponseEntity<T> response = null;

		HttpEntity<?> request = (body == null) 
				? new HttpEntity<>(headers)
				: new HttpEntity<>(body, headers);

		try {

			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

			response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.DELETE, request, type);

		} catch (HttpClientErrorException e) {

			LOGGER.info("Client error on DELETE: {}. Error code: {}", url, e.getStatusCode().value());
			throw e;
			
		} catch (HttpServerErrorException e) {

			LOGGER.info("Server error on DELETE: {}. Error code: {}" + "\nWith error: {}", url, e.getStatusCode().value(),
					e.getMessage());
			throw e;
			
		} catch (Exception e) {

			LOGGER.error("Error found executing DELETE petition", e);
			throw new RuntimeException(e);
		}

		return response;

	}

}