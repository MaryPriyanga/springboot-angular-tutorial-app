package com.snipe.learning.utility;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.snipe.learning.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final String SECRET = "K7O6vTSPFL+f7F1utAXa1OMllpCEy0LfO5m9BIsnNfU=";
	Instant now = Instant.now();
	Date expiryDate = Date.from(now.plus(Duration.ofHours(24)));

	private final SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));

	public String generateToken(User user) {
		return Jwts.builder()
			.setSubject(user.getEmail())
			.claim("userId", user.getId())
			.claim("role", user.getRole())
			.claim("status", user.getStatus().toString())
			.setIssuedAt(new Date())
			.setExpiration(expiryDate)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	public String extractEmail(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey).build()
			.parseClaimsJws(token)
			.getBody().getSubject();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		String email = extractEmail(token);
		return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		Date expiration = Jwts.parserBuilder()
			.setSigningKey(secretKey).build()
			.parseClaimsJws(token).getBody()
			.getExpiration();
		return expiration.before(new Date());
	}
}
