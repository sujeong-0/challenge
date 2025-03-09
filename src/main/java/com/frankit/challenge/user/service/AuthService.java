package com.frankit.challenge.user.service;

import com.frankit.challenge.user.dto.LoginRequest;
import com.frankit.challenge.user.entity.User;

/**
 * description    : 인증/권한 관련 비즈니스 로직 인터페이스
 * packageName    : com.frankit.challenge.user.service
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/8/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/8/25        ggong       최초 생성
 */
public interface AuthService {
	String login(LoginRequest request);

	boolean isUser(String userEmail);

	User findByEmail(String userEmail);

	void logout(String token);

	boolean isLoggedOut(String token);
}
