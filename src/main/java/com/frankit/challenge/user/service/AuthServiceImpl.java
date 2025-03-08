package com.frankit.challenge.user.service;

import com.frankit.challenge.security.JwtTokenProvider;
import com.frankit.challenge.user.dto.LoginRequest;
import com.frankit.challenge.user.entity.User;
import com.frankit.challenge.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description    :
 * packageName    : com.frankit.challenge.user.service
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/7/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/7/25        ggong       최초 생성
 */
@Service
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;

	private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

	public AuthServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.jwtTokenProvider = jwtTokenProvider;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public String login(LoginRequest request) {
		Optional<User> user = userRepository.findByEmail(request.getEmail());

		if(user.isEmpty() || !passwordEncoder.matches(request.getPassword(), user.get().getPassword()) || user.get().getRole() != request.getRole()) {
			throw new UsernameNotFoundException("존재하지 않거나, 비밀번호가 틀렸습니다. user email ["+request.getEmail() +"]");
		}
		return jwtTokenProvider.generateToken(request.getEmail());
	}

	@Override
	public boolean isUser(String userEmail) {
		Optional<User> user = userRepository.findByEmail(userEmail);
		return user.isPresent();
	}

	@Override
	public User findByEmail(String userEmail) {
		Optional<User> user = userRepository.findByEmail(userEmail);

		if(user.isEmpty()) {
			throw new UsernameNotFoundException("존재하지 않는 유저입니다. user email ["+userEmail +"]");
		}
		return user.get();
	}


	@Override
	public void logout(String token) {
//		token = token.substring(7);
		long expirationTime = jwtTokenProvider.getRemainingExpirationTime(token);
		blacklist.put(token, System.currentTimeMillis() + expirationTime);
	}

	@Override
	public boolean isLoggedOut(String token) {
		Long expiry = blacklist.get(token);
		if (expiry != null && expiry < System.currentTimeMillis()) {
			blacklist.remove(token);  // 만료된 토큰이면 삭제
			return false;
		}
		return expiry != null;
	}
}
