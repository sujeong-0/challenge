package com.frankit.challenge.config;

import com.frankit.challenge.product.service.ProductService;
import com.frankit.challenge.security.CustomPermissionEvaluator;
import com.frankit.challenge.security.JwtAuthenticationFilter;
import com.frankit.challenge.security.JwtTokenProvider;
import com.frankit.challenge.user.entity.User;
import com.frankit.challenge.user.repository.UserRepository;
import com.frankit.challenge.user.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * description    :
 * packageName    : com.frankit.challenge.config
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/7/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/7/25        ggong       최초 생성
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
		http.csrf(csrf -> csrf.disable())
		    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		    .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/login")
		                                       .permitAll()
		                                       .requestMatchers("/", "/index.html", "/index.js", "/favicon.ico", "/static/**")
		                                       .permitAll()
		                                       .requestMatchers(HttpMethod.GET, "/product")
		                                       .permitAll()
		                                       .requestMatchers(HttpMethod.GET, "/product/{productId}")
		                                       .permitAll()
		                                       .anyRequest()
		                                       .authenticated())

		    .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
			    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			    response.getWriter().write("Access Denied: You do not have permission to access this resource.");
		    }))
		    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository) {
		return username -> {
			User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다. user email ["+username +"]"));
			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
			                                                              List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService, AuthService authService) {
		return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService, authService);
	}


	@Bean
	public DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler(AuthService authService, ProductService productService) {
		DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
		handler.setPermissionEvaluator(new CustomPermissionEvaluator(authService, productService));
		return handler;
	}
}
