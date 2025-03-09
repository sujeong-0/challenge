package com.frankit.challenge.user.controller;

import com.frankit.challenge.user.dto.LoginRequest;
import com.frankit.challenge.user.dto.LoginResponse;
import com.frankit.challenge.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * description    : 로그인 컨트롤러 클래스
 * packageName    : com.frankit.challenge.user.controller
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/1/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/1/25        ggong       최초 생성
 */
@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {
	private final AuthService authService;
	private final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

	public LoginController(AuthService authService) {this.authService = authService;}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
		String token = authService.login(request);
		LoginResponse response = new LoginResponse(token, "Bearer", 86400);// todo 만료시간을 이렇게 정의하는지?
		return ResponseEntity.ok(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰");
		}

		authService.logout(authorizationHeader);

		return ResponseEntity.ok("로그아웃 성공");
	}

}
