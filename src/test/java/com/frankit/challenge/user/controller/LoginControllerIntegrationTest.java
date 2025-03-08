package com.frankit.challenge.user.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.frankit.challenge.user.dto.LoginRequest;
import com.frankit.challenge.user.entity.User;
import com.frankit.challenge.user.enums.UserRole;
import com.frankit.challenge.user.repository.UserRepository;
import com.frankit.challenge.user.service.AuthService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * description    : 로그인 서비스 통합 테스트
 * packageName    : com.frankit.challenge.user.controller
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/7/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/7/25        ggong       최초 생성
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class LoginControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthService authService;

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	private User loginTestUser;

	@BeforeAll
	void setTestUser() {
		loginTestUser = userRepository.save(new User(null, "test@user.com", passwordEncoder.encode("password"), UserRole.USER));
	}

	@AfterEach
	void deleteUser() {
		userRepository.deleteById(loginTestUser.getId());
	}


	@Test
	@DisplayName("로그인 성공 테스트")
	void testLoginSuccess() throws Exception {
		// Given
		LoginRequest request = new LoginRequest("test@user.com" ,"password", UserRole.USER );

		// When & Then
		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.accessToken").exists());
	}

	@Test
	@DisplayName("로그인 실패 테스트: 비밀번호 틀림")
	void testLoginFailByPassword() throws Exception {
		// Given
		LoginRequest request = new LoginRequest("test@email.com" ,"1" ,UserRole.USER);

		// When & Then
		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
		       .andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("로그인 실패 테스트: 이메일 틀림")
	void testLoginFailByEmail() throws Exception {
		// Given
		LoginRequest request = new LoginRequest("fail@email.com" ,"1234" ,UserRole.USER);

		// When & Then
		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
		       .andExpect(status().isUnauthorized());
	}
	@Test
	@DisplayName("로그아웃 성공")
	void 로그아웃_성공() throws Exception {

		// Given
		LoginRequest request = new LoginRequest("test@user.com" ,"password", UserRole.USER );

		MvcResult mvcResult = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
		                             .andExpect(status().isOk())
		                             .andExpect(jsonPath("$.accessToken").exists())
		                             .andReturn();

		// JSON에서 토큰 추출
		String responseBody = mvcResult.getResponse().getContentAsString();
		String token = objectMapper.readTree(responseBody).get("accessToken").asText();

		// When & Then
		mockMvc.perform(post("/auth/logout")
				                .header("Authorization", "Bearer " + token)
				                .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk());

		// 로그아웃 후 다시 요청하면 401 반환되어야 함
		mockMvc.perform(get("/product")
				                .header("Authorization", "Bearer " + token)
				                .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("로그아웃 후 동일 토큰 사용 불가")
	void 로그아웃후_토큰사용불가() throws Exception {

		// Given
		LoginRequest request = new LoginRequest("test@user.com" ,"password", UserRole.USER );

		MvcResult mvcResult = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
		                             .andExpect(status().isOk())
		                             .andExpect(jsonPath("$.accessToken").exists())
		                             .andReturn();

		// JSON에서 토큰 추출
		String responseBody = mvcResult.getResponse().getContentAsString();
		String token = objectMapper.readTree(responseBody).get("accessToken").asText();
		authService.logout(token); // 60초 만료

		// 블랙리스트 확인
		assertThat(authService.isLoggedOut(token)).isTrue();

		// 블랙리스트된 토큰으로 요청하면 401 반환
		mockMvc.perform(get("/product")
				                .header("Authorization", "Bearer " + token)
				                .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isUnauthorized());
	}
}