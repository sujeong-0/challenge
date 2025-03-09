package com.frankit.challenge.security;

import com.frankit.challenge.user.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * description    : JWT를 이용한 Filter
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final UserDetailsService userDetailsService;
	private final AuthService authService;

	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService, AuthService authService) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.userDetailsService = userDetailsService;
		this.authService = authService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain chain) throws ServletException, IOException {
		String token = resolveToken(request);
		if (token != null && jwtTokenProvider.validateToken(token)) {
			if (authService.isLoggedOut(token)) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("로그아웃된 토큰입니다.");
				return;
			}
			String username = jwtTokenProvider.getUsername(token);
			try {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(token));
			} catch (UsernameNotFoundException e) {
				log.debug("User not found for JWT Token");
			}
		}
		chain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}