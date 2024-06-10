package es.common.util;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public class TokenUtils {

	public static final String HEADER = "Authorization";
	public static final String PREFIX = "Bearer ";

	public final static String SECURITY_USER = "userId";
	public final static String ROLES = "roles";
	public final static String PERMISSIONS_GRANTED = "permissionsGranted";
	public final static String PERMISSIONS_REVOKED = "permissionsRevoked";
	
	/**
	 * Receive a token and a public key and return the claims of the token
	 * 
	 * @param jwtToken string with the token
	 * @param publicKey public key instance
	 * @throws JwtException if the token validation fails, encapsulating the internal exception
	 * @return
	 */
	public static Claims validateToken(@NotBlank String jwtToken, @NotNull PublicKey publicKey) {

		return validateToken(jwtToken, publicKey, 0L);

	}
	
	public static Claims validateToken(@NotBlank String jwtToken, @NotNull PublicKey publicKey, @PositiveOrZero Long clockSkewSeconds) {
		
		Claims claims = null;
		
		try {
			
			claims = Jwts
					.parser()
					.verifyWith(publicKey)
					.clockSkewSeconds(clockSkewSeconds)
					.build()
					.parseSignedClaims(jwtToken)
					.getPayload();
			
		} catch (SignatureException e) {
			log.info("Invalid JWT signature: " + e.getMessage());
			throw new JwtException("Token cant be verified against the signature", e);
		
		} catch (MalformedJwtException e) {
			log.info("Invalid JWT token: " + e.getMessage());
			throw new JwtException("Token is not well formatted", e);
		
		} catch (ExpiredJwtException e) {
			log.info("JWT token is expired: " + e.getMessage());
			throw new JwtException("Token has expired", e);
		
		} catch (UnsupportedJwtException e) {
			log.info("JWT token is unsupported: " + e.getMessage());
			throw new JwtException("Token is not valid", e);
		
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty: " + e.getMessage());
			throw new JwtException("Token is not valid", e);
		
		}
		
		return claims;
	}
	
	public static String generateTokenFromUsername(@NotNull String username,
			@NotNull String issuer, @NotNull @Valid ZonedDateTime issuedAt,
			@Valid Long secDuration, @NotNull @Valid Map<String, Object> additionalClaims,
			PrivateKey privateKey) {
	    
		Instant issuedAtInstant = issuedAt.toInstant();
		if (additionalClaims == null) additionalClaims = new HashMap<>();
		
		return Jwts.builder()
				.issuer("PM")
	    		.subject(username)
	    		.notBefore(Date.from(issuedAtInstant))
	    		.issuedAt(Date.from(issuedAtInstant))
	    		.expiration(Date.from(issuedAtInstant.plusSeconds(secDuration)))
	    		.claims().add(additionalClaims)
	    		.and()
	    		.signWith(privateKey)
	    		.compact();
	}
	
	public static String generateTokenFromUsername(@NotNull String username,
			@NotNull String issuer, @NotNull @Valid ZonedDateTime issuedAt,
			@Valid Long secDuration,
			PrivateKey privateKey) {
		
	    return generateTokenFromUsername(username, issuer, issuedAt, 
	    		secDuration, new HashMap<>(), privateKey);
		
	}
}
