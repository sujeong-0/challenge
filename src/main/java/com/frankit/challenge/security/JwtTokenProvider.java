package com.frankit.challenge.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * description    : JWT 토큰을 생성 및 검증하는 클래스
 * packageName    : com.frankit.challenge.security
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/8/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/8/25        ggong       최초 생성
 */
@Component
@Slf4j
public class JwtTokenProvider {
	private SecretKey secretKey;
	private final long expirationMs = 1000 * 60 * 60; // 1시간
	private final UserDetailsService userDetailsService;

	public JwtTokenProvider(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@PostConstruct
	public void init() {
		this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	}

	/**
	 * JWT 토큰 생성
	 */
	public String generateToken(String userEmail) {
		return Jwts.builder()
		           .setSubject(userEmail)
		           .setIssuedAt(new Date())
		           .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
		           .signWith(secretKey, SignatureAlgorithm.HS256)
		           .compact();
	}

	/**
	 * JWT 토큰에서 사용자 이름(이메일) 추출
	 */
	public String getUsername(String token) {
		return parseClaims(token).getSubject();
	}

	/**
	 * 토큰 검증
	 */
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.info("JWT 토큰이 만료되었습니다.");
		} catch (MalformedJwtException e) {
			log.info("JWT 토큰이 잘못되었습니다.");
		} catch (UnsupportedJwtException e) {
			log.warn("JWT 토큰이 지원되지 않습니다.");
		} catch (IllegalArgumentException e) {
			log.warn("JWT 토큰이 비어 있습니다.");
		}
		return false;
	}

	/**
	 * JWT 토큰을 기반으로 Authentication 객체 생성
	 */
	public Authentication getAuthentication(String token) {
		String username = getUsername(token);
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	/**
	 * JWT 토큰에서 Claims 파싱
	 */
	private Claims parseClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build()
		           .parseClaimsJws(token)
		           .getBody();
	}

	public long getRemainingExpirationTime(String token) {
		Claims claims = Jwts.parserBuilder()
		                    .setSigningKey(secretKey)
		                    .build()
		                    .parseClaimsJws(token)
		                    .getBody();

		long expirationMillis = claims.getExpiration().getTime(); // 토큰 만료 시간 (밀리초)
		long currentMillis = System.currentTimeMillis(); // 현재 시간 (밀리초)

		return Math.max(0, expirationMillis - currentMillis); // 만료까지 남은 시간 반환 (0 이하 방지)
	}
}
