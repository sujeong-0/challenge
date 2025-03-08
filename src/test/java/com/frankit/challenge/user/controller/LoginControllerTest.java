package com.frankit.challenge.user.controller;


import com.frankit.challenge.user.dto.LoginRequest;
import com.frankit.challenge.user.enums.UserRole;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
@ExtendWith(MockitoExtension.class) // ✅ Mockito 확장 사용
class LoginControllerTest {
	private static ValidatorFactory factory;
	private static Validator validator;

	@BeforeEach
	void setup() {
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}


	@Test
	@DisplayName("빈값: 이메일, 비밀번호")
	void testLoginValidationFailureEmailPassword() {
		// given
		LoginRequest invalidRequest = new LoginRequest("", "", UserRole.USER);

		// when
		Set<ConstraintViolation<LoginRequest>> violations = validator.validate(invalidRequest);

		//then
		Assertions.assertThat(violations).isNotEmpty();

		boolean emailError = false;
		boolean passwordError = false;

		for(ConstraintViolation<LoginRequest> violation : violations) {
			if(violation.getPropertyPath().toString().equals("email")) {
				emailError = true;
			}
			if(violation.getPropertyPath().toString().equals("password")) {
				passwordError = true;
			}
		}

		// 이메일과 비밀번호 유효성 검사 오류가 모두 발생했는지 확인
		assertThat(emailError).isTrue();
		assertThat(passwordError).isTrue();
	}
}