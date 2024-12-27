package com.gl.ceir.panel.security.jwt;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gl.ceir.panel.dto.response.JwtData;
import com.gl.ceir.panel.security.jwt.service.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

@Component
@Log4j2
@RequiredArgsConstructor
public class JwtUtils {
	@Value("${eirs.app.jwt.secret}")
	private String jwtSecret;
	@Value("${eirs.app.jwt.expiration.ms}")
	private int jwtExpirationMs;
	private final ObjectMapper objectMapper;
	private final ExpiringMap<String, String> session;

	public String generateJwtToken(Authentication authentication) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(key(), SignatureAlgorithm.HS256).compact();
	}

	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			JwtData jwtData = objectMapper.readValue(
					objectMapper.writeValueAsString(Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken).getBody()),JwtData.class);
			Instant current = Instant.ofEpochSecond(Instant.now().getEpochSecond());
	        Instant exp = Instant.ofEpochSecond(jwtData.getExp());
	        Duration duration = Duration.between(current, exp);
	        long differenceInSeconds = duration.getSeconds();
	        if (session.containsKey(authToken)==false) {
				session.put(authToken, jwtData.getSub(), ExpirationPolicy.ACCESSED, differenceInSeconds + 2, TimeUnit.SECONDS);
			}
			return true;
		} catch(SignatureException e) {
			
		} catch (JsonProcessingException e) {
			
		} catch (MalformedJwtException e) {
			log.info("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			session.remove(authToken);
			log.info("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.info("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
