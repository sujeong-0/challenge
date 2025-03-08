package com.frankit.challenge.user.dto;

import com.frankit.challenge.user.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * description    : 로그인 요청 DTO
 * packageName    : com.frankit.challenge.user.dto
 * fileName       : IntelliJ IDEA
 * author         : ggong
 * date           : 3/1/25
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/1/25        ggong       최초 생성
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
	/** 이메일 */
	@NotEmpty(message = "이메일은 필수 입력값입니다.")
	@NotBlank(message = "이메일은 필수 입력값입니다.")
	@Email(message = "올바른 이메일 형식을 입력해주세요.")
	private String email;

	/** 비밀번호 */
	@NotEmpty(message = "비밀번호는 필수 입력값입니다.")
	@NotBlank(message = "비밀번호는 필수 입력값입니다.")
	private String password;

	/** 권한 */
	@NotNull(message = "유저 권한은 필수 입력값입니다.")
	private UserRole role;
}
