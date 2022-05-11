package com.example.mkksecurity.security;

import com.example.mkksecurity.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {


	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${mkk.app.jwtSecret}")
	private String jwtSecret;

	@Value("${mkk.app.jwtExpritatiomMs}")
	private int jwtExprationMs;

	public String generateJwtToken(Authentication authentication){

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date().getTime() + jwtExprationMs)))
				.signWith(SignatureAlgorithm.HS512,jwtSecret)
				.compact();

	}

	public String generateJwtTokenWithUsername(String username){

		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date().getTime() + jwtExprationMs)))
				.signWith(SignatureAlgorithm.HS512,jwtSecret)
				.compact();

	}


	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {

		try {

			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		}
		catch (SignatureException signatureException) {
			logger.error("Invalid JWT Signature {}",signatureException.getMessage());
		}
		catch (MalformedJwtException e) {
			logger.error("Invalid JWT token {} ",e.getMessage());
		}
		catch (ExpiredJwtException e) {
			logger.error("Jwt token is expired : {}",e.getMessage());
		}
		catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}",e.getMessage());
		}
		catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty {}",e.getMessage());
		}

		return false;

	}





}
